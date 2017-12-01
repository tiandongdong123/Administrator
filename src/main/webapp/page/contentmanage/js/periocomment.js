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
    	var totalRow = res.pageTotal;
        var pageSize = res.pageSize;
        var pages;
        var groups;
        if(totalRow%pageSize==0) {	
        	 pages = totalRow/pageSize; 
        }else {
        	pages = totalRow/pageSize+1;
        }
        if(pages>=4) {
        groups=4;
        }else {
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
					if(index<=9){
						html+="<td>"+(num-1)+""+index+"</td>";
					}else if(index==10){
						html+="<td>"+num+"0</td>";
					}					
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
						html+="<td><a href='javascript:void(0);' onclick='findNote("+rows.id+");'>详情</a></td>"+
			            "</tr>";
					}else{
						html+="<td><a href='javascript:void(0);' onclick='findNote("+rows.id+");'>已处理</a></td>"+
			            "</tr>";
					}
					
	    
    }
	    $("#commentbody").html(html);
  }else {
	  layer.msg("没有检索到信息！请重新检索！");
	  }
    });

};


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
