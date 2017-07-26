var num = 1;
function query(){
	numStart = $.trim($("#numStart").val());//卡号开始
	numEnd = $.trim($("#numEnd").val());//卡号结束
	batchName = $("#batchName").val().trim();//批次
	if((numStart == '' || numStart == null) && (numEnd == '' || numEnd == null)){
		$("#result").hide();
		$("#result2").show();//卡号为空，显示列表2  批次表
	}else{
		$("#result2").hide();
		$("#result").show();//卡号不为空，显示列表1 充值卡表
	}
	
	Page(1);
}
//------------------------------------分页--------------
var batchName = "";
var numStart = "";
var numEnd = "";
var applyDepartment = "";
var applyPerson = "";
var startTime = "";
var endTime = "";
var cardType = "";
var invokeState = "";
var batchState = "";
var pageNum;
var pageSize = 8;
function Page(curr){
	//序号
	var serial = ((curr||1) - 1) * pageSize + 1;
	batchName = $("#batchName").val();//批次
	numStart = $.trim($("#numStart").val());//卡号开始
	numEnd = $.trim($("#numEnd").val());//卡号结束
	applyDepartment = $("#department").val().trim();//申请部门
	applyPerson = $("#person").val().trim();//申请人
	startTime = $("#startTime").val();//开始时间
	endTime = $("#endTime").val();//结束时间
	if((numStart == '' || numStart == null) && (numEnd == '' || numEnd == null)){
		num = 2;//卡号为空，显示列表2  批次表
	}else{
		num = 1;//卡号不为空，显示列表1 充值卡表
	}
	//--------------充值卡类型-----------------
	if($("#all_type").prop("checked") == false){//不全选
		cardType = "";
		$("input[name=cardType]").each(function(){
			if($(this).prop("checked") == true){
				cardType += $(this).val() + ",";
			}
		})
	}
	//--------------批次状态-----------------
	if($("#all_state").prop("checked") == false){//不全选
		batchState = "";
		$("input[name=checkState]").each(function(){
			if($(this).prop("checked") == true){
				batchState += $(this).val() + ",";
			}
		})
	}
	//-------------充值卡状态-----------------
	if($("#activate").is(":visible") && $("#card_state").prop("checked") == false){//不全选
		invokeState = "";
		$("input[name=invokeState]").each(function(){
			if($(this).prop("checked") == true){
				invokeState += $(this).val() + ",";
			}
		})
	}
	if(num == 1){//卡号不为空，显示列表1 充值卡表
	$.ajax({
		type : "post",  
		url : "../card/queryCard.do",
		data :{ 
			"batchName" : batchName, 
			"numStart" : numStart,
			"numEnd" : numEnd, 
			"applyDepartment" : applyDepartment,
			"applyPerson" : applyPerson,
			"startTime" : startTime,
			"endTime" : endTime,
			"cardType" : cardType,
			"batchState" : batchState,
			"invokeState" : invokeState,
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
		},
		dataType : "json",
		success : function(data){
			$("#list").empty();//清空
			var value="[";
			if(data.pageRow[0] != null){
				$.each(data.pageRow,function (i) {
//					type="";
//					for(var j=0;j<Code_list.length;j++){
//						if(data.pageRow[i].type==Code_list[j].cardTypeCode){
//							type=Code_list[j].cardTypeName;
//						}
//					}
					var invokeState = "";
					if(data.pageRow[i].invokeState == 1){
						invokeState = "未激活";
					}
					if(data.pageRow[i].invokeState == 2){
						invokeState = "已激活";
					}
					if(data.pageRow[i].invokeState == 3){
						invokeState = "已过期";
					}
					//------------------判断是否为空-------------------
					var invokeDate;
					if(data.pageRow[i].invokeDate == null){
						invokeDate = "";
					}else{
						invokeDate = dateChange(data.pageRow[i].invokeDate);
					}
					var invokeUser;
					if(data.pageRow[i].invokeUser == null){
						invokeUser = "";
					}else{
						invokeUser = data.pageRow[i].invokeUser;
					}
					var invokeIp;
					if(data.pageRow[i].invokeIp == null){
						invokeIp = "";
					}else{
						invokeIp = data.pageRow[i].invokeIp;
					}
					var html ='<tr>'
	                  +'<td>'+(serial+i)+'</td>'
	                  +'<td>'+data.pageRow[i].batchName+'</td>'
	                  +'<td>'+data.pageRow[i].cardTypeName+'</td>'
	                  +'<td>'+data.pageRow[i].cardNum+'</td>'
	                  +'<td>'+data.pageRow[i].password+'</td>'
	                  +'<td>'+data.pageRow[i].value+'</td>'
	                  +'<td>'+dateChange(data.pageRow[i].validStart)+'~'+dateChange(data.pageRow[i].validEnd)+'</td>'
	                  +'<td>'+dateChange(data.pageRow[i].applyDate)+'</td>'
	                  +'<td>'+invokeState+'</td>'
	                  +'<td>'+invokeDate+'</td>'
	                  +'<td>'+invokeUser+'</td>'
	                  +'<td>'+invokeIp+'</td>'
	                  +'<td><a href="../card/details.do?id='+data.pageRow[i].id+'">详情</a>&nbsp;&nbsp;&nbsp;'
	                  +'<a href="javascript:void(0);">冻结</a></td>'
	                  +'</tr>';
					value=value+"{data1:'"+data.pageRow[i].batchName+"',data2:'"+data.pageRow[i].cardTypeName+"',data3:'"+data.pageRow[i].cardNum+"',"
					+"data4:'"+data.pageRow[i].password+"',data5:'"+data.pageRow[i].value+"',data6:'"+dateChange(data.pageRow[i].validStart)+'~'+dateChange(data.pageRow[i].validEnd)+"',"
					+"data7:'"+dateChange(data.pageRow[i].applyDate)+"',";
					if(invokeState==''||invokeState==' '){
						value=value+"data8:'暂未激活',";
					}else{
						value=value+"data8:'"+invokeState+"',";
					}
					if(invokeDate==''||invokeDate==' '){
						value=value+"data9:'暂未激活',";
					}
					else{
						value=value+"data9:'"+invokeDate+"',";
					}
					if(invokeUser==''||invokeUser==' '){
						value=value+"data10:'暂未激活',";
					}
					else{
						value=value+"data10:'"+invokeUser+"',";
					}
					if(invokeIp==''||invokeIp==' '){
						value=value+"data11:'暂未激活'},";
					}
					else{
						value=value+"data11:'"+invokeIp+"'},";
					}
					$("#list").append(html);
				});
				value=value+"]";
				value=value.replace(",]","]");
				$("#data_source").val(value);
			}else{
				$("#list").append("暂无数据");
			}
			laypage(
		            {
		                cont: 'divPager',
		                pages: Math.ceil(data.totalRow / pageSize),	//总页数
		                curr: curr || 1,
		                skip: true,
		                skin: 'molv',
		                jump: function (obj, first) {
		                    if(!first){
		                        Page(obj.curr);
		                    }
		                }
		            });
		},	
	});
	
	}else{//卡号不为空，显示列表2  批次表
		$.ajax({
			type : "post",  
			url : "../card/queryCheck.do",
			data :{ 
				"batchName" : batchName, 
				"applyDepartment" : applyDepartment,
				"applyPerson" : applyPerson,
				"startTime" : startTime,
				"endTime" : endTime,
				"cardType" : cardType,
				"batchState" : batchState,
				"pageNum" : curr || 1,
				"pageSize" : pageSize,
			},
			dataType : "json",
			success : function(data){
				$("#list2").empty();//清空
				if(data.pageRow[0] != null){
					value="[";
					$.each(data.pageRow,function (i) {
						var valueNumber = data.pageRow[i].valueNumber;
						var batchState = "";
						var html1 = "";//审核通过、查看详情。如果批次状态是未审核，则操作是审核通过。
						if(data.pageRow[i].batchState == 1){
							batchState = "未审核";
							html1 = '<a href="javascript:void(0)" onclick="remind(\''
								+ data.pageRow[i].batchName+'\',\''+data.pageRow[i].cardTypeName+'\',\''
								+data.pageRow[i].applyDepartment+'\',\''+data.pageRow[i].applyPerson+'\',\''+data.pageRow[i].applyDate+'\');">提醒</a>';
						}
						if(data.pageRow[i].batchState == 2){
							batchState = "已审核未领取";
							html1 = '<a href="../card/exportCard.do?batchId='+data.pageRow[i].batchId+'&type=1">导出</a>&nbsp;&nbsp;&nbsp;'
								+'<a href="../card/batchDetailsUnGet.do?batchId='+data.pageRow[i].batchId+'&type=1">领取</a>';
						}
						if(data.pageRow[i].batchState == 3){
							batchState = "已领取";
							html1 = '<a href="../card/exportCard.do?batchId='+data.pageRow[i].batchId+'&type=2">导出</a>&nbsp;&nbsp;&nbsp;'
								+'<a href="../card/batchDetailsGet.do?batchId='+data.pageRow[i].batchId+'">详情</a>';
						}
						var valueNumber="";
						for(var j=0;j<eval(data.pageRow[i].valueNumber).length;j++){
							var numb=eval(data.pageRow[i].valueNumber);
							var param=numb[j];
							if(j==0){
								valueNumber=param.value+"/"+param.number;	
							}else{
								valueNumber=valueNumber+","+param.value+"/"+param.number;	
							}							
						}
						var html ='<tr>'
		                  +'<td>'+(serial+i)+'</td>'
		                  +'<td>'+data.pageRow[i].batchName+'</td>'
		                  +'<td>'+data.pageRow[i].cardTypeName+'</td>'
		                  +'<td>'+valueNumber+'</td>'
		                  +'<td>'+data.pageRow[i].amount+'</td>'
		                  +'<td>'+data.pageRow[i].validStart+'~'+data.pageRow[i].validEnd+'</td>'
		                  +'<td>'+data.pageRow[i].applyDepartment+'</td>'
		                  +'<td>'+data.pageRow[i].applyPerson+'</td>'
		                  +'<td>'+data.pageRow[i].applyDate+'</td>'
		                  +'<td>'+batchState+'</td>'
		                  +'<td>'+html1+'</td>'
		                  +'</tr>';
						$("#list2").append(html);
					});
				}else{
					$("#list2").append("暂无数据");
				}
				laypage(
			            {
			                cont: 'divPager2',
			                pages: Math.ceil(data.totalRow / pageSize),	//总页数
			                curr: curr || 1,
			                skip: true,
			                skin: 'molv',
			                jump: function (obj, first) {
			                    if(!first){
			                        Page(obj.curr);
			                    }
			                }
			            });
			},
			error : function(data){
				
			}
		})
	}
}
//-----------------------日期处理------------------------------
function dateChange(date){
	if(date != '' && date != null){
		var time = new Date(date);
		var year = time.getFullYear();
		var month = time.getMonth()+1 < 10 ? "0" + (time.getMonth() + 1) : time.getMonth() + 1;
		var currentDate = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
		time = year+"/"+month+"/"+currentDate;
		return time;
	}else{
		return "";
	}
}
//-----------------------------------充值卡类型------------------------------------------------
Code_list="";

