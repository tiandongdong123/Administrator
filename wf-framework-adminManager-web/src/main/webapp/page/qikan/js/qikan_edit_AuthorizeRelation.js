function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
var winName = getQueryString("winName");
var authorizeRelationId = window.parent.window[winName]
		.getAuthorizeRelationId();
$(function() {
	getAuthorizeRelationOne();
});
function getAuthorizeRelationOne() {
	$.getJSON("../dataSource/getAuthorizeRelationOne.do", {
		id : authorizeRelationId,
	}, function(res) {
		$("#detailsURL").val(res.detailsURL);
		$("#downloadURL").val(res.downloadURL);
	});
}
function editSubmit() {
	$.ajax({
		type : "post",
		url : "../dataSource/updateAuthorizeRelation.do",
		data : {
			id : authorizeRelationId,
			detailsURL : $("#detailsURL").val(),
			downloadURL : $("#downloadURL").val(),
		},
		success : function(data) {
			if (data > 0) {
				var index = parent.layer.getFrameIndex(window.name);
				window.parent.window[winName].getAuthorizeRelationList(1);
				window.parent.window[winName].layer.msg('提交成功！');
				parent.layer.close(index);
			}
		},
	});
}