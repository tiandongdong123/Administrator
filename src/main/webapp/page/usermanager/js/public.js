var perioZtr,degreeZtr,confZtr,patentZtr,bookZtr;var count = 0;
$(function(e){
	$("input[name='quotaName']").prop("checked",true);
	//是否开通管理员
	$("#checkuser").click(function(){
		if($(this).is(':checked')){
			$("#administrator").show();
		}else{
			$("#adminname").val("");
			$("#adminpassword").val("");
			$("#adminIP").val("");
			$("#adminEmail").val("");
			$("#administrator").hide();
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
		}else{
			$("#user_dinding").val("false");
			$("#bindAuthority").val("");
			$("#resourceInherited").prop("checked",true).siblings().prop("checked",false);
			$("#bindLimit").val("1");
			$("#bindLimit").val("");
			$("#bindValidity").val("");
			$("#downlaodLimit").val("");
			$("#dinding").hide();
		}
	})

	//设置机构账号并发数
	$("#checkp").click(function(){
		if($(this).is(':checked')){
			$("#pconcurrent_div").show();
		}else{
			$("#pConcurrentcountber").val("");
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
			$("#upperlimit").val("100");
			$("#sConcurrentnumber").val("1");
			$("#downloadupperlimit").val("30");
			$("#chargebacks").val("0");
			$("#sconcurrent_div").show();
		}else{
			$("#upperlimit").val("");
			$("#sConcurrentcountber").val("");
			$("#downloadupperlimit").val("");
			$("#chargebacks").val("");
			$("#sconcurrent_div").hide();
		}
	});
	//机构id点击全部
	$(".tol_quota").click(function(){
		$(".allSelectText").text("");
		$("input[name='quotaName']").prop("checked",$(".tol_quota").prop("checked"));
		if($(".tol_quota").is(":checked")){
			$(".enshrine").text("全部");
		}
		else{
			$(".enshrine").text("");
		}
	})
	//机构id点击其他
	$(".index").click(function(){
		commonCaption($(this));
	});
	$(".mechanism_id").hover(function(){
		if($(".enshrine").text()==''){
			$(".show_mechanism").hide();
		}else {
			$(".show_mechanism").text($(".enshrine").text());
			$(".show_mechanism").show();
		}
	},function(){
		$(".show_mechanism").hide();
	})

});
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
		$(".wrong").css("display","inline");
		$(".mistaken").css("display","none");
	}
}

