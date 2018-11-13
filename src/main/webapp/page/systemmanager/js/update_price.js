function addunit(){
	window.location.href="../admin/unitoption.do";
}


var starname="";
var startid="";
$(function(){
	starname=$("#name").val();
	startid=$("#rid").val();
});

function checkpricerid(){
	var rname=$("#name").val();
	var rid=$("#rid").val();
	if(rname!=starname||startid!=rid){
		$.ajax( {  
			type : "POST",  
			url : "../resourceprice/checkpricerid.do",
				data : {
					'resource_type':rname,
					'rid':rid,
				},
				dataType : "json",
				success : function(data) {
					if(data){
						layer.msg("该资源类型下ID重复",{time:2000});
					}else{
						doupdateprice();
					}
				}
			});
	}else{
		doupdateprice();
	}
}

function doupdateprice(){
	var name =$("#name").val();
	var rid=$("#rid").val();
	var son=$("#son").val();
	var product=$("#product").val();
	var rname=$("#rname").val();
	var unitname=$("#unitname").val();
	var price=$("#price").val();
	$.ajax( {  
		type : "POST",  
		url : "../resourceprice/doupdateprice.do",
			data : {
				'unitCode' : unitname,
				'price':price,
				'name':name,
				'resourceTypeCode':rname,
				'rid':rid,
				'sonCode' : son,
				'sourceCode' : product
			},
			dataType : "json",
			success : function(data) {
				if(data){
					layer.msg("修改成功",{time:2000});
					window.location.href="../admin/pricemanage.do";
				}else{
					layer.msg("修改失败",{time:2000});
				}
			}
		});
}

function select_product()
{	
	var son_code=$("#son").val();
	if(son_code!='请选择平台名称'&&son_code!='')
		{
		$.ajax({
			type:'POST',
			url : "../system/product_source.do",
			data: {
				'data' : son_code 
			},
			dataType : "json",
			success : function(data)
			{
				$("#product option").remove();
				for(var i=0;i<data.length;i++)
					{
					if(data[i]!='null'&&data[i]!=undefined&&data[i]!="")
						{
						var op="<option value="+data[i].productSourceCode+">"+data[i].tableName+"</option>";
						$("#product").append(op);
						}
						}
			}
		});
		}else{
			$("#product").val("");
			$("#rname").val("");
			}
}

function select_source()
{	
	var data=$("#product").val();
	$.ajax({
			type:'POST',
			url:"../system/product_type.do",
			data:{
				'data':data
				},
			dataType : "json",
			success : function(data){
		$("#rname option").remove();
		for(var i=0;i<data.length;i++)
		{
		var op="<option value="+data[i].resourceTypeCode+">"+data[i].resourceTypeName+"</option>";
		$("#rname").append(op);
		}
	}
});
}