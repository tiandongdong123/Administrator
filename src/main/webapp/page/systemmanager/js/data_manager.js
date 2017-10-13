var dataname;
$(function(){
	paging(1);
});

function findone(){
	dataname=$("#dataname").val();
	$.ajax({
		type : "post",
		async:false,
		url : "../data/getdata.do",
		dataType : "json",
		data : {
			pagenum: 1,
			pagesize :10,
			dataname:dataname
		},
		success : datapage
	});
}

/**
 * 分页事件
 */
function paging(curr){
	$.ajax({
		type : "post",
		async:false,
			url : "../data/getdata.do",
		dataType : "json",
		data : {
			pagenum: curr,
			pagesize :10,
		},
		success : datapage
	});
}

//分页显示
function datapage(res){
		var pageNum = res.pageNum;
		var pageTotal = res.pageTotal;
		$("#pageNum").val(pageNum);
		$("#pageTotal").val(pageTotal);
		var html="";
		var status="";
		for(var i =0;res.pageRow[i];i++){
			if(res.pageRow[i].status==1){
				status="<td>已发布</td>";
				buttonname="下撤";
			}else if(res.pageRow[i].status==null){
				status="<td>下撤</td>";
				buttonname="发布";
			}else{
				status="<td>未发布</td>";
				buttonname="再发布";
			}
			id = 10*(pageNum-1)+i+1;
			var describe = res.pageRow[i].tableDescribe;
			if(describe!=null){
				describe = describe.substring(0,35);
			}else{
				describe = "";
			}
			html+="<tr>" +
				"<td style='vertical-align:middle;' ><input type='checkbox' name='commonid' value='"+res.pageRow[i].id+"'></td>"+
				"<th>"+id+"</th>" +
				"<td>"+res.pageRow[i].tableName+"</td>" +
				"<td  style=\"word-wrap:break-word;\">"+describe+"...</td>" +
				"<td>"+res.pageRow[i].dbtype+"</td>" +
				"<td  style=\"word-wrap:break-word;\">"+res.pageRow[i].sourceDb+"</td>" +
				"<td>"+res.pageRow[i].resType+"</td>"+
				"<td>"+res.pageRow[i].language+"</td>"+
				"<td>"+(res.pageRow[i].customPolicy==null?"":res.pageRow[i].customPolicy)+"</td>"+
				status +
				"<td><button type='button' style='width: 100px;' class='btn btn-primary' onclick=\"doupdatedata('"+res.pageRow[i].id+"')\">修改</button></br>" +
				"<button type='button' style='width: 100px;'  class='btn btn-primary' onclick=\"deletedata('"+res.pageRow[i].id+"')\">删除</button></br>" +
				"<button type='button' style='width: 100px;' class='btn btn-primary' onclick=\"changedata(this,'"+res.pageRow[i].id+"')\">"+buttonname+"</button></br>";
		}
		document.getElementById('databody').innerHTML = html;
		var groups;
		if(pageTotal>=4){
			groups=4;
		}else{
			groups=pageTotal;
		}
		//显示分页
		laypage({
			cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
			pages: pageTotal, //通过后台拿到的总页数
			curr: pageNum, //当前页
			skin: 'molv',//当前页颜色，可16进制
			groups: groups, //连续显示分页数
			first: '首页', //若不显示，设置false即可
			last: '尾页', //若不显示，设置false即可
			prev: '上一页', //若不显示，设置false即可
			next: '下一页', //若不显示，设置false即可
			jump: function(obj, first){ //触发分页后的回调
				if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
					paging(obj.curr);
				}
			}
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
			if(data){
				layer.msg("修改成功");
				paging($(".laypage_curr").text());
			}else {
				layer.msg("修改失败");
			}

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
	$.ajax({
		type : "post",
		async:false,
		url:"../data/checkResourceForOne.do",
		dataType:"json",
		data:{"id":id},
		success:function(data){
			if(data.flag=="true"){
				window.location.href="../data/updatedata.do?id="+id;
			}else{
				alert("请先下撤再修改！");
			}
		}
	});
}
function deletedata(id){
	$.ajax({
		type : "post",
		async:false,
		url:"../data/checkResourceForOne.do",
		dataType:"json",
		data:{"id":id},
		success:function(data){
			if(data.flag=="true"){
				$.ajax( {
					type : "POST",
					url : "../data/deletedata.do",
					data : {
						"id" : id
					},
					dataType : "json",
					success : function(data) {
						if(data){
							layer.msg("删除成功");
							paging($(".laypage_curr").text());
						}else{
							layer.msg("删除失败");
						}
					}
				});
			}else{
				alert("请先下撤再删除！");
			}
		}
	});

}
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


function moveUp() {
	if (!($("input:checkbox[name=commonid]:checked").is(':checked'))) {
		alert("请选择上移内容！");
	} else {
		if ($("input:checkbox[name=commonid]:checked").size() >1) {
			alert("请不要多选！")
		} else {
			var id = "";
			$("input:checkbox[name=commonid]:checked").each(function(){
				id=$(this).val();
			});
			$.ajax({
				type : "post",
				async:false,
				url:"../data/moveUpDatabase.do",
				dataType:"json",
				data:{"id":id},
				success:function(data){
					if(data.flag=="true"){
						paging($(".laypage_curr").text());
					}else{
						alert("上移失败！");
					}
				}
			});
		}
	}
}

function moveDown() {
	if (!($("input:checkbox[name=commonid]:checked").is(':checked'))) {
		alert("请选择下移内容！");
	} else {
		if ($("input:checkbox[name=commonid]:checked").size()>1) {
			alert("请不要多选！")
		} else {
			var id = "";
			$("input:checkbox[name=commonid]:checked").each(function(){
				id =$(this).val();
			});
			$.ajax({
				type : "post",
				async:false,
				url:"../data/moveDownDatabase.do",
				dataType:"json",
				data:{"id":id},
				success:function(data){
					if(data.flag=="true"){
						paging($(".laypage_curr").text());
					}else{
						alert("下移失败！");
					}
				}
			});
		}
	}
}

function exportData(){
	window.location.href="../data/exportData.do?dataname="+dataname;
}
