var classid = window.parent.getClassId();
var providerId = window.parent.getproviderId();// 提供商 id
var resourceTypeId = window.parent.getresourceTypeIds();// 资源类型id

$(function() {
	findAllProvider(); // 资源提供商查询
	if (classid != null && classid != "") {
		getPSubjectCategory();
	}
});
/** ****根据id查学科*** */
function getPSubjectCategory() {
	$.getJSON("../dataSource/getPSubjectCategoryById.do", {
		classid : classid,
	}, function(res) {
		selected("provider", res.providerId);
		selected("resourceName", res.proResourceId);
		$("#parentId").val(res.parentId);
		$("#parentName").val(res.parentName), $("#pCategoryName").val(
				res.pCategoryName);
		$("#pCategoryCodes").val(res.pCategoryCodes);
	});
}
function selected(selectname, selectid) {
	$("#" + selectname + " option").each(function() { // 遍历全部option
		var v = $(this).val(); // 获取option的内容
		if (v == selectid) {
			$(this).attr("selected", "selected");
		}
	});
}

/** ****资源提供商 查询*** */
function findAllProvider() {
	$.getJSON("../dataSource/findAllProviders.do", {}, function(res) {
		$(res).each(function(n) {

					$("<option/>").html(res[n].nameZh).val(res[n].id).appendTo("#provider");
				});
		selected("provider", providerId);
		findResourceNamesById(); // 资源类型查询
	});
}
/** 查询资源类型  * */
function findResourceNamesById() {
	$("#resourceName").html("");
	$.ajax({
		type : "post",
		data : {
			"providerId" : $("#provider").val()
		},
		url : "../dataSource/findResourceNamesById.do",
		dataType : "json",
		success : function(res) {
			$(res).each(
					function(s) {
						$("<option />").html(res[s].resourceName)
								.val(res[s].id).appendTo("#resourceName");
					});
			selected("resourceName", resourceTypeId);// 默认选中资源类型
		}
	});
}
/** ****选择学科*** */
function xuanzexueke() {
	parent.layer.open({
		type : 2,
		area : [ '700px', '400px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '学科分类',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '確定 ', '关闭' ],
		content : [
				"../dataSource/qikan-fuleixueke.do?winName=" + window.name
						+ "&numType=0", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.queren();
		},
		cancel : function(index) {
		}
	});
}
function xueke() {
	$("#parentName").val("");
}
function getprociderId() {
	return $("#provider").val();
}
function getProResourceId() {
	return $("#resourceName").val();
}
/** *资源提供商 查询 结束** */

/** *增加学科分类** */
function remindSubmit() {
	if ($.trim($("#pCategoryName").val()).length < 1) {
		parent.layer.msg('数据名称不能为空', {icon:1});
		return false;
	}
	if ($.trim($("#pCategoryCodes").val()).length < 1) {
		parent.layer.msg('数据库编码不能为空', {icon:1});
		return false;
	}
	
	var reg=new RegExp("^[A-Za-z]+$"); 
	
	if (!reg.test($.trim($("#pCategoryCodes").val()))) {
		parent.layer.msg('数据库编码格式不正确', {icon:1});
		return false;
	}
	
	$.ajax({
		type : "post",
		url : "../dataSource/addPSubjectCategory.do",
		data : {
			"providerId" : $("#provider").val(),
			"proResourceId" : $("#resourceName").val(),
			"pCategoryName" : $("#pCategoryName").val(),
			"pCategoryCodes" : $("#pCategoryCodes").val(),
			"parentId" : $("#parentId").val(),
			"id" : classid,
		},
		success : function(data) {
			if (data > 0) {
				var index = parent.layer.getFrameIndex(window.name);
				window.parent.getSubject($("#provider").val());
				parent.layer.msg('提交成功', {
					icon : 1
				});
				parent.layer.close(index);
			}
		},
		complete : function() {
			$("#parentName").val("");
		}
	});
}