
var index = parent.layer.getFrameIndex(window.name);

function revocation(data){
		var appealReason=$("#appealReason").val();
		var isget=$("#isget").val();
		var perioid=$("#id").val();
		var dataState=1;
	if(appealReason!=undefined&&appealReason!=''&&appealReason!=null){
		$.ajax({
			type : "post",
			async:false,
			url : "../periocomment/updateNotes.do",
			dataType : "json",
			data : {
				"id":data,
				"dataState":dataState,
				"appealReason":appealReason,
				"isget":isget,
				"perioid":perioid
				},
			success : updatedata,
			error: function(XmlHttpRequest, textStatus, errorThrown){  
	            layer.msg("失败！")
	        }
	});
	}else{
		layer.msg("请输入人工审核建议！")
	}
}


function maintain(data){
	var appealReason=$("#appealReason").val();
	var isget=$("#isget").val();
	var perioid=$("#id").val();
	var dataState=0;
	if(appealReason!=undefined&&appealReason!=''&&appealReason!=null){
		$.ajax({
			type : "post",
			async:false,
			url : "../periocomment/updateNotes.do",
			dataType : "json",
			data : {
				"id":data,
				"dataState":dataState,
				"appealReason":appealReason,
				"isget":isget,
				"perioid":perioid
				},
			success : updatedata,
			error: function(XmlHttpRequest, textStatus, errorThrown){  
	            layer.msg("失败！")
	        }
	});
	}else{
		layer.msg("请输入人工审核建议！")
	}
}

function updatedata(){
	parent.layer.msg('操作成功');
	parent.location.reload(); 
}


function coloseLayer(){
	parent.layer.close(index);
}