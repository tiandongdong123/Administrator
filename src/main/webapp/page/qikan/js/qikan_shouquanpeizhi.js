$(function() {
	$("#lianJieURLPeiZhi").hide();
	$("#xinXiQuRen").hide();
	findAllProvider(); // 资源提供商查询
	//$("#fanxuan").hide();
});

// 下一步按钮
var zhiNum = 0;
function xiayibu() {
	var classStep = $("#classStep").attr("class");
	if (classStep == "pubook_step step01") { // 连接地址配置
//		if ($("#institutionName").val() == "") {
//			layer.msg('请填写用户机构');
//			return;
//		}
//		if (maId == "") {
//			layer.msg('请选择母体文献');
//			return;
//		}
		var class1 = $("#classButten1").attr("type", "button");
		var classStep = $("#classStep").attr("class", "pubook_step step02");
		var class2 = $("#class2").attr("class", "sel");
		$("#shouQuanPeiZhi").hide();
		$("#lianJieURLPeiZhi").show();
		findAuthorize();
		$("#detailsURL").val(providerURL);
		$("#downloadURL").val(providerURL);
		if(providerName == 'CQVIP'){
			
			$("#detailsURL2").val('QK/');
			$("#downloadURL2").val('QK/');
			$('#reference_address1').html("http://www.cqvip.com/QK/86398X/201109/39132057.html");
			$('#reference_address2').html("http://www.cqvip.com/QK/86398X/201109/39132057.html");
		}else if(providerName == 'CNKI'){
			$("#detailsURL2").val('Article/CJFDTotal-');
			$("#downloadURL2").val('Article/CJFDTotal-');
		}
	} else if (classStep == "pubook_step step02") { // 信息确认
		var classStep = $("#classStep").attr("class", "pubook_step step03");
		var class3 = $("#class3").attr("class", "sel");
		$("#classButten2").val("保存");
		$("#lianJieURLPeiZhi").hide();
		$("#xinXiQuRen").show();
		xinxi();
	} else if (classStep == "pubook_step step03") {
		if (zhiNum == 0) {
			addAuthorizeAndRelation();
			zhiNum = 1;
		}
	}
}
function xinxi() {
	var str = "<div class='search link confirm'>" + "<dl>" + "<dt>机构用户：</dt>"
			+ "<dd>" + $("#institutionName").val() + "</dd>" + "</dl>" + "<dl>"
			+ "<dt>资源提供商：</dt>" + "<dd>" + nameZh + "</dd>" + "</dl>"
			+ "<dl>" + "<dt>资源类型：</dt>";
	if (ziType == null || ziType == "") {
		str += "<dd>全部</dd>" + "</dl>" + "<dl>";
	} else {
		str += "<dd>" + $("[id=" + ziType + "]").val() + "</dd>" + "</dl>"
				+ "<dl>";
	}
	if ($("#startDate").val() != "" && $("#endDate").val() != "") {
		str += "<dt>年份：</dt>" + "<dd><span>" + $("#startDate").val()
				+ "年</span> --> <span>" + $("#endDate").val()
				+ "年</span></dd></dl>";
	} else if ($("#startDate").val() != "" && $("#endDate").val() == "") {
		str += "<dt>年份：</dt>" + "<dd><span>" + $("#startDate").val()
				+ "年&nbsp</span>--><span>&nbsp;无</span></dl>";
	} else if ($("#startDate").val() == "" && $("#endDate").val() != "") {
		str += "<dt>年份：</dt>" + "<dd><span>无&nbsp;</span>--><span>&nbsp;"
				+ $("#endDate").val() + "年</span></dl>";
	} else {
		str += "<dt>年份：</dt>"
				+ "<dd><span>无&nbsp;</span>--><span>&nbsp;无</span></dl>";
	}
	var ma = maValue.substring(0, maValue.length - 1);
	var maValues = ma.split(";");
	for ( var i = 0; i < maValues.length; i++) {
		var va = maValues[i].split(",");
		str += "<dl class='subject'>" + "<dt></dt>" + "<dd>" + va[1] + "</dd>"
				+ "</dl>";
	}
	str += "<dl class='Details_page'>" + "<dt>详情页地址：</dt>" + "<dd>http://"
			+ $("#detailsURL").val() + "/" + $("#detailsURL2").val() + "</dd>"
			+ "</dl>" + "<dl class='Details_page'>" + "<dt>原文下载地址：</dt>"
			+ "<dd>http://" + $("#downloadURL").val() + "/"
			+ $("#downloadURL2").val() + "</dd>" + "</dl></div>";
	document.getElementById('xinXiQuRen').innerHTML = str;
}
// 上一步按钮
function shangyibu() {
	var classStep = $("#classStep").attr("class");
	if (classStep == "pubook_step step01") {
		// 不可再操作
	} else if (classStep == "pubook_step step02") { // 授权配置
		var class1 = $("#classButten1").attr("type", "hidden");
		var classStep = $("#classStep").attr("class", "pubook_step step01");
		var class2 = $("#class2").attr("class", "");
		$("#shouQuanPeiZhi").show();
		$("#lianJieURLPeiZhi").hide();
	} else if (classStep == "pubook_step step03") { // 连接地址配置
		var classStep = $("#classStep").attr("class", "pubook_step step02");
		var class3 = $("#class3").attr("class", "");
		$("#classButten2").val("下一步");
		$("#lianJieURLPeiZhi").show();
		$("#xinXiQuRen").hide();
	}
}
/** 资源提供商 查询 */
var providerId;
var providerName;
var providerURL;
var nameZh;
function findAllProvider() {
	$
			.ajax({
				type : "post",
				url : "../dataSource/findAllProviders.do",
				data : {},
				dataType : "json",
				async : false,
				success : function(res) {
					var str = "<dt>资源提供商：</dt><dd>";
					for ( var i = 0; i < res[0].size; i++) {
						if (i == 0) {
							str += "<input type='radio' name='radio' id='' onclick='findResourceNamesById(null,"
									+ res[i].id + ",this.value)' value="
									+ res[i].providerName + ";"
									+ res[i].providerURL
									+ " checked='checked'/><span>"
									+ res[i].nameZh + "</span>";
							providerId = res[i].id;
							providerName = res[i].providerName;
							providerURL = res[i].providerURL;
							nameZh = res[i].nameZh;
						} else {
							str +=  "<input type='radio' name='radio' id='' onclick='findResourceNamesById(null,"
									+ res[i].id + ",this.value)' value="
									+ res[i].providerName + ";"
									+ res[i].providerURL + " /><span>"
									+ res[i].nameZh + "</span>" ;
						}
					}
					document.getElementById('providerHtml').innerHTML = str+ "</dd>";
					findResourceNamesById(null, providerId, providerName + ";"
							+ providerURL);
				}
			});
}
/** 资源提供商 查询 结束 */

