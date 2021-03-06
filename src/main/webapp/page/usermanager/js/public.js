var zhongZtr=null,perioZtr=null,patentZtr=null;var count = 0;
function meGetDate(time){
    var today=time ? new Date(time) : new Date();
    var year=today.getFullYear();
    var month=today.getMonth()+1;
    var day=today.getDate();
    var newTime = year + '-' +
        (month < 10? '0' + month : month) + '-' +
        (day < 10? '0' + day : day)
    return newTime;
}
$(function(e){
	$("input[name='quotaName']").prop("checked",true);
	//是否开通管理员
	$("#checkuser").click(function(){
		if($(this).is(':checked')){
			// 试用
			if($('#adminIsTrialCheck').is(':checked')) {
				$('#adminIsTrial').val('isTrial')
			}else {
				$('#adminIsTrial').val('notTrial')
			}
			$("#administrator").show();
			// 开通时限
			$("#Begintime").val(getData());
			resetAdminValidate();
		}else{
			$("#adminname").val("");
			$("#adminOldName").val("");
			$("#adminpassword").val("");
			$("#adminIP").val("");
			$("#adminEmail").val("");
			$("#administrator").hide();
			$("#checks").prop('checked',false);
			//机构子账号
			$("#upperlimit").val("");
			$("#sConcurrentcountber").val("");
			$("#downloadupperlimit").val("");
			$("#chargebacks").val("");
			$("#sconcurrent_div").hide();
			//统计分析
			$("input:checkbox[id=statistics]:checked").each(function(){
				$(this).prop('checked',false);
			});
			$("input:radio[name=managerType]").prop('checked',false);
			$("#tongji").val("");
			$("#tongjiDiv").hide();
			$("#checktongji").prop('checked',false);
			// 开通时限
			$("#adminBegintime").val('');
			// 试用
			$('#adminIsTrial').val('')
			$('#adminIsTrialCheck').prop('checked',false)
			$('#adminIsTrial').val('')
			$('#Begintime').val('')
			$('#Endtime').val('')
			$('#sIsTrialCheck').prop('checked',false)
			$('#sBegintime').val('')
			$('#sEndtime').val('')
			$("#upperlimit").val("");
			$("#sConcurrentnumber").val("");
			$("#downloadupperlimit").val("");
			$("#chargebacks").val("");
		}
	});

	//是否开通个人绑定机构
	$("#user_dinding").click(function(){
		if($(this).is(':checked')){
			$("#user_dinding").val("true");
			$("#bindAuthority").val("资源下载");
			$("#bindLimit").val("100");
			$("#bindValidity").val("180");
			$("#downlaodLimit").val("30");
			$("#dinding").show();
			$('#openBindStart1').val(meGetDate());
		}else{
			resetValidate('person')
			$("#user_dinding").val("false");
			$("#bindAuthority").val("");
			$("#resourceInherited").prop("checked",true).siblings().prop("checked",false);
			$("#bindLimit").val("1");
			$("#bindValidity").val("");
			$("#downlaodLimit").val("");
			$('#openBindStart1').val('')
			$('#openBindEnd1').val('')
			$("#dinding").hide();
		}
	})

	//设置机构账号并发数
	$("#checkp").click(function(){
		if($(this).is(':checked')){
			$("#pconcurrent_div").show();
		}else{
			$("#pConcurrentnumber").val("");
			$("#pconcurrent_div").hide();
		}
	});
	//机构管理员校验
	$("#adminIP").keyup(function(){
		this.value=this.value.replace(/[^.0-9\r\n-]/g,'');
	}).keydown(function(){
		this.value=this.value.replace(/[^.0-9\r\n-]/g,'');
	});
	//是否开通机构子账号
	$("#checks").click(function(){
		if($(this).is(':checked')){
			// 开通时限
			$("#sBegintime").val(getData())
			// 试用
			if($('#sIsTrialCheck').is(':checked')) {
				$('#sIsTrial').val('isTrial')
			}else {
				$('#sIsTrial').val('notTrial')
			}
			$("#upperlimit").val("100");
			$("#sConcurrentnumber").val("1");
			$("#downloadupperlimit").val("30");
			$("#chargebacks").val("0");
			$("#sconcurrent_div").show();
			
		}else{
			resetValidate('child')
			$("#upperlimit").val("");
			$("#sConcurrentnumber").val("");
			$("#downloadupperlimit").val("");
			$("#chargebacks").val("");
			$("#sconcurrent_div").hide();
			// 开通时限
			$("#sBegintime").val('')
			// 试用
			$('#sIsTrial').val('')
			$('#sIsTrialCheck').prop('checked',false)
			$('#sBegintime').val('')
			$('#sEndtime').val('')
		}
	});
	//添加机构管理员
	$("#newChecks").click(function(){
		if($(this).is(':checked')){
			// 开通时限
			$("#sBegintime").val(getData())
			// 试用
			if($('#sIsTrialCheck').is(':checked')) {
				$('#sIsTrial').val('isTrial')
			}else {
				$('#sIsTrial').val('notTrial')
			}
			$("#upperlimit").val("100");
			$("#sConcurrentnumber").val("1");
			$("#downloadupperlimit").val("30");
			$("#chargebacks").val("0");
			$("#sconcurrent_div").show();
			$('#newChecks').val('true')
			
		}else{
			resetValidate('child')
			$('#newChecks').val('false')
			$("#upperlimit").val("");
			$("#sConcurrentnumber").val("");
			$("#downloadupperlimit").val("");
			$("#chargebacks").val("");
			$("#sconcurrent_div").hide();
			// 开通时限
			$("#sBegintime").val('')
			// 试用
			$('#sIsTrial').val('')
			$('#sIsTrialCheck').prop('checked',false)
			$('#sBegintime').val('')
			$('#sEndtime').val('')
		}
	});
	//开通统计分析
	$("#checktongji").click(function(){
		if($(this).is(':checked')){
			var checkedList = new Array();
			$("input:checkbox[id=statistics]").each(function(){
				$(this).prop('checked',true);
				var val=$(this).val();
				if(val!='all'){
					checkedList.push(val);
				}
			});
			$("#tongji").val(checkedList.join());
			$("#tongjiDiv").show();
		}else{
			$("input:checkbox[id=statistics]:checked").each(function(){
				$(this).prop('checked',false);
			});
			$("#tongji").val("");
			$("#tongjiDiv").hide();
		}
	});
    $('#bindType').change(function(){
        if($(this).find("option:selected").val() == '2'){
            $('.qrEmail-box').show();
        }else{
            $('.qrEmail-box').hide();
        }
    });
});
//充值机构管理员的验证
function resetAdminValidate(){
	$("#fromList").data("bootstrapValidator").updateStatus("adminname","NOT_VALIDATED",null);
	$("#fromList").data("bootstrapValidator").updateStatus("adminOldName","NOT_VALIDATED",null);
	$("#fromList").data("bootstrapValidator").updateStatus("adminpassword","NOT_VALIDATED",null);
	$("#fromList").data("bootstrapValidator").updateStatus("adminEmail","NOT_VALIDATED",null);
	$("#fromList").data("bootstrapValidator").updateStatus("adminEndtime","NOT_VALIDATED",null);
}
//校验绑定个人上限
function check(){
	var reg = /^[1-9]\d*$/;
	if($("#bindLimit").val()==""){
		$(".mistaken").text("绑定个人上限不能为空，请填写正确的数字");
		style();
	}else if(!reg.test($("#bindLimit").val())){
		$(".mistaken").text("绑定个人上限是大于0的整数，请填写正确的数字");
		style()
	}else {
		$(".bind_num").css("color","#00a65a");
		$("#bindLimit").css("border-color","#00a65a");
		$(".wrong").css("background","url(../img/t.png)");
		$(".wrong").css("margin-left","10px");
		$(".wrong").css("display","inline");
		$(".mistaken").css("display","none");
	}
}

function style(){
	$(".bind_num").css("color","#dd4b39");
	$("#bindLimit").css("border-color","#dd4b39");
	$(".wrong").css("background","url(../img/f.png)");
	$(".wrong").css("margin-left","6px");
	$(".mistaken").css("display","inline");
	$(".wrong").css("display","inline");
}


//统计分析
function checkTj(obj){
	var value=$(obj).val();
	if(value=="all"){
		if($(obj).is(':checked')){
			var checkedList = new Array();
			$("input:checkbox[id=statistics]").each(function(){
				$(this).prop('checked',true);
				var val=$(this).val();
				if(val!='all'){
					checkedList.push(val);
				}
			});
			$("#tongji").val(checkedList.join());
		}else{
			$("input:checkbox[id=statistics]:checked").each(function(){
				$(this).prop('checked',false);
			});
			$("#tongji").val("");
		}
	}else{
		var checkedList = new Array();
		var obj,check=false,uncheck=false;
		$("input:checkbox[id=statistics]").each(function(){
			var val = $(this).val();
			if (val != 'all') {
				if ($(this).is(':checked')) {
					checkedList.push(val);
					check=true;
				} else {
					uncheck=true;
				}
			}else{
				obj=$(this);
			}
		});
		if(check&&!uncheck){
			obj.prop('checked',true);
		}else if(uncheck){
			obj.prop('checked',false);
		}
		$("#tongji").val(checkedList.join());
	}
	var tongji=$("#tongji").val();
	if(tongji==""){
		$("input:checkbox[id=statistics]:checked").each(function(){
			$(this).prop('checked',false);
		});
		$("#tongjiDiv").hide();
		$("#checktongji").prop('checked',false);
	}
}
//绑定个人继承权限
function bindingInherited(value){
	var all_index= $('.selFirst').length;
	var num= $('.selFirst:checked').length;
	var bindAuthority = new Array();
	if(value=="all"){
		$("input[name='resourceType']").prop("checked",$("#allInherited").prop("checked"));
	} else{
		if(all_index==num){
			$("#allInherited").prop("checked",true);
		}else {
			$("#allInherited").prop("checked",false);
		}
	}
	$("input[class='selFirst']:checked").each(function () {
		bindAuthority.push($(this).val());
	});
	$("#bindAuthority").val(bindAuthority);
}

//标准配置重置
function resetStandard(count,i){
	$("#fullIpRange_"+count+"_"+i).val("");
	$("#limitedParcelStarttime_"+count+"_"+i).val("");
	$("#limitedParcelEndtime_"+count+"_"+i).val("");
	$("#readingPrint_"+count+"_"+i).val("");
	$("#onlineVisitor_"+count+"_"+i).val("");
	$("#copyNo_"+count+"_"+i).val("");
	$("#totalPrintNo_"+count+"_"+i).val("");
	$("#singlePrintNo_"+count+"_"+i).val("");
}
//标准
function standardShow(count,i,id){
	resetStandard(count,i);
	$("#stand_div_"+count+"_"+i).hide();
	$("#isBK").hide();
	if(id=="isBK"){
		if($("#isBK_"+count+"_"+i).is(':checked')){
			$("#readingPrint_"+count+"_"+i).val("3");
			$("#onlineVisitor_"+count+"_"+i).val("-1");
			$("#copyNo_"+count+"_"+i).val("-1");
			$("#totalPrintNo_"+count+"_"+i).val("-1");
			$("#singlePrintNo_"+count+"_"+i).val("-1");
			$("#isBK").show();
			$("#stand_div_"+count+"_"+i).show();
		}
	}
}

//验证期刊分类号和期刊ID
function checkPerio(count,i){
	var value=$("#perioInfoClc_"+count+"_"+i).val();
	if(value!=null&&value!=""){
		value=value.toUpperCase();
		var reg = /^[A-Z0-9,]+$/;
		if(!reg.test(value)){
			$("#perioMsg_"+count+"_"+i).html('<font style="color:red">期刊分类输入格式不正确</font>');
			return false;
		}else{
			$("#perioInfoClc_"+count+"_"+i).val(value);
		}
	}
	var value2=$("#journalIdno_"+count+"_"+i).val();
	if(value2!=null&&value2!=""){
		var reg2 = /^[a-zA-Z0-9\n\r,_-]+$/;
		if(!reg2.test(value2)){
			$("#perioMsg_"+count+"_"+i).html('<font style="color:red">期刊ID输入格式不正确</font>');
			return false;
		}
	}
	$("#perioMsg_"+count+"_"+i).html('');
	return true;
}

//验证会议馆藏号
function checkConf(count,i){
	var value=$("#conferenceNo_"+count+"_"+i).val();
	if(value==null||value==""){
		$("#confMsg_"+count+"_"+i).html('');
		return true;
	}
	var reg = /^[a-zA-Z0-9\n\r,-]+$/;
	if(!reg.test(value)){
		$("#confMsg_"+count+"_"+i).html('<font style="color:red">会议论文集馆藏号输入格式不正确</font>');
		return false;
	}else{
		$("#confMsg_"+count+"_"+i).html('');
		return true;
	}
}

//验证专利RPC分类
function checkPatent(count,i){
	var value=$("#patentIpc_"+count+"_"+i).val();
	if(value==null||value==""){
		$("#patentMsg_"+count+"_"+i).html('');
		return true;
	}
	value=value.toUpperCase();
	var reg = /^[A-Z0-9,]+$/;
	if(!reg.test(value)){
		$("#patentMsg_"+count+"_"+i).html('<font style="color:red">IPC分类输入格式不正确</font>');
		return false;
	}else{
		$("#patentIpc_"+count+"_"+i).val(value);
		$("#patentMsg_"+count+"_"+i).html('');
		return true;
	}
}
//期刊年份限定
function validPerioYear(count,i){
	return perioYear(-1,count,i);
}
function perioYear(value,count,i){
	var startTime=$("#journal_startTime_"+count+"_"+i).val();
	var endTime=$("#journal_endTime_"+count+"_"+i).val();
	if(value==0){
		if(startTime!=''){
			if(endTime==''){
				var myDate = new Date();
				$("#journal_endTime_"+count+"_"+i).val(myDate.getFullYear());
			}
		}else{
			$("#journal_endTime_"+count+"_"+i).val("");
		}
	}else if(value==1){
		if(endTime!=''){
			if(startTime==''){
				$("#journal_startTime_"+count+"_"+i).val("1900");
			}
		}else{
			$("#journal_startTime_"+count+"_"+i).val("");
		}
	}
	if(parseInt(startTime)>parseInt(endTime)){
		$("#perioMsg_"+count+"_"+i).html('<font style="color:red">年限格式不对，结束时间应大于开始时间</font>');
		return false;
	}else{
		$("#perioMsg_"+count+"_"+i).html('');
	}
	return true;
}
//学位年份限定
function validDegreeYear(count,i){
	return degreeYear(-1,count,i);
}
function degreeYear(value,count,i){
	var startTime=$("#degreeStarttime_"+count+"_"+i).val();
	var endTime=$("#degreeEndtime_"+count+"_"+i).val();
	if(value==0){
		if(startTime!=''){
			if(endTime==''){
				var myDate = new Date();
				$("#degreeEndtime_"+count+"_"+i).val(myDate.getFullYear());
			}
		}else{
			$("#degreeEndtime_"+count+"_"+i).val("");
		}
	}else if(value==1){
		if(endTime!=''){
			if(startTime==''){
				$("#degreeStarttime_"+count+"_"+i).val("1900");
			}
		}else{
			$("#degreeStarttime_"+count+"_"+i).val("");
		}
	}
	if(parseInt(startTime)>parseInt(endTime)){
		$("#degreeMsg_"+count+"_"+i).html('<font style="color:red">年限格式不对，结束时间应大于开始时间</font>');
		return false;
	}else{
		$("#degreeMsg_"+count+"_"+i).html('');
	}
	return true;
}

