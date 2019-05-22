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
	    	checktree(ids,data);
	    },  
	});  
}

function checktree(ids,data){
	var number = data.menuNum
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
	$("#checkall").attr("checked",checkNodes.length==number);
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
		    chkboxType: { "Y" : "ps", "N" : "s" }
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

	var zNodes =json.purview;
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
		//zTree_Menu.expandNode(zTree_Menu.getNodes()[0],true);
	});
	
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
//		var number = tree.split(',').length
		var number = json.menuNum
		$("#checkall").prop("checked",beforNum==number);
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
		if(ids.length===0) {
			$("#ruleName").text('请选择角色权限')
			return
		}
		if((ids.indexOf("A11")!=-1||ids.indexOf("A12")!=-1||ids.indexOf("A13")!=-1||ids.indexOf("A141")!=-1)&&ids.indexOf("A142")==-1){
			$("#roleName").text('请选择"添加/移除机构管理员"权限')
			return
		}
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
					if(data.flag===true){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("修改成功");
						window.parent.rolepage(1);
						parent.layer.close(index);						    
					}else if(data.flag===false){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("修改失败");
						parent.layer.close(index);
						}else if(data.flag==="fail") {
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg(data.fail);
						parent.layer.close(index); 
					}
				}
			});
	}else{
		checkrolename($('#rolename').val())
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
		$("#treeids").val('');
	}
}

//取消按钮
function closeWindow() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}