var ziType = "";
/** 资源类型 查新开始 */
function findResourceNamesById(tyId, providerIds, values) {
	ziType = tyId;
	maId = "";
	maValue = "";
	$("#yixuanze").html("");
	var values2 = values.split(";");
	providerId = providerIds;
	providerName = values2[0];
	providerURL = values2[1];
	$
			.ajax({
				type : "post",
				url : "../dataSource/findResourceNamesById.do",
				data : {
					providerId : providerId,
				},
				dataType : "json",
				async : false,
				success : function(res) {
					var va2 = providerName + ";" + providerURL;
					var str = "<dt>资源类型：</dt>";
					str += "<dd style='margin-right: 5px;'>"
							+ "<button id='quanbus' onclick=findResourceNamesById(null,"
							+ providerId
							+ ",'"
							+ va2
							+ "') type='button' class='btn btn-block btn-default btn-sm' value='全部'>全部</button>"
							// +"<a onclick='' >全部</a>"
							+ "</dd>";
					if (tyId == null || tyId == "") {
						ziType = "quanbus";
					}
					for ( var i = 0; i < res.length; i++) {
						str += "<dd style='margin-right: 5px;'>"
								+ "<button id="
								+ res[i].id
								+ " onclick=findResourceNamesById("
								+ res[i].id
								+ ","
								+ providerId
								+ ",'"
								+ va2
								+ "') type='button' class='btn btn-block btn-default btn-sm' value="
								+ res[i].resourceName + ">"
								+ res[i].resourceName + "</button>"
								// +"<a href=''
								// onclick='findResourceNamesById("+res[i].id+","+providerId+",'"+va2+"')'
								// >"+res[i].resourceName+"</a>"
								+ "</dd>";
					}
					document.getElementById('ziyuanType').innerHTML = str;
					$("#" + ziType).attr("class", "btn btn-primary btn-sm");
					if (ziType == "quanbus") {
						ziType = "";
					}
					getSubject();
				}
			});

}
/** 资源类型 查询结束 */

