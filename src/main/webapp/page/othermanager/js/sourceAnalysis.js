$(function(){
	
	query();
//	change(1);
})

var time='';//日期选项
var type='';//来源类型选项
var indexType="";//分析指标取值
var date='';//time日期选项转换而来的日期,在以小时为统计单位时放置日期，如年-月-日
var startTime='';//time日期选项转换而来的开始时间，在以天为统计单位时放置日期，如年-月-日，在以小时为统计单位时放置时，如8时
var endTime='';//time日期选项转换而来的结束时间，在以天为统计单位时放置日期，如年-月-日，在以小时为统计单位时放置时，如8时

/**时间参数设定*/
function getTime(obj){
	
	$("#time").val(obj.innerText);
	$("#startTime").val("");
	$("#endTime").val("");
	
	$("[name=date]").each(function(){
		$(this).removeClass("btn-success");
	})
	$(obj).addClass("btn-success");
	query();
	
}

/**来源类型参数设定*/
function check(obj){
	
	$("#flag").val(obj.value);
	
}

/**相关参数赋值，查询相关数据*/
function getParameter(){
	
	//类型取值
	if($("#flag").val()!=''){
		
		type=$("#flag").val();
	}else{
		type="0";
	}
	
	//分析指标取值
	if($("#indexType").val()!=''){
		
		indexType=$("#indexType").val();
	}else{
		indexType="1";
	}
	
	//日期取值
	if(($("#startTime").val()=='')&&($("#endTime").val()=='')){
		time=$("#time").val();
		dateConversion(time);
	}
	else if(($("#startTime").val()!='')||($("#endTime").val()!='')){
		startTime=$("#startTime").val();
		endTime=$("#endTime").val();
		
		/**判断是否用小时为统计单位*/
//		if(startTime!=''&&endTime!=''){
//			var date1= new Date(endTime.replace(/-/g,"/"));
//			var date2= new Date(startTime.replace(/-/g,"/"));
//			var between = date1-date2;
//			if(between<=24*3600*1000){
//				date=date1.getFullYear()+"-"+(date1.getMonth()+1)+"-"+date1.getDate();
//				startTime=date2.getHours();
//				endTime=date1.getHours();
//				type=parseInt(type,10)+4+"";
////				alert("type:"+type+"date:"+date+"startTime:"+startTime+"endTime:"+endTime);
//			}
//		}
		if(startTime==endTime){
			date=startTime;
			startTime=0;
			endTime=23;
			type=parseInt(type,10)+4+"";
		}
		
		
	}else{
		time="昨天";
		dateConversion(time);
	}
	
	
}

/**分析指标改变函数*/
function change(value){

	$("#indexType").val(value);
	getParameter();
	/**取pie数据*/
	$.ajax({
		type : "POST",
		url : "../sourceAnalysis/getChart.do",
		data : {
			'flag' : type,
			'type' : indexType,
			'date' : date,
			'startTime' : startTime,
			'endTime' : endTime
		},
		dataType : "json",
		success : function(data) {
			
			pie(data);
			line(data);
		}
	})
}

/**查询执行方法*/
function query(){
	
	getParameter();
	tabulation(1);
	
	
	/**取pie数据*/
	$.ajax({
		type : "POST",
		url : "../sourceAnalysis/getChart.do",
		data : {
			'flag' : type,
			'type' : indexType,
			'date' : date,
			'startTime' : startTime,
			'endTime' : endTime
		},
		dataType : "json",
		success : function(data) {
			pie(data);
			line(data);
		}
	})
}

/**
 * 加载列表数据--liuYong
 */
