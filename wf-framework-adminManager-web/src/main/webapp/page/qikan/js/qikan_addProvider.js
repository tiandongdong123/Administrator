var gysId = window.parent.getgysId();
$(function() {
	if (gysId != null && gysId != "") {
		getProviderById();
	}
});
function getProviderById() {
	$.getJSON("../dataSource/getProviderById.do", {
		gysId : gysId,
	}, function(res) {
		$("#nameZh").val(res.nameZh);// 供应商名称
		$("#providerName").val(res.providerName);// 供应商名称
	});
}

function remindSubmit() {

	if ($.trim($("#nameZh").val()).length < 1) {
		parent.layer.msg('资源提供商名称不能为空', {icon:1});
		return false;
	}
	if ($.trim($("#providerName").val()).length < 1) {
		parent.layer.msg('资源提供商名称CODE码', {icon:1});
		return false;
	}
	
	var reg=new RegExp("^[A-Za-z]+$"); 
	if(!reg.test($.trim($("#providerName").val()))){
		parent.layer.msg('资源提供商名称CODE码格式不正确', {icon:1});
		return false;
	}
	
	$.ajax({
		type : "post",
		url : "../dataSource/addProvider.do",
		data : {
			"nameZh" : $("#nameZh").val(),
			"providerName" : $("#providerName").val(),
			"id" : gysId,
		},
		beforeSend : function() {
			$("#submit").attr({
				disabled : "disabled"
			});
		},
		success : function(data) {
			if (data > 0) {
				var index = parent.layer.getFrameIndex(window.name);
				window.parent.findAllProvider();
				parent.layer.msg('提交成功', {
					icon : 1
				});
				parent.layer.close(index);
			}
		},
		complete : function() {
			$("#providerName").val("");
			$("#submit").removeAttr("disabled");

		}
	});
}