/** 母体文献 查询 */
var curr;
var mids;
function findMatrixLiteratureList(curr) {
	$
			.ajax({
				type : "post",
				url : "../dataSource/getMatrixLiteratureList.do",
				data : {
					institutionId : $("#institutionId").val(),
					providerId : providerId,
					psubjectId : classid,
					startDate : $("#startDate").val(),
					endDate : $("#endDate").val(),
					proResourceId : ziType,
					pagesize : 10,
					pagenum : curr || 1,
				},
				dataType : "json",
				async : false,
				success : function(res) {
					var str = "";
					var totalRow;
					var pageSize;
					var pages;
					var groups;
					if (res[0] != null) {
						totalRow = res[0].pageList.pageTotal;
						pageSize = res[0].pageList.pageSize;
						for ( var i = 0; i < res[0].listSize; i++) {
							str += "<li>"
									+ "<div class='LiteraturePic'>"
									+ "<a href='#'> <img src='../page/qikan/images/"
									+ res[i].cover
									+ "'"
									+ "width='150' height='208'> <input type='checkbox' name='check' onclick='fucheck(this.value,0)'  id='"
									+ res[i].id + "' value='" + res[i].id + ","
									+ res[i].categoryName + "：" + res[i].title
									+ "'/>" + "</a>" + "</div>" + "<h4>"
									+ "<a href='#'>" + res[i].title + "</a>"
									+ "</h4></li>";
						}
					} else {
						str = "<div align='center'><span style='font-size: 20px;'>数据为空!</span></div>";
					}
					document.getElementById('mutiwenxian').innerHTML = str;
					morenCheck();
					// 显示分页
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
										// id="page"></div>
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
								findMatrixLiteratureList(curr);
							}
						}
					});
				}
			});
}
/** 母体文献查询 结束 */
// 母体文献 勾选
var maId = "";
var maValue = "";
function fucheck(values,type) {
	if(type=="0"){  // 点击文献复选框 type ==0 取消全选事件
		if(treeNodes!=null){
			treeNodes.checked=false;
			$("span[id="+treeAId+"_check]").attr("class","button chk checkbox_false_full");
		}
	}
	var values2 = values.split(",");
	maId += values2[0] + ",";
	maValue += values + ";";
	if (maId != "") {
		var idss = maId.substring(0, maId.length - 1);
		var ids = idss.split(",");
		var mvalue = maValue.substring(0, maValue.length - 1);
		var mvalues = mvalue.split(";");
		var maId2 = "";
		var maValue2 = "";
		for ( var i = 0; i < ids.length; i++) {
			var n = (maId.split(ids[i])).length - 1;
			if (n % 2 == 0) {
				ids[i] = null;
				mvalues[i] = null;
			} else {
				maId2 += ids[i] + ",";
				maValue2 += mvalues[i] + ";";
			}
		}
		maId = maId2;
		maValue = maValue2;
		morenCheck();
	}
	if (maValue != "") {
		mvalue = maValue.substring(0, maValue.length - 1);
		mvalues = mvalue.split(";");
		var str = "";
		var num2 = "0";
		for ( var i = 0; i < mvalues.length; i++) {
			var va = mvalues[i].split(",");
			str += "<span name='spancheck' id='" + va[0] + "'>" + va[1]
					+ "</span>";
		}
		document.getElementById('yixuanze').innerHTML = str;
	} else {
		document.getElementById('yixuanze').innerHTML = "";
	}
}
function chongzhi() {
	maId = "";
	maValue = "";
	var check = document.getElementsByName("check");
	var len = check.length;
	for ( var i = 0; i < len; i++) {
		if (check[i].checked) {
			$(check[i]).attr("checked", false);
		}
	}
	document.getElementById('yixuanze').innerHTML = "";
}
function morenCheck() {
	if (maId != "") {
		var m2 = maId.substring(0, maId.length - 1);
		var ids = m2.split(",");
		for ( var i = 0; i < ids.length; i++) {
			$("input[id=" + ids[i] + "]").attr("checked", true);
		}
	}
}

/** 添加 （完成按钮） */
function addAuthorizeAndRelation() {
	$.ajax({
				type : "post",
				url : "../dataSource/addAuthorizeAndRelation.do",
				data : {
					institutionId : $("#institutionId").val(),
					providerId : providerId,
					username : $("#username").val(),
					password : $("#password").val(),
					proResourceId : ziType,
					subjectId : classid,
					periodicalIds : maId,
					detailsURL : 'http://' + $("#detailsURL").val() + '/'
							+ $("#detailsURL2").val(),
					downloadURL : 'http://' + $("#downloadURL").val() + '/'
							+ $("#downloadURL2").val(),
				},
				dataType : "json",
				async : false,
				success : function(res) {
					if (res > 0) {
						layer.msg('添加成功');
						setTimeout(
								"javascript:location.href='../dataSource/qikan-jigouList.do'",
								300); // 定时加载
					}
				}
			});
}
function findAuthorize() {
	$.ajax({
		type : "post",
		url : "../dataSource/findAuthorize.do",
		data : {
			institutionId : $("#institutionId").val(),
			providerId : providerId,
		},
		dataType : "json",
		async : false,
		success : function(res) {
			if (res != null && res != "") {
				$("#username").val(res[0].username);
				$("#password").val(res[0].password);
				$("#username").attr("disabled", true);
			} else {
				$("#username").val("");
				$("#password").val("");
			}
		}
	});
}