function style(){
	$(".bind_num").css("color","#dd4b39");
	$("#bindLimit").css("border-color","#dd4b39");
	$(".wrong").css("background","url(../img/f.png)");
	$(".mistaken").css("display","inline");
	$(".wrong").css("display","inline");
}
//机构id点击其他
function commonCaption(e) {
	var all_index= $('.index').length;
	var num= $('.index:checked').length;
	var curText = e.next().text();

	if(all_index==num==0){
		$(".tol_quota").prop("checked",false);
		$(".allSelectText").text("");
	}
	if(e.is(':checked')){
		$('.enshrine').append('<span class="indexitemText" data-text='+curText+'>'+curText+","+'</span>');
		if(all_index==num){
			$(".tol_quota").prop("checked",true);
			$('.indexitemText').remove();
			$(".enshrine").text("全部");
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
	}
}
//统计分析
function checkTj(value){
	if(value=="all"){
		var bool=$("#statistics1").is(':checked');
		$("#tongji").val(bool?"AB":"");
		$("#statistics2").prop('checked',bool);
		$("#statistics3").prop('checked',bool);
	}else{
		var statistics2=$("#statistics2").is(':checked');
		var statistics3=$("#statistics3").is(':checked');
		if(statistics2&&statistics3){
			$("#tongji").val("AB");
		}else if(statistics2){
			$("#tongji").val("A");
		}else if(statistics3){
			$("#tongji").val("B");
		}else{
			$("#tongji").val("");
		}
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


//标准
function standardShow(count,i,id){
	resetStandard(count,i);
	$("#stand_div_"+count+"_"+i).hide();
	$("#isBK").hide();
	$("#isZJ").hide();
	if(id=="isZJ"){
		if($("#isZJ_"+count+"_"+i).is(':checked')){
			$("#isZJ").show();
			$("#stand_div_"+count+"_"+i).show();
			$("#isBK_"+count+"_"+i).attr("checked",false);
		}
	}else if(id=="isBK"){
		if($("#isBK_"+count+"_"+i).is(':checked')){
			$("#readingPrint_"+count+"_"+i).val("0");
			$("#onlineVisitor_"+count+"_"+i).val("-1");
			$("#copyNo_"+count+"_"+i).val("-1");
			$("#totalPrintNo_"+count+"_"+i).val("-1");
			$("#singlePrintNo_"+count+"_"+i).val("-1");
			$("#isBK").show();
			$("#stand_div_"+count+"_"+i).show();
			$("#isZJ_"+count+"_"+i).attr("checked",false);
		}
	}
}

//标准配置重置
function resetStandard(count,i){
	$("#company_"+count+"_"+i).val("");
	$("#orgName_"+count+"_"+i).val("");
	$("#companySimp_"+count+"_"+i).val("");
	$("#fullIpRange_"+count+"_"+i).val("");
	$("#limitedParcelStarttime_"+count+"_"+i).val("");
	$("#limitedParcelEndtime_"+count+"_"+i).val("");
	$("#readingPrint_"+count+"_"+i).val("");
	$("#onlineVisitor_"+count+"_"+i).val("");
	$("#copyNo_"+count+"_"+i).val("");
	$("#totalPrintNo_"+count+"_"+i).val("");
	$("#singlePrintNo_"+count+"_"+i).val("");
}

//校验标准内容
function validStandard(count,i){
	//1、时间不能为空，而且还是时间要小于等于结束时间
	var isZJ=$("#isZJ_"+count+"_"+i).is(':checked');
	var isBK=$("#isBK_"+count+"_"+i).is(':checked');
	if (isZJ || isBK) {
		var startTime=$("#limitedParcelStarttime_"+count+"_"+i).val();
		var endTime=$("#limitedParcelEndtime_"+count+"_"+i).val();
		if(startTime==null||startTime==""){
			layer.msg("开始时间不能为空",{icon: 2,time: 2000});
			return false;
		}
		if(endTime==null||endTime==""){
			layer.msg("结束时间不能为空",{icon: 2,time: 2000});
			return false;
		}
		var d1 = new Date(startTime.replace(/\-/g, "\/"));
		var d2 = new Date(endTime.replace(/\-/g, "\/"));
		if(d1>d2){
			layer.msg("开始时间不能大于结束时间",{icon: 2,time: 2000});
			return false;
		}
	}
	//2、元数据和包库的要分别校验
	if (isZJ) {// 1、元数据+全文
		var company=$("#company_"+count+"_"+i).val();
		var orgName=$("#orgName_"+count+"_"+i).val();
		var companySimp=$("#companySimp_"+count+"_"+i).val();
		if(company==null||company==""){
			layer.msg("单位名称不能为空",{icon: 2,time: 2000});
			return false;
		}
		if(orgName==null||orgName==""){
			layer.msg(" 机构名称不能为空",{icon: 2,time: 2000});
			return false;
		}else{
			if(/.*[\u4e00-\u9fa5]+.*$/.test(orgName)){
				layer.msg(" 机构名称不能包含汉字",{icon: 2,time: 2000});
				return false;
			}
		}
		if(companySimp==null||companySimp==""){
			layer.msg("机构单位简称不能为空",{icon: 2,time: 2000});
			return false;
		}else{
			if(companySimp.length<5 || companySimp.length>30){
				layer.msg("机构单位简称的长度必须在5到30之间",{icon: 2,time: 2000});
				return false;
			}
			if(/.*[\u4e00-\u9fa5]+.*$/.test(companySimp)){
				layer.msg(" 机构单位简称不能包含汉字",{icon: 2,time: 2000});
				return false;
			}
		}
	} else if (isBK) {// 2、包库
		var readingPrint=$("#readingPrint_"+count+"_"+i).val();
		if(readingPrint==null||readingPrint==""){
			layer.msg("版权阅读打印不能为空",{icon: 2,time: 2000});
			return false;
		}
	}
	//3、ip不能为空
	if (isZJ || isBK) {
		var fullIpRange=$("#fullIpRange_"+count+"_"+i).val();
		if(fullIpRange==null||fullIpRange==""){
			layer.msg("ip段必须填写",{icon: 2,time: 2000});
			return false;
		}
	}
	return true;
}

//验证标准机构是否合法
function checkOrg(count,i){
	var orgName=$("#orgName_"+count+"_"+i).val();
	var companySimp=$("#companySimp_"+count+"_"+i).val();
	var userId=$("#userId").val();
	if(orgName==""&& companySimp==""){
		return true;
	}
	var oldOrgName=$("#h_orgName_"+count+"_"+i).val();
	var oldCom=$("#h_companySimp_"+count+"_"+i).val();
	if(orgName==oldOrgName&&companySimp==oldCom){
		return true;
	}
	$.ajax({
		type : "post",
		data : {userId:userId,orgName:orgName,companySimp:companySimp},
		async:false,
		url : "../auser/findStandardUnit.do",
		dataType : "json",
		success:function(data){
			if(data.result!="0"){
				layer.msg(data.msg,{icon: 2});
			}
			if(data.result=="1"){
				$("#orgName_"+count+"_"+i).focus();
			}else if(data.result=="2"){
				$("#companySimp_"+count+"_"+i).focus();
			}
		}
	});
}

//登录方式切换
function switchcs(obj){
	$("#ipSegment").val("");
	if($(obj).val()=="1"){
		$("#upass").show().siblings().hide();
	}else if($(obj).val()=="0"){
		$("#password").val("");
		$("#ipvalue").show().siblings().hide();
	}else{
		$("#upass").show();
		$("#ipvalue").show();
	}
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
		    type: 1, //page层 1div，2页面submitForm()
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
	}else{
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '600px'],
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
		var text = '';
		if($(obj).val()!=""){
			text += '<div class="balance_block" name="full_div">';
			text += '<div class="resources_title"><input type="hidden" name="rdlist['+count+'].projectid" id="pro_'+projectid+'" value="'+projectid+'"><span>'+proname+'</span>';
			text += '<input type="hidden" name="rdlist['+count+'].projectname" value="'+proname+'"><input type="hidden" name="rdlist['+count+'].projectType" value="'+$(obj).val()+'">';
			text += '<button type="button" class="btn btn-primary" style="margin-left:1000px;" onclick="delDiv(this,\''+count+'\',1);">删除</button></div>';
			text += '<div class="time_block"><div class="time_input">';
			text += '<span><b>*</b>时限</span><input type="text" class="Wdate" value="'+getData()+'" name="rdlist['+count+'].validityStarttime" id="'+projectid+'_st" onclick="WdatePicker({maxDate:\'#F{$dp.$D('+projectid+'_et)}\'})"/>';
			text += '<span class="to">至</span><input type="text" class="Wdate" name="rdlist['+count+'].validityEndtime" id="'+projectid+'_et" onclick="WdatePicker({minDate:\'#F{$dp.$D('+projectid+'_st)}\'})"></div>';
			if($(obj).val()!="time"&&$(obj).val()!="count"&&flag!='2'){
				text += '<div class="time_input time_money"><span><b>*</b>金额</span><input type="text" name="rdlist['+count+'].totalMoney"></div>';
			}
			if($(obj).val()!="time"&&$(obj).val()!="balance"&&flag!='2'){
				text += '<div class="time_input time_money"><span><b>*</b>次数</span><input type="text" name="rdlist['+count+'].purchaseNumber"></div>';
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
							text += '<a href="javascript:void(0);" onclick="openPurchaseItems(\''+count+'\',\''+i+'\',\''+type+'\');">详情</a>';
							text += '<ul style="display: none;" class="checkbox_list subset_list">';
							for(var n in rp){
								text += '<li><input type="checkbox" '+checked+' name="rdlist['+count+'].rldto['+i+'].productid" value="'+rp[n].rid+'" class="rdlist['+count+'].tableName">'+rp[n].name+'</li>';
							}
							text += '</ul></li>';
							if($(obj).val()=="time"||$(obj).val()=="balance"){
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
	var mydate = new Date();
	//获取当前年
	var year = mydate.getFullYear();
	//获取当前月
	var month = mydate.getMonth()+1;
	//获取当前日
	var date = mydate.getDate();

	var now = year+'-'+getFormatDate(month)+"-"+getFormatDate(date);

	return now;
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
function delDiv(obj,count,flag,payChannelid,type,beginDateTime,endDateTime,institution,userId,projectname,balance){
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
		    		json+="','endDateTime':'"+endDateTime+"','institution':'"+institution;
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
	if(type.indexOf("local chronicles")>-1){
		text += '<li><a href="#localchronicles_'+count+'_'+i+'" data-toggle="tab">地方志限定</a></li>';
	}
	text += '</ul><div class="tab-content">';
	if(type.indexOf("perio")>-1){
		text += '<div class="tab-pane" id="perio_'+count+'_'+i+'">';
		text += '<label>中图分类法</label><ul class="ztree" id="perioZtree_'+count+'_'+i+'"></ul>';
		text += '<textarea placeholder="格式：[A,B,C]" class="form-control" name="rdlist['+count+'].rldto['+i+'].journalClc" id="journalClc_'+count+'_'+i+'"></textarea>';
		text += '<div class="form-group" style="width:60%;">';
		text += '<label>期刊ID</label>';
		text += '<textarea class="form-control" rows="3" name="rdlist['+count+'].rldto['+i+'].journalIdno" id="journalIdno_'+count+'_'+i+'"></textarea></div>';
		text += '<label>期刊年限</label>';
		text += '<div class="time_block">';
		text += '<select name="rdlist['+count+'].rldto['+i+'].journal_startTime" id="journal_startTime_'+count+'_'+i+'"></select>年——';
		text += '<select name="rdlist['+count+'].rldto['+i+'].journal_endTime" id="journal_endTime_'+count+'_'+i+'"></select>年';
		text += '</div></div>';
	}
	if(type.indexOf("degree")>-1){
		text += '<div class="tab-pane" id="degree_'+count+'_'+i+'">';
		text += '<label>中图分类法</label><ul class="ztree" id="degreeZtree_'+count+'_'+i+'"></ul>	';
		text += '<textarea placeholder="格式：[A,B,C]" class="form-control" name="rdlist['+count+'].rldto['+i+'].degreeClc" id="degreeClc_'+count+'_'+i+'"></textarea>';
		text += '<div class="form-group" style="width:60%;">';
		text += '<label>论文类型</label>';
		text += '<div style="height:50px;border:1px solid #C6C6C6;">';
		text += '<input type="checkbox" name="rdlist['+count+'].rldto['+i+'].degreeTypes" value="硕士">硕士论文';
		text += '<input type="checkbox" name="rdlist['+count+'].rldto['+i+'].degreeTypes" value="博士">博士论文';
		text += '<input type="checkbox" name="rdlist['+count+'].rldto['+i+'].degreeTypes" value="博士后">博士后论文';
		text += '</div></div>';
		text += '<label>论文年限</label>';
		text += '<div class="time_block">';
		text += '<select id="degreeStarttime_'+count+'_'+i+'" name="rdlist['+count+'].rldto['+i+'].degreeStarttime"></select>年——';
		text += '<select id="degreeEndtime_'+count+'_'+i+'" name="rdlist['+count+'].rldto['+i+'].degreeEndtime"></select>年';
		text += '</div></div>';
	}
	if(type.indexOf("conf")>-1){
		text += '<div class="tab-pane" id="conf_'+count+'_'+i+'">';
		text += '<label>中图分类法</label><ul class="ztree" id="confZtree_'+count+'_'+i+'"></ul>';
		text += '<textarea placeholder="格式：[A,B,C]" class="form-control" name="rdlist['+count+'].rldto['+i+'].conferenceClc" id="conferenceClc_'+count+'_'+i+'"></textarea>';
		text += '<div class="form-group" style="width:60%;">';
		text += '<label>会议论文集馆藏号</label>';
		text += '<textarea class="form-control" rows="3" name="rdlist['+count+'].rldto['+i+'].conferenceNo" id="conferenceNo_'+count+'_'+i+'"></textarea>';
		text += '</div></div>';
	}
	if(type.indexOf("patent")>-1){
		text += '<div class="tab-pane" id="patent_'+count+'_'+i+'">';
		text += '<div style="padding: 10px;">';
		text += '<div class="wrap">';
		text += '<label>IPC分类法</label><ul class="ztree" id="patentZtree_'+count+'_'+i+'"></ul>';
		text += '<textarea placeholder="格式：[A,B,C]" class="form-control" name="rdlist['+count+'].rldto['+i+'].patentIpc" id="patentIpc_'+count+'_'+i+'"></textarea>';
		text += '</div></div></div>';
	}
	if(type.indexOf("books")>-1){
		text += '<div class="tab-pane" id="book_'+count+'_'+i+'">';
		text += '<label>中图分类法</label><ul class="ztree" id="bookZtree_'+count+'_'+i+'"></ul>';
		text += '<textarea placeholder="格式：[A,B,C]" class="form-control" name="rdlist['+count+'].rldto['+i+'].booksClc" id="booksClc_'+count+'_'+i+'"></textarea>';
		text += '<div class="form-group" style="width:60%;">';
		text += '<label>图书ID</label>';
		text += '<textarea class="form-control" rows="3" name="rdlist['+count+'].rldto['+i+'].booksIdno" id="booksIdno_'+count+'_'+i+'"></textarea>';
		text += '</div></div>';
	}
	if(type.indexOf("standard")>-1){
		text += '<div class="tab-pane" id="standard_'+count+'_'+i+'"><div class="form-group input_block">';
		text += '<label><input type="checkbox" name="rdlist['+count+'].rldto['+i+'].standardTypes" value="WFLocal">行业标准</label>';
		// text += '<label><input type="checkbox" name="rdlist['+count+'].rldto['+i+'].standardTypes" id="isZJ_'+count+'_'+i+'" onclick="standardShow('+count+','+i+',\'isZJ\');" value="质检出版社">元数据+全文(质检)</label>';
		text += '<label><input type="checkbox" name="rdlist['+count+'].rldto['+i+'].standardTypes" id="isBK_'+count+'_'+i+'" onclick="standardShow('+count+','+i+',\'isBK\');" value="质检出版社">网络包库(质检)</label></div>'
		text += '<div style="display:none;" id="stand_div_'+count+'_'+i+'">';
		text += '<div class="form-group input_block"><label><b>*</b>限定时间：</label><input class="Wdate" name="rdlist['+count+'].rldto['+i+'].limitedParcelStarttime" id="limitedParcelStarttime_'+count+'_'+i+'" onclick="WdatePicker()" type="text">';
		text += '<span class="to">至</span><input class="Wdate" name="rdlist['+count+'].rldto['+i+'].limitedParcelEndtime" id="limitedParcelEndtime_'+count+'_'+i+'" onclick="WdatePicker()" type="text"></div>';
		text += '<div id="isZJ"><div class="form-group input_block"><label><b>*</b>单位名称：</label><input type="text" class="form-control input_width" name="rdlist['+count+'].rldto['+i+'].company" id="company_'+count+'_'+i+'"></div>';
		text += '<div class="form-group input_block"><label><b>*</b>机构名称：</label><input type="text" class="form-control input_width" name="rdlist['+count+'].rldto['+i+'].orgName" id="orgName_'+count+'_'+i+'"';
		text += ' onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9_]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^a-zA-Z0-9_]/g,\'\')" onblur="checkOrg('+count+','+i+')">（若账号为中文，则填写全拼）</div>';
		text += '<div class="form-group input_block"><label><b>*</b>机构单位简称：</label><input type="text" class="form-control input_width" name="rdlist['+count+'].rldto['+i+'].companySimp" id="companySimp_'+count+'_'+i+'"';
		text += ' onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9_]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^a-zA-Z0-9_]/g,\'\')"  onblur="checkOrg('+count+','+i+')"></div></div>';
		text += '<div id="isBK"><div class="form-group input_block"><label><b>*</b>版权阅读打印：</label><select class="form-control input_width" name="rdlist['+count+'].rldto['+i+'].readingPrint" id="readingPrint_'+count+'_'+i+'">';
		text += '<option value="0" checked>授权阅读打印</option><option value="1">授权阅读</option><option value="2">授权打印</option><option value="3">未阅读</option></select></div>';
		text += '<div class="form-group input_block"><label>&nbsp;&nbsp;在线用户数：</label><input type="text" class="form-control input_width" name="rdlist['+count+'].rldto['+i+'].onlineVisitor" id="onlineVisitor_'+count+'_'+i+'" value="-1"><span>-1表示不限制</span><br></div>';
		text += '<div class="form-group input_block"><label>&nbsp;&nbsp;副本数：</label><input type="text" class="form-control input_width" name="rdlist['+count+'].rldto['+i+'].copyNo" id="copyNo_'+count+'_'+i+'" value="-1">-1表示不限制<br></div>';
		text += '<div class="form-group input_block"><label>&nbsp;&nbsp;打印总份数：</label><input type="text" class="form-control input_width" name="rdlist['+count+'].rldto['+i+'].totalPrintNo" id="totalPrintNo_'+count+'_'+i+'" value="-1">-1表示不限制<br></div>';
		text += '<div class="form-group input_block"><label>&nbsp;&nbsp;单标准打印数：</label><input type="text" class="form-control input_width" name="rdlist['+count+'].rldto['+i+'].singlePrintNo" id="singlePrintNo_'+count+'_'+i+'" value="-1">-1表示不限制<br></div></div>';
		text += '<div class="form-group" style="width:60%;"><label><b>*</b>质检出版社标准全文IP范围：</label><textarea class="form-control" rows="3" name="rdlist['+count+'].rldto['+i+'].fullIpRange" id="fullIpRange_'+count+'_'+i+'"';
		text += ' onkeyup="this.value=this.value.replace(/[^.0-9\\n\\r-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^.0-9\\n\\r-]/g,\'\')"></textarea></div></div></div>';
	}
	if(type.indexOf("local chronicles")>-1){
		text += '<div class="tab-pane" id="localchronicles_'+count+'_'+i+'">';
		text += '<div style="height:400px;border:1px solid #C6C6C6;">';
		text += '<table align=center height="100%" width="100%" border="0"><tr height="60%" align=center><td width="15%"><input type="radio" name="fzlx" value="1" onclick="getSxdr(1,'+count+','+i+')" checked/>分类筛选</td>';
		text += '<td style="border:1px solid #C6C6C6;width:85%"><table align=center height="100%" width="100%" border="0">';
		text += '<tr><td align=right width="10%">资源分类</td><td style="align:left;width:90%;padding-left:20px;">';
		text += '<input onclick="gazetteerType(this.value,'+count+','+i+')" type="radio" value="" name="rdlist['+count+'].rldto['+i+'].gazetteersType" checked/>全部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		text += '<input onclick="gazetteerType(this.value,'+count+','+i+')" type="radio" value="FZ_New" name="rdlist['+count+'].rldto['+i+'].gazetteersType"/>新方志&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		text += '<input onclick="gazetteerType(this.value,'+count+','+i+')" type="radio" value="FZ_Old" name="rdlist['+count+'].rldto['+i+'].gazetteersType"/>旧方志</td></tr>'
		text += '<tr><td align=right width="10%">地区</td><td style="align:left;width:90%;padding-left:20px;">省 <select id="sheng_'+count+'_'+i+'" onchange="findArea(this.value,1,'+count+','+i+')"><option value="">全部</option></select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		text += '市 <select id="shi_'+count+'_'+i+'" onchange="findArea(this.value,2,'+count+','+i+')"><option value="">全部</option></select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		text += '县 <select id="xian_'+count+'_'+i+'" onchange="saveArea('+count+','+i+')"><option value="">全部</option></select></td></tr>';
		text += '<tr><td align=right width="10%">数据分类</td><td style="align:left;width:90%;padding-left:20px;"><input onclick="getDataType(this.value,'+count+','+i+')" type="radio" value="WFLocalChronicle" name="rdlist['+count+'].rldto['+i+'].drtm" checked/> 整本&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		text += '<input onclick="getDataType(this.value,'+count+','+i+')" type="radio" value="WFLocalChronicleItem" name="rdlist['+count+'].rldto['+i+'].drtm"/> 条目</td></tr>';
		text += '<tr><td align=right width="10%"><span id="zjfl_'+count+'_'+i+'">专辑分类</span></td><td style="align:left;width:90%;padding-left:20px;"><a href="javascript:;" onclick="checkfzAll(this,'+count+','+i+')" id="checkfz_'+count+'_'+i+'">全选</span></td></tr>';
		text += '<tr><td></td><td style="align:left;width:90%;padding-left:20px;height:40%">';
		text += '<div id="new_class_code_'+count+'_'+i+'"></div><div id="item_class_code_'+count+'_'+i+'" style="display:none"></div></td></tr></table></td>';
		text += '</tr><tr height="40%"><td align=center><input type="radio" name="fzlx" value="0" onclick="getSxdr(0,'+count+','+i+')"/>自定义导入</td>';
		text += '<td style="align:left;border:1px solid #C6C6C6;padding-left:5px;">';
		text += '<table><tr><td align=right width="20%">整本 （以;分隔）</td><td style="align:left;width:80%;padding-left:20px;"><textarea class="form-control" rows="3"  name="rdlist['+count+'].rldto['+i+'].gazetteersId" id="gazetteersId_'+count+'_'+i+'" style="width:100%;" disabled></textarea></td></tr>';
		text += '<tr><td align=right width="20%">条目 （以;分隔）</td><td style="align:left;width:80%;padding-left:20px;"><textarea class="form-control" rows="3" name="rdlist['+count+'].rldto['+i+'].itemId" id="itemId_'+count+'_'+i+'" style="width:100%;" disabled></textarea></td></tr>';
		text += '</table></td></tr></table>';
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersArea" id="gazetteersArea_'+count+'_'+i+'">';
		text += '<input type="hidden" name="rdlist['+count+'].rldto['+i+'].gazetteersAlbum" id="gazetteersAlbum_'+count+'_'+i+'"></div></div>';
		text += '<input type="hidden" value="WFLocalChronicle" name="rdlist['+count+'].rldto['+i+'].gazetteersLevel" id="gazetteersLevel_'+count+'_'+i+'"/></div></div>';
	}
	text += '</div></div>';
	$("#detail_0").append(text);
	findSubject(count,i);
	findPatent(count,i);
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
	if(value==1){//分类筛选
		$("#gazetteersId_"+count+"_"+i).val("");
		$("#itemId_"+count+"_"+i).val("");
		findShaiXuan(count,i);
	}else if(value==0){//自定义导入
		findSeftDefine(count,i);
	}
}

//选择大类
function gazetteerType(val,count,i){
	if(val=="FZ_New" || val==""){//新方志
		//datatype放开
		$("input[name='classCode_"+count+"_"+i+"']").each(function(){
			$(this).attr("disabled",false);
		});
		$("input[name='classCode2_"+count+"_"+i+"']").each(function(){
			$(this).attr("disabled",false);
		});
	}else if(val=="FZ_Old"){//旧方志
		//datatype禁用
		$("input[name='classCode_"+count+"_"+i+"']").each(function(){
			$(this).attr("disabled",true);
		});
		$("input[name='classCode2_"+count+"_"+i+"']").each(function(){
			$(this).attr("disabled",true);
		});
	}
}

//地方志
function initGazetteer(count,index){
	var area=$("#gazetteersArea_"+count+"_"+index).val();
	$.ajax({
		type : "post",
		data : {pid:0,area:area},
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
				for(var i=0;i<gazetter.length;i++){
					var name=gazetter[i].name;
					if(gazetter[i].pid=="0"){
						new_index++;
						gazetter_new +='<input type="checkbox" value="'+name+'" name="classCode_'+count+'_'+index+'" onclick="findBox('+count+','+index+')"/> '+name+' &nbsp;';
						if(new_index>0&&new_index%5==0&&gazetter.length!=new_index){
							gazetter_new +='<br/>'
						}
					}else{
						item_index++;
						gazetter_item +='<input type="checkbox" value="'+name+'" name="classCode2_'+count+'_'+index+'" onclick="findBox('+count+','+index+')"/> '+name+' &nbsp;';
						if(item_index>0&&item_index%5==0&&gazetter.length!=item_index){
							gazetter_item +='<br/>'
						}
					}
				}
				$("#new_class_code_"+count+"_"+index).html(gazetter_new);
				$("#item_class_code_"+count+"_"+index).html(gazetter_item);
			}

			var area='<option value="">全部</option>';
			var arrayArea=data.arrayArea;
			for(var i=0;i<arrayArea.length;i++){
				area+='<option value="'+arrayArea[i].id+'">'+arrayArea[i].name+'</option>';
			}
			$("#sheng_"+count+"_"+index).html(area);
			//地区填值
			var sheng=data.sheng;
			if(sheng!=null){
				$("#sheng_"+count+"_"+index).val(sheng.id);
				var shi=data.shi;
				if(shi!=null){
					$("#shi_"+count+"_"+index).html('<option value="'+shi.id+'">'+shi.name+'</option>');
					var xian=data.xian;
					if(xian!=null){
						$("#xian_"+count+"_"+index).html('<option value="'+xian.id+'">'+xian.name+'</option>');
					}
				}
			}
			setGazetteers(count,index);
		}
	});
}

//给方志赋值
function setGazetteers(count,index){
	var gazetteers_id=$("#gazetteersId_"+count+"_"+index).val();
	var itemId=$("#itemId_"+count+"_"+index).val();
	if(gazetteers_id!=""&&gazetteers_id!=null||itemId!=""&&itemId!=null){//自定义
		findSeftDefine(count,index);
		$("input[name='fzlx_"+count+"_"+index+"']").each(function(){
			if($(this).val()=="0"){
				$(this).attr("checked",true);
			}
		});
	}else{//筛选
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
		findShaiXuan(count,index);
	}
}

//点击筛选的样式
function findShaiXuan(count,i){
	$("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']").attr("disabled",false);
	var gazetteersType=$("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']:checked").val();
	if(gazetteersType=="FZ_New"|| gazetteersType==""){
		$("input[name='classCode_"+count+"_"+i+"']").attr("disabled",false);
		$("input[name='classCode2_"+count+"_"+i+"']").attr("disabled",false);
	}else{
		$("input[name='classCode_"+count+"_"+i+"']").attr("disabled",true);
		$("input[name='classCode2_"+count+"_"+i+"']").attr("disabled",true);
	}
	$("input[name='rdlist["+count+"].rldto["+i+"].drtm']").attr("disabled",false);

	$("#sheng_"+count+"_"+i).attr("disabled",false);
	$("#shi_"+count+"_"+i).attr("disabled",false);
	$("#xian_"+count+"_"+i).attr("disabled",false);
	$("#checkfz_"+count+"_"+i).attr("disabled",false);
	//自定义导入的禁用
	$("#gazetteersId_"+count+"_"+i).attr("disabled",true);
	$("#itemId_"+count+"_"+i).attr("disabled",true);
}
//点击自定义的样式
function findSeftDefine(count,i){
	$("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']").attr("disabled",true);
	$("input[name='classCode_"+count+"_"+i+"']").attr("disabled",true);
	$("input[name='classCode2_"+count+"_"+i+"']").attr("disabled",true);
	$("input[name='rdlist["+count+"].rldto["+i+"].drtm']").attr("disabled",true);

	$("#sheng_"+count+"_"+i).attr("disabled",true);
	$("#shi_"+count+"_"+i).attr("disabled",true);
	$("#xian_"+count+"_"+i).attr("disabled",true);
	$("#checkfz_"+count+"_"+i).attr("disabled",true);
	//自定义导入的开放
	$("#gazetteersId_"+count+"_"+i).attr("disabled",false);
	$("#itemId_"+count+"_"+i).attr("disabled",false);
}

//地方志查询区域
function findArea(value,index,count,num){
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
				$("#shi_"+count+"_"+num).html(area);
			}else if(index=="2"){
				$("#xian_"+count+"_"+num).html(area);
			}
			saveArea(count,num);
		}
	});
}

