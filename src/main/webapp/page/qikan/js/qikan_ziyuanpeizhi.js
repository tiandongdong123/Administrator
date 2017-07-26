$(function() {
	laypage({
		cont : 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
						// id="page1"></div>
		pages : 10, // 通过后台拿到的总页数
		curr : 1, // 当前页
		skip : true, // 是否开启跳页
		skin : 'molv',// 当前页颜色，可16进制
		groups : 6, // 连续显示分页数
		first : '首页', // 若不显示，设置false即可
		last : '尾页', // 若不显示，设置false即可
		prev : '上一页', // 若不显示，设置false即可
		next : '下一页', // 若不显示，设置false即可
		jump : function(obj, first) { // 触发分页后的回调
			if (!first) { // 点击跳页触发函数自身，并传递当前页：obj.curr
				var curr = obj.curr;
			}
		}
	});
	findAllProvider(); // 资源提供商查询

});

// 资源提供商 查询
var providerId = "";
function getproviderId() {
	return providerId;
}
function findAllProvider() {
	$
			.getJSON(
					"../dataSource/findAllProviders.do",
					{},
					function(res) {
						var str = "<dt>资源提供商：</dt>";
						for ( var i = 0; i < res[0].size; i++) {
							if (i == 0) {
								str += "<th>"
										+ "<button name='butt' id=button_"
										+ res[i].id
										+ " type='button' class='btn btn-primary btn-sm' style='padding: 6px 35px;' onclick='findResourceNamesById(this.value,null)' checked='checked' value="
										+ res[i].id + ">" + res[i].nameZh
										+ "</button>";
								str += "<button type='button' style='margin-right: 2px;vertical-align: top;' onclick='tianjiaProvider("
										+ res[i].id
										+ ")'><i class='fa fa-edit'></i></button>";
								str += "<button type='button' style='margin-right: 5px;vertical-align: top;' onclick='shanchuProvider("
										+ res[i].id
										+ ")'><i class='fa fa-trash-o'></i></button></th>";
								providerId = res[i].id;
							} else {
								str += "<th>"
										+ "<button name='butt' id=button_"
										+ res[i].id
										+ " type='button'  class='btn btn-primary btn-sm' style='padding: 6px 35px;' onclick='findResourceNamesById(this.value,null)' value="
										+ res[i].id + ">" + res[i].nameZh
										+ "</button>";
								str += "<button type='button' style='margin-right: 2px;vertical-align: top;' onclick='tianjiaProvider("
										+ res[i].id
										+ ")'><i class='fa fa-edit'></i></button>";
								str += "<button type='button' style='margin-right: 5px;vertical-align: top;' onclick='shanchuProvider("
										+ res[i].id
										+ ")'><i class='fa fa-trash-o' ></i></button></th>";

							}
						}
						document.getElementById('providerHtml').innerHTML = str;
						findResourceNamesById(providerId, null);// 查询资源类型 新增加方法
					});
}
// 资源提供商 查询 结束
// 删除供应商
function shanchuProvider(providId) {
	if (confirm("是否确认删除！！")) {
		$.ajax({
			type : "post",
			data : {
				id : providId
			},
			url : "../dataSource/deleteProvider.do",
			dataType : "json",
			async : false,
			success : function(res) {
				if (res == true) {
					// alert("删除成功");
					layer.msg('删除成功！');
				} else {
					layer.msg('有其他关联，删除失败！');
					// alert("删除失败");
				}
			}
		});
		findAllProvider();
	}

}
/** 查询资源类型 新增加的方法 * */
var resourceTypeIds;// 资源类型id
function getresourceTypeIds() {
	return resourceTypeIds;
}
function findResourceNamesById(providerIdx, reId) {

	var check = document.getElementsByName("butt");// 遍历把颜色换成默认
	var len = check.length;
	for ( var i = 0; i < len; i++) {
		$(check[i]).attr("class", "btn btn-primary btn-sm");
	}
	$("#button_" + providerIdx).attr("class", "btn bg-olive btn-sm");// 更换颜色
	resourceTypeIds = reId;
	var restr = "<tr><th style='text-align: center;'>#</th><th>资源类型</th></tr>";
	providerIds = providerIdx;
	$
			.ajax({
				type : "post",
				data : {
					providerId : providerIds
				},
				url : "../dataSource/findResourceNamesById.do",
				dataType : "json",
				async : false,
				success : function(res) {
					if (res != null) {
						for ( var i = 0; i < res.length; i++) {
							if (resourceTypeIds == null) {// 进来第一次进行赋值，不会重复赋值
															// id为全局变量时使用
								resourceTypeIds = res[i].id;
							}
							restr += "<tr ><td style='text-align: center;'>"
									+ (i + 1)
									+ "</td><td><a href='javascript:void(0)' name='abq' id=restype"
									+ res[i].id
									+ " onclick='findResourceNamesById(providerIds,"
									+ res[i].id + ")'>" + res[i].resourceName
									+ "</a></td>";
							restr += "<td ><button style='width: 50px;' type='button' onclick='tianjiaProResourceType("
									+ res[i].id
									+ ")' class='btn  btn-default btn-xs'>修改</button>&nbsp;&nbsp;";
							restr += "<button style='width: 50px;' type='button' onclick='shanchuProResourceType("
									+ res[i].id
									+ ")' class='btn  btn-default btn-xs'>删除</button></td></tr>";
						}
					} else {
						restr += "<div align='center'><span style='font-size: 20px;'>数据为空!</span></div>";
					}

					getSubject(providerIds, reId); // 查询当前提供商 学科分类

				}
			});
	document.getElementById('resourceName').innerHTML = restr;
}

