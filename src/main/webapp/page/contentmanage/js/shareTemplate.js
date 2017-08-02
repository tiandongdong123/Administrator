var shareType;
var pageNum;
$(function(){
	shareType=$("#shareType").val();
	if(shareType !=""){
		selectValue("xkjb",shareType);
	}
	showPage();
});

function findone(){
	/*var shareType =$("#xkjb").find("option:selected").val();
	window.location.href="../content/shareTemplate.do?shareType="+shareType;*/
	showPage();
}

/*分页显示*/
function showPage(){
	//显示分页
	pageNum = $("#pageNum").val();
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
		url : "../content/shareTemplateJson.do",
		dataType : "json",
		data : {
			"page":curr,
			 "shareType":shareType
			},
		success : serachdata
	});
}

function serachdata(data){
	var pageNum = data.pageNum;
	var pageTotal = data.pageTotal;
	$("#pageNum").val(pageNum);
	$("#pageTotal").val(pageTotal);
	var pageRow=data.pageRow;
	var resHtml = "<tbody><tr style='text-align: center;'>" +
	"<td style='width:10%'><input onclick=\"checkAll()\" class='allId' type='checkbox'></td>" +
	"<td class='mailbox-star' style='width:13%'>序号</td>" +
	"<td class='mailbox-name' style='width:17%'>分享类型</td>"+
	"<td class='mailbox-subject' style='width:30%'>分享内容</td>" +
	"<td class='mailbox-date' style='width:30%'>操作</td></tr>";
if(pageRow.length>0){
for(var i = 0;i<pageRow.length;i++){
	var index = 1+i;
	var rows = pageRow[i];
	resHtml+=" <tr style='text-align: center;'>" +
			"<td><input type='checkbox' name='commonid' value='"+rows.id+"'></td>" +
			"<td class='mailbox-star'><div style=' white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</td>" +
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.shareType+"</td>" +
			"<td class='mailbox-attachment' style='text-align: left;'>"+rows.shareContent+"</td>" +
			"<td class='mailbox-date'>" +
				"<div class='col-md-3 col-sm-4'><a href='#' onclick=\"updateResour('"+rows.id+"')\"><i class='fa fa-fw fa-pencil-square-o'></i></a></div>" +
				"<div class='col-md-3 col-sm-4'><a href='#' onclick=\"removee('"+rows.id+"')\"><i class='fa fa-fw fa-trash-o'></i></a></div></td>"+
          "</tr>";
		}
	 }
		resHtml+="</tbody>";
		$(".table-striped").html(resHtml);
		document.getElementById("here").scrollIntoView();
}

function updateResour(id){
	window.location.href="../content/updateShareTemplate.do?ids="+id;
}
function addResour(){
	window.location.href="../content/addShareTemplate.do";
}

//单条删除
function removee(id){
	$.ajax({
		type : "post",
		data : {ids: id},
		url :  "../content/deleteShareTemplate.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {},
		success : deleteCallback,
		complete : function(XMLHttpRequest, textStatus) {},
		error : function(data) {alert(data);}
	});
}
// 多条删除
function deleteMore(){
	if(!($("input:checkbox[name=commonid]:checked").is(':checked'))){
		layer.msg("请选择删除内容！",{icon: 2});
	}else{		
	var ids = "";
			$("input:checkbox[name=commonid]:checked").each(function(){
				ids += "," + $(this).val();
			});
			if (ids != "")
				ids = ids.substring(1);
			$.ajax({
				type : "post",
				data : {ids: ids},
				url :"../content/deleteShareTemplate.do",
				dataType : "json",
				beforeSend : function(XMLHttpRequest) {},
				success : deleteCallback,
				complete : function(XMLHttpRequest, textStatus) {},
				error : function(data) {alert(data);}
			});
	}
}
// 删除回执
function deleteCallback(data) {
	if (data.flag=="true") {
		layer.msg("删除成功！",{icon: 1});
		//showPage();
		window.location.href="../content/shareTemplate.do";
	}else{
		layer.msg("删除失败！",{icon: 2});
	}
}
//全选

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
	shareType=$("#xkjb").find("option:selected").text();
}

function findtext(){
	shareType=$("#xkjb").find("option:selected").text();
	if(shareType=="全部"){
		shareType="";
	}
}

function refresh(){
	window.location.href="../content/shareTemplate.do";
}

//导出分享模板
function exportshareTemplate(){
	window.location.href="../content/exportshareTemplate.do?shareType="+shareType;
}

