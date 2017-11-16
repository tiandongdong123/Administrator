var pageIndex;
var singmore;
var restype;
var urltype;
var date;
var starttime;
var endtime;
var username;
var unitname;
var product_source_code;
var source_db;
var database_name;
var pagesize=10;

$(function(){
	gettable(1);
	keyword();
	$(document).click(function(){
	    $("#searchsug").hide();
	});
})


 function changeres(){
 	restype=$("#restype").find("option:selected").val();
 	if(restype!=''){
 		$("#single").hide();
 		$("#more").show();
		$("input[name=item]").prop("checked",true);
		$("#checkallsource").prop("checked",true);
 		$("#singmore").val("0");
 	}else{
 		$("#singmore").val("1");
 		$("#single").show();
 		$("#more").hide();
		// $("input[name=item]").prop("checked",true);
 		// $("#checkallsource").prop("checked",true);

 	}
 }


$(function(){
	$('#pageChange').change(function(){
		restype=$("#restype").find("option:selected").val();
		urltype=$("#urltype").find("option:selected").val();
		starttime = $("#starttime").val();
		endtime=$("#endtime").val();
		username=$("#username").val();
		unitname=$("#institution_name").val();
		source_db=$("#source_db").val();
		product_source_code=$("#database").find("option:selected").val();
		var $number=pagesize*(parseInt($('.laypage_curr').text())-1)+1;
		pagesize=parseInt($(this).find('option:selected').val());
		pagenum=curr=parseInt($number/pagesize)+1;
		if(restype=='perio'||restype=='conference'||restype=='degree')
		{
			num=1;
			$.post("../resourceTypeStatistics/gettable.do", {
				pagenum: curr,//向服务端传的参数
				pagesize :pagesize,
				institutionName : unitname,
				userId:username,
				source_db:source_db,
				product_source_code:product_source_code,
				sourceTypeName:restype,
				starttime : starttime,
				endtime:endtime,
				operate_type:urltype,
				num:num,
				date:date,
			}, function(res){
				var html="";
				var htmltitle=""
				var htmlbody=""
				if(restype=='perio'){
					htmltitle="<th>期刊名称</th>";
				}else if(restype=='conference'){
					htmltitle="<th>会议名称</th>";
				}else if(restype=='degree'){
					htmltitle='<th>授予学位的机构名称</th>';
				}
				html=	"<tr>" +
					"<th>序号</th>" +htmltitle+
					"<th>资源类型</th>" +
					"<th>检索数</th>" +
					"<th>浏览数</th>" +
					"<th>下载数</th>" +
					"<th>跳转数</th>" +
					"<th>订阅数</th>" +
					"<th>收藏数</th>" +
					"<th>笔记数</th>" +
					"<th>分享数</th>" +
					"<th>导出数</th>" +
					"</tr>"
				for(var i =0;res.pageRow[i];i++){
					id = pagesize*(curr-1)+i+1;
					if(restype=='perio'||restype=='conference'||restype=='degree'){
						htmlbody="<td>"+res.pageRow[i].title+"</td>";
					}
					html+="<tr>" +
						"<td>"+id+"</td>" +htmlbody+//序号
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +//资源类型
						"<td>"+res.pageRow[i].sum3+"</td>" +//检索数
						"<td>"+res.pageRow[i].sum1+"</td>" +//浏览数
						"<td>"+res.pageRow[i].sum2+"</td>" +//下载数
						"<td>"+res.pageRow[i].sum8+"</td>" +//跳转数
						"<td>"+res.pageRow[i].sum9+"</td>" +//订阅数
						"<td>"+res.pageRow[i].sum5+"</td>" +//收藏数
						"<td>"+res.pageRow[i].sum7+"</td>" +//笔记数
						"<td>"+res.pageRow[i].sum4+"</td>" +//分享数
						"<td>"+res.pageRow[i].sum6+"</td>" +//导出数
						"</tr>";

				}
				document.getElementById('databody').innerHTML = html;
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
							gettable(obj.curr);
						}
					}
				});
			});
		}
		else{
			num=0;
			$.post("../resourceTypeStatistics/gettable.do", {
				pagenum: curr,//向服务端传的参数
				pagesize :pagesize,
				institutionName:unitname,
				userId:username,
				source_db:source_db,
				product_source_code:product_source_code,
				sourceTypeName:restype,
				starttime : starttime,
				endtime:endtime,
				operate_type:urltype,
				date:date,
				num:num,
			}, function(res){

				var html="";
				var htmltitle=""
				var htmlbody=""
				if(restype=='perio'){
					htmltitle="<th>期刊名称</th>";
				}else if(restype=='conference'){
					htmltitle="<th>会议名称</th>";
				}else if(restype=='degree'){
					htmltitle='<th>授予学位的机构名称</th>';
				}
				html=	"<tr>" +
					"<th><input type='checkbox' name='rscheck' onclick='checkAll();'></th>" +
					"<th>序号</th>" +
					"<th>资源类型</th>" +htmltitle+
					"<th>检索数</th>" +
					"<th>浏览数</th>" +
					"<th>下载数</th>" +
					"<th>跳转数</th>" +
					"<th>订阅数</th>" +
					"<th>收藏数</th>" +
					"<th>笔记数</th>" +
					"<th>分享数</th>" +
					"<th>导出数</th>" +
					"</tr>"
				for(var i =0;res.pageRow[i];i++){
					$(".showPage").css("display","block");
					id = pagesize*(curr-1)+i+1;
					if(restype=='perio'||restype=='conference'||restype=='degree'){

						var title;
						if(res.pageRow[i].title==null){
							title="";
						}else{
							title=res.pageRow[i].title;
						}

						htmlbody="<td>"+title+"</td>";
					}
					html+="<tr>" +
						"<th><input type='checkbox' id='rstype' value="+res.pageRow[i].sourceTypeName+" onclick='checkboxchange();'></th>" +
						"<td>"+id+"</td>" +
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +//资源类型   retrieval
						htmlbody+//序号
						"<td>"+res.pageRow[i].sum3+"</td>" +//检索数
						"<td>"+res.pageRow[i].sum1+"</td>" +//浏览数
						"<td>"+res.pageRow[i].sum2+"</td>" +//下载数
						"<td>"+res.pageRow[i].sum8+"</td>" +//跳转数
						"<td>"+res.pageRow[i].sum9+"</td>" +//订阅数
						"<td>"+res.pageRow[i].sum5+"</td>" +//收藏数
						"<td>"+res.pageRow[i].sum7+"</td>" +//笔记数
						"<td>"+res.pageRow[i].sum4+"</td>" +//分享数
						"<td>"+res.pageRow[i].sum6+"</td>" +//导出数
						"</tr>";
				}
				document.getElementById('databody').innerHTML = html;
				var totalRow = res.totalRow;
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
							gettable(obj.curr);
						}
					}
				});
			});
		}
	})
})
//分页显示   表格
function gettable(curr){
	if($("#starttime").val() == ''|| $("#endtime").val() == '') {
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
	else{
		pageIndex=curr;
		getTime();
		url="";
		restype=$("#restype").find("option:selected").val();
		urltype=$("#urltype").find("option:selected").val();
		starttime = $("#starttime").val();
		endtime=$("#endtime").val();
		username=$("#username").val();
		unitname=$("#institution_name").val();
		source_db=$("#source_db").val();
		product_source_code=$("#database").find("option:selected").val();
		num = "";
		if(restype=='perio'||restype=='conference'||restype=='degree')
		{
			num=1;
			$.post("../resourceTypeStatistics/gettable.do", {
				pagenum: curr,//向服务端传的参数
				pagesize :pagesize,
				institutionName : unitname,
				userId:username,
				source_db:source_db,
				product_source_code:product_source_code,
				sourceTypeName:restype,
				starttime : starttime,
				endtime:endtime,
				operate_type:urltype,
				num:num,
				date:date,
			}, function(res){
				var html="";
				var htmltitle=""
				var htmlbody=""
				if(restype=='perio'){
					htmltitle="<th>期刊名称</th>";
				}else if(restype=='conference'){
					htmltitle="<th>会议名称</th>";
				}else if(restype=='degree'){
					htmltitle='<th>授予学位的机构名称</th>';
				}
				html=	"<tr>" +
					"<th>序号</th>" +
					"<th>资源类型</th>" +htmltitle+
					"<th>检索数</th>" +
					"<th>浏览数</th>" +
					"<th>下载数</th>" +
					"<th>跳转数</th>" +
					"<th>订阅数</th>" +
					"<th>收藏数</th>" +
					"<th>笔记数</th>" +
					"<th>分享数</th>" +
					"<th>导出数</th>" +
					"</tr>"
				for(var i =0;res.pageRow[i];i++){
					$(".showPage").css("display","block");
					id = pagesize*(curr-1)+i+1;
					if(restype=='perio'||restype=='conference'||restype=='degree'){
						htmlbody="<td>"+res.pageRow[i].title+"</td>";
					}
					html+="<tr>" +
						"<td>"+id+"</td>" +
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +//资源类型
						htmlbody+//序号
						"<td>"+res.pageRow[i].sum3+"</td>" +//检索数
						"<td>"+res.pageRow[i].sum1+"</td>" +//浏览数
						"<td>"+res.pageRow[i].sum2+"</td>" +//下载数
						"<td>"+res.pageRow[i].sum8+"</td>" +//跳转数
						"<td>"+res.pageRow[i].sum9+"</td>" +//订阅数
						"<td>"+res.pageRow[i].sum5+"</td>" +//收藏数
						"<td>"+res.pageRow[i].sum7+"</td>" +//笔记数
						"<td>"+res.pageRow[i].sum4+"</td>" +//分享数
						"<td>"+res.pageRow[i].sum6+"</td>" +//导出数
						"</tr>";

				}
				document.getElementById('databody').innerHTML = html;
				var totalRow = res.totalRow;
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
							gettable(obj.curr);
						}
					}
				});
				$("#rstype:first").prop("checked",true);
				$("#simple").hide();
				$("#more").show();
				$("input[name=item]").prop("checked",true);
				$("#checkallsource").prop("checked",true);
			});
		}
		else{

			num=0;
			$.post("../resourceTypeStatistics/gettable.do", {
				pagenum: curr,//向服务端传的参数
				pagesize :pagesize,
				institutionName:unitname,
				userId:username,
				source_db:source_db,
				product_source_code:product_source_code,
				sourceTypeName:restype,
				starttime : starttime,
				endtime:endtime,
				operate_type:urltype,
				date:date,
				database_name:database_name,
				num:num,
			}, function(res){

				var html="";
				var htmltitle=""
				var htmlbody=""
				if(restype=='perio'){
					htmltitle="<th>期刊名称</th>";
				}else if(restype=='conference'){
					htmltitle="<th>会议名称</th>";
				}else if(restype=='degree'){
					htmltitle='<th>授予学位的机构名称</th>';
				}
				html=	"<tr>" +
					"<th><input type='checkbox' name='rscheck' onclick='checkAll()'></th>" +//onclick='checkAll()'
					"<th>序号</th>" +htmltitle+
					"<th>资源类型</th>" +
					"<th>检索数</th>" +
					"<th>浏览数</th>" +
					"<th>下载数</th>" +
					"<th>跳转数</th>" +
					"<th>订阅数</th>" +
					"<th>收藏数</th>" +
					"<th>笔记数</th>" +
					"<th>分享数</th>" +
					"<th>导出数</th>" +
					"</tr>"
				for(var i =0;res.pageRow[i];i++){
					$(".showPage").css("display","block");
					id = pagesize*(curr-1)+i+1;
					if(restype=='perio'||restype=='conference'||restype=='degree'){

						var title;
						if(res.pageRow[i].title==null){
							title="";
						}else{
							title=res.pageRow[i].title;
						}

						htmlbody="<td>"+title+"</td>";
					}
					var str = res.pageRow[i].resourceTypeCode;

					var rstr = str.replace(" ",'!');
					html+="<tr>" +
						"<th><input type='checkbox' name='rscheckr' id='rstype' value="+ rstr +" onclick='checkboxchange()'></th>" +//onclick='checkboxchange(); '
						"<td>"+id+"</td>" +htmlbody+//序号
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +//资源类型   retrieval
						"<td>"+res.pageRow[i].sum3+"</td>" +//检索数
						"<td>"+res.pageRow[i].sum1+"</td>" +//浏览数
						"<td>"+res.pageRow[i].sum2+"</td>" +//下载数
						"<td>"+res.pageRow[i].sum8+"</td>" +//跳转数
						"<td>"+res.pageRow[i].sum9+"</td>" +//订阅数
						"<td>"+res.pageRow[i].sum5+"</td>" +//收藏数
						"<td>"+res.pageRow[i].sum7+"</td>" +//笔记数
						"<td>"+res.pageRow[i].sum4+"</td>" +//分享数
						"<td>"+res.pageRow[i].sum6+"</td>" +//导出数
						"</tr>";
				}
				document.getElementById('databody').innerHTML = html;
				var totalRow = res.totalRow;
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
							gettable(obj.curr);
						}
					}
				});
				$("#rstype:first").prop("checked",true);
				$("#simple").hide();
				$("#more").show();
				$("input[name=item]").prop("checked",true);
				$("#checkallsource").prop("checked",true);
			});
		}
	}
};


