function doadddept(){
	var val = $("#checkname").val();
	if(val=='y'){	
		var deptname= $("#deptname").val();
		var describe=$("#deptdescribe").val();
		if(deptname!=null&&deptname!=''){
		$.ajax( {  
			type : "POST",  
			url : "../dept/doadddept.do",
			data : {
				'deptName' : deptname,
				'deptDescribe' : describe,
			},
			dataType : "json",
			success : function(data) {
				if(data){
					var index = parent.layer.getFrameIndex(window.name);
					parent.layer.msg('添加成功');
					parent.location.reload();
				}else{
					var index = parent.layer.getFrameIndex(window.name);
					parent.layer.msg('添加失败');
					parent.layer.close(index); 
				}
			}
		});
		}else{
			$("#rtcname").text("部门名称不能为空");
		}
	}else{
		$("#rtcname").text("请填写或修改部门名称");
	}
}


function checkdeptname(){
	var deptname= $("#deptname").val();
	$.ajax( {  
		type : "POST",  
		url : "../dept/checkdept.do",
			data : {
				'deptname' : deptname,
			},
			dataType : "json",
			success : function(data) {
				if(data){
					$("#checkname").val("n");
					$("#rtcname").text("部门名称重复");
				}else{
					$("#checkname").val("y");
					$("#rtcname").text("");
				}
			}
		});
}

function shuaxin(){
	
	$("#deptname").val("");
	$("#deptdescribe").val("");
	
}