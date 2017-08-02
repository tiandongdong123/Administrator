function doupdatecontrol(){
	var id = $("#controlid").val();
	var ip = $("#controlip").val();
	if(ip!=null&&ip!=''){
		$.ajax( {  
			type : "POST",  
			url : "../control/doupdatecontorl.do",
			data:{
				"id":id,
				"ip":ip
			},
				dataType : "json",
				success : function(data) {
					if(data){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg('修改成功');
						parent.location.reload();
					}else{
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg('修改失败');
						parent.layer.close(index); 
					}
				}
		});
	}else{
		$("#checkurl").text("请输入URL地址");
	}
}

function deletecontrol(){
	layer.confirm('确定要删除此节点？', {
		  btn: ['确定','取消'] //按钮
		}, function(){
			var id = $("#controlid").val();
			dodeletecontrol(id);
		}, function(){
			layer.close();
		});
}

function dodeletecontrol(id){
	$.ajax( {  
		type : "POST",  
		url : "../control/dodeletecontrol.do",
		data:{
			"id":id
		},
			dataType : "json",
			success : function(data) {
				if(data){
					var index = parent.layer.getFrameIndex(window.name);
					parent.layer.msg('删除成功');
					parent.location.reload();
				}else{
					var index = parent.layer.getFrameIndex(window.name);
					parent.layer.msg('删除失败');
					parent.layer.close(index); 
				}
			}
	});
}