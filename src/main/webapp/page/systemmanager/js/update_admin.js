function doupdateadmin(){
		var pagenum=$("#pagenum").val();
		var id=$("#userid").val();
		var password = $("#password").val();
		var username=$("#realname").val();
		var deptname=$("#deptname").find("option:selected").val();
		var roleid=$("#rolename").find("option:selected").val();
		var mphone = $("#mphone").val();
		var email=$("#email").val();
		
		if(password==null||password==''){
			$("#chcekpassword").text("请输入密码");
			$("#password").focus();
			return;
		}else{
			$("#checkpassword").text("");
		}
		
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
		
		$.ajax({  
			type : "POST",  
			url : "../admin/doupdateadmin.do",
				data : {
					'password': password,
					'user_realname':username,
					'mobile_phone':mphone,
					'tel_phone':telphone,
					'email':email,
					'department':deptname,
					'role_id':roleid,
					"id":id
				},
				dataType : "json",
				success : function(data) {
					if(data){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg('修改成功');
						window.parent.adminpage(pagenum);
						parent.layer.close(index);
					}else{
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg('修改失败');
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
