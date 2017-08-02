var curr;
$(function() {
	find(curr);
});
/** 系统管理 列表 开始 */
function find(curr) {
	$
			.ajax({
				type : "post",
				url : "../dataSource/findSettingPage.do",
				data : {
					pagenum : curr || 1,// 向服务端传的参数
					pagesize : 10,
					textKey : $("#textKey").val(),
					textValue : $("#textValue").val()
				},
				dataType : "json",
				async : false,
				success : function(res) {
					var str = "";
					if (res.pageRow.length > 0) {
						for ( var i = 0; i < res.pageRow.length; i++) {
							str += " <tr role='row'>"
									+ "<td width='6%' align='center'>"
									+ (i + 1)
									+ "</td>"
									+ "<td>"
									+ res.pageRow[i].settingName
									+ "</td>"
									+ "<td>"
									+ res.pageRow[i].settingKey
									+ "</td>"
									+ "<td>"
									+ res.pageRow[i].settingValue
									+ "</td>"
									+ "<td>"
									+ res.pageRow[i].remark
									+ "</td>"
									+ "<td width='15%'>"
									+ "<button onclick='deleteSetting(this.value)' value="
									+ res.pageRow[i].id
									+ " type='button' class='btn btn-primary btn-sm'>删除</button>&nbsp;"
									+ "<button onclick=settingAddOrEditManager(this.value) value="
									+ res.pageRow[i].id
									+ " type='button' class='btn btn-primary btn-sm'>编辑</button></td>"
									+ "</tr>";
						}
						document.getElementById('textHtml').innerHTML = str;
						// 显示分页
						var totalRow = res.pageTotal;
						var pageSize = res.pageSize;
						var pages;
						var groups;
						if (totalRow % pageSize == 0) {
							pages = totalRow / pageSize;
						} else {
							pages = totalRow / pageSize + 1;
						}
						if (pages >= 4) {
							groups = 4;
						} else {
							groups = pages;
						}
						laypage({
							cont : 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
											// id="page1"></div>
							pages : pages, // 通过后台拿到的总页数
							curr : curr, // 当前页
							skip : true, // 是否开启跳页
							skin : 'molv',// 当前页颜色，可16进制
							groups : groups, // 连续显示分页数
							first : '首页', // 若不显示，设置false即可
							last : '尾页', // 若不显示，设置false即可
							prev : '上一页', // 若不显示，设置false即可
							next : '下一页', // 若不显示，设置false即可
							jump : function(obj, first) { // 触发分页后的回调
								if (!first) { // 点击跳页触发函数自身，并传递当前页：obj.curr
									curr = obj.curr;
									find(curr);
								}
							}
						});
					} else {
						str = "<tr><td colspan='6'><div align='center'><span style='font-size: 20px;'>数据为空!</span></div></td></tr>";
						document.getElementById('textHtml').innerHTML = str;
					}

				}
			});
}
/** 系统管理 列表 结束 */

/** 删除 开始 */
function deleteSetting(id) {
	if (confirm("是否确认删除！！")) {
		$.ajax({
			type : "post",
			url : "../dataSource/deleteSetting.do",
			data : {
				id : id
			},
			dataType : "json",
			async : false,
			success : function(res) {
				if (res > 0) {
					layer.msg('删除成功！');
					find(1);
				}
			}
		});
	}
}
/** 删除 结束 */

/** 跳转到 新增编辑 页面 开始 */
var settingId;
function getSettingId() {
	return settingId;
}
function settingAddOrEditManager(id) {
	settingId = id;
	layer.open({
		type : 2,
		area : [ '600px', '480px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '系统配置操作',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '确认', '关闭' ],
		content : [ "../dataSource/setting-addOrEdit-Manager.do", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.addOrEditSubmit();
		},
		cancel : function(index) {
		}
	});
}
/** 跳转到 新增编辑 页面 结束 */
