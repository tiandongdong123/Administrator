var pageNum;
var pageSize = 20;

$(function(){
	showPage(1,'auto_tbody');
	showManualPage(1,'manual_tbody')
	selectValue("nature",$("#publish_strategy").val());
	changenature("#nature");
});

/*分页显示*/
function findOne(){
	showPage(1,'auto_tbody');
	showManualPage(1,'manual_tbody')
}

function showPage(curr,id){
	$.ajax({
		type : "post",
		async:false,
		url : "../content/getHotWordSettingJson.do",
		dataType : "json",
		data : {
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
			},
		success : function (data){
			serachAutodata(curr,data,id);
		}
	});
}

function serachAutodata(curr,data,id){
	var pageNum = data.pageNum;
	var pageRow=data.pageRow;
	var pageTotal = data.pageTotal;
	var resHtml ="";
	var totalRow=data.totalRow;
    var pageall;
    if(totalRow%pageSize==0){
 	   pageall=totalRow/pageSize;
    }else{
 	   pageall= parseInt(totalRow/pageSize)+1;
    }
    var maxLenght=(pageall+"").length;
    $("#totalRow").text(totalRow);
    $("#totalpage").text(pageall);
    $("#pageTotal").val(pageTotal);
    $("#pagenum").attr("maxlength",maxLenght); 
    if(totalRow<=20){
    	$("#pages").hide();
    }else{
    	$("#pages").show();
    }
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+pageNum++;
			var rows = pageRow[i];	
			resHtml+=" <tr style='text-align: center;'>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.publish_cyc+"天"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>近"+rows.time_slot+"天"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.publish_date+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.get_time+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.operation+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.operation_date+"</td>"+
            "<td class='mailbox-date'><div title=''>"+(rows.status==1?"已应用":"待应用")+"</td>"+
			"<td class='mailbox-name' style='width:350px;'><div>"+
			 divShow(rows.id,rows.status)+"&nbsp;" +
			"<button type='button' onclick=\"updateSetting('"+rows.id+"','"+rows.status+"')\" class='btn btn-primary' id=\"update"+id+"\">修改</button></div></td>" +
          "</tr>";
		}
	}
	
	$('#'+id).html(resHtml);
	layui.use(['laypage', 'layer'], function(){
		var laypage = layui.laypage,layer = layui.layer;
		laypage.render({
			elem: 'divPager',
			count: data.totalRow,
			first: '首页',
			last: '尾页',
			curr: curr || 1,
			page: Math.ceil(data.totalRow / pageSize),	//总页数
			limit: pageSize,
			layout: ['count', 'prev', 'page', 'next', 'skip'],
			jump: function (obj, first) {
	            if(!first){
	            	showPage(obj.curr);
	            }
			}
		});
	});
}

function firstPage(){
	var pagenum=Number($("#pagenum").val());
	if(pagenum==1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum").val("1");
		showPage(1,'auto_tbody');
	}
}


function upPage(){
	var pagenum=Number($("#pagenum").val())-1;
	if(pagenum<1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum").val(pagenum);
		showPage(pagenum,'auto_tbody');
	}

}

function lastPage(){
	var pagenum=$("#pagenum").val();
	var total=$("#totalpage").text();
	if(pagenum==$("#pageTotal").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum").val(total);
		showPage(total,'auto_tbody');
	}
}

function downPage(){
	var pagenum=Number($("#pagenum").val())+1;
	if(pagenum>$("#pageTotal").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum").val(pagenum);
		showPage(pagenum,'auto_tbody');
	}
}


function getAllpageNum(){
	$("#pagenum").val("1");
	showPage(pagenum,'auto_tbody');
}

function selectPage(){
	var keyCode=event.keyCode;
	var pagenum=$("#pagenum").val();
	var total=$("#totalpage").text();
	if(keyCode==13){
		if(pagenum>total){
			layer.msg("请输入正确的页码");
		}else if(pagenum==0){
			layer.msg("请输入正确的页码");
		}else if(pagenum<=total){
			showPage(pagenum,'auto_tbody');
		}
	}
}

function addWordSetting(){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['30%', '300px'],
	    title: '添加自动发布设置',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../content/addWordSetting.do",
	}); 
}


function changenature(that){
	if($(that).val()=="自动发布"){
		$("#publish_time_div").show();
	}else{
		$("#publish_time_div").hide();
	}
}