//保存区域
function saveArea(count,i){
	var sheng=$("#sheng_"+count+"_"+i).find("option:selected").text();
	if(sheng=="全部"){
		sheng="";
	}
	if(sheng==null||sheng==""){
		$("#gazetteersArea_"+count+"_"+i).val("");
		return;
	}
	var shi=$("#shi_"+count+"_"+i).find("option:selected").text();
	if(shi=="全部"){
		shi="";
	}
	if(shi==null||shi==""){
		$("#gazetteersArea_"+count+"_"+i).val(sheng);
		return;
	}
	var xian=$("#xian_"+count+"_"+i).find("option:selected").text();
	if(xian=="全部"){
		xian="";
	}
	if(xian==null||xian==""){
		$("#gazetteersArea_"+count+"_"+i).val(sheng+"_"+shi);
		return;
	}
	$("#gazetteersArea_"+count+"_"+i).val(sheng+"_"+shi+"_"+xian);
}
//添加checkbox值
function findBox(count,i){
	var album="";
	$("input[name='classCode_"+count+"_"+i+"']:checked").each(function(){
		if(album!=""){
			album+=";";
		}
		album+=$(this).val();
	});
	$("input[name='classCode2_"+count+"_"+i+"']:checked").each(function(){
		if(album!=""){
			album+=";";
		}
		album+=$(this).val();
	});
	$("#gazetteersAlbum_"+count+"_"+i).val(album);
}
//全选和全不选
function checkfzAll(obj,count,i){
	if($("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']:checked").val()=="FZ_Old"){
		return;
	}
	var text=$(obj).text();
	var type="";
	if($("input[name='rdlist["+count+"].rldto["+i+"].drtm']:checked").val()=="WFLocalChronicle"){
		type="input[name='classCode_"+count+"_"+i+"']";
	}else{
		type="input[name='classCode2_"+count+"_"+i+"']";
	}
	var album="";
	if(text=="全选"){
		$(obj).text("全不选");
		$(type).each(function(){
			$(this).prop("checked", "true");
			album+=$(this).val()+";";
		});
		$("#gazetteersAlbum_"+count+"_"+i).val(album);
	}else{
		$(obj).text("全选");
		$(type).each(function(){
			$(this).removeAttr("checked");
		});
		$("#gazetteersAlbum_"+count+"_"+i).val("");
	}
}

