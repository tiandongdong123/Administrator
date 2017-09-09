var branch,clum,human,startTime,endTime;
$(function(){
	branch=$("#branchf").val();
	clum=$("#columsf").val();
	if(branch !=""){
		selectValue("xjbm",branch);
	}
	if(clum !=""){
		selectValue("clum",clum);
	}
	showPage();
});

/*分页显示*/
function findOne(pagenum){
	findtext();
	if(pagenum==undefined||pagenum==""){
		lp=1;
	}else{
		lp=pagenum;
	}
	window.location.href="../content/message.do?branch="+branch+"&colums="+clum+"&human="+human+"&startTime="+startTime+"&endTime="+endTime+"&page="+lp;
//	showPage();
}

function showPage(){
	//显示分页
	var pageNum = $("#pageNum").val();
	var pageTotal = $("#pageTotal").val();
	laypage({
    	cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
        pages: pageTotal, //通过后台拿到的总页数
        curr: pageNum, //当前页
        skip: true, //是否开启跳页
	      skin: '#367fa9',//当前页颜色，可16进制
	      groups: 4, //连续显示分页数
	      first: '首页', //若不显示，设置false即可
	      last: '尾页', //若不显示，设置false即可
	      prev: '上一页', //若不显示，设置false即可
	      next: '下一页', //若不显示，设置false即可
        jump: function(obj, first){ //触发分页后的回调
        	if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
            	paging(obj.curr);
        }else{
        	paging(obj.curr);
        }
       }
	});
}
/**
 * 分页事件
 */
function paging(curr){
	findtext();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/messageJson.do",
		dataType : "json",
		data : {
			"page":curr || 1,
			"branch":branch,
			"colums":clum,
			"human":human,
			"startTime":startTime,
			"endTime":endTime
			},
		success : function (data){
			serachdata(data);
		}
	});
}

function serachdata(data){
	var pageNum = data.pageNum;
	var pageTotal = data.pageTotal;
	$("#pageNum").val(pageNum);
	$("#pageTotal").val(pageTotal);
	var pageRow=data.pageRow;
	var resHtml = "<tbody><tr style='text-align: center;'>" +
	"<td><input onclick=\"checkAll()\" class='allId' type='checkbox'></td>" +
	"<td class='mailbox-star'>序号</td>" +
	"<td class=\"mailbox-name\">栏目</td>"+
    "<td class=\"mailbox-attachment\" style='width:40%'>标题</td>"+
    "<td class=\"mailbox-name\">原文链接</td>"+
    "<td class=\"mailbox-name\">添加人</td>"+
    "<td class=\"mailbox-date\">添加日期</td>"+
    "<td class=\"mailbox-name\">操作</td>"+
    "</tr>";
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+i;
			var rows = pageRow[i];
			var issue = rows.issueState;
			var issueNum = 1;
			if(issue == 1){
				issue = "发布";
				issueNum = 2;
			}if(issue == 2){
				issue = "下撤";
				issueNum = 3;
			}if(issue == 3){
				issue = "再发布";
				issueNum = 2;
			}
			resHtml+=" <tr style='text-align: center;'>" +
			"<td style='width:10px;'><input type='checkbox' name='commonid' id='"+issue+"' value='"+rows.id+"'></td>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.colums+"</div></td>";
			resHtml+="<td><div style='text-align:left;word-wrap:break-word;word-break:break-all;'><a href='javascript:;' onclick=\"turnHtml('"+rows.colums+"','"+rows.id+"')\">"+rows.title+"</a></div></td>";
			resHtml+="<td class='mailbox-name'><div style='width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'><a href='"+rows.linkAddress+"'>"+rows.linkAddress+"</a></div></td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.human==null?"":rows.human)+"</td>"+
            "<td class='mailbox-date'><div title='"+rows.createTime+"'>"+rows.createTime+"</td>"+
			"<td class='mailbox-name' style='width:350px;'><div>";
			if(rows.colums=='基金会议'){
				resHtml+="<button type='button' onclick=\"stick('"+rows.id+"','"+rows.colums+"',"+issueNum+")\" style='width:50px;'>置顶</button>&nbsp;";
			}
			resHtml+="<button type='button' onclick=\"publish(this,'"+rows.id+"','"+rows.colums+"',"+issueNum+")\" style='width:60px;'>"+issue+"</button>&nbsp;" +
			"<button type='button' onclick=\"updateMessage('"+rows.id+"')\" style='width:50px;'>修改</button></div></td>" +
          "</tr>";
		}
	}
	resHtml+="</tbody>";
	$(".table-striped").html(resHtml);
	document.getElementById("here").scrollIntoView();
}

/*置顶*/
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
	window.location.href="../content/updateMessage.do?id="+id;
}
function addMessage(){
	window.location.href="../content/addMessage.do";
}

function findtext(){
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
	if (data.flag=="true"){
		layer.msg("删除成功！",{icon: 1});
		showPage();
		//window.location.href="../content/message.do";
	}else{
		layer.msg("删除失败!",{icon: 2});
	}
}

/*全选与全不选*/
function checkAll(){
		if($(".allId").is(':checked')){
			$("input[name=commonid]").each(function(){
				$(this).prop("checked", "checked");
			});
		}else{
			$("input[name=commonid]").each(function(){
				$(this).removeAttr("checked");
			});
		}
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

/*刷新*/

function refresh(){
	window.location.href="../content/message.do";
}
//----------------------------发布-------------------------------
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
	    			if(data){
	    				var num=$(".laypage_curr").text();
	    				findOne(num);
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	    }
	  });
}

function turnHtml(type,id){
	/*if(type=='专题聚焦'){
		source='feature';
	}else if(type=='万方资讯'){
		source='activity';
	}else if(type=='基金会议'){
		source='fund';
	}else if(type=='科技动态'){
		source='conf';
	}
	url=ZSFX_URL+"/informationController/getDetails.do?type="+source+"&id="+id;*/
	window.location.href="../content/getDetails.do?id="+id;
}
