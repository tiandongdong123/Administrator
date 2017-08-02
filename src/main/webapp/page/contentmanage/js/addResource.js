/*对提交内容进行验证-liuYong*/
function submission() {
//	 alert("验证结果------"+fieldsCheck());
	/*if (fieldsCheck()) {*/
		addResource();
	/*}*/
}


function addResource(){
	var typeName=$("#rtypeName").val();
	var typedescri=$("#rtypeDescri").val();
	var typeCode=$("#rtypeCode").val();
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	if(typeName!=""&&typeName!=undefined&&!re.test(typeName)
			&&typeCode!=""&&typeCode!=undefined&&!re.test(typeCode)){		
		
		if(!checkTypeName(typeName)){
			layer.msg('资源类型名称不能重复', {icon: 2});
		}else if(!checkTypeCode(typeCode)){
			layer.msg('资源类型Code不能重复', {icon: 2});
		}else{
			$.ajax({
				type : "post",
				async:true,
				url : "../content/addResourceJson.do",
				dataType : "json",
				data : {
					"typeName":typeName,
					"typedescri":typedescri,
					'typeCode' : typeCode
				},
				success : function(data){
					if(data.flag="true"){
						layer.msg('添加成功', {icon: 1});
						window.location.href= "../content/resourceManage.do";
						reset();
					}else{
						layer.msg('添加失败',{icon: 2});
					}
				}
			});
			}		
		
	}else{
		layer.msg("*必填项",{icon: 2});
	
}
}
function reset(){
	$(".b").val("");
	$("#rtypeDescri").val("");
}

function updateResource(){
	var id=$("#resourceId").val();
	var typeName=$("#rtypeName").val();
	var typedescri=$("#rtypeDescri").val();
	var typeCode=$("#rtypeCode").val();
	$.ajax({
		type : "post",
		async:true,
		url : "../content/updateResourceJson.do",
		dataType : "json",
		data : {
			"id":id,
			"typeName":typeName,
			"typedescri":typedescri,
			'typeCode' : typeCode
		},
		success : function(data){
			if(data.flag="true"){
				layer.msg('修改成功', {icon: 1});
				window.location.href="../content/resourceManage.do";
			}else{
				layer.msg('修改失败！', {icon: 2});
			}
		}
	});
}

function noupdate(){
	window.location.href="../content/resourceManage.do";
}

//判断资源类型名称是否重复
function checkTypeName(typeName){
	 var isExist=false;
	 
	 $.ajax({
		 type : "post",
		 async:false,
		 url:"../content/checkRsTypeName.do",
		 dataType:"json",
		 data:{"typeName":typeName},
		 success:function(data){
			 if(data.flag=="true"){
				 isExist=true;
			 }else{
				 isExist=false;
			 }
		 }
	 });
	 
	 return isExist;	 
}

//判断资源类型名称是否重复
function checkTypeCode(typeCode){
	 var isExist=false;
	 $.ajax({
		 type : "post",
		 async:false,
		 url:"../content/checkRsTypeCode.do",
		 dataType:"json",
		 data:{"typeCode":typeCode},
		 success:function(data){
			 if(data.flag=="true"){
				 isExist=true;
			 }else{
				 isExist=false;
			 }
		 }
	 });
	 
	 return isExist;	 
}