//校验标准内容
function validStandard(count,i){
	//1、时间不能为空，而且还是时间要小于等于结束时间
	var isBK=$("#isBK_"+count+"_"+i).is(':checked');
	return true;
}
//字符串转化为日期
function StringToDate(dateStr){
     var separator="-";
     var dateArr = dateStr.split(separator);
     var year = parseInt(dateArr[0]);
     var month;
     //处理月份为04这样的情况                         
     if(dateArr[1].indexOf("0") == 0){
         month = parseInt(dateArr[1].substring(1));
     }else{
          month = parseInt(dateArr[1]);
     }
     var day = parseInt(dateArr[2]);
     var date = new Date(year,month -1,day);
     return date;
 }

//是否试用
function checkMode(num){
	if($("#pro_mode_"+num).is(':checked')){
		var nextDay=$("#nextDay_"+num).val();
		if(nextDay!=undefined &&nextDay!=null){
			var time=$("input[name='rdlist["+num+"].validityEndtime']").val();
			if(time!=undefined &&time!=null&&StringToDate(nextDay)<=StringToDate(time)){
				$("#pro_mode_"+num).prop('checked',false);
				layer.msg("购买项目没有过有效期，不能转为试用",{icon: 2,time: 2000});
				return false;
			}
		}
	}
	if($("#pro_mode_"+num).is(':checked')){
		$("input[name='rdlist["+num+"].mode']").val('trical');
	}else{
		$("input[name='rdlist["+num+"].mode']").val('formal');
	}
}

//登录方式切换
function switchcs(obj){
	$("#ipSegment").val("");
	$("#password").val("");
	if($(obj).val()=="1"){
		$("#upass").show().siblings().hide();
	}else if($(obj).val()=="0"){
		$("#ipvalue").show().siblings().hide();
		$('.IPAutomatic').show().siblings().hide();
	}else{
		$("#upass").show();
		$("#ipvalue").show();
		$('.IPAutomatic').show();
	}
	if($(obj).val()=="0"){
		$("#checkIp").show();
		$("#deleteIp").show();
	}else{
		$("#checkIp").hide();
		$("#deleteIp").hide();
	}
	if ($(obj).val() != "1") {
		$("#wechatDiv").hide();
		$("#openAppDiv").hide();
		$("#sendMail").prop("checked", false);
		$("#openApp").prop("checked", false);
		$("#openWeChat").prop("checked", false);
		$("#appBegintime").val("");
		$("#appEndtime").val("");
		$("#weChatBegintime").val("");
		$("#weChatEndtime").val("");
		$("#weChatEamil").val("");
	}
	$("#openWeChatspan").html("");
	$("#openAppspan").html("");
	$("#fromList").data('bootstrapValidator').resetForm();
}

//全选和取消
function checkedall(count){
	if($('#checkbox_id_'+count).is(':checked')){
		$("input[class='rdlist["+count+"].tableName']").each(function(){
			$(this).prop("checked", "true");
		});
	}else{
		$("input[class='rdlist["+count+"].tableName']").each(function(){
			$(this).removeAttr("checked");
		});
	}
}

function openPurchaseItems(count,i,type){
	$("#tabs_custom_"+count+"_"+i).find("li").removeClass("active");
	if(type.indexOf("perio")>-1){
		$("a[href='#perio_"+count+"_"+i+"']").parent().addClass("active");
		$("#perio_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("degree")>-1){
		$("a[href='#degree_"+count+"_"+i+"']").parent().addClass("active");
		$("#degree_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("conf")>-1){
		$("a[href='#conf_"+count+"_"+i+"']").parent().addClass("active");
		$("#conf_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("patent")>-1){
		$("a[href='#patent_"+count+"_"+i+"']").parent().addClass("active");
		$("#patent_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("books")>-1){
		$("a[href='#book_"+count+"_"+i+"']").parent().addClass("active");
		$("#book_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("standard")>-1){
		$("a[href='#standard_"+count+"_"+i+"']").parent().addClass("active");
		$("#standard_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("local chronicles")>-1){
		$("a[href='#localchronicles_"+count+"_"+i+"']").parent().addClass("active");
		$("#localchronicles_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}
	if(type.indexOf("standard")>-1){
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(validStandard(count,i)){
		    		layer.closeAll();
		    	}
		    }
		});
	}else if(type.indexOf("perio")>-1){
		$("#perioMsg_"+count+"_"+i).html("");
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(checkPerio(count,i)&&validPerioYear(count,i)){
		    		layer.closeAll();
		    	}
		    },
		    cancel: function(){
		    	if(!checkPerio(count,i)||!validPerioYear(count,i)){
		    		return false;
		    	}
		    }
		});
	}else if(type.indexOf("conf")>-1){
		$("#confMsg_"+count+"_"+i).html("");
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(checkConf(count,i)){
		    		layer.closeAll();
		    	}
		    },
		    cancel: function(){
		    	if(!checkConf(count,i)){
		    		return false;
		    	}
		    }
		});
	}else if(type.indexOf("patent")>-1){
		$("#patentMsg_"+count+"_"+i).html("");
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(checkPatent(count,i)){
		    		layer.closeAll();
		    	}
		    },
		    cancel: function(){
		    	if(!checkPatent(count,i)){
		    		return false;
		    	}
		    }
		});
	}else if(type.indexOf("degree")>-1){
		$("#degreeMsg_"+count+"_"+i).html("");
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(validDegreeYear(count,i)){
		    		layer.closeAll();
		    	}
		    },
		    cancel: function(){
		    	if(!validDegreeYear(count,i)){
		    		return false;
		    	}
		    }
		});
	}else if(type.indexOf("local")>-1){
		initGazetteer(count,i);
		$('#gazetteersId_'+count+'_'+i).val($('#gazetteers_TopId_'+count+'_'+i).val())
		$('#itemId_'+count+'_'+i).val($('#gazetteers_BottomItem_'+count+'_'+i).val())
		if($('#gazetteersId_'+count+'_'+i).val()!=''||$('#itemId_'+count+'_'+i).val()!='') {
			$('#changeSelect_'+count+'_'+i).hide()
			$('#changeTextarea_'+count+'_'+i).show()
			$('#localchronicles_'+count+'_'+i+ ' .selectType input').prop('checked',false)
			$('#localchronicles_'+count+'_'+i+ ' .export input').prop('checked',true)
		}else {
			$('#localchronicles_'+count+'_'+i+ ' .selectType input').prop('checked',true)
		}
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '地方志限定',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(){
				if($('#errorTime_'+count+'_'+i).text()==''&&$('#errorOldTime_'+count+'_'+i).text()==''&&$('.checkType_'+count+'_'+i).text()=='') {
					if($('#newLocalType_'+count+"_"+i).is(':checked')) {
						var startTime = $('#startTime_'+count+"_"+i).attr('data-starttime'),
							endTime = $('#endTime_'+count+"_"+i).attr('data-endtime'),
							prev = $('#sheng_'+count+"_"+i).attr('data-prev'),
							city = $('#shi_'+count+"_"+i).attr('data-city'),
							county = $('#xian_'+count+"_"+i).attr('data-county'),
							dataType = $('.newDateSelect').attr('data-select'),
							dataClassify = $('.newClassify_'+count+"_"+i).attr('data-classify');
							
						dataType = (dataType=='')?'WFLocalChronicle':dataType;
						prev = (prev=='')?'':prev+'_';
						city = (city=='')?'':city+'_';
						county = (county=='')?'':county+'_';
						
						var Area = prev+city+county;
						Area = Area.substr(0,Area.length-1);
						
						$('#gazetteersStartTime_'+count+"_"+i).val(startTime) //新方志开始时间
						$('#gazetteersEndTime_'+count+"_"+i).val(endTime) // 新方志结束时间
						$('#gazetteersArea_'+count+"_"+i).val(Area) // 新方志地区
						$('#gazetteersAlbum_'+count+"_"+i).val(dataClassify) // 新方志专辑分类
						$('#gazetteersLevel_'+count+"_"+i).val(dataType) // 新方志数据分类
						$('#gazetteers_type_'+count+"_"+i).val('FZ_New')
					}else {
						gazetteerType('FZ_New',count,i)
						$('#gazetteers_type_'+count+"_"+i).val('')
						$('#gazetteersStartTime_'+count+'_'+i).val('')
						$('#gazetteersEndTime_'+count+'_'+i).val('')
						$('#gazetteersAlbum_'+count+'_'+i).val('')
						$('#gazetteersArea_'+count+'_'+i).val('')
					} 
					if($('#oldLocalType_'+count+"_"+i).is(':checked')) {
						var oldStartTime = $('#old_startTime_'+count+"_"+i).attr('data-oldstarttime'),
							oldEndTime = $('#old_endTime_'+count+"_"+i).attr('data-oldendtime'),
							oldprev = $('#o_sheng_'+count+"_"+i).attr('data-oldprev'),
							oldcity = $('#o_shi_'+count+"_"+i).attr('data-oldcity'),
							oldcounty = $('#o_xian_'+count+"_"+i).attr('data-oldcounty');
						oldprev = (oldprev=='')?'':oldprev+'_';
						oldcity = (oldcity=='')?'':oldcity+'_';
						oldcounty = (oldcounty=='')?'':oldcounty+'_';
						var oldArea = oldprev+oldcity+oldcounty;
						oldArea = oldArea.substr(0,oldArea.length-1);
						$('#gazetteersOldArea_'+count+"_"+i).val(oldArea) // 旧方志地区
						$('#gazetteersOldStartTime_'+count+"_"+i).val(oldStartTime) // 旧方志开始时间
						$('#gazetteersOldEndTime_'+count+"_"+i).val(oldEndTime) // 旧方志结束时间
						$('#gazetteers_type_'+count+"_"+i).val('FZ_Old')
					}else {
						gazetteerType('FZ_Old',count,i)
						$('#gazetteers_type_'+count+"_"+i).val('')
						$('#gazetteersOldStartTime_'+count+'_'+i).val('')
						$('#gazetteersOldEndTime_'+count+'_'+i).val('')
						$('#gazetteersOldArea_'+count+'_'+i).val('')
					}
					
					if($('#newLocalType_'+count+"_"+i).is(':checked')) {
						$('#gazetteers_type_'+count+"_"+i).val('FZ_New')
					}
					if ($('#oldLocalType_'+count+"_"+i).is(':checked')) {
						$('#gazetteers_type_'+count+"_"+i).val('FZ_Old')
					}
					if($('#newLocalType_'+count+"_"+i).is(':checked')&&$('#oldLocalType_'+count+"_"+i).is(':checked')) {
						var strVal = 'FZ_New;FZ_Old'
						$('#gazetteers_type_'+count+"_"+i).val(strVal)
						var oldStartTime = $('#old_startTime_'+count+"_"+i).attr('data-oldstarttime'),
							oldEndTime = $('#old_endTime_'+count+"_"+i).attr('data-oldendtime'),
							oldprev = $('#o_sheng_'+count+"_"+i).attr('data-oldprev'),
							oldcity = $('#o_shi_'+count+"_"+i).attr('data-oldcity'),
							oldcounty = $('#o_xian_'+count+"_"+i).attr('data-oldcounty');
						oldprev = (oldprev=='')?'':oldprev+'_';
						oldcity = (oldcity=='')?'':oldcity+'_';
						oldcounty = (oldcounty=='')?'':oldcounty+'_';
						var oldArea = oldprev+oldcity+oldcounty;
						oldArea = oldArea.substr(0,oldArea.length-1);
						$('#gazetteersOldArea_'+count+"_"+i).val(oldArea) // 旧方志地区
						$('#gazetteersOldStartTime_'+count+"_"+i).val(oldStartTime) // 旧方志开始时间
						$('#gazetteersOldEndTime_'+count+"_"+i).val(oldEndTime) // 旧方志结束时间
					}
					
					if((!$('#newLocalType_'+count+"_"+i).is(':checked'))&&(!$('#oldLocalType_'+count+"_"+i).is(':checked'))) {
						$('#gazetteers_type_'+count+"_"+i).val('')
						gazetteerType('FZ_New',count,i)
						gazetteerType('FZ_Old',count,i)
						$('#gazetteersArea_'+count+'_'+i).val('')
						$('#gazetteersAlbum_'+count+'_'+i).val('')
						$('#gazetteersLevel_'+count+'_'+i).val('')
						$('#gazetteersOldArea_'+count+'_'+i).val('')
						$('#gazetteersStartTime_'+count+'_'+i).val('')
						$('#gazetteersEndTime_'+count+'_'+i).val('')
						$('#gazetteersOldStartTime_'+count+'_'+i).val('')
						$('#gazetteersOldEndTime_'+count+'_'+i).val('')
					}
					
					if($("#localchronicles_"+count+"_"+i+" .selectType input").is(':checked')) {
						$('#gazetteersId_'+count+'_'+i).val('')
						$('#gazetteersId_'+count+'_'+i).text('')
						$('#itemId_'+count+'_'+i).val('')
						$('#itemId_'+count+'_'+i).text('')
						$('#gazetteers_TopId_'+count+'_'+i).val('')
						$('#gazetteers_BottomItem_'+count+'_'+i).val('')
					}else {
						$('#gazetteers_TopId_'+count+'_'+i).val($('#gazetteersId_'+count+'_'+i).val())
						$('#gazetteers_BottomItem_'+count+'_'+i).val($('#itemId_'+count+'_'+i).val())
					}
					if ($("#databaseInput_"+count+"_"+i).is(':checked')) {
						var databaseVal = $('#databaseInput_'+count+'_'+i).attr('data-database')
						$('#gazetteers_albumDatabase_'+count+'_'+i).val(databaseVal)
					}else {
						var databaseVal = $('#databaseInput_'+count+'_'+i).attr('data-database')
						$('#gazetteers_albumDatabase_'+count+'_'+i).val(databaseVal)
					}
					layer.closeAll();				
				}
//				$('.checkType_'+count+'_'+i).text('')
				if(!$("#localchronicles_"+count+"_"+i+" .selectType input").is(':checked')) {
					$('#errorTime_'+count+'_'+i).text('')
					$('#errorOldTime_'+count+'_'+i).text('')
					layer.closeAll();	
				}
		    },
		    cancel: function() {
		    	$('#errorTime_'+count+'_'+i).html('')
				$('#errorOldTime_'+count+'_'+i).html('')
				$('#gazetteersId_'+count+'_'+i).val('')
				$('#itemId_'+count+'_'+i).val('')
				$('.checkType_'+count+'_'+i).text('')
				layer.closeAll();					
		    }
		});
	}else{
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(){
		        layer.closeAll();
		    },
		});
	} 
}

