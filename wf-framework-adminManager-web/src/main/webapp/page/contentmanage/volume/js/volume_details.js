function jump(obj){
	var ex = document.getElementById("parent");  
	var offsetTop = 0;
	if(obj == 1){
		ex.scrollTop = offsetTop;    
	}else{
		for(var i = 1;i < obj;i++){
			offsetTop += $("#son" + i).height();
		}
		ex.scrollTop = offsetTop + 10;
	}
}