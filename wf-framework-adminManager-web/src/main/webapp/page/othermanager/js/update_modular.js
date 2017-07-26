function submission() {
//	 alert("验证结果------"+fieldsCheck());
	if (fieldsCheck()) {
		doUpdateModularManager();
	}
}

function doUpdateModularManager(){
		var id=$("#id").val();
		var modularName = $("#modularName").val();
		var linkAddress=$("#linkAddress").val();
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		if(id!=''&&id!=' '&&!re.test(id)&&modularName!=''&&modularName!=' '&&!re.test(modularName)&&linkAddress!=''&&linkAddress!=' '&&!re.test(linkAddress))
			{
			   if(IsURL(linkAddress)){
				   $.ajax( {  
						type : "POST",  
						url : "../modular/doUpdateModular.do",
							data : {
								'id': id,
								'modularName':modularName,
								'linkAddress':linkAddress
							},
							dataType : "json",
							success : function(data) {
								if(data){
									
									layer.msg("修改成功",{time:2000});
									window.location.href='../modular/modularManager.do';
								}else{
									layer.msg("修改失败",{time:2000});
								}
							}
						});
			   }else{
				   layer.msg("页面链接格式不正确！",{icon: 2});
			   }
			}else{
				layer.msg("请输入功能模块和页面链接！");
			}
}

//----------------------------------检测url地址的js-----------------------------------------
function IsURL(str_url){
	var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
	var re=new RegExp(reg);
	if(re.test(str_url)){
		return (true);
	}else{
		return (false);
	}
}

