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
		zTree_Menu.checkNode(zTree_Menu.getNodeByParam("id",ids[i]), true ); 
	}
	
	var nodes = zTree_Menu.getNodes();
	var checknodesNum=0;
	var checkNodes=zTree_Menu.getCheckedNodes(true);
	for(var i=0;i<checkNodes.length;i++){
	   if(checkNodes[i].level==0){
		   checknodesNum++;
	   }
	} 	
	$("#checkall").attr("checked",checknodesNum==nodes.length);
}

function purviewtree(json){
	var curMenu = null, zTree_Menu = null;
	var setting = {
		view: {
			showLine: true,
			selectedMulti: false,
			dblClickExpand: false,
			showTitle: false
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
		    chkboxType: { "Y" : "ps", "N" : "ps" }
		},
		
		data: {
			simpleData: {
				idKey:"id",
				pIdKey:"pid",
				enable: true
			},
			key: {
				name: "name"
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
		var tree="";
		var a = zTree_Menu.getCheckedNodes(true);
		for(var i=0;i<a.length;i++){
			tree+= a[i].id+",";
		}
		tree = tree.substring(0,tree.length-1);
		$("#treeids").val(tree);
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
			$("#checkrolename").text("请填写角色名称");
			$("#cname").val("N");
		}
		
	}else{
		$("#cname").val("Y");
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
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("修改成功",{icon: 1});
						setTimeout(() => {
							parent.location.reload(index);
					    }, 500);
						parent.layer.close(index);						    
					}else{
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("修改失败",{icon: 2});
						setTimeout(() => {
							parent.location.reload(index);
					    }, 500);
						parent.layer.close(index);
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
	}
}

//取消按钮
function closeWindow() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}