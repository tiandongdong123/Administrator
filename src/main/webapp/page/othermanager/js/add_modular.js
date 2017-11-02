/*对提交内容进行验证-liuYong*/
function submission() {
//	 alert("验证结果------"+fieldsCheck());
	if (fieldsCheck()) {
		doAddModular();
	}
}

function doAddModular() {
	var modularName = $("#modularName").val();
	var linkAddress = $("#linkAddress").val();
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	if(modularName!=''&&modularName!=' '&&!re.test(modularName)&&linkAddress!=''&&linkAddress!=' '&&!re.test(linkAddress))
		{
		   if(IsURL(linkAddress)){
			   $.ajax({
					type : "POST",
					url : "../modular/doAddModular.do",
					data : {
						'modularName' : modularName,
						'linkAddress' : linkAddress
					},
					dataType : "json",
					success : function(data) {
						if (data) {
							layer.msg("添加成功",{time:2000});
							window.location.href='../modular/modularManager.do';
						} else {
							layer.msg("添加失败",{time:2000});
						}
					}
				});
		   }else{
			   layer.msg("页面链接格式不正确！",{icon: 2});
		   }
		}else{
			layer.msg("请输入功能模块和页面链接！",{icon: 2});
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
	window.location.href="../modular/addModular.do";
}



