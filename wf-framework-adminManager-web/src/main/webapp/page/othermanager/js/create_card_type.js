function put(){
	var code=$("#code").val();
	var name=$("#name").val();
	
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	
	if(code!=''&&code!=undefined&&!re.test(code)&&name!=''&&name!=undefined&&!re.test(name)){
		$.post("../card/checkcode.do",{code:code,name:name},function(data){
			if(data==0){
				addtype(code,name);
			}
			else{
				layer.msg("此充值卡类型Code已存在！请重新输入！");
			}
		});
	}else{
		layer.msg("*为必填项！");
	}

}

function addtype(code,name){
	$.post("../card/addcardtype.do",{code:code,name:name},function(data){
		if(data==1)
			{
			layer.msg("添加成功！");
			window.location.href='../card/createCard.do';
			}else{
				layer.msg("添加失败！");
			}
	});
}