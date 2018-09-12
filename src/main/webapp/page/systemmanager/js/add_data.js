var zTree_Menu ="";
$(function(){
	/*getSubject();*/
});

function getSubject(){ 
	$.ajax({  
	    type : "POST",  
	    url : "../data/getsubjcet.do", 
	    dataType: "json",  
	    success : function(data) {  
	        if(data.success){  
	        	subjcettree(data.json);   
	        }  
	        else{  
	            layer.msg("获取失败！",{icon: 2});  
	        }  
	    },  
	});  
}   

function subjcettree(json){
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
		},
		
		data: {
			simpleData: {
				idKey:"id",
				pIdKey:"pid",
				enable: true
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
		curMenu = zTree_Menu.getNodes()[0];
	});
	
	function onCheck(){
		var tree=""
		var a = zTree_Menu.getCheckedNodes(true);
		for(var i=0;i<a.length;i++){
			tree+= a[i].id+",";
		}
		tree = tree.substring(0,tree.length-1);
		$("#treeids").val(tree)
	}
}


/**
 * 添加语种
 */
function addlanguage(){
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['40%', '200px'],
	    title: '新增语种',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#languagebox"),
	    btn: ['确认', '取消'],
		yes: function(){
			var languagename=$("#languagename").val();
			var code=$("#languagecode").val();
			checkname(languagename,code,1);
	    },
	}); 
}

/**
 * 添加资源
 */
function addsource(){
	layer.open({
	    type: 1, //page层 1div，2页面
	    area: ['40%', '200px'],
	    title: '新增来源',
	    moveType: 2, //拖拽风格，0是默认，1是传统拖动
	    content: $("#sourcebox"),
	    btn: ['确认', '取消'],
		yes: function(){
			var sourcename=$("#sourcename").val();
			var code=$("#sourcecode").val();
			checkname(sourcename,code,2);
	    },
	}); 
}

function checkDname(name){
	checkname(name,3);
}

function checkname(name,code,id){
	var url = "";
	if(id==1){
		url="../data/checklname.do";
	}else if(id==2){
		url="../data/checksname.do";
	}else{
		url="../data/checkdname.do"
	}
	$.ajax( {  
		type : "POST",  
		url : url,
			data : {
				'name' : name,
				'code' : code
			},
			dataType : "json",
			success : function(data) { 
				if(data){
					if(id==1){
						$("#checklname").text("名称或code重复，请重新输入");
					}else if(id==2){
						$("#checksname").text("名称或code重复，请重新输入");
					}else{
						$("#checkdname").text("名称或code重复，请重新输入");
					}
				}else{
					if(id==1){
						doaddlname(name,code);
					}else if(id==2){
						doaddsname(name,code);
					}else{
						$("#checkdname").text("");
					}
				}
			}
		});
}


function doaddlname(name,code){
	$.ajax( {  
		type : "POST",  
		url : "../data/doaddlname.do",
			data : {
				'name' : name,
				'code' : code
			},
			dataType : "json",
			success : function(data) {
				if(data){
					layer.msg("添加成功",{time:2000});
					layer.closeAll('page');
					$("#language").append("<input type='checkbox'  name='' id=''  value=''>"+name+"&nbsp;&nbsp;&nbsp;");
					$("#checklname").text("");
					$("#languagename").val("");
					$("#languagecode").val("");
				}else{
					layer.msg("添加失败",{time:2000});
					layer.closeAll('page');
				}
			}
		});
}

function doaddsname(name,code){
	$.ajax({  
		type : "POST",  
		url : "../data/doaddsname.do",
		data : {
			'name' : name,
			'code' : code
		},
		dataType : "json",
		success : function(data) {
			if(data){
				layer.msg("添加成功",{time:2000});
				layer.closeAll('page');
				$("#source").append("<input type='checkbox'  name='' id=''  value=''>"+name+"&nbsp;&nbsp;&nbsp;");
				$("#checksname").text("");
				$("#sourcename").val("");
				$("#sourcecode").val("");
			}else{
				layer.msg("添加失败",{time:2000});
				layer.closeAll('page');
			}
		}
	});
}

function addcontent(){
	var contentnum = $(".contenttext");
	var num = contentnum.length+1;
	var html="<div class='form-group input_block' id='d"+num+"' style=\"margin-left: 127px\">" 
			+"<button type=\"button\" class=\"btn btn-primary\" onclick=\"addcontent()\">+</button> "	
			+"<button type=\"button\" class=\"btn btn-primary\" onclick=\"delcontent()\">-</button> "
			+" <input type='text' id='i1_"+num+"' class='contenttext'> " +
			" <select  selected='selected' id='s1_"+num+"'> <option>精确</option> <option>模糊</option></select> " +
			" <input type='text' id='i2_"+num+"'>" +
			" <select selected='selected' id='s2_"+num+"'> <option>与</option> <option>或</option> <option>非</option></select>" +
			"</div>";
	$("#content").append(html);
}
function delcontent(){
	var contentnum = $(".contenttext");
	var num = contentnum.length;
	var rmobj = $("#d"+num);
	rmobj.remove();
}


