var word_nature,word,status,word_nature_manual,status_manual,word_manual;
var pageNum,pageNumManual;
var pageSize,pageSizeManual;
$(function(){
	showPage(1);
	enterAddWord();
	enterUpdateWord();
	showPageManual(1)
	enterAddWordManual()
	enterUpdateWordManual()
});

/*分页显示*/
function findOne(){
	$("#pagenum").val(1);
	showPage(1);
}

function showPage(curr){
	word_nature=$("#word_nature").find("option:selected").val();
	status=$("#status").find("option:selected").val();
	word=$.trim($("#word").val());
	pageSize=$("#pagesize").val();
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
	var totalRow=data.totalRow;
    var pageall;
    if(totalRow%pageSize==0){
 	   pageall=totalRow/pageSize;
    }else{
 	   pageall= parseInt(totalRow/pageSize)+1;
    }
    var maxLenght=(pageall+"").length;
    $("#totalRow").text(totalRow);
    $("#totalpage").text(pageall);
    $("#pageTotal").val(pageTotal);
    $("#pagenum").attr("maxlength",maxLenght); 
    if(totalRow<=50){
    	$("#pages").hide();
    }else{
    	$("#pages").show();
    }
    
	var resHtml = "<tbody><tr style='text-align: center;'>" +
	"<td><input onclick=\"checkAll()\" class='allId' type='checkbox'></td>" +
	"<td class='mailbox-star'>序号</td>" +
	"<td class=\"mailbox-name\">热搜词</td>"+
    "<td class=\"mailbox-name\">检索量</td>"+
    "<td class=\"mailbox-name\">热搜词来源</td>"+
    "<td class=\"mailbox-name\">操作时间</td>"+
    "<td class=\"mailbox-name\">操作人</td>"+
    "<td class=\"mailbox-name\">热搜词状态</td>"+
    "<td class=\"mailbox-name\">操作</td>"+
    "</tr>";
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+i+pageNum;
			var rows = pageRow[i];
			var issue = rows.wordStatus;
			var issueNum = 1;
			var  word_status="";
			if(issue == 2||issue == 3){
				issue = "发布";
				issueNum = 1;
			}if(issue == 1){
				issue = "下撤";
				issueNum = 3;
			}
			
			if(rows.wordStatus==1){
				word_status="已发布";
			}else if(rows.wordStatus==2){
				word_status="待发布";
			}else{
				word_status="已下撤";
			}
			
			resHtml+=" <tr style='text-align: center;'>" +
			"<td style='width:10px;'><input type='checkbox' name='commonid' id='"+issue+"' value='"+rows.id+"'></td>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'><span id=\""+rows.id+"_span\">"+rows.word+"</span>"+
			"<input id=\""+rows.id+"_value\" type=\"hidden\" value=\""+rows.word+"\" style=\"height:29px;\"/>"+
			"<button type='button' id=\""+rows.id+"_update_word\" onclick=\"update_word('"+rows.id+"')\" class='btn btn-primary' style=\"padding-left: 3px; padding-right: 3px;display:none;\">修改</button>&nbsp;" +
			"<button type='button' id=\""+rows.id+"_cancel\" onclick=\"cancel('"+rows.id+"')\" class='btn btn-primary' style=\"padding-left: 3px; padding-right: 3px;display:none;\">取消</button></div></td>"+
			"</div></td>"+
			"<td><div style='text-align:center;word-wrap:break-word;word-break:break-all;'>"+rows.searchCount+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.wordNature+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.operationTime==null?"":rows.operationTime.substr(0,rows.operationTime.length-2))+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.operation==null?"":rows.operation)+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+word_status+"</td>"+
			"<td class='mailbox-name' style='width:280px;'><div>"+
			"<button type='button' onclick=\"publish(this,'"+rows.id+"',"+issueNum+")\" class='btn btn-primary' id=\"update_issue\">"+issue+"</button>&nbsp;" +
			"<button type='button' onclick=\"update('"+rows.id+"','"+rows.wordStatus+"')\" class='btn btn-primary' id=\"update_one\">修改</button></div></td>" +
          "</tr>";
		}
	}
	resHtml+="</tbody>";
	$("#list").html(resHtml);
	enterUpdateWord();
}