function doaddWordSetting(){
	var publish_cyc=$("#publish_cycle").val();
	var time_slot=$("#time_quantum").val();
	var publish_strategy=$("#nature").val();
	var publish_date=$("#publish_date").val();
	var first_publish_time="";
	var get_time=$("#get_time").val();
	var issuccess=false;
	var isFirst=false;
	if(publish_strategy=="手动发布"){
		publish_date="";
	}
	if(isFirst!="true"){
		first_publish_time=$("#first_publish_time").val();
	}
	


	if(publish_cyc=='' || publish_cyc==null || publish_cyc==undefined){
		$("#checkpublish_cyc").text("请填写发布周期！");
		return;
	}else{
		$("#checkpublish_cyc").text("");
	}
	if(time_slot=='' || time_slot==null || time_slot==undefined){
		$("#checktime_slot").text("请填写抓取数据时间段！");
		return;
	}else{
		$("#checktime_slot").text("");
	}
	
	if(publish_strategy!="手动发布" && (publish_date=='' || publish_date==null || publish_date==undefined)){
		$("#checkpublish_date").text("请填写发布时间！");
		return;
	}else{
		$("#checkpublish_date").text("");		
	} 

	
	$.ajax({
		type : "post",
		async:false,
		url : "../content/doaddWordSetting.do",
		dataType : "json",
		data : {
			"publish_cyc" :publish_cyc,
			"time_slot" :time_slot,
			"publish_strategy" :publish_strategy,
			"publish_date" :publish_date,
			"get_time" :get_time,
			"isFirst":isFirst,
			},
		success : function (data){
			issuccess=data;
		}
	});
	
	
	if(issuccess){
		layer.msg("<div style=\"color:#0000FF;\">保存成功!</div>",{icon: 1});
		setTimeout("parent.location.reload();",1000);
	}else{
		layer.msg("<div style=\"color:#8B0000;\">保存失败!</div>",{icon: 2});
	}
	
}

function updateSetting(id,status){
	
/*	if(status==1){
		layer.msg("请下撤应用后修改！",{icon: 2});
		return;
	}*/
	
	
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['30%', '300px'],
	    title: '修改自动发布设置',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../content/getHotWordSetting.do?id="+id,
	}); 
}


function selectValue(id,val){
	for(var i=0;i<document.getElementById(id).options.length;i++)
    {
        if(document.getElementById(id).options[i].value == val)
        {
            document.getElementById(id).options[i].selected=true;
            break;
        }
    }
}


function doupdateWordSetting(){
	var publish_cyc=$("#publish_cycle").val();
	var time_slot=$("#time_quantum").val();
	var publish_strategy=$("#nature").val();
	var publish_date=$("#publish_date").val();
	var first_publish_time=$("#first_publish_time").val();
	var get_time=$("#get_time").val();
	var id=$("#id").val();
	var isFirst=$("#isFirst").val();	
	var issuccess=false;
	
	if(publish_strategy=="手动发布"){
		publish_date="";
	}
	

	if(publish_cyc=='' || publish_cyc==null || publish_cyc==undefined){
		$("#checkpublish_cyc").text("请填写发布周期！");
		return;
	}else{
		$("#checkpublish_cyc").text("");
	} 
	
	if(time_slot=='' || time_slot==null || time_slot==undefined){
		$("#checktime_slot").text("请填写数据统计时间段！");
		return;
	}else{
		$("#checktime_slot").text("");
	} 
	
	if(get_time=='' || get_time==null || get_time==undefined){
		$("#checkget_time").text("请填写发布时间！");
		return;
	}else{
		$("#checkget_time").text("");
	} 

	
	if(publish_strategy!="手动发布" && (publish_date=='' || publish_date==null || publish_date==undefined)){
		$("#checkpublish_date").text("请填写发布时间！");
		return;
	}else{
		$("#checkpublish_date").text("");
	} 

	
	$.ajax({
		type : "post",
		async:false,
		url : "../content/doupdateWordSetting.do",
		dataType : "json",
		data : {
			"publish_cyc" :publish_cyc,
			"time_slot" :time_slot,
			"publish_strategy" :publish_strategy,
			"publish_date" :publish_date,
			"first_publish_time" :first_publish_time,
			"get_time" :get_time,
			"id":id,
			"next_publish_time":$("#nextPublish").val(),
			"isFirst":isFirst,
			},
		success : function (data){
			issuccess=data;
		}
	});
	
	if(issuccess){
		layer.msg("<div style=\"color:#0000FF;\">修改成功!</div>",{icon: 1});
		setTimeout("parent.location.reload();",1000);
	}else{
		layer.msg("<div style=\"color:#8B0000;\">修改失败!</div>",{icon: 2});
	}

}

