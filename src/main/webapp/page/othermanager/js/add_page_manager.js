function submission() {
//	 alert("验证结果------"+fieldsCheck());
	if (!fieldsCheck()) {
		$("#btt").removeAttr("disabled");
	}else{
		doAddPageManager();
	}
}

function doAddPageManager(){
	var modularId = $("#modularId").val();
	var pageName = $("#pageName").val();
	var linkAddress=$("#linkAddress").val();
	
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	
	if(modularId!='' && modularId!=' ' && !re.test(modularId) && pageName!='' && pageName!=' ' && !re.test(pageName) && linkAddress!='' && linkAddress!=' ' && !re.test(linkAddress)){
		if(IsURL(linkAddress)){
			
			$.ajax( {  
				type : "POST",  
				url : "../page/doAddPageManager.do",
					data : {
						'modularId':modularId,
						'pageName':pageName,
						'linkAddress':linkAddress
					},
					dataType : "json",
					success : function(data) {
						if(data){
							layer.msg("添加成功",{time:2000});
							window.location.href='../page/pageManager.do';
						}else{
							layer.msg("添加失败",{time:2000});
						}
					}
				});
			
		}else{
			layer.msg("页面链接格式不正确！",{icon: 2});
		}
		
	}else{
		layer.msg("请选择功能模块或输入页面名称、链接！",{icon: 2});
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

function reset(){
	window.location.href="../page/addPageManager.do";
}


