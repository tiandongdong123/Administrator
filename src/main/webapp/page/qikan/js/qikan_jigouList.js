var curr;
$(function() {
	find(curr);
});
/** 授权列表 */
function find(curr) {
	$.ajax({
				type : "post",
				url : "../dataSource/getAuthorizelist.do",
				data : {
					pagenum : curr || 1,// 向服务端传的参数
					pagesize : 10,
					institutionId : $("#inUserId").val(),
				},
				dataType : "json",
				async : false,
				success : function(res) {

					var str = "";
					if (res != "" && res!=null) {
						for ( var i = 0; i < res[0].listSize; i++) {
							str += " <tr role='row'>"
									+ "<td width='6%' align='center'>"
									+ (i + 1)
									+ "</td>"
//									+ "<td>"
//									+ res[i].InstitutionName
//									+ "</td>"
									+ "<td>"
									+ res[i].InstitutionId
									+ "</td>"
									+ "<td>"
									+ res[i].nameZh
									+"/"
									+ res[i].providerName
									+ "</td>"
//									+ "<td>"
//									+ res[i].username
//									+ "</td>"
									+ "<td>"
									+ "<button onclick='deleteAuthorize("
									+ res[i].id
									+ ")' type='button' class='btn btn-primary btn-sm'>删除</button>&nbsp;"
									+ "<button onclick='updateAuthorize("
									+ res[i].id
									+ ")' type='button' class='btn btn-primary btn-sm'>编辑</button>&nbsp;"
									+ "<button onclick=xiangqing('"
									+ res[i].id
//									+ ",'"
//									+ res[i].InstitutionName
									+ "','"
									+ res[i].nameZh
									+ "','"
									+ res[i].providerId
									+ "','"
									+ res[i].InstitutionId
									+ "') type='button' class='btn btn-primary btn-sm'>查看详情</button></td>"
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
									find(curr);
								}
							}
						});
					} else {
						str = "<tr><td colspan='6'><div align='center'><span style='font-size: 20px;'>数据为空!</span></div></td></tr>";
						document.getElementById('textHtml').innerHTML = str;
					}

				}
			});
}
/** 授权列表 结束 */

/** 删除授权表 开始 */
function deleteAuthorize(id) {
	if (confirm("是否确认删除！！")) {
		$.ajax({
			type : "post",
			url : "../dataSource/deleteAuthorize.do",
			data : {
				id : id
			},
			dataType : "json",
			async : false,
			success : function(res) {
				if (res == "0") {
					layer.msg('有关联，不可删除！');
					return;
				} else {
					layer.msg('删除成功！');
					find(1);
				}
			}
		});
	}
}
/** 删除授权表 结束 */

/** 跳转到编辑页面 开始 */
var authorizeId;
function getAuthorizeId() {
	return authorizeId;
}
function updateAuthorize(id) {
	authorizeId = id;
	layer.open({
		type : 2,
		area : [ '500px', '300px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '授权编辑',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '确认', '关闭' ],
		content : [ "../dataSource/qikan-edit-Authorize.do", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.editSubmit();
		},
		cancel : function(index) {
		}
	});
}
/** 跳转到编辑页面 结束 */

/** 详情页跳转开始 */
var authorizeId; // 授权表ID
var institutionName; // 机构名称
var institutionId; // 机构ID
var providerName; // 资源提供商名称
var providerId; // 资源提供商ID

function getAuthorizeId() {
	return authorizeId;
}
function getInstitutionName() {
	return institutionName;
}
function getInstitutionId() {
	return institutionId;
}
function getProviderName() {
	return providerName;
}
function getProviderId() {
	return providerId;
}
function xiangqing(ids, proName, proId, InsId) {
	authorizeId = ids;
//	institutionName = InsName;
	providerName = proName;
	providerId = proId;
	institutionId = InsId;
	layer.open({
		type : 2,
		area : [ '800px', '593px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '详情列表',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '关闭' ],
		content : [ "../dataSource/qikan-jigouList-xiangqing.do", 'no' ],
		cancel : function(index) {
		}
	});
}
/** 详情页跳转 结束 */
