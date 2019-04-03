$(function(){
	productpage(1);
	source_data();
});


function productpage(curr){
	var purview=$.cookie('purview');
	 $.getJSON('../product/getproduct.do', {
	        pagenum: curr,//向服务端传的参数
	        pagesize :10
	    }, function(res){
	    	html="";
	    for(var i =0;res.pageRow[i];i++){
	    	id = 10*(curr-1)+i+1;
	    	html+="<tr><td><input type='checkbox' name='ids' value="+res.pageRow[i].resourceTypeCode+" ></td> <td>"+id+"</td><td>"+res.pageRow[i].resourceTypeName
	    	+"</td><td>"+res.pageRow[i].resourceTypeCode+"</td><td>"+res.pageRow[i].productResourceCode;
	    	if(purview.indexOf("F232")!=-1){
	    	html+="</td><td><button type='button' class='btn btn-primary' onclick=\"updateproduct('"+res.pageRow[i].resourceTypeName+"','"+res.pageRow[i].resourceTypeCode+"','"
	    	+res.pageRow[i].productResourceCode+"')\">修改</button>";
	    	}else{
	    		html+="</td><td><button style='display:none' type='button' class='btn btn-primary' onclick=\"updateproduct('"+res.pageRow[i].resourceTypeName+"','"+res.pageRow[i].resourceTypeCode+"','"
		    	+res.pageRow[i].productResourceCode+"')\">修改</button>";
	    	}
	    	if(purview.indexOf("F233")!=-1){
	    		html+="<button type='button' class='btn btn-primary' onclick=\"deleteproduct('"+res.pageRow[i].resourceTypeCode+"')\">删除</button></td></tr>";   	
	    	}else{
	    		html+="<button style='display:none' type='button' class='btn btn-primary' onclick=\"deleteproduct('"+res.pageRow[i].resourceTypeCode+"')\">删除</button></td></tr>";   	
	    	}
	    }
	    document.getElementById('ttab').innerHTML = html;
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
            skip: true, //是否开启跳页
    	      skin: 'molv',//当前页颜色，可16进制
    	      groups: groups, //连续显示分页数
    	      first: '首页', //若不显示，设置false即可
    	      last: '尾页', //若不显示，设置false即可
    	      prev: '上一页', //若不显示，设置false即可
    	      next: '下一页', //若不显示，设置false即可
            jump: function(obj, first){ //触发分页后的回调
                if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
                	productpage(obj.curr);
                }
            }
        });
});
}

function source_data(){
	$.post("../product/getSource.do",function(data){
		for(var i=0;i<data.length;i++){
			var html="	<input  name=\"source_code\" value="+data[i].productSourceCode+"  type=\"radio\">"+data[i].tableName+"&nbsp;&nbsp;&nbsp;";
			$("#source").append(html);
		}
	});
}

function deleteproduct(id){
	var ids=new Array();
    $("input[name=ids]").each(function() {  
        if ($(this).is(':checked')) {  
        	ids.push($(this).val());  
        }  
    }); 
	if(id!=null){
		ids.push(id);
	}
	$.ajax({  
		type : "POST",  
		url : "../product/deleteproduct.do",
		data : {
			'ids' : ids	
		},
		dataType : "json",
		success : function(data) {
			layer.msg("删除成功");
			productpage($(".laypage_curr").text());
		}
	});
}


function updateproduct(name,code,resourcecode){
	$("#updatae_name").val(name);
	$('#updatae_code').val(code);
	$("#up_source span").remove();
	$.post("../product/getSource.do",function(data){	
		for(var i=0;i<data.length;i++){
		if(resourcecode==data[i].tableName){
			var html="<span><input  name=\"source_code\" checked='checked' value="+data[i].productSourceCode+"  type=\"radio\">"+data[i].tableName+"&nbsp;&nbsp;&nbsp;</span>";
			$("#up_source").append(html);
		}else{
			var html="<span><input  name=\"source_code\"  value="+data[i].productSourceCode+"  type=\"radio\">"+data[i].tableName+"&nbsp;&nbsp;&nbsp;</span>";
			$("#up_source").append(html);	
		}
		}
	});
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['50%', '500px'],
	    title: '修改产品类型',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#update_product"),
	    btn: ['确认', '取消'],
		yes: function(){
			var productname=$("#updatae_name").val();
			var productcode=$("#updatae_code").val();
			var value_code=$("input[name='source_code']:checked").val();
			checkproductname(productname,productcode,value_code);
	    },
	}); 
}

function addproduct(){
	$("#product_code").val("");
	$("#product_name").val("");
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['50%', '500px'],
	    title: '新增产品类型',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#add_product"),
	    btn: ['确认', '取消'],
		yes: function(){
			var productcode=$("#resource_type_code").val();
			var productname=$("#resource_type_name").val();
			var value_code=$("input[name='source_code']:checked").val();
			checkproductnameto(productname,productcode,value_code);
			$("#resource_type_code").val("");
			$("#resource_type_name").val("");
			$("[name='source_code']").prop("checked",false);
	    },
	    btn2: function(){  
	    	$("#resource_type_code").val("");
			$("#resource_type_name").val("");
			$("[name='source_code']").prop("checked",false);
	    },  
	    cancel: function(){
	    	$("#resource_type_code").val("");
			$("#resource_type_name").val("");
			$("[name='source_code']").prop("checked",false);
	    },  
	}); 
}


function checkproductnameto(productname,productcode,value_code)
{	
	$.ajax( {  
		type : "POST",  
		url : "../product/checkproduct.do",
			data : {
				'resourceTypeCode' : productcode,
				'resourceTypeName' : productname,
				'productResourceCode' : value_code
			},
			dataType : "json",
			success : function(data) {
				if(data){
					$("#checkname").text("部门名称重复，请重新输入");
				}else{
						doaddproduct(productname,productcode,value_code);
				}
			}
		});
}

function checkproductname(productname,productcode,value_code){
	$("#checkname").text("");
	$.ajax( {  
		type : "POST",  
		url : "../product/checkproduct.do",
			data : {
				'resourceTypeCode' : productcode,
				'resourceTypeName' : productname,
				'productResourceCode' : value_code
			},
			dataType : "json",
			success : function(data) {
				if(data){
					$("#checkname").text("部门名称重复，请重新输入");
				}else{
						doupdateproduct(productname,productcode,value_code);
				}
			}
		});
}

function doaddproduct(productname,productcode,value_code){
	$("#checkname").text("");
	if(productname!=null&&productname!=''&&productcode!=null&&productcode!=''&&value_code!=null&&value_code!=''){
		$.ajax( {  
			type : "POST",  
			url : "../product/doaddproduct.do",
				data : {
					'resourceTypeCode' : productcode,
					'resourceTypeName' : productname,
					'productResourceCode' : value_code
				},
				dataType : "json",
				success : function(data) {
					if(data){
						layer.msg("添加成功",{time:2000});
						layer.closeAll('page');
						productpage($(".laypage_curr").text());
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
function doupdateproduct(productname,productcode,value_code){
	$("#checkname").text("");
	if(productname!=null&&productname!=''&&productcode!=null&&productcode!=''&&value_code!=null&&value_code!=''){
		$.ajax( {  
			type : "POST",  
			url : "../product/doupdateproduct.do",
				data : {
					'resourceTypeCode' : productcode,
					'resourceTypeName' : productname,
					'productResourceCode' : value_code
				},
				dataType : "json",
				success : function(data) {
					if(data){
						layer.msg("修改成功",{time:2000});
						layer.closeAll('page');
						productpage($(".laypage_curr").text());
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
	window.location.href=document.referrer;
}

