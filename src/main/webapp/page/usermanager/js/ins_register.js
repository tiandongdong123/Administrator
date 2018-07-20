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
    if($('#bindType').find("option:selected").val() == '2'){
        $('#fromList').bootstrapValidator('addField','email',{
            validators : {
                notEmpty : {
                    message : '请输入邮箱'
                },
                emailAddress: {/* 只需加此键值对，包含正则表达式，和提示 */
                    message: '请输入正确的邮箱地址'
                }
            }
		});
    }else{
        $('#fromList').bootstrapValidator('removeField','email');
	}
    $("#fromList").data('bootstrapValidator').resetForm();
	if(bool){
		return ;
	}
	var ip = $("#ipSegment").val();
	var adminIP = $("#adminIP").val();
	var userId = $("#userId").val();
	var adminname = $("#adminname").val();
	$("#submit").attr({disabled: "disabled"});
	if(!validateFrom()){
		$("#submit").removeAttr("disabled");
		return false;
	}
	var msg=validateUserId();
	if(msg!="true"){
		if(msg=='false'){
			layer.msg("该机构ID已存在",{icon: 2});
		}else if(msg=='old'){
			layer.msg("该机构ID在旧平台已存在",{icon: 2});
		}else if(msg=='error'){
			layer.msg("旧平台校验机构ID异常",{icon: 2});
		}
		$("#submit").removeAttr("disabled");
		return false;
	}else if(ip!="" && !IpFormat(ip)){
		layer.msg("机构账号IP段格式有误",{icon: 2});
		$("#submit").removeAttr("disabled");
		return false;
	}else if(adminIP!="" && !IpFormat(adminIP)){
		layer.msg("管理员IP段格式有误",{icon: 2});
		$("#submit").removeAttr("disabled");
		return false;
	}else if(ip!="" && validateIp(ip,userId,'#ipSegment')){
		$("#submit").removeAttr("disabled");
		return false;
	}else{
	   var data = new FormData($('#fromList')[0]);
        var openBindStart = data.get('openBindStart');
        var openBindEnd = data.get('openBindEnd');
        data.set('openBindStart',openBindStart+' 00:00:00');
        data.set('openBindEnd',openBindEnd+' 23:59:59');
        var isCheckedMe = $('#isPublishEmail').is(':checked');
        data.append('send',isCheckedMe);
	    $.ajax({  
	        url: '../auser/registerInfo.do',  
	        type: 'POST',
	        data: data,
	        cache: false,  
	        processData: false,
	        contentType: false  
	    }).done(function(data){
	    	if(data.flag=="success"){
	    		layer.alert("注册成功", {
	    			icon: 1,
	    		    skin: 'layui-layer-molv',
	    		    btn: ['确定'], //按钮
	    		    yes: function(){
	    		    	window.location.href='../auser/register.do';
	    		    }
	    		});
			}else{
				if(data.fail!=null){
					layer.msg(data.fail, {icon: 2});
				}else{
					layer.msg("注册失败", {icon: 2});
				}
			}
	    	$("#submit").removeAttr("disabled");
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