function firstPage(){
	var pagenum=Number($("#pagenum").val());
	if(pagenum==1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum").val("1");
		showPage(1);
	}
}


function upPage(){
	var pagenum=Number($("#pagenum").val())-1;
	if(pagenum<1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum").val(pagenum);
		showPage(pagenum);
	}

}

function lastPage(){
	var pagenum=$("#pagenum").val();
	var total=$("#totalpage").text();
	if(pagenum==$("#pageTotal").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum").val(total);
		showPage(total);
	}
}

function downPage(){
	var pagenum=Number($("#pagenum").val())+1;
	if(pagenum>$("#pageTotal").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum").val(pagenum);
		showPage(pagenum);
	}
}


function getAllpageNum(){
	$("#pagenum").val("1");
	showPage(pagenum);
}

function selectPage(){
	var keyCode=event.keyCode;
	var pagenum=$("#pagenum").val();
	var total=$("#totalpage").text();
	if(keyCode==13){
		if(pagenum>total){
			layer.msg("请输入正确的页码");
		}else if(pagenum==0){
			layer.msg("请输入正确的页码");
		}else if(pagenum<=total){
			showPage(pagenum);
		}
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


function checkCount(){
	var isCount=0;
	$.ajax({
		type : "post",  
		async:false, 
		url : "../content/checkCount.do",
		dataType : "json",
		success : function(data){
			isCount=data;
		},
		error : function(data){
		}
	});
	
	return isCount;
}


function checkWordExist(word){
	 var isExist=false;
		$.ajax({
			type : "post",
			async:false,
			url : "../content/checkWordExist.do",
			dataType : "json",
			data : {"word_content" :word},
			success : function (data){
				isExist=data;
			}
		});
	return isExist;
}

function checkForBiddenWord(word){
	 var isExist=false;
		$.ajax({
			type : "post",
			async:false,
			url : "../content/checkForBiddenWord.do",
			dataType : "json",
			data : {"word" :word},
			success : function (data){
				isExist=data;
			}
		});
	return isExist;
}


/**
 * 添加热搜词
 */
function add_word(){
 var word_content=$.trim($("#word_content").val());
 var success=false;
 if(word_content=='' || word_content==null || word_content==undefined){
	 layer.msg("<div style=\"color:#8B0000;\">请填写热搜词!</div>",{icon: 2});
	 return;
 }
 
 if(checkForBiddenWord(word_content)){
	 layer.msg("<div style=\"color:#8B0000;\">含有敏感词,请重新填写!</div>",{icon: 2});
	 return;
 }
 
 var isExist=checkWordExist(word_content);
 if(isExist){
	 layer.msg("<div style=\"color:#8B0000;\">该热搜词已存在!</div>",{icon: 2});
	 return;
 }
 
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
	layer.msg("<div style=\"color:#0000FF;\">添加成功!</div>",{icon: 1});
	clear();
	showPage(1);
  }else{
	layer.msg("<div style=\"color:#8B0000;\">添加失败!</div>",{icon: 2});
  }
		
}



function update(id,issueState){
	
	if(issueState==1){
		layer.msg("请先下撤该数据再进行修改",{icon: 2});
		return;
	}
	
	$("[id$='_span']").show();
	$("[id$='_value'").attr("type","hidden");
	$("[id$='_update_word'").hide();
	$("[id$='_cancel'").hide();
	
	$("#"+id+"_span").hide();
	$("#"+id+"_value").attr("type","text");
	$("#"+id+"_update_word").show();
	$("#"+id+"_cancel").show();
}

function cancel(id){
	$("#"+id+"_span").show();
	$("#"+id+"_value").attr("type","hidden");
	$("#"+id+"_update_word").hide();
	$("#"+id+"_cancel").hide();

}

function update_word(id){
	var word=$("#"+id+"_value").val();
	var spantext=$("#"+id+"_span").text();
	word=$.trim(word);
	var isExist=false;
	if(word==null || word=='' || word==undefined){
		layer.msg("<div style=\"color:#8B0000;\">请填写热搜词!</div>",{icon: 2});
		return;
	}
	
	if(spantext==word){
		cancel(id);
		return;
	}
		
	isExist=checkWordExist(word);
	if(isExist){
		layer.msg("<div style=\"color:#8B0000;\">该热搜词已存在!</div>",{icon: 2});
		return;
	}
	
	if(checkForBiddenWord(word)){
		layer.msg("<div style=\"color:#8B0000;\">含有敏感词,请重新填写!</div>",{icon: 2});
		return;
	}
	
	$.ajax({
		type : "post",
		async:false,
		url : "../content/updateWord.do",
		dataType : "json",
		data : {"word_content" :word,"id":id},
		success : function (data){
			success=data;
		}
	});
	
	if(success){
		layer.msg("<div style=\"color:#0000FF;\">修改成功!</div>",{icon: 1});
		clear();
		showPage(1);
	}else{
		layer.msg("<div style=\"color:#8B0000;\">修改失败!</div>",{icon: 2});
	}

}


/**
 * 回车添加热搜词
 */
function enterAddWord(){
	$("#word_content").keydown(function(e){
		var curKey=0,e=e||event; 
	  	curKey=e.keyCode||e.which||e.charCode; 
		if(curKey == 13){
			add_word();
			return false;
		}
	});
}



/**
 * 回车修改热搜词
 */
function enterUpdateWord(){
	$("input[id$='_value']").keydown(function(e){
		var curKey=0,e=e||event; 
	  	curKey=e.keyCode||e.which||e.charCode; 
		var id=$(this).attr('id');
		var index=id.indexOf('_');
		id=id.substr(0,index);
		if(curKey == 13){
			update_word(id);
			return false;
		}
	});
}




//发布
function publish(that,obj,issueState){
	if(issueState!='3' && checkCount()>=20){
    	layer.msg("<div style=\"color:#8B0000;\">热搜词已满20条,请下撤后发布!</div>",{icon: 2});
    	return;
	}
	var ids=new Array();
	
	if(issueState==3){
		$("input:checkbox[name=commonid][id='下撤']:checked").each(function(){
			ids.push($(this).val());
		});
	}else{
		$("input:checkbox[name=commonid][id='发布']:checked").each(function(){
			ids.push($(this).val());
		});
	}
	var count=checkCount();
	if((issueState==3)&& ((count-ids.length)<=15)) {
		layer.msg("<div style=\"color:#8B0000;width:330px\">不能再进行下撤了，可以先发布热搜词后再下撤!</div>",{icon: 2});
    	return;
	}
	
	var value = "";
	if(issueState=='3'){
		value = '下撤';
	}else{
		value = '发布';
	}
	var prompt="是否确定"+value+"?";
	layer.alert(prompt,{
		title: value,
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../content/updateWordIssue.do",
	    		data :{ 
	    			"id" : obj,
	    			"issueState":issueState
	    		},
	    		dataType : "json",
	    		success : function(data){
	    			layer.closeAll();
	    			if(data){
	    				layer.msg("<div style=\"color:#0000FF;\">"+value+"成功!</div>",{icon: 1});
	    				clear();
	    				clearManual()
	    				showPage(1);
	    				showPageManual(1)
	    			}else{
	    				layer.msg("<div style=\"color:#8B0000;\">"+value+"失败!</div>",{icon: 2});
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	    }
	  });
}



function batch(status){	
	var str=status==3?"下撤":"发布";
	var ids=new Array();
	if(!$("input:checkbox[name=commonid]:checked").is(':checked')){
		layer.msg("请选择"+str+"内容！",{icon: 2});
		return;
	}
	
	if(status==3){
		str="下撤";
		$("input:checkbox[name=commonid][id='下撤']:checked").each(function(){
			ids.push($(this).val());
		});
	}else{
		str="发布";
		$("input:checkbox[name=commonid][id='发布']:checked").each(function(){
			ids.push($(this).val());
		});
	}
	
	if(ids.length==0){
		layer.msg("<div style=\"color:#8B0000;\">您选择的热搜词没有必要"+str+"!</div>",{icon: 2});
		return;
	}
	
	var count=checkCount();
	if(status!=3 && count>=20){
		layer.msg("<div style=\"color:#8B0000;\">热搜词已满20条,请下撤后发布!</div>",{icon: 2});
    	return;
	}

	if((status==3) && (ids.length>=6)) {
		layer.msg("<div style=\"color:#8B0000;\">最多只能批量下撤5条数据!</div>",{icon: 2});
    	return;
	}
	
	if((status==3)&& ((count-ids.length)<15)) {
		layer.msg("<div style=\"color:#8B0000;width:330px\">不能再进行下撤了，可以先发布热搜词后再下撤!</div>",{icon: 2});
    	return;
	}
	
	layer.alert('确定'+str+'该数据吗？',{
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'], //按钮
	    yes: function(){
			$.ajax({
				type : "post",
				data : {ids: ids,"status":status},
				url :"../content/batch.do",
				dataType : "json",
				beforeSend : function(XMLHttpRequest) {},
				success :function(data){
	    			layer.closeAll();
	    			if(data){
	    				layer.msg("<div style=\"color:#0000FF;\">"+str+"成功!</div>",{icon: 1});
	    				clear();
	    				showPage(1);
	    				location.reload()
	    			}else{
	    				layer.msg("<div style=\"color:#8B0000;\">"+str+"失败!</div>",{icon: 2});
	    			}
	    		},
				complete : function(XMLHttpRequest, textStatus) {},
				error : function(data) {alert(data);}
			});
	        layer.closeAll();
	    }
	});
	
}

function clear(){
	$("#word_content").val("");
	$("#word").val("");
	$("#status option:first").prop("selected", 'selected'); 
	$("#word_nature option:first").prop("selected", 'selected'); 
}


// 手动管理
function findOneManual(){
	$("#pagenum_manual").val(1);
	showPageManual(1);
}
function showPageManual(curr){
	word_nature_manual=$("#word_nature_manual").find("option:selected").val();
	status_manual=$("#status_manual").find("option:selected").val();
	word_manual=$.trim($("#word_manual").val());
	pageSizeManual=$("#pagesize_manual").val();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/hotwordJson.do",
		dataType : "json",
		data : {
			"pageNum" : curr || 1,
			"pageSize" : pageSizeManual,
			"word":word_manual,
			"word_nature":word_nature_manual,
			"status":status_manual
			},
		success : function (data){
			serachdataManual(curr,data);
		}
	});
}

