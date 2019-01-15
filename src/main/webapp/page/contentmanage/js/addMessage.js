var ues;

$(function(){
    ues=UE.getEditor('content',{
        //autoHeightEnabled: true,
        wordCount:true,
        maximumWords:16000,
    });
    fileshow();//上传图片的展示
    fileEcho();//上传图片的回显
    fileAllShow();//悬浮附件上完全展示
    addMark();//点击添加标签
    addOrUpdateFile();//判断是添加还是修改的图片上传
    commonMark();
    getMark();
    blurEvent($("#abstracts"),$(".text_sensitive_error"));//摘要敏感词
    blurEvent($("#author"),$(".author_sensitive_error"));//作者敏感词
    blurEvent($("#organName"),$(".platform_sensitive_error"));//转载平台敏感词
    blurEvent($("#addMark"),$(".mark_sensitive_error"));//标签敏感词
    otherSpace();//点击空白
//详情页面编译器为不可编译&非详情页面栏目,标签回显
    if($("#informationHidden").val()=="information"){
        ues.ready(function(){
            UE.getEditor('content').setDisabled('fullscreen');//设置编译器为不可编译
        })
    }else{
        $(".colum_list span label").each(function(){
            if( $(this).text().trim() == $("#columsHidden").val().trim()){
                $(this).siblings("input").attr("checked","checked")
            }
        });
        if($("#labelHidden").val()!=""){
            var mark_list='';
            var labelArr = $("#labelHidden").val().split(",");
            for(var i = 0;i<labelArr.length;i++){
                mark_list += ' <span class="hover_item" >' + '<i class="fa fa-trash-o" onclick="deleteMark(this)"></i> '+' <p>' +  labelArr[i] +'</p> '+ '</span> ';
            }
            $("#markList").append(mark_list);
            $("#addMark").val("");
        }
    }
//标题校验
    $("#title").blur(function(){
        if(!$("#titleHidden").val()){
            checkTitle()
        }else{
            checkTitle('update')
        }
    });
//编译器内容校验
    UE.getEditor('content').addListener('blur',function(editor){
        if(ues.getContent()){
            $(".content_error").hide();
            checkSensitive(ues.getContent(),$(".content_sensitive_error"))
        }else{
            $(".content_sensitive_error").hide();
            $(".content_error").show();
        }
    });
    $("#abstracts").on("input propertychange",function(){
        if( $("#abstracts").val().length > 150){
           $(".text_beyond_error").show();
        }else{
            $(".text_beyond_error").hide();
        }
    });
//校验栏目
    $(".colum_list span input[type = radio]").each(function(){
        $(this).click(function(){
           $(".colum_error").hide();
        })
    })

});

