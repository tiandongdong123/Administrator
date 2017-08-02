var idss = window.parent.getmaId();// 一级页面传过来的 母体id

var providerId = window.parent.getproviderId();// 提供商 id
var resourceTypeId = window.parent.getresourceTypeIds();// 资源类型id
var classId = window.parent.getClassid();// 学科id

$(function() {
	findAllProvider(); // 资源提供商查询
	getSubject();
	if (idss != null && idss != "") {
		getMatrixLiterature();
	}
});
/** ** 根据Id 查询 母体 *** */
function getMatrixLiterature() {
	$.ajax({
		type : "post",
		url : "../dataSource/getMatrixLiteratureById.do",
		data : {
			idss : idss,
		},
		dataType : "json",
		async : false,
		success : function(res) {
			// alert(alert(JSON.stringify(res)));
			selected("provider", res.muti.providerId);// 提供商
			selected("resourceName", res.muti.proResourceId);// 资源类型
			$("#parentId").val(res.muti.psubjectId),// 学科ID
			$("#parentName").val(res.xueke.pCategoryName),// 学科名称
			$("#title").val(res.muti.title);// 篇名
			$("#nameen").val(res.muti.nameen);// 英文名
			$("#author").val(res.muti.author);// 作者
			$("#abstracts").val(res.muti.abstracts);// 摘要
			$("#datePeriod").val(res.muti.datePeriod);// 年/期
			$("#cover").val(res.muti.cover);// 封面
		}
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
		$(res).each(
				function(n) {
					$("<option/>").html(res[n].nameZh).val(res[n].id)
							.appendTo("#provider");
				});
		selected("provider", providerId);// 提供商默认选中
		findResourceNamesById();// 资源类型查寻
	});

}
/** 查询资源类型 * */
function findResourceNamesById() {
	// $("#parentId").val("");
	// $("#parentName").val("");
	// $("#resourceName").html("");
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
						$("<option  />").html(res[s].resourceName).val(
								res[s].id).appendTo("#resourceName");
					});
			selected("resourceName", resourceTypeId);
		}
	});
}
/** 查询资源类型 结束 * */
/** 查询学科分类 * */
function getSubject() {
	$.ajax({
		type : "post",
		data : {
			classid : classId,
		},
		url : "../dataSource/getPSubjectCategoryById.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {
		},
		success : function(res) {
			$("#parentId").val(res.id);
			$("#parentName").val(res.pCategoryName);
		}
	});
}

/** ****選擇學科*** */
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
						+ "&numType=1", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.queren();
		},
		cancel : function(index) {
		}
	});
}
function getprociderId() {
	return $("#provider").val();
}
function getProResourceId() {
	return $("#resourceName").val();
}
/** *资源提供商 查询 结束** */
/** *增加母体文献** */
function remindSubmit() {
	if ($.trim($("#parentName").val()).length < 1) {
		parent.layer.msg('请选择学科', {icon : 2});
		return false;
	}
	if ($.trim($("#title").val()).length < 1) {
		parent.layer.msg('篇名不能为空', {icon : 2});
		return false;
	}
	$.ajax({
		type : "post",
		url : "../dataSource/addMatrixLiterature.do",
		data : {
			"providerId" : $("#provider").val(),
			"psubjectId" : $("#parentId").val(),
			"proResourceId" : $("#resourceName").val(),
			"title" : $("#title").val(),
			"nameen" : $("#nameen").val(),
			"author" : $("#author").val(),
			"abstracts" : $("#abstracts").val(),
			"datePeriod" : $("#datePeriod").val(),
			"cover" : $("#cover").val(),
			"id" : idss,
		},

		success : function(data) {
			if (data > 0) {
				var index = parent.layer.getFrameIndex(window.name);
				window.parent.findMatrixLiteratureList(1);
				parent.layer.msg('提交成功', {
					icon : 1
				});
				parent.layer.close(index);
			}
		},
		complete : function() {
//			$("#parentName").val("");

		}
	});
}