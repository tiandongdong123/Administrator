var authorizeId = window.parent.getAuthorizeId();
//var institutionName = window.parent.getInstitutionName();
var institutionId = window.parent.getInstitutionId();
var providerName = window.parent.getProviderName();
var providerId = window.parent.getProviderId();
$(function() {
	$("#institutionName").html(institutionId);
	$("#providerName").html(providerName);
	findResourceNamesById();
	getAuthorizeRelationList(curr);
});

/** 资源类型下拉框 开始 */
function findResourceNamesById() {
	$.ajax({
		type : "post",
		url : "../dataSource/findResourceNamesById.do",
		data : {
			providerId : providerId,
		},
		dataType : "json",
		async : false,
		success : function(res) {
			$("<option/>").html("全部").val("").appendTo("#resourceType");
			$(res).each(
					function(n) {
						$("<option/>").html(res[n].resourceName).val(res[n].id)
								.appendTo("#resourceType");
					});
		}
	});
}
/** 资源类型下拉框 结束 */
var proResourceId;
function getProResourceId(pId) {
	proResourceId = pId;
	getAuthorizeRelationList(curr);
}
var curr;
function getAuthorizeRelationList(curr) {
	$.ajax({
				type : "post",
				url : "../dataSource/getAuthorizeRelationList.do",
				data : {
					institutionId : institutionId,
					providerId : providerId,
					proResourceId : proResourceId,
					pagenum : curr || 1,// 向服务端传的参数
					pagesize : 10,
					startDate : $("#startDate").val(),
					endDate : $("#endDate").val(),
				},
				dataType : "json",
				async : false,
				success : function(res) {
					var str = "";
					if (res != "") {
						for ( var i = 0; i < res[0].listSize; i++) {
							str += "<tr role='row'>"
									+ "<td class='sorting_1'><input type='checkbox' onclick=fucheck("
									+ res[i].id
									+ ") name='check' onclick=''/>&nbsp;&nbsp;"
									+ (i + 1)
									+ "</td>"
									+ "<td>"
									+ res[i].title
									+ "</td>"
									+ "<td>"
									+ res[i].pCategoryName
									+ "</td>"
									+ "<td>"
									+ res[i].proResourceName
									+ "</td>"
									+ "<td><button onclick='updateAuthorizeRelation("
									+ res[i].id
									+ ")' type='button' class='btn btn-primary btn-sm'>编辑URL</button></td>"
									+ "</tr>";
						}
						document.getElementById('textHtml').innerHTML = str;
						// 显示分页
						var totalRow = res[0].pageList.pageTotal;
						var pageSize = res[0].pageList.pageSize;
						var pages;
						var groups;
						if (totalRow % pageSize == 0) {
							pages = totalRow / pageSize;
						} else {
							pages = totalRow / pageSize + 1;
						}
						if (pages >= 4) {
							groups = 4;
						} else {
							groups = pages;
						}
						laypage({
							cont : 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
											// id="page1"></div>
							pages : pages, // 通过后台拿到的总页数
							curr : curr, // 当前页
							skip : true, // 是否开启跳页
							skin : 'molv',// 当前页颜色，可16进制
							groups : groups, // 连续显示分页数
							first : '首页', // 若不显示，设置false即可
							last : '尾页', // 若不显示，设置false即可
							prev : '上一页', // 若不显示，设置false即可
							next : '下一页', // 若不显示，设置false即可
							jump : function(obj, first) { // 触发分页后的回调
								if (!first) { // 点击跳页触发函数自身，并传递当前页：obj.curr
									curr = obj.curr;
									getAuthorizeRelationList(curr);
								}
							}
						});
					} else {
						str = "<tr><td colspan='6'><div align='center'><span style='font-size: 20px;'>数据为空!</span></div></td></tr>";
						document.getElementById('textHtml').innerHTML = str;
						laypage({
							cont : 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
											// id="page1"></div>
							pages : pages, // 通过后台拿到的总页数
							curr : curr, // 当前页
							skip : true, // 是否开启跳页
							skin : 'molv',// 当前页颜色，可16进制
							groups : groups, // 连续显示分页数
							first : '首页', // 若不显示，设置false即可
							last : '尾页', // 若不显示，设置false即可
							prev : '上一页', // 若不显示，设置false即可
							next : '下一页', // 若不显示，设置false即可
							jump : function(obj, first) { // 触发分页后的回调
								if (!first) { // 点击跳页触发函数自身，并传递当前页：obj.curr
									curr = obj.curr;
									getAuthorizeRelationList(curr);
								}
							}
						});
					}
				}
			});
}
// 复选框 获取选中id
var ARIds = "";
function fucheck(id) {
	ARIds += id + ",";
	if (ARIds != "") {
		var aridss = ARIds.substring(0, ARIds.length - 1);
		var ids = aridss.split(",");
		var ARId = "";
		for ( var i = 0; i < ids.length; i++) {
			var n = (ARIds.split(ids[i])).length - 1;
			if (n % 2 == 0) {
				ids[i] = null;
			} else {
				ARId += ids[i] + ",";
			}
		}
		ARIds = ARId;
	}
	aridss = ARIds.substring(0, ARIds.length - 1);
	var numFu = 0;
	if (ss != "") {
		var ss = aridss.split(",");
		for ( var i = 0; i < ss.length; i++) {
			if (ss[i] != "" && ss[i] != null) {
				numFu = numFu + 1;
			}
		}
	}
	$("#numFu").html(numFu);
}
// 删除
function deleteAuthorizeRelation() {

	if (ARIds != "") {
		if (confirm("是否确认删除！！")) {
			$.ajax({
				type : "post",
				url : "../dataSource/deleteAuthorizeRelation.do",
				data : {
					ids : ARIds
				},
				dataType : "json",
				async : false,
				success : function(res) {
					if (res == "0") {
						layer.msg('删除失败');
						return;
					} else {
						layer.msg('共删除 ' + res + ' 数据。。');
						return;
					}
				}
			});
		}
	} else {
		layer.msg("请选择后再删除！");
	}
}
var authorizeRelationId;
function getAuthorizeRelationId() {
	return authorizeRelationId;
}
function updateAuthorizeRelation(id) {
	authorizeRelationId = id;
	parent.layer.open({
		type : 2,
		area : [ '500px', '300px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '编辑RUL',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '确认', '关闭' ],
		content : [
				"../dataSource/qikan-edit-AuthorizeRelation.do?winName="
						+ window.name, 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.editSubmit();
		},
		cancel : function(index) {
		}
	});
}