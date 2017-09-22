function query(){
	$("#result").show();
	Page(1);
}
//------------------------------------分页--------------
var batchName = "";
var applyDepartment = "";
var applyPerson = "";
var startTime = "";
var endTime = "";
var cardType = "";
var pageNum;
var pageSize = 8;
function Page(curr){
	//序号
	var serial = ((curr||1) - 1) * pageSize + 1;
	batchName = $("#batchName").val().trim();//批次
	applyDepartment = $("#department").val().trim();//申请部门
	applyPerson = $("#person").val().trim();//申请人
	startTime = $("#startTime").val();//开始时间
	endTime = $("#endTime").val();//结束时间
	cardType = $("input[name='cardType']:checked").val();
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
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
		},
		dataType : "json",
		success : function(data){
			$("#list").empty();//清空
			var x=9+8*(curr-2);
			var sum=1;
			var  maxsum=0;
			var  sumnum=0;
			if(data.pageRow[0] != null){
				//先显示未审核的
				$.each(data.pageRow,function (i) {
					if(data.pageRow[i].checkState==1){
						var checkState = "";
						if(data.pageRow[i].checkState == 1){
							checkState = "未审核";
						}
						if(data.pageRow[i].checkState == 2){
							checkState = "已审核";
						}
						var batchState = "";
						var html1 = "";//审核通过、查看详情。如果批次状态是未审核，则操作是审核通过。
						if(data.pageRow[i].batchState == 1){
							batchState = "未审核";
							html1 = '<a href="javascript:void(0)" onclick="check(\''+data.pageRow[i].batchId+'\')">审核通过</a>';
						}
						if(data.pageRow[i].batchState == 2){
							batchState = "已审核未领取";
							html1 = '<a href="../card/batchDetailsUnGet.do?batchId='+data.pageRow[i].batchId+'&type=0">查看详情</a>';
						}
						if(data.pageRow[i].batchState == 3){
							batchState = "已领取";
							html1 = '<a href="../card/batchDetailsGet.do?batchId='+data.pageRow[i].batchId+'">查看详情</a>';
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
						sumnum++;
						maxsum=maxsum*1+data.pageRow[i].amount*1;
						var html ='<tr>'
							+'<td>'+x+'</td>'
		                  +'<td>'+data.pageRow[i].batchName+'</td>'
		                  +'<td>'+data.pageRow[i].cardTypeName+'</td>'
		                  +'<td>'+valueNumber+'</td>'
		                  +'<td>'+data.pageRow[i].amount+'</td>'
		                  +'<td>'+data.pageRow[i].validStart+'~'+data.pageRow[i].validEnd+'</td>'
		                  +'<td>'+data.pageRow[i].applyDepartment+'</td>'
		                  +'<td>'+data.pageRow[i].applyPerson+'</td>'
		                  +'<td>'+data.pageRow[i].applyDate+'</td>'
		                  +'<td><a href="../card/download1.do?url='+data.pageRow[i].adjunct+'">点击下载</a></td>'
		                  +'<td>'+checkState+'</td>'
		                  +'<td>'+html1+'</td>'
		                  +'</tr>';
						$("#list").append(html);
						x++;
					}
					sum=x;
				});
				//后显示已审核的
				$.each(data.pageRow,function (i) {
					if(data.pageRow[i].checkState==2)
						{
						var valueNumber = data.pageRow[i].valueNumber;
						valueNumber = valueNumber.replace("[", "").replace("]", "").replace(/\"/g, "");
						var checkState = "";
						if(data.pageRow[i].checkState == 1){
							checkState = "未审核";
						}
						if(data.pageRow[i].checkState == 2){
							checkState = "已审核";
						}
						var batchState = "";
						var html1 = "";//审核通过、查看详情。如果批次状态是未审核，则操作是审核通过。
						if(data.pageRow[i].batchState == 1){
							batchState = "未审核";
							html1 = '<a href="javascript:void(0)" onclick="check(\''+data.pageRow[i].batchId+'\')">审核通过</a>';
						}
						if(data.pageRow[i].batchState == 2){
							batchState = "已审核未领取";
							html1 = '<a href="../card/batchDetailsUnGet.do?batchId='+data.pageRow[i].batchId+'&type=0">查看详情</a>';
						}
						if(data.pageRow[i].batchState == 3){
							batchState = "已领取";
							html1 = '<a href="../card/batchDetailsGet.do?batchId='+data.pageRow[i].batchId+'">查看详情</a>';
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
						sumnum++;
						maxsum=maxsum*1+data.pageRow[i].amount*1;
						var html ='<tr>'
							 +'<td>'+sum+'</td>'
		                  /*+'<td>'+(serial+i)+'</td>'*/
		                  +'<td>'+data.pageRow[i].batchName+'</td>'
		                  +'<td>'+data.pageRow[i].cardTypeName+'</td>'
		                  +'<td>'+valueNumber+'</td>'
		                  +'<td>'+data.pageRow[i].amount+'</td>'
		                  +'<td>'+data.pageRow[i].validStart+'~'+data.pageRow[i].validEnd+'</td>'
		                  +'<td>'+data.pageRow[i].applyDepartment+'</td>'
		                  +'<td>'+data.pageRow[i].applyPerson+'</td>'
		                  +'<td>'+data.pageRow[i].applyDate+'</td>'
		                  +'<td><a href="../card/download.do?url=${request.contextPath}'+data.pageRow[i].adjunct+'">点击下载</a></td>'
		                  +'<td>'+checkState+'</td>'
		                  +'<td>'+html1+'</td>'
		                  +'</tr>';
						
						$("#list").append(html);
						sum++;
						}					
				});
			}else{
				$("#list").append("暂无数据");
			}
			laypage({
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
		error : function(data){}
	})
}

//----------------------------审核-------------------------------
function check(obj){
	layer.alert('确定审核通过？', {
		title: '万方卡审核',
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../card/updateCheckState.do",
	    		data :{ 
	    			"batchId" : obj, 
	    		},
	    		dataType : "json",
	    		success : function(data){
	    			if(data){
	    				Page(1);
	    			}
	    		},
	    		error : function(data){
	    		}
	    	});
	        layer.closeAll();
	    }
	  });
}


