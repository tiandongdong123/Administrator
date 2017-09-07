$(function(){
	goPage();
	
    $('.add_admin').click(function (e) {
        preventBubble(e);
    });
    
});

//全局点击事件(隐藏服务权限div)
$(document).click(function(){
	$(".btn-group").removeClass("open");
});

//组织事件冒泡
function preventBubble(event){
    event=event?event:window.event;
    if(event.cancelBubble){
        event.cancelBubble = true;
    }else if(event.stopPropagation){
        event.stopPropagation();
    }
    event.cancelBubble=true;
}

//查询机构下管理员、并修改
function showAdm(id,pid,institution,e){
	preventBubble(e);
	$.ajax({
		type : "post",
		url : "../auser/findins.do",
		data:{institution:institution,userId:id},
		success: function(data){
			var text = '<tr><th>管理员ID</th><th>密码</th><th>管理员IP段</th><th>管理员邮箱</th><th>管理机构账号数</th></tr>';
			for(var i = 0;i < data.length; i++){
				text += '<tr><td><input type="hidden" id="adminId_'+id+'_'+i+'" value="'+data[i].userId+'">'+data[i].userId+'</td>';
				text += '<td><input type="text" style="width:200px;" id="adminpassword_'+id+'_'+i+'" value="'+data[i].password+'"></td>';
				if(data[i].adminIP!=null && data[i].adminIP.length>0){					
					text += '<td><textarea style="margin: 0px; width: 300px; height: 44px;" id="adminIP_'+id+'_'+i+'">';
					for(var j = 0;j < data[i].adminIP.length; j++){						
						text += data[i].adminIP[j].beginIpAddressNumber+'-'+data[i].adminIP[j].endIpAddressNumber
					}
					text += '</textarea></td>';
				}else{
					text += '<td><textarea style="margin: 0px; width: 300px; height: 44px;" id="adminIP_'+id+'_'+i+'"></textarea></td>';
				}
				text += '<td><input type="text" style="width:200px;" id="adminEmail_'+id+'_'+i+'" value="'+data[i].adminEmail+'"></td>';
				text += '<td>'+data[i].num+'</td>';
			}
			$("#tbody_"+id).html(text);
			//弹出结果集
			layer.open({
			    type: 1, //page层 1div，2页面
			    shadeClose: false,
			    area: ['1100px', '500px'],
			    title: '修改机构信息',
			    moveType: 2, //拖拽风格，0是默认，1是传统拖动
			    content: $("#ins_"+id),
			    btn: ['提交','取消'],
				yes: function(){
					var count = $("#tbody_"+id+" tr").length-1;
					var customerArray = new Array();
					for(var i = 0; i < count; i++){
						customerArray.push({adminId: $("#adminId_"+id+"_"+i).val(), adminpassword: $("#adminpassword_"+id+"_"+i).val(),adminIP: $("#adminIP_"+id+"_"+i).val(),
							adminEmail: $("#adminEmail_"+id+"_"+i).val()});
					}
					$.ajax({
						type : "post",
						url : "../auser/updateins.do",
						data:{"institution":$("#institu_"+id).val(),"oldins":$("#ins_hidden_"+id).val(),"adminList":JSON.stringify(customerArray)},
						success: function(data){
							if(data.flag=="true"){
								layer.msg('管理员信息更新成功', {icon: 1});
							}else{
								layer.msg('更新失败', {icon: 2});
							}
							layer.closeAll();
							if($("#institution").val()!=""){
								$("#institution").val($("#institu_"+id).val());
							}							
							findList();
						}
					});
			    },
			    end: function(){
			    	preventBubble(e);
			    }
			});
		}
	});
}


//锁定/解锁切换
function setfreeze(flag,obj,aid,e){
	preventBubble(e);
	$.ajax({
		type : "post",
		url : "../auser/setfreeze.do",
		data:{aid : aid,flag:flag},
		success: function(data){
			if(flag==2){
				$(obj).removeClass('icon_close').addClass('icon_open');
				$(obj).attr("onclick","setfreeze(1,this,'"+aid+"');")
			}else{
				$(obj).removeClass('icon_open').addClass('icon_close');
				$(obj).attr("onclick","setfreeze(2,this,'"+aid+"');")
			}
		}
	});
}

//收起/展开切换
function unfolded(flag,obj,index){
	if(flag==2){
		$(obj).removeClass('icon_top').addClass('icon_tdown');
		$(obj).attr("onclick","unfolded(1,this,'"+index+"');")
		$("#"+index).hide();
	}else{
		$(obj).removeClass('icon_tdown').addClass('icon_top');
		$(obj).attr("onclick","unfolded(2,this,'"+index+"');")
		$("#"+index).show();
	}
}

//移除管理员
function delAdmin(aid,e){
	preventBubble(e);
	$.ajax({
		type : "post",
		url : "../auser/deladmin.do",
		data:{"aid":aid},
		success: function(data){
		   if(data == "true"){
			   layer.msg('移除成功', {icon: 1});
			   findList();
		   }
		}
	});
}

//添加/更新管理员
function setAdmin(userId,pid,institution,flag,e){
	preventBubble(e);
	$("#aid").val(userId);
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['750px', '520px'],
	    title: '机构管理员信息',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: 'goaddadmin.do?pid='+pid+"&institution="+institution+"&userId="+userId+"&flag="+flag
	}); 
}

