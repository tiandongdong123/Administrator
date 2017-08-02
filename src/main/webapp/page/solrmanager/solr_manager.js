$(function() {
	chongzhi();
	$("#type").val("");
});
//选择下撤类型
function getType(value){
	if(value=="perio"){
		$("#perio").show();
		$("#conference").hide();
		$("#gazetteer_new").hide();
		$("#Yearbooks").hide();
		$("#other").hide();
	}else if(value=="conference"){
		$("#perio").hide();
		$("#conference").show();
		$("#gazetteer_new").hide();
		$("#Yearbooks").hide();
		$("#other").hide();
	}else if(value=="gazetteer_new"){
		$("#perio").hide();
		$("#conference").hide();
		$("#gazetteer_new").show();
		$("#Yearbooks").hide();
		$("#other").hide();
	}else if(value=="Yearbooks"){
		$("#perio").hide();
		$("#conference").hide();
		$("#gazetteer_new").hide();
		$("#Yearbooks").show();
		$("#other").hide();
	}else{
		$("#perio").hide();
		$("#conference").hide();
		$("#gazetteer_new").hide();
		$("#Yearbooks").hide();
		$("#other").show();
	}
}
//提交验证
function put(){
	var model = $("#type").val();//模块名称
	if(model==""||model==null){
		layer.alert("请选择模块名称");
		return;
	}
	var param="";//变量名称
	if(model=="perio"){
		param=$("input[name='perio_']:checked").val();
	}else if(model=="conference"){
		param=$("input[name='conference_']:checked").val();
	}else if(model=="gazetteer_new"){
		param=$("input[name='gazetteer_new_']:checked").val();
	}else if(model=="Yearbooks"){
		param=$("input[name='Yearbooks_']:checked").val();
	}else{
		param=$("input[name='other_']:checked").val();
	}
	if(param==undefined||param==""){
		layer.alert("请选择限定条件");
		return;
	}
	var data=$("#data").val();
	if(data==null||data==""){
		layer.alert("请输入数据");
		return;
	}
	$.ajax({
		type : "post",  
		url : "../solr/deleteSolr.do",
		dataType : "json",
		data: {model:model,param:param,data:data},
		success : function(data){
			if(data){
				layer.alert(data.msg);
			}
		},
		error : function(data){
			layer.alert("下撤失败！");
		}
	});
}

function chongzhi(){
	$("#other").show();
	$("#perio").hide();
	$("#conference").hide();
	$("#gazetteer_new").hide();
	$("#Yearbooks").hide();
}