function getline(){
	if($("#starttime").val() == ''|| $("#endtime").val() == '') {
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
	else {
		getTime();
		var checkbox=$("#rstype:checked");
		var rstnames=new Array();
		var urls = new Array();
		//资源选项卡多选
		if( $("#restype").val() == "" || $("#restype").val() == null){
			//表格单选
			if($("input[name='rscheckr']:checked").length>1 || $("input[name='rscheckr']:checked").length==0){
				urls.push($("#urltype").val());
				if(urls==1){
					rstnames.push("浏览数");
				}else if(urls==2){
					rstnames.push("下载数");
				}else if(urls==3){
					rstnames.push("检索数");
				}else if(urls==4){
					rstnames.push("分享数");
				}else if(urls==5){
					rstnames.push("收藏数");
				}else if(urls==6){
					rstnames.push("导出数");
				}else if(urls==7){
					rstnames.push("笔记数");
				}else if(urls==8){
					rstnames.push("跳转数");
				}else if(urls==9){
					rstnames.push("订阅数");
				}
				singmore = 0 ;
			} else{
				$("#checkallsource").prop("checked",$("input[name='item']").length==$("input[name='item']:checked").length);
				$("input[name='item']:checked").each(function(){
					urls.push($(this).val());
					if($(this).val()==1){
						rstnames.push("浏览数");
					}else if($(this).val()==2){
						rstnames.push("下载数");
					}else if($(this).val()==3){
						rstnames.push("检索数");
					}else if($(this).val()==4){
						rstnames.push("分享数");
					}else if($(this).val()==5){
						rstnames.push("收藏数");
					}else if($(this).val()==6){
						rstnames.push("导出数");
					}else if($(this).val()==7){
						rstnames.push("笔记数");
					}else if($(this).val()==8){
						rstnames.push("跳转数");
					}else if($(this).val()==9) {
						rstnames.push("订阅数");
					}
				});
				singmore = 1 ;
			}

		}else{
			$("#checkallsource").prop("checked",$("input[name='item']").length==$("input[name='item']:checked").length);
			$("input[name='item']:checked").each(function(){
				urls.push($(this).val());
				if($(this).val()==1){
					rstnames.push("浏览数");
				}else if($(this).val()==2){
					rstnames.push("下载数");
				}else if($(this).val()==3){
					rstnames.push("检索数");
				}else if($(this).val()==4){
					rstnames.push("分享数");
				}else if($(this).val()==5){
					rstnames.push("收藏数");
				}else if($(this).val()==6){
					rstnames.push("导出数");
				}else if($(this).val()==7){
					rstnames.push("笔记数");
				}else if($(this).val()==8){
					rstnames.push("跳转数");
				}else if($(this).val()==9) {
					rstnames.push("订阅数");
				}
			});
			//资源选项卡单选
			singmore = 1 ;
		}

		var restype=$("#restype").find("option:selected").val();
		var urltype=$("#urltype").find("option:selected").val();
		var starttime = $("#starttime").val();
		var endtime=$("#endtime").val();
		var username=$("#username").val();
		var unitname=$("#institution_name").val();
		var source_db=$("#source_db").val();
		var product_source_code=$("#database").val();
		var resourcetypeName=new Array();

		$("input[id='rstype']:checked").each(function(){
			resourcetypeName.push($(this).val().replace('!',' '));
		});
		$.ajax( {
			type : "POST",
			url : "../resourceTypeStatistics/getline.do",
			data : {
				'institutionName':unitname,
				'starttime' : starttime,
				'endtime':endtime,
				'userId':username,
				'operate_type':urltype,
				'source_db':source_db,
				'sourceTypeName':restype,
				'product_source_code':product_source_code,
				'database_name':resourcetypeName,
				'urls':urls,
				'singmore':singmore,
				'date' : date,
			},
			dataType : "json",
			success : function(data) {
				if (singmore==0) {
					var myChart = echarts.init(document.getElementById('line'));
					option = {
						tooltip : {
							trigger: 'axis'
						},
						legend: {
							data:data.title
						},
						toolbox: {
							show : true,
							feature : {
								// mark : {show: true},
								// dataView : {show: true, readOnly: false},
								// magicType : {show: true, type: ['line']},
								// restore : {show: true},
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

					for(var i =0;i<data.title.length;i++){
						var name = data.title[i];
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
					pie(data);

				}
				else{
					var myChart = echarts.init(document.getElementById('line'));
					option = {
						tooltip : {
							trigger: 'axis'
						},
						legend: {
							data:rstnames,
						},
						toolbox: {
							show : true,
							feature : {
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

					for(var i =0;i<rstnames.length;i++){
						var name = rstnames[i];
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
					pie(data);
				}
			}
		});
	}
}

function checksource(){
		if ($("#checkallsource").is(':checked')) {
			$("input[name=item]").prop("checked", true);
		} else {
			$("input[name=item]").prop("checked", true);
		}
}


//导出
function exportresource(){
	getTime();
	if($("#starttime").val() == ''|| $("#endtime").val() == '') {
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
	else{
		var url="../resourceTypeStatistics/exportresourceType.do?" +
			"starttime="+starttime+"&endtime="+endtime+"&userId="+username+"&operate_type="+urltype+
			"&institutionName="+unitname+"&sourceTypeName="+restype+"&num="+num+"&source_db="+source_db+
			"&product_source_code="+product_source_code;
		window.location.href=encodeURI(encodeURI(url));
	}
}

//checkbox联动
function checkboxchange(){
	var checkbox=$("#rstype:checked");
	if(checkbox.length>1){
		$("#single").show();
		$("#more").hide();
	}else{
		$("#single").hide();
		$("#more").show();
		// $("input[name=item]").prop("checked",true);
		// $("#checkallsource").prop("checked",true);
	}
	//getline();
}

function checkAll(){
	if($("[name='rscheck']").is(':checked')){
		$("input[id=rstype]").each(function(){
			$(this).prop("checked", "checked");
		});
	}else{
		$("input[id=rstype]").each(function(){
			$(this).removeAttr("checked");
		});
	}
	checkboxchange();
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
				$("#source_db").empty();
				$("#source_db").append("<option value=''>--请选择数据来源--</option>");
				$(data).each(function(index,item) {
					$("#source_db").append("<option value='"+item.dbSourceCode+"'>"+item.dbSourceName+"</option>");
				});
			});
}
/**判断是否用小时为统计单位*/
function getTime(){
	starttime=$("#starttime").val();
	endTime=$("#endtime").val();

	if((starttime!=''&& endtime!='')&&(starttime != null && endtime != null)){
		var date1= new Date(endtime.replace(/-/g,"/"));
		var date2= new Date(starttime.replace(/-/g,"/"));
		var between = date1-date2;
		if(between<=0){
			date=date1.getFullYear()+"-"+(date1.getMonth()+1)+"-"+date1.getDate();
			starttime=0;
			endtime=24;
		}else{
			date="";
		}
	}else{
		date="";
	}
}
function pie(data){
	var myChart = echarts.init(document.getElementById('pie'));
	var urltype=$("#urltype").find("option:selected").text();
	option = {
		tooltip : {
			trigger: 'item',
			formatter: "{b} : {c} ({d}%)"
		},
		legend: {
			orient : 'vertical',
			x : 'left',
			data:data.title
		},
		calculable : true,
		series : [
			{
				type:'pie',
				radius : '60%',
				center: ['50%', '60%'],
				data:[
				]
			}
		]
	};


	for(var i =0;i<data.title.length;i++){
		var name = data.title[i];
		var num=new Array();
		num =data.content[name];
		var val = 0;
		for(var k =0;k<num.length;k++){
			val=val+parseInt(num[k]);
		}
		option.series[0].data.push(
			{
				value:val,
				name:name
			}
		)
	}


	myChart.setOption(option);
}

function checksource(){
	$("input[name='item']").prop("checked",$("#checkallsource").prop("checked"));
	var checkbox=$("#rstype:checked");
	var rstnames=new Array();
	var urls = new Array();

	$("#checkallsource").prop("checked",$("input[name='item']").length==$("input[name='item']:checked").length);
	$("input[name='item']:checked").each(function(){
		urlType.push($(this).val());

		if(urls==1){
			rstnames.push("浏览数");
		}else if(urls==2){
			rstnames.push("下载数");
		}else if(urls==3){
			rstnames.push("检索数");
		}else if(urls==4){
			rstnames.push("分享数");
		}else if(urls==5){
			rstnames.push("收藏数");
		}else if(urls==6){
			rstnames.push("导出数");
		}else if(urls==7){
			rstnames.push("笔记数");
		}else if(urls==8){
			rstnames.push("跳转数");
		}else if(urls==9){
			rstnames.push("订阅数");
		}
	});
	$("input[id='rstype']:checked").each(function(){
		database_name.push($(this).val());
	});
}



