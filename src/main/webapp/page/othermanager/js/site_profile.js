$(function(){
	pageView(0);
	selectSumNumbers(0);
	basicIndexNum(0,1);
	findWebsiteAttribute(0);
	$("#showTime").attr("checked",false);
	if($("#showTime").is(':checked')){
		 $("#date_range1").show();
	 }else{
		 $("#date_range1").hide();
	 }
});
 function showTime(){	
	 if($("#showTime").is(':checked')){
		 $("#date_range1").show();
	 }else{
		 $("#date_range1").val("");	
		 $("#date_range1").hide();
		 pageView($("#date_range").val());
	 }
 }
 //查询顾客属性 根据时间
 function findWebsiteAttribute(val){
	 var age;
	 var education;
	 var gender;
	 var jobTitle;
	 $.ajax({
			type : "post",
			data : {
				dateType : val,
			},
			url : "../siteProfile/findWebsiteAttribute.do",
			dataType : "json",
			async : false,
			success : function(res) {
				if(res !=null && res !=""){
					showAllUser(res);	
				age = res.age;
				education = res.education;
				gender = res.gender;
				jobTitle = res.jobTitle;
				interest = res.interest;
				agebar(age);
				educationbar(education);
				genderbar(gender);
				jobTitlebar(jobTitle);
				interestedbar(interest);
				
				}
			}
		});
 }
 function showAllUser(data){
	 //var str ="共有用户"+data.age[0].nums+ "人";
	 //$("#allUser").html(str);
	 //document.getElementById('allUser').innerHTML = str;
 }
 
