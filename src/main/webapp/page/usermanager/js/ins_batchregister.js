$(document).ready(function(){
	$("input[name='openState']").prop("checked",false);
	$("input[name='resourceType']").prop("checked",false);
	$("input[id='resourceInherited']").prop("checked",true);
	//绑定个人上限的提示
	$("#bindLimit").keyup(function(){
		check();
	})
});
//提交事件
function submitForm(){
	var reg = /^[1-9]\d*$/;
	var bool = false;
	if($("#bindLimit").val()==""){
		$(".mistaken").text("绑定个人上限不能为空，请填写正确的数字");
		style();
		bool = true;
	}else if(!reg.test($("#bindLimit").val())){
		$(".mistaken").text("绑定个人上限是大于0的整数，请填写正确的数字");
		style()
		bool = true;
	}else {
		$(".bind_num").css("color","#00a65a");
		$("#bindLimit").css("border-color","#00a65a");
		$(".wrong").css("background","url(../img/t.png)");
		$(".wrong").css("display","inline");
		$(".mistaken").css("display","none");
	}
	if(bool){
		return ;
	}
	addAtrr();
	if(!validateFrom()){
		removeAtrr();
		return false;
	}else{
		var data = new FormData($('#fromList')[0]);
		$.ajax({  
			url: '../auser/addbatchRegister.do',  
			type: 'POST',
			data: data,
			cache: false,  
			processData: false,
			contentType: false  
		}).done(function(data){
			if(data.flag=="success"){
	    		layer.alert(data.success, {
	    			icon: 1,
	    		    skin: 'layui-layer-molv',
	    		    btn: ['确定'], //按钮
	    		    yes: function(){
	    		    	window.location.href='../auser/batchregister.do';
	    		    }
	    		});
			}else if(data.flag=="fail"){
				layer.msg(data.fail,{icon: 2});
			}else{
				layer.msg("未知的系统错误，请联系管理员",{icon: 2});
			}
		});
		removeAtrr();
	}
}

function download1(title){
	window.location.href='../auser/worddownload.do?title='+encodeURI(encodeURI(title));
}

function removeAtrr(){
	$("#submit").removeAttr("disabled");
	$("#submit1").removeAttr("disabled");
}

function addAtrr(){
	$("#submit").attr({disabled: "disabled"});
	$("#submit1").attr({disabled: "disabled"});
}