function serachdataManual(curr,data){
	var pageSize = data.pageSize
	var pageNum = data.pageNum;
	var pageTotal = data.pageTotal;
	var pageRow=data.pageRow;
	var totalRow=data.totalRow;
    var pageall;
    if(totalRow%pageSize==0){
 	   pageall=totalRow/pageSize;
    }else{
 	   pageall= parseInt(totalRow/pageSize)+1;
    }
    var maxLenght=(pageall+"").length;
    $("#totalRowManual").text(totalRow);
    $("#totalpageManual").text(pageall);
    $("#pageTotalManual").val(pageTotal);
    $("#pagenum_manual").attr("maxlength",maxLenght); 
    if(totalRow<=50){
    	$("#pages_manual").hide();
    }else{
    	$("#pages_manual").show();
    }
    
	var resHtml = "<tbody><tr style='text-align: center;'>" +
	"<td><input onclick=\"checkAllManual()\" class='allIdManual' type='checkbox'></td>" +
	"<td class='mailbox-star'>序号</td>" +
	"<td class=\"mailbox-name\">热搜词</td>"+
    "<td class=\"mailbox-name\">检索量</td>"+
    "<td class=\"mailbox-name\">热搜词来源</td>"+
    "<td class=\"mailbox-name\">操作时间</td>"+
    "<td class=\"mailbox-name\">操作人</td>"+
    "<td class=\"mailbox-name\">热搜词状态</td>"+
    "<td class=\"mailbox-name\">操作</td>"+
    "</tr>";
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+i+pageNum;
			var rows = pageRow[i];
			var issue = rows.wordStatus;
			var issueNum = 1;
			var  word_status="";
			if(issue == 2||issue == 3){
				issue = "发布";
				issueNum = 1;
			}if(issue == 1){
				issue = "下撤";
				issueNum = 3;
			}
			
			if(rows.wordStatus==1){
				word_status="已发布";
			}else if(rows.wordStatus==2){
				word_status="待发布";
			}else{
				word_status="已下撤";
			}
			
			resHtml+=" <tr style='text-align: center;'>" +
			"<td style='width:10px;'><input type='checkbox' name='commonidManual' id='"+issue+"' value='"+rows.id+"'></td>" +
			"<td class='mailbox-star'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+index+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'><span id=\""+rows.id+"_mspan\">"+rows.word+"</span>"+
			"<input id=\""+rows.id+"_mvalue\" type=\"hidden\" value=\""+rows.word+"\" style=\"height:29px;\"/>"+
			"<button type='button' id=\""+rows.id+"_mupdate_word\" onclick=\"update_wordManual('"+rows.id+"')\" class='btn btn-primary' style=\"padding-left: 3px; padding-right: 3px;display:none;\">修改</button>&nbsp;" +
			"<button type='button' id=\""+rows.id+"_mcancel\" onclick=\"cancelManual('"+rows.id+"')\" class='btn btn-primary' style=\"padding-left: 3px; padding-right: 3px;display:none;\">取消</button></div></td>"+
			"</div></td>"+
			"<td><div style='text-align:center;word-wrap:break-word;word-break:break-all;'>"+rows.searchCount+"</div></td>"+
			"<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+rows.wordNature+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.operationTime==null?"":rows.operationTime.substr(0,rows.operationTime.length-2))+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+(rows.operation==null?"":rows.operation)+"</td>"+
            "<td class='mailbox-name'><div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+word_status+"</td>"+
			"<td class='mailbox-name' style='width:280px;'><div>"+
			"<button type='button' onclick=\"publishManual(this,'"+rows.id+"',"+issueNum+")\" class='btn btn-primary' id=\"update_issue\">"+issue+"</button>&nbsp;" +
			"<button type='button' onclick=\"updateManual('"+rows.id+"','"+rows.wordStatus+"')\" class='btn btn-primary' id=\"update_one\">修改</button></div></td>" +
          "</tr>";
		}
	}
	resHtml+="</tbody>";
	$("#list_manual").html(resHtml);
	enterUpdateWord();
}
function firstPageManual(){
	var pagenum=Number($("#pagenum_manual").val());
	if(pagenum==1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum_manual").val("1");
		showPageManual(1);
	}
}
function upPageManual(){
	var pagenum=Number($("#pagenum_manual").val())-1;
	if(pagenum<1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum_manual").val(pagenum);
		showPageManual(pagenum);
	}
}
function lastPageManual(){
	var pagenum=$("#pagenum_manual").val();
	var total=$("#totalpageManual").text();
	if(pagenum==$("#pageTotalManual").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum_manual").val(total);
		showPageManual(total);
	}
}
function downPageManual(){
	var pagenum=Number($("#pagenum_manual").val())+1;
	if(pagenum>$("#pageTotalManual").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum_manual").val(pagenum);
		showPageManual(pagenum);
	}
}
function getAllpageNumManual(){
	$("#pagenum_manual").val("1");
	showPageManual(pagenum);
}

