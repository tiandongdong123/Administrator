var pageNum;
var pageSize = 10;
$(function(){
	showPage(1);
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
	console.info(pageRow);
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
			"<button type='button' onclick=\"publish(this,'"+rows.id+"',"+rows.status+")\" class='btn btn-primary'>"+(rows.status==1?"已应用":"待应用")+"</button>&nbsp;" +
			"<button type='button' onclick=\"update('"+rows.id+"','"+rows.status+"')\" class='btn btn-primary'>修改</button></div></td>" +
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
	var first_publish_time=$("#first_publish_time").val();
	var get_time=$("#get_time").val();
	var issuccess=false;
	
	if(publish_strategy=="手动发布"){
		publish_date="";
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
			},
		success : function (data){
			issuccess=data;
		}
	});
	
	
	if(issuccess){
		layer.msg("保存成功!",{icon: 1});
		history.go(0);
	}else{
		layer.msg("保存失败!",{icon: 2});
	}
	
}

