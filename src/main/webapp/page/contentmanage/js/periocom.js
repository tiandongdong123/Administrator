function revocation(data){
	var dataState="1";
	var preliminaryOpinions=$("#preliminaryOpinions").val();
	var finalOpinion=$("input[name='finalOpinion']:checked").val();
//	alert(finalOpinion);
	if(finalOpinion=='自定义')
		{
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		var text=$("#zdy").val();
		if(text!=''&&text!=undefined&&!re.test(text))
			{
			$.ajax({
				type : "post",
				async:false,
				url : "../periocomment/updateNotes.do",
				dataType : "json",
				data : {
					"id":data,
					"dataState":dataState,
					"complaintStatus":"1",
					"preliminaryOpinions":preliminaryOpinions,
					"finalOpinion":text,
					"handlingStatus":3
					},
				success : updatedata					
			});
			}else{
				layer.msg("请输入自定义内容");
			}
		}else{
			$.ajax({
				type : "post",
				async:false,
				url : "../periocomment/updateNotes.do",
				dataType : "json",
				data : {
					"id":data,
					"dataState":dataState,
					"complaintStatus":"1",
					"preliminaryOpinions":preliminaryOpinions,
					"finalOpinion":finalOpinion,
					"handlingStatus":3
					},
				success : updatedata					
			});
		}
	
}


function maintain(data){
	var dataState="0";
	var preliminaryOpinions=$("#preliminaryOpinions").val();
	var finalOpinion=$("input[name='finalOpinion']:checked").val();
//	alert(finalOpinion);
	if(finalOpinion=='自定义')
		{
		var text=$("#zdy").val();
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		if(text!=''&&text!=undefined&&!re.test(text))
			{
			$.ajax({
				type : "post",
				async:false,
				url : "../periocomment/updateNotes.do",
				dataType : "json",
				data : {
					"id":data,
					"dataState":dataState,
					"complaintStatus":"1",
					"preliminaryOpinions":preliminaryOpinions,
					"finalOpinion":text,
					"handlingStatus":3
					},
				success : updatedata,
				error: function(XmlHttpRequest, textStatus, errorThrown){  
		            layer.msg("失败！")
		        }
			});
			}else{
				layer.msg("请输入自定义内容!");
			}
		}else{
			$.ajax({
				type : "post",
				async:false,
				url : "../periocomment/updateNotes.do",
				dataType : "json",
				data : {
					"id":data,
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
	
}
$(function(){
	$("#san").onclick(function(){
		if($("#san:checked")){
			$("#zdy").css("dispaly","block");
		}else{
			$("#zdy").css("dispaly","none");
			}
	});
});



function updatedata(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.msg('操作成功');
	parent.location.reload(); 
}