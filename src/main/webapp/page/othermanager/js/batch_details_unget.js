$(function(){
	Page(1);
})
//------------------------------------分页--------------
function Page(curr){
	if($("#checkState").val()==1){
		return;
	}
	var batchId = "";
	var pageNum;
	var pageSize = 8;
	//序号
	var serial = ((curr||1) - 1) * pageSize + 1;
	batchId = $("#batchId").val().trim();//批次
	$.ajax({
		type : "post",  
		url : "../card/queryCardByBatchId.do",
		data :{ 
			"batchId" : batchId, 
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
		},
		dataType : "json",
		success : function(data){
			$("#list").empty();//清空
			if(data.pageRow[0] != null){
				$.each(data.pageRow,function (i) {
					var html ='<tr>'
	                  +'<td>'+(serial+i)+'</td>'
	                  +'<td>'+data.pageRow[i].cardNum+'</td>'
	                  +'<td>'+data.pageRow[i].password+'</td>'
	                  +'<td>'+data.pageRow[i].value+'</td>'
	                  +'</tr>';
					
					$("#list").append(html);
				});
			}else{
				$("#list").append("暂无数据");
			}
			layui.use(['laypage', 'layer'], function(){
				var laypage = layui.laypage,layer = layui.layer;
				laypage.render({
					elem: 'divPager',
					count: data.totalRow,
					first: '首页',
					last: '尾页',
					curr: curr || 1,
					page: Math.ceil(data.totalRow / pageSize),	//总页数
					limit: pageSize,
					layout: ['count', 'prev', 'page', 'next', 'skip'],
					jump: function (obj, first) {
			            if(!first){
			                Page(obj.curr);
			            }
					}
				});
			});
		},
		error : function(data){
			
		}
	})
}
