$(function() {
		getline(1);
		getdataSource(1,1);
		keyword();
		$("input[type=checkbox]").prop("checked",true);
		$(document).click(function(){
		    $("#searchsug1").hide();
		    $("#searchsug2").hide();
		});
		
	});

function getdataSource(curr,num){
	var age ="";
	var title="";
	var exlevel="";
	var model="";
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var domain =$("#reserchdomain").val();
	var pagename=$("#pagename").val();
	
	var property=url_show();
	if(starttime!=''&&endtime!=''){
		num = 4;
	}
	$("input[name=age]").each(function() {  
        if ($(this).is(':checked')) {  
        	age+=$(this).val()+",";  
        } 
	});
	if(age!=""){
		age=age.substring(0,age.length-1);
	}
	
	$("input[name=tenure]").each(function() {  
        if ($(this).is(':checked')) {  
        	title+=$(this).val()+",";
        } 
	});
	if(title!=""){
		title=title.substring(0,title.length-1);
	}
	
	$("input[name=ex_level]").each(function() {  	
        if ($(this).is(':checked')) {  
        	exlevel+=$(this).val()+",";  
        } 
	});
	if(exlevel!=""){
		exlevel=exlevel.substring(0,exlevel.length-1);
	}
	
	
    $.getJSON("../pageAnalysis/getdataSource.do",{
        pagenum:curr,//向服务端传的参数
        pagesize :10,
        age:age,
		title:title,
		exlevel:exlevel,
		datetype:num,
		starttime:starttime,
		endtime:endtime,
		domain:domain,
		pageName:pagename,
		property:property,
    }, function(res){
    	
    	if(property==1){
    		$("#analysis_property").text("学历");
    	}else if(property==2){
    		$("#analysis_property").text("年龄");
    	}else if(property==3){
    		$("#analysis_property").text("职称");
    	}else if(property==4){
    		$("#analysis_property").text("感兴趣的主题");
    	}
    	
    	var html = "";
    	for(var i =0;res.pageRow[i];i++){
    		html+="<tr>" +
    				"<td>"+res.pageRow[i].classify+"</td>" +
    				"<td>"+pagename+"</td>" +
    				"<td>"+res.pageRow[i].PV+"</td>" +
    				"<td>"+res.pageRow[i].pageNum+"</td>" +
    				"<td>"+res.pageRow[i].UV+"</td>" +
    				"<td>"+res.pageRow[i].UV+"</td>" +
    				"<td>"+res.pageRow[i].pageAccessAvg+"</td>" +
    				"<td>"+res.pageRow[i].contributionNum+"</td>" +
    				"<td>"+res.pageRow[i].logoutNum+"</td>" +
    				"<td>"+res.pageRow[i].logoutNumP+"</td>" +
    				"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"+
					"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"+
    			  "</tr>";
    	}
    	
    	$("#tablebody").html(html);
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
                	datapage(obj.curr);
                }
            }
        });
    });

}



	function keyword(){
			
		$("#reserchdomain").focus(function(){	
			$("#searchsug1").show();
			$("#reserchdomain").keyup(function(event){
				$("#searchsug1").show();
				var keywords=$("#reserchdomain").val();						
				$("#searchsug1 li").remove();			
				$.post("head_word.do",{head_word:keywords},function(data){
					  $("#searchsug1 ul li").remove();
					var list=eval(data);
					for(var i=0;i<list.length;i++){
						var li="<li data-key=\""+list[i]+"\" style=\"line-height: 14px;text-align:left;\" onclick=\"text_show(this);\" ><span>"+list[i]+"</span></li>";
						$("#searchsug1 ul").append(li);			
						$("#searchsug1 ul li").mouseover(function(){
							$("#searchsug1 ul li").removeAttr("class");
							$(this).attr("class","bdsug-s"); 
									});	
							}
						});
					});
				});
			
			$("#pagename").focus(function(){	
			$("#searchsug2").show();
			$("#pagename").keyup(function(event){
				$("#searchsug2").show();
				var keywords=$("#pagename").val();
				
				
				$("#searchsug2 li").remove();			
				$.post("html_word.do",{html_word:keywords},function(data){
					  $("#searchsug2 ul li").remove();
					var list=eval(data);
					for(var i=0;i<list.length;i++){
						var li="<li data-key=\""+list[i]+"\" style=\"line-height: 14px;text-align:left;\" onclick=\"html_show(this);\" ><span>"+list[i]+"</span></li>";
						$("#searchsug2 ul").append(li);			
						$("#searchsug2 ul li").mouseover(function(){
							$("#searchsug2 ul li").removeAttr("class");
							$(this).attr("class","bdsug-s"); 
									});	
							}
						});
					});
				});
	};
	function tree(data) {
		var list=eval(data);
		console.info(data);
		var node=eval(list[0]);
		var type = $("#type").find("option:selected").text();
		var format="";
		if(node.length>0){
			if(node[0]=='00')
			{
			format='{b}时<br>'+type+': {c}';
			}
			else 
			{
			format='{b}日<br>'+type+': {c}';
			}
		}
		var myChart = echarts.init(document.getElementById('main'));
		option = {
			tooltip : {
				formatter:format
			},
			/* legend : {
				data : data.date
			}, */
			
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
			xAxis : [ {
				type : 'category',
				data : list[0]
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			dataZoom: [{start: 0, end: 10,}],
			series : [ {
    	            name:data.type,
    	            type:'line',
    	            data: list[1],
    	            label:{
						normal:{
							show:true,
							formatter:'{c}',
							position:['0%', '-10%']
							}
					}
    	        }			
			]
		};

		/* for ( var i = 0; i < data.title.length; i++) {
			var name = data.title[i];
			var num = new Array();
			num = data.content[name];
			option.series.push({
				name : name,
				type : 'line',
				data : num
			})
		} */
		myChart.setOption(option);
		window.onresize = myChart.resize;//echar 自适应屏幕
	}

	function checkallbox() {
		if ($("#checkallbox").is(':checked')) {
			$("input[type=checkbox]").prop("checked", true);
		} else {
			$("input[type=checkbox]").prop("checked", false);
		}
	}

	function checkallexlevel() {
		if ($("#checkallexlevel").is(':checked')) {
			$("input[name=ex_level]").prop("checked", true);
		} else {
			$("input[name=ex_level]").prop("checked", false);
		}
		
		if($("#checkallage").is(':checked')&&$("#checkallexlevel").is(':checked')&&$("#checkalltenure").is(':checked')){
			$("#checkallbox").prop("checked", true);		
		}else{
			$("#checkallbox").prop("checked", false);
		}
	
	}

	function checkallmodel() {
		if ($("#checkallmodel").is(':checked')) {
			$("input[name=model]").prop("checked", true);
		} else {
			$("input[name=model]").prop("checked", false);
		}
	}

	function checkallage() {
		if ($("#checkallage").is(':checked')) {
			$("input[name=age]").prop("checked", true);
		} else {
			$("input[name=age]").prop("checked", false);
		}
			
		if($("#checkallage").is(':checked')&&$("#checkallexlevel").is(':checked')&&$("#checkalltenure").is(':checked')){
			$("#checkallbox").prop("checked", true);		
		}	
		else{
			$("#checkallbox").prop("checked", false);
		}
	}

	function checkalltenure() {
		if ($("#checkalltenure").is(':checked')) {
			$("input[name=tenure]").prop("checked", true);
		} else {
			$("input[name=tenure]").prop("checked", false);
		}
		
		if($("#checkallage").is(':checked')&&$("#checkallexlevel").is(':checked')&&$("#checkalltenure").is(':checked'))
		{
			$("#checkallbox").prop("checked", true);		
		}	
		else{
			$("#checkallbox").prop("checked", false);
		}
	}
	
	
	function removetime(){
	$("#starttime").val("");
	$("#endtime").val("");
	}
	
	function querytime(obj, num) {
		$("#time").val(num);
		removetime();
		getline(num);
		getdataSource(1,num);
		$("button[name=time]").each(function() {
			$(this).removeClass("btn-success");
		});
		$(obj).addClass("btn-success");
	}
	
	function getselectline(){
		var num ="";
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
		if(starttime!=''&&endtime!=''){
			num = 4;
			$("button[name=time]").each(function() {
			$(this).removeClass("btn-success");
		});
		}else{
			num = $("#time").val();
		}
		getline(num);
		getdataSource(1,num);
	}
	
	
	var totalRow = 0;
    var pages;
    var groups;
	
	
	function getline(num) {
		var age = "";//年龄
		var title = "";//职称
		var exlevel = "";//学历
		var pagename = "";//页面名称
		var reserchdomain = "";//感兴趣主题
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
		var property=url_show();
		if (starttime != '' && endtime != '') {
			num = 4;
		}
		$("input[name=age]").each(function() {
			if ($(this).is(':checked')) {
				age += $(this).val() + ",";
			}
		});
		if (age != "") {
			age = age.substring(0, age.length - 1);
		}

		$("input[name=tenure]").each(function() {
			if ($(this).is(':checked')) {
				title += $(this).val() + ",";
			}
		});
		if (title != "") {
			title = title.substring(0, title.length - 1);
		}
	
		$("input[name=ex_level]").each(function() {
			if ($(this).is(':checked')) {
				exlevel += $(this).val() + ",";
			}
		});
		if (exlevel != "") {
			exlevel = exlevel.substring(0, exlevel.length - 1);
		}

		/* $("input[name=model]").each(function() {
			if ($(this).is(':checked')) {
				model += $(this).val() + ",";
			}
		});
		if (model != "") {
			model = model.substring(0, model.length - 1);
		} */
		if($("#reserchdomain").val() != ""){
		var reserchdomain = $("#reserchdomain").val();
		}
		if($("#pagename").val() != ""){
		 var pagename = $("#pagename").val();
		}

		var type = $("#type").find("option:selected").val();
		$.ajax({
			type : "POST",
			url : "../pageAnalysis/getline.do",
			data : {
				age : age,
				title : title,
				exlevel : exlevel,
				//model : model,
				reserchdomain : reserchdomain,//感兴趣主题
				pageName : pagename,//页面名称
				datetype : num,
				type : type,
				starttime : starttime,
				endtime : endtime,
				property:property,
			},
			dataType : "json",
			success : function(data) {							    
				tree(data);
				totalRow = data.pageTotal;
			    var pageSize = data.pageSize;
			    
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
				
				
			}
		});
	}
	
	
	function text_show(data)
	{
	$("#reserchdomain").val($(data).text());
	$("#searchsug1").css("display","none");
	/* $("#reserchdomain").blur(function(){
		$("#searchsug1").hide();
		}); */
	}
		
	function html_show(data){
	$("#pagename").val($(data).text());
	$("#searchsug2").css("display","none");
/* 	$("#pagename").blur(function(){
		$("#searchsug2").hide();
		}); */
	}
	
	function url_show(){
		var rt = 0;
		var checkEx_levelCount=$("input[type=checkbox][name='ex_level']").is(':checked');
		var checkAgeCount=$("input[type=checkbox][name='age']").is(':checked');
		var checkTenureCount=$("input[type=checkbox][name='tenure']").is(':checked');
		var domain=$("#reserchdomain").val();
		
		if(checkEx_levelCount && !checkAgeCount && !checkTenureCount &&  ""==domain){
			rt = 1;
		}else if(checkAgeCount && !checkEx_levelCount && !checkTenureCount && ""==domain){
			rt = 2;
		}else if(checkTenureCount && !checkEx_levelCount && !checkAgeCount && ""==domain){
			rt = 3;
		}else if(""!=domain && !checkTenureCount && !checkEx_levelCount && !checkAgeCount){
			rt = 4;
		}
		
		return rt;
	}

	
	
	
	