//购买项目div样式切换
function selectProject(obj,flag,checked){
	var proname = $(obj).find("option:selected").text();
	var count = parseInt($("#count").val())+1;
	var projectid = $(obj).find("option:selected").attr("id");
	var resourceType = $(obj).find("option:selected").attr("type");
	var proid = $(obj).find("option:selected").attr("proid");
	checked = checked==""?"":"checked='checked'"
	if(projectid.indexOf($("#pro_"+projectid).val())<0){
		//去除余额转限时和限时转化余额度功能
		if(projectid=="GTimeLimit"||projectid=="GBalanceLimit"){
			var proLimit1=$("#pro_GBalanceLimit").val();
			var proLimit2=$("#pro_GTimeLimit").val();
			if(proLimit1!=undefined &&proLimit1!=null ||proLimit2!=undefined &&proLimit2!=null){
				$("#buttonshow").hide();
				$("#buttonshow").next().css("margin-left","1000px");
			}
		}
		var text = '';
		if($(obj).val()!=""){
			text += '<div class="balance_block" name="full_div">';
			text += '<div class="resources_title"><input type="hidden" name="rdlist['+count+'].projectid" id="pro_'+projectid+'" value="'+projectid+'"><span>'+proname+'</span>';
			text += '<input type="hidden" name="rdlist['+count+'].projectname" value="'+proname+'"><input type="hidden" name="rdlist['+count+'].projectType" value="'+$(obj).val()+'">';
			text += '<span class="front_apan"><input type="hidden" name="rdlist['+count+'].mode" ><input type="checkbox" id="pro_mode_'+count+'" onclick="checkMode('+count+')">试用</span><span style="float:right;margin-right:150px;"><button type="button" class="btn btn-primary btn-sm" onclick="delDiv(this,\''+count+'\',1);">删除</button></span></div>';
			text += '<div class="time_block"><div class="time_input">';
			text += '<span><b>*</b>时限</span><input type="text" class="Wdate" value="'+getData()+'" name="rdlist['+count+'].validityStarttime" id="'+projectid+'_st" onclick="WdatePicker({maxDate:\'#F{$dp.$D('+projectid+'_et)}\'})"/>';
			text += '<span class="to">至</span><input type="text" class="Wdate" name="rdlist['+count+'].validityEndtime" id="'+projectid+'_et" onclick="WdatePicker({minDate:\'#F{$dp.$D('+projectid+'_st)}\'})"></div>';
			if($(obj).val()!="time"&&$(obj).val()!="count"&&flag!='2'){
				text += '<div class="time_input time_money"><span><b>*</b>金额</span><input type="text" name="rdlist['+count+'].totalMoney" onkeyup="checkMoney(this,0);" onafterpaste="checkMoney(this,0);"></div>';
			}
			if($(obj).val()!="time"&&$(obj).val()!="balance"&&flag!='2'){
				text += '<div class="time_input time_money"><span><b>*</b>次数</span><input type="text" name="rdlist['+count+'].purchaseNumber" onkeyup="checkNum(this,0);" onafterpaste="checkNum(this,0);"></div>';
			}
			if(projectid=='HistoryCheck'){
				text += '<p><div class="time_input"><span><b>*</b>查看交易信息</span> ';
				text += '<input type="radio" name="rdlist['+count+'].relatedIdAccountType" checked="checked" value="ViewHistoryCheck"/>可以<input type="radio" name="rdlist['+count+'].relatedIdAccountType" value="not"/>不可以</div>';
			}
			text += '</div>';
			if($(obj).val()!="count" && resourceType !="service"){
				text += '<div class="balance_list"><div class="checkbox_check"><div><b>*</b>选择数据库</div>';
				text += '<div><input type="checkbox" id="checkbox_id_'+count+'" '+checked+' onclick="checkedall('+count+');">全选</div></div>';
				text += '<ul class="checkbox_list">';
				$.ajax({
					type : "post",
					url : "../auser/getdata.do",
					data:{proid:proid},
					async: false,
					success: function(data){
						for(var i in data){
						var name;
						if(data[i].abbreviation==undefined){
							name = data[i].tableName;
						}else{
							name = data[i].abbreviation;
						}
						var type = data[i].resType;
						var code = data[i].productSourceCode;
						var rp = data[i].rp;
						if(type.indexOf("perio")>-1||type.indexOf("degree")>-1||type.indexOf("conf")>-1||type.indexOf("patent")>-1||type.indexOf("books")>-1||type.indexOf("standard")>-1||type.indexOf("local chronicles")>-1){						
							text += '<li><input type="checkbox" '+checked+' value="'+code+'" onclick="checkRes('+count+','+i+')" name="rdlist['+count+'].rldto['+i+'].resourceid" class="rdlist['+count+'].tableName" id="resourceid_'+count+'_'+i+'">';
							if(rp.length > 0){
								text += '<i onclick="showProduct(this,1)" class="icon_minus"></i>';
							}
							text += name;
							if('IsticBalanceLimit'!=projectid){
								text += '<a href="javascript:void(0);" onclick="openPurchaseItems(\''+count+'\',\''+i+'\',\''+type+'\');">详情</a>';
							}
							text += '<ul style="display: none;" class="checkbox_list subset_list">';
							for(var n in rp){									
								text += '<li><input type="checkbox" '+checked+' name="rdlist['+count+'].rldto['+i+'].productid" value="'+rp[n].rid+'" class="rdlist['+count+'].tableName">'+rp[n].name+'</li>';
							}
							text += '</ul></li>';
							if('IsticBalanceLimit'!=projectid&&($(obj).val()=="time"||$(obj).val()=="balance")){
								createDetail(count,i,code,type);
							}
						}else{
							text += '<li><input type="checkbox" '+checked+' value="'+code+'" onclick="checkRes('+count+','+i+')" name="rdlist['+count+'].rldto['+i+'].resourceid" class="rdlist['+count+'].tableName" id="resourceid_'+count+'_'+i+'">';
							if(rp.length > 0){									
								text += '<i onclick="showProduct(this,2)" class="icon_minus"></i>';
							}
							text += name;
							text += '<ul style="display: none;" class="checkbox_list subset_list">';
							for(var n in rp){									
								text += '<li><input type="checkbox" '+checked+' name="rdlist['+count+'].rldto['+i+'].productid" value="'+rp[n].rid+'" class="rdlist['+count+'].tableName">'+rp[n].name+'</li>';
							}
							text += '</ul></li>';
						}
					}
				}
			});
				text += '</ul></div>';
			}
			text += '</div>';
		}else{
			$("#multplediv").html("");
			$("#detail_0").html("");
		}
		$("#count").val(count);
		$("#multplediv").prepend(text);
	}
}

//选中产品的时候也要选中产品对应的id
function checkRes(count,i){
	if($('#resourceid_'+count+'_'+i).is(':checked')){
		$("input[name='rdlist["+count+"].rldto["+i+"].productid']").each(function(){
			$(this).prop("checked", "true");
		});
	}else{
		$("input[name='rdlist["+count+"].rldto["+i+"].productid']").each(function(){
			$(this).removeAttr("checked");
		});
	}
}


//获取当前时间，放入时限起始时间
function getData(){
	var now 
	$.ajax({
		type : "get",
		async: false,
		url : "../auser/getServerTime.do",
		success:function(data){
			now = data
		}
	})
	return now
}

function getFormatDate(s){
    return s < 10 ? '0' + s: s;
}


//产品信息显示/隐藏切换
function showProduct(obj,falg){
	if(falg=='1'){
		$(obj).attr("class","icon_minus");
		$(obj).siblings("ul").hide();
		$(obj).attr("onclick","showProduct(this,2)");
	}else{
		$(obj).attr("class","icon_add");
		$(obj).siblings("ul").show();
		$(obj).attr("onclick","showProduct(this,1)");
	}
}

//删除项目
function delDiv(obj,count,flag,payChannelid,type,beginDateTime,endDateTime,institution,userId,projectname,balance,mode){
	layer.alert('确定删除该项目吗？',{
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'],
	    yes: function(){
	    	if(flag!='1'){
	    		if($("#multplediv div[name='full_div']").length < 2){
	    			layer.msg('购买项目不可全部删除', {icon: 2});
	    		}else{
					var json="{'payChannelid':'"+payChannelid+"','type':'"+type+"','beginDateTime':'"+beginDateTime;
		    		json+="','endDateTime':'"+endDateTime+"','institution':'"+institution+"','mode':'"+mode;
		    		json+="','userId':'"+userId+"','balance':'"+balance+"','projectname':'"+projectname+"'}";
		    		$(obj).parents(".balance_block").remove();
		    		$("div[name='tabs_"+count+"']").remove();
		    		$("#multplediv").append('<input type="hidden" name="rdlist['+count+'].projectid" value="'+json+'">');
		    		
	    			layer.closeAll();
	    		}
	    	}else{
	    		$(obj).parents(".balance_block").remove();
	    		$("div[name='tabs_"+count+"']").remove();
	    		layer.closeAll();
	    		if($("#multplediv div[name='full_div']").length<=0){
	    			$("#resourcePurchaseType").get(0).selectedIndex=0;
	    		}
	    	}
	    }
	});
}

