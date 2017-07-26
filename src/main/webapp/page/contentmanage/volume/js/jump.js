var url;
$(function(){
	$.ajax({
		type : 'post',
		url: '../content/geturl.do',
		dataType : 'json',
		success : function(data){
			url = data;
		},
	})
})
function jump(docuType,docuId){
	window.open(url.search+"details/detail.do?_type="+docuType+"&id="+docuId);//跳详情页
}