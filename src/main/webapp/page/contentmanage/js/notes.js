
var types;
var dataState;
var complaintStatus;
var username;
var noteNum;
var resourceName;
var startTime;
var endTime;

$(function(){
	paging();
});
/*分页显示*/
function findOne(){
	paging();
}
function showPage(){
	//显示分页
	var pageNum = $("#pageNum").val();
	var pageTotal = $("#pageTotal").val();
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
	types=new Array();
	dataState=new Array();
	complaintStatus=new Array();
	username=$("#username").val();
	noteNum=$("#noteNum").val();
	resourceName=$("#resourceName").val();
	startTime=$("#startTime").val();
	endTime=$("#endTime").val();
	$("input:checkbox[name='resourceType']:checked").each(function(){
		types.push($(this).val());
	});
	
	$("input:checkbox[name='dataState']:checked").each(function(){
		dataState.push($(this).val());
	});
	
	$("input:checkbox[name='complaintStatus']:checked").each(function(){
		complaintStatus.push($(this).val());
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
			"endTime":endTime
			},
		success : serachdata,
		error: function(XmlHttpRequest, textStatus, errorThrown){  
            alert("失败！");
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
			var index = 1+i;
			var rows = pageRow[i];
			var datast ="";
			var compst="";
			var buttonval="";
			if(rows.dataState=='1'){
				datast="正常";
			}else if(rows.dataState=='0'){
				datast="禁用"
			}
			if(rows.complaintStatus=='1'){
				compst="正常";
			}else if(rows.complaintStatus=='0'){
				compst="<span style='color:red'>申诉</span>";
			}
			if(rows.handlingStatus==0){
				buttonval="<button id=\"statudiv\" type='button' onclick=\"benSHOW('"+rows.id+"')\">禁用</button>";
			}else if(rows.handlingStatus==1){
				buttonval="<button id=\"statudiv\" onclick=\"findNote('"+rows.id+"')\" type='button'>待处理</button>";
			}else if(rows.handlingStatus==2){
				buttonval="<button id=\"statudiv\" onclick=\"findNotes('"+rows.id+"')\" type='button'>处理中</button>";
			}else if(rows.handlingStatus==3){
				buttonval="<button id=\"statudiv\" onclick=\"findNotes('"+rows.id+"')\"   type='button'>已处理</button>";
			}else if(rows.handlingStatus==4){
				buttonval="<button id=\"statudiv\" onclick=\"opennote('"+rows.id+"')\" type='button'>解禁</button>";
			}
			resHtml+=" <tr>" +
				"<td>"+index+"</td>"+
				"<td>"+rows.noteNum+"</td>"+
				"<td>"+rows.resourceNum+"</td>"+
                "<td >"+rows.resourceName+"</td>"+
                "<td>"+rows.resourceType+"</td>"+
                "<td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;' onclick=\"NotesTextShow('"+rows.id+"')\"><a href=\"javascript:void(0)\">"+rows.noteContent+"</a></td>"+
                "<td>"+rows.userId+"</td>"+
                "<td>"+rows.noteDate.substring(0, rows.noteDate.length-2)+"</td>"+
                "<td>"+datast+"</td>"+
                "<td>"+compst+"</td>"+
				"<td>" +buttonval +
				"</td>"+
	            "</tr>";
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

function findNotes(id){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['60%', '90%'],
	    title: '详细内容',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../content/findNote.do?id="+id,
	}); 
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


/*全选与全不选*/
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

function refresh(){
	window.location.href="../content/message.do";
}

function benSHOW(data){
	  /*layer.open({
	        type: 1
	        ,title: false //不显示标题栏
	        ,closeBtn: false
	        ,area: '300px;'
	        ,shade: 0.8
	        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
	        ,btn: ['禁用', '取消']
	        ,moveType: 1 //拖拽模式，0或者1
	        ,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">*禁用后数据将不会在前台显示</div>'
	        ,success: function(layero){
	        	 var btn = layero.find('.layui-layer-btn0');
		          btn.css('text-align', 'center').on("click",function(){
		        	  closenote(data);
		          });
	        }
	      });*/
	
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['60%', '90%'],
	    title: '笔记内容',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../content/findNotes_close_note.do?id="+data+"&type=禁用",
	}); 
	
	
}

function buttonStyle(){
	var sty= $("#statudiv").text();
	if(sty=="待处理"){
		$("#statudiv").css('background','red');
	}else if(sty=="已处理"){
		$("#statudiv").css('background','green');
	}
}

function closenote(id){
	$.ajax( {  
		type : "POST",  
		url : "../content/closenote.do",
			data : {
				'id' : id
			},
			dataType : "json",
			success : function(data) {
				layer.msg("修改成功！",{icon: 1});
				paging($(".laypage_curr").text());
			}
		});
}

function opennote(id){
	$.ajax( {  
		type : "POST",  
		url : "../content/opennote.do",
			data : {
				'id' : id
			},
			dataType : "json",
			success : function(data) {
				layer.msg("修改成功！",{icon: 1});
				paging($(".laypage_curr").text());
			}
		});
}

function NotesTextShow(data){
	 layer.open({
	        type: 2 //此处以iframe举例
	        ,title: '笔记详情信息'
	        ,area: ['50%', '50%']
	        ,shade: 0
	        ,maxmin: true
	        ,content: '../content/noteShow.do?id='+data
	        ,btn: ['关闭'] //只是为了演示
	        ,yes: function(){
	        	layer.closeAll();
	        }
	      })
}


//笔记导出
function exportNotes(){
	window.location.href="../content/exportNotes.do?" +
			"&userName="+username+
			"&noteNum="+noteNum+
			"&resourceName="+resourceName+
			"&resourceType="+types+
			"&dataState="+dataState+
			"&complaintStatus="+complaintStatus+
			"&startTime="+startTime+
			"&endTime="+endTime;
}