//创建购买资源库及详情信息
function createDetail(count,i,resourceid,type){
	var text = '';
	text += '<div class="nav-tabs-custom" id="tabs_custom_'+count+'_'+i+'" name="tabs_'+count+'" style="display: none;">';
	text += '<ul class="nav nav-tabs">';
	if(type.indexOf("perio")>-1){
		text += '<li><a href="#perio_'+count+'_'+i+'" data-toggle="tab">期刊限定</a></li>';
	}
	if(type.indexOf("degree")>-1){
		text += '<li><a href="#degree_'+count+'_'+i+'" data-toggle="tab">学位限定</a></li>';
	}
	if(type.indexOf("conf")>-1){
		text += '<li><a href="#conf_'+count+'_'+i+'" data-toggle="tab">会议限定</a></li>';
	}
	if(type.indexOf("patent")>-1){
		text += '<li><a href="#patent_'+count+'_'+i+'" data-toggle="tab">专利限定</a></li>';
	}
	if(type.indexOf("books")>-1){
		text += '<li><a href="#book_'+count+'_'+i+'" data-toggle="tab">图书限定</a></li>';
	}
	if(type.indexOf("standard")>-1){
		text += '<li><a href="#standard_'+count+'_'+i+'" data-toggle="tab">标准限定</a></li>';
	}
	/*if(type.indexOf("local chronicles")>-1){
		text += '<li><a href="#localchronicles_'+count+'_'+i+'" data-toggle="tab">地方志限定</a></li>';
	}*/
	text += '</ul><div class="tab-content">';
	if(type.indexOf("perio")>-1){
		text += '<div class="tab-pane" id="perio_'+count+'_'+i+'">';
		text +='<button id="button0_'+count+'_'+i+'" onclick="changePerioClc(\'_'+count+'_'+i+'\',0)" type="button" class="btn btn-primary btn-sm">选刊</button>';
		text +='<button id="button1_'+count+'_'+i+'" onclick="changePerioClc(\'_'+count+'_'+i+'\',1)" type="button" class="btn btn-primary btn-sm btn-success2">选文献</button>';
		text +='<div id="perioInfoDiv_'+count+'_'+i+'"><label>期刊分类</label><ul class="ztree" id="perioInfoZtree_'+count+'_'+i+'"></ul>';
        text +='<textarea placeholder="格式：A,B,C" placeholder="格式：A,B,C" class="form-control" name="rdlist['+count+'].rldto['+i+'].perioInfoClc" id="perioInfoClc_'+count+'_'+i+'" onblur="checkPerio('+count+','+i+')"></textarea></div>';
		text += '<div id="perioDiv_'+count+'_'+i+'" style="display:none;"><label>中图分类</label><ul class="ztree" id="perioZtree_'+count+'_'+i+'"></ul>';
		text += '<textarea placeholder="格式：A,B,C" class="form-control" name="rdlist['+count+'].rldto['+i+'].journalClc" id="journalClc_'+count+'_'+i+'"></textarea></div>';
		text += '<div style="width:60%;" id="journalIdDiv_'+count+'_'+i+'">';
		text += '<label>期刊ID(支持输入格式:换行、英文逗号分隔)</label>';
		text += '<textarea class="form-control" rows="10" name="rdlist['+count+'].rldto['+i+'].journalIdno" id="journalIdno_'+count+'_'+i+'" onblur="checkPerio('+count+','+i+')"></textarea></div>';
		text += '<label>年限</label>';
		text += '<div class="time_block">';
		text += '<select name="rdlist['+count+'].rldto['+i+'].journal_startTime" id="journal_startTime_'+count+'_'+i+'" onchange="perioYear(0,'+count+','+i+')"></select>年——';
		text += '<select name="rdlist['+count+'].rldto['+i+'].journal_endTime" id="journal_endTime_'+count+'_'+i+'" onchange="perioYear(1,'+count+','+i+')"></select>年';
		text += '</div><div id="perioMsg_'+count+'_'+i+'"></div></div>';
	}
	if(type.indexOf("degree")>-1){
		text += '<div class="tab-pane" id="degree_'+count+'_'+i+'">';
		text += '<label>中图分类</label><ul class="ztree" id="degreeZtree_'+count+'_'+i+'"></ul>	';
		text += '<textarea placeholder="格式：A,B,C" class="form-control" name="rdlist['+count+'].rldto['+i+'].degreeClc" id="degreeClc_'+count+'_'+i+'"></textarea>';
		text += '<div class="form-group" style="width:60%;">';
		text += '<label>论文类型</label>';
		text += '<div style="height:50px;border:1px solid #C6C6C6;">';
		text += '<input type="checkbox" name="rdlist['+count+'].rldto['+i+'].degreeTypes" value="硕士">硕士论文';
		text += '<input type="checkbox" name="rdlist['+count+'].rldto['+i+'].degreeTypes" value="博士">博士论文';
		text += '<input type="checkbox" name="rdlist['+count+'].rldto['+i+'].degreeTypes" value="博士后">博士后论文';
		text += '</div></div>';
		text += '<label>论文年限</label>';
		text += '<div class="time_block">';
		text += '<select id="degreeStarttime_'+count+'_'+i+'" name="rdlist['+count+'].rldto['+i+'].degreeStarttime" onchange="degreeYear(0,'+count+','+i+')"></select>年——';
		text += '<select id="degreeEndtime_'+count+'_'+i+'" name="rdlist['+count+'].rldto['+i+'].degreeEndtime" onchange="degreeYear(1,'+count+','+i+')"></select>年';
		text += '</div><div id="degreeMsg_'+count+'_'+i+'"></div></div>';
	}
	if(type.indexOf("conf")>-1){
		text += '<div class="tab-pane" id="conf_'+count+'_'+i+'">';
		text += '<label>中图分类</label><ul class="ztree" id="confZtree_'+count+'_'+i+'"></ul>';
		text += '<textarea placeholder="格式：A,B,C" class="form-control" name="rdlist['+count+'].rldto['+i+'].conferenceClc" id="conferenceClc_'+count+'_'+i+'"></textarea>';
		text += '<div class="form-group" style="width:60%;">';
		text += '<label>会议论文集馆藏号(支持输入格式:换行、英文逗号分隔)</label>';
		text += '<textarea class="form-control" rows="6" name="rdlist['+count+'].rldto['+i+'].conferenceNo" id="conferenceNo_'+count+'_'+i+'" onblur="checkConf('+count+','+i+')"></textarea>';
		text += '</div></div><div id="confMsg_'+count+'_'+i+'"></div>';
	}
	if(type.indexOf("patent")>-1){
		text += '<div class="tab-pane" id="patent_'+count+'_'+i+'">';
		text += '<div style="padding: 10px;">';
		text += '<div class="wrap">';
		text += '<label>IPC分类</label><ul class="ztree" id="patentZtree_'+count+'_'+i+'"></ul>';
		text += '<textarea placeholder="格式：A,B,C" class="form-control" name="rdlist['+count+'].rldto['+i+'].patentIpc" id="patentIpc_'+count+'_'+i+'" onblur="checkPatent('+count+','+i+')"></textarea>';
		text += '</div></div><div id="patentMsg_'+count+'_'+i+'"></div></div>';
	}
	if(type.indexOf("books")>-1){
		text += '<div class="tab-pane" id="book_'+count+'_'+i+'">';
		text += '<label>中图分类</label><ul class="ztree" id="bookZtree_'+count+'_'+i+'"></ul>';
		text += '<textarea placeholder="格式：A,B,C" class="form-control" name="rdlist['+count+'].rldto['+i+'].booksClc" id="booksClc_'+count+'_'+i+'"></textarea>';
		text += '<div class="form-group" style="width:60%;">';
		text += '<label>图书ID</label>';
		text += '<textarea class="form-control" rows="3" name="rdlist['+count+'].rldto['+i+'].booksIdno" id="booksIdno_'+count+'_'+i+'"></textarea>';
		text += '</div></div>';
	}
	if(type.indexOf("standard")>-1){
		text += '<div class="tab-pane" id="standard_'+count+'_'+i+'"><div class="form-group input_block">';
		text += '<label><input type="checkbox" name="rdlist['+count+'].rldto['+i+'].standardTypes" value="WFLocal">行业标准</label>';
		text += '<label><input type="checkbox" name="rdlist['+count+'].rldto['+i+'].standardTypes" id="isBK_'+count+'_'+i+'" onclick="standardShow('+count+','+i+',\'isBK\');" value="质检出版社">网络包库(质检)</label></div>'
		text += '<div style="display:none;" id="stand_div_'+count+'_'+i+'">';
	}
	
	if(type.indexOf("local chronicles")>-1){
		var selectText =''
		text += '<div class="tab-pane" id="localchronicles_'+count+'_'+i+'">';
		text += '<div class="limit">';
		text += '<div class="limitTab">';
		text += '<span class="selectType"><input type="radio" name="fzlx" value="1" onclick="getSxdr(1,'+count+','+i+')" checked/>分类筛选</span>';
		text += '<span class="export"><input type="radio" name="fzlx" value="0" onclick="getSxdr(0,'+count+','+i+')"/>自定义导入</span>';
		text += '</div>';
		text += '<div id="changeSelect_'+count+"_"+i+'">'
		text += '<div class="newSelect">';
		text += '<input id="newLocalType_'+count+'_'+i+'" data-newtype="" onclick="gazetteerType(this.value,'+count+','+i+')" type="checkbox" value="FZ_New" name="rdlist['+count+'].rldto['+i+'].gazetteersType"/>新方志';
		text += '<div class="newTime"><span>资源更新时间</span>'
		text += '<div class="newSelectTime"><select class="noChecked" onchange="findStartTime(this.value,1,'+count+','+i+',\'new\')" disabled id="startTime_'+count+'_'+i+'" data-startTime=""><option value="">不限</option></select><span style="vertical-align: top;"> ——— </span>'
		text += '<select class="noChecked" onchange="findEndTime(this.value,2,'+count+','+i+',\'new\')" disabled id="endTime_'+count+'_'+i+'" data-endTime=""><option value="">不限</option></select><span id="errorTime_'+count+'_'+i+'" class="errorTime"></span></div>'
		text += '</div>'
		text += '<div class="newLocation"><span>地区</span><span class="newLocationSelect"><span class="locationPrev">省</span><span>'
		text += '<select disabled class="noChecked" id="sheng_'+count+'_'+i+'" onchange="findArea(this.value,1,'+count+','+i+',\'new\')" data-prev=""><option value="">全部</option></select>'
		text += '<span class="locationCity">市</span><select disabled class="noChecked" id="shi_'+count+'_'+i+'" onchange="findArea(this.value,2,'+count+','+i+',\'new\')" data-city=""><option value="">全部</option></select>'
		text += '<span class="locationCounty">县</span><select disabled class="noChecked" id="xian_'+count+'_'+i+'" onchange="saveArea('+count+','+i+',\'new\')" data-county=""><option value="">全部</option></select></div>'
		text += '<div class="newDate"><span>数据分类</span>'
		text += '<div data-select="" class="newDateSelect"><input disabled id="dataTypeFirst_'+count+'_'+i+'" onclick="getDataType(this.value,'+count+','+i+')" type="radio" value="WFLocalChronicle" name="rdlist['+count+'].rldto['+i+'].drtm" checked/> 整本'
		text += '<input id="dataTypeSecond_'+count+'_'+i+'" disabled style="margin-left: 50px;" onclick="getDataType(this.value,'+count+','+i+')" type="radio" value="WFLocalChronicleItem" name="rdlist['+count+'].rldto['+i+'].drtm"/> 条目</div>'
		text +=	'</div>';
		text += '<div class="newClassify_'+count+'_'+i+'" style="margin-left: 45px;margin-top: 10px;" data-classify=""><span id="zjfl_'+count+'_'+i+'">专辑分类</span><input type="checkbox" disabled style="margin-left: 20px;" checked="checked" onclick="checkfzAll(this,'+count+','+i+')" id="checkfz_'+count+'_'+i+'" />全选<span style="margin-left:60px;color:red;" class="checkType_'+count+'_'+i+'"></span>'
		text +=	'</div>';
		text += '<div id="new_class_code_'+count+'_'+i+'" style="width: 475px;margin-left: 120px;"></div><div id="item_class_code_'+count+'_'+i+'" style="display:none;width: 475px;margin-left: 120px;"></div>'
		text += '</div>'
		text += '<div class="oldSelect">'
		text += '<input id="oldLocalType_'+count+'_'+i+'" data-oldtype="" onclick="gazetteerType(this.value,'+count+','+i+')" type="checkbox" value="FZ_Old" name="rdlist['+count+'].rldto['+i+'].gazetteersOldType"/>旧方志'
		text += '<div class="oldTime"><span>资源更新时间</span>'
		text += '<div class="oldSelectTime"><select disabled class="noChecked" onchange="findStartTime(this.value,1,'+count+','+i+',\'old\')" id="old_startTime_'+count+'_'+i+'" data-oldStartTime=""><option>不限</option></select><span style="vertical-align: top;"> ——— </span>'
		text += '<select disabled class="noChecked" onchange="findEndTime(this.value,2,'+count+','+i+',\'old\')" id="old_endTime_'+count+'_'+i+'" data-oldEndTime=""><option value="">不限</option></select><span id="errorOldTime_'+count+'_'+i+'" class="errorOldTime"></span></div>'
		text += '</div>'
		text += '<div class="oldLocation"><span>地区</span><span class="oldLocationSelect"><span class="locationPrev">省</span><span>'
		text += '<select class="noChecked" disabled id="o_sheng_'+count+'_'+i+'" onchange="findArea(this.value,1,'+count+','+i+',\'old\')" data-oldprev=""><option value="">全部</option></select>'
		text += '<span class="locationCity">市</span><select disabled class="noChecked" id="o_shi_'+count+'_'+i+'" onchange="findArea(this.value,2,'+count+','+i+',\'old\')" data-oldcity=""><option value="">全部</option></select>'
		text += '<span class="locationCounty">县</span><select disabled class="noChecked" id="o_xian_'+count+'_'+i+'" onchange="saveArea('+count+','+i+',\'old\')" data-oldcounty=""><option value="">全部</option></select></div>'
		text += '</div>'
		text += '<div class="database">'
		text += '<input type="checkbox"  id="databaseInput_'+count+'_'+i+'" data-database="" onclick="gazetteerType(this.value,'+count+','+i+')" value="FZ_Culture">专辑数据库'
		text += '<div class="databaseInput"><input type="checkbox" onclick="databaseClick('+count+','+i+')" disabled id="checkData_'+count+'_'+i+'">文化志</div>'
		text += '</div>'
		text += '</div>'
		text += '<div id="changeTextarea_'+count+'_'+i+'" style="display:none">'
		text +=  '<div>整本 （以;分隔）<textarea class="form-control" rows="3" id="gazetteersId_'+count+'_'+i+'" style="width:100%;"></textarea></div>'
		text += '<div>条目 （以;分隔）<textarea class="form-control" rows="3" id="itemId_'+count+'_'+i+'" style="width:100%;"></textarea></div>'
		text += '</div>'
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersArea" id="gazetteersArea_'+count+'_'+i+'">';
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersAlbum" id="gazetteersAlbum_'+count+'_'+i+'">';
		text += '<input type="hidden" value="WFLocalChronicle" name="rdlist['+count+'].rldto['+i+'].gazetteersLevel" id="gazetteersLevel_'+count+'_'+i+'"/>'
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersOldArea" id="gazetteersOldArea_'+count+'_'+i+'">';
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersStartTime" id="gazetteersStartTime_'+count+'_'+i+'">';
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersEndTime" id="gazetteersEndTime_'+count+'_'+i+'">';
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersOldStartTime" id="gazetteersOldStartTime_'+count+'_'+i+'">';
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersOldEndTime" id="gazetteersOldEndTime_'+count+'_'+i+'">';
		text += '<input type="hidden" value="" name="rdlist['+count+'].rldto['+i+'].localType" id="gazetteers_type_'+count+'_'+i+'">'
		text += '<input type="hidden" value="" name="rdlist['+count+'].rldto['+i+'].gazetteersId" id="gazetteers_TopId_'+count+'_'+i+'">'
		text += '<input type="hidden" value="" name="rdlist['+count+'].rldto['+i+'].itemId" id="gazetteers_BottomItem_'+count+'_'+i+'">'
		text += '<input type="hidden" value="" name="rdlist['+count+'].rldto['+i+'].albumDatabase" id="gazetteers_albumDatabase_'+count+'_'+i+'">'
		text +=	'</div>'
	}
	text += '</div></div>';
	
	
	$("#detail_0").append(text);
	if(type.indexOf("perio")>-1||type.indexOf("degree")>-1||type.indexOf("conf")>-1||type.indexOf("books")>-1){
		findSubject(count,i);
	}
	if(type.indexOf("patent")>-1){
		findPatent(count,i);
	}
	if(type.indexOf("perio")>-1){
		findPerioSubject(count,i);
	}
	if(type.indexOf("local chronicles")>-1){//添加地方志专辑分类和区域代码
		initGazetteer(count,i);
	}
	if(type.indexOf("perio")>-1||type.indexOf("degree")>-1){
		//加载详情限定时间
		setDateList(count,i,type);
	}
}

/**
 * 获取时间控件
 */
function setDateList(count,i,type){
	var curYear = new Date().getFullYear();
	var startYear = 1900;// 起始年
	var yearCount = curYear-startYear;
	var startHtml = '<option value="">-请选择-</option>';
	var endYearHtml = '<option value="">-请选择-</option>';
	
	for(var j=0;j <= yearCount;j++){
		startHtml +='<option value="'+(startYear+j)+'">'+(startYear+j)+'</option>';
		endYearHtml +='<option value="'+(curYear-j)+'">'+(curYear-j)+'</option>';
	}
	if(type.indexOf("perio")>-1){		
		$("#journal_startTime_"+count+"_"+i).html(startHtml);
		$("#journal_endTime_"+count+"_"+i).html(endYearHtml);
	}else if(type.indexOf("degree")>-1){		
		$("#degreeStarttime_"+count+"_"+i).html(startHtml);
		$("#degreeEndtime_"+count+"_"+i).html(endYearHtml);
	}
}

/***************方志代码 开始************************/
//筛选导入
function getSxdr(value,count,i){
	$('.checkType_'+count+'_'+i).text('')
	if(value==1){//分类筛选
		findShaiXuan(count,i);
		$('.checkType_'+count+'_'+i).text('')
	}else if(value==0){//自定义导入
		findSeftDefine(count,i);
	}
}

//选择大类
function gazetteerType(val,count,i){
	if(val=="FZ_New" || val==""){//新方志
		$('.checkType_'+count+'_'+i).text('')
		if ($(event.target).is(":checked")) {
			$('#newLocalType_'+count+'_'+i).attr('data-newtype','FZ_New')
			$('#changeSelect_'+count+'_'+i+ ' .newSelectTime select').prop('disabled',false)
			$('#changeSelect_'+count+'_'+i+ ' .newLocationSelect select').prop('disabled',false)
			$('#changeSelect_'+count+'_'+i+ ' .newDateSelect input').prop('disabled',false)
			$('#changeSelect_'+count+'_'+i+ ' .newClassify_'+count+'_'+i+' input').prop('disabled',false)
			$('#new_class_code_'+count+'_'+i+' input').each(function(){
				$(this).prop('disabled',false)
			})
			$('#item_class_code_'+count+'_'+i+' input').each(function(){
				$(this).prop('disabled',false)
			})
			$('#checkfz_'+count+'_'+i).prop('checked',true)
			var itemValue=''
			$('#new_class_code_'+count+'_'+i+' input').each(function(){
				itemValue+= $(this).val()+';'
			})
			$('.newClassify_'+count+'_'+i).attr('data-classify',itemValue)
		}else {
			$('#changeSelect_'+count+'_'+i+ ' .newSelectTime select').prop('disabled',true)
			$('#changeSelect_'+count+'_'+i+ ' .newLocationSelect select').prop('disabled',true)
			$('#changeSelect_'+count+'_'+i+ ' .newDateSelect input').prop('disabled',true)
			$('#changeSelect_'+count+'_'+i+ ' .newClassify_'+count+'_'+i+' input').prop('disabled',true)
			$('#item_class_code_'+count+'_'+i+' input').each(function(){
				$(this).prop('disabled',true)
				$(this).prop('checked',true)
			})
			$('#newLocalType_'+count+'_'+i).attr('data-newtype','')
			$('#new_class_code_'+count+'_'+i+' input').each(function(){
				$(this).prop('disabled',true)
				$(this).prop('checked',true)
			})
			$('#checkfz_'+count+'_'+i).prop('checked',true)
			$('#dataTypeFirst_'+count+'_'+i).prop('checked',true)
			$('#zjfl_'+count+'_'+i).text('专辑分类')
			$('#new_class_code_'+count+'_'+i).show()
			$('#item_class_code_'+count+'_'+i).hide()
			var itemValue=''
			$('#new_class_code_'+count+'_'+i+' input').each(function(){
				itemValue+= $(this).val()+';'
			})
			$('.newClassify_'+count+'_'+i).attr('data-classify',itemValue)
			$('#startTime_'+count+'_'+i).attr('data-starttime','')
			$('#startTime_'+count+'_'+i).val('')
			$('#endTime_'+count+'_'+i).attr('data-endtime','')
			$('#endTime_'+count+'_'+i).val('')
			$('#sheng_'+count+'_'+i).attr('data-prev','')
			$('#sheng_'+count+'_'+i).val('')
			$('#shi_'+count+'_'+i).attr('data-city','')
			$('#shi_'+count+'_'+i).html('<option value="">全部</option>')
			$('#xian_'+count+'_'+i).attr('data-county','')
			$('#xian_'+count+'_'+i).html('<option value="">全部</option>')
			$('#errorTime_'+count+'_'+i).html('')
		}
	}else if(val=="FZ_Old"){//旧方志
		if ($(event.target).is(":checked")) {
			$('#oldLocalType_'+count+'_'+i).attr('data-oldtype','FZ_Old')
			$('#changeSelect_'+count+'_'+i+ ' .oldSelectTime select').prop('disabled',false)
			$('#changeSelect_'+count+'_'+i+ ' .oldLocationSelect select').prop('disabled',false)
		}else {
			$('#changeSelect_'+count+'_'+i+ ' .oldSelectTime select').prop('disabled',true)
			$('#changeSelect_'+count+'_'+i+ ' .oldLocationSelect select').prop('disabled',true)
			$('#oldLocalType_'+count+'_'+i).attr('data-oldtype','')
			$('#old_startTime_'+count+'_'+i).attr('data-oldstarttime','')
			$('#old_startTime_'+count+'_'+i).val('')
			$('#old_endTime_'+count+'_'+i).attr('data-oldendtime','')
			$('#old_endTime_'+count+'_'+i).val('')
			$('#o_sheng_'+count+'_'+i).attr('data-oldprev','')
			$('#o_sheng_'+count+'_'+i).val('')
			$('#o_shi_'+count+'_'+i).attr('data-oldcity','')
			$('#o_shi_'+count+'_'+i).html('<option value="">全部</option>')
			$('#o_xian_'+count+'_'+i).attr('data-oldcounty','')
			$('#o_xian_'+count+'_'+i).html('<option value="">全部</option>')
			$('#errorOldTime_'+count+'_'+i).html('')
		}
	}else if(val==='FZ_Culture') { // 专辑数据库
		if($(event.target).is(":checked")) {
			$('#checkData_'+count+'_'+i).prop('disabled',false)		
			$('#checkData_'+count+'_'+i).prop('checked', true)
			$('#databaseInput_'+count+'_'+i).attr('data-database', 'FZ_Culture')
		}else {
			$('#checkData_'+count+'_'+i).prop('disabled',true)
			$('#checkData_'+count+'_'+i).prop('checked', false)
			$('#databaseInput_'+count+'_'+i).attr('data-database', '')
		}
	}
}