function doadddata(){
    var urlMatch = /^((ht|f)tps?):\/\/([\w\-]+(\.[\w\-]+)*\/)*[\w\-]+(\.[\w\-]+)*\/?(\?([\w\-\.,@?^=%&:\/~\+#]*)+)?/;
	var tablename=$("#dataname").val();
    // if (tablename == null || tablename == '') {
    //     $("#dataname").focus();
    //     $("#checkdname").text("名称不能为空");
    //     return;
    // }
	var abbreviation = $("#abbreviation").val();
	var datadescribe = $("#datadescribe").val();
	var contentnum = $(".contenttext");
	var languages=""
	var sources = "";
	var resourcetypes="";
	var product_source_code=$("#prouct_source_code").val();
	var customs=new Array();
	$("input[name=languagenames]").each(function(){  
        if($(this).is(':checked')) {  
        	languages+=$(this).val()+",";  
        } 
	});
	$("input[name=sourcenames]").each(function(){  
        if($(this).is(':checked')){  
        	sources+=$(this).val()+",";  
        } 
	});
	$("input[name=resourcetype]").each(function(){  
        if($(this).is(':checked')){  
        	resourcetypes+=$(this).val()+",";  
        } 
	});
	var dbtype=$("#dbtype").find("option:selected").text();
	
	for(var i=0 ;i<contentnum.length+1;i++){
		var i1=$("#i1_"+i).val();
		var i2=$("#i2_"+i).val();
		var s1=$("#s1_"+i).find("option:selected").text();
		var s2=$("#s2_"+i).find("option:selected").text();
		var val = i1+"%"+i2+"%"+s1+"%"+s2;
		customs.push(val);
	}
    var imgLogoSrc=$("#imgLogoSrc").val();
    var link=$("#link").val();
    if(($.trim($('#dataname').val())==='')){
        $('#dataname').next().text('数据库名称不能为空！')
    }else if(($.trim($('#prouct_source_code').val())==='')){
        $('#prouct_source_code').next().text('数据库类型code不能为空！')
    }else if(!urlMatch.test($('#link').val())){
        $('#link').next().text('链接格式不正确！')
    }else if($.trim($('#imgLogoSrc').val())!=''){
        if(!urlMatch.test($('#imgLogoSrc').val())){
            $('#imgLogoSrc').next().text('地址不能为空或链接格式不正确！')
        }else{
            $('#imgLogoSrc').next().text('');
            $.ajax({
                type: "POST",
                url: "../data/doadddata.do",
                data: {
                    'tableName': tablename,
                    'tableDescribe': datadescribe,
                    'abbreviation': abbreviation,
                    'resType': resourcetypes.substring(0, resourcetypes.length - 1),
                    'sourceDb': sources.substring(0, sources.length - 1),
                    'language': languages.substring(0, languages.length - 1),
                    'customs': customs,
                    'productSourceCode': product_source_code,
                    'dbtype': dbtype,
                    'imgLogoSrc': imgLogoSrc,
                    'link': link
                },
                dataType: "json",
                success: function (data) {
                    if (data) {
                        layer.msg("添加成功", {icon: 1});
                        window.location.href = "../system/dataManager.do";
                    } else {
                        layer.msg("添加失败", {icon: 2});
                    }
                }
            });
        }
    }
    else {
        $('#dataname,#product_source_code,#link,#imgLogoSrc').next().text('');
        $.ajax({
            type: "POST",
            url: "../data/doadddata.do",
            data: {
                'tableName': tablename,
                'tableDescribe': datadescribe,
                'abbreviation': abbreviation,
                'resType': resourcetypes.substring(0, resourcetypes.length - 1),
                'sourceDb': sources.substring(0, sources.length - 1),
                'language': languages.substring(0, languages.length - 1),
                'customs': customs,
                'productSourceCode': product_source_code,
                'dbtype': dbtype,
                'imgLogoSrc': imgLogoSrc,
                'link': link
            },
            dataType: "json",
            success: function (data) {
                if (data) {
                    layer.msg("添加成功", {icon: 1});
                    window.location.href = "../system/dataManager.do";
                } else {
                    layer.msg("添加失败", {icon: 2});
                }
            }
        });
    }
}

function checktype(){
	if ($("#checkalltype").is(':checked')) {  
		 $("input[name=resourcetype]").prop("checked",true);
   }else{
   	 $("input[name=resourcetype]").prop("checked",false);
   }
}
function checklanguage(){
	if ($("#checkalllanguage").is(':checked')) {  
		 $("input[name=languagenames]").prop("checked",true);
   }else{
   	 $("input[name=languagenames]").prop("checked",false);
   }
}
function checksource(){
	if ($("#checkallsource").is(':checked')) {  
		 $("input[name=sourcenames]").prop("checked",true);
   }else{
   	 $("input[name=sourcenames]").prop("checked",false);
   }
}

function resettree(){
	getSubject();
}