function tabulation(curr){
	/**取列表数据*/
	$.ajax({
		type : "POST",
		url : "../sourceAnalysis/getPageList.do",
		data : {
			'flag' : type,
			'date' : date,
			'startTime' : startTime,
			'endTime' : endTime,
			'pageNum' : curr,//向服务端传的参数
	        'pageSize' : 10
		},
		dataType : "json",
		success : function(datas) {
		var data=(datas.pl).pageRow;
		var html="";
		var html1="";
		var thead="";
		var thead1="<thead><tr><th>网站浏览量</th><th>访问次数</th><th>独立IP数</th><th>平均访问页数</th><th>平均访问时长</th><th>跳出数</th><th>跳出率</th></tr></thead>";
		if(type=="0"||type=="4"){
			thead="<thead><tr><th>来源类型</th><th>网站浏览量</th><th>访问次数</th><th>独立IP数</th><th>平均访问页数</th><th>平均访问时长</th><th>跳出数</th><th>跳出率</th></tr></thead>";
			
			var access_type="";	
			for(var i=0;i<data.length;i++){
				if(data[i].access_type==0){
					access_type="直接访问";
				}else if(data[i].access_type==1){
					access_type="搜索引擎";
				}else if(data[i].access_type==2){
					access_type="外部链接";
				}else if(data[i].access_type==11111){
					access_type="当前汇总";
				}
				html+="<tr><td>"+access_type+"</td>" +
				"<td>"+data[i].sum1+"</td>" +
				"<td>"+data[i].sum2+"</td>" +
				"<td>"+data[i].sum3+"</td>" +
				"<td>"+data[i].sum4+"</td>" +
				"<td>"+data[i].sum6+"</td>" +
				"<td>"+data[i].sum7+"</td>" +
				"<td>"+data[i].sum8+"</td>" +
				"</tr>";
			}
			
			html1="<tr><td>"+datas.sasdSummary.sum1+"</td>" +
			"<td>"+datas.sasdSummary.sum2+"</td>" +
			"<td>"+datas.sasdSummary.sum3+"</td>" +
			"<td>"+datas.sasdSummary.sum4+"</td>" +
			"<td>"+datas.sasdSummary.sum6+"</td>" +
			"<td>"+datas.sasdSummary.sum7+"</td>" +
			"<td>"+datas.sasdSummary.sum8+"</td>" +
			"</tr>";
		}else if(type=="1"||type=="5"){
			thead="<thead><tr><th>搜索引擎</th><th>网站浏览量</th><th>访问次数</th><th>独立IP数</th><th>平均访问页数</th><th>平均访问时长</th><th>跳出数</th><th>跳出率</th></tr></thead>"
			for(var i=0;i<data.length;i++){
				html+="<tr><td>"+data[i].engine_name+"</td>" +
				"<td>"+data[i].sum1+"</td>" +
				"<td>"+data[i].sum2+"</td>" +
				"<td>"+data[i].sum3+"</td>" +
				"<td>"+data[i].sum4+"</td>" +
				"<td>"+data[i].sum6+"</td>" +
				"<td>"+data[i].sum7+"</td>" +
				"<td>"+data[i].sum8+"</td>" +
				"</tr>";
			}
			html1="<tr><td>"+datas.saseSummary.sum1+"</td>" +
			"<td>"+datas.saseSummary.sum2+"</td>" +
			"<td>"+datas.saseSummary.sum3+"</td>" +
			"<td>"+datas.saseSummary.sum4+"</td>" +
			"<td>"+datas.saseSummary.sum6+"</td>" +
			"<td>"+datas.saseSummary.sum7+"</td>" +
			"<td>"+datas.saseSummary.sum8+"</td>" +
			"</tr>";
			
		}else if(type=="2"||type=="6"){
			thead="<thead><tr><th>检索词</th><th>网站浏览量</th><th>访问次数</th><th>独立IP数</th><th>平均访问页数</th><th>平均访问时长</th><th>跳出数</th><th>跳出率</th></tr></thead>"
			for(var i=0;i<data.length;i++){
				html+="<tr><td>"+data[i].keyWord+"</td>" +
				"<td>"+data[i].sum1+"</td>" +
				"<td>"+data[i].sum2+"</td>" +
				"<td>"+data[i].sum3+"</td>" +
				"<td>"+data[i].sum4+"</td>" +
				"<td>"+data[i].sum6+"</td>" +
				"<td>"+data[i].sum7+"</td>" +
				"<td>"+data[i].sum8+"</td>" +
				"</tr>";
			}
			html1="<tr><td>"+datas.saseSummary.sum1+"</td>" +
			"<td>"+datas.saseSummary.sum2+"</td>" +
			"<td>"+datas.saseSummary.sum3+"</td>" +
			"<td>"+datas.saseSummary.sum4+"</td>" +
			"<td>"+datas.saseSummary.sum6+"</td>" +
			"<td>"+datas.saseSummary.sum7+"</td>" +
			"<td>"+datas.saseSummary.sum8+"</td>" +
			"</tr>";
				
		}else if(type=="3"||type=="7"){
			thead="<thead><tr><th>外部链接</th><th>网站浏览量</th><th>访问次数</th><th>独立IP数</th><th>平均访问页数</th><th>平均访问时长</th><th>跳出数</th><th>跳出率</th></tr></thead>"
			for(var i=0;i<data.length;i++){
				html+="<tr><td>"+data[i].link_host+"</td>" +
				"<td>"+data[i].sum1+"</td>" +
				"<td>"+data[i].sum2+"</td>" +
				"<td>"+data[i].sum3+"</td>" +
				"<td>"+data[i].sum4+"</td>" +
				"<td>"+data[i].sum6+"</td>" +
				"<td>"+data[i].sum7+"</td>" +
				"<td>"+data[i].sum8+"</td>" +
				"</tr>";
			}
			html1="<tr><td>"+datas.saseSummary.sum1+"</td>" +
			"<td>"+datas.saseSummary.sum2+"</td>" +
			"<td>"+datas.saseSummary.sum3+"</td>" +
			"<td>"+datas.saseSummary.sum4+"</td>" +
			"<td>"+datas.saseSummary.sum6+"</td>" +
			"<td>"+datas.saseSummary.sum7+"</td>" +
			"<td>"+datas.saseSummary.sum8+"</td>" +
			"</tr>";
					
		}
		document.getElementById('tabulation1').innerHTML = thead1+ html1;
		document.getElementById('tabulation').innerHTML = thead+ html;
       
		var totalRow=datas.pl.totalRow;
		var pageSize=datas.pl.pageSize;
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
                	tabulation(obj.curr);
            		
                }
            }
        });
		}
	});
}

