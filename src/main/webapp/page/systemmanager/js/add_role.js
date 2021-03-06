$(function(){
	purview();
});


function purview(){
	$.ajax({  
	    type : "POST",  
	    url : "../role/getpurview.do", 
	    dataType: "json",
	    success : function(data) {  
	    	purviewtree(data);   
	    },  
	});  
}


function purviewtree(json){
	var curMenu = null, zTree_Menu = null;
	var setting = {
		view: {
			showLine: true,
			selectedMulti: false,
			dblClickExpand: false,
			showTitle: false,
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y" : "ps", "N" : "s" }
		},
		
		data: {
			simpleData: {
				idKey:"id",
				enable: true
			},
			key: {
				name: "name",
				title: "name"
			}
		},
		callback: {
			onCheck: onCheck
		}
	};

	var zNodes =json.purview;
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
		//zTree_Menu.expandNode(zTree_Menu.getNodes()[0],true);
	});
	var allNodes = zTree_Menu.getCheckedNodes(false).length
	function onCheck(){
		var tree="";
		var checknodesNum=0;
		var a = zTree_Menu.getCheckedNodes(true);
		for(var i=0;i<a.length;i++){
			tree+= a[i].id+",";
			 if(a[i].level==0){
				   checknodesNum++;
			   }
		}
		tree = tree.substring(0,tree.length-1);
		$("#treeids").val(tree);
		var nodes = zTree_Menu.getNodes();
		var beforNum = $('#treeids').val().split(',').length
		$("#checkall").prop("checked",beforNum==allNodes);
	}
}

function checkrolename(name){
	if(name!=null&&name!=''){
		$.ajax( {  
			type : "POST",  
			url : "../role/checkrolename.do",
				data : {
					'name' : name,
				},
				dataType : "json",
				success : function(data) {
					if(data){
							$("#cname").val("N")
							$("#checkrolename").text("名称重复，请重新输入");
					}else{
							$("#cname").val("Y")
							$("#checkrolename").text("");
					}
				}
			});
	}else{
		$("#checkrolename").text("请输入角色名称");
		$("#cname").val("N");
	}
	
}

function doaddrole(){
	var cname=$("#cname").val();
	if(cname=="Y"){
		var rolename=$("#rolename").val();
		var roledescribe = $("#roledescribe").val();
		var ids = $("#treeids").val();
		var deptname=$("#deptname").find("option:selected").val();
		if(ids.length===0) {
			$("#ruleName").text('请选择角色权限')
			return
		}
		if((ids.indexOf("A11")!=-1||ids.indexOf("A12")!=-1||ids.indexOf("A13")!=-1||ids.indexOf("A141")!=-1)&&ids.indexOf("A142")==-1){
			$("#roleName").text('请选择"添加/移除机构管理员"权限')
			return
		}
		$.ajax({  
			type : "POST",  
			url : "../role/doaddrole.do",
				data : {
					'roleName' : rolename,
					'description':roledescribe,
					'purview': ids,
					'department':deptname
				},
				dataType : "json",
				success : function(data) {
					if(data){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("添加成功");
						window.parent.rolepage(1);
						parent.layer.close(index);
					}else{
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("添加失败");
						window.parent.rolepage(1);
						parent.layer.close(index);
					}
				}
			});
	}else{
		checkrolename($('#rolename').val())
	}
}

function checkrole(obj){//全选
	if(obj.checked){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.checkAllNodes(true);
		//获取treeids
		var tree="";
		var a = treeObj.getCheckedNodes(true);
		for(var i=0;i<a.length;i++){
			tree+= a[i].id+",";
		}
		tree = tree.substring(0,tree.length-1);
		$("#treeids").val(tree);
	}else{
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.checkAllNodes(false);
		$("#treeids").val('');
	}
}

function resttree(){
	purview();
}

//取消按钮
function closeWindow() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}
