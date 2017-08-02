var authorizeId = window.parent.getAuthorizeId();
$(function() {
	getAuthorizeOne();
});
function getAuthorizeOne() {
	$.ajax({
		type : "post",
		url : "../dataSource/getAuthorizeOne.do",
		data : {
			id : authorizeId,
		},
		dataType : "json",
		async : false,
		success : function(res) {
			$("#username").val(res.username);
			$("#password").val(res.password);
			$("#institutionId").val(res.institutionId);
			$("#providerId").val(res.providerId);
		}
	});
}
function editSubmit() {
	/*
	 * if($.trim($("#username").val()).length<1) { alert('资源提供商名称不能为空'); return
	 * null; }
	 */
	$.ajax({
		type : "post",
		url : "../dataSource/updateAuthorize.do",
		data : {
			id : authorizeId,
			institutionId : $("#institutionId").val(),
			providerId : $("#providerId").val(),
			username : $("#username").val(),
			password : $("#password").val(),
		},
		success : function(data) {
			if (data > 0) {
				var index = parent.layer.getFrameIndex(window.name);
				window.parent.find(1);
				parent.layer.msg('提交成功！');
				parent.layer.close(index);
			}
		},
	});
}