$(function(){
	action();
});

function action(){
	var colums=parent.$("#colums").find("option:selected").val();
	var title=parent.$("#title").val();
	var abstracts=parent.document.getElementById("abstracts").value;
	var editor1=parent.ues.getContent();
	var linkAddress=parent.$("#linkAddress").val();
	var author=parent.$("#author").val();
	var organName=parent.$("#organName").val();
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	if(abstracts!=''&&abstracts!=undefined){
		$("#zy").html("摘要：");
	}
	$("#cloums1").text(colums);
	$("#title1").text(title);
	$("#title2").text(title);
	$("#title0").text(abstracts);
	$("#context1").append(editor1);
	$("#source").text(organName);
	$("#anthours").text(author);
	$("#data").text(str);
}