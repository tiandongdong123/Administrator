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
	
	var data = new FormData($('#fromList')[0]);
    var openBindStart = data.get('openBindStart');
    var openBindEnd = data.get('openBindEnd');
    if(openBindStart){
        data.set('openBindStart',openBindStart+' 00:00:00');
    }
    if(openBindEnd){
        data.set('openBindEnd',openBindEnd+' 23:59:59');
    }
    var isCheckedMe = $('#isPublishEmail').is(':checked');
    data.append('send',isCheckedMe);
    
	var ip = $("#ipSegment").val();
	var adminIP = $("#adminIP").val();
	var userId = $("#userId").val();
	var adminOldName = $("#adminOldName option:selected").val();
	var userDinding = $("#user_dinding").prop('checked');
	addAtrr();
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
                    emailAddress: {/* 只需加此键值对，包含正则表达式，和提示 */
                        message: '请输入正确的邮箱地址'
                    }
                }
            });
        }else{
            $('#fromList').bootstrapValidator('removeField','email');
        }
        $("#fromList").data('bootstrapValidator').resetForm();
		if(bool){
			removeAtrr();
			return ;
		}
	}
	if(!validateFrom()){
		removeAtrr();
		return false;
	}else if(ip!="" && !IpFormat(ip)){
		layer.msg("机构账号IP段不合法，请填写规范的IP段",{icon: 2});
		removeAtrr();
		return false;
	}else if(adminIP!="" && !IpFormat(adminIP)){
		layer.msg("管理员账号IP段不合法，请填写规范的IP段",{icon: 2});
		removeAtrr();
		return false;
	}else if(ip!="" && validateIpChange(data,'#ipSegment')){
		removeAtrr();
		return false;
	}else{
		var olds=$("#oldInstitution").val();
		var institution=$("#institution").val();
		/*if(olds!=institution){
			 validateUserInstitution(olds,institution);
		}else{
			updateUser();
		}*/
		
		updateUser();
	}
}


function validateUserInstitution(olds,institution){
	$.ajax({
		type : "post",
		url : "../auser/findInstitutionAllUser.do",
		data:{"institution":olds},
		success: function(data){
			var html="";
			var admin=data.admin;
			var user=data.user;
			var num=0;
			if(user!=null){
				html+="该机构下的机构账号有：</br>";
				var array=user.split(",");
				for(ar in array){
					html+=(parseInt(ar)+1)+"、"+array[ar]+"</br>";
				}
				num+=array.length;
			}
			if(admin!=null){
				html+="该机构下的机构管理员有：</br>";
				var array=admin.split(",");
				for(ar in array){
					html+=(parseInt(ar)+1)+"、"+array[ar]+"</br>";
				}
				num+=array.length;
			}
			if(num>1){
				if(user!=null&&admin!=null){
					html+="确定要同时修改所有机构账号及机构管理员的机构名称吗？";
				}else if(admin==null){
					html+="确定要同时修改所有机构账号的机构名称吗？";
				}
				layer.alert(html,{
					icon: 1,
					title : ["确认", true],
				    skin: 'layui-layer-molv',
				    btn: ['确定','取消'],
				    yes: function(){
				    	updateUser();
				    }
				});
				removeAtrr();
			}else{
		    	updateUser();
			}
		}
	});
}