/** 删除资源类型 * */
function shanchuProResourceType(resourceId) {
	if (confirm("是否确认删除！！")) {
		$.ajax({
			type : "post",
			data : {
				id : resourceId
			},
			url : "../dataSource/deleteProResourceType.do",
			dataType : "json",
			async : false,
			success : function(res) {
				if (res == true) {
					// alert("删除成功");
					layer.msg('删除成功！');
				} else {
					// alert("删除失败");
					layer.msg('有其他关联，删除失败！');
				}
			}
		});
		findResourceNamesById(providerId);
	}
}
/** 查询资源类型 结束 * */

/** Ztree 学科分类 * */
var classid;
var classname;
function getClassid() {
	return classid;
}
function getSubject(providerIds, resourceTypeIdx) {
	classid = "";
	classname = "";
	providerId = providerIds;
	findMatrixLiteratureList(1);
	$.ajax({
		type : "post",
		data : {
			providerId : providerId,
			proResourceId : resourceTypeIdx,
		},
		url : "../dataSource/findAllPSubjectCategory.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {
		},
		success : drawTree,
	});
}
function drawTree(json) {
	var setting = {
		view : {
			dblClickExpand : false,
		},
		data : {
			simpleData : {
				enable : true,
			}
		},
		check : {
			enable : false
		},
		callback : {
			onClick : onClick
		},
	};
	var zNodes = eval(json);
	$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(true);
	zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.expandNode(zTree.getNodes()[0], true);
}
function onClick(event, treeId, treeNode) {
	classid = treeNode.id; // 节点id
	classname = treeNode.name;
	findMatrixLiteratureList(1);
}
/** Ztree 学科分类 结束* */

