$(function(){
	unitpage(1);
});


//分页显示
function unitpage(curr){
    $.getJSON('../unit/getunit.do', {
        pagenum: curr,//向服务端传的参数
        pagesize :10
    }, function(res){
    	html="";
    for(var i =0;res.pageRow[i];i++){
    	id = 10*(curr-1)+i+1;
    	html+="<tr><td><input type='checkbox' name='ids' value="+res.pageRow[i].id+" ></td> <td>"+id+"</td><td>"+res.pageRow[i].unitName+"</td><td>"+res.pageRow[i].unitCode+"</td><td><button type='button' class='btn btn-primary' onclick=\"updateunit("+res.pageRow[i].id+",'"+res.pageRow[i].unitName+"','"+res.pageRow[i].unitCode+"')\">修改</button><button type='button' class='btn btn-primary' onclick=\"deleteunit("+res.pageRow[i].id+")\">删除</button></td></tr>";   	
    }
        document.getElementById('unitbody').innerHTML = html;
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
                	unitpage(obj.curr);
            		
                }
            }
        });
    });
};

function deleteunit(id){
	var ids=new Array();
    $("input[name=ids]").each(function() {  
        if ($(this).is(':checked')) {  
        	ids.push($(this).val());  
        }  
    }); 
	if(id!=null){
		ids.push(id);
	}
	$.ajax( {  
		type : "POST",  
		url : "../unit/deleteunit.do",
			data : {
				'ids' : ids	
			},
			dataType : "json",
			success : function(data) {
				layer.msg("删除成功");
				unitpage($(".laypage_curr").text());
			}
		});
}

function updateunit(id,name,code){
	$("#unitname").val(name);
	$("#unitcode").val(code);
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['40%', '200px'],
	    title: '修改资源单位',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#tabs_custom"),
	    btn: ['确认', '取消'],
		yes: function(){
			var unitname=$("#unitname").val();
			var unitcode=$("#unitcode").val();
			checkunitname(unitname,unitcode,id);
	    },
	}); 
}



function addunit(){
	$("#unitname").val("");
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['40%', '200px'],
	    title: '新增资源单位',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#unit_add"),
	    btn: ['确认', '取消'],
		yes: function(){
			var unitname=$("#addunitname").val();
			var unitcode=$("#addunitcode").val();
			checkunitname(unitname,unitcode);
			$("#addunitname").val("");
			$("#addunitcode").val("");
	    },
	}); 
}

function checkunitname(unitname,unitcode,id){
	$.ajax( {  
		type : "POST",  
		url : "../unit/checkunit.do",
			data : {
				'unitname' : unitname,
				'unitcode' : unitcode
			},
			dataType : "json",
			success : function(data) {
				if(data){
					$("#checkname").text("部门名称重复，请重新输入");
				}else{
					if(id!=null&&id!=''){
						doupdateunit(unitname,unitcode,id);
					}else{
						doaddunit(unitname,unitcode)
					}
				}
			}
		});
}

function doaddunit(unitname,unitcode){
	$("#checkname").text("");
	if(unitname!=null&&unitname!=''&&unitcode!=null&&unitcode!=''){
		$.ajax( {  
			type : "POST",  
			url : "../unit/doaddunit.do",
				data : {
					'unitname' : unitname,
					'unitcode' : unitcode
				},
				dataType : "json",
				success : function(data) {
					if(data){
						layer.msg("添加成功",{time:2000});
						layer.closeAll('page');
						unitpage($(".laypage_curr").text());
					}else{
						layer.msg("添加失败",{time:2000});
						layer.closeAll('page');
					}
				}
			});
	}else{
		$("#checkname").text("请输入单位名称");
	}

}
function doupdateunit(unitname,unitcode,id){
	$("#checkname").text("");
	if(unitname!=null&&unitname!=''){
		$.ajax( {  
			type : "POST",  
			url : "../unit/doupdateunit.do",
				data : {
					'unitname' : unitname,
					'unitcode' : unitcode,
					'id':id
				},
				dataType : "json",
				success : function(data) {
					if(data){
						layer.msg("修改成功",{time:2000});
						layer.closeAll('page');
						unitpage($(".laypage_curr").text());
					}else{
						layer.msg("修改失败",{time:2000});
						layer.closeAll('page');
					}
				}
			});
	}else{
		$("#checkname").text("请输入单位名称");
	}
	
}

function goback(id){
	window.history.go(-1);
}