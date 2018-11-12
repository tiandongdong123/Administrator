$(function(){
	var level="";
	level=$("#level").val();
	if(level !=""){
		$("#xkjb").find("option:selected").text(level);
	}
});

/*对提交内容进行验证-liuYong*/
function submission(){
//	 alert("验证结果------"+fieldsCheck());
	//if(fieldsCheck()){
		addSubject();
	//}
}

function addSubject(){
	var level=$("#xkjb").find("option:selected").val();
	var classNum=$("#inputEmail3").val();
	var className=$("#inputEmail4").val();
	
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	
	if(level=="" ||level==undefined || re.test(level)){
		layer.msg("级别为必填项!",{icon: 2});
	}else if(classNum=="" || classNum==undefined || re.test(classNum)){
		layer.msg("分类号为必填项!",{icon: 2});
	}else if(className=="" || className==undefined || re.test(className)){
		layer.msg("分类名称为必填项!",{icon: 2});
	}else{
		$.ajax({
			type : "post",
			async:true,
			url : "../content/addSubjectType.do",
			dataType : "json",
			data : {
				"level":level,
				"classNum":classNum,
				"className":className
			},
			success : function(data){
				if(data.flag="true"){
					layer.msg("添加成功!",{icon: 1});
					window.location.href="../content/subjectquery.do";
				}else{
					layer.msg("添加失败!",{icon: 2});
				}
			},
			error: function(XmlHttpRequest, textStatus, errorThrown){  
	            alert("添加失败！");
	        }
		});
	 }
  }

function reset(){
	$("#inputEmail3").val("");
	$("#inputEmail4").val("");
}

function upateSubject(){
	var id=$("#idsub").val();
	var level=$("#level").val();
	var classNum=$("#inputEmail3").val();
	var className=$("#inputEmail4").val();
	$.ajax({
		type : "post",
		async:true,
		url : "../content/updateSubjectJson.do",
		dataType : "json",
		data : {
			"id":id,
			"level":level,
			"classNum":classNum,
			"className":className
		},
		success : function(data){
			if(data.flag="true"){
				layer.msg("修改成功!",{icon: 1});
				window.location.href="../content/subjectquery.do";
			}else{
				layer.msg("修改失败!",{icon: 2});
			}
		}
	});
}

function noupdate(){
	window.location.href="../content/subjectquery.do";
}