//点击空白
function otherSpace(){
    $(document).on("click",function(e){
        var target = $(e.target);
        if(target.closest(".add_mark_list").length == 0){
            $(".add_mark_list").hide();
        }
    });
}
//上传图片的展示
function fileshow(){
    $("#uploadFile").on("change",function(){
        var value = $(this).val();
        value = value.split("\\")[2];
        $("#showFile").val(value);
    });
}
//上传图片的回显
function fileEcho(){
    if($("#imageUrl").val()!=""){
        var imageUrl = $("#imageUrl").val();
        var index = imageUrl.indexOf("-");
        var showFile = imageUrl.substring(index + 1,imageUrl.length);
        $("#showFile").val(showFile);
    }
}
//悬浮附件上完全展示
function fileAllShow(){
    var allFileUrl = $(".all_file_url");
    $("#showFile").on("mouseover",function(){
        if($("#imageUrl").val()!="") {
            allFileUrl.text($("#showFile").val());
            allFileUrl.show();
        }
    }).on("mouseout",function(){
        allFileUrl.hide();
        allFileUrl.text( '')
    });
}
//获取上传图片的宽高
function verificationPicFile(file) {
    var filePath = file.value;
    if(filePath){
        //读取图片数据
        var filePic = file.files[0];
        var reader = new FileReader();
        reader.onload = function (e) {
            var data = e.target.result;
            //加载图片获取图片真实宽度和高度
            var image = new Image();
            image.onload=function(){
                var width = image.width;
                var height = image.height;
                if (width == 500 | height == 500){
                    uploadImage('upload');
                }else {
                    layer.msg("图片大小应为：500*500像素！");
                    file.value = "";
                    $("#showFile").val("");
                    return false;
                }
            };
            image.src= data;
        };
        reader.readAsDataURL(filePic);
    }else{
        return false;
    }
}
//点击添加标签
function addMark(){
    $(".add_label_btn").click(function () {
        blurEvent($("#addMark"),$(".mark_sensitive_error"));
        if($(".mark_sensitive_error").css("display") !="inline"){
            corporateMark();
        }
    });
    $('#addMark').keydown(function(event) {
        if (event.keyCode == 13) {
            if ($('#addMark').val() != '') {
                if ($('#addMark').val().trim() != '') {
                    $.ajax({
                        type: "post",
                        url: "../content/checkForBiddenWord.do",
                        data: {
                            word: $('#addMark').val()
                        },
                        success: function (data) {
                            if (!data) {
                                $(".mark_sensitive_error").hide();
                                corporateMark();
                            } else {
                                $(".mark_sensitive_error").show();
                            }
                        }
                    })
                } else {
                    $(".mark_sensitive_error").hide();
                }
            } else {
                $(".mark_sensitive_error").hide();
            }
        }
    })
}
//生成标签
function corporateMark(){
    var html = "";
    var addMark = $("#addMark").val();
    if(!addMark.trim()){
        layer.alert("请填写标签！")
    }else {
        $.ajax({
            type:'post',
            url:'../content/insertInformationLabel.do',
            data:{
                label:addMark,
            },
            success:function(data){
                var flag = true;
                var markList = [];
                if(data){
                    $(".hover_item p").each(function(){
                        markList.push($(this).text())
                    });
                    for(var i = 0;i<markList.length;i++){
                        if(addMark == markList[i] ){
                            flag = false
                        }
                    }
                    if(flag){
                        html = ' <span class="hover_item" ><i class="fa fa-trash-o" onclick="deleteMark(this)"></i> '+' <p>' + addMark +'</p></span> ';
                        $("#markList").append(html);
                        $("#addMark").val("");
                    }else{
                        $("#addMark").val('');
                    }
                }
            }
        });
    }
}
//点击删除标签
function deleteMark(that){
   $(that).parent("span").remove();
}
//判断是添加还是修改的图片上传
function addOrUpdateFile(){
    var columsh ="";
    columsh=$("#columsh").val();
    if(columsh !=""){
        selectValue("colums",columsh);
    }
    var addupdate =$("#addupdate").val();
    if(addupdate=="add"){
        imageShow("reupload");
    }else if(addupdate=="update"){
        imageShow("upload");
    }
}
//添加/修改/详情的取消
function reset(){
	window.location.href="../content/index.do";
}
//选择上传文件
function fileUpload(){
    $("#uploadFile").click();
}
//上传图片
function uploadImage(statu,flag){
	if(!checkImgType()){ //校验上传文件格式
		return false;
	}
    var options = {
    	url:"../content/uploadImg.do",
    	dataType: 'json',
        type : 'POST',
        success : function(data) {
        	var imageURL=data.url;
        	imageURL=imageURL.replace("\/","\\");
        	$("#imageUrl").val(imageURL);
        	$("#filetext").attr("src",imageURL);
        	imageShow(statu);
        },
        error: function(XmlHttpRequest, textStatus, errorThrown){
           layer.msg("照片上传失败！请检查是否添加照片！或照片是否符合规格！");
        }
    };
    $('#addGoodsForm').ajaxSubmit(options);
    return false;
}
/*验证填写添加内容--liuYong*/
function submission() {
//	 alert("验证结果------"+fieldsCheck());
	if (fieldsCheck()) {
		addMessage();
	}
}

