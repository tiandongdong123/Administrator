var pageIndex;
var singmore;
var restype;
var urltype;
var starttime;
var endtime;
var username;
var unitname;
var product_source_code;
var source_db;
var pagesize=10;

$(function(){
	getline(1);
	gettable(1);
	$("#single").show();
	$("#more").hide();
	keyword();
	$(document).click(function(){
	    $("#searchsug").hide();
	});
})


function changeres(){
	restype=$("#restype").find("option:selected").text();
	if(restype!='--请选择资源类型--'){
		$("#single").hide();
		$("#more").show();
		$("#singmore").val("0");
	}else{
		$("#singmore").val("1");
		$("#single").show();
		$("#more").hide();
		$("input[name=item]").prop("checked",false);
		$("#checkallsource").prop("checked",false);
		
	}
}


$(function(){
	$('#pageChange').change(function(){
		restype=$("#restype").find("option:selected").text();
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
		if(restype=='期刊'||restype=='会议'||restype=='学位'){
			num=1;
		}else{
			num=0;
		}
		if(restype=='学位')
		{
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
			}, function(res){
				var html="";
				var htmltitle=""
				var htmlbody=""
				if(restype=='期刊'){
					htmltitle="<th>期刊名称</th>";
				}else if(restype=='会议'){
					htmltitle="<th>会议名称</th>";
				}else if(restype=='学位'){
					htmltitle='<th>授予学位的机构名称</th>';
				}
				html=	"<tr>" +
					"<th><input type='checkbox' name='rscheck' onclick='checkAll();'></th>" +
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
					if(restype=='期刊'||restype=='会议'||restype=='学位'){
						htmlbody="<td>"+res.pageRow[i].title+"</td>";
					}
					html+="<tr>" +
						"<th><input type='checkbox' id='rstype' value="+res.pageRow[i].sourceTypeName+" onclick='checkboxchange();'></th>" +
						"<td>"+id+"</td>" +htmlbody+//序号
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +//资源类型
						"<td>"+res.pageRow[i].searchNum+"</td>" +//检索数
						"<td>"+res.pageRow[i].browseNum+"</td>" +//浏览数
						"<td>"+res.pageRow[i].downloadNum+"</td>" +//下载数
						"<td>"+res.pageRow[i].skipNum+"</td>" +//跳转数。。。。。
						"<td>"+res.pageRow[i].subscibeNum+"</td>" +//订阅数
						"<td>"+res.pageRow[i].collectNum+"</td>" +//收藏数
						"<td>"+res.pageRow[i].noteNum+"</td>" +//笔记数
						"<td>"+res.pageRow[i].shareNum+"</td>" +//分享数
						"<td>"+res.pageRow[i].exportNum+"</td>" +//导出数
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
		}  else{
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
				num:num,
			}, function(res){
				var html="";
				var htmltitle=""
				var htmlbody=""
				if(restype=='期刊'){
					htmltitle="<th>期刊名称</th>";
				}else if(restype=='会议'){
					htmltitle="<th>会议名称</th>";
				}else if(restype=='学位'){
					htmltitle='<th>授予学位的机构名称</th>';
				}
				html=	"<tr>" +
					"<th><input type='checkbox' name='rscheck' onclick='checkAll();'></th>" +
					"<th>序号</th>" +htmltitle+
					"<th>资源类型</th>" +
					"<th>检索数</th>" +
					"<th  >浏览数</th>" +
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
					if(restype=='期刊'||restype=='会议'||restype=='学位'){

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
						"<td>"+id+"</td>" +htmlbody+//序号
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +//资源类型   retrieval
						"<td>"+res.pageRow[i].searchNum+"</td>" +//检索数
						"<td>"+res.pageRow[i].browseNum+"</td>" +//浏览数
						"<td>"+res.pageRow[i].downloadNum+"</td>" +//下载数
						"<td>"+res.pageRow[i].skipNum+"</td>" +//跳转数
						"<td>"+res.pageRow[i].subscibeNum+"</td>" +//订阅数
						"<td>"+res.pageRow[i].collectNum+"</td>" +//收藏数
						"<td>"+res.pageRow[i].noteNum+"</td>" +//笔记数
						"<td>"+res.pageRow[i].shareNum+"</td>" +//分享数
						"<td>"+res.pageRow[i].exportNum+"</td>" +//导出数
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
	})
})
//分页显示
function gettable(curr){
	if($("#starttime").val() == ''|| $("#endtime").val() == '') {
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
	else{
		pageIndex=curr;
		getTime();
		url="";
		restype=$("#restype").find("option:selected").text();
		urltype=$("#urltype").find("option:selected").val();
		starttime = $("#starttime").val();
		endtime=$("#endtime").val();
		username=$("#username").val();
		unitname=$("#institution_name").val();
		source_db=$("#source_db").val();
		product_source_code=$("#database").find("option:selected").val();
		num = "";
		if(restype=='期刊'||restype=='会议'||restype=='学位'){
			num=1;
		}else{
			num=0;
		}
		if(restype=='学位')
		{
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
			}, function(res){
				var html="";
				var htmltitle=""
				var htmlbody=""
				if(restype=='期刊'){
					htmltitle="<th>期刊名称</th>";
				}else if(restype=='会议'){
					htmltitle="<th>会议名称</th>";
				}else if(restype=='学位'){
					htmltitle='<th>授予学位的机构名称</th>';
				}
				html=	"<tr>" +
					"<th><input type='checkbox' name='rscheck' onclick='checkAll();'></th>" +
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
					if(restype=='期刊'||restype=='会议'||restype=='学位'){
						htmlbody="<td>"+res.pageRow[i].title+"</td>";
					}
					html+="<tr>" +
						"<th><input type='checkbox' id='rstype' value="+res.pageRow[i].sourceTypeName+" onclick='checkboxchange();'></th>" +
						"<td>"+id+"</td>" +htmlbody+//序号
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +//资源类型
						"<td>"+res.pageRow[i].searchNum+"</td>" +//检索数
						"<td>"+res.pageRow[i].browseNum+"</td>" +//浏览数
						"<td>"+res.pageRow[i].downloadNum+"</td>" +//下载数
						"<td>"+res.pageRow[i].skipNum+"</td>" +//跳转数。。。。。
						"<td>"+res.pageRow[i].subscibeNum+"</td>" +//订阅数
						"<td>"+res.pageRow[i].collectNum+"</td>" +//收藏数
						"<td>"+res.pageRow[i].noteNum+"</td>" +//笔记数
						"<td>"+res.pageRow[i].shareNum+"</td>" +//分享数
						"<td>"+res.pageRow[i].exportNum+"</td>" +//导出数
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
		}  else{
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
				num:num,
			}, function(res){
				var html="";
				var htmltitle=""
				var htmlbody=""
				if(restype=='期刊'){
					htmltitle="<th>期刊名称</th>";
				}else if(restype=='会议'){
					htmltitle="<th>会议名称</th>";
				}else if(restype=='学位'){
					htmltitle='<th>授予学位的机构名称</th>';
				}
				html=	"<tr>" +
					"<th><input type='checkbox' name='rscheck' onclick='checkAll();'></th>" +
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
					if(restype=='期刊'||restype=='会议'||restype=='学位'){

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
						"<td>"+id+"</td>" +htmlbody+//序号
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +//资源类型   retrieval
						"<td>"+res.pageRow[i].searchNum+"</td>" +//检索数
						"<td>"+res.pageRow[i].browseNum+"</td>" +//浏览数
						"<td>"+res.pageRow[i].downloadNum+"</td>" +//下载数
						"<td>"+res.pageRow[i].skipNum+"</td>" +//跳转数
						"<td>"+res.pageRow[i].subscibeNum+"</td>" +//订阅数
						"<td>"+res.pageRow[i].collectNum+"</td>" +//收藏数
						"<td>"+res.pageRow[i].noteNum+"</td>" +//笔记数
						"<td>"+res.pageRow[i].shareNum+"</td>" +//分享数
						"<td>"+res.pageRow[i].exportNum+"</td>" +//导出数
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

	}


};

function getline(initial){
	var checkbox=$("#rstype:checked");
	var rstnames=new Array();
	var urls = new Array();
	getTime();
	$("input[name=item]").each(function() {  
        if ($(this).is(':checked')) {  
        	urls.push($(this).val());  
        } 
	});
	
	var singmore = $("#singmore").val();
	var restype=$("#restype").find("option:selected").text();
	var urltype=$("#urltype").find("option:selected").val();
	var starttime = $("#starttime").val();
	var endtime=$("#endtime").val();
	var username=$("#username").val();
	var unitname=$("#institution_name").val();
	var source_db=$("#source_db").val();
	var product_source_code=$("#database").val();
	
	var num=0;
	if(restype=='期刊'||restype=='会议'||restype=='学位'){
		num=1;
	}
	if(restype=='--请选择资源类型--' && initial==2){
		getLineByCheckMore(checkbox);
	}else{
		if(restype=='学位')
		{
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
				'urls':urls,
				'singmore':singmore,
				'num':num,
			},
			dataType : "json",
			success : function(data) {
				tree(data);
				pie(data);
			}
			});
		
		}else{
			$.ajax( {  
				type : "POST",  
				url : "../resourceTypeStatistics/getline.do",
				data : {
					'userId':username,
					'source_db':source_db,
					'starttime' : starttime,
					'endtime':endtime,
					'operate_type':urltype,
					'product_source_code':product_source_code,
					'institutionName':unitname,
					'sourceTypeName':restype,
					'urls':urls,
					'singmore':singmore,
					'num':num,
				},
				dataType : "json",
				success : function(data) {
					tree(data);
					pie(data);
				}
				});
	}
		
	}
	
}

function tree(data){
	if($("#starttime").val() == ''|| $("#endtime").val() == '') {
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
	else{
		var myChart = echarts.init(document.getElementById('line'));
		option = {
			tooltip : {
				trigger: 'axis'
			},
			legend: {
				data:data.title
			},
			calculable : true,
			xAxis : [
				{
					type : 'category',
					boundaryGap : false,
					data : data.date
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
	}
}

function pie(data){
	if($("#starttime").val() == ''|| $("#endtime").val() == '') {
		layer.msg("请选择前后统计时间!",{icon: 2});
	}
	else{
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
}

function checksource(){
		if ($("#checkallsource").is(':checked')) {
			$("input[name=item]").prop("checked", true);
		} else {
			$("input[name=item]").prop("checked", false);
		}
}

function checkitem(){
	getline(2);
}
//导出
function exportresource(){
	getTime();
	if($("#startTime").val() == ''|| $("#endTime").val() == '') {
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
		$("#singmore").val("1");
		$("#single").show();
		$("#more").hide();
		$("input[name=item]").prop("checked",false);
		$("#checkallsource").prop("checked",false);
	}else{
		$("#single").hide();
		$("#more").show();
		$("#singmore").val("0");
	}
	
	getLineByCheckMore(checkbox);
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


function getLineByCheckMore(checkbox){
	
	var rstnames=new Array();
	var urls = new Array();
	
	checkbox.each(function(){
		rstnames.push($(this).val());
	});
	
	$("input[name=item]").each(function() {  
        if ($(this).is(':checked')) {  
        	urls.push($(this).val());  
        } 
	});
	var singmore = $("#singmore").val();
	var urltype=$("#urltype").find("option:selected").val();
	var starttime = $("#startTime").val();
	var endtime=$("#endtime").val();
	var username=$("#username").val();
	var unitname=$("#institution_name").val();
	var source_db=$("#source_db").val();
	var product_source_code=$("#database").val();
	if(rstnames.length>0){
		if(rstnames.length==1 && rstnames[0]=='学位')
		{
		$.ajax( {  
			type : "POST",  
			url : "../resourceTypeStatistics/getLineBycheckMore.do",
			data : {
				'starttime' : starttime,
				'endtime':endtime,
				'userId':username,
				'operate_type':urltype,
				'institution_name':unitname,
				'source_db':source_db,
				'product_source_code':product_source_code,
				'rstnames':rstnames,
				'urls':urls,
				'singmore':singmore
			},
			dataType : "json",
			success : function(data) {
				tree(data);
				pie(data);
			}
			});
		}
	else{
		$.ajax( {  
			type : "POST",  
			url : "../resourceTypeStatistics/getLineBycheckMore.do",
			data : {
				'starttime' : starttime,
				'endtime':endtime,
				'userId':username,
				'operate_type':urltype,
				'institutionName':unitname,
				'source_db':source_db,
				'product_source_code':product_source_code,
				'rstnames':rstnames,
				'urls':urls,
				'singmore':singmore
			},
			dataType : "json",
			success : function(data) {
				tree(data);
				pie(data);
			}
			});
	}
}else{
		$("#singmore").val("1");
		$("#single").show();
		$("#more").hide();
		$("input[name=item]").prop("checked",false);
		$("#checkallsource").prop("checked",false);

		getline(1);
}
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
	startTime=$("#startTime").val();
	endTime=$("#endTime").val();

	if((startTime!=''&& endTime!='')&&(startTime != null && endTime != null)){
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