//分页
function goPage(curr){
	var totalRow = $("#totalRow").val();
	var pageSize = $("#pageSize").val();
	var pageNum = $("#pageNum").val();
	if(totalRow!=""&&pageSize!=""){
		laypage({
	        cont: $("#page"),
	        pages: Math.ceil(totalRow/pageSize),
	        curr: pageNum || 1,
	        skip: true, //是否开启跳页
	        last: '尾页',
	        first: '首页',
	        groups: 5,
	        skin: 'molv',
	        jump: function (obj,first){
				if(!first){
					$("#pageNum").val(obj.curr);
					$("#fromList").submit();
				}
	        }
	    });
	}else{
		$("#page").html("");
	}
}

function openPurchaseInfo(userId,payid,pscode,type){
	$("#open_"+userId+"_"+payid+"_"+pscode).find("li").removeClass("active");
	if(type.indexOf("perio")>-1){
		$("a[href='#perio_"+userId+"_"+payid+"_"+pscode+"']").parent().addClass("active");
		$("#perio_"+userId+"_"+payid+"_"+pscode).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("degree")>-1){
		$("a[href='#degree_"+userId+"_"+payid+"_"+pscode+"']").parent().addClass("active");
		$("#degree_"+userId+"_"+payid+"_"+pscode).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("conf")>-1){
		$("a[href='#conf_"+userId+"_"+payid+"_"+pscode+"']").parent().addClass("active");
		$("#conf_"+userId+"_"+payid+"_"+pscode).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("patent")>-1){
		$("a[href='#patent_"+userId+"_"+payid+"_"+pscode+"']").parent().addClass("active");
		$("#patent_"+userId+"_"+payid+"_"+pscode).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("book")>-1){
		$("a[href='#book_"+userId+"_"+payid+"_"+pscode+"']").parent().addClass("active");
		$("#book_"+userId+"_"+payid+"_"+pscode).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("standard")>-1){
		$("a[href='#standard_"+userId+"_"+payid+"_"+pscode+"']").parent().addClass("active");
		$("#standard_"+userId+"_"+payid+"_"+pscode).addClass("active").siblings().removeClass("active");
	}else if(type.indexOf("local chronicles")>-1){
		$("a[href='#localchronicles_"+userId+"_"+payid+"_"+pscode+"']").parent().addClass("active");
		$("#localchronicles_"+userId+"_"+payid+"_"+pscode).addClass("active").siblings().removeClass("active");
		showGazetteers(userId,payid,pscode);
	}
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['50%', '700px'],
	    title: '详情',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#open_"+userId+"_"+payid+"_"+pscode),
	    btn: ['确认', '取消'],
		yes: function(){
	        layer.closeAll();
	    },
	}); 
}

//初始化新方志模块
function showGazetteers(userId,payid,pscode){
	var temp=userId+"_"+payid+"_"+pscode;
	var area=$("#gazetteers_area_"+temp).val();
	if(area!=null){
		var areas=area.split("_");
		if(areas.length>=1){
			$("#sheng_"+temp).html(areas[0])
		}
		if(areas.length>=2){
			$("#shi_"+temp).html(areas[1])
		}
		if(areas.length>=3){
			$("#xian_"+temp).html(areas[2])
		}
	}
	var type=$("#gazetteers_type_"+temp).val();
	if(type!=null){
		if(type=="FZ_New"){
			$("#zjfl_"+temp).html("专辑分类");
		}else{
			$("#zjfl_"+temp).html("专题分类");
		}
	}
	
	var album=$("#gazetteers_album_"+temp).val();
	if(album!=null&&album!=""){
		var albums=album.split(";");
		var text="";
		for(var i=0;i<albums.length;i++){
			if(albums[i]==null || albums[i]==''){
				continue;
			}
			text +='<input type="checkbox" value="'+name+'" name="classCode2_'+temp+'" checked/> '+albums[i]+' &nbsp;';
			if(i>0&&i%4==0&&albums.length!=i+1){
				text +='<br/>'
			}
		}
		$("#new_class_code_"+temp).html(text);
	}
}

//服务权限设置隐藏/显示
function btnBlack(obj,e){
	preventBubble(e);
	var group = $(obj).parent().attr("class");
	if(group=="btn-group"){
		$(obj).parent().addClass("open");
	}else{
		$(obj).parent().removeClass("open");
	}
}


//服务权限设置弹窗
function showAuthority(msg,title,userId,e){
	preventBubble(e);
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['750px', '520px'],
	    title: title,
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: 'showAuthority.do?msg='+msg+'&userId='+userId
	}); 
}


//数据提交
function findList(){
	var institution = $("#institution").val();
	if(institution!=null && institution!=""){
		if(!institution.startWith("%") && !institution.endWith("%")){
			institution = "%" + institution + "%";
		}else if(institution.startWith("%") && !institution.endWith("%")){
			institution = institution + "%";
		}else if(!institution.startWith("%") && institution.endWith("%")){
			institution = "%" + institution;
		}
		$("#institution").val(institution);
	}
	$("#pageNum").val(1);
	$("#fromList").submit();
}


String.prototype.startWith=function(str){     
	var reg = new RegExp("^"+str);     
	return reg.test(this);        
}  

String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}