function showMessage(){
    var text =ues.getContent();
	var colums = $("input[name=colum_item]:checked").val();
	var title = $("#title").val();
	var author = $("#author").val();
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	if(text!=''&&text!=undefined&&!re.test(text)
			&&colums!=''
			&&title!=''&&title!=undefined&&!re.test(title)){
		layer.open({
		    type: 2, //page层 1div，2页面
		    area: ['90%', '90%'],
		    title: '资讯内容',
		    moveType: 1, //拖拽风格，0是默认，1是传统拖动
		    content: "showMessage.do",
		    end:function(){
		    }
		}); 
	}else{
		layer.msg("*为必填项！",{icon: 2});
	}
}
//详情页面的发布
function detailPublish(){
    var colum = $("#columsh").val();
    var messageId = $("#messageId").val();
    var params;
    var _json = {
        "id" : messageId,
        "colums":colum,
        "issueState":2
    };
    params = JSON.stringify(_json);
    $.ajax({
        type: "post",
        url: "../content/updateIssue.do",
        data: {
            parameters: params
        },
        traditional: true,
        success: function (data) {
            if (data) {
                window.location.href = "../content/index.do";
            } else {
                layer.msg("发布失败！")
            }

        }
    });
}
//添加页面发布
function messagePublish(){
    var errorCount = 0;
    var channel = $(".channel").val();
    var colum =  $("input[name=colum_item]:checked").val();
    var title=$("#title").val();
    var abstracts=document.getElementById("abstracts").value;
    var content= ues.getContent();
    var imageUrl=$("#imageUrl").val();
    var linkAddress=$("#linkAddress").val();
    var author=$("#author").val();
    var organName=$("#organName").val();
    var addMark = "";
    $(".hover_item p").each(function(){
        addMark += $(this).text()+','
    });
    addMark = addMark.substring(0,addMark.length-1);
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    checkColum();//校验栏目
    checkTitle();//校验标题
    checkCheet();//校验内容
    $(".error_mark ").each(function(){
        if($(this).css("display") == "inline") {
            errorCount=errorCount+1;
        }
    });
    if(errorCount == 0){
        if(colum!=""&&colum!=undefined
            &&title!=""&&title!=undefined&&!re.test(title)
            &&content!=""&&content!=undefined&&!re.test(content)){
            if(linkAddress.length>0 && !IsURL(linkAddress)){
                layer.msg("源文链接格式不正确！",{icon: 2});
            }else{
                $.ajax({
                    type : "post",
                    url : "../content/addMessageJson.do",
                    dataType : "json",
                    data : {
                        "channel":channel,
                        "colums":colum,
                        "title":title,
                        "abstracts":abstracts,
                        "content":content,
                        "imageUrl":imageUrl,
                        "linkAddress":linkAddress,
                        "author":author,
                        "organName":organName,
                        "label":addMark
                    },
                    async: false,
                    beforeSend : function(XMLHttpRequest) {},
                    success : function(data){
                        if(data.code == 200){
                            var params;
                            var _json = {
                                "id" : data.data,
                                "colums":colum,
                                "issueState":2
                            };
                            params = JSON.stringify(_json);
                            $.ajax({
                                type:"post",
                                url:"../content/updateIssue.do",
                                data:{
                                    parameters:params
                                },
                                traditional: true,
                                success:function(data){
                                    if(data){
                                        window.location.href="../content/index.do";
                                    }else{
                                        layer.msg("发布失败！")
                                    }
                                }
                            })
                        }else{
                            layer.msg("添加失败！")
                        }
                    },
                    complete : function(XMLHttpRequest, textStatus) {},
                    error : function(data) {}
                });
            }
        }
    }
}
function addMessage(){
    var errorCount = 0;
    var channel = $(".channel").val();
    var colum =  $("input[name=colum_item]:checked").val();
	var title=$("#title").val();
	var abstracts=document.getElementById("abstracts").value;
	var content= ues.getContent();
	var imageUrl=$("#imageUrl").val();
	var linkAddress=$("#linkAddress").val();
	var author=$("#author").val();
	var organName=$("#organName").val();
	var addMark = "";
    $(".hover_item p").each(function(){
        addMark += $(this).text()+','
	});
    addMark = addMark.substring(0,addMark.length-1);
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
    checkColum();
    checkTitle();
    checkCheet();
    $(".error_mark ").each(function(){
        if($(this).css("display") == "inline") {
            errorCount=errorCount+1;
        }
    });
    if(errorCount == 0){
        if (colum != "" && colum != undefined
            && title != "" && title != undefined && !re.test(title)
            && content != "" && content != undefined && !re.test(content)) {

            if (linkAddress.length > 0 && !IsURL(linkAddress)) {
                layer.msg("源文链接格式不正确！", {icon: 2});
            } else {
                $.ajax({
                    type: "post",
                    url: "../content/addMessageJson.do",
                    dataType: "json",
                    data: {
                        "channel": channel,
                        "colums": colum,
                        "title": title,
                        "abstracts": abstracts,
                        "content": content,
                        "imageUrl": imageUrl,
                        "linkAddress": linkAddress,
                        "author": author,
                        "organName": organName,
                        "label": addMark
                    },
                    async: false,
                    beforeSend: function (XMLHttpRequest) {
                    },
                    success: function (data) {
                        var rspCode = data.code;
                        if (rspCode == 200) {
                            layer.msg("添加成功!", {icon: 1});
                            window.location.href = "../content/index.do";
                        } else {
                            layer.msg("添加失败!", {icon: 2});
                        }
                    },
                    complete: function (XMLHttpRequest, textStatus) {
                    },
                    error: function (data) {

                    }
                });
            }
        }
    }
}
function updatePublish(){
    var errorCount = 0;
    var channel = $(".channel").val();
    var id=$("#messageId").val();
    var colums =  $("input[name=colum_item]:checked").val();
    var title=$("#title").val();
    var abstracts=document.getElementById("abstracts").value;
    var content= ues.getContent();
    var imageUrl=$("#imageUrl").val();
    var linkAddress=$("#linkAddress").val();
    var author=$("#author").val();
    var organName=$("#organName").val();
    var addMark = "";
    $(".hover_item p").each(function(){
        addMark += $(this).text()+','
    });
    addMark = addMark.substring(0,addMark.length-1);
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    $(".error_mark ").each(function(){
        if($(this).css("display") == "inline") {
            errorCount=errorCount+1;
        }
    });
    if(errorCount == 0){
        if($("#issueState").val()==2){
            layer.msg("请先下撤该数据再进行修改",{icon: 2});
        }else{
            if(colums!=""&&colums!=undefined&&!re.test(colums)
                &&title!=""&&title!=undefined&&!re.test(title)
                &&content!=""&&content!=undefined&&!re.test(content)){
                if(linkAddress.length>0 && !IsURL(linkAddress)){
                    layer.msg("源文链接格式不正确！",{icon: 2});
                }else{
                    $.ajax({
                        type : "post",
                        url : "../content/updateMessageJson.do",
                        dataType : "json",
                        data : {
                            "channel":channel,
                            "id":id,
                            "colums":colums,
                            "title":title,
                            "abstracts":abstracts,
                            "content":content,
                            "imageUrl":imageUrl,
                            "linkAddress":linkAddress,
                            "author":author,
                            "organName":organName,
                            "label":addMark
                        },
                        async: false,
                        beforeSend : function(XMLHttpRequest) {},
                        success : function(data){
                            if(data.flag == 'true'){
                                var params;
                                var _json = {
                                    "id" : $("#messageId").val(),
                                    "colums":colums,
                                    "issueState":2
                                };
                                params = JSON.stringify(_json);
                                $.ajax({
                                    type:"post",
                                    url:"../content/updateIssue.do",
                                    data:{
                                        parameters:params
                                    },
                                    traditional: true,
                                    success:function(data){
                                        if(data){
                                            window.location.href="../content/index.do";
                                        }else{
                                            layer.msg("发布失败！")
                                        }
                                    }
                                })
                            }else{
                                layer.msg("添加失败！")
                            }
                        },
                        complete : function(XMLHttpRequest, textStatus) {},
                        error : function(data) {}
                    });
                }
            }
        }
    }
}
function updateMessage(){
    if($("#showFile").val()!=""){
        uploadImage('upload','update');
    }
    var errorCount = 0;
    var channel = $(".channel").val();
    var id=$("#messageId").val();
    var colums =  $("input[name=colum_item]:checked").val();
    var title=$("#title").val();
    var abstracts=document.getElementById("abstracts").value;
    var content= ues.getContent();
    var imageUrl=$("#imageUrl").val();
    var linkAddress=$("#linkAddress").val();
    var author=$("#author").val();
    var organName=$("#organName").val();
    var addMark = "";
    $(".hover_item p").each(function(){
        addMark += $(this).text()+','
    });
    addMark = addMark.substring(0,addMark.length-1);
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    $(".error_mark ").each(function(){
        if($(this).css("display") == "inline") {
            errorCount=errorCount+1;
        }
    });
    if(errorCount == 0){
        if($("#issueState").val()==2){
            layer.msg("请先下撤该数据再进行修改",{icon: 2});
        }else{
            if(colums!=""&&colums!=undefined&&!re.test(colums)
                &&title!=""&&title!=undefined&&!re.test(title)
                &&content!=""&&content!=undefined&&!re.test(content)){
                if(linkAddress.length>0 && !IsURL(linkAddress)){
                    layer.msg("源文链接格式不正确！",{icon: 2});
                }else{
                    $.ajax({
                        type : "post",
                        url : "../content/updateMessageJson.do",
                        dataType : "json",
                        data : {
                            "channel":channel,
                            "id":id,
                            "colums":colums,
                            "title":title,
                            "abstracts":abstracts,
                            "content":content,
                            "imageUrl":imageUrl,
                            "linkAddress":linkAddress,
                            "author":author,
                            "organName":organName,
                            "label":addMark
                        },
                        async: false,
                        beforeSend : function(XMLHttpRequest) {},
                        success : function(data){
                            if(data.flag == 'true'){
                                layer.msg("修改成功!",{icon: 1});
                                window.location.href="../content/index.do";
                            }else{
                                layer.msg("修改失败!",{icon: 2});
                            }
                        },
                        complete : function(XMLHttpRequest, textStatus) {},
                        error : function(data) {

                        }
                    });
                }
            }
        }
    }
}
function selectValue(id,val){
    if(document.getElementById(id)){
        for(var i=0;i<document.getElementById(id).options.length;i++)
        {
            if(document.getElementById(id).options[i].value == val)
            {
                document.getElementById(id).options[i].selected=true;
                break;
            }
        }
    }

}
function imageShow(statu){
	if(statu=="upload"){
		$("#filebutton1").hide();
		$("#uploadFile").show();
		/*$("#filetext").show();
		$("#refile").show();
		$("#fileDiv").show();*/
        $("#filetext").hide();
        $("#refile").hide();
        $("#fileDiv").hide();
	}else if(statu=="reupload"){
		$("#filebutton1").show();
		$("#uploadFile").show();
		$("#filetext").hide();
		$("#refile").hide();
		$("#fileDiv").hide();
	}
}

