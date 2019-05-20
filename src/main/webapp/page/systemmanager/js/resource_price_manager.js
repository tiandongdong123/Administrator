
$(function(){
	pricepage(1);
	//getRtype();
});

//分页显示
function pricepage(curr){
	var name=$("#inputEmail3").val();
	var purview="";
	$.ajax({
		type : "get",
		cache: false,
		async: false,
		url : "../user/getadminpurview.do",
		success : function (data){
			purview=data.purview;
		}
	});
    $.getJSON('../resourceprice/getprice.do', {
        pagenum: curr,//向服务端传的参数
        pagesize :10,
        Rname : name
       
    }, function(res){
    	html="";
    for(var i =0;res.pageRow[i];i++){
    	id = 10*(curr-1)+i+1;
    	var usertype="";
    	if(res.pageRow[i].applyUserType==0){
    		usertype="个人用户";
    	}else if(res.pageRow[i].applyUserType==1){
    		usertype="机构用户";
    	}else if(res.pageRow[i].applyUserType==2){
    		usertype="所有用户类型";
    	}else if(res.pageRow[i].applyUserType==""||res.pageRow[i].applyUserType==null||res.pageRow[i].applyUserType==undefined){
    		usertype="暂无";
    	}
    	html+="<tr><td><input type='checkbox' name='ids' value='"+res.pageRow[i].rid+"'></td>" +
    			"<td>"+res.pageRow[i].rid.substring(0,15)+"..."+"</td>" +
    			"<td>"+res.pageRow[i].name+"</td>" +
    			"<td>"+res.pageRow[i].sourceCode+"</td>" +
    			"<td>"+res.pageRow[i].resourceTypeCode+"</td>" +
    			"<td>"+res.pageRow[i].unitCode+"</td>" +
    			"<td>"+res.pageRow[i].price+"元</td>" +
    			"<td>"+res.pageRow[i].sonCode+"</td>" +
    			"<td>"+usertype+"</td>";
    	if(purview.indexOf("F222")!=-1){
    	html+="<td><button type='button' class='btn btn-primary' onclick=\"updateprice('"+res.pageRow[i].rid+"')\">修改</button>";    	
    	}else{
    	html+="<td><button style='display:none' type='button' class='btn btn-primary' onclick=\"updateprice('"+res.pageRow[i].rid+"')\">修改</button>"; 
    	}
    	if(purview.indexOf("F223")!=-1){
        html+="<button type='button' class='btn btn-primary' onclick=\"deletePrice('"+res.pageRow[i].rid+"')\">删除</button></td></tr>";    	
        }else{
        html+="<button style='display:none' type='button' class='btn btn-primary' onclick=\"deletePrice('"+res.pageRow[i].rid+"')\">删除</button></td></tr>"; 
        }
    	
    }
        document.getElementById('ttab').innerHTML = html;
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
                	pricepage(obj.curr);
            		
                }
            }
        });
    });
};

function checkall(obj){
	if ($("#checkboxall").is(':checked')) {  
		 $("input[name=items]").prop("checked",true);
    }else{
    	 $("input[name=items]").prop("checked",false);
    }
}

function deletePrice(id){
	var ids=new Array();
    $("input[name=ids]").each(function() {  
        if ($(this).is(':checked')) {  
        	ids.push($(this).val());  
        }  
    }); 
	if(id!=null){
		ids.push(id);
	}
	if(ids.length>0)
		{
		$.ajax( {  
			type : "POST",  
			url : "../resourceprice/deleteprice.do",
				data : {
					'ids' : ids	
				},
				dataType : "json",
				success : function(data) {
					layer.msg("删除成功");
					pricepage($(".laypage_curr").text());
				}
			});
		}else{
			layer.msg("请选择删除的信息！");
		}
}

function getRtype(){
	$.ajax( {  
		type : "POST",  
		url : "../resourceprice/getRtype.do",
			dataType : "json",
			success : function(data) {
				var rt="";
				for(var k = 0 ;k<data.length;k++){
					rt+="<input type='checkbox'  name='items' value="+data[k].typeName+">"+data[k].typeName+"&nbsp;&nbsp;&nbsp;";
				}
				/*document.getElementById('checkboxs').innerHTML = rt;*/
			}
		});
}

function checkedids(){
	if ($("#num").is(':checked')) {  
		 $("input[name=ids]").prop("checked",true);
   }else{
   	 $("input[name=ids]").prop("checked",false);
   }
}

function addprice(){
	window.location.href="../admin/priceadd.do";
}

function updateprice(id){
	window.location.href="../admin/pricemodify.do?rid="+id;
}