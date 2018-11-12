var volumeType = 1;
var startTime="";
var endTime="";
var keywords="";
var volumeType="";
var volumeState="";
var sortColumn="";//排序字段
var sortWay="";//排序方式（升序、降序）
var serial;
var pageSize = 10;
var subject_val="";//学科名称
var subject_value="";//学科值
$(function(){
	//打开文辑管理页面，默认显示优选文辑列表
	query();
	$("#price").val('');
	//---------------------文辑状态的显示与隐藏------------------
	$(":radio").click(function(){//触发时判断
		volumeType = $("input[name='volumeType']:checked").val();
		if(volumeType == 1){
			$("#state").show();
		}else{
			$("#state").hide();
		}
	});
	if($("#gradio").is(":checked")){//加在页面时判断
		$("#state").hide();
	}else{
		$("#state").show();
	}
	//获取学科信息
	 $.ajax({
			url:"../content/getsubject.do",
			dataType:"json",
			success:function(result){
				var subjects = eval(result);
				$(".choice_left").children("ul").html("");
				for(var i=0;i<subjects.length;i++){
					var subject = subjects[i];
					var id = subject.id;
					var name = subject.name;
					var data = subject.data;
					name = name.substring(0,12)+"...";
					$(".choice_left").children("ul").append("<li onclick=\"play_level(this,'"+i+"');\" style='overflow:hidden;'  ><a href='javascript:void(0);' style='border-right:none;text-decoration:none'  >"+name+"</a></li>");
						if(i==0){
							$(".choice_tab").append("<div class='choice_right' ></div>");
						}else{
							$(".choice_tab").append("<div class='choice_right' style='display:none;' ></div>");
						}
						var nextlevel = eval(data);
						for(var j=0;j<nextlevel.length;j++){
							var subject2 = nextlevel[j];
							var name2 = subject2.name;
							var data2 = eval(subject2.data);
							var li_ = "";
							for(var k=0;k<data2.length;k++){
								var subject3 = data2[k];
								var name3 = subject3.name;
								var value = subject3.value;
								li_ = li_ +"<li><input type='checkbox' id='"+value+"' onclick=\"on_choise(this,'"+name3+"');\" />"+name3+"</li>";
							}
							if(j==0){
								$(".choice_tab").children(".choice_right:last").append("<div><a onclick='getthreelevel(this);'  href='javascript:void(0);' ><img src='../images/icon_minus.gif' /></a>"+name3+"<div><input type='hidden' value='"+i+"' /><ul>"+li_+"</ul></div></div>");
							}else{
								$(".choice_tab").children(".choice_right:last").append("<div><a onclick='getthreelevel(this);' href='javascript:void(0);' ><img src='../images/icon_add.gif' /></a>"+name3+"<div style='display:none;' ><input type='hidden' value='"+i+"' /><ul>"+li_+"</ul></div></div>");
							}
							
						}
				}
				play_level($(".choice_left").find("li:first"),0);
			}
		});
		 $(".choice").children("input").click(function(){//选择学科
			 var choice_ = $(".choice_box");
			 if(choice_.css("display")=="none"){
				 choice_.css("display","block");
				 $(".write_box").css("height","860px;"); 
			 }else{
				 choice_.css("display","none");
				 $(".write_box").removeClass("style"); 
			 }
			
		 });
})
//-----------------------列表的显示与隐藏------------------------
function showTable(){
	if(volumeType == 1){
		$("#personResult").show();
		$("#goodResult").hide();
	}else{
		$("#goodResult").show();
		$("#personResult").hide();
	}
}
function query(){
	Page(1);
	showTable();
}
function Page(curr){
	//序号
	serial = ((curr||1) - 1) * pageSize + 1;
	startTime = $("#startTime").val();
	endTime = $("#endTime").val();
	keywords = $("#keywords").val();
	volumeType = $("input[name='volumeType']:checked").val();
	volumeState = $("#volumeState").val() == 0 ? "" : $("#volumeState").val();
	if(volumeType == 2){
		volumeState = 1;
	}
	$.ajax({
		type : "post",
		url : "../content/getVolume.do",
		data : {
			"startTime" : startTime,
			"endTime" : endTime,
			"searchWord" : keywords,
			"volumeType" : volumeType,
			"volumeState" : volumeState,
			"sortColumn" : sortColumn,
			"sortWay" : sortWay,
			"pageNum" : curr || 1,
			"pageSize" : pageSize,
		},
		dataType : "json",
		success : function(data){
			var divPager = "personDivPager";
			if(volumeType == 1){//用户文辑
				$("#plist").empty();//清空
				divPager = "personDivPager";
				sourceData="[";
				if(data.pageRow[0] != null){
					$.each(data.pageRow,function (i) {
						var state = "";
						var handle = "";
						if(data.pageRow[i].volumeState == 1){
							state = "公开";
							handle = '<a href="javascript:void(0)" onclick="showPush(this.id)" id="'+data.pageRow[i].id+'" target=_blank>推优</a>';
						}
						if(data.pageRow[i].volumeState == 2){
							state = "私有";
							handle = '<a href="../content/papercollectdetail.do?id='+data.pageRow[i].id+'&volumeChapter='+data.pageRow[i].volumeChapter+'" target=_blank >推优</a>';
						}
						var content = data.pageRow[i].volumeName;
						if(content.length > 10){
							content = content.substring(0,10) + "..."; 
						}
						var html ='<tr>'
		                  +'<td>'+(serial+i)+'</td>'
		                  +'<td>'+data.pageRow[i].volumeNo+'</td>'
		                  +'<td><a href="http://work.wanfangdata.com.cn/volume/detailsChapter.do?id='+data.pageRow[i].id+'" target=_blank>'+content+'</a></td>'
		                  +'<td>'+data.pageRow[i].keyword+'</td>'
		                  +'<td>'+state+'</td>'
		                  +'<td>'+data.pageRow[i].publishPerson+'</td>'
		                  +'<td>'+data.pageRow[i].publishDate+'</td>'
		                  +'<td>'+data.pageRow[i].docuNumber+'</td>'
		                  +'<td>'+handle+'</td>'
		                  +'</tr>';
						
						$("#plist").append(html);
						
						sourceData=sourceData+"{val1:'"+data.pageRow[i].volumeNo+"',val2:'"+data.pageRow[i].volumeName+"',val3:'" +data.pageRow[i].keyword
								   +"',val4:'"+state+"',val5:'"+data.pageRow[i].publishPerson+"',val6:'"+data.pageRow[i].publishDate+"',"
								   +"val7:'"+data.pageRow[i].docuNumber+"'},";
					});
					sourceData=sourceData+"]";
					sourceData=sourceData.replace(",]","]");
					$("#source_data").val(sourceData);
				}else{
					$("#plist").append("暂无数据");
				}
			}else{//优选文辑
				$("#glist").empty();//清空
				divPager = "goodDivPager";
				sourceData="[";
				if(data.pageRow != null){
					$.each(data.pageRow,function (i) {
						var content = data.pageRow[i].volumeName;
						if(content.length > 10){
							content = content.substring(0,10) + "..."; 
						}
						var issue = data.pageRow[i].issue;
						var issueHtml = "";
						if(issue == 1){//发布到solr
							issue = "发布";
							issueHtml='<a href="javascript:void(0)" onclick="issue(this.id,2)" id="'+data.pageRow[i].id+'" vc="'+data.pageRow[i].volumeChapter+'">'+issue+'</a>';//后台文辑详情页
						}else if(issue == 2){//从solr中删除
							issue = "下撤";
							issueHtml='<a href="javascript:void(0)" onclick="issue(this.id,3)" id="'+data.pageRow[i].id+'" vc="'+data.pageRow[i].volumeChapter+'">'+issue+'</a>';//后台文辑详情页
						}else{//再发布到solr
							issue = "再发布";
							issueHtml='<a href="javascript:void(0)" onclick="issue(this.id,2)" id="'+data.pageRow[i].id+'" vc="'+data.pageRow[i].volumeChapter+'">'+issue+'</a>';//后台文辑详情页
						}
						var html ='<tr>'
							  +'<td><input type="checkbox" name="goodVolume" value="'+data.pageRow[i].id+'"></td>'
			                  +'<td>'+(serial+i)+'</td>'
			                  +'<td>'+data.pageRow[i].volumeNo+'</td>'
			                  +'<td>'+(data.pageRow[i].subjectName == null?data.pageRow[i].subjectName:data.pageRow[i].subjectName.substring(0,10))+'...</td>'
			                  +'<td><a href="../content/papercollectdetail.do?id='+data.pageRow[i].id+'&volumeChapter='+data.pageRow[i].volumeChapter+'" target=_blank>'+content+'</a></td>'
			                  +'<td>'+data.pageRow[i].keyword+'</td>'
			                  +'<td>'+data.pageRow[i].userId+'</td>'
			                  +'<td>'+data.pageRow[i].publishDate+'</td>'
			                  +'<td>'+data.pageRow[i].docuNumber+'</td>'
			                  +'<td><div class="price_div">'+data.pageRow[i].volumePrice+'<i class="fa fa-fw fa-pencil" onclick="pencilHide(this,\''+data.pageRow[i].id+'\',event)" id="'+data.pageRow[i].volumePrice+'"></i></div></td>'
			                  +'<td>'+issueHtml+'<a href="../content/papercollectmodify.do?id='+data.pageRow[i].id+'" target=_blank>修改</a><a href="javascript:void(0)" onclick="deleteOne(\''+data.pageRow[i].id+'\')">删除</a></td>'
			                  +'</tr>';
						
						$("#glist").append(html);
						sourceData=sourceData+"{val1:'"+data.pageRow[i].volumeNo+"',val2:'"+content+"',val3:'" +data.pageRow[i].keyword
									+"',val4:'"+state+"',val5:'"+data.pageRow[i].publishPerson+"',val6:'"+data.pageRow[i].publishDate+"',val7:'"+data.pageRow[i].docuNumber+"'},";
						});
						sourceData=sourceData+"]";
						sourceData=sourceData.replace(",]","]");
						$("#source_data").val(sourceData);
					}else{
					$("#glist").append("暂无数据");
				}
			}
			
			laypage(
		            {
		                cont: divPager,
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
	});
}
//----------------------------排序---------------------------------
function sort(obj,column,way){
	sortColumn = column;
	sortWay = way;
	Page(1);
	showTable();
	$("a[name=sort]").removeClass("selected");
	$(obj).addClass("selected");
}
//----------------------------删除单个-------------------------------
function deleteOne(obj){
	layer.alert('确定删除该文辑？', {
		title: '文辑删除',
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定'], //按钮
	    yes: function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../content/delete.do",
	    		data :{ 
	    			"id" : obj,
	    		},
	    		dataType : "json",
	    		success : function(data){
	    			if(data){
	    				layer.msg("删除成功",{icon: 1});
	    				Page(1);
	    			}else{
	    				layer.msg("删除失败",{icon: 2});
	    			}
	    		},
	    	});
	        layer.closeAll();
	    }
	  });
}
//----------------------------批量删除-------------------------------
function deleteMore(){
	var id = "";
	var flag = false;
	$("input[name=goodVolume]").each(function(){
		   if($(this).is(":checked")){
			   id += $(this).val() + ",";
			   flag = true;
		   }
		});
	if(flag){
		layer.alert('确定删除文辑？', {
			title: '文辑删除',
			icon: 1,
		    skin: 'layui-layer-molv',
		    btn: ['确定'], //按钮
		    yes: function(){
		    	$.ajax({
		    		type : "post",  
		    		url : "../content/delete.do",
		    		data :{ 
		    			"id" : id, 
		    		},
		    		dataType : "json",
		    		success : function(data){
		    			if(data){
		    				layer.msg("删除成功",{icon: 1});
		    				query();
		    			}else{
		    				layer.msg("删除失败",{icon: 2});
		    			}
		    		},
		    	});
		        layer.closeAll();
		    }
		  });
	}else{
		layer.alert("请选择要删除的文辑！");
	}
	
}
//----------------------------全选-------------------------------
function selectAll(obj){
	if($(obj).is(":checked")){
		$("input[name=goodVolume]").each(function(){
			   $(this).prop("checked", "true");
			});
	}else{
		$("input[name=goodVolume]").each(function(){
			   $(this).removeAttr("checked");
			});
	}
}
////----------------------------推优-------------------------------
//function push(obj){
//	var content = '学科领域:<input type="text" id="subject"><br><br>'
//		+'定价:<input type="text" id="price">';
//	layer.alert(content,{
//		title: '用户文辑推优',
//	    skin: 'layui-layer-molv',
//	    btn: ['确定','取消'], //按钮
//	    yes:function(){
//	    	var subject = $("#subject").val();
//	    	var price = $("#price").val();
//	    	$.ajax({
//	    		type : "post",  
//	    		url : "../content/push.do",
//	    		data :{ 
//	    			"id" : obj,
//	    			"subject" : subject, 
//	    			"price" : price
//	    		},
//	    		dataType : "json",
//	    		success : function(data){
//	    			if(data){
//	    				layer.alert('推优成功 ! 此文集已经进入优选文集列表 ',{
//	    					title: '用户文辑推优',
//	    					icon: 1,
//	    				    skin: 'layui-layer-molv',
//	    				    btn: ['查看','取消'], //按钮
//	    				    yes:function(){
//	    				    	$("#gradio").prop("checked", "true");
//	    				    	$("#state").hide();
//	    				    	query();
//	    				    	layer.closeAll();
//	    				    },
//	    				    btn2:function(){ 
//	    				    	query();
//	    				    }
//	    				  });
//	    			}else{
//	    				layer.msg("操作失败",{icon: 2});
//	    			}
//	    		},
//	    	});
//	        layer.closeAll();
//	    }
//	  });
//}
//---------------------------显示推优弹框-------------------------
function showPush(obj){
	$('.subject').find('input[type=hidden]').val("");
	$('.subject_text').html("");
	$('#price').val("");
	$('#push').show();
	$('#push_id').val(obj);
}
//---------------------------显示推优弹框-------------------------
function closePush(){
	$('#push').hide();
}
//----------------------------推优-------------------------------
function push(){
	var mores = $(".subject_text").children("span");
	for(var j=0;j<mores.length;j++){
		var text = $(mores[j]).text();
		var value1 = $(mores[j]).find('input').val();
		if(value1.indexOf("[") >= 0){//去掉中括号
			value1 = value1.replace("[","");
			value1 = value1.replace("]","");
		}
		subject_val += "," + text;
		subject_value += "," + value1;
	}
	$('#subject').val(subject_value.substring(1));
	$('#subjectName').val(subject_val.substring(1));
	var subject = subject_value.substring(1);
	var subjectName = subject_val.substring(1);
    var push_id = $('#push_id').val();
	var price = $("#price").val();
	var flag = true;
	//学科为空js验证
	if(subject_value.substring(1) == ''){
		layer.alert('学科不能为空!');
		flag = false;
	}else if(price == ''){
		layer.alert('价格不能为空!');
		flag = false;
	}
	if(flag){
		$.ajax({
			type : "post",  
			url : "../content/push.do",
			data :{ 
				"id" : push_id,
				"subject" : subject, 
				"subjectName" : subjectName, 
				"price" : price
			},
			dataType : "json",
			success : function(data){
				closePush();
				if(data){
					layer.alert('推优成功 ! 此文集已经进入优选文集列表 ',{
						title: '用户文辑推优',
						icon: 1,
					    skin: 'layui-layer-molv',
					    btn: ['查看','取消'], //按钮
					    yes:function(){
					    	$("#gradio").prop("checked", "true");
					    	$("#state").hide();
					    	query();
					    	layer.closeAll();
					    },
					    btn2:function(){ 
					    	query();
					    }
					  });
				}else{
					layer.msg("操作失败",{icon: 2});
				}
			},
		});
	    layer.closeAll();
	}
	
}
//------------------------------发布----------------------------------
function issue(obj,issueNum){
	var element = document.getElementById(obj);
	var volumeChapter = element.getAttribute("vc");
	var hint = "";
	if(issueNum == 2){//发布和再发布
		hint = "发布";
	}else{//下撤
		hint = "下撤";
	}
	layer.alert('确定'+hint+'文辑',{
		title: hint+'文辑',
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'], //按钮
	    yes:function(){
	    	$.ajax({
	    		type : "post",  
	    		url : "../content/issue.do",
	    		data :{ 
	    			"id" : obj,
	    			"volumeChapter" : volumeChapter,
	    			"issueNum" : issueNum
	    		},
	    		dataType : "json",
	    		success : function(data){
	    			if(data){
	    				layer.alert('文辑已成功'+hint,{
	    					title: hint+'文辑',
	    					icon: 1,
	    				    skin: 'layui-layer-molv',
	    				    btn: ['确定'], //按钮
	    				    yes:function(){
	    				    	layer.msg("操作成功",{icon: 1});
	    				    	query();
	    				    	layer.closeAll();
	    				    },
	    				  });
	    			}else{
	    				layer.msg("操作失败",{icon: 2});
	    			}
	    		},
	    	});
	        layer.closeAll();
	    }
	  });
}
//----------------------------修改价格----------------------
function pencilHide(obj,volumeId,event){
	event.stopPropagation();
	$(obj).parent().hide();
	var id = obj.id;
	var html = '<div class="update_div"><input type="text" size="4" value="'+id+'"><button type="button" height="20px" onclick="updatePrice(this,\''+volumeId+'\')">保存</button></div>';
	$(obj).parent().parent().append(html);
}
function updatePrice(obj,volumeId){
	var price = $(obj).siblings().val();
	$.ajax({
		type : "post",
		url : "../content/updatePrice.do",
		data : {
			"price" : price,
			"volumeId" : volumeId,
		},
		dataType : "json",
		success : function(data){
			if(data){
				query();
			}else{
				layer.msg("操作失败",{icon: 2});
			}
		}
	})
}

