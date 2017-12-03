$(function(){
	paging();
});
//
//function showPage(){
//	//显示分页
//	var pageNum = $("#pageNum").val();
//	var pageTotal = $("#pageTotal").val();
//	laypage({
//    	cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
//        pages: pageTotal, //通过后台拿到的总页数
//        curr: pageNum || 1, //当前页
//        skip: true, //是否开启跳页
//	      skin: '#367fa9',//当前页颜色，可16进制
//	      groups: 4, //连续显示分页数
//	      first: '首页', //若不显示，设置false即可
//	      last: '尾页', //若不显示，设置false即可
//	      prev: '上一页', //若不显示，设置false即可
//	      next: '下一页', //若不显示，设置false即可
//	      jump: function(obj, first){ //触发分页后的回调
//	        if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
//	            paging(obj.curr);
//	        }else{
//	        	paging(obj.curr);
//	        }
//       }
//    });
//}
/**
 * 分页事件
 */
function paging(curr){
	var types=new Array();
	var dataState=new Array();
	var complaintStatus=new Array();
	var noteProperty=new Array();
	var performAction=new Array();
	
	var username=$("#username").val();//用户ID
	var noteNum=$("#noteNum").val();
	var resourceName=$("#resourceName").val();//文献标题
	var startTime=$("#startTime").val();//开始时间
	var endTime=$("#endTime").val();//结束时间
	$("input:checkbox[name='resourceType']:checked").each(function(){
		types.push($(this).val());
	});
	$("input:checkbox[name='dataState']:checked").each(function(){
		dataState.push($(this).val());
	});
	
	$("input:checkbox[name='complaintStatus']:checked").each(function(){
		complaintStatus.push($(this).val());
	});
	
	$("input:checkbox[name='noteProperty']:checked").each(function(){
		noteProperty.push($(this).val());
	});
	$("input:checkbox[name='performAction']:checked").each(function(){
		performAction.push($(this).val());
	});
	$.ajax({
		type : "post",
		async:false,
		url : "../content/notesJson.do",
		dataType : "json",
		data : {
			"page":curr || 1,
			"userName":username,
			"noteNum":noteNum,
			"resourceName":resourceName,
			"resourceType":types,
			"dataState":dataState,
			"complaintStatus":complaintStatus,
			"startTime":startTime,
			"endTime":endTime,
			"noteProperty":noteProperty,
			"performAction":performAction
			},
		success : serachdata,
		error: function(XmlHttpRequest, textStatus, errorThrown){  
            alert("请求失败，请刷新页面！");
        }
	});
}

function serachdata(data){
	var pageNum = data.pageNum;
	var pageTotal = data.pageTotal;
	$("#pageNum").val(pageNum);
	$("#pageTotal").val(pageTotal);
	var pageRow = data.pageRow;
	if(pageRow.length>0){
		var resHtml ="";
		for(var i = 0;i<pageRow.length;i++){
			var index = 1 + i + (pageNum-1)*10;
			var rows = pageRow[i];
			var datast ="";
			var name = "";
			if(rows.dataState=='1'){
				datast="正常";
			}else if(rows.dataState=='0'){
				datast="<span style='color:red;'>禁用</span>";
			}
			if(null != rows.userRealname && '' != rows.userRealname){
				name = rows.userRealname;
			}else if(null != rows.userNickname && '' != rows.userNickname){
				name = rows.userNickname;
			}else {
				name = rows.auditId;
			}
			resHtml+="<tr>";
			resHtml+="<td>"+index+"</td>";
			resHtml+="<td>"+rows.noteNum+"</td>";
			resHtml+="<td >"+rows.resourceName+"</td>";
			resHtml+="<td>"+rows.resourceType+"</td>";
			resHtml+="<td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;'>"+rows.noteContent+"</td>";
			resHtml+="<td>"+rows.userId+"</td>";
			if(rows.noteDate.indexOf('.') != -1){				
				resHtml+="<td>"+rows.noteDate.substring(0,rows.noteDate.indexOf('.'))+"</td>";
			}else{
				resHtml+="<td></td>";
			}
			var performAction = "";
			if(rows.performAction == 0){
				performAction = "新增";
			}else if(rows.performAction == 1){
				performAction = "修改";
			}else if(rows.performAction == 2){
				performAction = "删除";
			}
			resHtml+="<td>" + performAction + "</td>";	
			var noteProperty = "";
			if(rows.noteProperty == 0){
				noteProperty = "私有";
			}else if(rows.noteProperty == 1){
				noteProperty = "公开";
			}
			resHtml+="<td>" + noteProperty + "</td>";	
			resHtml+="<td>"+datast+"</td>";
			resHtml+="<td>" + name + "</td>";
			if(rows.auditTime.indexOf('.') != -1){	
				resHtml+="<td>"+ rows.auditTime.substring(0,rows.auditTime.indexOf('.')) + "</td>";
			}else{
				resHtml+="<td></td>";
			}
			resHtml+="<td><a  id=\"statudiv\" href=\"javascript:void(0);\" onclick=\"findNote('"+rows.recordId+"')\">详情</a></td>";
			resHtml+="</tr>";
			}
		$("#notebody").html(resHtml);
		//显示分页
		//var pageNum = $("#pageNum").val();
		//var pageTotal = $("#pageTotal").val();
		laypage({
	    	cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
	        pages: pageTotal, //通过后台拿到的总页数
	        curr: pageNum || 1, //当前页
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
		      }
	       }
	   });
	}else{
		$("#page").html("");
		$("#notebody").html("");
	}
}

