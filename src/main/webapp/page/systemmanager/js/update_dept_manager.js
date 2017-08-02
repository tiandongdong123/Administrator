function doupdatedept(){
	var val = $("#checkname").val();
	if(val=='y'){
	var deptname= $("#deptname").val();
	if(deptname!=null&&deptname!=''){
	var describe=$("#deptdescribe").val();
	var id = $("#deptid").val();
	$.ajax( {  
		type : "POST",  
		url : "../dept/doupdatedept.do",
			data : {
				'id':id,
				'deptName' : deptname,
				'deptDescribe' : describe,
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
		$("#rtcname").text("部门名称不能为空");
	}
	}
}
function checkdeptname(){
	var deptname= $("#deptname").val();
	var deptnamebef=$("#deptnamebef").val();
	if(deptname!=deptnamebef){
		$.ajax( {  
			type : "POST",  
			url : "../dept/checkdept.do",
				data : {
					'deptname' : deptname,
				},
				dataType : "json",
				success : function(data) {
					if(data){
						$("#checkname").val("n");
						$("#rtcname").text("部门名称重复");
					}else{
						$("#checkname").val("y");
						$("#rtcname").text("");
					}
				}
			});
	}
}
var name="";
var word="";
$(function(){
	name=$("#deptname").val();
	word=$("#deptdescribe").val();
});


function shuaxin(){
	$("#deptname").val(name);
	$("#deptdescribe").val(word);
}