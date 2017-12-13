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
	paging();
});
$(function(){
	$(".layui-layer-close1").on('click',function(){
		 window.location.reload();
	});
});
(function () {
    var getPager = function (url, $container) {
        // $.get(url, function (html) {
        //     $container.replaceWith(html);
        // });
        $container.find('.loadingPic').show();
        $.ajax({
            url:url,
            success:function (html) {
                $container.replaceWith(html);
                $container.find('.loadingPic').hide();
            },
            error:function () {
                $container.find('.loadingPic').hide();
            }
        });
    };
    //page-a异步跳转
    $(document).on('click', '.sync-html .page a', function () {
        var $this = $(this);
        // $this.closest('.sync-html').empty('');
        var href = $this.attr('href');
        $this.removeAttr('href');
        $this.closest('.sync-html').find('.loadingPic').show();
        // $.get(href, function (html) {
        //     $this.closest('.sync-html').html(html);
        //     $this.closest('.sync-html').find('.loadingPic').hide();
        // })
        $.ajax({
            url:href,
            success:function (html) {
                $this.closest('.sync-html').replaceWith(html);
                $this.closest('.sync-html').find('.loadingPic').hide();
            },
            error:function () {
                $this.closest('.sync-html').find('.loadingPic').hide();
            }
        });
    });
    //page-form异步跳转
    $(document).on('submit', '.sync-html .page form', function () {
        var $this = $(this);
        var action = $this.attr('action');
        var inputPage = parseInt($this.find('.laypage_skip').last().val());
        var allPage = $this.attr('data-all');
        if (inputPage > 0 && inputPage <= allPage) {
            var href = action + inputPage;
            getPager(href, $this.closest('.sync-html'));
        } else {
            alert('请输入正确页码');
        }
        return false;
    });
    //page-form同步跳转
    $(document).on('submit', '.no-sync .page form', function () {
        var $this = $(this);
        var action = $this.attr('action');
        var inputPage = parseInt($this.find('.laypage_skip').last().val());
        var allPage = $this.attr('data-all');
        if (inputPage > 0 && inputPage <= allPage) {
            window.location.href = action + encodeURIComponent(inputPage);
        } else {
            alert('请输入正确页码');
        }
        return false;
    });
})();




function findNote(data){
	var id=data;
	layer.open({
		  type: 2,//page层 1div，2页面
		  area: ['60%', '90%'],
		  fixed: false, //不固定
		  title: '期刊点评详情',
		  maxmin: true,
		  moveType: 1, //拖拽风格，0是默认，1是传统拖动
		  content: "findNote.do?id="+id,
		});
}



