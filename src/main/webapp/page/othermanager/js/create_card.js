var valueNumber = new Array();
var type = "";
var value = "";
var number = "";
var validStart = "";
var validEnd = "";
var applyDepartment = "";
var applyPerson = "";
var applyDate = "";
var file = $("#adjunct").val();

$(function(){
	$('#file').change(function(){
		var filepath = $('#file').val();
		var extStart = filepath.lastIndexOf('\\');
		var ext = filepath.substring(extStart+1, filepath.length).toUpperCase();//上传文件的后缀
		$('#file_name').html(ext);
	})
});

function showMore(){
	$("#add_more").show();
	value = $("#value").val();
	number = $("#number").val();
	valueNumber.push(value+"/"+number);

	showarea();
}

function addCardType(){
	window.location.href='../card/createCardType.do';
}


function put(){
	type = $("#cardType").val(); 
	validStart = $("#valid_start").val();
	validEnd = $("#valid_end").val();
	applyDepartment = $("#apply_department").val();
	applyPerson = $("#apply_person").val();
	applyDate = $("#apply_date").val();
	var valueNumber="[";
	 $("input[name='filer_val']").each(function(index,item){
		 var data=$(this).attr("value").split("_");
		 valueNumber=valueNumber+"{ value:"+data[0]+", number:"+data[1]+"},";
	 });
	 valueNumber=valueNumber+"]";
	 valueNumber=valueNumber.replace(",]", "]");
	 valueNumber=eval(valueNumber);
	var formData = new FormData();
	formData.append('type', type);
	formData.append('valueNumber', JSON.stringify(valueNumber));
	formData.append('validStart', validStart);
	formData.append('validEnd', validEnd);
	formData.append('applyDepartment', applyDepartment);
	formData.append('applyPerson', applyPerson);
	formData.append('applyDate', applyDate);
	formData.append('file', $('#file')[0].files[0]);
	if(valueNumber.length < 1){
		layer.msg("面值和张数不能为空",{icon: 2});
		return false;
	}else if($.trim($('#valid_start').val()) == '' || $('#valid_start').val() == null){
		layer.msg("有效期不能为空",{icon: 2});
		return false;
	}else if($.trim($('#valid_end').val()) == '' || $('#valid_end').val() == null){
		layer.msg("有效期不能为空",{icon: 2});
		return false;
	}else if($.trim($('#apply_department').val()) == '' || $('#apply_department').val() == null){
		layer.msg("申请部门不能为空",{icon: 2});
		return false;
	}else if($.trim($('#apply_person').val()) == '' || $('#apply_person').val() == null){
		layer.msg("申请人不能为空",{icon: 2});
		return false;
	}else if($.trim($('#apply_date').val()) == '' || $('#apply_date').val() == null){
		layer.msg("申请日期不能为空",{icon: 2});
		return false;
	}else if($.trim($('#file')[0].files[0]) == '' || $('#file')[0].files[0] == null){
		layer.msg("请上传附件",{icon: 2});
		return false;
	}
	//获取上传文件的格式
	var filepath = $('#file').val();
	var extStart = filepath.lastIndexOf(".");
	var ext = filepath.substring(extStart, filepath.length).toUpperCase();//上传文件的后缀
	if(ext!='.BMP'&&ext!='.PNG'&&ext!='.GIF'&&ext!='.JPG'&&ext!='.JPEG'&&ext!='.PDF'){
		layer.msg("上传的附件只能是图片或是PDF文件",{icon: 2});
		return false;
	}
	$("#submit").attr({disabled: "disabled"});
	$.ajax({
		type : "post",  
		url : "../card/addCardBatch.do",
		cache: false,
		data: formData,
		processData: false,
		contentType: false,
		success : function(data){
			if(data){
				layer.msg('提交成功', {icon: 1});
				reback();
			}
			$("#submit").removeAttr("disabled");
		},
		error : function(data){
			layer.msg("提交失败！");
			$("#submit").removeAttr("disabled");
		}
	});
}

function showarea()
{	

	var value=$("#value").val();
	var number=$("#number").val();
	var reg = new RegExp("^[0-9]*$");
	var r = new RegExp("^\\d+(\\.\\d+)?$");
	var oDate = new Date(); //实例一个时间对象；
	var date=oDate.getFullYear()+""+oDate.getMonth()+1+""+oDate.getDate()+""+oDate.getHours()+""+oDate.getSeconds();
	 if (r.test(value)&&value!=''&&value!=" "&&reg.test(number)&&number!=''&&number!=" ")
	 {
		 var select= "<div id="+date+" class=\"FilerItem\"><input type='hidden' name='filer_val' value='"+value+"_"+number+"'><span>"+value+"(元)x"+number+"(张) <i onclick=\"del_click('"+date+"');\" class=\"del\"></i></span></div>";
			$(".FilterList2").append(select);
			$(".FilterList2").show(); 
	 }
	else 
	{
	layer.msg("请输入正确格式！");	
	}
}

function del_click(data){
	$("#"+data).remove();
}

function reback(){
	$("input").val("");
	$(".FilerItem").remove();
}