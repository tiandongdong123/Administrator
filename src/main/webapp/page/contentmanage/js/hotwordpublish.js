var pageNum;
var pageSize = 30;
$(function(){
	showPage(1);
	selectValue("nature",$("#publish_strategy").val());
	changenature("#nature");
});

/*分页显示*/
function findOne(){
	showPage(1);
}

function showPage(curr){
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
			serachdata(curr,data);
		}
	});
}

function serachdata(curr,data){
	var pageNum = data.pageNum;
	var pageRow=data.pageRow;
	var resHtml ="";
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+i+10*(pageNum);
			var rows = pageRow[i];	
			resHtml+=" <tr style='text-align: center;'>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.first_publish_time+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.publish_cyc+"天"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>近"+rows.time_slot+"天"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.publish_strategy+"</div></td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.publish_date+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.get_time+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.next_publish_time+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.next_publish_time_space+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.operation+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.operation_date+"</td>"+
            "<td class='mailbox-date'><div title=''>"+(rows.status==1?"已应用":"待应用")+"</td>"+
			"<td class='mailbox-name' style='width:350px;'><div>"+
			 divShow(rows.id,rows.status)+"&nbsp;" +
			"<button type='button' onclick=\"updateSetting('"+rows.id+"','"+rows.status+"')\" class='btn btn-primary' id=\"update\">修改</button></div></td>" +
          "</tr>";
		}
	}
	
	$("#tbody").html(resHtml);
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

function addWordSetting(){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['50%', '70%'],
	    title: '热搜词发布设置',
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
	var isFirst=$("#isFirst").val();
	if(publish_strategy=="手动发布"){
		publish_date="";
	}
	if(isFirst!="true"){
		first_publish_time=$("#first_publish_time").val();
	}
	
	if(isFirst!="true" && (first_publish_time=='' || first_publish_time==null || first_publish_time==undefined)){
		$("#checkfirst_publish_time").text("请填写首次发布日期！");
		return;
	}else{
		$("#checkfirst_publish_time").text("");
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
		$("#checkget_time").text("请填写抓取时间！");
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
		url : "../content/doaddWordSetting.do",
		dataType : "json",
		data : {
			"publish_cyc" :publish_cyc,
			"time_slot" :time_slot,
			"publish_strategy" :publish_strategy,
			"publish_date" :publish_date,
			"first_publish_time" :first_publish_time,
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
	    area: ['50%', '70%'],
	    title: '热搜词发布设置',
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
	var first_publish_time="";
	var get_time=$("#get_time").val();
	var id=$("#id").val();
	var isFirst=$("#isFirst").val();	
	var issuccess=false;
	
	if(publish_strategy=="手动发布"){
		publish_date="";
	}
	
	if(isFirst!="true"){
		first_publish_time=$("#first_publish_time").val();
	}
	
	if(isFirst!="true" && (first_publish_time=='' || first_publish_time==null || first_publish_time==undefined)){
		$("#checkfirst_publish_time").text("请填写首次发布日期！");
		return;
	}else{
		$("#checkfirst_publish_time").text("");
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
		$("#checkfirst_publish_time").text("请填写抓取时间！");
		return;
	}else{
		$("#checkfirst_publish_time").text("");
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
	layer.alert("确定要应用此数据吗?",{
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
		html="<button type='button' onclick=\"publish('"+id+"','1')\" class='btn btn-primary' id=\"application\">应用</button>";
	}
	return html;
}

function cancel(){
	 var index = parent.layer.getFrameIndex(window.name);  
	parent.layer.close(index);
}