$(function(){
	$.post("../card/codelist.do",function(data){
		if(data.length>0){
			for(var i=0;i<data.length;i++){
				
				var html="<input type=\"checkbox\" name=\"cardType\" value="+data[i].cardTypeCode+">"+data[i].cardTypeName;
				$("#type_list").append(html);
			}
			Code_list=data;	 
		}
	});
	$('#numStart').blur(function(){
		if($('#numStart').val().trim() != '' || $('#numEnd').val().trim() != ''){
			$('#activate').show();
		}else{
			$('#activate').hide();
		}
	});
	$('#numEnd').blur(function(){
		if($('#numStart').val().trim() != '' || $('#numEnd').val().trim() != ''){
			$('#activate').show();
		}else{
			$('#activate').hide();
		}
	});
});
//--------------------------------------提醒-----------------------------------------------------
function remind(batchName,type,applyDepartment,applyPerson,applyDate){
	$.ajax({
		type : 'post',
		url : '../card/remind.do',
		data : {
			'batchName':batchName,
			'type':type,
			'applyDepartment':applyDepartment,
			'applyPerson':applyPerson,
			'applyDate':applyDate,
			},
		dataType : 'json',
		success : function(data){
			if(data){
				layer.alert("提醒成功");
			}else{
				layer.alert("提醒失败");
			}
		}
		
	})
}
function exportAll(){
	window.location.href="../card/exportCard.do?batchId=&type=2";
}
////------------------------------------保存-----------------------------------------------------
//function exportcard(batchId){
//	$.ajax({
//		type : 'post',
//		url : '../card/exportCard.do',
//		data : {
//			'batchId':batchId
//			},
//		
//	})
//}