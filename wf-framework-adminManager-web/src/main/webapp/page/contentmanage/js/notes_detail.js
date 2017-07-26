/*撤销禁用*/
function revocation(){
	var id=$("#noteid").val();
	var preliminaryOpinions=$("#preliminaryOpinions").val();
	var finalOpinion=$("input:radio[name='finalOpinion']:checked").val();
	if(finalOpinion=='自定义'){
		finalOpinion=$("#zdy").val();
	}
	$.ajax({
		type : "post",
		async:false,
		url : "../content/updateNotes.do",
		dataType : "json",
		data : {
			"id":id,
			"dataState":"1",
			"complaintStatus":"1",
			"preliminaryOpinions":preliminaryOpinions,
			"finalOpinion":finalOpinion,
			"handlingStatus":3
			},
		success : updatedata,
		error: function(XmlHttpRequest, textStatus, errorThrown){  
            layer.msg("失败");
        }
	});
}

/*维持禁用*/
function maintain(){
	var id=$("#noteid").val();
	var dataState="0";
	var preliminaryOpinions=$("#preliminaryOpinions").val();
	var finalOpinion=$("input:radio[name='finalOpinion']:checked").val();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/updateNotes.do",
		dataType : "json",
		data : {
			"id":id,
			"dataState":dataState,
			"complaintStatus":"1",
			"preliminaryOpinions":preliminaryOpinions,
			"finalOpinion":finalOpinion,
			"handlingStatus":3
			},
		success : updatedata,
		error: function(XmlHttpRequest, textStatus, errorThrown){  
            layer.msg("失败！")
        }
	});
}

/*禁用*/
function closenote(id){
	var id=$("#noteid").val();
	var finalOpinion=$("input:radio[name='finalOpinion']:checked").val();
	if(finalOpinion=='自定义'){
		finalOpinion=$("#zdy").val();
	}
	
	$.ajax( {  
		type : "POST",  
		url : "../content/closenote.do",
			data : {
				'id' : id,
				'finalOpinion':finalOpinion,
			},
			dataType : "json",
			success :updatedata,
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

