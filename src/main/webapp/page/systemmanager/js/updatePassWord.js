function checkOldPassWord(val) {
	if(val!=null&&val!=''){
		$.ajax( {  
			type : "POST",  
			url : "../admin/doUpdatePasswordcheck.do",
				data : {
					'oldPassword' : val,
				},
				dataType : "json",
				success : function(data) {
					if(!data) {
						$("#checkrolename").text("密码输入错误")
					}else {
						$("#cname").val("Y")
						$("#checkrolename").text("")
					}
				}
			});
	}else{
		$("#checkrolename").text("密码不能为空")
	}
}
function checkrNew(val) {
	var patt1 = new RegExp(/\s+/g);
	if(val=='') {
		$('#checkroleNew').text('密码不能为空')
		return
	}else {
		$('#checkroleNew').text('')
	}
	if(val.length<6) {
		$('#checkroleNew').text('密码长度为6-16位字符')
		return
	}else {
		$('#checkroleNew').text('')
	}
	if(patt1.test(val)) {
		$('#checkroleNew').text('密码不能包含空格')
		return
	}else {
		$('#checkroleNew').text('')
	}
	if(!(escape(val).indexOf("%u")<0)) {
		$('#checkroleNew').text('密码不能包含中文')
		return
	}else {
		$('#checkroleNew').text('')
	}
	
}
function checkrPwd(val) {
	var patt1 = new RegExp(/\s+/g);
	if(val=='') {
		$('#checkrolePwd').text('密码不能为空')
		return
	}else {
		$('#checkrolePwd').text('')
	}
	if(val.length<6) {
		$('#checkrolePwd').text('密码长度为6-16位字符')
		return
	}else {
		$('#checkrolePwd').text('')
	}
	if(patt1.test(val)) {
		$('#checkrolePwd').text('密码不能包含空格')
		return
	}else {
		$('#checkrolePwd').text('')
	}
	if(!(escape(val).indexOf("%u")<0)) {
		$('#checkrolePwd').text('密码不能包含中文')
		return
	}else {
		$('#checkrolePwd').text('')
	}
	
	if($('#roleNew').val()!=$('#rolePwd').val()) {
		$('#checkrolePwd').text('两次密码输入不一致')
	}else {
		$('#checkrolePwd').text('')
	}
}

function doaddrole() {
	var cname=$("#cname").val();
	if($('#rolename').val()=='') {
		$("#checkrolename").text("密码不能为空")
	}else {
		$("#checkrolename").text('')
	}
	if($('#roleNew').val()=='') {
		$('#checkroleNew').text('密码不能为空')
	}else {
		$('#checkroleNew').text('')
	}
	if($('#rolePwd').val()=='') {
		$('#checkrolePwd').text('密码不能为空')
	}else {
		$('#checkrolePwd').text('')
	}
	checkrNew($('#roleNew').val())
	checkrPwd($('#rolePwd').val())
	if(cname=="Y"){
		if($('#roleNew').val()!=$('#rolePwd').val()) {
			return
		}
		if($('#roleNew').val().length<6||$('#rolePwd').val().length<6){
			return
		}
		if(!(escape($('#roleNew').val()).indexOf("%u")<0)||!(escape($('#rolePwd').val()).indexOf("%u")<0)) {
			return
		}
		$.ajax({  
			type : "POST",  
			url : "../admin/doUpdatePassword.do",
				data : {
					'oldPassword' : $('#rolename').val(),
					'newPassword':$('#roleNew').val(),
					'repeatPassword': $('#rolePwd').val()
				},
				dataType : "json",
				success : function(data) {
					if(data){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("修改成功");
						parent.layer.close(index);
					}else{
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("修改失败");
						parent.layer.close(index);
					}
				}
			});
	}else {
		if($('#rolename').val()=='') {
			$("#checkrolename").text("密码不能为空")
			return
		}
		
		$("#checkrolename").text("密码输入错误")
		return
	}
}
//取消按钮
function closeWindow() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}
