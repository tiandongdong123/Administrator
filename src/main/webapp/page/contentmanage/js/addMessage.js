var ues;

$(function(){
	ues=UE.getEditor('content',{
		//autoHeightEnabled: true,
	});
});

$(function(){
	var  columsh="";
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
});

function reset(){
	window.location.href="../content/addMessage.do";
}

function uploadImage(statu){
	if(!checkImgType()){
		return false;
	}
    var options = {
    	url:"../content/uploadImg.do",
    	dataType: 'json',
        type : 'POST',
        success : function(data) {
        	$("#fileDiv").text("上传成功！");
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

function resetfile(statu){
	imageShow(statu);
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
	var text=ues.getContent();
	var colums=$("#colums").val();
	var title=$("#title").val();
	var author=$("#author").val();
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	if(text!=''&&text!=undefined&&!re.test(text)
			&&colums!=''&&colums!=undefined&&!re.test(colums)
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

function addMessage(){
	var id=$("messageId").val();
	var colums=$("#colums").find("option:selected").val();
	var title=$("#title").val();
	var abstracts=document.getElementById("abstracts").value;
	var content= ues.getContent();
	var imageUrl=$("#imageUrl").val();
	var linkAddress=$("#linkAddress").val();
	var author=$("#author").val();
	var organName=$("#organName").val();
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	if(colums!=""&&colums!=undefined&&!re.test(colums)
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
					"colums":colums,
					"title":title,
					"abstracts":abstracts,
					"content":content,
					"imageUrl":imageUrl,
					"linkAddress":linkAddress,
					"author":author,
					"organName":organName
				},
				async: false,
				beforeSend : function(XMLHttpRequest) {},
				success : function(data){
					var rspCode = data.flag;
			    	if(rspCode == 'true'){
			    		layer.msg("添加成功!",{icon: 1});
			    		window.location.href="../content/message.do";
			    	}else{
			    		layer.msg("添加失败!",{icon: 2});
			    	}
				},
				complete : function(XMLHttpRequest, textStatus) {},
				error : function(data) {alert("添加失败");}
			});
		}
	}else{
		layer.msg("*为必填项！",{icon: 2});
	}
}

function updateMessage(){
	var id=$("#messageId").val();
	var colums=$("#colums").find("option:selected").val();
	var title=$("#title").val();
	var abstracts=document.getElementById("abstracts").value;
	var content= ues.getContent();
	var imageUrl=$("#imageUrl").val();
	var linkAddress=$("#linkAddress").val();
	var author=$("#author").val();
	var organName=$("#organName").val();
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
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
						"id":id,
						"colums":colums,
						"title":title,
						"abstracts":abstracts,
						"content":content,
						"imageUrl":imageUrl,
						"linkAddress":linkAddress,
						"author":author,
						"organName":organName
					},
					async: false,
					beforeSend : function(XMLHttpRequest) {},
					success : function(data){
						if(data.flag == 'true'){
			        		layer.msg("修改成功!",{icon: 1});
			        		window.location.href="../content/message.do";
			        	}else{
			        		layer.msg("修改失败!",{icon: 2});
			        	}
					},
					complete : function(XMLHttpRequest, textStatus) {},
					error : function(data) {alert(data);}
				});
			}
		}
	}
}

function noupdate(){
	window.location.href="../content/message.do";
}

function selectValue(id,val){
	for(var i=0;i<document.getElementById(id).options.length;i++)
    {
        if(document.getElementById(id).options[i].value == val)
        {
            document.getElementById(id).options[i].selected=true;
            break;
        }
    }
}

function imageShow(statu){
	if(statu=="upload"){
		$("#filebutton1").hide();
		$("#uploadFile").hide();
		$("#filetext").show();
		$("#refile").show();
		$("#fileDiv").show();
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
