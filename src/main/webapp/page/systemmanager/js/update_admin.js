function doupdateadmin(){
	var pagenum=$("#pagenum").val();
	var id=$("#userid").val();
	var password = $.trim($("#password").val());
	var username=$.trim($("#realname").val());
	var deptname=$.trim($("#deptname").val());
	var roleid=$("#rolename").find("option:selected").val();

	if(password==null||password==''){
		$("#pwdStr").text("请填写密码");
		$("#password").focus();
		return;
	}else if (password.length<6) {
		$("#pwdStr").text("由6-16个字符组成");
		$("#password").focus();
		return;
	}else if (!checkId(password)){
		$("#pwdStr").text('密码不能包含中文');
		$("#password").focus();
		return;
	}else{
		$("#pwdStr").text("");
	}
	
	if(username==null||username==''){
		$("#nameStr").text("请填写真实姓名");
		$("#realname").focus();
		return;
	}else{
		$("#nameStr").text("");
	}

	var patrn= /^[\u4E00-\u9FA5]+$/i;
	if(deptname==null||deptname==''){
		$("#checkpassword").text("请填写部门名称");
		$("#deptname").focus();
		return;
	}else if(!patrn.test(deptname)){
		$("#departmentStr").text("部门必须为中文");
		$("#deptname").focus();
		return;
	}else{
		$("#departmentStr").text("");
	}

	$.ajax({  
		type : "POST",  
		url : "../admin/doupdateadmin.do",
		data : {
			'password': password,
			'user_realname':username,
			'department':deptname,
			'role_id':roleid,
			"id":id
		},
		dataType : "json",
		success : function(data) {
			if(data.flag===true){
				var index = parent.layer.getFrameIndex(window.name);
				parent.layer.msg('修改成功');
				window.parent.adminpage(pagenum);
				parent.layer.close(index);
			}else if(data.flag===false){
				var index = parent.layer.getFrameIndex(window.name);
				parent.layer.msg('修改失败');
				parent.layer.close(index); 
			}else if(data.flag==="fail") {
				var index = parent.layer.getFrameIndex(window.name);
				parent.layer.msg(data.fail);
				parent.layer.close(index); 
			}
		}
	});
}
function checkMobile(str) {
	var re = /^0?1[3|4|5|8][0-9]\d{8}$/;
	if (re.test(str)) {
		return true;
	} else {
		return false;
	}
}

function checkPhone(str){
	var re =/^0\d{2,3}-\d{7,8}(-\d{1,6})?$/;
	if(re.test(str)){
		return true;
	}else{
		return false;
	}
}

function checkemail(str){
	/*var reg =/^[A-Za-zd]+([-_.][A-Za-zd]+)*@([A-Za-zd]+[-.])+[A-Za-zd]{2,5}$/;*/
	/*	var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;*/
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if(reg.test(str)){
		return true;
	}else{
		return false;
	}
}

//输入框内容空值校验
function checkValue(type,str) {
	let typeObj = {
			"pwd": "请填写密码",
			"name":"请填写真实姓名",
			"department": "请填写部门名称"
	}
	var typeClass = "#"+type+"Str"
	if(str===''){
		$(typeClass).text(typeObj[type])
	}else {
		$(typeClass).text('')
	}
	if (type==='pwd') {
		if(str.length<6) {
			$(typeClass).text('由6-16个字符组成')
		}else if (!checkId(str)) {
			$(typeClass).text('密码不能包含中文')
		}
	}
}
//取消按钮
function closeWindow() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}
function checkId(str) {
    var reg = /^\w+$/;
    if(reg.test(str)){
    	return true;
    }else{
    	return  false;
    }
}