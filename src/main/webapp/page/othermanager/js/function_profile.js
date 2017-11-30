var property=0;


$(function(){
	$("input[type=checkbox][name!='item'][id!='showall']").prop("checked",true);
	getline(1);
	gettable(1,1);
	keyword();
	
	$(document).click(function(){
	    $("#searchsug1").hide();
	});
	
});



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


/**生成饼图*/
function pie(data){
	var myChart = echarts.init(document.getElementById('pie'));
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
			            
	 
	if(null!=data.title){
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
		    	);
		    }

	}	 
	 
	 myChart.setOption(option); 
}

function getline(num){
	var age ="";
	var title="";
	var exlevel="";
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var domain =$("#reserchdomain").val();
	
	property=urlshow();
	
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
	
	var type=$("#change_type").find("option:selected").val();
	
	$.ajax( {  
		type : "POST",  
		url : "../functionProfile/getline.do",
		data : {
			age:age,
			title:title,
			exlevel:exlevel,
			datetype:num,
			type:type,
			starttime:starttime,
			endtime:endtime,
			domain:domain,
			property:property
		},
		dataType : "json",
		success : function(data) {
			/*$("#reserchdomain").val(data.reserch_domain);
			var availableTags = new Array();
		    var val  = $("#reserch_domain").val();
		    availableTags=val.split(",");
		    $( "#domain" ).autocomplete({
		        source: availableTags
		    });*/
			tree(data);
			pie(data);
		}
		});
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
   
    
    if(null!=data.title){
        for(var i =0;i<data.title.length;i++){
        	var name = data.title[i];
        	var num=new Array();
        	num =data.content[name];
        	option.series.push({
        		name:name,
        		type:'line',
        		data:num,
        	});
        }

    }

    
    myChart.setOption(option); 
}


/**
 * 加载列表数据
 */