//----------------------------------检测url地址的js-----------------------------------------
function IsURL(str_url){
	var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
	var re=new RegExp(reg);
	if(re.test(str_url)){
		return (true);
	}else{
		return (false);
	}
}
/* 
 * 判断图片类型 
 *  
 * @param ths  
 *          type="file"的javascript对象 
 * @return true-符合要求,false-不符合 
 */ 
function checkImgType(){
	var file=$("#uploadFile").val();
    if (file == "" || file == null) {
    	layer.msg("请上传图片!",{icon: 2});
        return false;
    } else {
        if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(file)) {
        	layer.msg("图片类型必须是.gif,jpeg,jpg,png中的一种!",{icon: 2});
        	$("#uploadFile").val("");
            return false;
        }
    }
    return true;
}
 
/* 
 * 判断图片大小 
 *  
 * @param ths  
 *          type="file"的javascript对象 
 * @param width 
 *          需要符合的宽  
 * @param height 
 *          需要符合的高 
 * @return true-符合要求,false-不符合 
 */ 

function checkImgPX(ths, width, height) {
	var img = null;
	img = document.createElement("img");
	document.body.insertAdjacentElement("beforeEnd", img); // firefox不行
	img.style.visibility = "hidden";
	img.src = ths.value;
	var imgwidth = img.offsetWidth;
	var imgheight = img.offsetHeight;
	if (imgwidth != width || imgheight != height) {
		alert("图的尺寸应该是" + width + "x" + height);
		ths.value = "";
		return false;
	}
	return true;

}
//获取常用标签
 function commonMark(){
     var mark_html = '';
     var cur;
     var max;
     $.ajax({
         type:"post",
         url:"../content/echoInformationLabel.do",
         success:function (data) {
             if(data){
                 max =data[0].totalNumber;
                 for(var j = 0;j < data.length;j++){
                     cur = data[j].totalNumber;
                     cur > max ? max = cur : max;
                 }
                 for(var i = 0;i<data.length;i++){
                     if(data[i].totalNumber == 0){
                         mark_html += '<span style ="font-size:12px ;cursor: pointer" onclick="selecteMark(this)">' + data[i].label + '</span>'
                     }else{
                         mark_html += '<span style ="font-size:'+(data[i].totalNumber/max)*28+'px ;cursor: pointer" onclick="selecteMark(this)">' + data[i].label + '</span>'
                     }
                 }
                 $(".common_mark").empty();
                 $(".common_mark").append(mark_html);
             }else{
                 $(".mark_common_wrap").hide();
             }

         }
     });
 }
 //模糊查询标签
