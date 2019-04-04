$(function(){
	rolepage(1);
});


//分页显示
function rolepage(curr){
    $.getJSON('../role/getrole.do', {
        pagenum: curr,//向服务端传的参数
        pagesize :20
    }, function(res){
    	html="";
    	var purview=$.cookie('purview');
    for(var i =0;res.pageRow[i];i++){
    	id = 20*(curr-1)+i+1;
    	html+="<tr><td>"+id+"</td>" +
    			  "<td style=\"word-wrap:break-word;\" width=\"160px;\">"+res.pageRow[i].roleName+"</td>" +
    			  "<td style=\"word-wrap:break-word;\" width=\"160px;\">"+res.pageRow[i].description+"</td>" +
//    			  "<td style=\"word-wrap:break-word;\" width=\"160px;\">"+res.pageRow[i].purview+"</td>";
    			  "<td style=\"word-wrap:break-word;\" width=\"160px;\"><ul id=\"treeDemo"+i+"\" class=\"ztree\"></ul></td>";
    	if(purview.indexOf("F122")!=-1){
    		html+= "<td><span class=\"buttonSpan\" onclick=\"updaterole('"+res.pageRow[i].roleId+"')\">修改</span>";
    	}else{
    		html+= "<td><span style='display:none' class=\"buttonSpan\" onclick=\"updaterole('"+res.pageRow[i].roleId+"')\">修改</span>";
    	}  
    	if(purview.indexOf("F123")!=-1){
    		html+= "<span class=\"buttonSpan\" onclick=\"deleterole('"+res.pageRow[i].roleId+"')\">删除</span></td>" +
			  "</tr>"; 
    	}else{
    		html+= "<span style='display:none' class=\"buttonSpan\" onclick=\"deleterole('"+res.pageRow[i].roleId+"')\">删除</span></td>" +
			  "</tr>";
    	}	   	
    }
        document.getElementById('rolebody').innerHTML = html;
        for(var i =0;res.pageRow[i];i++){
        	purviewtree(res.pageRow[i].purviewTree,i)        	
        }
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
        if (totalRow/pageSize>1) {
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
        }
    });
};

// 树形结构
function purviewtree(json,i){
	var curMenu = null, zTree_Menu = null;
	var setting = {
		
	};

	var zNodes =json;
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"+i), setting, zNodes);
		zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo"+i);
		//zTree_Menu.expandNode(zTree_Menu.getNodes()[0],true);
	});
}

function addrole(){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['50%', '70%'],
	    title: '添加角色',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../admin/roleadd.do",
	}); 
//	window.location.href="../admin/roleadd.do";
}

function updaterole(id){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['50%', '70%'],
	    title: '修改角色',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../admin/rolemodify.do?id="+id,
	}); 
//	window.location.href="../admin/rolemodify.do?id="+id;
}

function deleterole(id){
	var msg="确定要删除该角色吗?";
	var title="删除";
	layer.alert(msg,{
		icon: 1,
		title : [title, true],
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'],
	    yes: function(){
	    	$.ajax({  
	    		type : "POST",  
	    		url : "../role/deleterole.do",
	    			data : {
	    				'id' : id,
	    			},
	    			dataType : "json",
	    			success : function(data) {
	    				if(data){
	    					layer.msg("删除成功");
	    					rolepage($(".laypage_curr").text()||1);
	    				}else{
	    					layer.msg("该角色下有管理员不能删除");
	    				}
	    			}
	    		});
	    }
	});
}