//选择数据类型
function getDataType(val,count,i){
	$("#checkfz_"+count+"_"+i).html("全选");
	if(val=="WFLocalChronicle"){//志书
		$("#zjfl_"+count+"_"+i).html("专辑分类");
		$("#new_class_code_"+count+"_"+i).show();
		$("#item_class_code_"+count+"_"+i).hide();
	}else if(val=="WFLocalChronicleItem"){//条目
		$("#zjfl_"+count+"_"+i).html("专题分类");
		$("#new_class_code_"+count+"_"+i).hide();
		$("#item_class_code_"+count+"_"+i).show();
	}
	$("#gazetteersLevel_"+count+"_"+i).val(val);
	if($("input[name='rdlist["+count+"].rldto["+i+"].gazetteersType']:checked").val()=="FZ_New"){
		$("input[name='classCode_"+count+"_"+i+"']").each(function(){
			$(this).removeAttr("checked");
		});
		$("input[name='classCode2_"+count+"_"+i+"']").each(function(){
			$(this).removeAttr("checked");
		});
	}
}

/*****************方志代码 结束**********************/
//学科中图分类树
function findSubject(count,i){
	$.ajax({
		type : "post",
		data : {num:count+"_"+i},
		async:false,
		url : "../auser/findsubject.do",
		//dataType : "json",
		beforeSend : function(XMLHttpRequest) {},
		success:function(data){
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
						chkStyle: "checkbox",
						//chkboxType:{"Y":"s","N":"s"}
					},
					callback: {
						onCheck: function(){
							var pz = $.fn.zTree.getZTreeObj("perioZtree_"+data.number);
							if(pz!=null){
								var text = new Array();
								text=getCheckNode(pz);
								$("#journalClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
							var dz = $.fn.zTree.getZTreeObj("degreeZtree_"+data.number);
							if(dz!=null){
								var text = new Array();
								text=getCheckNode(dz);
								$("#degreeClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
							var cz = $.fn.zTree.getZTreeObj("confZtree_"+data.number);
							if(cz!=null){
								var text = new Array();
								text=getCheckNode(cz);
								$("#conferenceClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
							var bz = $.fn.zTree.getZTreeObj("bookZtree_"+data.number);
							if(bz!=null){
								var text = new Array();
								text=getCheckNode(bz);
								$("#booksClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
						}
					}
			};
			//期刊中途分类
			$.fn.zTree.init($("#perioZtree_"+data.number), setting, data.ztreeJson);
			//学位中途分类
			$.fn.zTree.init($("#degreeZtree_"+data.number), setting, data.ztreeJson);
			//会议中途分类
			$.fn.zTree.init($("#confZtree_"+data.number), setting, data.ztreeJson);
			//图书中途分类
			$.fn.zTree.init($("#bookZtree_"+data.number), setting, data.ztreeJson);
		}
	});
}


//专利中图分类树
function findPatent(count,i){
	$.ajax({
		type : "post",
		data : {num:count+"_"+i},
		async:false,
		url : "../auser/findpatent.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {},
		success:function(data){
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
						chkStyle: "checkbox",
						//chkboxType:{"Y":"s","N":"s"}
					},
					callback: {
						onCheck: function(){
							var pa = $.fn.zTree.getZTreeObj("patentZtree_"+data.number);
							if(pa!=null){
								var text = new Array();
								text=getCheckNode(pa);
								$("#patentIpc_"+data.number).val(text.length>0?"["+text+"]":"");
							}else{
								$("#patentIpc_"+data.number).val("");
							}
						}
					}
			};
			//期刊中途分类
			$.fn.zTree.init($("#patentZtree_"+data.number), setting, data.ztreeJson);
		}
	});
}


//校验用户名是否已存在
function validateUserId(){
	var userId = $("#userId").val();
	var bool = false;
	$.ajax({
	 	type : "post",
	 	async:false,
		url : "../auser/getPersion.do",
		data:{userId:userId},
		dataType : "json",
		success: function(data){
		   if(data.flag=="true"){
			   bool = true;
		   }else{
			   bool = false;
		   }
		}
	});
	return bool;
}

//校验IP是否存在交集
function validateIp(ip,userId,object){
	var loginMode = $("#loginMode").val();
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
					layer.tips("IP冲突提示:</br>"+data.userId+"</br>"+data.errorIP+"</br>"+data.tableIP, object, {
						tips: [2, '#3595CC'],
						area: ['260px', ''], //宽高
						time: 0
					});
				}else{
					bool = false;
				}
			}
		});
		return bool;
	}
}

