var level,classNum,className,pageIndex;
$(function(){
	level=$("#level").val();
	if(level !=""){
		selectValue("xkjb",level);
	}
	showPage();
	//ztreeshow();
});

//点击树节点，选中节点的所有叶子节点
function treenodeClick(event, treeId, treeNode, clickFlag) { 
    //此处treeNode为当前节点 
     var str ='' ; 
     if(treeNode.isParent){
         str = getAllChildrenNodes(treeNode,str);
     }else{
       zTree.selectNode(treeNode);
     }
}
 
//递归，得到叶子节点的数据
function getAllChildrenNodes(treeNode,result){ 
    if (treeNode.isParent) { 
      var childrenNodes = treeNode.children; 
      if (childrenNodes) { 
          for (var i = 0; i < childrenNodes.length; i++) { 
           if(childrenNodes[i].isParent){
            getAllChildrenNodes(childrenNodes[i], result); 
           }else{
            zTree.selectNode(childrenNodes); 
           }
          } 
      } 
  } 
}

function find(){
	findtext();
	window.location.href="../content/subjectquery.do?level="+level+"&classNum="+classNum+"&className="+className;
//	showPage();
}
/*分页显示*/
function showPage(){
	//显示分页
	var pageNum = $("#pageNum").val();
	var pageTotal = $("#pageTotal").val();
	laypage({
    	cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
        pages: pageTotal, //通过后台拿到的总页数
        curr: pageNum, //当前页
        skip: true, //是否开启跳页
	      skin: '#367fa9',//当前页颜色，可16进制
	      groups: 4, //连续显示分页数
	      first: '首页', //若不显示，设置false即可
	      last: '尾页', //若不显示，设置false即可
	      prev: '上一页', //若不显示，设置false即可
	      next: '下一页', //若不显示，设置false即可
        jump: function(obj, first){ //触发分页后的回调
        	if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
            	paging(obj.curr);
        }else{
        	paging(obj.curr);
        }
       }
    });
}
/**
 * 分页事件
 */
function paging(curr){
	pageIndex=curr;
	findtext();
	$.ajax({
		type : "post",
		async:false,
		url : "../content/subjectJson.do",
		dataType : "json",
		data : {
			"page":curr,
			"level":level,
			"classNum":classNum,
			"className":className
		},
		success : serachdata
	});
}

function serachdata(data){
	var pageNum = data.pageNum;
	var pageTotal = data.pageTotal;
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
	$("#pageNum").val(pageNum);
	$("#pageTotal").val(pageTotal);
	var pageRow=data.pageRow;
	var resHtml = "<tbody><tr style='text-align: center;'>" +
			"<td><input onclick=\"checkAll()\" class='allId' type='checkbox'></td>" +
			"<td class='mailbox-star'>序号</td>" +
			"<td class='mailbox-name'>级别</td>" +
			"<td class='mailbox-attachment'>分类号</td>" +
			"<td class='mailbox-subject'>分类名称</td>" +
			"<td class='mailbox-date'>操作</td></tr>";
	if(pageRow.length>0){
		for(var i = 0;i<pageRow.length;i++){
			var index = 1+i;
			var rows = pageRow[i];
			resHtml+=" <tr style='text-align: center;'>" +
					"<td><input type='checkbox' name='commonid' value='"+rows.id+"'></td>" +
					"<td class='mailbox-star'>"+index+"</td>" +
					"<td class='mailbox-name'>"+rows.level+"</td>" +
					"<td class='mailbox-subject'>"+rows.classNum+"</td>" +
					"<td class='mailbox-attachment' style='text-align: left;'>"+rows.className+"</td>" +
					"<td class='mailbox-date'>";
			if(purview.indexOf("C417")!=-1){
			resHtml+="<div class='col-md-3 col-sm-4'><a href='#' onclick=\"updateSub('"+rows.id+"')\"><i class='fa fa-fw fa-pencil-square-o'></i></a></div>";
			}else{
			resHtml+="<div class='col-md-3 col-sm-4'><a style='display:none' href='#' onclick=\"updateSub('"+rows.id+"')\"><i class='fa fa-fw fa-pencil-square-o'></i></a></div>";
			}
			if(purview.indexOf("C412")!=-1){
			resHtml+="<div class='col-md-3 col-sm-4'><a href='#' onclick=\"removee('"+rows.id+"')\"><i class='fa fa-fw fa-trash-o'></i></a></div><td>"+
            "</tr>";
			}else{
			resHtml+="<div class='col-md-3 col-sm-4'><a style='display:none' href='#' onclick=\"removee('"+rows.id+"')\"><i class='fa fa-fw fa-trash-o'></i></a></div><td>"+
	        "</tr>";	
			}		
						
		}
	}
	resHtml+="</tbody>";
	$(".table-striped").html(resHtml);
	document.getElementById("here").scrollIntoView();
}
function addsub(){
	window.location.href="../content/subjectadd.do";
}
function updateSub(id){
	window.location.href="../content/subjectmodify.do?idSub="+id;
}
function findtext(){
	level=$("#xkjb").find("option:selected").val();
	if(level=="全部"){
		level="";
	}
	classNum=$("#inputEmail3").val();
	className=$("#inputEmail4").val();
}

