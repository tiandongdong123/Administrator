$(document).ready(function(){
	$("#fromList").data("bootstrapValidator").updateStatus("OrderContent","NOT_VALIDATED",null);
	$('#fromList').bootstrapValidator('removeField', 'OrderContent');
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
	var type=$("#OrderType").val();
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
	        url: '../auser/updatebatchregister.do',  
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
	    		    	window.location.href='../group/batchmodify.do';
	    		    }
	    		});
			}else if(data.flag=='fail'){
				layer.msg(data.fail, {icon: 2});
			}else if(data.flag=='error'){
	    		layer.alert(data.fail, {
	    			icon: 2,
	    			title: '提示',
	    		    skin: 'layui-layer-molv',
	    		    btn: ['继续添加'], //按钮
	    		    yes: function(){
	    		    	window.location.href='../group/batchmodify.do';
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

//工单类型
function selectOrder2(obj){
	var type=$("#OrderType").val();
	var msg="";
	if(type=='crm'){
		$("#orderTypeSpan").html("CRM工单号");
		msg='CRM工单号不能为空，请填写CRM工单号';
	}else if(type=='inner'){
		$("#orderTypeSpan").html("申请部门");
		msg='申请部门不能为空，请填写申请部门';
	}else{
		$("#orderTypeSpan").html("CRM工单号");
	}
	$("#OrderContent").val("");
	if(type=='crm'||type=='inner'){
		$("#fromList").bootstrapValidator("addField","OrderContent", {
			validators: {notEmpty: {message: msg}}
		});
	}else{
		$("#fromList").data("bootstrapValidator").updateStatus("OrderContent","NOT_VALIDATED",null);
		$('#fromList').bootstrapValidator('removeField', 'OrderContent');
	}
}
//选择国家
function selectRegion2(obj){
	var region=$(obj).val();
	if(region=='foreign'){
		$("#PostCode").html('<option value="none">无</option>');
		$("#PostCode").val('none');
	}else if(region=='China'){
		$.ajax({
			type : "post",
			async:false,
			url : "../auser/getRegion.do",
			dataType : "json",
			beforeSend : function(XMLHttpRequest) {},
			success:function(data){
				var area='<option value="">--请选择--</option>';
				var arrayArea=data;
				for(var i=0;i<arrayArea.length;i++){
					area+='<option value="'+arrayArea[i].id+'">'+arrayArea[i].name+'</option>';
				}
				$("#PostCode").html(area);
			}
		});
	}else{
		$("#PostCode").html('<option value="">--请选择--</option>');
	}
	if(region=='China'){
		$("#fromList").bootstrapValidator("addField","PostCode", {
			validators: {notEmpty: {message: "地区不能为空，请选择地区"}}
		});
	}else{
		$("#fromList").data("bootstrapValidator").updateStatus("PostCode","NOT_VALIDATED",null);
		$('#fromList').bootstrapValidator('removeField', 'PostCode');
	}
}
