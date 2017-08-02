$(function() {
		getline(1);
		getdataSource(1);
		keyword();
		$("input[type=checkbox]").prop("checked",true);
		$(document).click(function(){
		    $("#searchsug1").hide();
		    $("#searchsug2").hide();
		});
		
	});

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
		
		if($("#checkallage").is(':checked')&&$("#checkallexlevel").is(':checked')&&$("#checkalltenure").is(':checked'))
		{
			$("#checkallbox").prop("checked", true);		
		}	
		else{
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
			
		if($("#checkallage").is(':checked')&&$("#checkallexlevel").is(':checked')&&$("#checkalltenure").is(':checked'))
		{
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
		getdataSource(num);
		$("button[name=time]").each(function() {
			$(this).removeClass("btn-success");
		})
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
		})
		}else{
			num = $("#time").val();
		}
		getline(num);
		getdataSource(num)
	}
	
	
	var totalRow = 0;
    var pages;
    var groups;
	
	
	function getline(num) {
		var num = num;
		var age = "";//年龄
		var title = "";//职称
		var exlevel = "";//学历
		var pagename = "";//页面名称
		var reserchdomain = "";//感兴趣主题
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
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
				title += $(this).val() + ","
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
				endtime : endtime
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
		
	function html_show(data)
	{
	$("#pagename").val($(data).text());
	$("#searchsug2").css("display","none");
/* 	$("#pagename").blur(function(){
		$("#searchsug2").hide();
		}); */
	}
	function exlevel_check(){
		var i=0;
		$("input[name=ex_level]").each(function() {
			if ($(this).is(':checked')) {
				i++;
			}
		});	
		var j=0;
		$("input[name=age]").each(function() {
			if ($(this).is(':checked')) {
				j++;
			}
		});
		var k=0;
		$("input[name=tenure]").each(function() {
			if ($(this).is(':checked')) {
				k++;
			}
		});
		if (i==5) {
			$("#checkallexlevel").prop("checked", true);
		} else {
			$("#checkallexlevel").prop("checked", false);
		}		
			if (j==6) {
				$("#checkallage").prop("checked", true);
			} else {
				$("#checkallage").prop("checked", false);
			}	
			
			if (k==5) {
				$("#checkalltenure").prop("checked", true);
			} else {
				$("#checkalltenure").prop("checked", false);
			}
			
			if(i==5&&j==6 &&k==5)
				{
				$("#checkallbox").prop("checked", true);
				}
			else{
				$("#checkallbox").prop("checked", false);
			}
	}

	function getdataSource(num){
	
		var num = num;
		var age = "";//年龄
		var title = "";//职称
		var exlevel = "";//学历
		var pagename = "";//页面名称
		var reserchdomain = "";//感兴趣主题
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
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
				title += $(this).val() + ","
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
		
		if($("#reserchdomain").val() != ""){
			var reserchdomain = $("#reserchdomain").val();
			}
			if($("#pagename").val() != ""){
			 var pagename = $("#pagename").val();
			}
		
		if(age.length==0&&exlevel.length==0&&title.length!=0 && ""==reserchdomain)
		{	
			$.ajax({
				type : "POST",
				url : "../pageAnalysis/getdataSource.do",
				data : {
					age : 'null',
					title : title,
					exlevel : 'null',
					//model : model,
					reserchdomain : reserchdomain,//感兴趣主题
					pageName : pagename,//页面名称
					datetype : num,
					starttime : starttime,
					endtime : endtime
				},
				dataType : "json",
				success : function(data) {							    
					titlelist(data);
				}
			});
		}
		else if(age.length!=0&&exlevel.length==0&&title.length==0 && ""==reserchdomain)
		{	
			$.ajax({
				type : "POST",
				url : "../pageAnalysis/getdataSource.do",
				data : {
					age : age,
					title : 'null',
					exlevel : 'null',
					//model : model,
					reserchdomain : reserchdomain,//感兴趣主题
					pageName : pagename,//页面名称
					datetype : num,
					starttime : starttime,
					endtime : endtime
				},
				dataType : "json",
				success : function(data) {							    
					agelist(data);
				}
			});
		}
		else if(age.length==0&&exlevel.length!=0&&title.length==0 && ""==reserchdomain)
		{	
			$.ajax({
				type : "POST",
				url : "../pageAnalysis/getdataSource.do",
				data : {
					age : 'null',
					title : 'null',
					exlevel : exlevel ,
					//model : model,
					reserchdomain : reserchdomain,//感兴趣主题
					pageName : pagename,//页面名称
					datetype : num,		
					starttime : starttime,
					endtime : endtime
				},
				dataType : "json",
				success : function(data) {							    
					exlevellist(data);
				}
			});
		}else if(""!=reserchdomain && age.length==0 && exlevel.length==0 && title.length==0 ){
			$.ajax({
				type : "POST",
				url : "../pageAnalysis/getdataSource.do",
				data : {
					age : age,
					title : title,
					exlevel : exlevel,
					//model : model,
					reserchdomain : reserchdomain,//感兴趣主题
					pageName : pagename,//页面名称
					datetype : num,
					starttime : starttime,
					endtime : endtime
				},
				dataType : "json",
				success : function(data) {							    
					reserchdomainlist(data);
				}
			});
		}else {
			$.ajax({
				type : "POST",
				url : "../pageAnalysis/getonedataSource.do",
				data : {
					age : age,
					title : title,
					exlevel : exlevel,
					//model : model,
					reserchdomain : reserchdomain,//感兴趣主题
					pageName : pagename,//页面名称
					datetype : num,
					starttime : starttime,
					endtime : endtime
				},
				dataType : "json",
				success : function(data) {							    
					otherlist(data);
				}
			});
		}	
	}
	
	function reserchdomainlist(data){
		
		
		$("#titleName").text("主题");
		$("#tabulation tr:not( tr:first)").remove();
		var pagename=$("#pagename").val();
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		if(pagename==""||pagename==undefined||re.test(pagename))
			{
			pagename="全部";
			}
		var list=eval(data);
		var json=list[0];
		
		var tr="<tr>"
			+"<td>"+json["value"]+"</td>"
			+"<td>"+pagename+"</td>"
			+"<td>"+json["value1"]+"</td>"
			+"<td>"+json["value2"]+"</td>"
			+"<td>"+json["value3"]+"</td>"
			+"<td>"+json["value4"]+"</td>"
			+"<td>"+json["value5"]+"</td>"
			+"<td>"+json["value6"]+"</td>"
			+"<td>"+json["value7"]+"</td>"
			+"<td>"+json["value8"]+"%</td>"
			+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
			+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
			+"</tr>";
			$("#tabulation tbody").append(tr);
		
		
	}
	
	
	function titlelist(data)
	{		
		$("#titleName").text("职称");
		$("#tabulation tr:not( tr:first)").remove();
		var pagename=$("#pagename").val();
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		if(pagename==""||pagename==undefined||re.test(pagename))
			{
			pagename="全部";
			}
		var list=eval(data);
		for(var i=0;i<list.length;i++)
		{
			if(list[i].type=="0")
			{	
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>初级</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="1")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>中级</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="2")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>副高级</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="3")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>正高级</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="4")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>其他</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}		
		}
		
	}
	
	function otherlist(data)
	{	
		$("#tabulation tr:not( tr:first)").remove();
		var pagename=$("#pagename").val();
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		if(pagename==""||pagename==undefined||re.test(pagename))
			{
			pagename="全部";
			}
		var list=eval(data);
		var json= eval(list[0].value);
		var tr="<tr>"
		+"<td>全部</td>"
		+"<td>"+pagename+"</td>"
		+"<td>"+json[0].value+"</td>"
		+"<td>"+json[1].value+"</td>"
		+"<td>"+json[2].value+"</td>"
		+"<td>"+json[3].value+"</td>"
		+"<td>"+json[4].value+"</td>"
		+"<td>"+json[5].value+"</td>"
		+"<td>"+json[6].value+"</td>"
		+"<td>"+json[7].value+"%</td>"	
		+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
		+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
		+"</tr>";
		$("#tabulation tbody").append(tr);
	}
	
	function agelist(data)
	{	
		$("#titleName").text("年龄");
		$("#tabulation tr:not( tr:first)").remove();
		var pagename=$("#pagename").val();
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		if(pagename==""||pagename==undefined||re.test(pagename))
			{
			pagename="全部";
			}
		var list=eval(data);
		for(var i=0;i<list.length;i++)
		{	
			if(list[i].type=='1')
			{	
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>20岁以下</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="2")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>20岁至29岁</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="3")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>30岁至39岁</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="4")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>40岁至49岁</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="5")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>50岁至59岁</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="6")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>60岁以上</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript：void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript：void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
		}
	}
	
	function  exlevellist(data)
	{
		$("#titleName").text("学历");
		$("#tabulation tr:not( tr:first)").remove();
		var pagename=$("#pagename").val();
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		if(pagename==""||pagename==undefined||re.test(pagename))
			{
			pagename="全部";
			}
		var list=eval(data);
		for(var i=0;i<list.length;i++)
		{
			if(list[i].type=="0")
			{	   
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>大专</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="1")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>本科</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="2")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>硕士</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="3")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>博士</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}
			else if(list[i].type=="4")
			{
				var json=eval(list[i].value);
				var tr="<tr>"
					+"<td>其他</td>"
					+"<td>"+pagename+"</td>"
					+"<td>"+json[0].value+"</td>"
					+"<td>"+json[1].value+"</td>"
					+"<td>"+json[2].value+"</td>"
					+"<td>"+json[3].value+"</td>"
					+"<td>"+json[4].value+"</td>"
					+"<td>"+json[5].value+"</td>"
					+"<td>"+json[6].value+"</td>"
					+"<td>"+json[7].value+"%</td>"	
					+"<td><a href=\"javaScript:void(0);\">查看热力图</a></td>"
					+"<td><a href=\"javaScript:void(0);\">查看链接点击图</a></td>"
					+"</tr>";
					$("#tabulation tbody").append(tr);
			}			
		}
	}