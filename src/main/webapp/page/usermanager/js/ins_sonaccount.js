$(function(){
	if($(".parameter").val()){
		$("#userId").text($(".parameter").val());
		findList();
	}
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
	$("#fromList").submit();
}

//导出
function exportexecl(){
	window.location.href="../content/exportMessage.do?branch="+branch+"&colums="+clum+"&human="+human+"&startTime="+startTime+"&endTime="+endTime;
}
//跳转到机构查询页
function goInformation(pid){
	window.location.href='../auser/information.do?userId='+pid;
}
