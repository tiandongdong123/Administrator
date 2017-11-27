
var index = parent.layer.getFrameIndex(window.name);

function revocation(data){
	var appealReason=$("#appealReason").val();
	var dataState=1;
	$.ajax({
		type : "post",
		async:false,
		url : "../periocomment/updateNotes.do",
		dataType : "json",
		data : {
			"id":data,
			"dataState":dataState,
			"appealReason":appealReason,
			},
		success : updatedata,
		error: function(XmlHttpRequest, textStatus, errorThrown){  
            layer.msg("失败！")
        }
});
}


function maintain(data){
	var appealReason=$("#appealReason").val();
	var dataState=0;
	$.ajax({
		type : "post",
		async:false,
		url : "../periocomment/updateNotes.do",
		dataType : "json",
		data : {
			"id":data,
			"dataState":dataState,
			"appealReason":appealReason,
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