function selectPageManual(){
	var keyCode=event.keyCode;
	var pagenum=$("#pagenum_manual").val();
	var total=$("#totalpageManual").text();
	if(keyCode==13){
		if(pagenum>total){
			layer.msg("请输入正确的页码");
		}else if(pagenum==0){
			layer.msg("请输入正确的页码");
		}else if(pagenum<=total){
			showPageManual(pagenum);
		}
	}
}
function checkAllManual() {
	if ($(".allIdManual").is(':checked')) {
		$("input[name=commonidManual]").each(function() {
			$(this).prop("checked", "checked");
		});
	} else {
		$("input[name=commonidManual]").each(function() {
			$(this).removeAttr("checked");
		});
	}
}
function checkCountManual(){
	var isCount=0;
	$.ajax({
		type : "post",  
		async:false, 
		url : "../content/checkCount.do",
		dataType : "json",
		success : function(data){
			isCount=data;
		},
		error : function(data){
		}
	});
	
	return isCount;
}


function checkWordExistManual(word){
	 var isExist=false;
		$.ajax({
			type : "post",
			async:false,
			url : "../content/checkWordExist.do",
			dataType : "json",
			data : {"word_content" :word},
			success : function (data){
				isExist=data;
			}
		});
	return isExist;
}

function checkForBiddenWordManual(word){
	 var isExist=false;
		$.ajax({
			type : "post",
			async:false,
			url : "../content/checkForBiddenWord.do",
			dataType : "json",
			data : {"word" :word},
			success : function (data){
				isExist=data;
			}
		});
	return isExist;
}
function add_word_manual(){
	 var word_content=$.trim($("#word_content_manual").val());
	 var success=false;
	 if(word_content=='' || word_content==null || word_content==undefined){
		 layer.msg("<div style=\"color:#8B0000;\">请填写热搜词!</div>",{icon: 2});
		 return;
	 }
	 
	 if(checkForBiddenWordManual(word_content)){
		 layer.msg("<div style=\"color:#8B0000;\">含有敏感词,请重新填写!</div>",{icon: 2});
		 return;
	 }
	 
	 var isExist=checkWordExistManual(word_content);
	 if(isExist){
		 layer.msg("<div style=\"color:#8B0000;\">该热搜词已存在!</div>",{icon: 2});
		 return;
	 }
	 
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
		layer.msg("<div style=\"color:#0000FF;\">添加成功!</div>",{icon: 1});
		clearManual();
		showPage(1);
		showPageManual(1)
	  }else{
		layer.msg("<div style=\"color:#8B0000;\">添加失败!</div>",{icon: 2});
	  }
			
	}
