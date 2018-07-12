var branch,clum,human,startTime,endTime,isTop;
var pageNum;
var pageSize = 20;

//数据提交
function findList(){
	$("#fromList").submit();
}
function serachdata(curr,data){
	var pageNum = data.pageNum;
	var pageTotal = data.pageTotal;
	var pageRow=data.pageRow;
	var resHtml = "<tbody><tr style='text-align: center;'>" +
	"<td class='mailbox-star'>序号</td>" +
	"<td class=\"mailbox-name\">机构名称</td>"+
	"<td class=\"mailbox-name\">机构ID</td>"+
    "<td class=\"mailbox-name\">购买项目</td>"+
    "<td class=\"mailbox-name\">购买数据库</td>"+
    "<td class=\"mailbox-name\">子账号上线</td>"+
    "<td class=\"mailbox-name\">自账号并发数</td>"+
    "<td class=\"mailbox-name\">子账号下载量上限/天</td>"+
    "<td class=\"mailbox-name\">子账号扣款模式</td>"+
    "<td class=\"mailbox-name\">子账号余额上限</td>"+
    "<td class=\"mailbox-name\">子账号类型</td>"+
    "<td class=\"mailbox-name\">子账号名称</td>"+
    "<td class=\"mailbox-name\">子账号密码</td>"+
    "<td class=\"mailbox-name\">子账号IP</td>"+
    "<td class=\"mailbox-name\">子账号余额</td>"+
    "<td class=\"mailbox-name\">子账号次数</td>"+
    "<td class=\"mailbox-name\">子账号权限</td>"+
    "<td class=\"mailbox-name\">子账号有效期</td>"+
    "<td class=\"mailbox-name\">子账号注册时间</td>"+
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
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.colums+"</div></td>";
			resHtml+="<td><div style='text-align:left;word-wrap:break-word;word-break:break-all;'><a href='javascript:;'>"+rows.colums+"</a></div></td>";
			resHtml+="<td class='mailbox-name' style='width:200px;'><div style='width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'><a href='"+rows.linkAddress+"'>"+rows.linkAddress+"</a></div></td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.human==null?"":rows.human)+"</td>"+
            "<td class='mailbox-date'><div title='"+rows.createTime+"'>"+rows.createTime+"</td>"+
            "<td class='mailbox-date'><div title='"+is_top+"' style='width:40px;'>"+is_top+"</td>"+
			"<td class='mailbox-name' style='width:350px;'>111</td>" +
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
}

//导出
function exportexecl(){
	window.location.href="../content/exportMessage.do?branch="+branch+"&colums="+clum+"&human="+human+"&startTime="+startTime+"&endTime="+endTime;
}
//跳转到机构查询页
function goInformation(pid){
	window.location.href='../auser/information.do?userId='+pid;
}
