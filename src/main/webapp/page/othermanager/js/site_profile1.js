$(function(){
	removeCheck();
	var datetime = new Date();
	var year = datetime.getFullYear();
	var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
	var day = datetime.getDate() < 10 ? "0" + (datetime.getDate()-1) : datetime.getDate()-1;
	
	$("#date_range").daterangepicker({
	    // format: 'YYYY-MM-DD',
	    //maxDate : month+'/'+'/'+day+'/'+year, //最大时间
		maxDate : year+'-'+month+'-'+day, //最大时间
		startDate: year+'-'+month+'-'+day,
		endDate: year+'-'+month+'-'+day,
	    dateLimit: {days:366},	//起止时间的最大间隔366天
	    showDropdowns : true,
	    buttonClasses : ['btn btn-default'],
	    applyClass : 'btn-small btn-primary blue',
	    cancelClass : 'btn-small',
	    format:'YYYY-MM-DD',//控件中from和to 显示的日期格式  
	    separator : '/',
	    locale : {
	        applyLabel : '确定',
	        cancelLabel : '取消',
	        fromLabel : '从',
	        toLabel : '到',
	        customRangeLabel : 'Custom',
	        daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
	        monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月',
	        '七月', '八月', '九月', '十月', '十一月', '十二月' ],
	        firstDay : 1
	    }
//	    $("input[name = 'daterangepicker_start' ]").val(($("input[name = daterangepicker_start ]").val()).format('YYYY-MM-DD'));
//	    $("input[name = 'daterangepicker_end' ]").val(($("input[name = daterangepicker_end ]").val()).format('YYYY-MM-DD'));

	}, function(start, end, label) {//格式化日期显示框 
		$("#startEndTime").html("");//取消显示对比时间
		removeCheck();
		
        $('#date_range').val(start.format('YYYY-MM-DD') + '/' + end.format('YYYY-MM-DD'));
        pageView($('#date_range').val());
        
        var days = DateDiff($("#date_range").val());
		$("#anzhou").attr("disabled", true);
		$("#anyue").attr("disabled", true);
		$('#yigeyue').removeClass('btn-success');
		$('#qitian').removeClass('btn-success');
		$('#zuotian').removeClass('btn-success');
		if(days>7 && days<=30){
			$("#anzhou").attr("disabled", false);
			$("#anyue").attr("disabled", true);
		} else if(days>30){
			$("#anzhou").attr("disabled", false);
			$("#anyue").attr("disabled", false);
		}
    });
    $("#date_range1").daterangepicker({
	    //maxDate : moment(), //最大时间
	    //maxDate : month+'/'+'/'+day+'/'+year, //最大时间
    	maxDate : year+'-'+month+'-'+day, //最大时间
    	startDate: year+'-'+month+'-'+day,
		endDate: year+'-'+month+'-'+day,
	    dateLimit: {days:366},	//起止时间的最大间隔366天
	    showDropdowns : true,
	    buttonClasses : [ 'btn btn-default' ],
	    applyClass : 'btn-small btn-primary blue',
	    cancelClass : 'btn-small',
	    format:'YYYY-MM-DD',
	    separator : '/',
	    locale : {
	        applyLabel : '确定',
	        cancelLabel : '取消',
	        fromLabel : '从',
	        toLabel : '到',
	        customRangeLabel : 'Custom',
	        daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
	        monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月',
	        '七月', '八月', '九月', '十月', '十一月', '十二月' ],
	        firstDay : 1
	    }
	
	}, function(start, end, label) {//格式化日期显示框 
		//获取 与其他时间段相比 的天数
		var startDays = DateDiff($("#date_range1").val());
		var startTimes = startDays*24*3600;
		var endStartTime = Date.parse(start.format('YYYY-MM-DD'))/1000;	//对比日期的开始时间戳
		var endEndTime = endStartTime+startTimes*1;
        $('#date_range1').val(start.format('YYYY-MM-DD') + '/' + UnixToDate(endEndTime));
        
        //显示对比时间
        if( ($("#date_range").val()) !=null && ($("#date_range1").val()) != ""){
        var html = "<th>对比时间 :"+ "<lable style='color:#c23531;'>●</lable>"+$("#date_range").val() +"与<lable style='color:#2f4554;'>●</lable>"+$('#date_range1').val()+"</th>";
        $("#startEndTime").html(html);
        }
        duibi($('#date_range').val(),$('#date_range1').val());
    }
    );
	/**       
     * 时间戳转换日期       
     * @param <int> unixTime  待时间戳(秒)       
     * @param <bool> isFull  返回完整时间(Y-m-d 或者 Y-m-d H:i:s)       
     * @param <int> timeZone  时区       
     */
    function UnixToDate(unixTime, isFull, timeZone) {

      var time = new Date(unixTime * 1000);
      var ymdhis = "";
      ymdhis += time.getUTCFullYear() + "-";
      if((time.getUTCMonth()+1)>=10){
      	ymdhis += (time.getUTCMonth()+1) + "-";
      } else {
      	ymdhis += '0'+(time.getUTCMonth()+1) + "-";
      }
      if(time.getUTCDate()>=10){
      	ymdhis += time.getUTCDate();
      } else {
      	ymdhis += '0'+time.getUTCDate();
      }
      if (isFull === true)
      {
        ymdhis += " " + time.getUTCHours() + ":";
        ymdhis += time.getUTCMinutes() + ":";
        ymdhis += time.getUTCSeconds();
      }
      return ymdhis;
    }
    /* 正则
	$("#date_range").change(function(){
	    var pattern = /^(\d{1,2})(\/)(\d{1,2})(\/)(\d{4})(-)(\d{1,2})(\/)(\d{1,2})(\/)(\d{4})$/;
	    var date_range = $("#date_range").val();
	    if(false == pattern.test(date_range)){
	        alert("日期格式不正确！");
	        return false;
	    }
	});
	$("#date_range1").change(function(){
	    var pattern = /^(\d{1,2})(\/)(\d{1,2})(\/)(\d{4})(-)(\d{1,2})(\/)(\d{1,2})(\/)(\d{4})$/;
	    var date_range = $("#date_range1").val();
	    if(false == pattern.test(date_range)){
	        alert("日期格式不正确！");
	        return false;
	    }
	});
	*/
	
});
	  //切换type类型事件
   function  querypageView(){
		if($("#showTime").is(':checked')){
			if($("#date_range1").val() != null && $("#date_range1").val() != ""){
				duibi($('#date_range').val(),$('#date_range1').val());
			}else{
				pageView($("#date_range").val());
			}
		}else{
			pageView($("#date_range").val());
		}
	}
	 //对比指标
    function duibi(val1,val2){
    var day = DateDiff(val1);
    var day2=DateDiff(val2);
    	if( day ==0 && day2==0 ){
    		$.ajax({
			type : "post",
			data : {
				dateType : val1,
				contrastDate : val2,//对比时间
				type : $("#typeId").val(), 
				
			},
			url : "../siteProfile/contrastPageViewHourly.do",//对比折线图方法 小时
			dataType : "json",
			async : false,
			success : function(res) {
				contrastTree1(res);
			}
		});	
    	} else {
    	$.ajax({
			type : "post",
			data : {
				dateType : val1,
				contrastDate : val2,//对比时间
				type : $("#typeId").val(), 
				
			},
			url : "../siteProfile/contrastPageView.do",//对比折线图方法 天
			dataType : "json",
			async : false,
			success : function(res) {
			contrastTree(res);
			}
		});
      }
    }
 	
    function contrastTree(data){
		var myChart = echarts.init(document.getElementById('main'));
	function xAxisformat(data){
	if(data.dateLine.type == "跳出率"){
		data.dateLine.type = data.dateLine.type+"(%)";
		for(var i=0;i<data.dateLine.numbers.length;i++){
			data.contrastLine.numbers[i]= data.contrastLine.numbers[i].replace("%","");
			data.dateLine.numbers[i]= data.dateLine.numbers[i].replace("%","");
		}
	}else if(data.dateLine.type =="平均访问时长"){
		data.dateLine.type= data.dateLine.type+"(/s)";
	}else if(data.dateLine.type== "访问总时长"){
		data.dateLine.type = data.dateLine.type+"(/s)";
	}
	
		var dateLine =data.dateLine;
		for(var a=0;a<dateLine.date.length;a++){
			dateLine.date[a]= dateLine.date[a]+"与"+data.contrastLine.date[a];
		}
		return dateLine.date;
		
	}
		
    option = {
    	    tooltip : {
    	        trigger: 'axis',
    	         formatter: 
    	          function (params) {
				    var num=$("#typeId :selected").text();
				     var list1=params[0].name;
				     var name=list1.split("与");
				     return  "时间&nbsp;&nbsp;" +  "&nbsp;&nbsp;&nbsp;&nbsp;"+num+ "<br/>"
                   + "<lable style='color:#c23531;'>●</lable>"+(name[0]!=undefined?name[0]:"无") + " : " + (params[0].value!=undefined?params[0].value:0) + "<br/>"
                    + "<lable style='color:#2f4554;'>●</lable>"+(name[1]!=undefined?name[1]:"无") + " : " +( params[1].value!=undefined?params[1].value:0)  ;
  					} 
        	
    	    },
    	     /* legend: {//对比线缩略图
    	        data:[data.dateLine.time,data.contrastLine.time]
    	    },  */
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
    	            data : xAxisformat(data),
    	        }
    	    ],
    	    yAxis : [
    	        {
    	        	name:data.dateLine.type,
    	            type : 'value'
    	        }
    	    ],
    	    series : [
    	        {
    	           	name:data.dateLine.time,
    	           	
    	           	itemStyle:{
		            	normal :{
		            		color:'#c23531'
		            	}
		            },
		            
    	            type:'line',
    	            data: data.dateLine.numbers,
    	        },
    	         {
    	            name:data.contrastLine.time,
    	            type:'line',
    	            itemStyle:{
		            	normal :{
		            		color:'#2f4554'
		            	}
		            },
    	            data:data.contrastLine.numbers,
    	        },
    	    ]
    	};
    myChart.setOption(option); 
    window.onresize = myChart.resize;//echar 自适应屏幕
	}
	function contrastTree1(data){
		if(data.dateLine.type == "跳出率"){
			data.dateLine.type = data.dateLine.type+"(%)";
			for(var i=0;i<data.dateLine.numbers.length;i++){
				data.contrastLine.numbers[i]= data.contrastLine.numbers[i].replace("%","");
				data.dateLine.numbers[i]= data.dateLine.numbers[i].replace("%","");
			}
		}else if(data.dateLine.type =="平均访问时长"){
			data.dateLine.type= data.dateLine.type+"(/s)";
		}else if(data.dateLine.type== "访问总时长"){
			data.dateLine.type = data.dateLine.type+"(/s)";
		}
	 var myChart = echarts.init(document.getElementById('main'));
    option = {
    	    tooltip : {
    	        trigger: 'axis',
    	        formatter: function (params) {
				     var num=$("#typeId :selected").text();
				     var list1=params[0].seriesName;
				     var name=list1.split("/");
				     var list2=params[1].seriesName;
				     var name1=list2.split("/");
				     return '时间&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+num+ '<br/>'
                   +'<lable style="color:#c23531;">●</lable>' +name[0]+'&nbsp;'+ params[0].name+ '时 : ' + params[0].value + '<br/>'
                   +'<lable style="color:#2f4554;">●</lable>' +name1[0]+'&nbsp;'+ params[0].name + '时 : ' + params[1].value  ; 
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
    	    calculable : true,
    	    xAxis : [
    	        {
    	            type : 'category',
    	            boundaryGap : false,
    	            data : data.dateLine.date,
    	        }
    	    ],
    	    yAxis : [
    	        {
    	        	name:data.dateLine.type,
    	            type : 'value'
    	        }
    	    ],
    	    series : [
    	        {
    	            name:data.dateLine.time,
    	            type:'line',
    	          
    	            data: data.dateLine.numbers,
    	        },
    	        {
    	            name:data.contrastLine.time,
    	            type:'line',
    	          
    	            data:data.contrastLine.numbers,
    	        },
    	      
    	    ]
    	};
    myChart.setOption(option); 
    window.onresize = myChart.resize;//echar 自适应屏幕
	}
	
	//点击消除对比时间框
	function removeCheck(){
		$("#date_range1").val("");
		$("#date_range1").hide();
		$("#showTime").attr("checked",false);
	}
	
	 //计算日期差
    function DateDiff(sDate) {  //sDate1和sDate2是yyyy-MM-dd格式
	    var aDate, oDate1, oDate2, iDays;
	    aDate = sDate.split("/");
	    oDate1 = new Date(aDate[0]);  //转换为yyyy-MM-dd格式
	    oDate2 = new Date(aDate[1]);
	    iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24); //把相差的毫秒数转换为天数
	    return iDays;  //返回相差天数
	}