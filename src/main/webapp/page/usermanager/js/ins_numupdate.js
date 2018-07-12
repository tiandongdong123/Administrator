var already = true;
$(document).ready(function(){
	var all_index= $('.selFirst').length;
	var num= $('.selFirst:checked').length;
	var bindAuthority = new Array();
	if(all_index==num){
		$("#allInherited").prop("checked",true);
	}else {
		$("#allInherited").prop("checked",false);
	}
	$("input[class='selFirst']:checked").each(function () {
		bindAuthority.push($(this).val());
	});
	$("#bindAuthority").val(bindAuthority);
    if($("#user_dinding").is(':checked')){
        $("#dinding").show();
    }else {
        $("#dinding").hide();
    }
	//绑定个人上限的提示
	$("#bindLimit").keyup(function(){
		var userId = $("#userId").val();
		var bindLimit = $("#bindLimit").val();
		var reg = /^[1-9]\d*$/;
		if($("#bindLimit").val()==""){
			$(".mistaken").text("绑定个人上限不能为空，请填写正确的数字");
			$(".wrong").css("margin-left","5px");
			style();
			return;
		}
		if(!reg.test($("#bindLimit").val())){
			$(".mistaken").text("绑定个人上限是大于0的整数，请填写正确的数字");
			$(".wrong").css("margin-left","5px");
			style()
			return;
		}
		$.ajax({
			url: '../bindAuhtority/checkBindLimit.do',
			type: 'POST',
			dateType:"json",
            async:false,
			data:{
				userId: userId,
				bindLimit:bindLimit,
			},
			success: function(data){
                if(!data){
                    $(".mistaken").text("已绑定人数超过修改后的个人上限，请联系管理员解绑");
                    style()
					already = false;
                }else {
                    $(".bind_num").css("color","#00a65a");
                    $("#bindLimit").css("border-color","#00a65a");
                    $(".wrong").css("background","url(../img/t.png)");
					$(".wrong").css("margin-left","10px");
                    $(".wrong").css("display","inline");
                    $(".mistaken").css("display","none");
					already = true;
                }
			}
		});
	})

});
//提交事件
function submitForm(){

	var ip = $("#ipSegment").val();
	var adminIP = $("#adminIP").val();
	var userId = $("#userId").val();
	var adminname = $("#adminname").val();
	var userDinding = $("#user_dinding").prop('checked');
	$("#submit").attr({disabled: "disabled"});
	if(userDinding){
		var reg = /^[1-9]\d*$/;
		var bool = false;
		if($("#bindLimit").val()==""){
			$(".mistaken").text("绑定个人上限不能为空，请填写正确的数字");
			style();
			bool = true;
		}else if(!reg.test($("#bindLimit").val())){
			$(".mistaken").text("绑定个人上限是大于0的整数，请填写正确的数字");
			style();
			bool = true;
		}else if(!already){
			$(".mistaken").text("已绑定人数超过修改后的个人上限，请联系管理员解绑");
			style();
			bool = true;
		}else {
			$(".bind_num").css("color","#00a65a");
			$("#bindLimit").css("border-color","#00a65a");
			$(".wrong").css("background","url(../img/t.png)");
			$(".wrong").css("margin-left","10px");
			$(".wrong").css("display","inline");
			$(".mistaken").css("display","none");
		}
        if($('#bindType').find("option:selected").val() == '2'){
            $('#fromList').bootstrapValidator('addField','email',{
                validators : {
                    notEmpty : {
                        message : '请输入邮箱'
                    },
                    regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
                        message: '请输入正确的邮箱地址'
                    },
                }
            });
        }else{
            $('#fromList').bootstrapValidator('addField','email');
        }
		if(bool){
			return ;
		}
	}
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
        var openBindStart = data.get('openBindStart');
        var openBindEnd = data.get('openBindEnd');
        data.set('openBindStart',openBindStart+' 00:00:00');
        data.set('openBindEnd',openBindEnd+' 23:59:59');
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
	}else if(type.indexOf("perio")>-1){
		$("#perioMsg_"+count+"_"+i).html("");
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(checkPerio(count,i)&&validPerioYear(count,i)){
		    		layer.closeAll();
		    	}
		    },
		    cancel: function(){
		    	if(!checkPerio(count,i)||!validPerioYear(count,i)){
		    		return false;
		    	}
		    }
		});
	}else if(type.indexOf("conf")>-1){
		$("#confMsg_"+count+"_"+i).html("");
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(checkConf(count,i)){
		    		layer.closeAll();
		    	}
		    },
		    cancel: function(){
		    	if(!checkConf(count,i)){
		    		return false;
		    	}
		    }
		});
	}else if(type.indexOf("patent")>-1){
		$("#patentMsg_"+count+"_"+i).html("");
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(checkPatent(count,i)){
		    		layer.closeAll();
		    	}
		    },
		    cancel: function(){
		    	if(!checkPatent(count,i)){
		    		return false;
		    	}
		    }
		});
	}else if(type.indexOf("degree")>-1){
		$("#degreeMsg_"+count+"_"+i).html("");
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '详情',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(index, layero){
		    	if(validDegreeYear(count,i)){
		    		layer.closeAll();
		    	}
		    },
		    cancel: function(){
		    	if(!validDegreeYear(count,i)){
		    		return false;
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
								$("#perioMsg_"+data.number).html("");
								$("#perioInfoClc_"+data.number).val(getCheckNode(qk));
							}
						}
					}
				};
			//期刊分类
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
								$("#journalClc_"+data.number).val(getCheckNode(pz));
							}
							var dz = $.fn.zTree.getZTreeObj("degreeZtree_"+data.number);
							if(dz!=null){
								$("#degreeClc_"+data.number).val(getCheckNode(dz));
							}
							var cz = $.fn.zTree.getZTreeObj("confZtree_"+data.number);
							if(cz!=null){
								$("#conferenceClc_"+data.number).val(getCheckNode(cz));
							}
							var bz = $.fn.zTree.getZTreeObj("bookZtree_"+data.number);
							if(bz!=null){
								$("#booksClc_"+data.number).val(getCheckNode(bz));
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
		var arr = value.replace(/[ ]/g,"").split(",");
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

//IPC分类树
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
								$("#patentMsg_"+data.number).html("");
								$("#patentIpc_"+data.number).val(getCheckNode(pa));
							}
						}
					}
			};
			//IPC分类
			$.fn.zTree.init($("#patentZtree_"+data.number), setting, data.ztreeJson);
			if($.fn.zTree.getZTreeObj("patentZtree_"+data.number)!=null){
				checkedZtree($("#patentIpc_"+data.number).val(),$.fn.zTree.getZTreeObj("patentZtree_"+data.number));
			}
		}
	});
}