// 基础指标列表
function basicIndexNum(val,curr){
	var day;
	if(val != 0 && val != "qitian" && val != "yigeyue"){
	 day = DateDiff(val);
	}
	if(val ==0 || day == 0 ){
	$.ajax({
		type : "post",
		data : {
			dateType : val,
			pagesize : 10,
			pagenum : curr || 1,
		},
		url : "../siteProfile/basicIndexNumHourly.do",
		dataType : "json",
		async : false,
		success : function(res) {
			var str = "";
			var pageRow ;
			var totalRow ;
			var pageSize ;
			if(res.pageRow != null && res.pageRow != "" ){
				pageRow = res.pageRow;
				totalRow = res.totalRow;
				pageSize = res.pageSize;
			for ( var i = 0; i < pageRow.length; i++) {
				str +="<tr class='even'>";
				str += "<td>"+pageRow[i].hour +"</td>";
				str += "<td>"+pageRow[i].网站浏览量 +"</td>";
				str += "<td>"+pageRow[i].访问次数 +"</td>";
				str += "<td>"+pageRow[i].独立访客数 +"</td>";
				str += "<td>"+pageRow[i].新增访客 +"</td>";
				str += "<td>"+pageRow[i].独立IP数 +"</td>";
				str += "<td>"+pageRow[i].平均访问页面 +"</td>";
				str += "<td>"+pageRow[i].平均访问时长 +"</td>";
				str += "<td>"+pageRow[i].跳出率 +"</td>";
				str +="</tr>";
				}
				}else{
				str = "<tr class='even'><td> 数据为空！！</td></tr>";
				}
		
			$("#date_type").text("时间");
			document.getElementById('jichuzhibiao').innerHTML = str;
		
			//显示分页
			if (totalRow % pageSize == 0) {
				pages = totalRow / pageSize;
			} else {
				pages = totalRow / pageSize + 1;
			}
			if (pages >= 4) {
				groups = 4;
			} else {
				groups = pages;
			}
			laypage({
				cont : 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page"></div>
				pages : pages, //通过后台拿到的总页数
				curr : curr, //当前页
				skip : true, //是否开启跳页
				skin : 'molv',//当前页颜色，可16进制
				groups : groups, //连续显示分页数
				first : '首页', //若不显示，设置false即可
				last : '尾页', //若不显示，设置false即可
				prev : '上一页', //若不显示，设置false即可
				next : '下一页', //若不显示，设置false即可
				jump : function(obj, first) { //触发分页后的回调
					if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
						curr = obj.curr;
						basicIndexNum(val,curr);
					}
				}
			});
		}
	});
	}else {
		$.ajax({
			type : "post",
			data : {
				dateType : val,
				pagesize : 10,
				pagenum : curr || 1,
			},
			url : "../siteProfile/basicIndexNum.do",
			dataType : "json",
			async : false,
			success : function(res) {
				var str = "";
				var pageRow ;
				var totalRow ;
				var pageSize ;
				if(res.pageRow != "" && res.pageRow != "" ){
					pageRow = res.pageRow;
					totalRow = res.totalRow;
					pageSize = res.pageSize;
				for ( var i = 0; i < pageRow.length; i++) {
					str +="<tr class='even'>";
					str += "<td>"+pageRow[i].date +"</td>";
					str += "<td>"+pageRow[i].网站浏览量 +"</td>";
					str += "<td>"+pageRow[i].访问次数 +"</td>";
					str += "<td>"+pageRow[i].独立访客数 +"</td>";
					str += "<td>"+pageRow[i].新增访客 +"</td>";
					str += "<td>"+pageRow[i].独立IP数 +"</td>";
					str += "<td>"+pageRow[i].平均访问页面 +"</td>";
					str += "<td>"+pageRow[i].平均访问时长 +"</td>";
					str += "<td>"+pageRow[i].跳出率 +"</td>";
					str +="</tr>";
					}
					}else{
					str = "<tr class='even'><td> 数据为空！！</td></tr>";
					}
				$("#date_type").text("日期");
				document.getElementById('jichuzhibiao').innerHTML = str;
			
				//显示分页
				if (totalRow % pageSize == 0) {
					pages = totalRow / pageSize;
				} else {
					pages = totalRow / pageSize + 1;
				}
				if (pages >= 4) {
					groups = 4;
				} else {
					groups = pages;
				}
				laypage({
					cont : 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page"></div>
					pages : pages, //通过后台拿到的总页数
					curr : curr, //当前页
					skip : true, //是否开启跳页
					skin : 'molv',//当前页颜色，可16进制
					groups : groups, //连续显示分页数
					first : '首页', //若不显示，设置false即可
					last : '尾页', //若不显示，设置false即可
					prev : '上一页', //若不显示，设置false即可
					next : '下一页', //若不显示，设置false即可
					jump : function(obj, first) { //触发分页后的回调
						if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
							curr = obj.curr;
							basicIndexNum(val,curr);
						}
					}
				});
			}
		});
	}
}
	
	function pageView(val){
		var day;
	if(!(val ==null || val == undefined )){
		$("#dataId").val(val);
		selectSumNumbers(val);
		findWebsiteAttribute(val);
		basicIndexNum(val,1);
		if(val=='qitian'||val==0||val=='yigeyue')
 			{
			if (val == 0) {
				$('#yigeyue').removeClass('btn-success');
				$('#qitian').removeClass('btn-success');
				$('#zuotian').addClass('btn-success');
				$("#anzhou").attr("disabled", true);
				$("#anyue").attr("disabled", true);
				findPageViewHourly(val);
				$("#startEndTime").html("");// 取消显示对比时间
			} else if (val == 'qitian') {
				$('#zuotian').removeClass('btn-success');
				$('#yigeyue').removeClass('btn-success');
				$('#qitian').addClass('btn-success');
				$("#anyue").attr("disabled", true);
				findPageView(val);
				$("#startEndTime").html("");// 取消显示对比时间
			} else if (val == 'yigeyue') {
				$('#zuotian').removeClass('btn-success');
				$('#qitian').removeClass('btn-success');
				$('#yigeyue').addClass('btn-success');
				$("#anyue").attr("disabled", true);
				findPageView(val);
				$("#startEndTime").html("");// 取消显示对比时间
			}
		}
		else if(!(val=='qitian'||val==0||val=='yigeyue')){
			 $("#startEndTime").html("");//取消显示对比时间
				day =DateDiff(val);
				if(day== 0){
					findPageViewHourly(val);
				}else{
				findPageView(val);
				}
		}
		
		
		/*if(val ==0 ){
			$('#yigeyue').removeClass('btn-success');
			$('#qitian').removeClass('btn-success');
			$('#zuotian').addClass('btn-success');
			$("#anzhou").attr("disabled", true);
			$("#anyue").attr("disabled", true);
			findPageViewHourly(val);
			 $("#startEndTime").html("");//取消显示对比时间
		}else if(val == 'qitian'){
			$('#zuotian').removeClass('btn-success');
			$('#yigeyue').removeClass('btn-success');
			$('#qitian').addClass('btn-success');
			$("#anyue").attr("disabled", true);
			findPageView(val);
			 $("#startEndTime").html("");//取消显示对比时间
		}else if(val=='yigeyue'){
			$('#zuotian').removeClass('btn-success');
			$('#qitian').removeClass('btn-success');
			$('#yigeyue').addClass('btn-success');
			$("#anyue").attr("disabled", true);
			findPageView(val);
			 $("#startEndTime").html("");//取消显示对比时间
		}else{
			 $("#startEndTime").html("");//取消显示对比时间
			day =DateDiff(val);
			$('#zuotian').removeClass('btn-success');
			$('#qitian').removeClass('btn-success');
			$('#yigeyue').removeClass('btn-success');
			if(day== 0){
				findPageViewHourly(val);
			}else{
			findPageView(val);
			}
		}*/
	}
	else
		{
		var val = $("#dataId").val();
		if(!(val==null||val==undefined))
			{
			if(val ==0 ){
				findPageViewHourly(val);
			}else{
				
				findPageView(val);	
			}
			}
		}
}
function findPageViewHourly(val){
	
	$.ajax({
		type : "post",
		data : {
			type : $("#typeId").val(), 
			dateType : val,
		},
		url : "../siteProfile/findPageViewHourly.do",
		dataType : "json",
		async : false,
		success : function(res) {
			inputTime(res);
			tree(res);
		}
	});
}
	function inputTime(data){
		$("#date_range").val(data.time);
	}
	function findPageView(val){
		if(val == "yigeyue"){
			$("#anzhou").attr("disabled", false);
		}else if(val == "qitian"){
			$("#anzhou").attr("disabled", true);
		}
		$.ajax({
			type : "post",
			data : {
				dateType : val,
				type : $("#typeId").val(),
			},
			url : "../siteProfile/findPageView.do",
			dataType : "json",
			async : false,
			success : function(res) {
				inputTime(res);
				tree(res);
			}
		});
		
	}
