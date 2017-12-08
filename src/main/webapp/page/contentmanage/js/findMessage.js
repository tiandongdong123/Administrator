var branch,clum,human,startTime,endTime,isTop;
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
	branch=$("#xjbm").find("option:selected").text();
	if(branch=='全部'){
		branch="";
	}
	clum=$("#clum").find("option:selected").text();
	if(clum=='全部'){
		clum="";
	}
	human=$("#human").val();
	startTime=$("#startTime").val();
	endTime=$("#endTime").val();
	isTop=$("#isTop").val();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/messageJson.do",
		dataType : "json",
		data : {
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
			"branch":branch,
			"colums":clum,
			"human":human,
			"startTime":startTime,
			"endTime":endTime,
			"isTop":isTop
			},
		success : function (data){
			serachdata(curr,data);
		}
	});
}

function serachdata(curr,data){
	var pageNum = data.pageNum;
	var pageTotal = data.pageTotal;
	var pageRow=data.pageRow;
	var resHtml = "<tbody><tr style='text-align: center;'>" +
	"<td><input onclick=\"checkAll()\" class='allId' type='checkbox'></td>" +
	"<td class='mailbox-star'>序号</td>" +
	"<td class=\"mailbox-name\">栏目</td>"+
    "<td class=\"mailbox-attachment\" style='width:30%'>标题</td>"+
    "<td class=\"mailbox-name\">原文链接</td>"+
    "<td class=\"mailbox-name\">添加人</td>"+
    "<td class=\"mailbox-date\">添加日期</td>"+
    "<td class=\"mailbox-date\">是否置顶</td>"+
    "<td class=\"mailbox-name\">操作</td>"+
    "</tr>";
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+i+10*(pageNum-1);
			var rows = pageRow[i];
			var issue = rows.issueState;
			var issueNum = 1;
			if(issue == 1||issue == 3){
				issue = "发布";
				issueNum = 2;
			}if(issue == 2){
				issue = "下撤";
				issueNum = 3;
			}
			var is_top = rows.isTop;
			if(is_top=="1"){
				is_top="是";
			}else{
				is_top="否";
			}
			resHtml+=" <tr style='text-align: center;'>" +
			"<td style='width:10px;'><input type='checkbox' name='commonid' id='"+issue+"' value='"+rows.id+"'></td>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.colums+"</div></td>";
			resHtml+="<td><div style='text-align:left;word-wrap:break-word;word-break:break-all;'><a href='javascript:;' onclick=\"turnHtml('"+rows.colums+"','"+rows.id+"')\">"+rows.title+"</a></div></td>";
			resHtml+="<td class='mailbox-name' style='width:200px;'><div style='width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'><a href='"+rows.linkAddress+"'>"+rows.linkAddress+"</a></div></td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.human==null?"":rows.human)+"</td>"+
            "<td class='mailbox-date'><div title='"+rows.createTime+"'>"+rows.createTime+"</td>"+
            "<td class='mailbox-date'><div title='"+is_top+"' style='width:40px;'>"+is_top+"</td>"+
			"<td class='mailbox-name' style='width:350px;'><div>";
			if(issueNum!=3){
				resHtml+="<button type='button' onclick=\"stick('"+rows.id+"','"+rows.colums+"')\" class='btn btn-primary'>置顶</button>&nbsp;";
			}
			resHtml+="<button type='button' onclick=\"publish(this,'"+rows.id+"','"+rows.colums+"',"+issueNum+")\" class='btn btn-primary'>"+issue+"</button>&nbsp;" +
			"<button type='button' onclick=\"updateMessage('"+rows.id+"',"+rows.issueState+")\" class='btn btn-primary'>修改</button></div></td>" +
          "</tr>";
		}
	}
	resHtml+="</tbody>";
	$("#list").html(resHtml);
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
	document.getElementById("here").scrollIntoView();
}

//置顶
function stick(id,colums){
	$.ajax({
		type : "post",
		async:false,
		url : "../content/stick.do",
		dataType : "json",
		data : {
			"id":id,
			'colums':colums
			},
		success : function(){
			showPage();
		}
	});
}

