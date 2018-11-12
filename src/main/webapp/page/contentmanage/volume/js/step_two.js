//----------------下一步------------------
function next(obj){
	$('#fromList').attr('action','../content/stepThree.do');
	$('#volumeChapter').val(obj);
	$('#fromList').submit();
}
//----------------返回上一步-------------------
function returnOne(){
	$('#fromList').attr('action','../content/papercollectadd.do');
	$('#fromList').submit();
}