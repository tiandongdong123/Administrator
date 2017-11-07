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
			dblClickExpand: false
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y" : "ps", "N" : "ps" }
		},
		
		data: {
			simpleData: {
				idKey:"menuId",
				pIdKey:"pid",
				enable: true
			},
			key: {
				name: "menuName"
			}
		},
		callback: {
			onCheck: onCheck
		}
	};

	var zNodes =json;
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
		//zTree_Menu.expandNode(zTree_Menu.getNodes()[0],true);
	});
	
	function onCheck(){
		var tree=""
		var a = zTree_Menu.getCheckedNodes(true);
		for(var i=0;i<a.length;i++){
			tree+= a[i].menuId+",";
		}
		tree = tree.substring(0,tree.length-1);
		$("#treeids").val(tree);
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
		$("#checkrolename").text("请输入角色名");
	}
	
}

function doaddrole(){
	var cname=$("#cname").val();
	if(cname=="Y"){
		var rolename=$("#rolename").val();
		var roledescribe = $("#roledescribe").val();
		var ids = $("#treeids").val();
		var deptname=$("#deptname").find("option:selected").val();
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
						layer.msg("添加成功",{icon: 1});
						window.location.href="../system/roleManager.do";
					}else{
						layer.msg("添加失败",{icon: 2});
					}
				}
			});
	}else{
		$("#checkrolename").text("请输入或修改角色名");
	}
}

function checkrole(obj){//全选
	if(obj.checked){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.checkAllNodes(true);
		//获取treeids
		var tree=""
		var a = treeObj.getCheckedNodes(true);
		for(var i=0;i<a.length;i++){
			tree+= a[i].menuId+",";
		}
		tree = tree.substring(0,tree.length-1);
		$("#treeids").val(tree);
	}else{
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.checkAllNodes(false);
	}
}

function resttree(){
	purview();
}