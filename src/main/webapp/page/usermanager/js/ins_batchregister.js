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
	$("#errorList").html("");
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
	addAtrr();
	if(!validateFrom()){
		removeAtrr();
		return false;
	}else{
		var data = new FormData($('#fromList')[0]);
        var openBindStart = data.get('openBindStart');
        var openBindEnd = data.get('openBindEnd');
        if(openBindStart){
            data.set('openBindStart',openBindStart+' 00:00:00');
        }
        if(openBindEnd){
            data.set('openBindEnd',openBindEnd+' 23:59:59');
        }
        var isCheckedMe = $('#isPublishEmail').is(':checked');
        data.append('send',isCheckedMe);
		$.ajax({  
			url: '../auser/addbatchRegister.do',  
			type: 'POST',
			data: data,
			cache: false,
			timeout:1800000,
			processData: false,
			contentType: false  
		}).done(function(data){
			if(data.flag=="success"){
	    		layer.alert(data.success, {
	    			icon: 1,
	    		    skin: 'layui-layer-molv',
	    		    btn: ['确定'], //按钮
	    		    yes: function(){
	    		    	window.location.href='../group/batchregister.do';
	    		    }
	    		});
			}else if(data.flag=='fail'){
				layer.msg(data.fail, {icon: 2});
			}else if(data.flag=='error'){
	    		layer.alert(data.fail, {
	    			icon: 2,
	    		    skin: 'layui-layer-molv',
	    		    title: '提示',
	    		    btn: ['继续添加'], //按钮
	    		    yes: function(){
	    		    	window.location.href='../group/batchregister.do';
	    		    }
	    		});
			}else if(data.flag=='list'){
				showError(data.fail);
			}else{
				layer.msg("未知的系统错误，请联系管理员",{icon: 2});
			}
			removeAtrr();
		});
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