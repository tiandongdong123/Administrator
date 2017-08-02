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
			chkboxType: { "Y": "p", "N": "s" }
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