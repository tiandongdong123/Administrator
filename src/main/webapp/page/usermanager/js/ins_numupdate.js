//提交事件
function submitForm(){
	var ip = $("#ipSegment").val();
	var adminIP = $("#adminIP").val();
	var userId = $("#userId").val();
	var adminname = $("#adminname").val();
	$("#submit").attr({disabled: "disabled"});
	if(!validateFrom()){
		$("#submit").removeAttr("disabled");
		return false;
	}else if(ip!="" && !IpFormat(ip)){
		layer.msg("机构账号IP段格式有误",{icon: 2});
		$("#submit").removeAttr("disabled");
		return false;
	}else if(adminIP!="" && !IpFormat(adminIP)){
		layer.msg("管理员IP段格式有误",{icon: 2});
		$("#submit").removeAttr("disabled");
		return false;
	}else if(ip!="" && validateIp(ip,userId,'#ipSegment')){
		$("#submit").removeAttr("disabled");
		return false;
	}else{
		var data = new FormData($('#fromList')[0]);
		$.ajax({  
			url: '../auser/updateinfo.do',
			type: 'POST',
			data: data,
			cache: false,  
			processData: false,
			contentType: false  
		}).done(function(data){
			if(data.flag=="success"){
	    		layer.alert("更新成功", {
	    			icon: 1,
	    		    skin: 'layui-layer-molv',
	    		    btn: ['确定'], //按钮
	    		    yes: function(){
	    		    	window.location.href="../auser/information.do?userId="+parent.$("#userId").val();
	    		    }
	    		});
			}else{
				if(data.fail!=null){
					layer.msg(data.fail, {icon: 2});
				}else{
					layer.msg("更新失败，请联系管理员", {icon: 2});
				}
			}
			$("#submit").removeAttr("disabled");
		});
	}
}


