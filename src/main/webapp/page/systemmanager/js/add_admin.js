function checkadminid(id){
	if(id!=null&&id!=''){
		$.ajax( {  
			type : "POST",  
			url : "../admin/checkadminid.do",
				data : {
					'id' : id,
				},
				dataType : "json",
				success : function(data) {
					if(data){
							$("#cname").val("N")
							$("#checkadminid").text("名称重复，请重新输入");
					}else{
							$("#cname").val("Y")
							if($("#usernameAdmin")[0].value.length>=6) {
								$("#checkadminid").text("一旦注册成功不能修改");								
							} 
					}
				}
			});
	}else{
		$("#checkadminid").text("请填写用户名");
	}
}
function doaddadmin(){
	var cname=$("#cname").val();
	$("#usernameAdmin").focus();
	if(cname=="Y"){
		var adminid=$.trim($("#usernameAdmin").val());
		if(adminid.length<6){
			$("#usernameAdmin").focus();
			return;
		}
		var cid = checkId(adminid);
		if(cid==false){
			$("#usernameAdmin").focus();
			$("#checkadminid").text("用户名限定为字母、数字或下划线的组合");
			return;
		}
		var password = $.trim($("#passwordAdmin").val());
		if(password==null||password==''){
			$("#checkpassword").text("请输入密码");
			$("#passwordAdmin").focus();
			return;
		}else if (password.length<6) {
			$("#checkpassword").text("由6-16个字符组成");
			$("#passwordAdmin").focus();
			return;
		}else if (!checkId(password)){
			$("#checkpassword").text('密码不能包含中文');
			$("#passwordAdmin").focus();
			return;
		}else{
			$("#checkpassword").text("");
		}
		
		var deptname=$.trim($("#deptname").val());
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
		
		var name = $.trim($("#name").val());
		if(name==null||name==''){
			$("#checkpassword").text("请填写真实姓名");
			$("#name").focus();
			return;
		}else{
			$("#checkpassword").text("");
		}
		
		var roleid=$("#rolename").find("option:selected").val();
		

		$.ajax( {  
			type : "POST",  
			url : "../admin/doaddadmin.do",
				data : {
					'wangfang_admin_id' : adminid,
					'user_realname':name,
					'password': password,
					'department':deptname,
					'role_id':roleid,
				},
				dataType : "json",
				success : function(data) {
					if(data){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg('添加成功',{icon: 1});
						parent.location.reload();
					}else{
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg('添加失败',{icon: 2});
						parent.layer.close(index); 
					}
				}
			});
	}
}

function showvalue(str){
	if(str.length<6) {
		$("#checkadminid").text("由6-16个字符组成");		
	}
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


function checkId(str) {
    var reg = /^\w+$/;
    if(reg.test(str)){
    	return true;
    }else{
    	return  false;
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

// 输入框内容空值校验
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
// 取消按钮
function closeWindow() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}