function updateManual(id,issueState){
	
	if(issueState==1){
		layer.msg("请先下撤该数据再进行修改",{icon: 2});
		return;
	}
	
	$("[id$='_mspan']").show();
	$("[id$='_mvalue'").attr("type","hidden");
	$("[id$='_mupdate_word'").hide();
	$("[id$='_mcancel'").hide();
	
	$("#"+id+"_mspan").hide();
	$("#"+id+"_mvalue").attr("type","text");
	$("#"+id+"_mupdate_word").show();
	$("#"+id+"_mcancel").show();
}
function cancelManual(id){
	$("#"+id+"_mspan").show();
	$("#"+id+"_mvalue").attr("type","hidden");
	$("#"+id+"_mupdate_word").hide();
	$("#"+id+"_mcancel").hide();
}
function update_wordManual(id){
	var word=$("#"+id+"_mvalue").val();
	var spantext=$("#"+id+"_mspan").text();
	word=$.trim(word);
	var isExist=false;
	if(word==null || word=='' || word==undefined){
		layer.msg("<div style=\"color:#8B0000;\">请填写热搜词!</div>",{icon: 2});
		return;
	}
	
	if(spantext==word){
		cancelManual(id);
		return;
	}
		
	isExist=checkWordExistManual(word);
	if(isExist){
		layer.msg("<div style=\"color:#8B0000;\">该热搜词已存在!</div>",{icon: 2});
		return;
	}
	
	if(checkForBiddenWordManual(word)){
		layer.msg("<div style=\"color:#8B0000;\">含有敏感词,请重新填写!</div>",{icon: 2});
		return;
	}
	
	$.ajax({
		type : "post",
		async:false,
		url : "../content/updateWord.do",
		dataType : "json",
		data : {"word_content" :word,"id":id},
		success : function (data){
			success=data;
		}
	});
	
	if(success){
		layer.msg("<div style=\"color:#0000FF;\">修改成功!</div>",{icon: 1});
		clearManual();
		showPageManual(1);
	}else{
		layer.msg("<div style=\"color:#8B0000;\">修改失败!</div>",{icon: 2});
	}
}
function enterAddWordManual(){
	$("#word_content_manual").keydown(function(e){
		var curKey=0,e=e||event; 
	  	curKey=e.keyCode||e.which||e.charCode; 
		if(curKey == 13){
			add_word_manual();
			return false;
		}
	});
}
function enterUpdateWordManual(){
	$("input[id$='_mvalue']").keydown(function(e){
		var curKey=0,e=e||event; 
	  	curKey=e.keyCode||e.which||e.charCode; 
		var id=$(this).attr('id');
		var index=id.indexOf('_');
		id=id.substr(0,index);
		if(curKey == 13){
			update_wordManual(id);
			return false;
		}
	});
}

