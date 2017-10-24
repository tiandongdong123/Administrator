var institution_name;
$(function(){
	//输入相似性查询
	$("#institution,#adminname").keyup(function(e){
		var institution = $(this).val();
		institution_name = $(this).attr("name");
		var posturl = ""; 
		if(institution_name=="adminname"){
			posturl="../auser/getadminname.do";
		}else{
			posturl="../auser/getkeywords.do"
		}
		e.preventDefault();
		if(institution!="" && e.keyCode != 38 && e.keyCode != 40){
			$.ajax({
				url:posturl,
				type:"post",
				data:{"value":institution},
				success:function(data){
					var ul=$("#"+institution_name).siblings("div").find("ul");
					var div=$("#"+institution_name).siblings("div");
					ul.html("");
					if(data.length<=0){
						div.hide();
					}
					for(var i= 0; i< data.length; i++){
						ul.append("<li data-key="+data[i]+" onclick='addKeyword(this,0);' class='hoverli'><span>"+data[i]+"</span></li>");
					}
					var width_input = parseInt($("#"+institution_name).css("width"));
					div.css("width",width_input);
					div.show();
				}
			});
		}
	});
	
});

$(document).bind("click",function(e){ 
	var target = $(e.target); 
	if(target.closest(".searchsug").length == 0){ 
		$(".searchsug").find("li").remove(); 
		$(".searchsug").hide(); 
	} 
});
var eq_num = -1;
var input_value = "";
document.onkeydown=function(e){
	var li_ = $("#"+institution_name).siblings("div").find("li");
	if(li_.length>0 && (e.keyCode==38 || e.keyCode==40)){
		if(eq_num==-1){
			input_value = $("#"+institution_name).val();
		}
		$("#"+institution_name).blur();
		e.preventDefault();
		if (e.keyCode == 38) {
			// 向上键
			if (eq_num > 0) {
				eq_num--;
			} else if (eq_num == 0) {
				$("#"+institution_name).val(input_value);
				eq_num--;
			} else if (eq_num == -1) {
				eq_num = li_.length - 1;
			}
		} else {// 向下键
			if (eq_num < li_.length - 1) {
				eq_num++;
			} else if (eq_num == li_.length - 1) {
				$("#"+institution_name).focus();
				$("#"+institution_name).val(input_value);
				eq_num++;
			} else if (eq_num == li_.length) {
				eq_num = 0;
			}
		}
		if(eq_num != li_.length && eq_num != -1){
			$("#"+institution_name).siblings("div").find("li:eq("+eq_num+")").siblings("li").css("background-color","");
			$("#"+institution_name).siblings("div").find("li:eq("+eq_num+")").css("background-color","#D3D3D3");
    		addKeyword($("#"+institution_name).siblings("div").find("li:eq("+eq_num+")"),'1');
		}else{
			$("#"+institution_name).siblings("div").find("li").css("background-color","");
		}
		$("#"+institution_name).focus();
	}
}

//获取焦点重置 eq_num
function input_onfocus(text){
	eq_num = -1;
	$(text).attr("onfocus","");
}

function addKeyword(text,key_num){
	var add_keyword = $(text).find("span").text();
	var inputkeywords = $("#"+institution_name).val();
	if(inputkeywords.indexOf(":")>0){
		inputkeywords = inputkeywords.substring(0,inputkeywords.indexOf(":")+1);
		$("#"+institution_name).val(inputkeywords+add_keyword);
	}else{
		$("#"+institution_name).val(add_keyword);
	}
	if(key_num=="0"){
		$("#"+institution_name).siblings("div").hide();
		$("#"+institution_name).siblings("div").find("li").remove();
	}
	$("#"+institution_name).focus();
}

$(".searchsug ul li").hover(function(){
	$(this).addClass("bdsug-s");
	
},function(){
	$(this).removeClass("bdsug-s");
});
