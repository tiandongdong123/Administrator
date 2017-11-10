var pagesize=10;
$(function(){
	
	tabulation(1);
	moreOrSimple();
	keyword();
	
	$(document).click(function(){
	    $("#searchsug").hide();
	});
	
})

var date="";
var startTime;
var endTime;
var institution_name;
var user_id;
var restype;
var dbname;
var pagenum;
/**查询执行方法*/
function query(){
	tabulation(1);
	moreOrSimple();
}


/**
 * 加载列表数据--liuYong
 */
$(function(){
	$('#pageChange').change(function(){
			institution_name=$("#institution_name").val();
			user_id=$("#user_id").val();
			restype=$("#restype").val();
			dbname=$("#database").val();
			var $number=pagesize*(parseInt($('.laypage_curr').text())-1)+1;
			pagesize=parseInt($(this).find('option:selected').val());
			pagenum=curr=parseInt($number/pagesize)+1;
			$.ajax({
				type : "POST",
				url : "../databaseAnalysis/getPage.do",
				data : {
					'institution_name' : institution_name,
					'user_id' : user_id,
					'date' : date,
					'source_db':restype,
					'product_source_code':dbname,
					'startTime' : startTime,
					'endTime' : endTime,
					'pagenum' : curr,//向服务端传的参数
					'pagesize' : pagesize
				},
				dataType : "json",
				success : function(datas) {
					var data=datas.pageRow;
					var totalRow = datas.totalRow;
					var pageSize = datas.pageSize;
					var html="";
					var id;
					for(var i=0;i<data.length;i++){
						id=pageSize*(curr-1)+i+1;
						html+="<tr><td><input type='checkbox' name='checkOne' onclick='checkOne();' value='"+data[i].product_source_code+"'></td>" +
							"<td>"+id+"</td>" +
							"<td>"+data[i].database_name+"</td>" +
							"<td>"+data[i].sum1+"</td>" +
							"<td>"+data[i].sum2+"</td>" +
							"<td>"+data[i].sum3+"</td>" +
							"</tr>";
					}
					document.getElementById('tbody').innerHTML = html;
					var pages;
					var groups;
					if(totalRow%pageSize==0)
					{
						pages = parseInt(totalRow/pageSize);
					}else
					{
						pages = parseInt(totalRow/pageSize+1);
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
								tabulation(obj.curr);
							}
						}
					});
				}
			})
    })
})
function tabulation(curr){
	if($("#startTime").val() == ''|| $("#endTime").val() == '' || $("#startTime").val() == null || $("#endTime").val() ==  null) {
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
	else{
		pagenum=curr;
		getTime();
		institution_name=$("#institution_name").val();
		user_id=$("#user_id").val();
		restype=$("#restype").val();
		dbname=$("#database").val();
		$.ajax({
			type : "POST",
			url : "../databaseAnalysis/getPage.do",
			data : {
				'institution_name' : institution_name,
				'user_id' : user_id,
				'date' : date,
				'source_db':restype,
				'product_source_code':dbname,
				'startTime' : startTime,
				'endTime' : endTime,
				'pagenum' : curr,//向服务端传的参数
				'pagesize' : pagesize
			},
			dataType : "json",
			success : function(datas) {

				var data=datas.pageRow;
				var totalRow = datas.totalRow;
				var pageSize = datas.pageSize;
				var html="";
				var id;
				if(data.length>0){
					$(".showPage").css("display","block");
					for(var i=0;i<data.length;i++){
						id=pageSize*(curr-1)+i+1;
						html+="<tr><td><input type='checkbox' name='checkOne' onclick='checkOne();' value='"+data[i].product_source_code+"'></td>" +
							"<td>"+id+"</td>" +
							"<td>"+data[i].database_name+"</td>" +
							"<td>"+data[i].sum1+"</td>" +
							"<td>"+data[i].sum2+"</td>" +
							"<td>"+data[i].sum3+"</td>" +
							"</tr>";
					}
					document.getElementById('tbody').innerHTML = html;
					var pages;
					var groups;
					if(totalRow%pageSize==0)
					{
						pages = parseInt(totalRow/pageSize);
					}else
					{
						pages = parseInt(totalRow/pageSize+1);
					}
					if(pages>=4)
					{
						groups=4;
					}else
					{
						groups=pages;
					}
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
							tabulation(obj.curr);
						}
					}
				});
			}
		})
	}
}

//
/**
 * 加载图表数据--liuYong
 */