function findNote(id){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['60%', '90%'],
	    title: '笔记内容',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../content/findNote.do?id="+id,
	}); 
}

//笔记导出
function exportNotes(){
	var types=new Array();
	var dataState=new Array();
	var complaintStatus=new Array();
	var noteProperty=new Array();
	var performAction=new Array();
	
	var username=$("#username").val();//用户ID
	var noteNum=$("#noteNum").val();
	var resourceName=$("#resourceName").val();//文献标题
	var startTime=$("#startTime").val();//开始时间
	var endTime=$("#endTime").val();//结束时间
	$("input:checkbox[name='resourceType']:checked").each(function(){
		types.push($(this).val());
	});
	
	$("input:checkbox[name='dataState']:checked").each(function(){
		dataState.push($(this).val());
	});
	
	$("input:checkbox[name='complaintStatus']:checked").each(function(){
		complaintStatus.push($(this).val());
	});
	
	$("input:checkbox[name='noteProperty']:checked").each(function(){
		noteProperty.push($(this).val());
	});
	
	$("input:checkbox[name='performAction']:checked").each(function(){
		performAction.push($(this).val());
	});
	window.location.href="../content/exportNotes.do?" +
			"&userName="+username+
			"&noteNum="+noteNum+
			"&resourceName="+resourceName+
			"&resourceType="+types+
			"&dataState="+dataState+
			"&complaintStatus="+complaintStatus+
			"&startTime="+startTime+
			"&endTime="+endTime+
			"&noteProperty="+noteProperty +
			"&performAction=" + performAction;
}

/*资源类型全选与全不选*/
function checkAllText(){
	if($("#resourType").is(':checked')){
		$("input[name=resourceType]").each(function(){
			$(this).prop("checked", "checked");
		});
	}else{
		$("input[name=resourceType]").each(function(){
			$(this).removeAttr("checked");
		});
	}
}

/*笔记性质全选与全不选*/
function checkAllNoteProperty(){
	if($("#noteProperty").is(':checked')){
		$("input[name=noteProperty]").each(function(){
			$(this).prop("checked", "checked");
		});
	}else{
		$("input[name=noteProperty]").each(function(){
			$(this).removeAttr("checked");
		});
	}
}

/*执行操作全选与全不选*/
function checkAllPerformAction(){
	if($("#performAction").is(':checked')){
		$("input[name=performAction]").each(function(){
			$(this).prop("checked", "checked");
		});
	}else{
		$("input[name=performAction]").each(function(){
			$(this).removeAttr("checked");
		});
	}
}

function resourType(){
	var state = true;
	$('input[name="resourceType"]').each(function(){
		if(state&&!$(this).is(':checked')){
			state = false;
		}
	});
	if(state){
		$('#resourType').prop("checked", "checked");
	}else{
		$('#resourType').removeAttr('checked');
	}
}

function noteProperty(){
	var state = true;
	$('input[name=noteProperty]').each(function(){
		if(state&&!$(this).is(':checked')){
			state = false;
		}
	});
	if(state){
		$('#noteProperty').prop("checked", "checked");
	}else{
		$('#noteProperty').removeAttr('checked');
	}
}

function performAction(){
	var state = true;
	$('input[name=performAction]').each(function(){
		if(state&&!$(this).is(':checked')){
			state = false;
		}
	});
	if(state){
		$('#performAction').prop("checked", "checked");
	}else{
		$('#performAction').removeAttr('checked');
	}
}


