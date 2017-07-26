
var typeName;
$(function(){
	showPage();
});

function findone(){
	typeName =$("#typeName").val();
	
	typeName=window.encodeURI(window.encodeURI(typeName));
window.location.href="../content/resourceManage.do?typeName="+typeName;
}

/*分页显示*/
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
	typeName =$("#typeName").val();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/resourceManageJson.do",
		dataType : "json",
		data : {
			"page":curr,
			 "typeName":typeName
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
	$("#tbody").remove();
	var resHtml = "<tbody id='tbody'><tr style='text-align: center;'>" +
	"<td><input onclick=\"checkAll()\" class='allId' type='checkbox'></td>" +
	"<td class='mailbox-star'>序号</td>" +
	"<td class='mailbox-name'>资源类型名称</td>"+
	"<td class='mailbox-subject'>资源类型描述</td>" +
	"<td class='mailbox-subject'>资源类型code</td>" +
	"<td class='mailbox-date'>操作</td></tr>";
if(pageRow.length>0){
for(var i = 0;i<pageRow.length;i++){
	var index = 1+i;
	var rows = pageRow[i];
	resHtml+=" <tr style='text-align: center;'>" +
			"<td><input type='checkbox' name='commonid' value='"+rows.id+"'></td>" +
			"<td class='mailbox-star'>"+index+"</td>" +
			"<td class='mailbox-name'>"+rows.typeName+"</td>" +
			"<td class='mailbox-attachment' style='text-align: left;'>"+rows.typedescri+"</td>" +
			"<td class='mailbox-attachment' style='text-align: left;'>"+rows.typeCode+"</td>" +
			"<td class='mailbox-date'>" +
				"<div class='col-md-3 col-sm-4'><a href='#' onclick=\"updateResour('"+rows.id+"')\"><i class='fa fa-fw fa-pencil-square-o'></i></a></div>" +
				"<div class='col-md-3 col-sm-4'><a href='#' onclick=\"remove('"+rows.id+"')\"><i class='fa fa-fw fa-trash-o'></i></a></div></td>"+
				/*"<div class='col-md-3 col-sm-4'><a href='#' onclick=\"publish('"+rows.id+"')\">发布</a></div>"+*/
          "</tr>";
		}
	}
	resHtml+="</tbody>";
	$(".table-striped").html(resHtml);
	document.getElementById("here").scrollIntoView();
}

function updateResour(id){
	window.location.href="../content/updateResource.do?idRes="+id;
}
function addResour(){
	window.location.href="../content/addResource.do";
}

//单条删除
function remove(id){
			$.ajax({
				type : "post",
				data : {ids: id},
				url :  "../content/deleteResourceType.do",
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
		alert("请选择删除内容！");
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
				url :"../content/deleteResourceType.do",
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
		alert("删除成功!");
		showPage();
		//window.location.href="../content/resourceManage.do";
	}else{
		alert("删除失败!");
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

function refresh(){
	window.location.href="../content/resourceManage.do";
}

//----------------------------发布--liuYong-----------------------------
function publish(obj,colums,issueState){
	layer.alert('确定发布到前台？', {
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../content/resourcePublish.do",
	    		data :{ 
	    			"id" : obj,
	    			"colums":colums,
	    			"issueState":issueState
	    		},
	    		dataType : "json",
	    		success : function(data){
	    			if(data){
	    				refresh();
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	        layer.closeAll();
	    }
	  });
}

function pushData(){
	$.post("../content/pushdata.do",function(){});
}

//导出资源
function exportResource(){
	window.location.href="../content/exportResource.do?resouceType="+typeName;
}