//---------------------------点击空白消失------------------------------------------------
$(document).click(function(e){
	var target  = $(e.target);
	if(target.closest(".update_div").length == 0){
	       $(".update_div").hide();
	       $(".update_div").show();
	}
});

function play_level(txt,i_num){
	$(txt).find("a").css({"background":"#F5F5F5"});
	$(txt).siblings("li").find("a").css("background","#FFFFFF");
	$(".choice_right:eq("+i_num+")").siblings(".choice_right").hide();
	$(".choice_right:eq("+i_num+")").show();
}
function getthreelevel(text){
	$(text).next("div").show();
	$(text).children("img").attr("src","../images/icon_minus.gif");
	$(text).parent("div").siblings("div").find("img").attr("src","../images/icon_add.gif");
	$(text).parent("div").siblings("div").find("div").hide();
}
function on_choise(checkon,text){
	var checks = $("input[type='checkbox']:checked");
	var count = 0;
	for(var i=0;i<checks.length;i++){
		var checked = checks[i];
			count++;
	}
	if(count>3){
		alert("最多选择三个");
		$(checkon).removeAttr("checked");
	}else{
		if($(checkon).is(':checked')){
			var svalue = $(checkon).attr("id");//学科value
			var html = "<input type='text' hidden='true'  name='subject_value' value='"+svalue+"'>";
			$(".more").append("<span style='color: #484848;'>"+text+"<img onclick=\"del_subject(this,'"+text+"');\" src='../images/theme_close.png' />"+html+"</span>");
		}else{
			var checkval = $(checkon).parent("li").text();
			var mores = $(".more").children("span");
			for(var j=0;j<mores.length;j++){
				var more = mores[j];
				if($(more).text()==checkval){
					$(more).remove();
				}
			}
		}
	}
}
function del_subject(del,text){
	var checks = $("input[type='checkbox']");
	for(var i=0;i<checks.length;i++){
		var checked = checks[i];
		var checkval = $(checked).parent("li").text();
		if($(checked).attr("checked") && checkval==text){
			 $(checked).removeAttr("checked");
		}
	}
	$(del).parent("span").remove();
}
function sub_del(txt){
	$(".choice_box").hide();
}
function sub_add(){
	var mores = $(".more").children("span:gt(0)");
	$(".choice_box").hide();
	$(".subject_text").html("");
	for(var j=0;j<mores.length;j++){
		var text = $(mores[j]).text();
		var value1 = $(mores[j]).find('input').val();
		if(value1.indexOf("[") >= 0){//去掉中括号
			value1 = value1.replace("[","");
			value1 = value1.replace("]","");
		}
		var subject_html = "<span style='color: #484848;'>"
			+text+"<img onclick=\"del_subject(this,'"+text+"');\" src='../images/theme_close.png' />"
			+"<input hidden='true' name='subject_value' value='"+value1+"'></span>";
		$(".subject_text").append(subject_html);
//		subject_val += "," + text;
//		subject_value += "," + value1;
	}
	$(".more").children("span:gt(0)").remove();
	$("input[type='checkbox']").attr("checked",false);
	$('#checkSubject').next().remove();
}
function search_subject(txt){
	var search_val = $(txt).parent("a").prev("input").val();
	var checks = $("input[type='checkbox']");
	for(var i=0;i<checks.length;i++){
		var checked = checks[i];
		var checkval = $(checked).parent("li").text();
		if(checkval==search_val){
			var i_num = $(checked).parent("li").parent("ul").prev("input").val();
			play_level($(".choice_left").find("li:eq("+i_num+")"),i_num);
			$(checked).parent("li").parent("ul").parent("div").prev("a").click();
			 $(checked).parent("li").parent("ul").parent("div").show();
		}
	}
}

function on_submit(){
	$("#subject_").val(subject_val);
	$("#authenticationForm").submit();
}


//文辑导出
function exportVolumeDocu(){
	window.location.href="../content/exportVolumeDocu.do?"
			+"startTime="+startTime
			+"&endTime="+endTime
			+"&searchWord="+keywords
			+"&volumeType="+volumeType
			+"&volumeState="+volumeState
			+"&sortColumn="+sortColumn
			+"&sortWay="+sortWay;
}