//地方志
function initGazetteer(count,index){
	var localType = $('#localchronicles_'+count+'_'+index+' #gazetteers_type_'+count+'_'+index).val()
	if($('.export input').is(':checked')){$('.selectType input').prop('checked',true)}
	
	if(($('#gazetteersLevel_'+count+'_'+index).val()=='')||($('#gazetteersLevel_'+count+'_'+index).val()=='WFLocalChronicle')) {
		$('#dataTypeFirst_'+count+'_'+index).prop('checked',true)
		$('#dataTypeFirst_'+count+'_'+index).parent().attr('data-select','WFLocalChronicle')
		$('#dataTypeFirst_'+count+'_'+index).prop('checked',true)
	}else if($('#gazetteersLevel_'+count+'_'+index).val()=='WFLocalChronicleItem') {
		$('#dataTypeSecond_'+count+'_'+index).prop('checked',true)
		$('#dataTypeSecond_'+count+'_'+index).parent().attr('data-select','WFLocalChronicleItem')
		$('#dataTypeSecond_'+count+'_'+index).prop('checked',true)
	}
	var area=$("#gazetteersArea_"+count+"_"+index).val();
	var oldArea = $("#gazetteersOldArea_"+count+"_"+index).val();
	$.ajax({
		type : "post",
		async:false,
		url : "../auser/getlocalDate.do",
		dataType : "json",
		
		success:function(data){
			var startTime='<option value="">不限</option>'
				var d = data.data
			var year = d.length
			for(var j = 0;j<year;j++) {
				startTime += '<option value="'+d[j]+'">'+d[j]+'</option>'
			}
			$("#startTime_"+count+"_"+index).html(startTime)
			$("#endTime_"+count+"_"+index).html(startTime)
			$("#old_startTime_"+count+"_"+index).html(startTime)
			$("#old_endTime_"+count+"_"+index).html(startTime)
		}
	})
	$.ajax({
		type : "post",
		data : {pid:0,area:area,oldArea:oldArea},
		async:false,
		url : "../auser/findGazetteer.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {},
		success:function(data){
			var gazetter=data.arrayGazetter;
			if(gazetter!=null){
				var gazetter_new='';
				var gazetter_item='';
				var new_index=0;
				var item_index=0;
				var gazetter_newName =''
				var gazetter_itemName = ''
				for(var i=0;i<gazetter.length;i++){
					var name=gazetter[i].name;
					if(gazetter[i].pid=="0"){
						new_index++;
						gazetter_newName+=name+';'
						gazetter_new +='<input type="checkbox" disabled value="'+name+'" name="classCode_'+count+'_'+index+'" checked="checked" onclick="findBox('+count+','+index+')"/> '+name+' &nbsp;';
						if(new_index>0&&new_index%5==0&&gazetter.length!=new_index){
							gazetter_new +='<br/>'
						}
						
					}else{
						item_index++;
						gazetter_itemName+=name +';'
						gazetter_item +='<input type="checkbox" disabled value="'+name+'" name="classCode2_'+count+'_'+index+'" checked="checked" onclick="findBox('+count+','+index+')"/> '+name+' &nbsp;';
						if(item_index>0&&item_index%5==0&&gazetter.length!=item_index){
							gazetter_item +='<br/>'
						}
						
					}
				}
				if($('#changeSelect_'+count+'_'+index+' .newDateSelect').attr('data-select')=='WFLocalChronicle') {
					$('.newClassify_'+count+'_'+index).attr('data-classify',gazetter_newName)
				}else {
					$('.newClassify_'+count+'_'+index).attr('data-classify',gazetter_itemName)
				}
				$("#new_class_code_"+count+"_"+index).html(gazetter_new);
				$("#item_class_code_"+count+"_"+index).html(gazetter_item);
			}
			
			if(localType=='FZ_New;FZ_Old') {
				$('#newLocalType_'+count+'_'+index).prop('checked',true)
				$('#oldLocalType_'+count+'_'+index).prop('checked',true)
				$('#newLocalType_'+count+'_'+index).attr('data-newtype','FZ_New')
				$('#oldLocalType_'+count+'_'+index).attr('data-oldtype','FZ_Old')
				$('#changeSelect_'+count+'_'+index+ ' .newSelectTime select').prop('disabled',false)
				$('#changeSelect_'+count+'_'+index+ ' .newLocationSelect select').prop('disabled',false)
				$('#changeSelect_'+count+'_'+index+ ' .newDateSelect input').prop('disabled',false)
				$('#changeSelect_'+count+'_'+index+ ' .newClassify_'+count+'_'+index+' input').prop('disabled',false)
				$('#new_class_code_'+count+'_'+index+' input').each(function(){
					$(this).prop('disabled',false)
				})
				$('#item_class_code_'+count+'_'+index+' input').each(function(){
					$(this).prop('disabled',false)
				})
				$('#changeSelect_'+count+'_'+index+ ' .oldSelectTime select').prop('disabled',false)
				$('#changeSelect_'+count+'_'+index+ ' .oldLocationSelect select').prop('disabled',false)
			}else if(localType.indexOf('FZ_New')>-1) {
				$('#newLocalType_'+count+'_'+index).prop('checked',true)
				$('#newLocalType_'+count+'_'+index).attr('data-newtype','FZ_New')
				$('#changeSelect_'+count+'_'+index+ ' .newSelectTime select').prop('disabled',false)
				$('#changeSelect_'+count+'_'+index+ ' .newLocationSelect select').prop('disabled',false)
				$('#changeSelect_'+count+'_'+index+ ' .newDateSelect input').prop('disabled',false)
				$('#changeSelect_'+count+'_'+index+ ' .newClassify_'+count+'_'+index+' input').prop('disabled',false)
				$('#new_class_code_'+count+'_'+index+' input').each(function(){
					$(this).prop('disabled',false)
				})
				$('#item_class_code_'+count+'_'+index+' input').each(function(){
					$(this).prop('disabled',false)
				})
				$('#changeSelect_'+count+'_'+index+ ' .oldSelectTime select').prop('disabled',true)
				$('#changeSelect_'+count+'_'+index+ ' .oldLocationSelect select').prop('disabled',true)
			}else if (localType.indexOf('FZ_Old')>-1) {
				$('#changeSelect_'+count+'_'+index+ ' .newSelectTime select').prop('disabled',true)
				$('#changeSelect_'+count+'_'+index+ ' .newLocationSelect select').prop('disabled',true)
				$('#changeSelect_'+count+'_'+index+ ' .newDateSelect input').prop('disabled',true)
				$('#changeSelect_'+count+'_'+index+ ' .newClassify_'+count+'_'+index+' input').prop('disabled',true)
				$('#new_class_code_'+count+'_'+index+' input').each(function(){
					$(this).prop('disabled',true)
				})
				$('#item_class_code_'+count+'_'+index+' input').each(function(){
					$(this).prop('disabled',true)
				})
				$('#oldLocalType_'+count+'_'+index).prop('checked',true)
				$('#oldLocalType_'+count+'_'+index).attr('data-oldtype','FZ_Old')
				$('#changeSelect_'+count+'_'+index+ ' .oldSelectTime select').prop('disabled',false)
				$('#changeSelect_'+count+'_'+index+ ' .oldLocationSelect select').prop('disabled',false)
			}
			if(localType=='') {
				$('#newLocalType_'+count+'_'+index).prop('checked',false)
				$('#oldLocalType_'+count+'_'+index).prop('checked',false)
				$('#newLocalType_'+count+'_'+index).attr('data-newtype','')
				$('#oldLocalType_'+count+'_'+index).attr('data-oldtype','')
				$('#changeSelect_'+count+'_'+index+ ' .newSelectTime select').prop('disabled',true)
				$('#changeSelect_'+count+'_'+index+ ' .newLocationSelect select').prop('disabled',true)
				$('#changeSelect_'+count+'_'+index+ ' .newDateSelect input').prop('disabled',true)
				$('#changeSelect_'+count+'_'+index+ ' .newClassify_'+count+'_'+index+' input').prop('disabled',true)
				$('#new_class_code_'+count+'_'+index+' input').each(function(){
					$(this).prop('disabled',true)
				})
				$('#item_class_code_'+count+'_'+index+' input').each(function(){
					$(this).prop('disabled',true)
				})
				$('#checkfz_'+count+'_'+index).prop('checked',false)
				$('#changeSelect_'+count+'_'+index+ ' .oldSelectTime select').prop('disabled',true)
				$('#changeSelect_'+count+'_'+index+ ' .oldLocationSelect select').prop('disabled',true)
			}
			
			var area='<option value="">全部</option>';
			var arrayArea=data.arrayArea;
			for(var i=0;i<arrayArea.length;i++){
				area+='<option value="'+arrayArea[i].id+'">'+arrayArea[i].name+'</option>';
			}
			$("#sheng_"+count+"_"+index).html(area);
			$("#o_sheng_"+count+"_"+index).html(area);
			//地区填值
			var newLocationVal = '' 
			var sheng=data.sheng;
			if(sheng!=null){
				$("#sheng_"+count+"_"+index).val(sheng.id);
				$("#sheng_"+count+"_"+index).attr('data-prev',sheng.name);
				var shi=data.shi;
				if(shi!=null){
					$("#shi_"+count+"_"+index).html('<option value="'+shi.id+'">'+shi.name+'</option>');
					$("#shi_"+count+"_"+index).attr('data-city',shi.name);
					var xian=data.xian;
					if(xian!=null){
						$("#xian_"+count+"_"+index).html('<option value="'+xian.id+'">'+xian.name+'</option>');
						$("#xian_"+count+"_"+index).attr('data-county',xian.name);
					}else {
						$("#xian_"+count+"_"+index).attr('data-county','');
					}
				}else {
					
					$("#shi_"+count+"_"+index).attr('data-city','');
					
				}
			}else {
				findArea('','1',count,index,'new')
				$("#sheng_"+count+"_"+index).attr('data-prev','');
			}
			// 旧地方志
			var publicTemp = count+'_'+index
			var old_sheng=data.old_sheng;
			if(old_sheng!=null){
				$("#o_sheng_"+count+"_"+index).val(old_sheng.id);
				$("#o_sheng_"+count+"_"+index).attr('data-oldprev',old_sheng.name);
				var old_shi=data.old_shi;
				if(old_shi!=null){
					$("#o_shi_"+count+"_"+index).html('<option value="'+old_shi.id+'">'+old_shi.name+'</option>');
					$("#o_shi_"+count+"_"+index).attr('data-oldcity',old_shi.name);
					var old_xian=data.old_xian;
					if(old_xian!=null){
						$("#o_xian_"+count+"_"+index).html('<option value="'+old_xian.id+'">'+old_xian.name+'</option>');
						$("#o_xian_"+count+"_"+index).attr('data-oldcounty',old_xian.name);
					}else {
						$("#o_xian_"+count+"_"+index).attr('data-oldcounty','');
					}
				}else {
					$("#o_shi_"+count+"_"+index).attr('data-oldcity','');
				}
			}else {
				findArea('','2',count,index,'old')
				$("#o_sheng_"+count+"_"+index).attr('data-oldprev','');
			}
			// 新方志开始时间
			var newStartTime = $('#gazetteersStartTime_'+publicTemp).val()
			if(newStartTime!=null&&newStartTime!=''){
				$('#startTime_'+publicTemp).val(newStartTime)
				$('#startTime_'+publicTemp).attr('data-starttime',newStartTime)
			}else {
				$('#newStartTime_'+publicTemp).val('')
				$('#startTime_'+publicTemp).attr('data-starttime','')
			}
			// 结束时间
			var newEndTime = $('#gazetteersEndTime_'+publicTemp).val()
			if(newEndTime!=null&&newEndTime!=''){
				$('#endTime_'+publicTemp).val(newEndTime)
				$('#endTime_'+publicTemp).attr('data-endtime',newEndTime)
			}else {
				$('#endTime_'+publicTemp).val('')
				$('#endTime_'+publicTemp).attr('data-endtime','')
			}
			// 旧方志开始时间
			var oldStartTime = $('#gazetteersOldStartTime_'+publicTemp).val()
			if(oldStartTime!=null&&oldStartTime!=''){
				$('#old_startTime_'+publicTemp).val(oldStartTime)
				$('#old_startTime_'+publicTemp).attr('data-oldstarttime',oldStartTime)
			}else {
				$('#old_startTime_'+publicTemp).val('')
				$('#old_startTime_'+publicTemp).attr('data-oldstarttime','')
			}
			
			// 旧方志结束时间
			var oldEndTime = $('#gazetteersOldEndTime_'+publicTemp).val()
			if(oldEndTime!=null&&oldEndTime!=''){
				$('#old_endTime_'+publicTemp).val(oldEndTime)
				$('#old_endTime_'+publicTemp).attr('data-oldendtime',oldEndTime)
			}else {
				$('#old_endTime_'+publicTemp).val('')
				$('#old_endTime_'+publicTemp).attr('data-oldendtime','')
			}
			setGazetteers(count,index);
		}
	});
}

//给方志赋值
function setGazetteers(count,index){
	var gazetteers_id=$("#gazetteersId_"+count+"_"+index).val();
	var itemId=$("#itemId_"+count+"_"+index).val();
	//筛选
		var gazetteers_type=$("#gazetteers_type_"+count+"_"+index).val();
		$("input[name='rdlist["+count+"].rldto["+index+"].gazetteersType']").each(function(){
			if($(this).val()==gazetteers_type){
				$(this).attr("checked",true);
			}
		});
		var gazetteersLevel=$("#gazetteersLevel_"+count+"_"+index).val();
		$("input[name='rdlist["+count+"].rldto["+index+"].drtm']").each(function(){
			if($(this).val()==gazetteersLevel){
				$(this).attr("checked",true);
			}
		});
		//设置专辑分类和专题分类是否选中
		var album=$("#gazetteersAlbum_"+count+"_"+index).val();
		var albums=album.split(";");
		var class_code="";
		if(gazetteersLevel=="WFLocalChronicle"){
			class_code="input[name='classCode_"+count+"_"+index+"']";
			$("#new_class_code_"+count+"_"+index).show();
			$("#item_class_code_"+count+"_"+index).hide();
		}else if(gazetteersLevel=="WFLocalChronicleItem"){
			class_code="input[name='classCode2_"+count+"_"+index+"']";
			$("#new_class_code_"+count+"_"+index).hide();
			$("#item_class_code_"+count+"_"+index).show();
		}
		$(class_code).each(function(){
			var val=$(this).val();
			for(var c=0;c<albums.length;c++){
				if(val==albums[c]){
					$(this).attr("checked",true);
					continue;
				}
			}
		});

		var albumIdNum = 0
		var albumItemNum = 0
		$("input[name='classCode_"+count+"_"+index+"']:checked").each(function(){
			if($(this).is(':checked')) {
				albumIdNum++				
			}
		});
		$("input[name='classCode2_"+count+"_"+index+"']:checked").each(function(){
			if($(this).is(':checked')) {
				albumItemNum++				
			}
		});
		if($('#dataTypeFirst_'+count+'_'+index).is(':checked')) {		
			if(albumIdNum==$('input[name=classCode_'+count+'_'+index+']').length) {
				$('#checkfz_'+count+'_'+index).prop('checked',true)
			}else {
				$('#checkfz_'+count+'_'+index).prop('checked',false)
			}
		}else {
			if(albumItemNum==$('input[name=classCode2_'+count+'_'+index+']').length) {
				$('#checkfz_'+count+'_'+index).prop('checked',true)
			}else {
				$('#checkfz_'+count+'_'+index).prop('checked',false)
			}		
		}
		findShaiXuan(count,index);
	
}