function publish(id,status){
	
/*	var issuccess=compareGetTime(id);
	
	if(issuccess){
		layer.msg("<div style=\"color:#8B0000;\">抓取时间大于发布时间,请修改后应用!</div>",{icon: 2});
		return;
	}
*/	
	
	layer.alert("应用设置后立即生效，确定要应用此设置吗?",{
		title: '添加自动发布设置',
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",
	    		async:false,
	    		url : "../content/updateWordSettingStatus.do",
	    		dataType : "json",
	    		data : {
	    			"id":id,
	    			"status":status,
	    			},
	    		success : function (data){
	    			issuccess=data;
	    		}
	    	});
	    	
	    	if(issuccess){
	    		layer.msg("<div style=\"color:#0000FF;\">应用成功!</div>",{icon: 1});
	    		setTimeout("window.location.reload();",1000);
	    	}else{
	    		layer.msg("<div style=\"color:#8B0000;\">应用失败!</div>",{icon: 2});
	    	}
	    }
	
	  });
}

function compareGetTime(id){
	var issuccess=false;
	$.ajax({
		type : "post",
		async:false,
		url : "../content/compareGetTime.do",
		dataType : "json",
		data : {
			"id":id,
			},
		success : function (data){
			issuccess=data;
		}
	});
	
	return issuccess;

}


function  divShow(id,status){
	var html="";
	if(status==2){
		html="<button type='button' onclick=\"publish('"+id+"','1')\" class='btn btn-primary' id=\"application"+id+"\">应用</button>";
	}
	return html;
}

function cancel(){
	 var index = parent.layer.getFrameIndex(window.name);  
	parent.layer.close(index);
}


// 获取手动发布设置内容
function showManualPage(curr,id){
	$.ajax({
		type : "post",
		async:false,
		url : "../content/getHotWordManualSettingJson.do",
		dataType : "json",
		data : {
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
			},
		success : function (data){
			serachManualdata(curr,data,id);
		}
	});
}
// 渲染手动发布内容
function serachManualdata(curr,data,id){
	var pageNum = data.pageNum;
	var pageRow=data.pageRow;
	var pageTotal = data.pageTotal;
	var totalRow=data.totalRow;
	var resHtml ="";
    var pageall;
    if(totalRow%pageSize==0){
 	   pageall=totalRow/pageSize;
    }else{
 	   pageall= parseInt(totalRow/pageSize)+1;
    }
    var maxLenght=(pageall+"").length;
    $("#totalRowManual").text(totalRow);
    $("#totalpageManual").text(pageall);
    $("#pageTotalManual").val(pageTotal);
    $("#pagenum_manual").attr("maxlength",maxLenght); 
    if(totalRow<=20){
    	$("#pages_manual").hide();
    }else{
    	$("#pages_manual").show();
    }
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+pageNum++;
			var rows = pageRow[i];	
			resHtml+=" <tr style='text-align: center;'>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.get_cyc+"天"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>近"+rows.time_slot+"天"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.get_time+"</div></td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.operation+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.operation_date+"</td>"+
            "<td class='mailbox-date'><div title=''>"+(rows.status==1?"已应用":"待应用")+"</td>"+
			"<td class='mailbox-name' style='width:350px;'><div>"+
			divManualShow(rows.id,rows.status)+"&nbsp;" +
			"<button type='button' onclick=\"updateManualSetting('"+rows.id+"','"+rows.status+"')\" class='btn btn-primary' id=\"update"+id+"\">修改</button></div></td>" +
          "</tr>";
		}
	}
	
	$('#'+id).html(resHtml);
	layui.use(['laypage', 'layer'], function(){
		var laypage = layui.laypage,layer = layui.layer;
		laypage.render({
			elem: 'divPager',
			count: data.totalRow,
			first: '首页',
			last: '尾页',
			curr: curr || 1,
			page: Math.ceil(data.totalRow / pageSize),	//总页数
			limit: pageSize,
			layout: ['count', 'prev', 'page', 'next', 'skip'],
			jump: function (obj, first) {
	            if(!first){
	            	showManualPage(obj.curr);
	            }
			}
		});
	});
}
function firstPageManual(){
	var pagenum=Number($("#pagenum_manual").val());
	if(pagenum==1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum_manual").val("1");
		showManualPage(1,'manual_tbody');
	}
}
function upPageManual(){
	var pagenum=Number($("#pagenum_manual").val())-1;
	if(pagenum<1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum_manual").val(pagenum);
		showManualPage(pagenum,'manual_tbody');
	}
}
function lastPageManual(){
	var pagenum=$("#pagenum_manual").val();
	var total=$("#totalpageManual").text();
	if(pagenum==$("#pageTotalManual").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum_manual").val(total);
		showManualPage(total,'manual_tbody');
	}
}
function downPageManual(){
	var pagenum=Number($("#pagenum_manual").val())+1;
	if(pagenum>$("#pageTotalManual").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum_manual").val(pagenum);
		showManualPage(pagenum,'manual_tbody');
	}
}
function getAllpageNumManual(){
	$("#pagenum_manual").val("1");
	showManualPage(pagenum,'manual_tbody');
}