//统计列表 查询
	function selectSumNumbers(val){
		var str = "<tr class='even'>";
		$.ajax({
			type : "post",
			data : {
				dateType : val,
			},
			url : "../siteProfile/selectSumNumbers.do",
			dataType : "json",
			async : false,
			success : function(res) {
				if(res.length==0){
					str +="<td>数据为空</td>";
					str +="</tr>";
				}else{
				str += "<td>"+res[0].value+"</td>";
				str += "<td>"+res[1].value+"</td>";
				str += "<td>"+res[2].value+"</td>";
				str += "<td>"+res[3].value+"</td>";
				str += "<td>"+res[4].value+"</td>";
				str += "<td>"+res[5].value+"</td>";
				str += "<td>"+res[7].value+"</td>";
				str += "<td>"+res[9].value+"</td>";
				str += "</tr>";
			}
			}
		});
		$("#tongjiliebiao").html(str);
	}
	

function tree(data){
	if(data.type == "跳出率"){
		data.type = data.type+"(%)";
		for(var i=0;i<data.numbers.length;i++){
		
			data.numbers[i]= data.numbers[i].replace("%","");
		}
	}else if(data.type =="平均访问时长"){
		data.type= data.type+"(/s)";
	}else if(data.type== "访问总时长"){
		data.type = data.type+"(/s)";
	}
    var myChart = echarts.init(document.getElementById('main'));
    option = {
    	    tooltip : {
    	        trigger: 'axis'
    	    },
    	   /* legend: {
    	        data:['网站浏览量','访问次数','独立访问数','新访客数','独立IP数']
    	    },*/
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
    	            data : data.date,
    	        }
    	    ],
    	    yAxis : [
    	        {
    	        	name:data.type,
    	            type : 'value'
    	        }
    	    ],
    	    series : [
    	        {
    	            name:data.type,
    	            type:'line',
    	            data: data.numbers,
    	        },
    	    ]
    	};
    myChart.setOption(option); 
    window.onresize = myChart.resize;//echar 自适应屏幕
}

	function agebar(data){
		
		var datas;
		if(data.length==0){
			datas=eval("[0,0,0,0,0,0]");
		}else{
			datas="["+(data[0].percent==0?0:(data[0].percent*100).toFixed(2))+","
			 +(data[1].percent==0?0:(data[1].percent*100).toFixed(2))+","
			 +(data[2].percent==0?0:(data[2].percent*100).toFixed(2))+","
			 +(data[3].percent==0?0:(data[3].percent*100).toFixed(2))+","
			 +(data[4].percent==0?0:(data[4].percent*100).toFixed(2))+","
			 +(data[5].percent==0?0:(data[5].percent*100).toFixed(2))+"]";
			datas=eval(datas);
		}
		
		var myChart = echarts.init(document.getElementById('nianlingfenbu'));
		option = {
			    title: {
			        text: '年龄分布',
			    },
			    tooltip: {
			        trigger: 'item',
			        axisPointer: {
			            type: 'shadow'
			        }
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
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    xAxis: {
			        type: 'category',
			        data: ['0-19岁','20-29岁','30-39岁','40-49岁','50-59岁','60+岁'],
			        
			        boundaryGap: [0, 0.01]
			    },
			    yAxis: {
			        type: 'value',
			        axisLabel:{
			        	formatter:'{value} %'
			        },
			    },
			    series: [
			        {
			            name: '年龄分布',
			            type: 'bar',
			            barWidth :'30%',
			            itemStyle:{
			            	normal :{
			            		color:'#367fa9'
			            	}
			            },
			            data:datas,
			        }
			    ]
			};
		myChart.setOption(option);	
		window.onresize = myChart.resize;//echar 自适应屏幕
		
	}
	function educationbar(data){
		
		var datas;
		
		if(data.length==0){
			datas=eval("[0,0,0,0,0]");
		}else{
			datas="["+(data[0].percent==0?0:(data[0].percent*100).toFixed(2))+","
			 +(data[1].percent==0?0:(data[1].percent*100).toFixed(2))+","
			 +(data[2].percent==0?0:(data[2].percent*100).toFixed(2))+","
			 +(data[3].percent==0?0:(data[3].percent*100).toFixed(2))+","
			 +(data[4].percent==0?0:(data[4].percent*100).toFixed(2))+"]";
			datas=eval(datas);
		}
		
		var myChart = echarts.init(document.getElementById('xuelifenubu'));
		option = {
			    title: {
			        text: '学历分布',
			    },
			    
			    tooltip: {
			        trigger: 'item',
			        axisPointer: {
			            type: 'shadow'
			        }
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
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    xAxis: {
			        type: 'category',
			        data: ['专科','本科','硕士','博士','其他'],
			        
			        boundaryGap: [0, 0.01]
			    },
			    yAxis: {
			        type: 'value',
			        axisLabel:{
			        	formatter:'{value} %'
			        },
			        
			    },
			    series: [
			        {
			            name: '学历分布',
			            type: 'bar',
			            barWidth :'30%',
			            itemStyle:{
			            	normal :{
			            		color:'#367fa9'
			            	}
			            },
			            data:datas,
			        }
			       
			    ]
			};
		myChart.setOption(option);	
	}
	function jobTitlebar(data){
		
		var datas;
		if(data.length==0){
			datas=eval("[0,0,0,0,0]");
		}else{
			datas="["+(data[0].percent==0?0:(data[0].percent*100).toFixed(2))+","
			 +(data[1].percent==0?0:(data[1].percent*100).toFixed(2))+","
			 +(data[2].percent==0?0:(data[2].percent*100).toFixed(2))+","
			 +(data[3].percent==0?0:(data[3].percent*100).toFixed(2))+","
			 +(data[4].percent==0?0:(data[4].percent*100).toFixed(2))+"]";
			datas=eval(datas);
		}

		var myChart = echarts.init(document.getElementById('zhichengfenubu'));
		option = {
			    title: {
			        text: '职称分布',
			    },
			    
			    tooltip: {
			        trigger: 'item',
			        axisPointer: {
			            type: 'shadow'
			        }
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
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    xAxis: {
			        type: 'category',
			        data: ['初级','中级','副高级','正高级','其他'],
			        
			        boundaryGap: [0, 0.01]
			    },
			    yAxis: {
			        type: 'value',
			        axisLabel:{
			        	formatter:'{value} %'
			        },
			        
			    },
			    series: [
			        {
			            name: '职称分布',
			            type: 'bar',
			            barWidth :'30%',
			            itemStyle:{
			            	normal :{
			            		color:'#367fa9'
			            	}
			            },
			            data:datas,
			        }
			       
			    ]
			};
		myChart.setOption(option);
		window.onresize = myChart.resize;//echar 自适应屏幕
	}
	
	function genderbar(data){
		
		var datas;
		if(data.length==0){
			datas=eval("[0,0]");
		}else{
			datas="["+(data[0].percent==0?0:(data[0].percent*100).toFixed(2))+","
			 +(data[1].percent==0?0:(data[1].percent*100).toFixed(2))+"]";
			datas=eval(datas);
		}
		
		var myChart = echarts.init(document.getElementById('saxfenbu'));
		option = {
		    title: {
		        text: '性别分布',
		    },
		    
		    tooltip: {
		        trigger: 'item',
		        axisPointer: {
		            type: 'shadow'
		        }
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
		   
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis: {
		        type: 'category',
		        data: ['男','女'],
		        
		        boundaryGap: [0, 0.01]
		    },
		    yAxis: {
		        type: 'value',
		        axisLabel:{
		        	formatter:'{value} %'
		        },
		        
		    },
		    series: [
		        {
		            name: '性别分布',
		            type: 'bar',
		            itemStyle:{
		            	normal :{
		            		color:'#367fa9'
		            	}
		            },
		            barWidth: '20%',//调整柱状图粗细
		            data:datas,
		        	
		        }
		    ]
		};
		
		myChart.setOption(option);
		window.onresize = myChart.resize;
	}
	
	function interestedbar(data){
		var name="[";
		var percent="[";
		if(data.length>0){
			for(var i=0;i<data.length;i++){
				if(i == 0){
					name += "'"+(data[i].group_name==undefined?'':data[i].group_name) +"'";
					percent += (data[i].percent==0||data[i].percent==undefined?0:(data[i].percent*100).toFixed(2));
				}else{
					name += ",'"+(data[i].group_name==undefined?'':data[i].group_name)+"'";
					percent += "," + (data[i].percent==0||data[i].percent==undefined?0:(data[i].percent*100).toFixed(2));
				}
			}
		}
		name+="]";
		percent+="]";
		
		name = eval(name);
		percent = eval(percent);
		var myChart = echarts.init(document.getElementById('ganxingqu'));
		
		option = {
		    title: {
		        text: '感兴趣的主题分布',
		    },
		    
		    tooltip: {
		        trigger: 'item',
		        axisPointer: {
		            type: 'shadow'
		        }
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
		   
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis: {
		        type: 'category',
		        data: name,
		        //	['IT','教育/学生','营销/公关','建筑','电信/网络','服务','医疗/保健','金融/房产','传媒/娱乐','农林/化工'],
		        
		        boundaryGap: ['20%', '20%']
		    },
		    yAxis: {
		        type: 'value',
		        axisLabel:{
		        	formatter:'{value} %'
		        },
		        
		    },
		    series: [
		        {
		            name: '感兴趣的主题分布',
		            type: 'bar',
		            barWidth: '30%',//调整柱状图粗细
		            barMaxWidth:'30%',//最大粗戏
		            data: percent,
		            //	['35','23','3','5','4','13','5','3','6','3'],
		            itemStyle:{
		            	normal :{
		            		color:'#367fa9'
		            	},
		           /* emphasis:{
						color:'#6A5ACD' 
						}*/
		            }
		        }
		       
		    ]
		};
		myChart.setOption(option);
		window.onresize = myChart.resize;//echar 自适应屏幕
	}