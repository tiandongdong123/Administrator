$(document).ready(function(){
	$("input[name='openState']").prop("checked",false);
	$("input[name='resourceType']").prop("checked",false);
	$("input[id='resourceInherited']").prop("checked",true);
	$("#loginMode").val("1");
	//绑定个人上限的提示
	$("#bindLimit").keyup(function(){
		check();
	})
});

//提交事件
function submitForm(type){
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
	addAtrr();
	if(!validateFrom()){
		removeAtrr();
		return false;
	}
	if(ip!="" && !IpFormat(ip)){
		layer.msg("机构账号IP段不合法，请填写规范的IP段",{icon: 2});
		removeAtrr();
		return false;
	}else if(adminIP!="" && !IpFormat(adminIP)){
		layer.msg("管理员账号IP段不合法，请填写规范的IP段",{icon: 2});
		removeAtrr();
		return false;
	}else if(ip!="" && validateIp(ip,userId,'#ipSegment')){
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
	    		    	if(type==0){
	    		    		window.location.href='../group/register.do';
	    		    	}else{
	    		    		window.location.href='../group/list.do?userId='+userId;
	    		    	}
	    		    }
	    		});
			}else{
				if(data.flag=='fail'){
					layer.msg(data.fail, {icon: 2});
				}else if(data.flag=='error'){
		    		layer.alert(data.fail, {
		    			icon: 2,
		    			title: '提示',
		    		    skin: 'layui-layer-molv',
		    		    btn: ['继续添加'], //按钮
		    		    yes: function(){
		    		    	window.location.href='../group/modify.do?userId='+userId;
		    		    }
		    		});
				}else{
					layer.msg("注册失败", {icon: 2});
				}
			}
	    	removeAtrr();
	    });
	}
}

function removeAtrr(){
	$("#submit1").removeAttr("disabled");
	$("#submit2").removeAttr("disabled");
	$("#submit3").removeAttr("disabled");
}

function addAtrr(){
	$("#submit1").attr({disabled: "disabled"});
	$("#submit2").attr({disabled: "disabled"});
	$("#submit3").attr({disabled: "disabled"});
}

//验证机构用户是否存在
function validatePeron(obj){
	var userId=$(obj).val();
	if(userId!=''){
		$.ajax({
			type:"post",
			async: false,
			url:"../auser/getPerson.do?t="+new Date(),
			data:{"userId":userId},
			dataType:"json",
			success:function(data){
				if(data.flag=="fail"){
					layer.msg(data.fail,{icon: 2});
				}
			}
		});
	}
}
// 检查IP冲突 
function checkIPError () {
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
        url: '../auser/validateip.do',  
        type: 'POST',
        data: data,
        cache: false,  
        processData: false,
        contentType: false,
        success: function(data){
			if(data.flag=="true"){
				var msg="";
				if(data.tableIP!=null){
					msg="<font style='color:red'>以下IP段存在冲突</font></br><font style='color:#000000'>"+data.errorIP+"</font><font style='color:red'>相冲突账号</font></br><font style='color:#000000'>"+data.tableIP+"</font>"
					msg += "<font style='color:red'>相冲突购买项目</font></br><font style='color:#000000'>"+data.tableProject+"</font>";
					$('#IpErrorInfo').append(msg)
				}else{
					msg="<font style='color:red'>IP格式错误:</font></br><font style='color:#000000'>"+data.errorIP+"</font>";
					$('#IpErrorInfo').append(msg)
				}
				
			}else{
				layer.tips("<font style='color:#000000'>无冲突</font></br>", "#checkIp", {
					tips: [3, '#FFFFFF'],
					area: ['350px', ''], //宽高
					closeBtn :1,
					time: 0
				});
			}
		}
    })
}
function deleteIPError() {
	layer.closeAll();
	$('#IpErrorInfo').html('')
}