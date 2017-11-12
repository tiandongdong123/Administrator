/*$(function(){
	getResource();
});*/
function addunit(){
	window.location.href="../system/unitSystem.do?id=goback";
}

function select_product(){
	var son_code=$("#son").val();
	if(son_code!='请选择平台名称'&&son_code!=''){
		$.ajax({
			type:'POST',
			url : "../system/product_source.do",
			data: {
				'data' : son_code 
			},
			dataType : "json",
			success : function(data){
				$("#product option").remove();
				for(var i=0;i<data.length;i++)
					{
					if(data[i]!='null'&&data[i]!=undefined&&data[i]!=""){
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

function select_source(){	
	var data=$("#product").val();
	$.ajax({
		type:'POST',
		url:"../system/product_type.do",
		data:{'data':data},
		dataType : "json",
		success : function(data){
			$("#rname option").remove();
			for(var i=0;i<data.length;i++){
				var op="<option value="+data[i].resourceTypeCode+">"+data[i].resourceTypeName+"</option>";
				$("#rname").append(op);
			}
		}
	});
}


function checkpricerid(id){
	var name=$("#name").val();
	var rid=$("#rid").val();
	if(rid!=null&&rid!=''){
		$.ajax( {  
			type : "POST",  
			url : "../resourceprice/checkpricerid.do",
				data : {
					'name':name,
					'rid':rid,
				},
				dataType : "json",
				success : function(data) {
					if(!data){
						doaddprice(id);
						$("#checkrid").text("");
					}else{
						$("#checkrid").text("产品ID重复");
					}
				}
			});
	}else{
		layer.msg("请输入产品id和产品名称！");
	}
	
}

function doaddprice(id){
	var name =$("#name").val();
	if(name==null||name==''){
		$("#name").focus();
		$("#checkname").text("产品名字不能为空");
		layer.msg("请选择正确的子系统名称");
		return;
	}
	var rid=$("#rid").val();
	var son=$("#son").val();
	var product=$("#product").val();
	var rname=$("#rname").val();
	var unitname=$("#unitname").val();
	var price=$("#price").val();
	if(price==null||price==''){
		$("#price").focus();
		$("#checkmoney").text("请输入产品单价");
		return;	
	}else{
		var val = MoneyCheck(price);
		if(val==false){
			$("#checkmoney").text("请输入正确的价格");
			return;
		}
	}
	$.ajax( {  
		type : "POST",  
		url : "../resourceprice/doaddprice.do",
			data : {
				'unitCode' : unitname,
				'price':price,
				'name':name,
				'sonCode':son,
				'sourceCode':product,
				'resourceTypeCode':rname,
				'rid':rid,
			},
			dataType : "json",
			success : function(data) {
				if(data){
					if(id==1){
						layer.msg("添加成功",{time:2000});
						$("#name").val("");
						$("#rid").val("");
						$("#price").val("");
					}else{
						layer.msg("添加成功",{time:2000});
						window.location.href="../system/resourceManager.do";
					}
				}else{
					layer.msg("添加失败",{time:2000});
				}
			}
		});
}

function MoneyCheck(str){
    var isNum = /^[1-9]{1}\d*(\.\d{1,2})?$/;
    if(!isNum.test(str)){
        return false;
    }else{
    	return true;
    }
}
function productType()
{
	window.location='../system/prductType.do';
}
