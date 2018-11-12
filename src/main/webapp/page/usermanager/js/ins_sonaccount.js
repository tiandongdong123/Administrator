$(function(){
	if($(".parameter").val()){
		$("#userId").text($(".parameter").val());
		findList();
	}
	$("#queryTable").bind("keydown",function(e){
        // 兼容FF和IE和Opera    
	    var theEvent = e || window.event;    
	    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;    
	    if (code == 13) {    
	    	//回车执行查询
	    	$("#queryButton").click();
		}
	});
	goPage();
});
//分页
function goPage(curr){
	var totalRow = $("#totalRow").val();
	var pageSize = $("#pageSize").val();
	var pageNum = $("#pageNum").val();
	if(totalRow!=""&&pageSize!=""){
		laypage({
	        cont: $("#page"),
	        pages: Math.ceil(totalRow/pageSize),
	        curr: pageNum || 1,
	        skip: true, //是否开启跳页
	        last: '尾页',
	        first: '首页',
	        groups: 5,
	        skin: 'molv',
	        jump: function (obj,first){
				if(!first){
					$("#pageNum").val(obj.curr);
					$("#goPage").val("1");
					$("#fromList").submit();
				}
	        }
	    });
	}else{
		$("#page").html("");
	}
}

//数据提交
function findList(){
	$("#goPage").val("");
	$("#fromList").submit();
}

//导出
function exportexecl(){
	var institution=$("#institution").val();
	var start_time=$("#start_time").val();
	var end_time=$("#end_time").val();
	var pid=$("#pid").val();
	var userId=$("#userId").val();
	window.location.href="../auser/exportSonAccount.do?userId="+userId+"&institution="+institution+"&start_time="+start_time+"&end_time="+end_time+"&pid="+pid+"&t="+new Date();
}
//跳转到机构查询页
function goInformation(pid){
	window.location.href='../group/list.do?userId='+pid;
}

function showTd(id,obj){
	var type=$(obj).attr("type");
	if(type=="0"){
		$("."+id+":gt(4)").show();
		$(obj).attr("type","1");
		$(obj).attr("src","../img/up.png");
	}else{
		$("."+id+":gt(4)").hide();
		$(obj).attr("type","0");
		$(obj).attr("src","../img/down.png");
	}
}