function selectPageManual(){
	var keyCode=event.keyCode;
	var pagenum=$("#pagenum_manual").val();
	var total=$("#totalpageManual").text();
	if(keyCode==13){
		if(pagenum>total){
			layer.msg("请输入正确的页码");
		}else if(pagenum==0){
			layer.msg("请输入正确的页码");
		}else if(pagenum<=total){
			showManualPage(pagenum,'manual_tbody');
		}
	}
}
// 手动发布设置按钮弹窗
function addWordManualSetting(){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['30%', '300px'],
	    title: '添加手动发布设置',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../content/addWordManualSetting.do",
	}); 
}
// 手动发布弹窗保存
function doaddWordManualSetting(){
	var publish_cyc=$("#get_cycle").val();
	var time_slot=$("#time_quantum").val();
	var publish_strategy=$("#nature").val();
	var publish_date=$("#publish_date").val();
	var first_publish_time="";
	var get_time=$("#get_time").val();
	var issuccess=false;
	var isFirst=$("#isFirst").val();
	

	if(publish_cyc=='' || publish_cyc==null || publish_cyc==undefined){
		$("#checkpublish_cyc").text("请填写抓取数据周期！");
		return;
	}else{
		$("#checkpublish_cyc").text("");
	}
	if(time_slot=='' || time_slot==null || time_slot==undefined){
		$("#checktime_slot").text("请填写抓取数据时间段！");
		return;
	}else{
		$("#checktime_slot").text("");
	}
	
	
	$.ajax({
		type : "post",
		async:false,
		url : "../content/doaddWordManualSetting.do",
		dataType : "json",
		data : {
			"get_cyc" :publish_cyc,
			"time_slot" :time_slot,
			},
		success : function (data){
			issuccess=data;
		}
	});
	
	if(issuccess){
		layer.msg("<div style=\"color:#0000FF;\">保存成功!</div>",{icon: 1});
		setTimeout("parent.location.reload();",1000);
	}else{
		layer.msg("<div style=\"color:#8B0000;\">保存失败!</div>",{icon: 2});
	}
	
}
// 手动发布修改
function updateManualSetting(id,status){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['30%', '300px'],
	    title: '修改手动发布设置',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../content/getHotWordManualSetting.do?id="+id,
	}); 
}

// 手动发布应用
function  divManualShow(id,status){
	var html="";
	if(status==2){
		html="<button type='button' onclick=\"publishManual('"+id+"','1')\" class='btn btn-primary' id=\"application"+id+"\">应用</button>";
	}
	return html;
}
function publishManual(id,status){		
	layer.alert("应用设置后立即生效，确定要应用此设置吗?",{
		title: '添加手动发布设置',
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",
	    		async:false,
	    		url : "../content/updateWordManualSettingStatus.do",
	    		dataType : "json",
	    		data : {
	    			"id":id,
	    			"status":status,
	    			},
	    		success : function (data){
	    			issuccess=data;
	    		}
	    	});
	    	if(issuccess){
	    		layer.msg("<div style=\"color:#0000FF;\">应用成功!</div>",{icon: 1});
	    		setTimeout("window.location.reload();",1000);
	    	}else{
	    		layer.msg("<div style=\"color:#8B0000;\">应用失败!</div>",{icon: 2});
	    	}
	    }
	
	  });
}

function doupdateWordSettingManual(){
	var publish_cyc=$("#get_cycle").val();
	var time_slot=$("#time_quantum").val();
	var publish_strategy=$("#nature").val();
	var publish_date=$("#publish_date").val();
	var first_publish_time=$("#first_publish_time").val();
	var get_time=$("#get_time").val();
	var isFirst=$("#isFirst").val();	
	var id=$("#id").val();
	var issuccess=false;
	
	if(publish_strategy=="手动发布"){
		publish_date="";
	}
	

	if(publish_cyc=='' || publish_cyc==null || publish_cyc==undefined){
		$("#checkpublish_cyc").text("请填写发布周期！");
		return;
	}else{
		$("#checkpublish_cyc").text("");
	}
	if(time_slot=='' || time_slot==null || time_slot==undefined){
		$("#checktime_slot").text("请填写数据统计时间段！");
		return;
	}else{
		$("#checktime_slot").text("");
	}

	
	$.ajax({
		type : "post",
		async:false,
		url : "../content/doupdateWordManualSetting.do",
		dataType : "json",
		data : {
			"get_cyc" :publish_cyc,
			"time_slot" :time_slot,
			"id":id,
			"isFirst":isFirst,
			},
		success : function (data){
			issuccess=data;
		}
	});
	
	if(issuccess){
		layer.msg("<div style=\"color:#0000FF;\">修改成功!</div>",{icon: 1});
		setTimeout("parent.location.reload();",1000);
	}else{
		layer.msg("<div style=\"color:#8B0000;\">修改失败!</div>",{icon: 2});
	}

}