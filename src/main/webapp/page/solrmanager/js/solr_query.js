function query(){
	Page(1);
}
var pageNum;
var pageSize = 10;
var curr=1;
function Page(curr){
	//序号
	var startTime = $("#startTime").val();//开始时间
	var endTime = $("#endTime").val();//结束时间
	$.ajax({
		type : "post",  
		url : "../solr/solrList.do",
		data :{
			"startTime" : startTime,
			"endTime" : endTime,
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
		},
		dataType : "json",
		success : function(data){
			$("#list").empty();//清空
			var x=11+10*(curr-2);
			if(data.pageRow[0] != null){
				$.each(data.pageRow,function (i) {
					var html ='<tr>'
					  +'<td>'+x+'</td>'
	                  +'<td>'+data.pageRow[i].id+'</td>'
	                  +'<td>'+data.pageRow[i].idType+'</td>'
	                  +'<td>'+data.pageRow[i].modelName+'</td>'
	                  +'<td>'+data.pageRow[i].userIp+'</td>'
	                  +'<td>'+data.pageRow[i].userId+'</td>'
	                  +'<td>'+data.pageRow[i].createTime+'</td>'
	                  +'<td><a href="javascript:void(0)" onclick="deleteArt(\''+data.pageRow[i].id+'\')">删除</a></td>'
	                  +'</tr>';
					$("#list").append(html);
					x++;
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
		error : function(data){}
	})
}

//----------------------------删除-------------------------------
function deleteArt(id){
	layer.alert('确定删除通过？', {
		title: '黑名单管理',
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../solr/deleteArticleById.do",
	    		data :{ "id" : id},
	    		dataType : "json",
	    		success : function(data){
	    			if(data){
	    				Page(1);
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	        layer.closeAll();
	    }
	  });
}
//----------------------------批量删除-------------------------------
function deleteAll(){
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	if(startTime=='' ||endTime==''){
		layer.alert("请添加要删除的时间段!");
		return;
	}
	layer.alert('确定要删除'+startTime+'到'+endTime+'的所有下撤数据吗？', {
		title: '黑名单管理',
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../solr/deleteArticleList.do",
	    		data :{ "startTime" : startTime,"endTime":endTime},
	    		dataType : "json",
	    		success : function(data){
	    			if(data>0){
	    				Page(1);
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	        layer.closeAll();
	    }
	  });
}