//校验多行ip对
function IpFormat(str){
	var ipLimigLineRegex = /^\d{1,3}\.\d{1,3}\.\d{1,3}.\d{1,3}\s*-\s*\d{1,3}\.\d{1,3}.\d{1,3}.\d{1,3}\s*$/i;
	var ip = str.split("\n");
	for(i in ip){
		if(ip[i]!=""){
			if(ipLimigLineRegex.test(ip[i])){
				continue;
			}else{
				return false;
			}
		}
	}
	return true;
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

function checkemail(str){
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if(reg.test(str)){
		return true;
	}else{
		return false;
	}
}

function radioClick(addOrUpdate,isBatch){
	var $selectedvalue = $("input[name='managerType']:checked").val();
	if ($selectedvalue =="new") {
		$("#adminOldName").val("");
		$("#oldManager").hide();
		$("#newManager").show();
	}else {
		var institution=$("#institution").val();
		if(isBatch!="batch" &&(institution==undefined || ""==institution || null==institution)){
			$("input:radio[value='new']").prop('checked','true');
			layer.msg("请输入机构名称",{icon: 2});
			return;
		}
		$.ajax({
			type:"post",
			async: false,
			url:"../auser/getOldAdminNameByInstitution.do",
			data:{"institution":institution},
			dataType:"json",
			success:function(data){
				if(null==data || ""==data){
					layer.msg("该机构下没有机构管理员,建议添加新的机构管理员",{icon: 2});
					$("input:radio[value='new']").prop('checked','true');
					$("#oldManager").hide();
					$("#newManager").show();
				}else{
					$("#adminOldName option:not(:first)").remove();
					for(var i in data){
						if(isBatch!="batch"){
							$("#adminOldName").append("<option value='"+data[i].userId+"'>"+data[i].userId+"</option>");
						}else{
							$("#adminOldName").append("<option value='"+data[i].userId+"/"+data[i].institution+"'>"+data[i].userId+"/"+data[i].institution+"</option>");
						}
					}
					if(addOrUpdate!="update"){
						$("#adminname").val("");
						$("#adminpassword").val("");
						$("#adminIP").val("");
						$("#adminEmail").val("");
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