/**
 * 加载饼图数据--liuYong
 */
function pie(data){
	
	var myChart = echarts.init(document.getElementById('pie'));
	if(indexType=="1"||indexType=="2"){
	option = {
		tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
		},
	    calculable : true,
	    series : [
	        {
	            name:'访问来源',
	            type:'pie',
	            radius : '55%',
	            center: ['50%', '60%'],
	            data:eval(data.jsonArr),
	        }
	    ]
	};
	myChart.setOption(option);
	}else if(indexType=="4"||indexType=="6"){
		option = {
			   
			    tooltip : {
			        trigger: 'axis'
			    },
			   
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            data : eval(data.groupArr)
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [
			        {
			            name:'值',
			            type:'bar',
			            data: eval(data.dataList),
			            markLine : {
			                data : [
			                     [{name: '平均值起点',value:data.average,  xAxis: -1, yAxis: data.average},
			                       {name: '平均值终点', xAxis: data.xaverage, yAxis: data.average}]
			                ]
			            }
			        }
			    ]
			};
		
		myChart.setOption(option);
	}else if(indexType=="8"){
		option = {   
			    tooltip : {
			        trigger: 'axis'
			    },
			   
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            data : eval(data.groupArr)
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            axisLabel:{
			            	formatter:function(value){
			            		var sub_title=eval(value)+'%';
			            		return sub_title;
			            	}
			            }
			        }
			    ],
			    series : [
			        {	 
			        	tooltip:{
			        		show:true,
			        		formatter:function(value){
			        			var sub_title=value[0].name+"<br/> "+value[0].seriesName+" ： "+value[0].value+"%";
			        			return sub_title;
			            	}
			        	},
			            name:'值',
			            type:'bar',
			            data: eval(data.dataList),
			        }
			    ]
			};
		
		myChart.setOption(option);
		
	}
	
	
}


/**
 * 加载折线图数据--liuYong
 */
function line(data){
	
    var myChart = echarts.init(document.getElementById('line')); 
    
    if(indexType=="8"){
    	
 	   option = {
 			    tooltip : {
 			        trigger: 'axis',
 			        formatter:function(data){
 			        	var sun_title=data[0].name+"<br/>";
 			        	for(var i=0;i<data.length;i++){
 			        		sun_title=sun_title+data[i].seriesName+" ："+data[i].value+"%<br/>";
 			        	}
 			        	return sun_title;
 			        }
 			    },
 			    legend: {
 			        data:eval(data.groupArr)
 			    },
 			    calculable : true,
 			    xAxis : [
 			        {
 			            type : 'category',
 			            boundaryGap : false,
 			            data : eval(data.dateArr)
 			        }
 			    ],
 			    yAxis : [
 			        {
 			            type : 'value',
 			            name : '值（单位：—）',
 			            splitNumber:10, 
 			            axisLabel:{
			            	formatter:function(value){
			            		var sub_title=eval(value)+'%';
			            		return sub_title;
			            	}
			            }
 			        }
 			    ],
 			    series : eval(data.seriesArr),
 			};
    	
    	
    }else{
    	
 	   option = {
  			    tooltip : {
  			        trigger: 'axis'
  			    },
  			    legend: {
  			        data:eval(data.groupArr)
  			    },
//  			    toolbox: {
//  			        show : true,
//  			        feature : {
//  			            mark : {show: true},
//  			            dataView : {show: true, readOnly: false},
//  			            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
//  			            restore : {show: true},
//  			            saveAsImage : {show: true}
//  			        }
//  			    },
  			    calculable : true,
  			    xAxis : [
  			        {
  			            type : 'category',
  			            boundaryGap : false,
  			            data : eval(data.dateArr)
  			        }
  			    ],
  			    yAxis : [
  			        {
  			            type : 'value',
  			            name : '值（单位：—）',
  			            splitNumber:10
  			        }
  			    ],
  			    series : eval(data.seriesArr),
  			};
    }
    
	myChart.setOption(option);
}

/**日期转换函数*/
function GetDateStr(AddDayCount) {
	var now = new Date();
	var dd = new Date(now.getTime() - AddDayCount*24*3600*1000);
	var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    return y+"-"+m+"-"+d;
}

/**选定日期参数转换成startTime、endTime*/
function dateConversion(time){
	if(time=="昨天"){
//		startTime = GetDateStr(1); 
//		endTime=startTime;
		date=GetDateStr(1);
		startTime=0;
		endTime=23;
		type=parseInt(type,10)+4+"";
	}else if(time=="近7天"){
		startTime=GetDateStr(6);
		endTime=GetDateStr(0);
	}else if(time=="最近30天"){
		startTime=GetDateStr(29);
		endTime=GetDateStr(0);
	}
}