var curr;
function find(curr) {
	$.ajax({
				type : "post",
				url : "../dataSource/findQueryPersion.do",
				data : {
					userId : $("#inUserId").val()
				},
				dataType : "json",
				async : false,
				success : function(res) {
					$('#textHtml').empty();
					var str = "";
					if (res.person != "" && res.person != null) {
						str += " <tr role='row'>"
								+ "<td><input type='radio' name='radio' onclick='checkPer(this.value)'  value="
								+ res.person.userId + ","
								+ res.person.institution
								+ " />&nbsp;&nbsp;&nbsp;"
								+ "</td>" + "<td>" + res.person.userId
								+ "</td>" + "<td>"
								+ res.person.institution + "</td>"
								+ "</tr>";
						document.getElementById('textHtml').innerHTML = str;
					} else {
						str = "<tr><td colspan='3'><div align='center'><span style='font-size: 20px;'>数据为空!</span></div></td></tr>";
						document.getElementById('textHtml').innerHTML = str;
					}

				}
			});
}
$(function() {
//	find(curr);
});
var institutionId = "";
var institutionName = "";
function checkPer(valuePer) {
	var v = valuePer.split(",");
	institutionId = v[0];
	institutionName = v[1];
}
function queren() {
	var index = parent.layer.getFrameIndex(window.name);
	window.parent.$("#institutionId").val(institutionId);
	window.parent.$("#institutionName").val(institutionId + '/' + institutionName);
	window.parent.findAllProvider(1);
	parent.layer.msg('选择成功');
	parent.layer.close(index);
}