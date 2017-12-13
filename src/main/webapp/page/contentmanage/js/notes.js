$(function(){
	paging();
});

function selectPage(e){
	var keyCode=e.keyCode;
	var pagenum=$("#pagenum").val();
	var total=$("#pageTotal").val();
	if(keyCode==13){
		if(isNaN(pagenum)){
			layer.msg("请输入数字");
			return;
		}else if(parseInt(pagenum)<1 || parseInt(total)<parseInt(pagenum)){
			layer.msg("请输入正确的页码");
			return
		}else if(1<=parseInt(pagenum)<=total){
			paging(pagenum);
		}
	}
}

/**
 * 分页事件
 */
function paging(curr){
	if(0 == curr){
		layer.msg("已经是第一页了");
		return;
	}
	if(curr > $("#pageTotal").val()){
		layer.msg("已经是最后一页了");
		return;
	}
	var types=new Array();
	var dataState=new Array();
	var complaintStatus=new Array();
	var noteProperty=new Array();
	var performAction=new Array();
	
	var pagesize = $("#pagesize").val();//页面大小
	
	var username=$("#username").val();//用户ID
	var noteNum=$("#noteNum").val();
	var resourceName=$("#resourceName").val();//文献标题
	var startTime=$("#startTime").val();//开始时间
	var endTime=$("#endTime").val();//结束时间
	if("" == $("#resourceType").val()){	
		$("#resourceType option").each(function(){
			if("" != $(this).val()){				
				types.push($(this).val());
			}
		});
	}else{
		types.push($("#resourceType").val());
	}
	if("" == $("#dataState").val()){	
		$("#dataState option").each(function(){
			if("" != $(this).val()){				
				dataState.push($(this).val());
			}
		});
	}else{
		dataState.push($("#dataState").val());
	}
	if("" == $("#complaintStatus").val()){	
		$("#complaintStatus option").each(function(){
			if("" != $(this).val()){				
				complaintStatus.push($(this).val());
			}
		});
	}else{
		complaintStatus.push($("#complaintStatus").val());
	}
	if("" == $("#noteProperty").val()){	
		$("#noteProperty option").each(function(){
			if("" != $(this).val()){				
				noteProperty.push($(this).val());
			}
		});
	}else{
		noteProperty.push($("#noteProperty").val());
	}
	if("" == $("#performAction").val()){	
		$("#performAction option").each(function(){
			if("" != $(this).val()){				
				performAction.push($(this).val());
			}
		});
	}else{
		performAction.push($("#performAction").val());
	}
	$.ajax({
		type : "post",
		async:false,
		url : "../content/notesJson.do",
		dataType : "json",
		data : {
			"page":curr || 1,
			"pagesize":pagesize || 10,
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
	var pageSize = data.pageSize;
	var totalRow = data.totalRow;
	$("#pageTotal").val(pageTotal);
	var pageRow = data.pageRow;
	if(pageRow.length>0){
		var resHtml ="";
		for(var i = 0;i<pageRow.length;i++){
			var index = 1 + i + (pageNum-1)*pageSize;
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
			resHtml+="<td>" + datast +"</td>";
			resHtml+="<td>" + name + "</td>";
			if(rows.auditTime.indexOf('.') != -1){	
				resHtml+="<td>"+ rows.auditTime.substring(0,rows.auditTime.indexOf('.')) + "</td>";
				resHtml+="<td><a  id=\"statudiv\" href=\"javascript:void(0);\" onclick=\"findNote('"+rows.recordId+"')\" style=\"color:black;\">已处理</a></td>";
			}else{
				resHtml+="<td></td>";
				resHtml+="<td><a  id=\"statudiv\" href=\"javascript:void(0);\" onclick=\"findNote('"+rows.recordId+"')\">详情</a></td>";
			}
			resHtml+="</tr>";
		}
		$("#notebody").html(resHtml);
		
		var pageHtml ="";
		pageHtml += '<span>每页显示:</span>';
		pageHtml += '<select id="pagesize" onchange="paging();">';
		pageHtml += '	<option value="10">10</option>';
		pageHtml += '	<option value="20">20</option>';	
		pageHtml += '	<option value="30">30</option>';
		pageHtml += '	<option value="50">50</option>';
		pageHtml += '</select>';
		pageHtml += '<span>条数据</span>';
		pageHtml += '<a href="javascript:void(0);" class="first_page" onclick="paging();"><img src="../page/contentmanage/images/first_page.png"/></a>';
		pageHtml += '<a href="javascript:void(0);" class="up_page" onclick="paging(' + (pageNum-1) +');"><img src="../page/contentmanage/images/up_page.png"/></a>';
		pageHtml += '<span>当前在第</span>';
		pageHtml += '<input id="pagenum" maxlength="" onkeydown="selectPage(event);"  style="width: 40px;" value="' + pageNum + '" />';
		pageHtml += '<span>页</span>';
		pageHtml += '<a href="javascript:void(0);" class="down_page" onclick="paging(' + (pageNum+1) +');"><img src="../page/contentmanage/images/down_page.png"/></a>';
		pageHtml += '<a href="javascript:void(0);" class="last_page" onclick="paging(' + (pageTotal) +');"><img src="../page/contentmanage/images/last_page.png"/></a>';
		pageHtml += '<span>共<u id="totalRow">' + totalRow + '</u>条数据</span>';
		pageHtml += '<span>共<u id="totalpage">' + pageTotal + '</u>页</span>';
		$('#pages').html(pageHtml);
		$('#pagesize option[value="'+pageSize+'"]').attr('selected', true);//选中页面大小值
	}else{
		$("#pages").html("");
		$("#notebody").html("");
		layer.msg("没有检索到数据!");
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


