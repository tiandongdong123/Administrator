var pageIndex;
var restype;
var singmore;
var restype;
var urltype;
var starttime;
var endtime;
var username;
var unitname;

$(function(){
	getline(1);
	gettable(1);
	$("#single").show();
	$("#more").hide();
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

//分页显示
function gettable(curr){
	pageIndex=curr;
	url="";
	restype=$("#restype").find("option:selected").text();
	urltype=$("#urltype").find("option:selected").val();
	starttime = $("#starttime").val();
	endtime=$("#endtime").val();
	username=$("#username").val();
	unitname=$("#unitname").val();
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
	        pagesize :10,
	        starttime : starttime,
			endtime:endtime,
			userId:username,
			urlType:urltype,
			sourceName : unitname,
			sourceTypeName:restype,
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
				"<th>浏览数</th>" +
				"<th>下载数</th>" +
				"<th>检索数</th>" +
				"<th>分享数</th>" +
				"<th>收藏数</th>" +
				"<th>导出数</th>" +
				"<th>笔记数</th>" +
				"<th>订阅数</th>" +
				"</tr>"
		    for(var i =0;res.pageRow[i];i++){
		    	id = 10*(curr-1)+i+1;
		    	if(restype=='期刊'||restype=='会议'||restype=='学位'){
		    		htmlbody="<td>"+res.pageRow[i].sourceName+"</td>";
		    	}
		    	html+="<tr>" +
		    			"<th><input type='checkbox' id='rstype' value="+res.pageRow[i].sourceTypeName+" onclick='checkboxchange();'></th>" +
		    			"<td>"+id+"</td>" +htmlbody+
						"<td>"+res.pageRow[i].sourceTypeName+"</td>" +
						"<td>"+res.pageRow[i].browseNum+"</td>" +
						"<td>"+res.pageRow[i].downloadNum+"</td>" +
						"<td>"+res.pageRow[i].searchNum+"</td>" +
						"<td>"+res.pageRow[i].shareNum+"</td>" +
						"<td>"+res.pageRow[i].collectNum+"</td>" +
						"<td>"+res.pageRow[i].exportNum+"</td>" +
						"<td>"+res.pageRow[i].noteNum+"</td>" +
						"<td>"+res.pageRow[i].subscibeNum+"</td>" +
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
		        pagesize :10,
		        starttime : starttime,
				endtime:endtime,
				userId:username,
				urlType:urltype,
				institutionName:unitname,
				sourceTypeName:restype,
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
					"<th>浏览数</th>" +
					"<th>下载数</th>" +
					"<th>检索数</th>" +
					"<th>分享数</th>" +
					"<th>收藏数</th>" +
					"<th>导出数</th>" +
					"<th>笔记数</th>" +
					"<th>订阅数</th>" +
					"</tr>"
			    for(var i =0;res.pageRow[i];i++){
			    	id = 10*(curr-1)+i+1;
			    	if(restype=='期刊'||restype=='会议'||restype=='学位'){
			    		
			    		var institution_name;
			    		if(res.pageRow[i].institution_name==null){
			    			institution_name="";
			    		}else{
			    			institution_name=res.pageRow[i].institution_name;
			    		}
			    		
			    		htmlbody="<td>"+institution_name+"</td>";
			    	}
			    	html+="<tr>" +
			    			"<th><input type='checkbox' id='rstype' value="+res.pageRow[i].sourceTypeName+" onclick='checkboxchange();'></th>" +
			    			"<td>"+id+"</td>" +htmlbody+
							"<td>"+res.pageRow[i].sourceTypeName+"</td>" +
							"<td>"+res.pageRow[i].browseNum+"</td>" +
							"<td>"+res.pageRow[i].downloadNum+"</td>" +
							"<td>"+res.pageRow[i].searchNum+"</td>" +
							"<td>"+res.pageRow[i].shareNum+"</td>" +
							"<td>"+res.pageRow[i].collectNum+"</td>" +
							"<td>"+res.pageRow[i].exportNum+"</td>" +
							"<td>"+res.pageRow[i].noteNum+"</td>" +
							"<td>"+res.pageRow[i].subscibeNum+"</td>" +
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
};

function getline(initial){
	var checkbox=$("#rstype:checked");
	var rstnames=new Array();
	var urls = new Array();
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
	var unitname=$("#unitname").val();
	
	if(restype=='--请选择资源类型--' && initial==2){
		getLineByCheckMore(checkbox);
	}else{
		if(restype=='学位')
		{
		$.ajax( {  
			type : "POST",  
			url : "../resourceTypeStatistics/getline.do",
			data : {
				'starttime' : starttime,
				'endtime':endtime,
				'userId':username,
				'urlType':urltype,
				'sourceName':unitname,
				'sourceTypeName':restype,
				'urls':urls,
				'singmore':singmore,
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
			url : "../resourceTypeStatistics/getline.do",
			data : {
				'starttime' : starttime,
				'endtime':endtime,
				'userId':username,
				'urlType':urltype,
				'institutionName':unitname,
				'sourceTypeName':restype,
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
		
	}
	
}

function tree(data){
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
	if ($("#checkallsource").is(':checked')) {  
		 $("input[name=item]").prop("checked",true);
   }else{
   	 $("input[name=item]").prop("checked",false);
   }
}

function checkitem(){
	getline(2);
}

//导出
function exportresource(){
	
	if(restype=='学位'){
		window.location.href="../resourceTypeStatistics/exportresourceType.do?" +
				"starttime="+starttime+"&endtime="+endtime+"&userId="+username+"&urlType="+urltype+
				"&sourceName="+unitname+"&sourceTypeName="+restype+"&num="+num;
	}else{	
		window.location.href="../resourceTypeStatistics/exportresourceType.do?" +
				"starttime="+starttime+"&endtime="+endtime+"&userId="+username+"&urlType="+urltype+
				"&institutionName="+unitname+"&sourceTypeName="+restype+"&num="+num;
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
	var starttime = $("#starttime").val();
	var endtime=$("#endtime").val();
	var username=$("#username").val();
	var unitname=$("#unitname").val();
	
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
				'urlType':urltype,
				'sourceName':unitname,
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
				'urlType':urltype,
				'institutionName':unitname,
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