function line(urlType,database_name,datas){
	if($("#startTime").val() == ''|| $("#endTime").val() == '') {
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
	else{
		institution_name=$("#institution_name").val();
		user_id=$("#user_id").val();
		restype=$("#restype").val();
		dbname=$("#database").val();
		getTime();

		$.ajax({
			type : "POST",
			url : "../databaseAnalysis/getChart.do",
			data : {
				'institution_name' : institution_name,
				'user_id' : user_id,
				'date' : date,
				'source_db':restype,
				'product_source_code':dbname,
				'startTime' : startTime,
				'endTime' : endTime,
				'urlType':urlType,
				'databaseNames':database_name,
			},
			dataType : "json",
			success : function(data) {

				var myChart = echarts.init(document.getElementById('main'));
				option = {
					tooltip : {
						trigger: 'axis'
					},
					legend: {
						data:datas,
					},
					toolbox: {
						show : true,
						feature : {
							mark : {show: true},
							dataView : {show: true, readOnly: false},
							magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
							restore : {show: true},
							saveAsImage : {show: true}
						}
					},
					calculable : true,
					xAxis : [
						{
							type : 'category',
							boundaryGap : false,
							data : data.timeArr
						}
					],
					yAxis : [
						{
							type : 'value'
						}
					],
					series : [

					]
				};
				for(var i =0;i<datas.length;i++){
					var name = datas[i];
					var num=new Array();
					num =data.content[name];
					option.series.push(
						{
							name:name,
							type:'line',
							data:num
						}
					)
				}
				myChart.setOption(option);

			}
		});
	}
}

/**判断是否用小时为统计单位*/
function getTime(){
	startTime=$("#startTime").val();
	endTime=$("#endTime").val();
	
	if(startTime!=''&&endTime!=''){
		var date1= new Date(endTime.replace(/-/g,"/"));
		var date2= new Date(startTime.replace(/-/g,"/"));
		var between = date1-date2;
		if(between<=0){
			date=date1.getFullYear()+"-"+(date1.getMonth()+1)+"-"+date1.getDate();
			startTime=0;
			endTime=24; 	
//			alert("type:"+type+"date:"+date+"startTime:"+startTime+"endTime:"+endTime);
		}else{
			date="";
		}
	}else{
		date="";
	}
}

//导出
function exportDatabase(){
	if(''!=$("#startTime").val() && ''!=$("#endTime").val()){
		getTime();
		institution_name=$("#institution_name").val();
		user_id=$("#user_id").val();
		restype=$("#restype").val();
		dbname=$("#database").val();
		var url="../databaseAnalysis/exportDatabase.do?" +
			"institution_name="+institution_name +"&user_id="+user_id+
			"&source_db="+restype+
			"&product_source_code="+dbname+
			"&date="+date+"&startTime="+startTime+"&endTime="+endTime;
		window.location.href=encodeURI(encodeURI(url));
	}else{
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
}

//数据库选择  全选 全不选
function checkAll(){
	$("input[name='checkOne']").prop("checked",$("#checkAll").prop("checked"));
	moreOrSimple();
}

//单选
function checkOne(){
	$("#checkAll").prop("checked",$("input[name='checkOne']").length==$("input[name='checkOne']:checked").length);
	moreOrSimple();
}

//根据数据库选择情况  展示指标数量
function moreOrSimple(){
	if($("input[name='checkOne']:checked").length>1 || $("input[name='checkOne']:checked").length==0){
		$("#simple").show();
		$("#more").hide();
		
		checkitem();
		
	}else{
		$("#simple").hide();
		$("#more").show();
		
		checkitem_more();
	}
} 

//指标单选
function checkitem(){
	var urlType=new Array();
	var database_name=new Array();
	var datas=new Array();
	var url=$("#urltype").val();
	urlType.push(url);
	
	if(url==1){
		datas.push("浏览数");
	}else if(url==2){
		datas.push("下载数");
	}else if(url==3){
		datas.push("检索数");
	}
	
	
	$("input[name='checkOne']:checked").each(function(){
		database_name.push($(this).val());
	});
	
	
	line(urlType,database_name,datas);
}

//多选指标  全部
function checksource(){

	$("input[name='item']").prop("checked",$("#checkallsource").prop("checked"));
	checkitem_more();
}

//指标多选
function checkitem_more(){
	
	var urlType=new Array();
	var database_name=new Array();
	var datas=new Array();
	
	$("#checkallsource").prop("checked",$("input[name='item']").length==$("input[name='item']:checked").length);
	$("input[name='item']:checked").each(function(){
		urlType.push($(this).val());
		
		if($(this).val()==1){
			datas.push("浏览数");
		}else if($(this).val()==2){
			datas.push("下载数");
		}else if($(this).val()==3){
			datas.push("检索数");
		}
		
	});
	$("input[name='checkOne']:checked").each(function(){
		database_name.push($(this).val());
	});
	
	line(urlType,database_name,datas);
}


function getDatabaseBySourceCode(code){
	
	$("#database").empty();
	$("#database").append("<option value=''>--请选择数据库名称--</option>");

	if(""!=code && null!=code && undefined!=code){
		$.ajax({
			type : "POST",
			url : "../databaseAnalysis/getDatabaseBySourceCode.do",
			data : {"code":code},
			dataType : "json",
			success : function(data) {
				$(data).each(function(index,item) {
					$("#database").append("<option value='"+item.productSourceCode+"'>"+item.tableName+"</option>");
				});
			}
		});
	}
	
}


function keyword(){
	$("#institution_name").focus(function(){	
		$("#searchsug").show();
		$("#institution_name").keyup(function(event){
			$("#searchsug").show();
			var keywords=$("#institution_name").val();						
			$("#searchsug li").remove();			
			$.post("../databaseAnalysis/getAllInstitution.do",{"institution":keywords},function(data){
				  $("#searchsug ul li").remove();
				var list=eval(data);
				for(var i=0;i<list.length;i++){
					var li="<li data-key=\""+list[i]+"\" style=\"line-height: 14px;text-align:left;\" onclick=\"text_show(this);\" ><span>"+list[i]+"</span></li>";
					$("#searchsug ul").append(li);			
					$("#searchsug ul li").mouseover(function(){
						$("#searchsug ul li").removeAttr("class");
						$(this).attr("class","bdsug-s"); 
								});	
						}
					});
				});
			});
}

function text_show(data){
	$("#institution_name").val($(data).text());
	$("#searchsug").css("display","none");
	
	$.post("../databaseAnalysis/getDB_SourceByInstitution.do",
			{"institution":$("#institution_name").val()},
			function(data){
				$("#restype").empty();
				$("#restype").append("<option value=''>--请选择数据来源--</option>");
				$(data).each(function(index,item) {
					$("#restype").append("<option value='"+item.dbSourceCode+"'>"+item.dbSourceName+"</option>");
				});
			});
}