//导出期刊
function exportPerio(){
	
	window.location.href="../periocomment/exportPerio.do?" +
			"authorName="+username+
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


function chekbox(data){
	var tal= $(data).val();
	if(tal=="all"){
		if ($("#check1").is(':checked')) {  
			$("input[name='complaintStatus']").prop("checked",true);
	   }else{
		   $("input[name='complaintStatus']").prop("checked",false);
	   }
	}
	var i=0;
	$("input[name='complaintStatus']").each(function() {
		if ($(this).is(':checked')) {
			i++;
		}
	});	
	if(i==3){
		$("input[name='complaintStatus1']").prop("checked",true);
	}else{
		 $("input[name='complaintStatus1']").prop("checked",false);
	}
}

function paging(){
	
	var pagenum=$("#pagenum").val();
	var pagesize=$("#pagesize").val();
	
    $("#commentbody tr").remove();
	 username = $("#username").val();
	 perioname = $("#perioname").val();
	 startTime = $("#startTime").val();
	 endTime = $("#endTime").val();
	 subtime=$("#subtime").find("option:selected").text();
	if(subtime=='请选择'){
		subtime='';
	}else if(subtime=='1年内'){
		subtime=12;
	}else if(subtime=='2年内'){
		subtime=24;
	}else if(subtime=='大于2年'){
		subtime=25;
	}else{
		subtime=subtime.split("个月内")[0];
	}
	
	if(startTime!=null&&endTime!=null){
		 var d1 = new Date(startTime.replace(/\-/g, "\/"));  
		 var d2 = new Date(endTime.replace(/\-/g, "\/")); 
		 if(d1 >d2){
			 layer.msg("请查看，起始日期和结束日期时间是否符合标准！");
			 return false;
		 }
	}
	sauditm = $("#sauditm").val();
	eauditm = $("#eauditm").val();
	slayoutm = $("#slayoutm").val();
	elayoutm = $("#elayoutm").val();
	
	dataState=new Array();
	complaintStatus=new Array();
	
/*	$("input:checkbox[name='dataState']:checked").each(function(){
		dataState.push($(this).val());
	});*/
	
	var is_dataState=$("#dataState option:selected").val();
	if(is_dataState=="all"){
		var selectlist=$("select [name='select_data']");
		for(var i=0;i<selectlist.length;i++){
			dataState.push(selectlist.eq(i).val());
		}
	}else{
		dataState.push(is_dataState);
	}
	var is_Status=$("#complaintStatus option:selected").val();
	if(is_Status=="all"){
		var selectlist=$("select [name='select_Status']");
		for(var i=0;i<selectlist.length;i++){
			complaintStatus.push(selectlist.eq(i).val());
		}
	}else{
		complaintStatus.push(is_Status);
	}
	
   $.post('../periocomment/getcomment.do', {
       pagenum: pagenum,//向服务端传的参数
       pagesize :pagesize,
       user_id:username,
       perio_name : perioname,
       startTime :startTime,
       endTime:endTime,
       submit_period:subtime,
       sauditm:sauditm,
       eauditm:eauditm,
       slayoutm:slayoutm,
       elayoutm:elayoutm,
       dataStateArr:dataState,
       complaintStatusArr:complaintStatus
   }, function(res){
   	   var totalRow = res.totalRow;
       var pageSize = res.pageSize;
       var pageTotal=res.pageTotal;
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
   	if(res.pageTotal>0){
   	html="";
	    for(var i =0;i<res.pageRow.length;i++){
			var rows = res.pageRow[i];
			var datast ="";
			var compst="";		
			var buttonval="";
			if(rows.data_state=='1'){
				datast="正常";
			}else{
				datast="<span style='color:red;'>禁用</span>"
			}
			if(rows.executive_operation=='1'){
				compst="新增";
			}else if(rows.executive_operation=='2'){
				compst="修改";
			}else if(rows.executive_operation=='3'){
				compst="删除";
			}
					
			html+=" <tr>"; 
			
			html+="<td>"+((1+i+(pagenum-1)*pagesize))+"</td>";
			html+="<td>"+rows.id+"</td>"+
					"<td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;' >"+rows.perio_name+"</td>"+
			 		"<td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;'>"+rows.publishing_discipline+"</td>"+
					"<td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;'>"+rows.hire_con+"</td>";
					if(rows.submit_period==12){
						html+= "<td >1年</td>";
					}else if(rows.submit_period==24){
						html+= "<td >2年</td>";
					}else if(rows.submit_period==null){
						html+="<td ></td>";
					}else if(rows.submit_period==25){
						html+= "<td >大于2年</td>";
					}else{
						html+= "<td >"+rows.submit_period+"个月</td>";
					}
					html+="<td style='text-align: center' >"+(rows.audit_money==null?"":rows.audit_money)+"</td>"+
	                "<td style='text-align: center'  >"+(rows.layout_money==null?"":rows.layout_money)+"</td>"+
	                "<td style='overflow:hidden;white-space:nowrap;text-overflow:ellipsis;'>"+rows.comment_content+"</td>"+
	                "<td>"+rows.user_id+"</td>"+
	                "<td>"+rows.creat_date+"</td>"+
	                "<td>"+rows.goods+"</td>"+
	                "<td>"+datast+"</td>"+
	                "<td>"+compst+"</td>"+
	                "<td>"+(rows.auditor==null?"":rows.auditor)+"</td>"+
	                "<td>"+(rows.audit_time==null?"":rows.audit_time)+"</td>";
					if(rows.auditor==null){
						html+="<td><a href='javascript:void(0);' onclick='findNote("+rows.rand_id+");'>详情</a></td>"+
			            "</tr>";
					}else{
						html+="<td><a href='javascript:void(0);' onclick='findNote("+rows.rand_id+");'>已处理</a></td>"+
			            "</tr>";
					}
					
	    
   }
	    $("#commentbody").html(html);
	    $("#pagenum").find("option[value='"+pagenum+"']").attr("selected","selected");	    
 }else {
	  layer.msg("没有检索到信息！请重新检索！");
	  }
   });
}

function firstPage(){
	var pagenum=Number($("#pagenum").val());
	if(pagenum==1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum").val("1");
		paging();
	}
}


function upPage(){
	var pagenum=Number($("#pagenum").val())-1;
	if(pagenum<1){
		layer.msg("已经是第一页了");
	}else{
		$("#pagenum").val(pagenum);
		paging();
	}

}

function lastPage(){
	var pagenum=$("#pagenum").val();
	var total=$("#totalpage").text();
	if(pagenum==$("#pageTotal").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum").val(total);
		paging();
	}
}

function downPage(){
	var pagenum=Number($("#pagenum").val())+1;
	if(pagenum>$("#pageTotal").val()){
		layer.msg("已经是最后一页了");
	}else{
		$("#pagenum").val(pagenum);
		paging();
	}
}


function getAllpageNum(){
	$("#pagenum").val("1");
	paging();
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
			paging();
		}
	}
}