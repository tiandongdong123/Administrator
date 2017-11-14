//提交事件
function submitForm(){
	$("#submit").attr({disabled: "disabled"});
	if(!validateFrom()){
		$("#submit").removeAttr("disabled");
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
		$("#submit").removeAttr("disabled");
	}
}

function download1(title){
	window.location.href='../auser/worddownload.do?title='+encodeURI(encodeURI(title));
}
