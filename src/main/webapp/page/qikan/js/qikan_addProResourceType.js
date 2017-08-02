var providerId = window.parent.getproviderId();// 提供商 id
var proId = window.parent.getProId();// 资源类型Id
$(function() {
	findAllProvider(); // 资源提供商查询
	if (proId != null && proId != "") {
		getProResourceType();
	}
});

/** ****资源提供商 查询*** */
var providerId;
function findAllProvider() {
	$.getJSON("../dataSource/findAllProviders.do", {}, function(res) {
		$(res).each(
				function(n) {

					$("<option/>").html(res[n].nameZh).val(res[n].id)
							.appendTo("#provider");
				});
		selected("provider", providerId);
	});

}

function getProResourceType() {

	$.getJSON("../dataSource/getProResourceType.do", {
		proid : proId,
	}, function(res) {
		selected("provider", res.providerId);
		$("#proResourceName").val(res.resourceName);
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

/** *资源提供商 查询 结束** */
/** *增加资源类型** */
function remindSubmit() {
	if ($.trim($("#proResourceName").val()).length < 1) {
		parent.layer.msg('资源提类型名称不能为空',{icon:2});
		return null;
	}
	$.ajax({
		type : "post",
		url : "../dataSource/addProResourceType.do",
		data : {
			"providerId" : $("#provider").val(),
			"resourceName" : $("#proResourceName").val(),
			"id" : proId,
		},

		success : function(data) {
			if (data > 0) {
				var index = parent.layer.getFrameIndex(window.name);
				window.parent.findResourceNamesById($("#provider").val());
				parent.layer.msg('提交成功', {
					icon : 1
				});
				parent.layer.close(index);
			}
		},
		complete : function() {
			$("#proResourceName").val("");

		}
	});
}