//分页显示
function gettable(curr,num){
	var age ="";
	var title="";
	var exlevel="";
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var domain =$("#reserchdomain").val();
	
	property=urlshow();
	
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
	
	var type=$("#change_type").find("option:selected").val();
    $.getJSON("../functionProfile/gettable.do", {
        pagenum: curr,//向服务端传的参数
        pagesize :10,
        age:age,
		title:title,
		exlevel:exlevel,
		datetype:num,
		type:type,
		starttime:starttime,
		endtime:endtime,
		domain:domain,
		property:property
    }, function(res){
    	var html = "";
    	var htmltitle="";
    	var htmltitleval="";
    	if(property==1){
    		htmltitle="<th>学历</th>";
    	}else if(property==2){
    		htmltitle="<th>年龄</th>";
    	}else if(property==3){
    		htmltitle="<th>职称</th>";
    	}else if(property==4){
    		htmltitle="<th>主题</th>";
    	}
    	
    
    	var ht="<tr>" +htmltitle
				+"<th>检索数</th>" 
				+"<th>浏览数</th>" 
				+"<th>下载数</th>" 
				+"<th>收藏数</th>" 
				+"<th>笔记数</th>" 
				+"<th>分享数</th>" 
				+"<th>导出数</th>" 
				+"<th>跳转数</th>" 
				+"<th>订阅数</th>" 
				+"</tr>";
    	
    	for(var i =0;res.pageRow[i];i++){
    		if(property!=0){
        		htmltitleval="<td>"+res.pageRow[i].classify+"</td>";
    		}
    		
    		html+="<tr>" +htmltitleval
					+"<td>"+(res.pageRow[i].searchNum==null?0:res.pageRow[i].searchNum)+"</td>" 
					+"<td>"+(res.pageRow[i].browseNum==null?0:res.pageRow[i].browseNum)+"</td>" 
					+"<td>"+(res.pageRow[i].downloadNum==null?0:res.pageRow[i].downloadNum)+"</td>" 
					+"<td>"+(res.pageRow[i].collectNum==null?0:res.pageRow[i].collectNum)+"</td>" 
					+"<td>"+(res.pageRow[i].noteNum==null?0:res.pageRow[i].noteNum)+"</td>" 
					+"<td>"+(res.pageRow[i].shareNum==null?0:res.pageRow[i].shareNum)+"</td>" 
					+"<td>"+(res.pageRow[i].exportNum==null?0:res.pageRow[i].exportNum)+"</td>" 
					+"<td>"+(res.pageRow[i].breakNum==null?0:res.pageRow[i].breakNum)+"</td>" 
					+"<td>"+(res.pageRow[i].subscriptionNum==null?0:res.pageRow[i].subscriptionNum)+"</td>" 
					+"</tr>";
    	}
    	$("#tablebody").html(ht+html);
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

function urlshow(){
	
	var rt = 0;
	var checkEx_levelCount=$("input[type=checkbox][name='ex_level']").is(':checked');
	var checkAgeCount=$("input[type=checkbox][name='age']").is(':checked');
	var checkTenureCount=$("input[type=checkbox][name='tenure']").is(':checked');
	var domain=$("#reserchdomain").val();
	
	
	if(($("[name='ex_level']:checked").length+$("[name='age']:checked").length+$("[name='tenure']:checked").length)>1){
		$("#change_type").show();
		$("#more").hide();
	}else{
		$("#change_type").hide();
		$("#more").show();
	}
	
	
	if(checkEx_levelCount && !checkAgeCount && !checkTenureCount && ""==domain){
		rt = 1;
	}else if(checkAgeCount && !checkEx_levelCount && !checkTenureCount && ""==domain){
		rt = 2;
	}else if(checkTenureCount && !checkEx_levelCount && !checkAgeCount && ""==domain){
		rt = 3;
	}else if(""!=domain && !checkTenureCount && !checkEx_levelCount && !checkAgeCount){
		rt = 4;
	}else{
		$("#change_type").show();
		$("#more").hide();
	}
	
	/*$("input[name=chec]").each(function(){
		if($(this).is(":checked")){
			rt+=1;
		}
	})
	 if(rt==1){
		 var st =0;
		 var objs1=$("input[name=ex_level]");
		 var objs2=$("input[name=age]");
		 var objs3=$("input[name=tenure]");
		 var obj1 = $("#checkallexlevel");
		 var obj2 = $("#checkallage");
		 var obj3 = $("#checkalltenure");
		 $(objs1).each(function(){
			 if($(this).is(":checked")&&!$(obj1).is(":checked")){
				 st = st+1;
			 }
		 })
		 $(objs2).each(function(){
			 if($(this).is(":checked")&&!$(obj2).is(":checked")){
				 st = st+1;
			 }
		 })
		 $(objs3).each(function(){
			 if($(this).is(":checked")&&!$(obj3).is(":checked")){
				 st = st+1;
			 }
		 })
		 if(st==0){
			 
			 if($(obj1).is(":checked")){
				 rt=1;
			 }
			 if($(obj2).is(":checked")){
				 rt=2;
			 }
			 if($(obj3).is(":checked")){
				 rt=3;
			 }
			 $("#change_type").show();
			 $("#showall").hide();
		 }else{
			 rt=0;
			 $("#change_type").hide();
			 $("#showall").show();
		 }
	 }else{
		 $("#change_type").hide();
		 $("#showall").show();
	 }*/
	
	return rt;
}

function querytime(obj,num){
	var nums  = num;
	$("#time").val(num);
	getline(num);
	 $("#starttime").val("");
	 $("#endtime").val("");
	$("button[name=time]").each(function(){
		$(this).removeClass("btn-success");
	});
	$(obj).addClass("btn-success");
	gettable(1,nums);
}

function checkallbox(){
	if ($("#checkallbox").is(':checked')) {  
		 $("input[type=checkbox][name!='item'][id!='showall']").prop("checked",true);
   }else{
   	 $("input[type=checkbox][name!='item'][id!='showall']").prop("checked",false);
   }
	urlshow();
}

function checkallexlevel(){
	if ($("#checkallexlevel").is(':checked')) {  
		 $("input[name=ex_level]").prop("checked",true);
   }else{
   	 $("input[name=ex_level]").prop("checked",false);
   }
	urlshow();
}


function checkallage(){
	if ($("#checkallage").is(':checked')) {  
		 $("input[name=age]").prop("checked",true);
   }else{
   	 $("input[name=age]").prop("checked",false);
   }
	urlshow();
}

function checkalltenure(){
	if ($("#checkalltenure").is(':checked')) {  
		 $("input[name=tenure]").prop("checked",true);
   }else{
   	 $("input[name=tenure]").prop("checked",false);
   }
	urlshow();
}

function checkall(obj,id){
	var rt = true;
	urlshow();
	var name = $(obj).attr("name");
	$("input[name="+name+"]").each(function(){
		if (!$(this).is(':checked')) {  
	   	 	rt = false;
	   }
	});
	if(rt){
		$("#"+id).prop("checked",true);
	}else{
		$("#"+id).prop("checked",false);
	}
}

function getselectline(n){
	var num ="";
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	if(starttime!=''&&endtime!=''){
		num = 4;
		$("#zuotian").removeClass("btn-success");
		$("#qitian").removeClass("btn-success");
		$("#yigeyue").removeClass("btn-success");
	}else{
		num = $("#time").val();
	}
	getline(num);
	if(n==null){
		gettable(1,num);
	}
}

function checksource(){
	$("[name='item']").prop("checked",$("#showall").prop("checked"));
}

function checkitem(){
	if($("[name='item']:checked").length==$("[name='item']").length){
		$("#showall").prop("checked",true);
	}else{
		$("#showall").prop("checked",false);
	}
}

function Indexanalysis(indexType){
	var num=0;
	var age ="";
	var title="";
	var exlevel="";
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var domain =$("#reserchdomain").val();
	var type="";
	property=urlshow();
	
	if(starttime!=''&&endtime!=''){
		$("#zuotian").removeClass("btn-success");
		$("#qitian").removeClass("btn-success");
		$("#yigeyue").removeClass("btn-success");
		num = 4;
	}else{
		num = $("#time").val();
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
	
	if(indexType==1){
		type=$("#change_type").find("option:selected").val()+",";
	}else{
		$("[name='item']:checked").each(function(){
			type+=$(this).val()+",";
		});
	}
	if(type!=""){
		type=type.substring(0,type.length-1);
	}
	
	$.ajax( {  
		type : "POST",  
		url : "../functionProfile/getline.do",
		data : {
			age:age,
			title:title,
			exlevel:exlevel,
			datetype:num,
			type:type,
			starttime:starttime,
			endtime:endtime,
			domain:domain,
			property:property
		},
		dataType : "json",
		success : function(data) {
			$("#reserch_domain").val(data.reserch_domain);
			var availableTags = new Array();
		    var val  = $("#reserch_domain").val();
		    availableTags=val.split(",");
		    $( "#domain" ).autocomplete({
		        source: availableTags
		    });
			tree(data);
			pie(data);
			
		}
		});
}



