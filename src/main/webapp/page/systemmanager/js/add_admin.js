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
							$("#checkadminid").text("一旦注册成功不能修改");
					}
				}
			});
	}else{
		$("#checkadminid").text("请输入角色名");
	}
}
function doaddadmin(){
	var cname=$("#cname").val();
	if(cname=="Y"){
		var adminid=$("#username").val();
		if(adminid.length<6){
			$("#username").focus();
			return;
		}
		var cid = checkId(adminid);
		if(cid==false){
			$("#username").focus();
			$("#checkadminid").text("用户名限定为字母、数字或下划线的组合");
			return;
		}
		var password = $("#password").val();
		if(password==null||password==''){
			$("#checkpassword").text("请输入密码");
			$("#password").focus();
			return;
		}else{
			$("#checkpassword").text("");
		}
		
		
		
		var name = $("#name").val();
		
		
		var deptname=$("#deptname").find("option:selected").val();
		var roleid=$("#rolename").find("option:selected").val();
		
		
		var mphone = $("#mphone").val();
		if(mphone!=null&&mphone!=''){
			var cm = checkMobile(mphone);
			if(cm==false){
				$("#mphone").focus();
				$("#chcekmphone").text("该手机号无效");
				return;
			}else{
				$("#chcekmphone").text("");
			}
		}
		var telphone=$("#telphone").val();
		if(telphone!=null&&telphone!=''){
			var ctel = checkPhone(telphone);
			if(ctel==false){
				$("#telphone").focus();
				$("#checktelphone").text("该固话号码无效");
				return;
			}else{
				$("#checktelphone").text("");
			}
		}
		var email = $("#email").val();
		if(email!=null&&email!=''){
			var cemail = checkemail(email);
			if(cemail==false){
				$("#email").focus();
				$("#checkemail").text("该邮箱无效");
				return;
			}else{
				$("#checkemail").text("");
			}
		}
		$.ajax( {  
			type : "POST",  
			url : "../admin/doaddadmin.do",
				data : {
					'wangfang_admin_id' : adminid,
					'user_realname':name,
					'password': password,
					'mobile_phone':mphone,
					'tel_phone':telphone,
					'department':deptname,
					'role_id':roleid,
					'email':email
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

function showvalue(){
	$("#checkadminid").text("由6-16个字符组成");
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
}
// 取消按钮
function closeWindow() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}