//单条删除
function removee(id){
	$.ajax({
		type : "post",
		data : {ids: id},
		url :  "../content/deleteSubject.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {},
		success : deleteCallback,
		complete : function(XMLHttpRequest, textStatus) {},
		error : function(data) {alert(data);}
	});
}
// 多条删除
function deleteMore(){
	if(!($("input:checkbox[name=commonid]:checked").is(':checked'))){
		layer.msg("请选择删除内容！",{icon: 3});
	}else{
	var ids = "";
			$("input:checkbox[name=commonid]:checked").each(function(){
				ids += "," + $(this).val();
			});
			if (ids != "")
				ids = ids.substring(1);
			$.ajax({
				type : "post",
				data : {ids: ids},
				url :"../content/deleteSubject.do",
				dataType : "json",
				beforeSend : function(XMLHttpRequest) {},
				success : deleteCallback,
				complete : function(XMLHttpRequest, textStatus) {},
				error : function(data) {alert(data);}
			});
	}	
}
// 删除回执
function deleteCallback(data) {
	if (data.flag=="true") {
		layer.msg("删除成功!",{icon: 1});
		showPage();
	}else{
		layer.msg("删除失败!",{icon: 2});
	}
}
//全选

/*全选与全不选*/
function checkAll(){
		if($(".allId").is(':checked')){
			$("input[name=commonid]").each(function(){
				$(this).prop("checked", "checked");
			});
		}else{
			$("input[name=commonid]").each(function(){
				$(this).removeAttr("checked");
			});
		}
}
function selectValue(id,val){
	for(var i=0;i<document.getElementById(id).options.length;i++)
    {
        if(document.getElementById(id).options[i].value == val)
        {
            document.getElementById(id).options[i].selected=true;
            break;
        }
    }
	level=$("#xkjb").find("option:selected").text();
}

function refresh(){
	window.location.href="../content/subjectquery.do";
}

//----------------------------发布--liuYong-----------------------------
function publish(){
	layer.alert('确定发布到前台？', {
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../content/subjectPublish.do",
	    		success : function(data){
	    			if(data){
	    				refresh();
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	        layer.closeAll();
	    }
	  });
}



//导出
function exportSubject(){
	window.location.href="../content/exportSubject.do?level="+level+"&classNum="+classNum+"&className="+className;
}


//ztree展示
function ztreeshow(){
	var setting = {
            check: {
                enable: true,
                chkboxType: {"Y":"ps", "N":"ps"}
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
	
	$.ajax({
		url:"../content/subjectTree.do",
		dataType:"json",
		success:function(data){
	        $.fn.zTree.init($("#subjectTree"),setting, data);
		}
	});
}



