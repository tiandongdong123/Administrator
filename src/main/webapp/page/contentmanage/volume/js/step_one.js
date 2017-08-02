//-------------------选择模板-------------------
function chooseTem(obj){
	var src = $(obj).find('img').attr('src');
	$('.choosed').css("background-image","url("+src+")");
	$('#coverUrl').val(src.substring(2));
}
function useTem(){
	var src = $('#template').find('img').attr('src');
	$('#coverUrl').val(src.substring(2));
}
var subject_val = "";
var subject_value = "";
//-------------------表单提交数据----------------
function submitForm(){
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
//	if(subject_value.substring(1) != '' && $('#subject').val() != subject_value.substring(1)){//学科分类有变化重新提交表单
//		
		$('#subject').val(subject_value.substring(1));
		$('#subjectName').val(subject_val.substring(1));
//	}
	var flag = true;
	//文辑名称js验证
	var flag1 = checkName('volumeName');
	//文辑简介js验证
	var flag2 = checkName('volumeAbstract');
	//文辑关键词js验证
	var flag3 = checkName('keyword');
	flag = flag1 && flag2 && flag3;
	//学科为空js验证
	if(subject_value.substring(1) == ''){
		$('#checkSubject').after('<span style="color: #FF0000;">学科不能为空!</span>');
		flag = false;
	}
	if(flag){
		
		if($.trim($('#coverUrl').val()) == '' || $('#coverUrl').val() == null || $('#coverUrl').val()== undefined){
			$('#coverUrl').val('/images/cover01.jpg');
		}
		$("#fromList").submit();
	}
}
//文辑名称、简介、关键词js验证
function checkName(text){
	var num = 50;//提示长度
	var nameHtml = '';//提示框
	$('#'+text).siblings().remove()
	if(text == 'volumeAbstract'){//文辑简介不能超过150
		num = 150;
	}
	if($('#'+text).val().trim() == '' || $('#'+text).val() == null){
		nameHtml = '<span style="color: #FF0000;">输入框不能为空!</span>';
		$('#'+text).after(nameHtml);
		return false;
	}else if($('#'+text).val().trim().length > num){//长度不能大于50
		nameHtml = '<span style="color: #FF0000;">长度不能大于'+num+'</span>';
		$('#'+text).after(nameHtml);
		return false;
	}
	return true;
}
$(function(){
	
	//------------文辑封面显示----------------
	if($('#coverUrl').val() != null && $('#coverUrl').val()!= ''){
		var coverUrl = $('#coverUrl').val();
		$('.choosed').css("background-image","url("+".."+coverUrl+")");
	}
	//-------------将标题加在图片上-----------------
	$('#volumeName').blur(function(){
		var volumeName = $('#volumeName').val().trim();
		var flag = checkName('volumeName');
		if(flag){
			$('.choosed_title').html(volumeName);
		}
	})
	//-------------文辑简介  js验证不为空---------------------
	$('#volumeAbstract').blur(function(){
		checkName('volumeAbstract');
	})
	//-------------关键词  js验证不为空---------------------
	$('#keyword').blur(function(){
		checkName('keyword');
	})
	
	//--------------------学科分类-----------------------
	var subjectName = $('#subjectName').val();
	var subjectValue = $('#subject').val();
	if(subjectName.length >0){//有学科分类
		var array = subjectName.split(",");
		var array1 = subjectValue.split(",");
		for(var i = 0;i<array.length;i++){
			
			$(".subject_text").append("<span style='color: #484848;'>"+array[i]+"<img onclick=\"del_subject(this,'"+array[i]+"');\" src='../images/theme_close.png' /><input hidden='true' name='subject_value' value='"+array1[i]+"'></span>");
		}
	}
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
//			subject_val += "," + text;
//			subject_value += "," + value1;
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