//点击筛选的样式
function findShaiXuan(count,i){
//	$("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']").attr("disabled",false);
//	var gazetteersType=$("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']:checked").val();
//	if(gazetteersType=="FZ_New"|| gazetteersType==""){
//		$("input[name='classCode_"+count+"_"+i+"']").attr("disabled",false);
//		$("input[name='classCode2_"+count+"_"+i+"']").attr("disabled",false);
//	}else{
//		$("input[name='classCode_"+count+"_"+i+"']").attr("disabled",true);
//		$("input[name='classCode2_"+count+"_"+i+"']").attr("disabled",true);
//	}
//	$("input[name='rdlist["+count+"].rldto["+i+"].drtm']").attr("disabled",false);
//	
//	$("#sheng_"+count+"_"+i).attr("disabled",false);
//	$("#shi_"+count+"_"+i).attr("disabled",false);
//	$("#xian_"+count+"_"+i).attr("disabled",false);
//	$("#checkfz_"+count+"_"+i).attr("disabled",false);
	//自定义导入的禁用
	if($('#dataTypeFirst_'+count+'_'+i).is(':checked')){
		if($('.newClassify_'+count+'_'+i).attr('data-classify')=='') {
			$('.checkType_'+count+'_'+i).text('至少选择选择一项专辑分类')
		}else {
			$('.checkType_'+count+'_'+i).text('')			
		}
	}else {
		if($('.newClassify_'+count+'_'+i).attr('data-classify')=='') {
			$('.checkType_'+count+'_'+i).text('至少选择选择一项专题分类')
		}else {
			$('.checkType_'+count+'_'+i).text('')			
		}
	}
	$("#changeSelect_"+count+"_"+i).show()
	$("#changeTextarea_"+count+"_"+i).hide()
}
//点击自定义的样式
function findSeftDefine(count,i){
//	$("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']").attr("disabled",true);
//	$("input[name='classCode_"+count+"_"+i+"']").attr("disabled",true);
//	$("input[name='classCode2_"+count+"_"+i+"']").attr("disabled",true);
//	$("input[name='rdlist["+count+"].rldto["+i+"].drtm']").attr("disabled",true);
//	
//	$("#sheng_"+count+"_"+i).attr("disabled",true);
//	$("#shi_"+count+"_"+i).attr("disabled",true);
//	$("#xian_"+count+"_"+i).attr("disabled",true);
//	$("#checkfz_"+count+"_"+i).attr("disabled",true);
	//自定义导入的开放
	$("#changeSelect_"+count+"_"+i).hide()
	$("#changeTextarea_"+count+"_"+i).show()
}


// 开始时间
function findStartTime(value,index,count,number,type) {
	if (type=='new') {
		$('#startTime_'+count+'_'+number).attr('data-starttime',value)
//		$("#gazetteersStartTime_"+count+"_"+number).val(value);		
	}else if(type=='old') {
		$('#old_startTime_'+count+'_'+number).attr('data-oldstarttime',value)
//		$("#gazetteersOldStartTime_"+count+"_"+number).val(value);
	}
	var startTime = $('#startTime_'+count+'_'+number).attr('data-starttime'),
		endTime = $('#endTime_'+count+'_'+number).attr('data-endtime'),
		oldStartTime = $('#old_startTime_'+count+'_'+number).attr('data-oldstarttime'),
		oldEndTime = $('#old_endTime_'+count+'_'+number).attr('data-oldendtime')
	if(endTime<startTime) {
		if(endTime==''||startTime=='') {
			$('#errorTime_'+count+'_'+number).text('');
		}else {
			$('#errorTime_'+count+'_'+number).text('结束时间不能小于开始时间');		
		}
	}else {
		$('#errorTime_'+count+'_'+number).text('');
	}
	
	if(oldEndTime<oldStartTime) {
		if(oldEndTime==''||oldStartTime=='') {
			$('#errorOldTime_'+count+'_'+number).text('');
		}else {
			$('#errorOldTime_'+count+'_'+number).text('结束时间不能小于开始时间');		
		}
	}else {
		$('#errorOldTime_'+count+'_'+number).text('');
	}
}

//开始时间
function findEndTime(value,index,count,number,type) {
	if (type=='new') {
		$('#endTime_'+count+'_'+number).attr('data-endtime',value)	
//		$("#gazetteersEndTime_"+count+"_"+number).val(value);		
	}else if(type=='old') {
		$('#old_endTime_'+count+'_'+number).attr('data-oldendtime',value)	
//		$("#gazetteersOldEndTime_"+count+"_"+number).val(value);
	}
	var startTime = $('#startTime_'+count+'_'+number).attr('data-starttime'),
		endTime = $('#endTime_'+count+'_'+number).attr('data-endtime'),
		oldStartTime = $('#old_startTime_'+count+'_'+number).attr('data-oldstarttime'),
		oldEndTime = $('#old_endTime_'+count+'_'+number).attr('data-oldendtime')
	if(endTime<startTime) {
		if(endTime==''||startTime=='') {
			$('#errorTime_'+count+'_'+number).text('');
		}else {
			$('#errorTime_'+count+'_'+number).text('结束时间不能小于开始时间');		
		}
	}else {
		$('#errorTime_'+count+'_'+number).text('');
	}
	
	if(oldEndTime<oldStartTime) {
		if(oldEndTime==''||oldStartTime=='') {
			$('#errorOldTime_'+count+'_'+number).text('');
		}else {
			$('#errorOldTime_'+count+'_'+number).text('结束时间不能小于开始时间');		
		}
	}else {
		$('#errorOldTime_'+count+'_'+number).text('');
	}
}

//地方志查询区域
function findArea(value,index,count,num,type){
	if(type==='new') {
		if(index=="1"){
			$("#xian_"+count+"_"+num).html('<option value="">全部</option>');
		}
		$.ajax({
			type : "post",
			data : {pid:value},
			async:false,
			url : "../auser/findArea.do",
			dataType : "json",
			beforeSend : function(XMLHttpRequest) {},
			success:function(data){
				var area='<option value="">全部</option>';
				var arrayArea=data.arrayArea;
				for(var i=0;i<arrayArea.length;i++){
					area+='<option value="'+arrayArea[i].id+'">'+arrayArea[i].name+'</option>';
				}
				if(index=="1"){
					if($('#sheng_'+count+'_'+num).val()=='') {
						$("#shi_"+count+"_"+num).html('<option value="">全部</option>');
					}else {
						$("#shi_"+count+"_"+num).html(area);						
					}
				}else if(index=="2"){
					$("#xian_"+count+"_"+num).html(area);
				}
				saveArea(count,num,type);
			}
		});		
	}else if (type==='old') {
		if(index=="1"){
			$("#o_xian_"+count+"_"+num).html('<option value="">全部</option>');
		}
		$.ajax({
			type : "post",
			data : {pid:value},
			async:false,
			url : "../auser/findArea.do",
			dataType : "json",
			beforeSend : function(XMLHttpRequest) {},
			success:function(data){
				var area='<option value="">全部</option>';
				var arrayArea=data.arrayArea;
				for(var i=0;i<arrayArea.length;i++){
					area+='<option value="'+arrayArea[i].id+'">'+arrayArea[i].name+'</option>';
				}
				if(index=="1"){
					if($('#o_sheng_'+count+'_'+num).val()=='') {
						$("#o_shi_"+count+"_"+num).html('<option value="">全部</option>');
					}else {
						$("#o_shi_"+count+"_"+num).html(area);						
					}
				}else if(index=="2"){
					$("#o_xian_"+count+"_"+num).html(area);
				}
				saveArea(count,num,type);
			}
		});		
	}
}

//保存区域
function saveArea(count,i,type){
	if(type==='new') {
		var sheng=$("#sheng_"+count+"_"+i).find("option:selected").text();
		if(sheng=="全部"){
			sheng="";
		}
		if(sheng==null||sheng==""){
			$('#sheng_'+count+'_'+i).attr('data-prev','')
			$('#shi_'+count+'_'+i).attr('data-city','')
			$('#xian_'+count+'_'+i).attr('data-county','')
//			$("#gazetteersArea_"+count+"_"+i).val("");
			return;
		}else {
			$('#sheng_'+count+'_'+i).attr('data-prev',sheng)
		}
		var shi=$("#shi_"+count+"_"+i).find("option:selected").text();
		if(shi=="全部"){
			shi="";
		}
		if(shi==null||shi==""){
			$('#shi_'+count+'_'+i).attr('data-city','')
//			$("#gazetteersArea_"+count+"_"+i).val(sheng);
			return;
		}else {
			$('#shi_'+count+'_'+i).attr('data-city',shi)
		}
		var xian=$("#xian_"+count+"_"+i).find("option:selected").text();
		if(xian=="全部"){
			xian="";
		}
		if(xian==null||xian==""){
			$('#xian_'+count+'_'+i).attr('data-county','')
//			$("#gazetteersArea_"+count+"_"+i).val(sheng+"_"+shi);
			return;
		}else {
			$('#xian_'+count+'_'+i).attr('data-county',xian)
		}
//		$("#gazetteersArea_"+count+"_"+i).val(sheng+"_"+shi+"_"+xian);	
	}else if (type==='old') {
		var oldsheng=$("#o_sheng_"+count+"_"+i).find("option:selected").text();
		if(oldsheng=="全部"){
			oldsheng="";
		}
		if(oldsheng==null||oldsheng==""){
			$('#o_sheng_'+count+'_'+i).attr('data-oldprev','')
			$('#o_shi_'+count+'_'+i).attr('data-oldcity','')
			$('#o_xian_'+count+'_'+i).attr('data-oldcounty','')
//			$("#gazetteersOldArea_"+count+"_"+i).val("");
			return;
		}else {
			$('#o_sheng_'+count+'_'+i).attr('data-oldprev',oldsheng)
		}
		var oldshi=$("#o_shi_"+count+"_"+i).find("option:selected").text();
		if(oldshi=="全部"){
			oldshi="";
		}
		if(oldshi==null||oldshi==""){
			$('#o_shi_'+count+'_'+i).attr('data-oldcity','')
//			$("#gazetteersOldArea_"+count+"_"+i).val(sheng);
			return;
		}else {
			$('#o_shi_'+count+'_'+i).attr('data-oldcity',oldshi)
		}
		var oldxian=$("#o_xian_"+count+"_"+i).find("option:selected").text();
		if(oldxian=="全部"){
			oldxian="";
		}
		if(oldxian==null||oldxian==""){
			$('#o_xian_'+count+'_'+i).attr('data-oldcounty','')
//			$("#gazetteersOldArea_"+count+"_"+i).val(sheng+"_"+shi);
			return;
		}else {
			$('#o_xian_'+count+'_'+i).attr('data-oldcounty',oldxian)
		}
//		$("#gazetteersOldArea_"+count+"_"+i).val(sheng+"_"+shi+"_"+xian);	
	}
}
//添加checkbox值
function findBox(count,i){
	var album="";
	var albumIdNum = 1
	var albumItemNum = 1
	
	if($('#dataTypeFirst_'+count+'_'+i).is(':checked')) {
		$("input[name='classCode_"+count+"_"+i+"']:checked").each(function(){
			if(album!=""){
				album+=";";
				albumIdNum++
			}
			album+=$(this).val();
		});
		if(albumIdNum==$('input[name=classCode_'+count+'_'+i+']').length) {
			$('#checkfz_'+count+'_'+i).prop('checked',true)
		}else {
			$('#checkfz_'+count+'_'+i).prop('checked',false)
		}
	}else {
		$("input[name='classCode2_"+count+"_"+i+"']:checked").each(function(){
			if(album!=""){
				album+=";";
				albumItemNum++
			}
			album+=$(this).val();
		});
		if(albumItemNum==$('input[name=classCode2_'+count+'_'+i+']').length) {
			$('#checkfz_'+count+'_'+i).prop('checked',true)
		}else {
			$('#checkfz_'+count+'_'+i).prop('checked',false)
		}		
	}
	if($('#dataTypeFirst_'+count+'_'+i).is(':checked')) {
		if(album=='') {
			$('.checkType_'+count+'_'+i).text('至少选择选择一项专辑分类')
		}else {
			$('.checkType_'+count+'_'+i).text('')
		}
	}else {
		if(album=='') {
			$('.checkType_'+count+'_'+i).text('至少选择选择一项专题分类')
		}else {
			$('.checkType_'+count+'_'+i).text('')
		}
	}
	$('.newClassify_'+count+'_'+i).attr("data-classify",album)
//	$("#gazetteersAlbum_"+count+"_"+i).val(album);
}
//全选和全不选
function checkfzAll(obj,count,i){
//	if (!$($(event.target).parent().parent()[0].firstElementChild).is(':checked')) {
//		return
//	} else {
//		if($("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']:checked").val()=="FZ_Old"){
//			return;
//		}
		var text=$(obj).is(':checked');
		var type="";
		if($("input[name='rdlist["+count+"].rldto["+i+"].drtm']:checked").val()=="WFLocalChronicle"){
			type="input[name='classCode_"+count+"_"+i+"']";
		}else{
			type="input[name='classCode2_"+count+"_"+i+"']";
		}
		var album="";
		if(text){
			$('.checkType_'+count+'_'+i).text('')
			$(type).each(function(){
				$(this).prop("checked", "true");
				album+=$(this).val()+";";
			});
			$('.newClassify_'+count+"_"+i).attr("data-classify",album)
//			$("#gazetteersAlbum_"+count+"_"+i).val(album);
		}else{
			if($('#dataTypeSecond_'+count+'_'+i).is(':checked')) {
				$('.checkType_'+count+'_'+i).text('至少选择选择一项专题分类')				
			}else {
				$('.checkType_'+count+'_'+i).text('至少选择选择一项专辑分类')
			}
			$(type).each(function(){
				$(this).removeAttr("checked");
			});
			$('.newClassify_'+count+"_"+i).attr("data-classify",'')
//			$("#gazetteersAlbum_"+count+"_"+i).val("");
		}
//	}
}

//选择数据类型
function getDataType(val,count,i){
	$("#checkfz_"+count+"_"+i).prop('checked',false);
	$('.checkType_'+count+'_'+i).text('')
	if(val=="WFLocalChronicle"){//志书
		$("#zjfl_"+count+"_"+i).html("专辑分类");
		$("#new_class_code_"+count+"_"+i).show();
		$("#item_class_code_"+count+"_"+i).hide();
		var locakValue = ''
			$("input[name='classCode_"+count+"_"+i+"']").each(function(){
				locakValue += $(this).val()+';';
			});
		$('.newClassify_'+count+'_'+i).attr('data-classify',locakValue)
	}else if(val=="WFLocalChronicleItem"){//条目
		$("#zjfl_"+count+"_"+i).html("专题分类");
		$("#new_class_code_"+count+"_"+i).hide();
		$("#item_class_code_"+count+"_"+i).show();
		var locakItemValue = ''
			$("input[name='classCode2_"+count+"_"+i+"']").each(function(){
				locakItemValue += $(this).val()+';';
			});
		$('.newClassify_'+count+'_'+i).attr('data-classify',locakItemValue)
	}
//	$("#gazetteersLevel_"+count+"_"+i).val(val);
	$(".newDateSelect").attr('data-select',val)
	$('#checkfz_'+count+'_'+i).prop('checked',true)
	if($("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']:checked").val()=="FZ_New"){
		$("input[name='classCode_"+count+"_"+i+"']").each(function(){
			$(this).prop("checked",true);
		});
		$("input[name='classCode2_"+count+"_"+i+"']").each(function(){
			$(this).prop("checked",true);
		});
	}
}

