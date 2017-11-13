$(function(){
	sonpage(1);	
});


function sonpage(curr)
{
	 $.getJSON('../son/getson.do', {
	        pagenum: curr,//向服务端传的参数
	        pagesize :10
	    }, function(res){
	    	html="";
	    for(var i =0;res.pageRow[i];i++){
	    	id = 10*(curr-1)+i+1;
	    	html+="<tr><td><input type='checkbox' name='ids' value="+res.pageRow[i].id+" ></td> " +
	    			"<td>"+id+"</td><td>"+res.pageRow[i].sonName+"</td><td>"+res.pageRow[i].sonCode+"</td>" +
	    					"<td><button type='button' class='btn btn-primary' onclick=\"updateson("+res.pageRow[i].id+",'"+res.pageRow[i].sonName+"','"+res.pageRow[i].sonCode+"','"+res.pageRow[i].productResourceCode+"')\">修改</button><button type='button' class='btn btn-primary' onclick=\"deleteson("+res.pageRow[i].id+")\">删除</button></td></tr>";   	
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
                	sonpage(obj.curr);
            		
                }
            }
        });
});
}

function source_data()
{
	$.post("../son/source_data.do",function(data){
		$("#source span").remove();
		var input="<span><input type='checkbox' onclick='checkBox();'  id='checkall'>全选</span>";
		$("#source").append(input);
		for(var i=0;i<data.length;i++)
			{
			var html=" <span><input type=\"checkbox\"  name=\"sourcedata\"   value="+data[i].productSourceCode+">"+data[i].tableName+"&nbsp;&nbsp;&nbsp;</span>";
			$("#source").append(html);
			}
	},"json");
}

function checkBox(){
	if(document.getElementById("checkall").checked==true){
		$("[name='sourcedata']").prop("checked",true);
	}else if(document.getElementById("checkall").checked==false){
		$("[name='sourcedata']").prop("checked",false);
	}
}

function deleteson(id){
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
		url : "../son/deleteson.do",
			data : {
				'ids' : ids	
			},
			dataType : "json",
			success : function(data) {
				layer.msg("删除成功");
				sonpage($(".laypage_curr").text());
			}
		});
}


function updateson(id,name,code,source){
	$('#up_name').val(name);
	$('#up_code').val(code);
	var json=source.split(",");
	$("#upsource span").remove();
	$.post("../son/source_data.do",function(data){
		$("#upsource span").remove();
		var html="<span><input type='checkbox' onclick='upcheckall();'  id='upall'>全选</span>";
			$("#upsource").append(html);
		var input=null;
			for(var i=0;i<data.length;i++)
			{
				var int=0;
				for(var j=0;j<json.length;j++)
				{
					if(json[j]==data[i].tableName){
						int=1;
					}
				}
				if(int==1){
					input="<span><input type=\"checkbox\" checked='checked'  name=\"updatasource\"   value="+data[i].productSourceCode+">"+data[i].tableName+"&nbsp;&nbsp;&nbsp;</span>";
				}else{
					input="<span><input type=\"checkbox\"  name=\"updatasource\"   value="+data[i].productSourceCode+">"+data[i].tableName+"&nbsp;&nbsp;&nbsp;</span>";
				}
				$("#upsource").append(input);
		}
	},"json");
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['50%', '500px'],
	    title: '修改平台类型',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#up_sonSystem"),
	    btn: ['确认', '取消'],
		yes: function(){
			var sonname=$("#up_name").val();
			var soncode=$("#up_code").val();
			var chk_value =[]; 
			$('input[name="updatasource"]:checked').each(function(){
				if(chk_value=="")
					{
					chk_value=$(this).val(); 
					}
				else
					{
					chk_value=chk_value+","+$(this).val();
					}
			});
			checksonname(id,sonname,soncode,chk_value);
	    },
	}); 
}

function upcheckall(){
	
	if(document.getElementById("upall").checked==true){
		$("[name='sourcedata']").prop("checked",true);
	}else if(document.getElementById("upall").checked==false){
		$("[name='sourcedata']").prop("checked",false);
	}
}

function addson(){
	$("#son_code").val("");
	$("#son_name").val("");
	
	source_data();
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['50%', '500px'],
	    title: '新增平台类型',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#add_sonSystem"),
	    btn: ['确认', '取消'],
		yes: function(){
			var soncode=$("#son_code").val();
			var sonname=$("#son_name").val();
			/*var chk_value =[]; 
			$('input[name="sourcedata"]:checked').each(function(){ 
				if(chk_value=="")
				{
				chk_value=$(this).val(); 
				}
			else
				{
				chk_value=chk_value+","+$(this).val();
				}
			}); */
			checksonnameto(sonname,soncode);
	    },
	}); 
}


function checksonnameto(sonname,soncode)
{	
	$.ajax( {  
		type : "POST",  
		url : "../son/checkson.do",
			data : {
				'sonName' : sonname,
				'sonCode' : soncode,
			},
			dataType : "json",
			success : function(data) {
				if(data){
					layer.msg("平台名称或平台Code重复,请重新输入....",{time:2000});
				}else{	
					doaddson(sonname,soncode);
				}
			}
		});
}

function checksonname(id,sonname,soncode){
	doupdateson(id,sonname,soncode);
}

function doaddson(sonname,soncode){
	$("#checkname").text("");
	if(sonname!=null&&sonname!=''&&soncode!=null&&soncode!=''){
		$.ajax( {  
			type : "POST",  
			url : "../son/doaddson.do",
				data : {
					'sonName' : sonname,
					'sonCode' : soncode,
				},
				dataType : "json",
				success : function(data) {
					if(data){
						layer.msg("添加成功",{time:2000});
						layer.closeAll('page');
						sonpage($(".laypage_curr").text());
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
function doupdateson(id,sonname,soncode){
	$("#checkname").text("");
	if(sonname!=null&&sonname!=''&&soncode!=null&&soncode!=''){
		$.ajax( {  
			type : "POST",  
			url : "../son/doupdateson.do",
				data : {
					'id':id,
					'sonName' : sonname,
					'sonCode' : soncode,
				},
				dataType : "json",
				success : function(data) {
					if(data){
						layer.msg("修改成功",{time:2000});
						layer.closeAll('page');
						sonpage($(".laypage_curr").text());
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



