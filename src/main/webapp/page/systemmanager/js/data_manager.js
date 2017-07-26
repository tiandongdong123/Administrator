var dataname;
$(function(){
	datapage(1);
});


//分页显示
function datapage(curr){
	dataname=$("#dataname").val();
    $.post('../data/getdata.do', {
        pagenum: curr,//向服务端传的参数
        pagesize :10,
        dataname:dataname
    }, function(res){
    	var html="";
    	var status="";
	    for(var i =0;res.pageRow[i];i++){
	    	if(res.pageRow[i].status==1){
	    		status="<td>已发布</td>";
	    		buttonname="下撤";
	    	}else if(res.pageRow[i].status==null){
	    		status="<td>未发布</td>";
	    		buttonname="发布";
	    	}else{
	    		status="<td>下撤</td>";
	    		buttonname="再发布";
	    	}
	    	id = 10*(curr-1)+i+1;
	    	var describe = res.pageRow[i].tableDescribe;
	    	if(describe!=null){
	    		describe = describe.substring(0,35);
	    	}else{
	    		describe = "";
	    	}
	    	html+="<tr>" +
    			"<th>"+id+"</th>" +
    			"<td>"+res.pageRow[i].tableName+"</td>" +
    			"<td  style=\"word-wrap:break-word;\">"+describe+"...</td>" +
    			"<td>"+res.pageRow[i].dbtype+"</td>" +
    			"<td  style=\"word-wrap:break-word;\">"+res.pageRow[i].sourceDb+"</td>" +
    			"<td>"+res.pageRow[i].resType+"</td>"+
    			"<td>"+res.pageRow[i].language+"</td>"+
    			"<td>"+(res.pageRow[i].customPolicy==null?"":res.pageRow[i].customPolicy)+"</td>"+
    			status +
    			"<td><button type='button' class='btn btn-primary' onclick=\"doupdatedata('"+res.pageRow[i].id+"')\">修改</button>" +
    			"<button type='button' class='btn btn-primary' onclick=\"changedata(this,'"+res.pageRow[i].id+"')\">"+buttonname+"</button>";   	
	    }
        document.getElementById('databody').innerHTML = html;
        var totalRow = res.pageTotal;
        var pageSize = res.pageSize;
        var pages;
        var groups;
        if(totalRow%pageSize==0){	
        	 pages = totalRow/pageSize; 
        }else{
        	pages = totalRow/pageSize+1;
        }
        if(pages>=4){
        	groups=4;
        }else{
        	groups=pages;
        }
        //显示分页
        laypage({
        	cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
            pages: pages, //通过后台拿到的总页数
            curr: curr, //当前页
    	      skin: 'molv',//当前页颜色，可16进制
    	      groups: groups, //连续显示分页数
    	      first: '首页', //若不显示，设置false即可
    	      last: '尾页', //若不显示，设置false即可
    	      prev: '上一页', //若不显示，设置false即可
    	      next: '下一页', //若不显示，设置false即可
            jump: function(obj, first){ //触发分页后的回调
                if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
                	datapage(obj.curr);
                }
            }
        });
    });
};

function changedata(obj,id){
	var btnname ="do"+ $(obj).text();
	var a = btnname.indexOf("下撤");
	if(a>0){
		url="../data/closedata.do";
	}else{
		url="../data/opendata.do"
	}
	$.ajax({  
		type : "POST",  
		url : url,
		data : {
			'id' : id	
		},
		dataType : "json",
		success : function(data) {
			layer.msg("修改成功");
			datapage($(".laypage_curr").text());
		}
	});
}

function PushData(){
	$.post("../data/pushdata.do",function(){});
}

function adddata(){
	window.location.href="../data/adddata.do";
}

function doupdatedata(id){
	window.location.href="../data/updatedata.do?id="+id;
}

function exportData(){
	window.location.href="../data/exportData.do?dataname="+dataname;
}
