var username;
var perioname;
var startTime;
var endTime; 
var subtime;
var sauditm;
var eauditm;
var slayoutm;
var elayoutm;
var dataState;
var complaintStatus;

$(function(){
	commentpage(1);
});
$(function(){
	$(".layui-layer-close1").on('click',function(){
		 window.location.reload();
	});
});
//分页显示
function commentpage(curr){
	
    $("#commentbody tr").remove();
	 username = $("#username").val();
	 perioname = $("#perioname").val();
	 startTime = $("#startTime").val();
	 endTime = $("#endTime").val();
	 subtime=$("#subtime").find("option:selected").text();
	if(subtime=='请选择'){
		subtime='';
	}
	
	if(startTime!=null&&endTime!=null){
		 var d1 = new Date(startTime.replace(/\-/g, "\/"));  
		 var d2 = new Date(endTime.replace(/\-/g, "\/")); 
		 if(d1 >d2){
			 layer.msg("请查看，起始日期和结束日期时间是否符合标准！");
			 return false;
		 }
	}
	subtime=subtime.replace("个月","");
	sauditm = $("#sauditm").val();
	eauditm = $("#eauditm").val();
	slayoutm = $("#slayoutm").val();
	elayoutm = $("#elayoutm").val();
	
	dataState=new Array();
	complaintStatus=new Array();
	
	$("input:checkbox[name='dataState']:checked").each(function(){
		dataState.push($(this).val());
	});
	
	$("input:checkbox[name='complaintStatus']:checked").each(function(){
		complaintStatus.push($(this).val());
	});
	
    $.post('../periocomment/getcomment.do', {
        pagenum: curr,//向服务端传的参数
        pagesize :10,
        authorName:username,
        perioName : perioname,
        startTime :startTime,
        endTime:endTime,
        subTime:subtime,
        sauditm:sauditm,
        eauditm:eauditm,
        slayoutm:slayoutm,
        elayoutm:elayoutm,
        dataStateArr:dataState,
        complaintStatusArr:complaintStatus
    }, function(res){
    	
    	var totalRow = res.pageTotal;
        var pageSize = res.pageSize;
        var pages;
        var groups;
        if(totalRow%pageSize==0)
        {	
        	 pages = totalRow/pageSize; 
        }else
        {
        	pages = totalRow/pageSize+1;
        }
        if(pages>=4)
        {
        groups=4;
        }else
        {
        	groups=pages;
        }
        //显示分页
        laypage({
        	cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
            pages: pages, //通过后台拿到的总页数
            curr: curr, //当前页
            skip: true, //是否开启跳页
    	      skin: 'molv',//当前页颜色，可16进制
    	      groups: groups, //连续显示分页数
    	      first: '首页', //若不显示，设置false即可
    	      last: '尾页', //若不显示，设置false即可
    	      prev: '上一页', //若不显示，设置false即可
    	      next: '下一页', //若不显示，设置false即可
            jump: function(obj, first){ //触发分页后的回调
                if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
                	commentpage(obj.curr);
            		
                }
            }
        });
    	
    	if(res.pageTotal>0){
    	html="";
	    for(var i =0;i<res.pageRow.length;i++){
			var index = 1+i;
			var rows = res.pageRow[i];
			var datast ="";
			var compst="";
			var num="";
			if($(".laypage_curr").text()=="")
				{
				num=1;
				}
			else{
				num=$(".laypage_curr").text();
			}		
			var buttonval="";
			if(rows.dataState=='1'){
				datast="正常";
			}else{
				datast="禁用"
			}
			if(rows.complaintStatus=='1'){
				compst="正常";
			}else{
				compst="<span style='color:red'>申诉</span>";
			}
				if(rows.handlingStatus==0){
					buttonval="<button id='"+rows.id+"_"+rows.userId+"' style=\"width: 65px\" type='button' onclick=\"benSHOW('"+rows.id+"_"+rows.userId+"')\">禁用</button>";
				}else if(rows.handlingStatus==1){
					buttonval="<button id='"+rows.id+"_"+rows.userId+"' style=\"width: 65px\" onclick=\"findNote('"+rows.id+"_"+rows.userId+"');\" type='button'>待处理</button>";
				}else if(rows.handlingStatus==2){
					buttonval="<button id='"+rows.id+"_"+rows.userId+"' style=\"width: 65px\" onclick=\"findNotes('"+rows.id+"_"+rows.userId+"');\" type='button'>处理中</button>";
				}else if(rows.handlingStatus==3){
					buttonval="<button id='"+rows.id+"_"+rows.userId+"' style=\"width: 65px\" type='button' onclick=\"chechPerio('"+rows.id+"_"+rows.userId+"')\">已处理</button>";
				}else if(rows.handlingStatus==4){
					buttonval="<button id='"+rows.id+"_"+rows.userId+"' style=\"width: 65px\" onclick=\"opennote('"+rows.id+"_"+rows.userId+"')\" type='button'>解禁</button>";
				}
					
			html+=" <tr>"; 
					if(index<=9)
						{
						html+="<td>"+(num-1)+""+index+"</td>";
						}
					else if(index==10)
						{
						html+="<td>"+num+"0</td>";
						}					
			html+=" <td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;'>"+rows.perioName+"</td>"+
					"<td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;'>"+rows.hireCon+"</td>";
					if(rows.subTime==0){
						html+= "<td >其他</td>";
					}else{
						html+= "<td >"+rows.subTime+"个月</td>";
					}
					html+="<td>"+rows.auditMoney+"</td>"+
	                "<td>"+rows.layoutMoney+"</td>"+
	                "<td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;'>"+rows.commentContent+"</td>"+
	                "<td>"+rows.authorName+"</td>"+
	                "<td>"+datast+"</td>"+
	                "<td>"+compst+"</td>"+
					"<td>" +buttonval +
					"</td>"+
		            "</tr>";
	    
    }
	    $("#commentbody").html(html);
  }else
	  {
	  layer.msg("没有检索到信息！请重新检索！");
	  }
    });

};