function updateMessage(id,issueState){
	if(issueState!=2){
		window.location.href="../content/updateMessage.do?id="+id;
	}else{
		layer.msg("请先下撤该数据再进行修改",{icon: 2});
	}
}

function addMessage(){
	window.location.href="../content/addMessage.do";
}

//单条删除
function removee(id){
	$.ajax({
		type : "post",
		data : {ids: id},
		url :  "../content/deleteMessage.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {},
		success : deleteCallback,
		complete : function(XMLHttpRequest, textStatus) {},
		error : function(data) {alert(data);}
	});
}

// 多条删除
function deleteMore(){
	var issueNum = "";
	$("input:checkbox[name=commonid]:checked").each(function(){
		issueNum += $(this).attr("id") + ",";
	});
	if(!$("input:checkbox[name=commonid]:checked").is(':checked')){
		layer.msg("请选择删除内容！",{icon: 2});
	}else if(issueNum.indexOf("下撤")>=0){
		layer.msg("请先下撤数据再进行删除！",{icon: 2});
	}else{
		layer.alert('确定删除该数据吗？',{
			icon: 1,
		    skin: 'layui-layer-molv',
		    btn: ['确定'], //按钮
		    yes: function(){
				var ids = "";
				$("input:checkbox[name=commonid]:checked").each(function(){
					ids += "," + $(this).val();
				});
				if (ids != "")
					ids = ids.substring(1);
				$.ajax({
					type : "post",
					data : {ids: ids},
					url :"../content/deleteMessage.do",
					dataType : "json",
					beforeSend : function(XMLHttpRequest) {},
					success : deleteCallback,
					complete : function(XMLHttpRequest, textStatus) {},
					error : function(data) {alert(data);}
				});
		        layer.closeAll();
		    }
		});
	}
}

// 删除回执
function deleteCallback(data) {
	if (data.flag == "true") {
		layer.msg("删除成功！", {
			icon : 1
		});
		showPage();
	} else {
		layer.msg("删除失败!", {
			icon : 2
		});
	}
}

//全选与全不选
function checkAll() {
	if ($(".allId").is(':checked')) {
		$("input[name=commonid]").each(function() {
			$(this).prop("checked", "checked");
		});
	} else {
		$("input[name=commonid]").each(function() {
			$(this).removeAttr("checked");
		});
	}
}

//赋值
function selectValue(id, val) {
	for (var i = 0; i < document.getElementById(id).options.length; i++) {
		if (document.getElementById(id).options[i].value == val) {
			document.getElementById(id).options[i].selected = true;
			break;
		}
	}
}

// 刷新
function refresh(){
	window.location.href="../content/message.do";
}

//发布
function publish(that,obj,colums,issueState){
	var value = "";
	if(issueState=='3'){
		value = '是否确定下撤?';
	}else if(issueState=='2'){
		if($(that).text()=='发布'){
			value = '是否确定发布?';
		}else{
			value = '是否确定再发布?';
		}
	}else{
		value = '是否确定发布?';
	}
	layer.alert(value,{
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../content/updateIssue.do",
	    		data :{ 
	    			"id" : obj,
	    			"colums":colums,
	    			"issueState":issueState
	    		},
	    		dataType : "json",
	    		success : function(data){
	    			layer.closeAll();
	    			if(data){
	    				findOne();
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	    }
	  });
}

//详情页
function turnHtml(type,id){
	window.location.href="../content/getDetails.do?id="+id;
}

//导出
function exportexecl(){
	window.location.href="../content/exportMessage.do?branch="+branch+"&colums="+clum+"&human="+human+"&startTime="+startTime+"&endTime="+endTime;
}
 
//一键发布
function oneKeyDeploy(){
	var index = null;
	$.ajax({
		type : "post",
		url : "../content/oneKeyDeploy.do",
		beforeSend: function (request) {
			index = layer.load();
		},
		success : function (data){
			layer.close(index);
			if(data){
				layer.msg("一键发布成功!");
			}else{
				layer.msg("发布异常!");
			}
		}
	});
}
