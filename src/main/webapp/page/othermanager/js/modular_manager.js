var ids;
var pageIndex;
$(function(){
	page(1);
	getModularType();
});


//分页显示
function page(curr){
	ids=new Array();
	pageIndex=curr;
    $("input[name=items]").each(function() {  
        if ($(this).is(':checked')) {  
            ids.push($(this).val());  
        }  
    }); 
//	var pageName=$("#pageName").val();
    $.getJSON('../modular/getModular.do', {
        ids:ids,
        pageNum: curr,//向服务端传的参数
        pageSize :10
    }, function(res){
    	html="";
    for(var i =0;res.pageRow[i];i++){
    	id = 10*(curr-1)+i+1;
    	html+="<tr><td>"+id+"</td><td>"+res.pageRow[i].modularName+"</td><td>"+res.pageRow[i].linkAddress+"</td><td><button type='button' class='btn btn-primary' onclick=\"updateModularManager('"+res.pageRow[i].id+"')\">修改</button><button type='button' class='btn btn-primary' onclick=\"deleteModularManager('"+res.pageRow[i].id+"')\">删除</button></td></tr>";   	
    }
        document.getElementById('modularbody').innerHTML = html;
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
                	page(obj.curr);
            		
                }
            }
        });
    });
};

function getModularType(){
	
	$.ajax( {  
		type : "POST",  
		url : "../page/getModularType.do",
			dataType : "json",
			success : function(data) {
				var rt="<input type='checkbox'  onclick='checkallbox();' id='all' name='items' value=\"all\">全部&nbsp;&nbsp;&nbsp;";
				for(var k = 0 ;k<data.length;k++){
					rt+="<input type='checkbox'  name='items' id='item' value="+data[k].id+" onclcik=\"checkOne();\">"+data[k].modularName+"&nbsp;&nbsp;&nbsp;";
				}	
				document.getElementById('checkboxs').innerHTML = rt;
			}	
		});	
}

function addModularManager(){
	window.location.href="../modular/addModular.do";
}

function updateModularManager(id){
	window.location.href="../modular/updateModular.do?id="+id;
}

function deleteModularManager(id){
	$.ajax( {  
		type : "POST",  
		url : "../modular/deleteModular.do",
			data : {
				'id' : id	
			},
			dataType : "json",
			success : function(data) {
				layer.msg("删除成功");
				refurbish();
			}
		});
}

function checkallbox() {
	if ($("#all").is(':checked')) {
		$("input[type=checkbox]").prop("checked", true);
	} else {
		$("input[type=checkbox]").prop("checked", false);
	}
}

function checkOne(){
	if($("input[id='item']").length==$("input[id='item']:checked").length){
		$("#all").prop("checked", "checked");
	}else{
		$("#all").removeAttr("checked");
	}
}

function datapage(data){
	page(data);
}

//导出
function exportmodular(){
	window.location.href="../modular/exportmodular.do?ids="+ids;
}

function refurbish(){
	$("input[type=checkbox]").prop("checked", false);
	page(1);
}