/** Ztree 销售资源数据库 * */
var classid;
var classname;
function getSubject() {
	classid = null;
	findMatrixLiteratureList(1);
	$.ajax({
		type : "post",
		data : {
			providerId : providerId,
			proResourceId : ziType
		},
		url : "../dataSource/findAllPSubjectCategory.do",
		dataType : "json",
		beforeSend : function(XMLHttpRequest) {
		},
		success : drawTree,
	});
}
function drawTree(json) {
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
			enable : true,
			chkboxType: {"Y":"", "N":""}
		},
		callback : {
			onCheck: onCheck,
			onClick : onClick
		},
	};
	var zNodes = eval(json);
	$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(true);
	zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.expandNode(zTree.getNodes()[0], true);
}
var treeAId;
var checkedTree; //选中状态
var treeNodes;
function onCheck(e, treeId, treeNode) {
	$("a[title="+classname+"]").removeClass("curSelectedNode");
	treeNodes=treeNode;
	classid = treeNode.id;
	classname = treeNode.name;
	checkedTree=treeNode.checked;
	$("a[title="+classname+"]").addClass("curSelectedNode");
	treeAId =$("a[title="+classname+"]").attr("id");
	treeAId=treeAId.substring(0, treeAId.length-2);   //获取复选框 id 编号
	quanXuan(classid);
}
function onClick(event, treeId, treeNode) {
	$("a[title="+classname+"]").removeClass("curSelectedNode");
	classid = treeNode.id;
	classname = treeNode.name;
	$("a[title="+classname+"]").addClass("curSelectedNode");
	treeNodes=treeNode;
	checkedTree=treeNode.checked;
	treeAId =$("a[title="+classname+"]").attr("id");
	treeAId=treeAId.substring(0, treeAId.length-2);   //获取复选框 id 编号
	findMatrixLiteratureList(1);
}
function SuQuanXuan(){ //全选 树形Ztree
	SuQuXiao();
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.checkAllNodes(true);
	nodes = treeObj.getCheckedNodes(true),
	//alert(JSON.stringify(nodes));
	forXunhuan(nodes);
	//$("#quanxuan").hide();
	//$("#fanxuan").show();
}
function SuQuXiao(){   //反选 树形Ztree
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.checkAllNodes(false);
	maId = "";
	maValue = "";
	classid="";
	classname="";
	chongzhi();
	//$("#quanxuan").show();
	//$("#fanxuan").hide();
}
function forXunhuan(nodes){
	for ( var i = 0; i < nodes.length; i++) {
		classname=nodes[i].name;
		quanXuan(nodes[i].id);
	}
}

//调用 全选 某类型 事件
function quanXuan(suId){
	$
	.ajax({
		type : "post",
		url : "../dataSource/getMatrixLiteratureListIds.do",
		data : {
			subjId : suId,
			providerId : providerId,
			maId,maId,
			checkedTree,checkedTree,
			startDate : $("#startDate").val(),
			endDate : $("#endDate").val(),
		},
		dataType : "json",
		async : false,
		success : function(res) {
			var str="";
			for ( var i = 0; i < res.length; i++) {
				str =res[i].id+","+classname+":"+res[i].title;
				fucheck(str,"1");
			}
			findMatrixLiteratureList(1);
		}
	});
}

/** Ztree 销售资源数据库 结束* */

/** 跳转 机构查询页面 开始 */
function qinkanfindPerson() {
	layer.open({
		type : 2,
		area : [ '800px', '593px' ],
		scrollbar : false,
		shadeClose : true,
		fix : false, // 不固定
		title : '机构查询',
		// maxmin : true,
		skin : 'layui-layer-rim', // 加上边框
		btn : [ '确认', '关闭' ],
		content : [ "../dataSource/qikan-findPerson.do", 'no' ],
		yes : function(index, layer) {
			var iframeWin = parent.window[layer.find('iframe')[0]['name']];
			iframeWin.queren();
			$("#username").attr("disabled", false);
			$("#password").attr("disabled", false);
		},
		cancel : function(index) {
		}
	});

}
/** 跳转 机构查询页面 结束 */
