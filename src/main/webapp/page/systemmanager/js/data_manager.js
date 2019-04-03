var dataname="";
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
		var purview=$.cookie('purview');
		$("#pageNum").val(pageNum);
		$("#pageTotal").val(pageTotal);
		var html="";
		var state="";
		for(var i =0;res.pageRow[i];i++){
			if(res.pageRow[i].status==1) {
				buttonOne = "冻结";
			}else{
				buttonOne="解冻";
			}
			if(res.pageRow[i].state==1){
				state="<td>已发布</td>";
				buttonTwo="下撤";
			}else if(res.pageRow[i].state==null){
				state="<td>下撤</td>";
				buttonTwo="发布";
			}else{
				state="<td>未发布</td>";
				buttonTwo="再发布";
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
				state;
			if(purview.indexOf("F212")!=-1){
				html+=	"<td><button type='button' style='width: 100px;' class='btn btn-primary' onclick=\"doupdatedata('"+res.pageRow[i].id+"')\">修改</button></br>";
			}else{
				html+=	"<td><button style='display:none' type='button' style='width: 100px;' class='btn btn-primary' onclick=\"doupdatedata('"+res.pageRow[i].id+"')\">修改</button></br>";
			}
			if(purview.indexOf("F213")!=-1){
				html+=	"<button type='button' style='width: 100px;'  class='btn btn-primary' onclick=\"changedata(this,'"+res.pageRow[i].id+"')\">"+buttonOne+"</button></br>";
			}else{
				html+=	"<button style='display:none' type='button' style='width: 100px;'  class='btn btn-primary' onclick=\"changedata(this,'"+res.pageRow[i].id+"')\">"+buttonOne+"</button></br>";
			}
			if(purview.indexOf("F214")!=-1){
				html+=	"<button type='button' style='width: 100px;' class='btn btn-primary' onclick=\"PushData(this,'"+res.pageRow[i].id+"')\">"+buttonTwo+"</button></br>";
			}else{
				html+=	"<button style='display:none' type='button' style='width: 100px;' class='btn btn-primary' onclick=\"PushData(this,'"+res.pageRow[i].id+"')\">"+buttonTwo+"</button></br>";
			}
		
				
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
	var result = btnname.indexOf("冻结");
	if(result>0){
		$.ajax({
			type : "post",
			async:false,
			url:"../data/checkResourceForOne.do",
			dataType:"json",
			data:{"id":id},
			success:function(data){
				if(data.flag=="true"){
					$.ajax({
						type : "POST",
						url : "../data/closedata.do",
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
				}else {
					layer.msg("请先下撤后再冻结！");
				}
			}
		});
	}else{
		$.ajax({
			type : "POST",
			url : "../data/opendata.do",
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
}

function PushData(obj,id){
	var btnname ="do"+ $(obj).text();
	var result = btnname.indexOf("发布");
	if(result>0){
		$.ajax({
			type : "post",
			async:false,
			url:"../data/checkStatus.do",
			dataType:"json",
			data:{"id":id},
			success:function(data){
				if(data.flag=="true"){
					$.ajax({
						type : "POST",
						url : "../data/releaseData.do",
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
				}else {
					layer.msg("请先解冻后再发布！");
				}
			}
		});
	}else{
		$.ajax({
			type : "POST",
			url : "../data/descendData.do",
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
}

function adddata(){
	window.location.href="../admin/dbadd.do";
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
				window.location.href="../admin/dbmodify.do?id="+id;
			}else{
				layer.msg("请先下撤再修改！");
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
				layer.msg("请先下撤再删除！");
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
		layer.msg("请选择上移内容！");
	} else {
		if ($("input:checkbox[name=commonid]:checked").size() >1) {
			layer.msg("请不要多选！")
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
						layer.msg("上移失败！");
					}
				}
			});
		}
	}
}

function moveDown() {
	if (!($("input:checkbox[name=commonid]:checked").is(':checked'))) {
		layer.msg("请选择下移内容！");
	} else {
		if ($("input:checkbox[name=commonid]:checked").size()>1) {
			layer.msg("请不要多选！")
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
						layer.msg("下移失败！");
					}
				}
			});
		}
	}
}

function exportData(){
	window.location.href="../data/exportData.do?dataname="+dataname;
}
