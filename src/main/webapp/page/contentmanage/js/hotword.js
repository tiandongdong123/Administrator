var word_nature,word,status;
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
	word_nature=$("#word_nature").find("option:selected").val();
	status=$("#status").find("option:selected").val();
	word=$("#word").val();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/hotwordJson.do",
		dataType : "json",
		data : {
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
			"word":word,
			"word_nature":word_nature,
			"status":status
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
	"<td class=\"mailbox-name\"  style='width:30%'>热搜词</td>"+
    "<td class=\"mailbox-attachment\"  style='width:20%'>检索量</td>"+
    "<td class=\"mailbox-name\">热搜词性质</td>"+
    "<td class=\"mailbox-name\">操作时间</td>"+
    "<td class=\"mailbox-date\">热搜词状态</td>"+
    "<td class=\"mailbox-name\">操作</td>"+
    "</tr>";
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+i+10*(pageNum);
			var rows = pageRow[i];
			var issue = rows.wordStatus;
			var issueNum = 1;
			var  word_status="";
			if(issue == 1||issue == 3){
				issue = "发布";
				issueNum = 2;
			}if(issue == 2){
				issue = "下撤";
				issueNum = 3;
			}
			
			if(issue==1){
				word_status="未发布";
			}else if(issue==2){
				word_status="已发布";
			}else{
				word_status="已下撤";
			}
			
			resHtml+=" <tr style='text-align: center;'>" +
			"<td style='width:10px;'><input type='checkbox' name='commonid' id='"+issue+"' value='"+rows.id+"'></td>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'><span id=\""+rows.id+"_span\">"+rows.word+"</span>"+
			"<input id=\""+rows.id+"_value\" type=\"hidden\" value=\""+rows.word+"\"/>"+
			"<button type='button' id=\""+rows.id+"_update_word\" onclick=\"update_word(this,'"+rows.id+"','"+rows.colums+"',"+issueNum+")\" class='btn btn-primary' style=\"padding-left: 3px; padding-right: 3px;display:none;\">修改</button>&nbsp;" +
			"<button type='button' id=\""+rows.id+"_cancel\" onclick=\"cancel('"+rows.id+"')\" class='btn btn-primary' style=\"padding-left: 3px; padding-right: 3px;display:none;\">取消</button></div></td>"+
			"</div></td>";
			resHtml+="<td><div style='text-align:left;word-wrap:break-word;word-break:break-all;'>"+rows.searchCount+"</div></td>";
			resHtml+="<td class='mailbox-name' style='width:200px;'><div style='width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.wordNature+"</div></td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.operationTime.substr(0,rows.operationTime.length-2)+"</td>"+
            "<td class='mailbox-date'><div title=''>"+word_status+"</td>"+
			"<td class='mailbox-name' style='width:350px;'><div>";
			resHtml+="<button type='button' onclick=\"publish(this,'"+rows.id+"','"+rows.colums+"',"+issueNum+")\" class='btn btn-primary'>"+issue+"</button>&nbsp;" +
			"<button type='button' onclick=\"updateMessage('"+rows.id+"')\" class='btn btn-primary'>修改</button></div></td>" +
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

function updateMessage(id){

	$("#"+id+"_span").hide();
	$("#"+id+"_value").attr("type","text");
	$("#"+id+"_update_word").show();
	$("#"+id+"_cancel").show();
	
	
/*	if(issueState!=2){
		window.location.href="../content/updateMessage.do?id="+id;
	}else{
		layer.msg("请先下撤该数据再进行修改",{icon: 2});
	}*/
}

function cancel(id){
	$("#"+id+"_span").show();
	$("#"+id+"_value").attr("type","hidden");
	$("#"+id+"_update_word").hide();
	$("#"+id+"_cancel").hide();

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


/**
 * 添加热搜词
 */
function add_word(){
 var word_content=$("#word_content").val();
 var isExist=false;
 var success=false;
	$.ajax({
		type : "post",
		async:false,
		url : "../content/checkWordExist.do",
		dataType : "json",
		data : {"word_content" :word_content},
		success : function (data){
			isExist=data;
		}
	});
	
 if(isExist){
	 layer.msg("该热搜词已存在!",{icon: 2});
 }else{
		$.ajax({
			type : "post",
			async:false,
			url : "../content/addWord.do",
			dataType : "json",
			data : {"word_content" :word_content},
			success : function (data){
				success=data;
			}
		});
		
		if(success){
			layer.msg("添加成功!",{icon: 1});
		}else{
			layer.msg("添加失败!",{icon: 2});
		}
		
 } 	
	
}

function hot_word_setting(){
	window.location.href="../content/hotWordPublish.do";
}