/*****************方志代码 结束**********************/
//学科中图分类树
function findSubject(count,i){
	var num=count+"_"+i;
	if(zhongZtr==null){
		$.ajax({
			type : "post",
			data : {num:num},
			async:false,
			url : "../auser/findsubject.do",
			beforeSend : function(XMLHttpRequest) {},
			success:function(data){
				zhongZtr=data;
				setSuject(data,num);
			}
		});
	}else{
		setSuject(zhongZtr,num);
	}
}
//学科分类号填值
function setSuject(data,num){
	var setting = {
			view: {
				dblClickExpand: false,
			},
			data: {
				simpleData: {
		            enable:true,
		            idKey:"id",
		            pIdKey:"pid"
		    	},
		        key:{name:"name"}
			},
			check: {
				enable: true,
				chkStyle: "checkbox"
			},
			callback: {
				onCheck: function(){
					var pz = $.fn.zTree.getZTreeObj("perioZtree_"+num);
					if(pz!=null){
						$("#journalClc_"+num).val(getCheckNode(pz));
					}
					var dz = $.fn.zTree.getZTreeObj("degreeZtree_"+num);
					if(dz!=null){
						$("#degreeClc_"+num).val(getCheckNode(dz));
					}
					var cz = $.fn.zTree.getZTreeObj("confZtree_"+num);
					if(cz!=null){
						$("#conferenceClc_"+num).val(getCheckNode(cz));
					}
					var bz = $.fn.zTree.getZTreeObj("bookZtree_"+num);
					if(bz!=null){
						$("#booksClc_"+num).val(getCheckNode(bz));
					}
				}
			}
	};
	//期刊中途分类
	$.fn.zTree.init($("#perioZtree_"+num), setting, data.ztreeJson);
	//学位中途分类
	$.fn.zTree.init($("#degreeZtree_"+num), setting, data.ztreeJson);
	//会议中途分类
	$.fn.zTree.init($("#confZtree_"+num), setting, data.ztreeJson);
	//图书中途分类
	$.fn.zTree.init($("#bookZtree_"+num), setting, data.ztreeJson);

}


//专利中图分类树
function findPatent(count,i){
	var num=count+"_"+i;
	if(patentZtr==null){
		$.ajax({
			type : "post",
			data : {num:num},
			async:false,
			url : "../auser/findpatent.do",
			dataType : "json",
			beforeSend : function(XMLHttpRequest) {},
			success:function(data){
				setPatent(data,num);
			}
		});
	}else{
		setPatent(patentZtr,num);
	}
}
function setPatent(data,num){
	var setting = {
			view: {
				dblClickExpand: false,
			},
			data: {
				simpleData: {
		            enable:true,
		            idKey:"id",
		            pIdKey:"pid"
		    	},
		        key:{name:"name"}
			},
			check: {
				enable: true,
				chkStyle: "checkbox"
			},
			callback: {
				onCheck: function(){
					var pa = $.fn.zTree.getZTreeObj("patentZtree_"+num);
					if(pa!=null){
						$("#patentMsg_"+num).html("");
						$("#patentIpc_"+num).val(getCheckNode(pa));
					}
				}
			}
	};
	//专利中途分类
	$.fn.zTree.init($("#patentZtree_"+num), setting, data.ztreeJson);
}


//期刊中图分类树
function findPerioSubject(count,i){
	var num=count+"_"+i
	if(perioZtr==null){
		$.ajax({
			type : "post",
			data : {num:num},
			async:false,
			url : "../auser/findPerioSubject.do",
			dataType : "json",
			beforeSend : function(XMLHttpRequest) {},
			success:function(data){
				setPerioSubject(data,num);
			}
		});
	}else{
		setPerioSubject(perioZtr,num);
	}
}
function setPerioSubject(data,num){
	var setting = {
			view: {
				dblClickExpand: false,
			},
			data: {
				simpleData: {
		            enable:true,
		            idKey:"id",
		            pIdKey:"pid"
		    	},
		        key:{name:"name"}
			},
			check: {
				enable: true,
				chkStyle: "checkbox"
			},
			callback: {
				onCheck: function(){
					var qk = $.fn.zTree.getZTreeObj("perioInfoZtree_"+num);
					if(qk!=null){
						$("perioMsg_"+num).html("");
						$("#perioInfoClc_"+num).val(getCheckNode(qk));
					}
				}
			}
	};
	//期刊中途分类
	$.fn.zTree.init($("#perioInfoZtree_"+num), setting, data.ztreeJson);
}

function validateFrom(){
	var bool = false;
	if(fieldsCheck()){
		bool = true;
	}else{
		bool = false;
	}
	return bool;
}

function fieldsCheck() {
	var bootstrapValidator = $("#fromList").data('bootstrapValidator');
	bootstrapValidator.validate();
	return (bootstrapValidator.isValid());
}

function checkemail(str){
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if(reg.test(str)){
		return true;
	}else{
		return false;
	}
}

function radioClick(isBatch){
	var $selectedvalue = $("input[name='managerType']:checked").val();
	$("#adminOldName").val("");
	$("#adminname").val("");
	$("#adminpassword").val("");
	$("#adminIP").val("");
	$("#adminEmail").val("");
	//去除验证提示
	resetAdminValidate();
	if ($selectedvalue =="new") {
		$("#oldManager").hide();
		$("#newManager").show();
	}else {
		var institution=$("#institution").val();
		if(isBatch!="batch" &&(institution==undefined || ""==institution || null==institution)){
			$("input:radio[value='new']").prop('checked','true');
			layer.msg("请输入机构名称",{icon: 2});
			return;
		}
		if(isBatch=="batch"){
			institution="";
		}
		$.ajax({
			type:"post",
			async: false,
			url:"../auser/getOldAdminNameByInstitution.do",
			data:{"institution":institution,"batch":isBatch},
			dataType:"json",
			success:function(data){
				if(null==data || ""==data){
					layer.msg("该机构下没有机构管理员,建议添加新的机构管理员",{icon: 2});
					$("input:radio[value='new']").prop('checked','true');
					$("#oldManager").hide();
					$("#newManager").show();
				}else{
					$("#adminOldName option").remove();
					if(isBatch=="batch"){
						$("#adminOldName").append("<option value=''>--请选择--</option>");
					}
					for(var i in data){
						$("#adminOldName").append("<option value='"+data[i].userId+"'>"+data[i].userId+"</option>");
					}
					if(isBatch=="batch"){
						$("#institution").val(data[i].institution);
					}else{
						getAdmin($("#adminOldName").get(0));
					}
					$("#newManager").hide();
					$("#oldManager").show();
				}
			}
		});
	}
}

/**
 * 获取被选中的学科分类号
 * @param treeObj
 * @returns
 */
function getCheckNode(treeObj){
	var nodes = treeObj.getNodes();
	var text = new Array();
	for(var i=0;i<nodes.length;i++){
		var node=nodes[i];
		if(node.checked){
			Array.prototype.push.apply(text,getListNode(node));
		}
	}
	return text;
}

/**
 * 期刊限定(选刊，选论文)
 * @param type
 */
function changePerioClc(obj,type){
	if(type==0){
		$("#button0"+obj).removeClass('btn-success2');
		$("#button1"+obj).addClass('btn-success2');
		$("#perioInfoDiv"+obj).show();
		$("#perioDiv"+obj).hide();
		$("#journalIdDiv"+obj).show();
	}else if(type==1){
		$("#button1"+obj).removeClass('btn-success2');
		$("#button0"+obj).addClass('btn-success2');
		$("#perioDiv"+obj).show();
		$("#perioInfoDiv"+obj).hide();
		$("#journalIdDiv"+obj).hide();
	}
}

/**
 * 获取节点下的学科分类号
 * @param treeObj
 * @returns
 */
function getListNode(treeObj){
	var text = new Array();
	var flag=true;
	var temp = new Array();
	var childNodes = treeObj.children;
	if(childNodes){
		for(var i=0;i<childNodes.length;i++){
			var childNode=childNodes[i];
			if(childNode.checked){
				if(childNode.isParent){
					var child=childNode.children;
					if(child){
						var flag1=true;
						var temp1 = new Array();
						for(var j=0;j<child.length;j++){
							if(child[j].checked){
								temp1.push(child[j].value);
							}else{
								flag1=false;
							}
						}
						if(flag1){
							temp.push(childNode.value);
						}else{
							Array.prototype.push.apply(temp,temp1);
							flag=false;
						}
					}
				}else{
					temp.push(childNode.value);
				}
			}else{
				flag=false;
			}
		}
	}
	if(flag){
		text.push(treeObj.value);
	}else{
		Array.prototype.push.apply(text,temp);
	}
	return text;
}

$(document).on("click",function(e){
	if($(e.target).parents(".userId").length==0){
		$(".quota").css("display","none");
		$(".arrow").css({"background-position-x":"-10px"});
	}
})

//鼠标经过有提示
function  showFont(){
	$(".mechanism_id").hover(function(){
		if($('.index:checked').length!=0){
			$(".show_mechanism").text($(".enshrine").text());
			$(".show_mechanism").show();
		}
	},function(){
		$(".show_mechanism").hide();
	});
}

//机构id点击其他
function commonCaption(e) {
	var all_index= $('.jg_index').length;
	var num= $('.index:checked').length;
	var curText = e.next().text();
	if(e.is(':checked')){
		$(".mechanism_id").css("border-color","#00a65a");
		$(".bind_numm").css("color","#00a65a");
		$(".wrongm").css("background","url(../img/t.png)");
		$(".wrongm").css("margin-left","10px");
		$(".wrongm").css("display","inline");
		$(".mistakenm").css("display","none");
		$(".data_first").css("display","block");
		$('.enshrine').append('<span class="indexitemText" data-text='+curText+'>'+curText+","+'</span>');
		if(all_index==num){
			if(all_index==1){
				$(".data_first").css("display","none");
				$(".enshrine").text($(".quota li:eq(1)").text());
			}else {
				$(".tol_quota").prop("checked",true);
				$('.indexitemText').remove();
				$(".enshrine").text("全部");
			}
		}
	}else {
		$('.tol_quota').prop("checked","");
		$('.allSelectText').text('');
		$('.enshrine span').each(function () {
			if(curText == $(this).data('text')){
				$(this).remove();
			}
		});
		$('.enshrine').text('');
		$('.index:checked').each(function(){
			curText = $(this).next().text();
			$('.enshrine').append('<span class="indexitemText" data-text='+curText+'>'+curText+","+'</span>');
		});
		if($(".enshrine").text()==""){
			$(".mechanism_id").css("border-color","#dd4b39");
			$(".bind_numm").css("color","#dd4b39");
			$(".wrongm").css("background","url(../img/f.png)");
			$(".wrongm").css("margin-left","8px");
			$(".mistakenm").css("display","inline");
			$(".wrongm").css("display","inline");
			$(".mistakenm").text("机构ID不能为空");
		}else {
			$(".mechanism_id").css("border-color","#00a65a");
			$(".bind_numm").css("color","#00a65a");
			$(".wrongm").css("background","url(../img/t.png)");
			$(".wrongm").css("margin-left","10px");
			$(".wrongm").css("display","inline");
			$(".mistakenm").css("display","none");
		}
	}
}
//点击箭头变化
function icont(){

	if(length==0){
		$(".arrow").css({"background-position-x":"-10px"});
		$(".quota").hide();
	}
	else {
		$(".arrow").css({"background-position-x":"-39px"});
		$(".quota").toggle();
	}
	if($.trim($(".quota").css("display"))=="none"){
		$(".arrow").css({"background-position-x":"-10px"});
	}
}


/******************/

//开通微信公众号嵌入服务
function checkWeChat(obj,type) {
	$("#weChatEamil").val("");
	if ($(obj).is(':checked')) {
		if($("#loginMode").val()!=1){
			$("#openWeChatspan").html("该机构账号登录方式不是用户名密码，不能开通此权限");
			$(obj).prop("checked",false);
			return;
		}
		$("#wechatDiv").show();
		if(type==0){
			$("#sendMail").prop("checked",true);
		}else{
			$("#sendMail").prop("checked",false);
		}
		$("#weChatBegintime").val(getData());
		// 试用
		if($('#weChatIsTrialCheck').is(':checked')) {
			$('#weChatIsTrial').val('isTrial')
		}else {
			$('#weChatIsTrial').val('notTrial')
		}
	} else {
		resetValidate('wechat')
		$("#wechatDiv").hide();
		$('#weChatIsTrial').val('')
		$('#weChatIsTrialCheck').prop('checked',false)
		$('#weChatBegintime').val('')
		$('#weChatEndtime').val('')
	}
	$("#openWeChatspan").html("");
}
$(function(){
    var meDatePicker = (function () {
        return {
            datePicker: function (target, obeject, isStartTime) {
                var compareTime = function (startdate, enddate) {
                    return (Date.parse(startdate) < Date.parse(enddate)) ? true : false;
                }
                target.focus(function () {
                    WdatePicker({
                        autoUpdateOnChanged: false,
                        readOnly: false,
                        onpicked: function () {
                            if (isStartTime) {
                                if (!compareTime(target.val(), obeject.val()) && obeject.val()) {
                                    obeject.val(target.val());
                                }
                            } else {
                                if (!compareTime(obeject.val(), target.val()) && obeject.val()) {
                                    obeject.val(target.val());
                                }
                            }
                            $(this).blur()
                        }
                    })
                })
            }
        }
    })();
    meDatePicker.datePicker($('#openBindStart'), $('#openBindEnd'), true);
    meDatePicker.datePicker($('#openBindEnd'), $('#openBindStart'), false);
    meDatePicker.datePicker($('#adminBegintime'), $('#adminEndtime'), true);
    meDatePicker.datePicker($('#adminEndtime'), $('#adminBegintime'), false);
})

//开通APP嵌入服务
function checkApp(obj){
	if ($(obj).is(':checked')) {
		if($("#loginMode").val()!=1){
			$(obj).prop("checked",false);
			$("#openAppspan").html("该机构账号登录方式不是用户名密码，不能开通此权限");
			return;
		}else{
			$("#appBegintime").val(getData());
			$("#openAppDiv").show();
		}
		// 试用
		if($('#appIsTrialCheck').is(':checked')) {
			$('#appIsTrial').val('isTrial')
		}else {
			$('#appIsTrial').val('notTrial')
		}
	}else{
		resetValidate('app')
		$("#openAppDiv").hide();
		// 试用
		$('#appIsTrialCheck').prop('checked',false)
		$('#appIsTrial').val('')
		$('#appBegintime').val('')
		$('#appEndtime').val('')
	}
	$("#openAppspan").html("");
}

//选择购买项目
function selectType(obj){
	var val=$(obj).val();
	if(val==""){
		$("#resourcePurchaseType").hide();
		return;
	}
	$("#resourcePurchaseType").html('<option value="">-请选择-</option>');
	$.ajax({
		type:"post",
		async: false,
		url:"../auser/getProject.do",
		data:{"val":val},
		dataType:"json",
		success:function(data){
			for(var i in data){
				var pro=data[i];
				$("#resourcePurchaseType").append('<option proid="'+pro.productDetail+'" type="'+pro.resourceType+'" value="'+pro.type+'" id="'+pro.id+'">'+pro.name+'</option>');
			}
			$("#resourcePurchaseType").show();
		}
	});
}