function getMark(){
    var mark;
    $("#addMark").on("input",function(){
        mark = $(this).val();
       $.ajax({
           type:"post",
           url:"../content/echoInformationLabel.do",
           data:{
               label:mark
           },
           success:function(data){
             if(data.length>0){
                 var mark_list='';
                 for(var i = 0;i<data.length;i++){
                     mark_list += '<li onclick="selecteMark(this)">' + data[i].label + '</li> '
                 }
                 $(".add_mark_list ul").empty();
                 $(".add_mark_list ul").append(mark_list);
                 $(".add_mark_list").show();
             }
           }
       })
    })
}
function selecteMark(that){
      $("#addMark").val($(that).text());
      $("#addMark").focus();
    if( $(that).parent('ul').parent('.add_mark_list').css("display")== "block"){
        $(that).parent('ul').parent('.add_mark_list').hide();
    }
}
//校验pc端栏目
function checkColum(){
        var list= $('input:radio[name="colum_item"]:checked').val();
        if(list==null){
           $(".colum_error").show();
        }else{
            $(".colum_error").hide();
		}
}
//校验标题
function checkTitle(flag){
    if($("#title").val() !=''){
        if(!$("#title").val().trim()){
            $(".title_error").siblings("span").hide();
            $(".title_error").show()
        }else{
            $(".title_error").hide();
            var titleVal = $("#title").val().trim();
            var titleExist = $(".title_exist");
            if(flag == 'update'){
                $.ajax({
                    type:"post",
                    url:"../content/judgeMessageTitle.do",
                    data:{
                        title:titleVal,
                        messageId:$("#messageId").val()
                    },
                    success:function(data){
                        if(!data){
                            titleExist.siblings("span").hide();
                            titleExist.show();
                        }else{
                            titleExist.hide();
                            checkSensitive(titleVal,$(".title_sensitive_error"))
                        }
                    }
                })
            }else{
                $.ajax({
                    type:"post",
                    url:"../content/judgeMessageTitle.do",
                    data:{
                        title:titleVal
                    },
                    success:function(data){
                        if(!data){
                            titleExist.siblings("span").hide();
                            titleExist.show();
                        }else{
                            titleExist.hide();
                            checkSensitive(titleVal,$(".title_sensitive_error"))
                        }
                    }
                })
            }

        }
    }else{
        $(".title_error").siblings("span").hide();
        $(".title_error").show()
    }

}
//校验内容
function checkCheet(){
    var content= ues.getContent();
    if(!content){
        $(".content_sensitive_error").hide();
    	$(".content_error").show();
	}else{
        $(".content_error").hide();
        checkSensitive(content,$(".content_sensitive_error"))
	}
}
//敏感词校验
function checkSensitive(word,obj){
    $.ajax({
        type:"post",
        url:"../content/checkForBiddenWord.do",
        data:{
            word:word
        },
        success:function(data){
            if(!data){
                obj.hide();
            }else{
                obj.siblings("span").hide();
                obj.show();
			}
        }
    })
}
function blurEvent(that,obj){
   that.blur(function(){
       if(that.val() !=''){
           (that.val().trim() !='')?checkSensitive(that.val().trim(),obj): obj.hide();
       }else{
           obj.hide();
       }
   })
}