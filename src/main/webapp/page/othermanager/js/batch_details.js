$(function(){
	//上传附件
	$('#file').change(function(){
		submitFile();
	});
});

//显示上传附件按钮
function updateFile(){
	$("#adjunct").show();
}

//上传附件
function submitFile(){
	var filepath = $('#file').val();
	var extStart = filepath.lastIndexOf('\\');
	var ext = filepath.substring(extStart+1, filepath.length).toUpperCase();//上传文件的后缀
	$('#file_name').html(ext);
	var formData = new FormData();
	formData.append('batchId', $("#batchId").val());
	formData.append('file', $('#file')[0].files[0]);
	if($.trim($('#file')[0].files[0]) == '' || $('#file')[0].files[0] == null){
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
	$.ajax({
		type : "post",  
		url : "../card/updateAttachment.do",
		cache: false,
		data: formData,
		processData: false,
		contentType: false,
		success : function(data){
			if(data.msg){
				$("#download").attr("href","../card/download1.do?url="+data.adjunct);
				layer.msg('提交成功', {icon: 1});
			}else{
				layer.msg('提交失败', {icon: 1});
			}
		},
		error : function(data){
			layer.msg("提交失败！");
		}
	});
}