function openItems(count,i,type){
	$("#tabs_custom_"+count+"_"+i).find("li").removeClass("active");
	if(type.indexOf("perio")>-1){
		$("a[href='#perio_"+count+"_"+i+"']").parent().addClass("active");
		$("#perio_"+count+"_"+i).addClass("active").siblings().removeClass("active");
		perioSubject(count+"_"+i);
	}else if(type.indexOf("degree")>-1){
		$("a[href='#degree_"+count+"_"+i+"']").parent().addClass("active");
		$("#degree_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("conf")>-1){
		$("a[href='#conf_"+count+"_"+i+"']").parent().addClass("active");
		$("#conf_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("patent")>-1){
		$("a[href='#patent_"+count+"_"+i+"']").parent().addClass("active");
		$("#patent_"+count+"_"+i).addClass("active").siblings().removeClass("active");
		findPatentEcho(count+"_"+i);
	}else if(type.indexOf("books")>-1){
		$("a[href='#book_"+count+"_"+i+"']").parent().addClass("active");
		$("#book_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("standard")>-1){
		$("a[href='#standard_"+count+"_"+i+"']").parent().addClass("active");
		$("#standard_"+count+"_"+i).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("local chronicles")>-1){
		$("a[href='#localchronicles_"+count+"_"+i+"']").parent().addClass("active");
		$("#localchronicles_"+count+"_"+i).addClass("active").siblings().removeClass("active");
		initGazetteer(count,i);
	}
	if(type.indexOf("local chronicles")==-1){
		//Ztree 返显
		findSubjectEcho(count+"_"+i);
	}
	
	if(type.indexOf("standard")>-1){
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(validStandard(count,i)){
		    		layer.closeAll();
		    	}
		    }
		});
	}else{
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(){
		        layer.closeAll();
		    },
		});
	}
}
//期刊学科分类号
function perioSubject(num){
	$.ajax({
		type : "post",
		data : {num:num},
		async:false,
		url : "../auser/findPerioSubject.do",
		beforeSend : function(XMLHttpRequest) {},
		success:function(data){
			var setting = {
					view: {
						dblClickExpand: false,
					},
					data: {
						simpleData: { 
				            enable:true,
				            idKey:"id",
				            pIdKey:"pid"
				    	},
				        key:{name:"name"}
					},
					check: {
						enable: true,
						chkStyle: "checkbox"
					},
					callback: {
						onCheck: function(){
							var qk = $.fn.zTree.getZTreeObj("perioInfoZtree_"+data.number);
							if(qk!=null){
								var text = new Array();
								text=getCheckNode(qk);
								$("#perioInfoClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
						}
					}
				};
			//期刊中途分类
			$.fn.zTree.init($("#perioInfoZtree_"+data.number), setting, data.ztreeJson);
			if($.fn.zTree.getZTreeObj("perioInfoZtree_"+data.number)!=null){				
				checkedZtree($("#perioInfoClc_"+data.number).text(),$.fn.zTree.getZTreeObj("perioInfoZtree_"+data.number));
			}
		}
	});
}

//学科中图分类树
function findSubjectEcho(num){
	$.ajax({
		type : "post",
		data : {num:num},
		async:false,
		url : "../auser/findsubject.do",
		//dataType : "json",
		beforeSend : function(XMLHttpRequest) {},
		success:function(data){
			var setting = {
					view: {
						dblClickExpand: false,
					},
					data: {
						simpleData: { 
				            enable:true,
				            idKey:"id",
				            pIdKey:"pid"
				    	},
				        key:{name:"name"}
					},
					check: {
						enable: true,
						chkStyle: "checkbox"
					},
					callback: {
						onCheck: function(){
							var pz = $.fn.zTree.getZTreeObj("perioZtree_"+data.number);
							if(pz!=null){
								var text = new Array();
								text=getCheckNode(pz);
								$("#journalClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
							var dz = $.fn.zTree.getZTreeObj("degreeZtree_"+data.number);
							if(dz!=null){
								var text = new Array();
								text=getCheckNode(dz);
								$("#degreeClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
							var cz = $.fn.zTree.getZTreeObj("confZtree_"+data.number);
							if(cz!=null){
								var text = new Array();
								text=getCheckNode(cz);
								$("#conferenceClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
							var bz = $.fn.zTree.getZTreeObj("bookZtree_"+data.number);
							if(bz!=null){
								var text = new Array();
								text=getCheckNode(bz);
								$("#booksClc_"+data.number).val(text.length>0?"["+text+"]":"");
							}
						}
					}
				};
			//期刊中途分类
			$.fn.zTree.init($("#perioZtree_"+data.number), setting, data.ztreeJson);
			//学位中途分类
			$.fn.zTree.init($("#degreeZtree_"+data.number), setting, data.ztreeJson);
			//会议中途分类
			$.fn.zTree.init($("#confZtree_"+data.number), setting, data.ztreeJson);
			//图书中途分类
			$.fn.zTree.init($("#bookZtree_"+data.number), setting, data.ztreeJson);
			
			if($.fn.zTree.getZTreeObj("perioZtree_"+data.number)!=null){				
				checkedZtree($("#journalClc_"+data.number).text(),$.fn.zTree.getZTreeObj("perioZtree_"+data.number));
			}
			if($.fn.zTree.getZTreeObj("degreeZtree_"+data.number)!=null){
				checkedZtree($("#degreeClc_"+data.number).text(),$.fn.zTree.getZTreeObj("degreeZtree_"+data.number));
			}
			if($.fn.zTree.getZTreeObj("confZtree_"+data.number)!=null){				
				checkedZtree($("#conferenceClc_"+data.number).text(),$.fn.zTree.getZTreeObj("confZtree_"+data.number));
			}
			if($.fn.zTree.getZTreeObj("bookZtree_"+data.number)!=null){				
				checkedZtree($("#booksClc_"+data.number).text(),$.fn.zTree.getZTreeObj("bookZtree_"+data.number));
			}
		}
	});
}

function checkedZtree(value,obj){
	if(value!=null && value!=""){
		var arr = value.substring(1).substring(0,value.length-2).replace(/[ ]/g,"").split(",");
		for(var i=0;i<arr.length;i++){
			treenodeClick(obj.getNodeByParam("value",arr[i].replace(/["“”]/g,"")), obj);
		}
	}
}

//点击树节点，选中节点的所有叶子节点
function treenodeClick(treeNode,obj){ 
    //此处treeNode为当前节点 
     if(treeNode.isParent){
         str = getAllChildrenNodes(treeNode, obj);
     }
     obj.checkNode(treeNode, true);
}

//递归，得到叶子节点的数据
function getAllChildrenNodes(treeNode,obj){
	if(treeNode.isParent){ 
	var childrenNodes = treeNode.children; 
		if(childrenNodes){ 
			for(var i = 0; i < childrenNodes.length; i++){ 
				if(childrenNodes[i].isParent){
					getAllChildrenNodes(childrenNodes[i],obj); 
				}
				obj.checkNode(childrenNodes[i], true);
			} 
		} 
	} 
}

//学科中图分类树
function findPatentEcho(num){
	$.ajax({
		type : "post",
		data : {num:num},
		async:false,
		url : "../auser/findpatent.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {},
		success:function(data){
			var setting = {
					view: {
						dblClickExpand: false,
					},
					data: {
						simpleData: { 
				            enable:true,
				            idKey:"id",
				            pIdKey:"pid"
				    	},
				        key:{name:"name"}
					},
					check: {
						enable: true,
						chkStyle: "checkbox",
						//chkboxType:{"Y":"s","N":"s"}
					},
					callback: {
						onCheck: function(){
							var pa=$.fn.zTree.getZTreeObj("patentZtree_"+data.number);
							if(pa!=null){
								var text = new Array();
								text=getCheckNode(pa);
								$("#patentIpc_"+data.number).val(text.length>0?"["+text+"]":"");
							}else{
								$("#patentIpc_"+data.number).val("");
							}
						}
					}
			};
			//期刊中途分类
			$.fn.zTree.init($("#patentZtree_"+data.number), setting, data.ztreeJson);
			checkedZtree($("#patentIpc_"+data.number).val(),$.fn.zTree.getZTreeObj("patentZtree_"+data.number));
		}
	});
}