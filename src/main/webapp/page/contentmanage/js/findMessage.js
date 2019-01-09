var branch,clum,human,startTime,endTime,isTop;
var pageNum;
var pageSize = 20;
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
    var infor = $("#infor").find("option:selected").val();
	human=$("#human").val().trim();
	startTime=$("#startTime").val();
	endTime=$("#endTime").val();
    var title = $("#title").val().trim();
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
			"title":title,
			"issueState":infor
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
    "<td class=\"mailbox-name\">发布渠道</td>"+
	"<td class=\"mailbox-name\">PC端栏目</td>"+
    "<td class=\"mailbox-attachment\" style='width:26%'>标题</td>"+
    "<td class=\"mailbox-name\">原文链接</td>"+
    "<td class=\"mailbox-name\">标签</td>"+
    "<td class=\"mailbox-name\">操作人</td>"+
    "<td class=\"mailbox-date\">操作时间</td>"+
    "<td class=\"mailbox-date\">资讯状态</td>"+
    "<td class=\"mailbox-name\">操作</td>"+
    "</tr>";
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+i+20*(pageNum-1);
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
			var is_sueState = rows.issueState;
            var is_top = rows.isTop;
			if(is_sueState=="3"){
                is_sueState="已下撤";
			}else if(is_sueState=="1"){
                is_sueState="未发布";
			}else if(is_sueState=="2"){
                if (is_top == "1"){
                    is_sueState="已置顶发布";
				}else {
                    is_sueState="已发布";
				}
            }
			resHtml+=" <tr style='text-align: center;'>" +
			"<td style='width:10px;'><input type='checkbox' name='commonid' id='"+issue+"' value='"+rows.id+"'></td>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
            "<td class='mailbox-name mailbox-clum'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.channel+"</div></td>"+
			"<td class='mailbox-name mailbox-clum'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.colums+"</div></td>";

			resHtml+="<td><div style='text-align:left;word-wrap:break-word;word-break:break-all;'><a href='javascript:;' onclick=\"turnHtml('"+rows.colums+"','"+rows.id+"')\">"+rows.title+"</a></div></td>";

			resHtml+= "<td class='mailbox-name' style='width:200px;'><div style='width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'><a href='"+rows.linkAddress+"'>"+rows.linkAddress+"</a></div></td>"+

             "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.label==null?"":rows.label)+"</td>"+

            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.human==null?"":rows.human)+"</td>"+
            "<td class='mailbox-date'><div title='"+rows.createTime+"'>"+rows.stick+"</td>"+
            "<td class='mailbox-date'><div title='"+is_sueState+"'>"+is_sueState+"</td>"+
			"<td class='mailbox-name' style='width:353px;'><div>";
            if(is_top==0){
                resHtml+="<button type='button' onclick=\"stick('"+rows.id+"','"+rows.colums+"',1)\" class='btn btn-primary top_btn'>置顶</button>&nbsp;";
            }else {
                resHtml+="<button type='button' onclick=\"stick('"+rows.id+"','"+rows.colums+"',0)\" class='btn btn-primary top_btn' >撤销置顶</button>&nbsp;";
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
    (data.totalRow  > 20)?$("#divPager").show(): $("#divPager").hide();
	/*document.getElementById("here").scrollIntoView();*/
}

//置顶
function stick(id,colums,topState){
	$.ajax({
		type : "post",
		async:false,
		url : "../content/stick.do",
		dataType : "json",
		data : {
			"id":id,
			'colums':colums,
            'isTop':topState
			},
		success : function(data){
		    if(data.code == 200){
                if(topState == 0){
                    layer.msg("置顶撤销成功！");
                }else if( topState == 1){
                    layer.msg("置顶发布成功！");
                }
                showPage();
            }else{
		        layer.msg("置顶发布失败！")
            }
		}
	});
}

function updateMessage(id,issueState){
	if(issueState!=2){
		window.location.href="../content/modify.do?id="+id;
	}else{
		layer.msg("请先下撤该数据再进行修改",{icon: 2});
	}
}

function addMessage(){
	window.location.href="../content/add.do";
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


//批量发布
function publishMore(that,issueState){
    var publishId = [];
    var publishClums = [];
    var params = [];
	$("input:checkbox[name=commonid]:checked").each(function(){
		publishId.push($(this).val()) ;
		publishClums.push($(this).parent("td").siblings(".mailbox-clum").children("div").text());
	});
   for(var i=0;i<publishId.length;i++){
       var _json = {
           id:publishId[i],
           colums:publishClums[i],
           issueState:issueState
	   };
       params[i] = JSON.stringify(_json)
   }
   console.log(params)
    if(issueState == 3){
        value = '是否确定批量下撤?';
    }else if(issueState == 2){
        value = '是否确定批量发布?';
    }
    layer.alert(value,{
        icon: 1,
        skin: 'layui-layer-molv',
        btn: ['确定'], //按钮
        yes: function() {
            layer.closeAll();
            $.ajax({
                type: "post",
                url: "../content/updateIssue.do",
                data:{
                    parameters:params
				},
                dataType: "json",
                traditional: true,
                success: function (data) {
                    layer.closeAll();
                    if (data) {
                        findOne();
                    }
                },
            });
        }
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
	window.location.href="../content/index.do";
}

//发布
function publish(that,obj,colums,issueState){
	var value = "";
	var params=[];
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
	var _json = {
        "id" : obj,
        "colums":colums,
        "issueState":issueState
	}
    params = JSON.stringify(_json);
	layer.alert(value,{
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../content/updateIssue.do",
	    		data :{
                    parameters:params
				},
	    		dataType : "json",
                traditional: true,
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
	window.location.href="../content/detail.do?id="+id;
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