function closenote(data)
{	
	var list=data.split("_");
	var userid=list[1];
	var id=list[0];
	if($("#"+data).text()=='禁用'){
		$.post("controltype.do",{userId : userid,id : id,dataState:'0',handlingStatus:'3'},function(data)
				{
				if(data=='ok')
					{
					 window.location.reload();
					}					
				});
	}
}

function benSHOW(data){
	  layer.open({
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
	      });
}


function opennote(data)
{
	
	var list=data.split("_");
	var userid=list[1];
	var id=list[0];
	if($("#"+data).text()=='解禁'){
		$.post("controltype.do",{userId : userid,id : id,dataState:'1',handlingStatus:'3'},function(data)
				{
				/*if(data=='ok')
					{
					 window.location.reload();
					}			*/		
				});
}
}

function findNote(data){
	var list=data.split("_");
	var userid=list[1];
	var id=list[0];
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['60%', '90%'],
	    title: '详细内容',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "findNote.do?id="+id,
	}); 
}


function findNotes(data){
	var list=data.split("_");
	var userid=list[1];
	var id=list[0];
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['60%', '90%'],
	    title: '详细内容',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "findNotes.do?id="+id,
	}); 
}

function chechPerio(id)
{
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['60%', '90%'],
	    title: '详细内容',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "findNotes.do?id="+id,
	}); 
}

//导出期刊
function exportPerio(){
	
	window.location.href="../periocomment/exportPerio.do?" +
			"userId="+username+
			"&perioName="+perioname+
			"&startTime="+startTime+
			"&endTime="+endTime+
			"&subTime="+subtime+
			"&sauditm="+sauditm+
			"&eauditm="+eauditm+
			"&slayoutm="+slayoutm+
			"&elayoutm="+elayoutm+
			"&dataStateArr="+dataState+
			"&complaintStatusArr="+complaintStatus;
	
}



