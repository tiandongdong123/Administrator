$(function(){
	goPage();
});

//分页
function goPage(){
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
	$("#pageNum").val(1);
	$("#pageSize").val(10);
	$("#totalRow").val("");
	$("#fromList").submit();
}

function showPage(data){
	//显示分页
	var pageNum = $("#pageNum").val();
	var pageTotal;
	if(pageNum>0){
		if(pageNum%5==0){
			pageTotal=pageNum/5;
		}else{
			pageTotal=pageNum/5+1;
		}
	}
	laypage({
    	cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
        pages: pageTotal, //通过后台拿到的总页数
        curr: data, //当前页
        skip: true, //是否开启跳页
	      skin: '#367fa9',//当前页颜色，可16进制
	      groups: 4, //连续显示分页数
	      first: '首页', //若不显示，设置false即可
	      last: '尾页', //若不显示，设置false即可
	      prev: '上一页', //若不显示，设置false即可
	      next: '下一页', //若不显示，设置false即可
        jump: function(obj, first){ //触发分页后的回调
        		selcetOP(obj.curr);
       }
	});
}

//收起/展开切换
function unfolded(flag,obj,index){
	if(flag==2){
		$(obj).removeClass('icon_top').addClass('icon_tdown');
		$(obj).attr("onclick","unfolded(1,this,'"+index+"');")
		$("#"+index).hide();
	}else{
		$(obj).removeClass('icon_tdown').addClass('icon_top');
		$(obj).attr("onclick","unfolded(2,this,'"+index+"');")
		$("#"+index).show();
	}
}