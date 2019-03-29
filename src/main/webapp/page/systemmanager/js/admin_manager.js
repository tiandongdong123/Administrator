var pageindex=1;
$(function(){
	adminpage();
});

//分页显示
function adminpage(curr){
	pageindex = curr || 1;
	var adminname = $("#inputEmail3").val();
    $.post('../admin/getadmin.do', {
        pagenum: curr || 1,//向服务端传的参数
        pagesize :20,
        adminname : adminname     
    }, function(res){
    	html="";
    	if(res.pageNum>1) {
    		var pageNumber = res.pageNum*10    		
    	} else {
    		var pageNumber = 0
    	}
    for(var i =0;res.pageRow[i];i++){
    	//id = 10*(curr-1)+i+1;
    	var status="";
    	var buttonname="";
    	if(res.pageRow[i].status==1){
    		status="<td>解冻</td>";
    		buttonname="冻结";
    	}else{
    		status="<td>冻结</td>";
    		buttonname="解冻";
    	}
    	html+=" <tr><td>"+(pageNumber+i+1)+"</td>" +
    			"<td>"+res.pageRow[i].wangfang_admin_id+"</td><td>"+res.pageRow[i].user_realname+"</td>" +
    			/*"<td>"+res.pageRow[i].dept.deptName+"</td>" +*/
    			"<td>"+res.pageRow[i].department+"</td>"+status+"<td><button type='button' class='btn btn-primary' onclick=\"updateadmin('"+res.pageRow[i].id+"')\">修改</button>" +
    			"<button type='button' class='btn btn-primary' onclick=\"changeadmin(this,'"+res.pageRow[i].id+"')\">"+buttonname+"</button></td></tr>";    	
    }
    
        $('#adminbody').html(html);
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
        if (res.totalRow/pageSize>1) {
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
        				adminpage(obj.curr);
        				
        			}
        		}
        	});        	
        } 
    });
};

function changeadmin(obj,id){
	var ids=new Array();
	var btnname ="do"+ $(obj).text();
    $("input[name=ids]").each(function() {  
        if ($(this).is(':checked')) {  
        	ids.push($(this).val());  
        }  
    }); 
	if(id!=null){
		ids.push(id);
	}
	var msg="";
	var title="";
	var a = btnname.indexOf("冻结");
	if(a>0){
		msg="确定要冻结机构账户吗？";
		title="冻结机构账号";
		url="../admin/closeadmin.do";
	}else{
		msg="确定要解冻机构账户吗？";
		title="解冻机构账号";
		url="../admin/openadmin.do";
	}
	
	layer.alert(msg,{
		icon: 1,
		title : [title, true],
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'],
	    yes: function(){
	    	$.ajax({  
	    		type : "POST",  
	    		url : url,
	    			data : {
	    				'ids' : ids
	    			},
	    			dataType : "json",
	    			success : function(data) {
	    				layer.msg("修改成功");
	    				adminpage($(".laypage_curr").text());
	    			}
	    		});
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

function addadmin(){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['50%', '70%'],
	    title: '添加管理员',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../admin/addadmin.do",
	}); 
}

function updateadmin(id){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['50%', '70%'],
	    title: '修改管理员',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../admin/updateadmin.do?id="+id+"&pagenum="+pageindex,
	}); 
}
function serachword(){
	var word=$("#inputEmail3").val();
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	
	if(word!=""&&word!=undefined&&!re.test(word)){
		$.post("../admin/serach.do",{word:word},function(data){
			
		});
	}
}