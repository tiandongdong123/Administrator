$(function(){
	rolepage(1);
});


//分页显示
function rolepage(curr){
    $.getJSON('../role/getrole.do', {
        pagenum: curr,//向服务端传的参数
        pagesize :10
    }, function(res){
    	html="";
    for(var i =0;res.pageRow[i];i++){
    	id = 10*(curr-1)+i+1;
    	html+="<tr><td>"+id+"</td><td>"+res.pageRow[i].deptName+"</td><td>"+res.pageRow[i].roleName+"</td><td>"+res.pageRow[i].description+"</td><td style=\"word-wrap:break-word;\" width=\"160px;\">"+res.pageRow[i].purview+"</td><td><button type='button' class='btn btn-primary' onclick=\"updaterole('"+res.pageRow[i].roleId+"')\">修改</button>" +
    			"<button type='button' class='btn btn-primary' onclick=\"deleterole('"+res.pageRow[i].roleId+"')\">删除</button></td>" +
    					"</tr>";   	
    }
        document.getElementById('rolebody').innerHTML = html;
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
                	rolepage(obj.curr);
            		
                }
            }
        });
    });
};

function addrole(){
	window.location.href="../role/addrole.do";
}

function updaterole(id){
	window.location.href="../role/updaterole.do?id="+id;
}

function deleterole(id){
	$.ajax( {  
		type : "POST",  
		url : "../role/deleterole.do",
			data : {
				'id' : id,
			},
			dataType : "json",
			success : function(data) {
				if(data){
					layer.msg("删除成功");
					rolepage($(".laypage_curr").text());
				}else{
					layer.msg("该角色下有管理员不能删除");
				}
			}
		});
}