function updateUser(){
	var data = new FormData($('#fromList')[0]);
    var openBindStart = data.get('openBindStart');
    var openBindEnd = data.get('openBindEnd');
    if(openBindStart){
        data.set('openBindStart',openBindStart+' 00:00:00');
    }
    if(openBindEnd){
        data.set('openBindEnd',openBindEnd+' 23:59:59');
    }
    var isCheckedMe = $('#isPublishEmail').is(':checked');
    var userId = $("#userId").val();
    data.append('send',isCheckedMe);
	$.ajax({
		url: '../auser/updateinfo.do',
		type: 'POST',
		data: data,
		cache: false,
		processData: false,
		contentType: false
	}).done(function(data){
		if(data.flag=="success"){
			$('#IpErrorInfo').html('')
    		layer.alert("更新成功", {
    			icon: 1,
    		    skin: 'layui-layer-molv',
    		    btn: ['确定'], //按钮
    		    yes: function(){
    		    	window.location.href="../group/list.do?userId="+parent.$("#userId").val();
    		    }
    		});
		}else{
			if(data.flag=='fail'){
				layer.msg(data.fail, {icon: 2});
			}else if(data.flag=='error'){
	    		layer.alert(data.fail, {
	    			icon: 2,
	    			title: '提示',
	    		    skin: 'layui-layer-molv',
	    		    btn: ['继续修改'], //按钮
	    		    yes: function(){
	    		    	window.location.href='../group/modify.do?userId='+userId;
	    		    }
	    		});
			}else{
				layer.msg("更新失败，请联系管理员", {icon: 2});
			}
		}
		removeAtrr();
	});

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
		$('.checkType').prop('checked',false)
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
	}else if(type.indexOf("local")>-1){
		layer.open({
		    type: 1, //page层 1div，2页面
		    area: ['40%', '90%'],
		    title: '地方志限定',
		    moveType: 2, //拖拽风格，0是默认，1是传统拖动
		    content: $("#tabs_custom_"+count+"_"+i),
		    btn: ['确认'],
			yes: function(){
				layer.closeAll();
		    },
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

//余额转化为限时，限时转化为余额
function changeLimit(obj,i){
	var channelid=$("#pro_GBalanceLimit").val();
	if(channelid==undefined || channelid==null){
		channelid=$("#pro_GTimeLimit").val();
	}
	var changeFront=$("#changeFront").val();
	if(changeFront==null||changeFront==''){
		$("#changeFront").val(channelid);
	}
	var msg="";
	if(channelid=='GBalanceLimit'){
		msg="确定要将余额账号转换为限时账号吗？";
	}else if(channelid=='GTimeLimit'){
		msg="确定要将限时账号转换为余额账号吗？";
	}
	layer.alert(msg,{
		icon: 1,
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'],
	    yes: function(){
			if(channelid=='GTimeLimit'){
				$("#pro_"+channelid).attr("id","pro_GBalanceLimit");
				$("#buttonspan_"+i).html("余额转限时");
				$("#proname"+i).html('资源余额');
				$("input[name='rdlist["+i+"].projectname']").val('资源余额');
				$("#pro_GBalanceLimit").val('GBalanceLimit');
				$("input[name='rdlist["+i+"].projectType']").val('balance');
				$("#time_money_"+i).html('<span><b>*</b>金额</span><input name="rdlist['+i+'].totalMoney" type="text" value="0" onkeyup="checkMoney(this);" onafterpaste="checkMoney(this);" maxlength="9"><span style="margin-left:15px;color:#00B2FF;">项目余额：0元</span>');
			}else if(channelid=='GBalanceLimit'){
				$("#pro_"+channelid).attr("id","pro_GTimeLimit");
				$("#buttonspan_"+i).html("限时转余额");
				$("#proname"+i).html('资源限时');
				$("input[name='rdlist["+i+"].projectname']").val('资源限时');
				$("#pro_GTimeLimit").val('GTimeLimit');
				$("input[name='rdlist["+i+"].projectType']").val('time');
				$("#time_money_"+i).html('');
				$("input[name='rdlist["+i+"].validityEndtime2']").val('');
				$("input[name='rdlist["+i+"].validityStarttime2']").val('');
			}
	    	layer.closeAll();
	    }
	});
}

function removeAtrr(){
	$("#submit").removeAttr("disabled");
	$("#submit1").removeAttr("disabled");
}

function addAtrr(){
	$("#submit").attr({disabled: "disabled"});
	$("#submit1").attr({disabled: "disabled"});
}
