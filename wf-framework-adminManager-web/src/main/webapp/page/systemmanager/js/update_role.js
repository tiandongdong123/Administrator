$(function(){
	purview();
	
});
function purview(ids){
	$.ajax({  
	    type : "POST",  
	    url : "../role/getpurview.do", 
	    dataType: "json",
	    success : function(data) {  
	    	purviewtree(data);
	    	checktree(ids);
	    },  
	});  
}

function checktree(ids){
	var treeids = "";
	if(ids==null||ids==''){
		treeids =$("#treeids").val();
	}else{
		treeids=ids;
	}
	var ids=new Array();
	ids = treeids.split(",");
	zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
	for(var i=0;i<ids.length;i++){
		zTree_Menu.checkNode(zTree_Menu.getNodeByParam("menuId",ids[i]), true ); 
	}
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
		$("#treeids").val(tree)
	}
}

function checkrolename(name){
	var cname = $("#srolename").val();
	if(name!=cname){
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
								$("#cname").val("N");
								$("#checkrolename").text("名称重复，请重新输入");
						}else{
								$("#cname").val("Y");
								$("#checkrolename").text("");
						}
					}
				});
		}else{
			$("#checkrolename").text("名字不能为空");
			$("#cname").val("N");
		}
		
	}else{
		$("#cname").val("Y")
	}
}

function doupdaterole(){
	var cname=$("#cname").val();
	if(cname=="Y"){
		var rolename=$("#rolename").val();
		var roledescribe = $("#roledescribe").val();
		var ids = $("#treeids").val();
		var deptname=$("#deptname").find("option:selected").val();
		var roleid = $("#roleid").val();
		$.ajax( {  
			type : "POST",  
			url : "../role/doupdaterole.do",
				data : {
					'roleName' : rolename,
					'description':roledescribe,
					'purview': ids,
					'department':deptname,
					'roleId':roleid
				},
				dataType : "json",
				success : function(data) {
					if(data){
						layer.msg("修改成功");
						window.location.href="../system/roleManager.do";
					}else{
						layer.msg("修改失败");
					}
				}
			});
	}
}

function checkrole(obj){
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
	var ids = $("#treeidstart").val();
	purview(ids);
}