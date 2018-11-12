$(function(){
	$('#volume_price').blur(function(){
		checkName("price");
		$('#volumePrice').val($(this).val());
	})
})

function jump(obj){
	var ex = document.getElementById("parent");  
	var offsetTop = 0;
	if(obj == 1){
		ex.scrollTop = offsetTop;    
	}else{
		for(var i = 1;i < obj;i++){
			offsetTop += $("#son" + i).height();
		}
		ex.scrollTop = offsetTop + 10;
	}
}
//---------------------------------提交---------------------
function commit(obj){
	if(obj == 1){//分章节
		var chapterArray = new Array();
		$('.chapter').each(function(){
			var cTitle = $(this).next().val();//章的title
			var chapterId = $(this).attr('id');
			var section = $('#son'+chapterId).find('.section').val();
			chapterArray.push({chapterTitle: cTitle, sections: section});
		});
		$('#listContent').val(JSON.stringify(chapterArray));
	}
	
	var id = $('#id').val();
	var volumeName = $('#volumeName').val();
	var volumeAbstract = $('#volumeAbstract').val();
	var subject = $('#subject').val();
	var subjectName = $('#subjectName').val();
	var keyword = $('#keyword').val();
	var volumeState = $('#volumeState').val();
	var coverUrl = $('#coverUrl').val();
	var volumeChapter = $('#volumeChapter').val();
	var listContent = $('#listContent').val();
	var volumePrice = $('#volumePrice').val().trim();
	var flag = checkName("price");
	if(flag){
		
	$.ajax({
		type: 'post',
		url: '../content/commit.do',
		data: {
			'id': id,
			'volumeName': volumeName,
			'volumeAbstract': volumeAbstract,
			'subject': subject,
			'keyword': keyword,
			'volumeState': volumeState,
			'coverUrl': coverUrl,
			'volumeChapter': volumeChapter,
			'listContent': listContent,
			'volumePrice': volumePrice,
			'subjectName' : subjectName,
		},
		dataType:'json',
		success: function(data){
			if(data){
				window.location.href="../content/papercollectquery.do";
			}
		},
	})
	}
}
//文辑名称、简介、关键词js验证
function checkName(text){
	var num = 50;//提示长度
	var nameHtml = '';//提示框
	$('#'+text).next('.sp_price').remove();
	if(text == 'price'){//文辑价格
		var volumePrice = $('#volume_price').val().trim();
		if(volumePrice == null || volumePrice == ''){
			nameHtml = '&nbsp;&nbsp;&nbsp;<span class="sp_price" style="color: #FF0000;">价格不能为空!</span>';
			$('#price').after(nameHtml);
			return false;
		}
	}
	return true;
}
//-----------------------------------------------返回上一步----------------------
function returnThree(){
	if($('#id').val() != ""){
		$('#fromList').attr('action','../content/updateTwo.do');
	}else{
		
		$('#fromList').attr('action','../content/stepThree.do');
	}
	$('#fromList').submit();
}
//------------------------------------------------修改volumeName----------------------------
function update(obj,str){
	$(obj).prev().show();//输入框显示
	$(obj).hide();//修改标签隐藏
	$(obj).prev().prev().hide();//span隐藏
	$(obj).prev().blur(function(){
		$(obj).prev().hide();//输入框隐藏
		$(obj).show();//修改标签显示
		$(obj).prev().prev().html($(obj).prev().val());//将修改后的值付给span标签
		$(obj).prev().prev().show();//显示span标签
		if('volumeName' == str){
			$('#volumeName').val($(obj).prev().val());
		}else if('keyword' == str){
			$('#keyword').val($(obj).prev().val());
		}else if('volumeAbstract' == str){
			$('#volumeAbstract').val($(obj).prev().val());
		}
	});
	
}