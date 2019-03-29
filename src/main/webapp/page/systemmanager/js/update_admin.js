function doupdateadmin(){
		var pagenum=$("#pagenum").val();
		var id=$("#userid").val();
		var password = $("#password").val();
		var username=$("#realname").val();
		var deptname=$("#deptname").val();
		var roleid=$("#rolename").find("option:selected").val();
		
		if(password==null||password==''){
			$("#chcekpassword").text("请输入密码");
			$("#password").focus();
			return;
		}else{
			$("#checkpassword").text("");
		}
		
		var deptname=$("#deptname").val();
		if(deptname==null||deptname==''){
			$("#checkpassword").text("请填写部门名称");
			$("#deptname").focus();
			return;
		}else{
			$("#checkpassword").text("");
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
}
// 取消按钮
function closeWindow() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}