function publishManual(that,obj,issueState){
	if(issueState!='3' && checkCount()>=20){
    	layer.msg("<div style=\"color:#8B0000;\">热搜词已满20条,请下撤后发布!</div>",{icon: 2});
    	return;
	}
var ids=new Array();
	
	if(issueState==3){
		$("input:checkbox[name=commonid][id='下撤']:checked").each(function(){
			ids.push($(this).val());
		});
	}else{
		$("input:checkbox[name=commonid][id='发布']:checked").each(function(){
			ids.push($(this).val());
		});
	}
	var count=checkCount();
	if((issueState==3)&& ((count-ids.length)<=15)) {
		layer.msg("<div style=\"color:#8B0000;width:330px\">不能再进行下撤了，可以先发布热搜词后再下撤!</div>",{icon: 2});
    	return;
	}
	
	var value = "";
	if(issueState=='3'){
		value = '下撤';
	}else{
		value = '发布';
	}
	var prompt="是否确定"+value+"?";
	layer.alert(prompt,{
		title: value,
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../content/updateWordManualIssue.do",
	    		data :{ 
	    			"id" : obj,
	    			"issueState": issueState
	    		},
	    		dataType : "json",
	    		success : function(data){
	    			layer.closeAll();
	    			if(data){
	    				layer.msg("<div style=\"color:#0000FF;\">"+value+"成功!</div>",{icon: 1});
	    				clear();
	    				clearManual()
	    				showPage(1);
	    				showPageManual(1)
	    			}else{
	    				layer.msg("<div style=\"color:#8B0000;\">"+value+"失败!</div>",{icon: 2});
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	    }
	  });
}
function batchManual(status){	
	var str=status==3?"下撤":"发布";
	var ids=new Array();
	if(!$("input:checkbox[name=commonidManual]:checked").is(':checked')){
		layer.msg("请选择"+str+"内容！",{icon: 2});
		return;
	}
	
	if(status==3){
		str="下撤";
		$("input:checkbox[name=commonidManual][id='下撤']:checked").each(function(){
			ids.push($(this).val());
		});
	}else{
		str="发布";
		$("input:checkbox[name=commonidManual][id='发布']:checked").each(function(){
			ids.push($(this).val());
		});
	}
	
	if(ids.length==0){
		layer.msg("<div style=\"color:#8B0000;\">您选择的热搜词没有必要"+str+"!</div>",{icon: 2});
		return;
	}
	
	var count=checkCount();
	console.log(count)
	if(status!=3 && count>=20){
		layer.msg("<div style=\"color:#8B0000;\">热搜词已满20条,请下撤后发布!</div>",{icon: 2});
    	return;
	}
	
	if((status==3) && (ids.length>=6)&&(((count-ids.length)<=15))) {
		layer.msg("<div style=\"color:#8B0000;\">最多只能批量下撤5条数据!</div>",{icon: 2});
    	return;
	}
	
	if((status==3)&& ((count-ids.length)<15)) {
		layer.msg("<div style=\"color:#8B0000;width:330px;\">不能再进行下撤了，可以先发布热搜词后再下撤!</div>",{icon: 2});
    	return;
	}
	
	layer.alert('确定'+str+'该数据吗？',{
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'], //按钮
	    yes: function(){
			$.ajax({
				type : "post",
				data : {ids: ids,"status":status},
				url :"../content/batchManual.do",
				dataType : "json",
				beforeSend : function(XMLHttpRequest) {},
				success :function(data){
	    			layer.closeAll();
	    			if(data){
	    				layer.msg("<div style=\"color:#0000FF;\">"+str+"成功!</div>",{icon: 1});
	    				clear();
	    				clearManual()
	    				showPage(1);
	    				showPageManual(1)
	    				location.reload()
	    			}else{
	    				layer.msg("<div style=\"color:#8B0000;\">"+str+"失败!</div>",{icon: 2});
	    			}
	    		},
				complete : function(XMLHttpRequest, textStatus) {},
				error : function(data) {alert(data);}
			});
	        layer.closeAll();
	    }
	});
	
}
function clearManual(){
	$("#word_content_manual").val("");
	$("#word_manual").val("");
	$("#status_manual option:first").prop("selected", 'selected'); 
	$("#word_nature_manual option:first").prop("selected", 'selected'); 
}