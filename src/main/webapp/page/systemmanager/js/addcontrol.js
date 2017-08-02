function doaddcontrol(){
	var pid = $("#pid").val();
	var ip = $("#controlip").val();
	if(ip!=null&&ip!=''){
		$.ajax( {  
			type : "POST",  
			url : "../control/doaddcontrol.do",
			data:{
				"pid":pid,
				"ip":ip
			},
				dataType : "json",
				success : function(data) {
					if(data){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg('添加成功');
						parent.location.reload();
					}else{
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg('添加失败');
						parent.layer.close(index); 
					}
				}
		});

	}else{
		$("#checkurl").text("请输入URL地址");
	}
	
}