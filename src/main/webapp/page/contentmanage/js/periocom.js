
var index = parent.layer.getFrameIndex(window.name);

function revocation(data){
	var appealReason=$("#appealReason").val();
	var rand_id=$("#rand_id").val();
	var isget=$("#isget").val();
	if(appealReason==null||appealReason==''||appealReason==undefined){
		layer.msg("请输入人工审核建议！")
		return false;
	}
	
	var isupdata=0;
	if(isget==undefined||isget==''||isget==null){
		isupdata=1;
	}
	var dataState=1;
	$.ajax({
		type : "post",
		async:false,
		url : "../periocomment/updateNotes.do",
		dataType : "json",
		data : {
			"id":data,
			"dataState":dataState,
			"rand_id":rand_id,
			"appealReason":appealReason,
			"isupdata":isupdata
			},
		success : updatedata,
		error: function(XmlHttpRequest, textStatus, errorThrown){  
            layer.msg("失败！")
        }
});
}


function maintain(data){
	var appealReason=$("#appealReason").val();
	var isget=$("#isget").val();
	var rand_id=$("#rand_id").val();
	var isupdata=0;
	if(isget==undefined||isget==''||isget==null){
		isupdata=1;
	}
	if(appealReason==null||appealReason==''||appealReason==undefined){
		layer.msg("请输入人工审核建议！")
		return false;
	}
	var dataState=0;
	$.ajax({
		type : "post",
		async:false,
		url : "../periocomment/updateNotes.do",
		dataType : "json",
		data : {
			"id":data,
			"rand_id":rand_id,
			"dataState":dataState,
			"appealReason":appealReason,
			"isupdata":isupdata
			},
		success : updatedata,
		error: function(XmlHttpRequest, textStatus, errorThrown){  
            layer.msg("失败！")
        }
});
}

function updatedata(){
	parent.layer.msg('操作成功');
	parent.location.reload(); 
}


function coloseLayer(){
	parent.layer.close(index);
}