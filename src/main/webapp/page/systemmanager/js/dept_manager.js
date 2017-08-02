$(function(){
	deptpage(1);
});


//分页显示
function deptpage(curr){
    $.getJSON('../dept/getdept.do', {
        pagenum: curr,//向服务端传的参数
        pagesize :10
    }, function(res){
    	html="";
    for(var i =0;res.pageRow[i];i++){
    	id = 10*(curr-1)+i+1;
    	html+="<tr><td>"+id+"</td><td>"+res.pageRow[i].deptName+"</td><td>"+res.pageRow[i].deptDescribe+"</td><td><button type='button' class='btn btn-primary' onclick=\"updatedept("+res.pageRow[i].id+")\">修改</button> <button type='button' class='btn btn-primary' onclick=\"deletedept("+res.pageRow[i].id+")\">删除</button></td></tr>";   	
    }
        document.getElementById('deptbody').innerHTML = html;
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
                	deptpage(obj.curr);
            		
                }
            }
        });
    });
};

function adddept(){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['50%', '400px'],
	    title: '部门添加',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../dept/adddept.do",
	}); 
}

function updatedept(id){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['50%', '400px'],
	    title: '部门修改',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../dept/updatedept.do?id="+id,
	}); 
}

function deletedept(id){
	$.ajax( {  
		type : "POST",  
		url : "../dept/deletedept.do",
			data : {
				'id' : id	
			},
			dataType : "json",
			success : function(data) {
				if(data){
					layer.alert("删除成功");
					deptpage($(".laypage_curr").text());
				}else{
					layer.alert("该部门下有管理员不能删除");
				}
			}
		});
}
