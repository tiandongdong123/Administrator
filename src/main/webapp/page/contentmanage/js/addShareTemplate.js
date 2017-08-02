 $(function(){
	 checkOne();
});

function addShareTemplate(){
	var shareType = $("#shareType").val();
	var checkbox=$("input[name='checkOne']:checked");
	var checkValue=new Array();
	checkbox.each(function(){
		checkValue.push($(this).val());
	});
	if(null==shareType || ""==shareType){
		layer.msg("分享类型必选项!",{icon: 2});
	}else if(!checkShareType(shareType)){
		layer.msg("分享类型不能重复!",{icon: 2});
	}else if(checkbox.length==0){
		layer.msg("分享字段为必选项!",{icon: 2});
	}else{
		$.ajax({
			type : "post",
			async:true,
			url : "../content/addShareTemplateJson.do",
			dataType : "json",
			data : {
				"shareType":shareType,
				"checkValue":checkValue,
			},
			success : function(data){
				if(data.flag==true ||data.flag=="true"){
					layer.msg("添加成功!");
					window.location.href="../content/shareTemplate.do";
				}else{
					layer.msg("添加失败!");
				}
			}
		});
	}
}
function reset(){
	$(".b").val("");
	$("#rtypeDescri").val("");
	$("#type_field").val("");
}

function updateShareTemplate(){
	var id=$("#resourceId").val();
	var shareType=$("#shareType").val();
	var checkbox=$("input[name='checkOne']:checked");
	var checkValue=new Array();
	checkbox.each(function(){
		checkValue.push($(this).val());
	});
	
	if(""==shareType || null==shareType){
		layer.msg("分享类型必填!",{icon: 2});
	}else if(!checkShareType(shareType,id,"update")){
		layer.msg("分享类型不能重复!",{icon: 2});
	}else if(checkbox.length==0){
		layer.msg("分享字段为必选项!",{icon: 2});
	}else{
		$.ajax({
			type : "post",
			async:true,
			url : "../content/updateShareTemplates.do",
			dataType : "json",
			data : {
				"id":id,
				"shareType":shareType,
				"checkValue":checkValue,
			},
			success : function(data){
				if(data.flag=="true"){
					layer.msg("修改成功!",{icon: 1});
					window.location.href="../content/shareTemplate.do";
				}else{
					layer.msg("修改失败！",{icon: 2});
				}
			}
		});
	}
}

function checkShareType(shareType,id,addOrUpdate){
	 var check_res=false;
	$.ajax({
		type : "post",
		async:false,
		url : "../content/checkShareTemplates.do",
		dataType : "json",
		data : {
			"id":id,
			"shareType":shareType,
			"addOrUpdate":addOrUpdate,
		},
		success : function(data){
			if(data.flag=="true"){
				check_res=true;
			}
		}
	});
	return check_res;
}

function getallcore(){
	$.ajax({
		type : "post",
		async:true,
		url : "../content/getallcore.do",
		dataType : "json",
		success : function(data){
			for(var i=0;i<data.length;i++){
				var core = data[i];
				$("#core_type").append("<option  value='"+core.field+"'>"+core.name+"</option>");
			}
		}
	});
}

function changecore(field){
	$.ajax({
	type : "post",
	async:true,
	url : "../content/getallfield.do",
	dataType : "json",
	data:{"field":$(field).val()},
	success : function(data){
		var type_field = "";
		for(var i=0;i<data.length;i++){
			var fields = data[i];
			type_field  = type_field + "${"+fields.field_field+"}->"+fields.name+"\t\t";
		}
		$("#type_field").html(type_field);
	}
});
}

function noupdate(){
	window.location.href="../content/shareTemplate.do";
}

//选择分享类型  展示相应的分享类型字段
function shareTypeChange(obj){
	
	var id=$(obj).children('option:selected').attr("id");
	$("#filedCheckBox").html("");
	if(id){
		$.ajax({
			type : "post",
			url:"../content/shareTypeChange.do",
			data:{"id":id},
			dataType : "json",
			success : function(data){
				var html="<input type='checkbox' value='' style='margin-left:10px' id='checkAll' onclick='checkAll();'/>全部";
				$(data).each(function(index,filed){
					html+="<input type='checkbox' value='"+filed.field_zh+":${"+filed.field_eng+"}' style='margin-left:10px' name='checkOne' onclick='checkOne();'/>"+filed.field_zh;
				});
				
				$("#filedCheckBox").html(html);
			}
		});
	}
}


//全选全不选
function checkAll(){
	
	if($("#checkAll").is(':checked')){
		$("input[name='checkOne']").each(function(){
			$(this).prop("checked", "checked");
		});
	}else{
		$("input[name='checkOne']").each(function(){
			$(this).removeAttr("checked");
		});
	}
}

function checkOne(){
	if($("input[name='checkOne']").length==$("input[name='checkOne']:checked").length){
		$("#checkAll").prop("checked", "checked");
	}else{
		$("#checkAll").removeAttr("checked");
	}
}