//获取机构管理员信息
function getAdmin(obj){
	var userId=$(obj).val();
	if(userId==""){
		$("#adminpassword").val("");
		$("#adminIP").val("");
		$("#adminEmail").val("");
		return;
	}
	$.ajax({
		type:"post",
		async: false,
		url:"../auser/getAdmin.do",
		data:{"userId":userId},
		dataType:"json",
		success:function(data){
			$("#adminpassword").val(data.password);
			var ip="";
			if(data.adminIP!=null){
				for(var i in data.adminIP){
					if(ip!=""){
						ip+="\r\n";
					}
					ip+=data.adminIP[i].beginIpAddressNumber+"-"+data.adminIP[i].endIpAddressNumber;
				}
			}
			$("#adminIP").val(ip);
			$("#adminEmail").val(data.adminEmail);
			$("#institution").val(data.institution);
		}
	});
}
//党建管理员
function checkParty(obj){
	$("#partyAdmin").val("");
	$("#partyPassword").val("");
	$("#partyBegintime").val("");
	$("#partyEndtime").val("");
	$("input:radio[name=isTrial]").prop('checked',false);
	if($(obj).is(':checked')) {
		$("#partyBegintime").val(getData());
		$("#partyDiv").show();
		$("input:radio[value=notTrial]").prop("checked",true);
		// 试用
		if($('#partyIsTrialCheck').is(':checked')) {
			$('#partyIsTrial').val('isTrial')
		}else {
			$('#partyIsTrial').val('notTrial')
		}
	}else{
		resetValidate('party');
		$("#partyDiv").hide();
		$("#partyIsTrial").val('')
		$('#partyIsTrialCheck').prop('checked',false)
		$('#partyBegintime').val('')
		$('#partyEndtime').val('')
		$('#partyAdmin').val('')
		$('#partyPassword').val('')
		
	}
}
//工单类型
function selectOrder(obj){
	var type=$("#OrderType").val();
	var msg="";
	if(type=='crm'){
		$("#orderTypeSpan").html("CRM工单号");
		msg='CRM工单号不能为空，请填写CRM工单号';
	}else{
		$("#orderTypeSpan").html("申请部门");
		msg='申请部门不能为空，请填写申请部门';
	}
	$("#OrderContent").val("");
	var bootstrapValidator = $("#fromList").data('bootstrapValidator');
	$("#fromList").bootstrapValidator("addField","OrderContent", {
		validators: {notEmpty: {message: msg}}
	});
}
//选择国家
var arrayArea="";
function selectRegion(obj){
	var region=$(obj).val();
	if(region=='foreign'){
		arrayArea=$("#PostCode").html();
		$("#PostCode").html('<option value="none">无</option>');
		$("#fromList").data("bootstrapValidator").updateStatus("PostCode","NOT_VALIDATED",null);
	}else{
		if(arrayArea==""||arrayArea==null){
			arrayArea=$("#allRegion").html();
		}
		$("#PostCode").html(arrayArea);
		var bootstrapValidator = $("#fromList").data('bootstrapValidator');
		$("#fromList").bootstrapValidator("addField","PostCode", {
			validators: {notEmpty: {message: "地区不能为空，请选择地区"}}
		});
		bootstrapValidator.validate();
	}
}
var errorIP="";
//校验IP是否存在交集
function validateIp(ip,userId,object){
	var loginMode = $("#loginMode").val();
	errorIP="";
	if(loginMode==0){
		var bool = false;
		$.ajax({
			type : "post",
			async:false,
			url : "../auser/validateip.do",
			data:{ip:ip,userId:userId},
			dataType : "json",
			success: function(data){
				if(data.flag=="true"){
					bool = true;
					var msg="";
					if(data.tableIP!=null){
						errorIP=data.errorIP;
						msg="<font style='color:red'>以下IP段存在冲突</font></br><font style='color:#000000'>"+data.errorIP+"</font><font style='color:red'>相冲突账号</font></br><font style='color:#000000'>"+data.tableIP+"</font>";
					}else{
						msg="<font style='color:red'>IP格式错误:</font></br><font style='color:#000000'>"+data.errorIP+"</font>";
					}
					layer.tips(msg, object, {
						tips: [3, '#FFFFFF'],
						area: ['350px', ''], //宽高
						closeBtn :1,
						time: 0
					});
				}else{
					layer.tips("<font style='color:#000000'>无冲突</font></br>", object, {
						tips: [3, '#FFFFFF'],
						area: ['350px', ''], //宽高
						closeBtn :1,
						time: 0
					});
					bool = false;
				}
			}
		});
		return bool;
	}
}

function validateIpChange(data,object){
	var loginMode = $("#loginMode").val();
	errorIP="";
	if(loginMode==0){
		var bool = false;
		$.ajax({
			type : "post",
			async:false,
			cache: false,  
	        processData: false,
	        contentType: false,
			url : "../auser/validateip.do",
			data:data,
			dataType : "json",
			success: function(data){
				if(data.flag=="true"){
					bool = true;
					$('#IpErrorInfo').html('');
					layer.closeAll();
					var msg="";
					if(data.tableIP!=null){
						errorIP=data.errorIP;
						msg="<font style='color:red'>以下IP段存在冲突</font></br><font style='color:#000000'>"+data.errorIP+"</font><font style='color:red'>相冲突账号</font></br><font style='color:#000000'>"+data.tableIP+"</font>"
						msg += "<font style='color:red'>相冲突购买项目</font></br><font style='color:#000000'>"+data.tableProject+"</font>";
						$('#IpErrorInfo').append(msg)
					}else{
						errorIP=data.errorIP;
						msg="<font style='color:red'>IP格式错误:</font></br><font style='color:#000000'>"+data.errorIP+"</font>";
						$('#IpErrorInfo').append(msg)
					}
					
				}else if(data.flag === 'false'){
					layer.tips("<font style='color:#000000'>无冲突</font></br>", "#checkIp", {
						tips: [3, '#FFFFFF'],
						area: ['350px', ''], //宽高
						closeBtn :1,
						time: 0
					});
					bool = false;
				}else if (data.flag === 'fail') {
					layer.msg(data.fail, {icon: 2});
					bool = false;
					}
				}
		});
		return bool;
	}
}
//校验多行ip对
var IpArray=new Array();
function IpFormat(str){
	IpArray=new Array();
	var ipLimigLineRegex = /^\d{1,3}\.\d{1,3}\.\d{1,3}.\d{1,3}\s*-\s*\d{1,3}\.\d{1,3}.\d{1,3}.\d{1,3}\s*$/i;
	var ip = str.split("\n");
	var isTrue=true;
	for(i in ip){
		if(ip[i]!=""){
			var ips=ip[i].split("-");
			if(ips.length!=2){
				IpArray.push(ip[i]);
				isTrue=false;
				continue;
			}else{
				if(ips[0].split(".").length!=4||ips[1].split(".").length!=4){
					IpArray.push(ip[i]);
					isTrue=false;
					continue;
				}
			}
			if(ipLimigLineRegex.test(ip[i])){
				continue;
			}else{
				IpArray.push(ip[i]);
				isTrue=false;
			}
		}
	}
	return isTrue;
}

//检查IP
function checkIP(){
	var ip = $("#ipSegment").val();
	if(ip==""){
		var msg="<font style='color:red'>IP不存在</font>";
		layer.tips(msg, "#checkIp", {
			tips: [3, '#FFFFFF'],
			area: ['350px', ''], //宽高
			closeBtn :1,
			time: 0
		});
		return;
	}
	if(!IpFormat(ip)){
		var  msg="<font style='color:red''>IP格式错误：</font>";
		for(var ar in IpArray){
			msg+="<br><font style='color:#000000'>"+IpArray[ar]+"</font>";
		}
		layer.tips(msg, "#checkIp", {
			tips: [3, '#FFFFFF'],
			area: ['350px', ''], //宽高
			closeBtn :1,
			time: 0
		});
		return;
	}
	var userId = $("#userId").val();
	validateIp(ip,userId,"#checkIp");
}
$(function(){
    var meDatePicker = (function () {
        return {
            datePicker: function (target, obeject, isStartTime) {
                var compareTime = function (startdate, enddate) {
                    return (Date.parse(startdate) < Date.parse(enddate)) ? true : false;
                }
                target.focus(function () {
                    WdatePicker({
                        autoUpdateOnChanged: false,
                        readOnly: false,
                        onpicked: function () {
                            if (isStartTime) {
                                if (!compareTime(target.val(), obeject.val()) && obeject.val()) {
                                    obeject.val(target.val());
                                }
                            } else {
                                if (!compareTime(obeject.val(), target.val()) && obeject.val()) {
                                    obeject.val(target.val());
                                }
                            }
                            $(this).blur()
                        }
                    })
                })
            }
        }
    })();
    meDatePicker.datePicker($('#openBindStart'), $('#openBindEnd'), true);
    meDatePicker.datePicker($('#openBindEnd'), $('#openBindStart'), false);
})

//剔除IP
function deleteIP(){
	layer.closeAll();
	var ipSegment = $("#ipSegment").val();
	if (ipSegment == "") {
		return;
	}
	var ips = ipSegment.split("\n");
	var array = errorIP.split('</br>');
	var ipHtml = "";
	for (var ip in ips) {
		if(ips[ip]==""){
			continue;
		}
		var flag = false;
		for(var ar in IpArray){
			if(IpArray[ar]==""){
				continue;
			}
			if (ips[ip]==IpArray[ar]) {
				flag = true;
			}
		}
		for (var ar in array) {
			if(array[ar]==""){
				continue;
			}
			if (ips[ip]==array[ar]) {
				flag = true;
			}
		}
		if (!flag) {
			ipHtml += ips[ip] + "\n";
		}
	}
	$("#ipSegment").val(ipHtml);
}
//验证次数
var maxData=99999999;
function checkNum(obj,num){
	var val=obj.value;
	var flag=false;
	if(val.substr(0,1)=="-"){
		flag=true;
	}
	val= val.replace(/[^\d]/g,""); //清除"数字"和"."以外的字符
	val = val.replace(/^\./g,""); //验证第一个字符是数字
	val = val.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
	val = val.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	if(parseFloat(val)>maxData){
		val=val.substring(0,8);
	}
	obj.value=(flag&&(num==1)?"-":"")+val;
}
//验证金额
function checkMoney(obj,num){
	var val=obj.value;
	var flag=false;
	if(val.substr(0,1)=="-"){
		flag=true;
	}
	val= val.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
	val = val.replace(/^\./g,""); //验证第一个字符是数字
	val = val.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
	val = val.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	val = val.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
	if(parseFloat(val)>maxData){
		val=val.substring(0,8);
	}
	obj.value=(flag&&(num==1)?"-":"")+val;
}
//校验ip
function validateIPS(obj){
	obj.value=obj.value.replace(/[^.0-9\r\n-]/g,'')
	layer.closeAll();
}
//批量展示错误信息
function showError(data){
	var html='<table border="1" style="margin:0 auto;"><tr style="text-align:center;font-weight:900;"><td width="10%" style="padding-top:6px;padding-bottom:6px;">机构账号ID</td><td width="10%">机构名称</td><td width="30%">错误信息</td></tr>';
	for(var i in data){
		var userId=data[i].userId==undefined?"":data[i].userId;
		var institution=data[i].institution==undefined?"":data[i].institution;
		var fail=data[i].fail==undefined?"":data[i].fail;
		html += '<tr style="text-align:left;"><td style="padding-top:6px;padding-bottom:6px;">'+userId+'</td><td>'+institution+'</td><td style="color:red;">'+fail+'</td></tr>';
	}
	html+="</table>";
	$("#errorList").html(html);
}

// 检测ip冲突
function checkIPError () {
	layer.closeAll();
	$('#IpErrorInfo').html('')
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
    var ip = $("#ipSegment").val();
	if(ip==""){
		var msg="<font style='color:red'>IP不存在</font>";
		layer.tips(msg, "#checkIp", {
			tips: [3, '#FFFFFF'],
			area: ['350px', ''], //宽高
			closeBtn :1,
			time: 0
		});
		return;
	}
	if(!IpFormat(ip)){
		var  msg="<font style='color:red''>IP格式错误：</font>";
		for(var ar in IpArray){
			msg+="<br><font style='color:#000000'>"+IpArray[ar]+"</font>";
		}
		layer.tips(msg, "#checkIp", {
			tips: [3, '#FFFFFF'],
			area: ['350px', ''], //宽高
			closeBtn :1,
			time: 0
		});
		return;
	}
    validateIpChange(data,"#checkIp");
}

// 剔除冲突ip
function deleteIPError() {
	layer.closeAll();
	$('#IpErrorInfo').html('')
	var ipSegment = $("#ipSegment").val();
	if (ipSegment == "") {
		return;
	}
	var ips = ipSegment.split("\n");
	var array = errorIP.split('</br>');
	var ipHtml = "";
	for (var ip in ips) {
		if(ips[ip]==""){
			continue;
		}
		var flag = false;
		for(var ar in IpArray){
			if(IpArray[ar]==""){
				continue;
			}
			if (ips[ip]==IpArray[ar]) {
				flag = true;
			}
		}
		for (var ar in array) {
			if(array[ar]==""){
				continue;
			}
			if (ips[ip].indexOf(array[ar])>-1) {
				flag = true;
			}
		}
		if (!flag) {
			ipHtml += ips[ip] + "\n";
		}
	}
	$("#ipSegment").val(ipHtml);
}
function closeLayer () {
	layer.closeAll();
}

function openEnterpriseLike(obj){
	var userId = $("#userId").val();
	if(userId==''){
		alert('机构Id不能为空，请填写规范的机构Id');
			return;
	}
	var reg=/^[A-Za-z0-9_]*$/;
	if(!reg.test(userId)){
		alert('机构Id格式不对，请填写规范的机构Id');
		return;
	}
	$.ajax({
		type:"post",
		async: false,
		url:"../group/openEnterpriseLike.do",
		data:{
			  "userId":userId,
			  "enterpriseType":obj,
		},
		success : function (data){
			window.open(data);
		}
	});
}
function databaseClick(count, i) {
	if (!$(event.target).is(":checked")) {
		$('#databaseInput_'+count+'_'+i).prop('checked',false)
		$('#databaseInput_'+count+'_'+i).attr('data-database', '')
		$('#checkData_'+count+'_'+i).prop('disabled', true)
	}
}
//试用按钮
function changeVal(obj) {
	if($('#'+obj).val()=='notTrial') {
		$('#'+obj).val('isTrial')
	}else {
		$('#'+obj).val('notTrial')
	}
}
// 重置校验规征
function resetValidate(val) {
	if(val=='party') {
		$("#fromList").data("bootstrapValidator").updateStatus("partyEndtime","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("partyAdmin","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("partyPassword","NOT_VALIDATED",null);
	}else if (val=='child') {
		$("#fromList").data("bootstrapValidator").updateStatus("sEndtime","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("upperlimit","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("sConcurrentnumber","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("downloadupperlimit","NOT_VALIDATED",null);
	}else if (val=='person') {
		$("#fromList").data("bootstrapValidator").updateStatus("openBindEnd","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("bindValidity","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("downloadLimit","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("resourceType","NOT_VALIDATED",null);
	}else if (val=='app') {
		$("#fromList").data("bootstrapValidator").updateStatus("appEndtime","NOT_VALIDATED",null);
	} else if (val=='wechat') {
		$("#fromList").data("bootstrapValidator").updateStatus("weChatEndtime","NOT_VALIDATED",null);
		$("#fromList").data("bootstrapValidator").updateStatus("weChatEamil","NOT_VALIDATED",null);
	}
}