/** 删除 学科分类 * */
function shanchuxueke() {
	if (confirm("是否确认删除！！")) {
		$.ajax({
			type : "post",
			data : {
				id : classid
			},
			url : "../dataSource/deletePSubjectCategory.do",
			dataType : "json",
			async : false,
			success : function(res) {
				if (res == true) {
					// alert("删除成功");
					layer.msg('删除成功！');
				} else {
					layer.msg('有其他关联， 删除失败！');
					// alert("删除失败");
				}
			}
		});
		getSubject(providerIds);
	}
}
// 母体文献 查询
var curr;
var mids;
var idss;// 母体 Id
function findMatrixLiteratureList(curr) {
	$("#mutiwenxian").html("");
	resourceTypeId = resourceTypeIds;
	$
			.ajax({
				type : "post",
				url : "../dataSource/getMatrixLiteratureLists.do",
				data : {
					providerId : providerId,
					psubjectId : classid,
					proResourceId : resourceTypeId,
					pagesize : 10,
					pagenum : curr || 1,
				},
				dataType : "json",
				async : false,
				success : function(res) {
					var str = "";
					var totalRow;
					var pageSize;
					var pages;
					var groups;
					idss = "";
					if (res[0] != null) {
						totalRow = res[0].pageList.pageTotal;
						pageSize = res[0].pageList.pageSize;
						for ( var i = 0; i < res[0].listSize; i++) {
							idss = res[i].id;
							str += "<tr class='odd' role='row'>";
							str += "<td class='sorting_1'>" + (i + 1) + "</td>";// 母体id
							str += "<td>" + res[i].title + "</td>";// 母体标题
							str += "<td>" + res[i].nameen + "</td>";// 母体英文名
							str += "<td>" + res[i].author + "</td>";// 母体作者
							str += "<td>" + res[i].categoryName + "</td>";// 学科名称
							str += "<td><button type='button' onclick=tianjiamuti('"
									+ res[i].id
									+ "') class='btn btn-default btn-xs'>修改</button>&nbsp;&nbsp;";
							str += "<button type='button' onclick=shanchumuti('"
									+ res[i].id
									+ "') class='btn btn-default btn-xs'>删除</button></td></tr>";
							str += "</tr>";
						}
					} else {
						str = "<tr><td colspan='6'><div align='center'><span style='font-size: 20px;'>数据为空!</span></div></td></tr>";
					}
					document.getElementById('mutiwenxian').innerHTML = str;
					// morenCheck();
					// 显示分页
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
										// id="page"></div>
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
								findMatrixLiteratureList(curr);
							}
						}
					});
				}
			});
}
/** *****母体文献查询 结束******** */

/** *****添加供应商******** */
function getgysId() {
	return gysId;
}
var gysId = "";
function tianjiaProvider(pval) {
	gysId = pval;
	layer.open({
		type : 2,
		area : [ '550px', '303px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '资源提供商',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '保存 ', '关闭' ],
		content : [ "../dataSource/qikan-addProvider.do", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.remindSubmit();
		},
		cancel : function(index) {
		}
	});
}
/** *****添加供应商******** */

/** *****修改添加 资源类型******** */
var proId = "";// 向二级页面 传 提供商Id
function getProId() {
	return proId;
}
function tianjiaProResourceType(id) {
	proId = id;
	layer.open({
		type : 2,
		area : [ '800px', '500px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '资源类型',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '保存 ', '关闭' ],
		content : [ "../dataSource/qikan-addProResourceType.do", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.remindSubmit();
		},
		cancel : function(index) {
		}
	});
}

/** *****添加學科類別******** */
function tianjiaxueke(val) {
	if (val == "0") {
		classid = null;
	}
	layer.open({
		type : 2,
		area : [ '800px', '500px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '学科分类',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '保存 ', '关闭' ],
		content : [ "../dataSource/qikan-addPSubjectCategory.do", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.remindSubmit();
		},
		cancel : function(index) {
		}
	});
}
/** *****添加学科分类結束******** */

/** *****修改学科分类******** */
function getClassId() {
	return classid;
}
function updatexueke() {
	if (classid != null && classid != "") {
		tianjiaxueke();
	} else {
		layer.msg("请选择学科分类，再编辑！");
	}

}
/** *****添加母体******** */
// 向二级页面传参 母体id
function getmaId() {
	return maId;
}
var maId = "";
function tianjiamuti(vals) {
	maId = vals;
	layer.open({
		type : 2,
		area : [ '800px', '600px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '母体文献',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '保存 ', '关闭' ],
		content : [ "../dataSource/qikan-addMatrixLiterature.do", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.remindSubmit();
		},
		cancel : function(index) {
		}
	});
}
/** ***删除母体***** */
function shanchumuti(idss) {
	if (confirm("是否确认删除！！")) {
		$.ajax({
			type : "post",
			data : {
				id : idss
			},
			url : "../dataSource/deleteMatrixLiterature.do",
			dataType : "json",
			async : false,
			success : function(res) {
				if (res == true) {
					// alert("删除成功");
					layer.msg('删除成功！');
				} else {
					// alert("删除失败");
					layer.msg('有其他关联，删除失败！');
				}
			}
		});
		findResourceNamesById(providerId);

	}
}