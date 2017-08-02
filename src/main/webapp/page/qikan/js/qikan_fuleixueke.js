function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
var winName = getQueryString("winName");
var numType = getQueryString("numType");
var prociderId = window.parent.window[winName].getprociderId(); // 资源提供商Id
var proResourceType = window.parent.window[winName].getProResourceId();// 资源类型Id
if (numType != 0) {
	$("#genmulu").hide();
}
// 树状结构
$(function() {
	initTree();
});
function initTree() {
	$.ajax({
		type : "post",
		data : {
			"providerId" : prociderId,
			"proResourceId" : proResourceType
		},
		url : "../dataSource/findAllPSubjectCategory.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {
		},
		success : drawTree,
	});
}

function drawTree(data) {
	var setting = {
		view : {
			dblClickExpand : false,
		},
		data : {
			simpleData : {
				enable : true,
			}
		},
		check : {
			enable : false
		},
		callback : {
			onClick : onClick
		},
	};
	var zNodes = eval(data);
	$.fn.zTree.init($("#tree"), setting, zNodes).expandAll(true);
	zTree = $.fn.zTree.getZTreeObj("tree");
	zTree.expandNode(zTree.getNodes()[0], true);// 指定某节点折叠

}
var saveid;
var savename;
function onClick(e, treeId, treeNode) {
	saveid = treeNode.id; // 获取点击 树形 ID
	savename = treeNode.name; // 获取点击 树形 name
}

var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引

function qingling() {
	window.parent.window[winName].$("#parentId").val("");
	window.parent.window[winName].$("#parentName").val("根节点");
	window.parent.window[winName].layer.tips("Ok", "#xuanze", {
		time : 5000
	});
	parent.layer.close(index);
}
function queren() {
	if (saveid == null) {
		window.parent.window[winName].$("#parentId").val("");
		window.parent.window[winName].$("#parentName").val("根目录");
		parent.layer.close(index);

	} else {
		window.parent.window[winName].$("#parentId").val(saveid);
		window.parent.window[winName].$("#parentName").val(savename);
		window.parent.window[winName].layer.tips("Ok", "#xuanze", {
			time : 5000
		});
		parent.layer.close(index);
	}
}
