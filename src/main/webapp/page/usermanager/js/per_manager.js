
$(function(){
	goPage(1);
	
});

//数据提交
function findList(){
	var str="";
	
	if($("#allUser").is(':checked')){
		str+=$("#allUser").val()+",";
	}
	
	$("input[name='userRole']").each(function(){ 
		if(this.checked){
			str+=$(this).val()+",";
		}
	});
	$("#selectUser").val(str);
//	$("#flag").val('1');
	$("#fromList").submit();
}

/*锁定/解锁切换*/
function setfreeze(flag,obj,userId){
	$.ajax({
		type : "post",
		url : "../auser/setfreeze.do",
		data:{aid : userId,flag:flag},
		success: function(data){
			if(flag==2){
				$(obj).removeClass('icon_close').addClass('icon_open');
				$(obj).attr("onclick","setfreeze(1,this,'"+userId+"');")
			}else{
				$(obj).removeClass('icon_open').addClass('icon_close');
				$(obj).attr("onclick","setfreeze(2,this,'"+userId+"');")
			}
		}
	});
}

/*编辑*/
function edit(userId){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['50%', '400px'],
	    title: '个人账号修改',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../auser/updatePerson.do?userId="+userId
	}); 
	
}


/*全选/全不选/各选项状态设置*/
function checkAll(obj, cName) {
	  if(obj.name=="allRoles"){
		  var checkboxs = document.getElementsByName(cName);  
		  for ( var i = 0; i < checkboxs.length; i+=1) {  
		    checkboxs[i].checked = obj.checked;  
		  }
	  }else if(obj.name="userRole"){
		  var checkboxs = document.getElementsByName(cName);  
		  for ( var i = 0; i < checkboxs.length; i+=1) {
			  if(checkboxs[i].checked!=true){
				  document.getElementsByName("allRoles")[0].checked=checkboxs[i].checked;  
			  }
		  }
	  }
	  
	  checkOne();
}

function checkOne(){
	if($("input[name='userRole']").length==$("input[name='userRole']:checked").length){
		$("#allUser").prop("checked", "checked");
	}else{
		$("#allUser").removeAttr("checked");
	}
}


//收起/展开切换
function unfolded(flag,obj,index){
	if(flag==1){
		$("#chart_"+index).hide();
		$("#form_"+index).show();
	}else{
		$("#chart_"+index).show();
		$("#form_"+index).hide();
	}
}

//选项卡切换
function tab(Id,id,obj){
	if(Id=='tabb'){
		$("#"+Id+"_"+id).css("display", "block");
		$(".cur").attr("");
		$(obj).siblings("li").removeClass("active");
		$("#"+Id+"_"+id).attr("cur");
		$('#tabbb_'+id).css("display","none");
		$("#tab_"+id).css("display","none");
	}
	else if(Id=='tabbb'){
		$("#"+Id+"_"+id).css("display", "block");
		$(".cur").attr("");
		$(obj).siblings("li").removeClass("active");
		$("#"+Id+"_"+id).attr("cur");
		$("#tab_"+id).css("display","none");
		$("#tabb_"+id).css("display","none");
	}
	else if(Id=="tab"){
		$("#tabbb_"+id).css("display","none");
		$(".cur").attr("");
		$(obj).siblings("li").removeClass("active");
		$("#"+Id+"_"+id).attr("cur");
		$("#tabb_"+id).css("display","none");
		$("#"+Id+"_"+id).css("display","block");
	}
	
	
}

//详情窗口
var i=0;
function details(id){
	var dom=document.getElementById(id);
	var pageX=dom.offsetLeft;
	var pageY=dom.offsetTop;
		 pageX = pageX+30,
         pageY = pageY+14;
        $("#"+id+"_details").css({
            top: pageY,
            left: pageX
        });
   if(i%2==0){
     $("#"+id+"_details").show();                    
     }else{
     $("#"+id+"_details").hide();
     } 
   i++;
}






//分页
function goPage(){
	var totalRow = $("#totalRow").val();
	var pageSize = $("#pageSize").val();
	var pageNum = $("#pageNum").val();
	var pages=Math.ceil(totalRow/pageSize);
	var groups;
	if(pages>=4)
    {
    groups=4;
    }else
    {
    	groups=pages;
    }
	
	if(totalRow!=""&&pageSize!=""){
		laypage({
	        cont: $("#page"),
	        pages: pages,
	        curr: pageNum || 1,
	        skin: 'molv',
	        groups: groups,
	        last: '尾页',
	        first: '首页',
	        prev: '上一页', //若不显示，设置false即可
  	      	next: '下一页', //若不显示，设置false即可
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