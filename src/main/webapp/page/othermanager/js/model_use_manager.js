$(function(){
	$("input[type=checkbox]").prop("checked",true);
	keyword();
	getline(1);
	gettable(1,1);
	
	$(document).click(function(){
	    $("#searchsug1").hide();
	});
	
})

function querytime(obj,num){
	var nums  = num;
	$("#time").val(num);
	getline(num);
	 
	$("button[name=time]").each(function(){
		$(this).removeClass("btn-success");
	})
	$(obj).addClass("btn-success");
	gettable(1,nums)
}

//分页显示
function gettable(curr,num){
	var num = num;
	var age ="";
	var title="";
	var exlevel="";
	var model="";
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var domain =$("#reserchdomain").val();
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
        	title+=$(this).val()+","
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
	
	$("input[name=model]").each(function() {  	
        if ($(this).is(':checked')) {  
        	model+=$(this).val()+",";  
        } 
	});
	if(model!=""){
		model=model.substring(0,model.length-1);
	}
	
	var type=$("#type").find("option:selected").val();
    $.getJSON("../modelanalysis/gettable.do", {
        pagenum: curr,//向服务端传的参数
        pagesize :10,
        age:age,
		title:title,
		exlevel:exlevel,
		model:model,
		datetype:num,
		model:model,
		type:type,
		starttime:starttime,
		endtime:endtime,
		domain:domain,
		property:property,
    }, function(res){
    	
    	if(property==0){
    		$("#analysis_property").text("功能模块");
    	}else if(property==1){
    		$("#analysis_property").text("学历");
    	}else if(property==2){
    		$("#analysis_property").text("年龄");
    	}else if(property==3){
    		$("#analysis_property").text("职称");
    	}
    	
    	var html = "";
    	
    	for(var i =0;res.pageRow[i];i++){
    		html+="<tr>" +
    				"<td>"+res.pageRow[i].modelname+"</td>" +
    				"<td>"+res.pageRow[i].modelPV+"</td>" +
    				"<td>"+res.pageRow[i].modelVV+"</td>" +
    				"<td>"+res.pageRow[i].modelUV+"</td>" +
    				"<td>1000</td>" +
    				"<td>"+res.pageRow[i].modelUV+"</td>" +
    				"<td>"+res.pageRow[i].modeAUP+"</td>" +
    				"<td></td>" +
    			  "</tr>  "
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
};

function getline(num){
	var num = num;
	var age ="";
	var title="";
	var exlevel="";
	var model="";
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var domain =$("#reserchdomain").val();
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
        	title+=$(this).val()+","
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
	
	$("input[name=model]").each(function() {  	
        if ($(this).is(':checked')) {  
        	model+=$(this).val()+",";  
        } 
	});
	if(model!=""){
		model=model.substring(0,model.length-1);
	}
	
	var type=$("#type").find("option:selected").val();
	
	$.ajax( {  
		type : "POST",  
		url : "../modelanalysis/getline.do",
		data : {
			age:age,
			title:title,
			exlevel:exlevel,
			model:model,
			datetype:num,
			model:model,
			type:type,
			starttime:starttime,
			endtime:endtime,
			domain:domain,
			property:url_show(),
		},
		dataType : "json",
		success : function(data){
			/*$("#reserchdomain").val(data.reserch_domain);
			var availableTags = new Array();
		    var val  = $("#reserchdomain").val();
		    availableTags=val.split(",");
		    $( "#domain" ).autocomplete({
		        source: availableTags
		    });*/
			tree(data);
		}
		});
}

	function keyword(){
		
		$("#reserchdomain").focus(function(){	
			$("#searchsug1").show();
			$("#reserchdomain").keyup(function(event){
				$("#searchsug1").show();
				var keywords=$("#reserchdomain").val();						
				$("#searchsug1 li").remove();			
				$.post("../pageAnalysis/head_word.do",{head_word:keywords},function(data){
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
		
	}

	function text_show(data){
		$("#reserchdomain").val($(data).text());
		$("#searchsug1").css("display","none");
	}

	function tree(data){
	    var myChart = echarts.init(document.getElementById('main'));
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
	    	option.series.push({
	    		name:name,
	    		type:'line',
	    		data:num
	    	})
	    }
	    myChart.setOption(option); 
	}
	
	function getselectline(){
		var num ="";
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
		if(starttime!=''&&endtime!=''){
			num = 4;
		}else{
			num = $("#time").val();
		}
		getline(num);
		gettable(1,num);
	}
	
	function checkallbox(){
		if ($("#checkallbox").is(':checked')) {  
			 $("input[type=checkbox]").prop("checked",true);
	   }else{
	   	 $("input[type=checkbox]").prop("checked",false);
	   }
	}
	
	function checkallexlevel(){
		if ($("#checkallexlevel").is(':checked')) {  
			 $("input[name=ex_level]").prop("checked",true);
	   }else{
	   	 $("input[name=ex_level]").prop("checked",false);
	   }
	}
	
	function checkallmodel(){
		if ($("#checkallmodel").is(':checked')) {  
			 $("input[name=model]").prop("checked",true);
	   }else{
	   	 $("input[name=model]").prop("checked",false);
	   }
	}
	
	function checkallage(){
		if ($("#checkallage").is(':checked')) {  
			 $("input[name=age]").prop("checked",true);
	   }else{
	   	 $("input[name=age]").prop("checked",false);
	   }
	}
	
	function checkalltenure(){
		if ($("#checkalltenure").is(':checked')) {  
			 $("input[name=tenure]").prop("checked",true);
	   }else{
	   	 $("input[name=tenure]").prop("checked",false);
	   }
	}
	
	function url_show(){
		
		var rt = 0;
		var checkEx_levelCount=$("input[type=checkbox][name='ex_level']").is(':checked');
		var checkAgeCount=$("input[type=checkbox][name='age']").is(':checked');
		var checkTenureCount=$("input[type=checkbox][name='tenure']").is(':checked');
		var checkModelCount=$("input[type=checkbox][name='model']").is(':checked');
		var domain=$("#reserchdomain").val();
		
		if(checkEx_levelCount && !checkAgeCount && !checkTenureCount && !checkModelCount && ""==domain){
			rt = 1;
		}else if(checkAgeCount && !checkEx_levelCount && !checkTenureCount && !checkModelCount && ""==domain){
			rt = 2;
		}else if(checkTenureCount && !checkEx_levelCount && !checkAgeCount && !checkModelCount && ""==domain){
			rt = 3;
		}else if(""!=domain && !checkTenureCount && !checkEx_levelCount && !checkAgeCount && !checkModelCount){
			rt = 4;
		}
		
		return rt;
	}
	
	
	