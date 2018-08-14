$(function(){
	if($(".parameter").val()){
		$("#userId").text($(".parameter").val());
		findList();
	}
	goPage();
    $('.add_admin').click(function (e) {
        preventBubble(e);
    });

	//点击重置二维码
	$(document).on("click",".resets",function(){
		var userId_qr = $(this).parents(".info_cont").find(".user_qrCode").text();
		var pictures = $(this).siblings('.pictures');
		pictures.attr('src','../bindAuhtority/resetQRCode.do?userId='+userId_qr+'&time='+(new Date()));
	});
	if($("#proType").val()!=''){
		queryType();
	}
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
	//弹出结果集
	layer.open({
	    type: 1, //page层 1div，2页面
	    shadeClose: false,
	    area: ['500px', '200px'],
	    title: '修改机构名称',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#ins_"+id),
	    btn: ['修改','取消'],
		yes: function(){
			var institution=$("#institu_"+id).val();
			var reg = /^[\u4e00-\u9fa5 A-Za-z0-9-_（）()]+$/;
			if(institution==null||institution==""){
				layer.msg('机构名称不能为空，请填写规范的机构名称', {icon: 2,time: 1000});
				return false;
			}
			if(!reg.test(institution)){
				layer.msg('格式不对，请填写规范的机构名称', {icon: 2,time: 1000});
				return false;
			}
			var olds=$("#ins_hidden_"+id).val();
			if(institution==olds){
				layer.msg('未修改机构名称', {icon: 2,time: 1000});
				return false;
			}
			$.ajax({
				type : "post",
				url : "../auser/findInstitutionAllUser.do",
				data:{"institution":olds},
				success: function(data){
					var html="";
					var admin=data.admin;
					var user=data.user;
					if(user!=null){
						html+="该机构下的机构账号有：</br>";
						var array=user.split(",");
						for(ar in array){
							html+=(parseInt(ar)+1)+"、"+array[ar]+"</br>";
						}
					}
					if(admin!=null){
						html+="该机构下的机构管理员有：</br>";
						var array=admin.split(",");
						for(ar in array){
							html+=(parseInt(ar)+1)+"、"+array[ar]+"</br>";
						}
					}
					if(user!=null&&admin!=null){
						html+="确定要同时修改所有机构账号及机构管理员的机构名称吗？";
					}else if(admin==null){
						var array=user.split(",");
						if(array.length>1){
							html+="确定要同时修改所有机构账号的机构名称吗？";
						}else{
							html+="确定要修改机构账号的机构名称吗？";
						}
					}
					layer.alert(html,{
						icon: 1,
						title : ["确认", true],
					    skin: 'layui-layer-molv',
					    btn: ['确定','取消'],
					    yes: function(){
					    	updateInstitution(institution,olds,id);//更新机构名称
					    }
					});
				}
			});
	    },
	    end: function(){
	    	preventBubble(e);
	    }
	});
}

//更新机构名称
function updateInstitution(news,olds,id){
	$.ajax({
		type : "post",
		url : "../auser/updateins.do",
		data:{"institution":news,"oldins":olds},
		success: function(data){
			if(data.flag=="true"){
				layer.msg('管理员信息更新成功', {icon: 1});
			}else{
				layer.msg('管理员信息更新失败', {icon: 2});
				return;
			}
			layer.closeAll();
			if($("#institution").val()!=""){
				$("#institution").val($("#institu_"+id).val());
			}
			findList();
		}
	});
}


//锁定/解锁切换
function setfreeze(flag,obj,aid,e){
	preventBubble(e);
	var msg="";
	var title="";
	if(flag=='1'){
		msg="确定要冻结机构账户吗？";
		title="冻结机构账号";
	}else{
		msg="确定要解冻机构账户吗？";
		title="解冻机构账号";
	}
	layer.alert(msg,{
		icon: 1,
		title : [title, true],
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'],
	    yes: function(){
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
	    			layer.closeAll();
	    		}
	    	});
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
function delAdmin(userId,e){
	preventBubble(e);
	layer.alert('确定要移除机构管理员吗？',{
		icon: 1,
		title:['移除机构管理员',true],
	    skin: 'layui-layer-molv',
	    btn: ['确定','取消'],
	    yes: function(){
	    	$.ajax({
	    		type : "post",
	    		url : "../auser/deladmin.do",
	    		data:{"userId":userId},
	    		success: function(data){
	    		   if(data == "true"){
	    			   layer.msg('移除成功', {icon: 1});
	    			   findList();
	    		   }
	    		}
	    	});
	    }
	});
}

//添加/更新管理员
function setAdmin(userId,pid,institution,e){
	preventBubble(e);
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['750px', '520px'],
	    title: '添加机构管理员',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: '../auser/goaddadmin.do?institution='+institution+'&userId='+userId
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
	    area: ['40%', '700px'],
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
			if(i>0&&i%5==0&&albums.length!=i+1){
				text +='<br/>'
			}
			text +='<input type="checkbox" value="'+name+'" name="classCode2_'+temp+'" checked/> '+albums[i]+' &nbsp;';
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

//数据提交
function findList(){
	var ipSegment = $("#ipSegment").val();
	if(ipSegment!=null&&ipSegment!=''){
		if(!checkOneIp(ipSegment)&&!IpFormat(ipSegment)){
			$("#ipResult").html("IP格式不合法");
			$("#ipSegment").css("border","1px solid red");
			return false;
		}
	}
	$("#ipResult").html("");
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
	var proType=$("#proType").val();
	var resource=$("#resource").val();
	if(proType!=''&&resource==''){
		layer.msg('请选择具体的购买项目', {icon: 2});
		return false;
	}
	$("#pageNum").val(1);
	$("#fromList").submit();

}

String.prototype.startWith=function(str){
	var reg=new RegExp("^"+str);
	return reg.test(this); 
}

String.prototype.endWith=function(str){
	var reg=new RegExp(str+"$");
	return reg.test(this);
}

//校验ip
function checkOneIp(val){
	// ip地址
	if (val.startWith("0.")) {
		return false;
	}
	var exp=/^(\d{1,2}|1\d\d|2[0-1]\d|22[0-3])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	var reg = val.match(exp);
	if (reg == null) {
		return false;
	} else {
		return true;
	}
}

String.prototype.startWith=function(str){     
	var reg = new RegExp("^"+str);     
	return reg.test(this);        
}  

String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}

//工单类型
function queryOrder(obj){
	var type=$("#OrderType").val();
	$("#OrderContent").val("");
	if(type=='inner'){
		$("#ordercontent_td").show();
	}else{
		$("#ordercontent_td").hide();
	}
}
//选择购买项目
function queryType(){
	var val=$("#proType").val();
	if(val==""){
		$("#resource_td").hide();
		return;
	}
	$("#resource").html('<option value="">--请选择--</option>');
	var oldresource=$("#oldresource").val();
	$.ajax({
		type:"post",
		async: false,
		url:"../auser/getProject.do",
		data:{"val":val},
		dataType:"json",
		success:function(data){
			for(var i in data){
				var pro=data[i];
				var type="";
				if(pro.id==oldresource){
					type='selected="selected"';
				}
				$("#resource").append('<option value="'+pro.id+'"'+type+'>'+pro.name+'</option>');
				$("#resource_td").show();
			}
		}
	});
}

//显示更多菜单
function showMore(){
	var isHidden=$("#isHidden").val();
	$("#openLimit").val('');
	$("#Organization").val('');
	$("#PostCode").val('');
	$("#OrderType").val('');
	$("#proType").val('');
	$("#OrderContent").val('');
	$("#resource").val('');
	if(isHidden=='0'){
		$("#isHidden").val('1');
		$("#openlimit_td").show();
		$("#organization_id").show();
		$("#postcode_id").show();
		$("#ordertype_id").show();
		$("#protype_id").show();
	}else{
		$("#isHidden").val('0');
		$("#openlimit_td").hide();
		$("#organization_id").hide();
		$("#postcode_id").hide();
		$("#ordertype_id").hide();
		$("#protype_id").hide();
	}
}