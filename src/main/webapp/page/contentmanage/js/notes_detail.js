/*撤销禁用*/
function disableNootes(dataState){
	var finalOpinion = $("#finalOpinion").val();
	if(null == finalOpinion || '' == finalOpinion.replace(/(^\s*)|(\s*$)/g, '')){
		layer.msg("人工处理意见不能为空");
		return;
	}
	var id=$("#noteid").val();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/updateNotes.do",
		dataType : "json",
		data : {
			"id":id,
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

