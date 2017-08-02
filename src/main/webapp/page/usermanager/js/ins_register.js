$(function(){
	
});

//else if(adminIP!="" && validateIp(adminIP,adminname,'#adminIP')){return false;}
//提交事件
function submitForm(){
	var ip = $("#ipSegment").val();
	var adminIP = $("#adminIP").val();
	var userId = $("#userId").val();
	var adminname = $("#adminname").val();
	
	if(!validateFrom()){
		return false;
	}else if(!validateUserId()){
		layer.msg("该机构ID已存在",{icon: 2});
	}else if(ip!="" && !IpFormat(ip)){
		layer.msg("机构账号IP段格式有误",{icon: 2});
	}else if(adminIP!="" && !IpFormat(adminIP)){
		layer.msg("管理员IP段格式有误",{icon: 2});
	}else if(ip!="" && validateIp(ip,userId,'#ipSegment')){
		return false;
	}else{
	   var data = new FormData($('#fromList')[0]);
	    $.ajax({  
	        url: '../auser/registerInfo.do',  
	        type: 'POST',
	        data: data,
	        cache: false,  
	        processData: false,
	        contentType: false  
	    }).done(function(data){
	    	if(data.flag=="success"){
	    		layer.msg("注册成功",{icon: 1});
	    	}else{
	    		layer.msg("注册失败",{icon: 2});
	    	}
	    });
	}
}

function validatePasAndIp(){
	var loginMode = $("#loginMode").val();
	var value = false;
	if(loginMode=='1'){
		if($("#password").val().length > 0){			
			value = true;
		}
	}else if(loginMode=='0'){
		if($("#ipSegment").val().length > 0){
			value = true;
		}
	}else if(loginMode=='2'){
		if($("#ipSegment").val().length > 0&&$("#password").val().length > 0){
			value = true;
		}
	}
	return value;
}

