var username;
var ip;
var module;
var behavior;
var startTime;
var endTime;

$(function(){
	tabulation();
});

/**
 * 加载列表数据--liuYong
 */
function tabulation(curr){
		username=$("#user_id").val();
		ip=$("#institution_name").val();
		module=$("#model").find("option:selected").val();
		behavior=$("#restype").find("option:selected").val();
		startTime=$("#startTime").val();
		endTime=$("#endTime").val();
		$.ajax({
			type:"POST",
			data:{
				"username":username,
				"ip":ip,
				"module":module,
				"behavior":behavior,
				"startTime":startTime,
				"endTime":endTime,
				"pageNum":curr||1,
			},
			url:"../log/getLogJson.do",
			dataType:"json",
			success:function(data){
				
				var pagerow=data.pageRow;
				var html="";
				
				if("机构用户信息管理"==module){
					$.each(pagerow, function(i, obj) {
						html+="<tr>" +
						"<td>"+(10*(curr||1-1)+i+1)+"</td>" +
						"<td>"+pagerow[i].username+"</td>" +
						"<td>"+pagerow[i].ip+"</td>" +
						"<td>"+timeStamp2String(pagerow[i].time)+"</td>" +
						"<td>"+pagerow[i].module+"</td>" +
						"<td>"+pagerow[i].behavior+"</td>" +
						"<td>"+pagerow[i].userId+"</td>" +
						"<td>"+pagerow[i].projectname+"</td>" +
						"<td>"+pagerow[i].totalMoney+"</td>" +
						"<td>"+pagerow[i].purchaseNumber+"</td>" +
						"<td>"+pagerow[i].validityStarttime+"</td>" +
						"<td>"+pagerow[i].validityEndtime+"</td>" +
					 "</tr>";
					});
					$("#tbody_").html(html);
				}else{
					$.each(pagerow, function(i, obj) {
						html+="<tr>" +
						"<td>"+(10*(curr||1-1)+i+1)+"</td>" +
						"<td>"+pagerow[i].username+"</td>" +
						"<td>"+pagerow[i].ip+"</td>" +
						"<td>"+timeStamp2String(pagerow[i].time)+"</td>" +
						"<td>"+pagerow[i].module+"</td>" +
						"<td>"+pagerow[i].behavior+"</td>" +
						"<td>"+pagerow[i].operation_content+"</td>" +
					 "</tr>";
					});
					$("#tbody").html(html);
				}
				
				var pageTotal=data.pageTotal;
				var pageSize=data.pageSize;
				var pages=pageTotal%pageSize==0?pageTotal/pageSize:pageTotal/pageSize+1;
				var groups=pages>=4?4:pages;
				
				 // 显示分页
		        laypage({
			    	cont: 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
									// id="page1"></div>
			        pages: pages, // 通过后台拿到的总页数
			        curr: curr, // 当前页
			        skip: true, // 是否开启跳页
				    skin: 'molv',// 当前页颜色，可16进制
				    groups: groups, // 连续显示分页数
				    first: '首页', // 若不显示，设置false即可
				    last: '尾页', // 若不显示，设置false即可
				    prev: '上一页', // 若不显示，设置false即可
				    next: '下一页', // 若不显示，设置false即可
		            jump: function(obj, first){ // 触发分页后的回调
		                if(!first){ // 点击跳页触发函数自身，并传递当前页：obj.curr
		                	tabulation(obj.curr);
		                }
		            }
		        });
			}
		});	
}

//单个删除
function remove(id){
	$.ajax({
		type:"POST",
		data:{"ids":id},
		url:"../log/deleteLog.do",
		dataType:"json",
		success:function(data){
			if(data>=1){
				refresh();
				layer.msg("删除成功",{icon: 1});
			}else{
				layer.msg("删除失败",{icon: 2});
			}
		},
		error:function(error){
			layer.msg(error,{icon: 2});
		}
	});
}

//批量删除
function deleteMore(){
	var ids=new Array();
	var checked=$("input[name='check_one']:checked");
	
	checked.each(function(){
		ids.push($(this).val());
	});
	
	if(null==ids || ""==ids){
		layer.msg("请选择要删除的数据",{icon:2});
		return;
	}
	$.ajax({
		type:"POST",
		data:{"ids":ids},
		url:"../log/deleteLog.do",
		dataType:"json",
		success:function(data){
			if(data>=1){
				layer.msg("成功删除"+data+"条数据",{icon: 1});
				refresh();
			}else{
				layer.msg("删除失败",{icon: 2});
			}
		},
		error:function(error){
			layer.msg(error,{icon: 2});
		}
	});
}

//全选全不选
function checkAll(){
	$("input[name='check_one']").prop("checked",$("#checkAll").prop("checked"));
}


//日志导出
function exportLog(){
	
	username=$("#user_id").val();
	ip=$("#institution_name").val();
	module=$("#model").find("option:selected").val();
	behavior=$("#restype").find("option:selected").val();
	startTime=$("#startTime").val();
	endTime=$("#endTime").val();
	window.location.href="../log/exportLog.do?" +
			"username="+username+
			"&module="+module+
			"&ip="+ip+
			"&behavior="+behavior+
			"&startTime="+startTime+
			"&endTime="+endTime;
}

//刷新页面
function refresh(){
	window.location.href="../admin/logmanage.do";
}

//动态获取模块对应的操作类型
function getResTypeOnSelect(model){
	$("#restype").empty();
	$("#restype").append("<option value=''>--请选择操作类型--</option>");
	if(null!=$(model).val() && ""!=$(model).val()){
		$.ajax({
			type:"POST",
			data:{"modelname":$(model).val()},
			url:"../log/getResTypeByModel.do",
			dataType:"json",
			success:function(data){
				if(null!=data && ""!=data){
					$.each(data,function(key,val) {
						$("#restype").append("<option value='"+val+"'>"+val+"</option>");
					});
				}
			},
			error:function(error){
				layer.msg(error,{icon: 2});
			}
		});
		
		changeShow($(model).val());
		tabulation();
		
	}
}

/**
 * 是否展示机构用户信息
 * @param model 模块名称
 */
function changeShow(model){
	if("机构用户信息管理"==model){
		$("#one").hide();
		$("#two").show();
	}else{
		$("#two").hide();
		$("#one").show();
	}
}



/**
 * 日期转换
 * @param time
 * @returns {String}
 */
function timeStamp2String(time){
    var year = 1900+time.year;
    var month = time.month + 1 < 10 ? "0" + (time.month + 1) : time.month + 1;
    var date = time.date < 10 ? "0" + time.date : time.date;
    var hour = time.hours< 10 ? "0" + time.hours : time.hours;
    var minute = time.minutes< 10 ? "0" + time.minutes : time.minutes;
    var second = time.seconds< 10 ? "0" + time.seconds : time.seconds;
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}


