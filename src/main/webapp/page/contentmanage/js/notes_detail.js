/*撤销禁用*/
function disableNootes(dataState){
	var finalOpinion = $("#finalOpinion").val();
	if(null == finalOpinion || '' == finalOpinion.replace(/(^\s*)|(\s*$)/g, '')){
		$('#finalOpinionPrompt').show();
		return;
	}
	var id=$("#recordId").val();
	var noteNum=$("#noteNum").val();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/updateNotes.do",
		dataType : "json",
		data : {
			"recordId":id,
			"noteNum":noteNum,
			"dataState":dataState,
			"finalOpinion":finalOpinion,
			},
		success : updatedata,
		error: function(XmlHttpRequest, textStatus, errorThrown){  
            layer.msg("失败");
        }
	});
}

function updatedata(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.msg('操作成功');
	parent.paging(); 
	parent.layer.close(index);	
}

/*关闭*/
function close_(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}