var cp_num = 1;
$(function(){
	//---------------------------判断是返回上一步还是下一步进的页面-----------------
	var listContent = eval($('#listContent').val());
	if(listContent != null){//点击返回上一步进的页面
		if($('#volumeChapter').val() == 1){//分章节
			$('#cp_1').remove();
			$('#chapter_1').remove();
			for(var i = 0;i < listContent.length;i++){
				var chapterTitle = listContent[i].chapterTitle;
				var sections = eval(listContent[i].sections);
				//-------------------------------添加章div------------------
				var cpHtml ='<div id="cp_'+(i+1)+'" class="cur" name="div_cp" onclick="showCp(this);">第'+(i+1)+'章<span><i class="icon icon_close" onclick="deleteCp(this,event);"></i></span></div>';
				$('#cp_add').before(cpHtml);//在添加按钮前面添加新的div
				var content='<div id="chapter_'+(i+1)+'" class="cp_content" style="display:none;">'
					+'<div class="chapter_btn" style="display:none;">'
					+'<input type="text" placeholder="请输入章节名称" />'
					+'<input type="button" onclick="save(this);" value="保存" class="btn_base">'
					+'</div>'
					+'<div class="Chapter_One" style="display:black;">'+chapterTitle+'<a href="javascript:void(0);" onclick="updateTitle(this);"><i class="icon icon_edit"></i></a></div>'
					+'<div id="selected_'+(i+1)+'" style="display:none;">'
					+'<div class="create_left_title create_check">已选文献 12条</div>'
					+'<table width="100%" border="0" cellspacing="1" cellpadding="0" class="TableL JournalTableL">'
					+'<tr>'
					+'<th align="left" class="title JouColumn AnnalsNum" scope="col">'
					+'<div>序号</div>'
					+'</th>'
					+'<th align="left" class="title JouColumn" scope="col">'
					+'<div>标题 </div>'
					+'</th>'
					+'<th align="left" class="title JouColumn Operate" scope="col">'
					+'<div>操作</div>'
					+'</th>'
					+'</tr>'
					+'</table>'
					+'<div class="TableF">'
					+'<table width="97%" border="0" cellspacing="1" cellpadding="0" class="TableL JournalTableL" >'
					+'<tbody id="list_'+(i+1)+'">'
					+'</tbody>'
					+'</table>'
					+'</div>'
					+'</div>'
					+'</div>';
				$('.create_left').append(content);//添加章的同时添加章节相应的div
				$('.cp_content').hide();
				for(var j = 0;j < sections.length;j++){
					//-------------------------------添加节div------------------
					var docuid = sections[j].docuId;
					var randomid = sections[j].randomId;
					var title = sections[j].docuTitle;
					var author = sections[j].docuAuthor;
					var docutype = sections[j].docuType;
					var classtype = sections[j].classType;
					var l= $('#list_'+(i+1)+' tr').length;//总行数
					var listHtml='<tr>'
						+'<td align="left" valign="top" class="JouColumn Rank">'
						+'<div>'+(l+1)+'</div>'
						+'</td>'
						+'<td align="left" valign="top" class="JouColumn author">'
						+'<div class="Section">'
						+'<a href="#">'+title+'</a>'
						+'<input type="text" hidden="true" name="docuid" id="docuid" value="'+docuid+'">'
						+'<input type="text" hidden="true" name="randomid" id="randomid" value="'+randomid+'">'
						+'<input type="text" hidden="true" name="author" id="author" value="'+author+'">'
						+'<input type="text" hidden="true" name="docutype" id="docutype" value="'+docutype+'">'
						+'<input type="text" hidden="true" name="classtype" id="classtype" value="'+classtype+'">'
						+'</div>'
						+'</td>'
						+'<td align="left" valign="top" class="JouColumn author">'
						+'<div class="sort_operate">'
						+'<a href="javascript:void(0);" onclick="upSection(this);"><img src="../images/sort_up.png" alt="" /></a>'
						+'<a href="javascript:void(0);" onclick="downSection(this);"><img src="../images/sort_down.png" alt="" /></a>'
						+'<a href="javascript:void(0);" onclick="deleteSection(this,1);"><img src="../images/close_1.png" alt="" /></a>'
						+'</div>'
						+'</td>'
						+'</tr>';
					$('#list_'+(i+1)).append(listHtml);
				}
				$('#selected_'+(i+1)).find('.create_check').html('已选文献 '+sections.length+'条');
			}
			$('#chapter_1').show();//显示新添加的div
			$('#selected_1').show();
			cp_num = listContent.length;
		}else{//不分章节
			var sections = eval(listContent);
			for(var j = 0;j < sections.length;j++){
				//-------------------------------添加节div------------------
				var docuid = sections[j].docuId;
				var randomid = sections[j].randomId;
				var title = sections[j].docuTitle;
				var author = sections[j].docuAuthor;
				var docutype = sections[j].docuType;
				var classtype = sections[j].classType;
				var l= $('#list tr').length;//总行数
				var listHtml='<tr>'
					+'<td align="left" valign="top" class="JouColumn Rank">'
					+'<div>'+(l+1)+'</div>'
					+'</td>'
					+'<td align="left" valign="top" class="JouColumn author">'
					+'<div class="Section">'
					+'<a href="#">'+title+'</a>'
					+'<input type="text" hidden="true" name="docuid" id="docuid" value="'+docuid+'">'
					+'<input type="text" hidden="true" name="randomid" id="randomid" value="'+randomid+'">'
					+'<input type="text" hidden="true" name="author" id="author" value="'+author+'">'
					+'<input type="text" hidden="true" name="docutype" id="docutype" value="'+docutype+'">'
					+'<input type="text" hidden="true" name="classtype" id="classtype" value="'+classtype+'">'
					+'</div>'
					+'</td>'
					+'<td align="left" valign="top" class="JouColumn author">'
					+'<div class="sort_operate">'
					+'<a href="javascript:void(0);" onclick="upSection(this);"><img src="../images/sort_up.png" alt="" /></a>'
					+'<a href="javascript:void(0);" onclick="downSection(this);"><img src="../images/sort_down.png" alt="" /></a>'
					+'<a href="javascript:void(0);" onclick="deleteSection(this,2);"><img src="../images/close_1.png" alt="" /></a>'
					+'</div>'
					+'</td>'
					+'</tr>';
				$('#list').append(listHtml);
			}
			$('#nochapter').show();
			$('#nochapter').find('.create_check').html('已选文献 '+sections.length+'条');
		}
		
	}
	
	$('#wb_all_global').click(function(){//全站全选
		if($('#wb_all_global').prop('checked')){
			$('input[name="global_docu"]').prop('checked',true);
		}else{
			$('input[name="global_docu"]').prop('checked',false);
		}
	});
	$(":radio").click(function(){
		GlobalPage(1);
	});
	
})
//----------------返回上一步-------------------
function returnTwo(){
	$('#fromList').attr('action','../content/updateOne.do');
	$('#fromList').submit();
}

//---------------搜全站--------------------
function GlobalPage(curr){
	var queryWords = $('#input_keywords_global').val()==null?"":$('#input_keywords_global').val();
	var sort = $('input[name="sort"]:checked').val();
	var pageSize = 12;
	$('#wb_all_global').removeAttr('checked');
	$.ajax({
		type: 'post',
		url: '../content/queryGlobal.do',
		data:{'queryWords':queryWords,
			  'sort' : sort,
			  'pageNum':curr || 1,
			  'pageSize':pageSize
			  },
		dataType:'json',
		success: function(data){
			$('#Inquire_global').empty();//清空
			$('#totalRow_global').empty();
			$('#totalRow_global').append('共'+data.totalRow+'条');
			if(data.totalRow > 0){
				$.each(data.pageRow,function (i) {
					var docuId;
					var docuType;
					if("perio_artical" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].article_id;
						docuType = 'perio';
					}else if("degree_artical" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].article_id;
						docuType = 'degree';
					}else if("conf_artical" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].article_id;
						docuType = 'conference';
					}else if("ebooks" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].ebook_id;
						docuType = 'ebooks';
					}else if("reference_book" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].ref_b_id;
						docuType = 'ebooks';
					}else if("patent_element" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].patent_id;
						docuType = 'patent';
					}else if("tech_report" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].report_id;
						docuType = 'tech';
					}else if("tech_result" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].result_id;
						docuType = 'techResult';
					}else if("standards" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].stand_id;
						docuType = 'standards';
					}else if("legislations" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].legis_id;
						docuType = 'legislations';
					}else if("yearbook_single" == data.pageRow[i].class_type){
						docuId = data.pageRow[i].single_id;
						docuType = 'yearbook';
					}
					var html ='<div class="condition_info">'
						+'<input id="'+docuId+'" name="global_docu" value=\''+data.pageRow[i].title+'\' type="checkbox"' 
						+'randomId="'+data.pageRow[i].random_id+'"'
						+'author="'+(data.pageRow[i].authors_name==undefined?"":data.pageRow[i].authors_name)+'"'
						+'docuType="'+docuType+'"'
						+'classType="'+data.pageRow[i].class_type+'"'
						+'onclick="global_docuClick();"/>'+ data.pageRow[i].title
						+'</div>';
					$('#Inquire_global').append(html);
				});
			}else{
				$('#Inquire_global').append("暂无数据");
			}
			laypage(
		            {
		                cont: 'divPager_global',
		                pages: Math.ceil(data.totalRow / pageSize),	//总页数
		                curr: curr || 1,
		                skip: false,
		                skin: 'molv',
		                groups: 0,
		                first: false,
		                last: false,
		                jump: function (obj, first) {
		                    if(!first){
		                    	GlobalPage(obj.curr);
		                    }
		                }
		            });
		},
	})
}
//------------------------------------------------------分章节---------------------------
///--------------------------------添加章---------------------
function addChapter(){
	cp_num++;//章节的标识
	var cpHtml ='<div id="cp_'+cp_num+'" class="cur" name="div_cp" onclick="showCp(this);">第'+cp_num+'章<span><i class="icon icon_close" onclick="deleteCp(this,event);"></i></span></div>';
	$('#cp_add').before(cpHtml);//在添加按钮前面添加新的div
	$('#cp_'+cp_num).siblings().removeClass('cur');//新添加的显示选中状态
	var content='<div id="chapter_'+cp_num+'" class="cp_content" style="display:black;">'
		+'<div class="chapter_btn" >'
		+'<input type="text" placeholder="请输入章节名称" />'
		+'<input type="button" onclick="save(this);" value="保存" class="btn_base">'
		+'</div>'
		+'<div class="Chapter_One" style="display:none;">请添加章描述概述  <a href="javascript:void(0);" onclick="updateTitle(this);"><i class="icon icon_edit"></i></a></div>'
		+'<div id="selected_'+cp_num+'" style="display:none;">'
		+'<div class="create_left_title create_check">已选文献 12条</div>'
		+'<table width="100%" border="0" cellspacing="1" cellpadding="0" class="TableL JournalTableL">'
		+'<tr>'
		+'<th align="left" class="title JouColumn AnnalsNum" scope="col">'
		+'<div>序号</div>'
		+'</th>'
		+'<th align="left" class="title JouColumn" scope="col">'
		+'<div>标题 </div>'
		+'</th>'
		+'<th align="left" class="title JouColumn Operate" scope="col">'
		+'<div>操作</div>'
		+'</th>'
		+'</tr>'
		+'</table>'
		+'<div class="TableF">'
		+'<table width="97%" border="0" cellspacing="1" cellpadding="0" class="TableL JournalTableL" >'
		+'<tbody id="list_'+cp_num+'">'
		+'</tbody>'
		+'</table>'
		+'</div>'
		+'</div>'
		+'</div>';
	$('.create_left').append(content);//添加章的同时添加章节相应的div
	$('.cp_content').hide();
	$('#chapter_'+cp_num).show();//显示新添加的div
}
///--------------------------------删除章---------------------
function deleteCp(obj,event){
	event.stopPropagation();
	$(obj).parent().parent().remove();//删除章div
	var id = $(obj).parent().parent().attr('id').replace('cp', 'chapter');//获取章节内容id
	$('#'+id).remove();//删除章节内容div
	cp_num = 1;
	$('div[name="div_cp"]').each(function(){
		var old_id = $(this).attr('id');//获取删除之前的id
		$(this).attr('id','cp_'+cp_num);//删除之后重新给id赋值
		$(this).html('第'+cp_num+'章<span><i class="icon icon_close" onclick="deleteCp(this,event);"></i></span>');//删除之后重新赋值
		$('#'+old_id.replace('cp', 'chapter')).attr('id','chapter_'+cp_num);//给章节相应的div的id重新赋值
		$('#'+old_id.replace('cp', 'selected')).attr('id','selected_'+cp_num);
		$('#'+old_id.replace('cp', 'list')).attr('id','list_'+cp_num);
		cp_num++;
	});
	cp_num--;
}
//------------------------------章节的显示与隐藏---------------------
function showCp(obj){
	var id = $(obj).attr('id');//获取删除之前的id
	$(obj).attr('class','cur');
	$(obj).siblings().removeClass('cur');
	$('.cp_content').hide();
	$('#'+$(obj).attr('id').replace('cp', 'chapter')).show();//显示章节相应的div
	var l= $('#'+$(obj).attr('id').replace('cp', 'list')+' tr').length;//总行数 
	if(l>0){
		$('#'+$(obj).attr('id').replace('cp', 'selected')).show();
	}
}
//-------------------------------点击保存按钮章显示-----------------
function save(obj){
	var cpNum = $(obj).parent().parent().attr('id');//获取第几章
	var chapterTitle = $.trim($(obj).siblings().val());
	if(chapterTitle == "" || chapterTitle == null){
		layer.alert("章节名称不能为空！");
		return false;
	}else if(chapterTitle.length > 50){
		layer.alert("长度不能超过50！");
		return false;
	}
	$(obj).parent().hide();
	$(obj).parent().next().html(cpNum.replace('chapter_', '第')+'章：'+chapterTitle+'<a <a href="javascript:void(0);" onclick="updateTitle(this);"><i class="icon icon_edit"></i></a>');
	$(obj).parent().next().show();
}
//-------------------------------添加到章节----------------------
function addSection(obj,name){
	if(obj == 1){//分章节
		var selectId = $('.cur').attr('id').replace('cp', 'selected');
		var listId = $('.cur').attr('id').replace('cp', 'list');
		$('#'+selectId).show();
		$('input[name="'+name+'"]:checked').each(function(){
			var docuid = $(this).attr('id');
			var obj = document.getElementById(docuid);
			var title = $(this).val();
			var randomid = obj.getAttribute("randomId");
			var author = obj.getAttribute("author");
			var docutype = obj.getAttribute("docuType");
			var classtype = obj.getAttribute("classType");
			var l= $('#'+listId+' tr').length;//总行数
			var listHtml='<tr>'
				+'<td align="left" valign="top" class="JouColumn Rank">'
				+'<div>'+(l+1)+'</div>'
				+'</td>'
				+'<td align="left" valign="top" class="JouColumn author">'
				+'<div class="Section">'
				+'<a href="#">'+title+'</a>'
				+'<input type="text" hidden="true" name="docuid" id="docuid" value="'+docuid+'">'
				+'<input type="text" hidden="true" name="randomid" id="randomid" value="'+randomid+'">'
				+'<input type="text" hidden="true" name="author" id="author" value="'+author+'">'
				+'<input type="text" hidden="true" name="docutype" id="docutype" value="'+docutype+'">'
				+'<input type="text" hidden="true" name="classtype" id="classtype" value="'+classtype+'">'
				+'</div>'
				+'</td>'
				+'<td align="left" valign="top" class="JouColumn author">'
				+'<div class="sort_operate">'
				+'<a href="javascript:void(0);" onclick="upSection(this);"><img src="../images/sort_up.png" alt="" /></a>'
				+'<a href="javascript:void(0);" onclick="downSection(this);"><img src="../images/sort_down.png" alt="" /></a>'
				+'<a href="javascript:void(0);" onclick="deleteSection(this,1);"><img src="../images/close_1.png" alt="" /></a>'
				+'</div>'
				+'</td>'
				+'</tr>';
			$('#'+listId).append(listHtml);
		});
		var number = $('#'+selectId).find("tbody tr").length-1;
		$('#'+selectId).find('.create_check').html('已选文献 '+number+'条');
//		$('input[name="'+name+'"]:checked').prop('checked',false);
//		$('#wb_all').prop('checked',false);
	}else{//不分章节
		$('#nochapter').show();
		$('input[name="'+name+'"]:checked').each(function(){
			var docuid = $(this).attr('id');
			var obj = document.getElementById(docuid);
			var title = $(this).val();
			var randomid = obj.getAttribute("randomId");
			var author = obj.getAttribute("author");
			var docutype = obj.getAttribute("docuType");
			var classtype = obj.getAttribute("classType");
			var l= $('#list tr').length;//总行数
			var listHtml='<tr>'
				+'<td align="left" valign="top" class="JouColumn Rank">'
				+'<div>'+(l+1)+'</div>'
				+'</td>'
				+'<td align="left" valign="top" class="JouColumn author">'
				+'<div class="Section">'
				+'<a href="#">'+title+'</a>'
				+'<input type="text" hidden="true" name="docuid" id="docuid" value="'+docuid+'">'
				+'<input type="text" hidden="true" name="randomid" id="randomid" value="'+randomid+'">'
				+'<input type="text" hidden="true" name="author" id="author" value="'+author+'">'
				+'<input type="text" hidden="true" name="docutype" id="docutype" value="'+docutype+'">'
				+'<input type="text" hidden="true" name="classtype" id="classtype" value="'+classtype+'">'
				+'</div>'
				+'</td>'
				+'<td align="left" valign="top" class="JouColumn author">'
				+'<div class="sort_operate">'
				+'<a href="javascript:void(0);" onclick="upSection(this);"><img src="../images/sort_up.png" alt="" /></a>'
				+'<a href="javascript:void(0);" onclick="downSection(this);"><img src="../images/sort_down.png" alt="" /></a>'
				+'<a href="javascript:void(0);" onclick="deleteSection(this,2);"><img src="../images/close_1.png" alt="" /></a>'
				+'</div>'
				+'</td>'
				+'</tr>';
			$('#list').append(listHtml);
		});
		var number = $("#list tr").length;
		$('#nochapter').find('.create_check').html('已选文献 '+number+'条');
//		$('input[name="'+name+'"]:checked').prop('checked',false);
//		$('#wb_all').prop('checked',false);
	}
	
}
//------------------------上移---------------------
function upSection(obj){
	var i=$(obj).parent().parent().parent().index();//当前行的id
    if(i > 0){//不是表头，也不是第一行，则可以上移
      var tem0=$(obj).parent().parent().parent().html();
      var tem1=$(obj).parent().parent().parent().prev().html();
      $(obj).parent().parent().parent().prev().html(tem0);
      $(obj).parent().parent().parent().html(tem1);
    }
}
//------------------------下移---------------------
function downSection(obj){
	var l;
	 if($('#volumeChapter').val() == 1){//分章节
	 	
		 var listId = $('.cur').attr('id').replace('cp', 'list');
		 l= $('#'+listId+' tr').length;//总行数
	 }else{
		 l= $('#list tr').length;//总行数
	 }
	 var i=$(obj).parent().parent().index();//当前行的id
	 if(i!=l){//不是最后一行，则可以下移
	   var tem0=$(obj).parent().parent().parent().html();
       var tem1=$(obj).parent().parent().parent().next().html();
       $(obj).parent().parent().parent().next().html(tem0);
       $(obj).parent().parent().parent().html(tem1);
	 }
}
//------------------------删除---------------------
function deleteSection(obj,num){
	$(obj).parent().parent().parent().remove();
	if(num == 1){//分章节
		var selectId = $('.cur').attr('id').replace('cp', 'selected');
		var number = $('#'+selectId).find("tbody tr").length-1;
		$('#'+selectId).find('.create_check').html('已选文献 '+number+'条');
	}else{
		
		var number = $("#list tr").length;
		$('#nochapter').find('.create_check').html('已选文献 '+number+'条');
	}
}
//-----------------------修改章标题---------------------
function updateTitle(obj){
	var old_title = $(obj).parent().text();
	var w=old_title.indexOf("：");
	$(obj).parent().hide();
	$(obj).parent().prev().find('input[type="text"]').val(old_title.substring(w+1));
	$(obj).parent().prev().show();
}
//------------------------下一步----------------------
function next(obj){
	if(obj == 1){//分章节
		var chapterArray = new Array();
		$('div[name="div_cp"]').each(function(){
			var old_id = $(this).attr('id');
			var selectId = old_id.replace('cp', 'selected');
			var listId = old_id.replace('cp', 'list');
			var cTitle = $('#'+selectId).prev().text();//章的title
			var sectionArray = new Array();
			$('#'+listId).find('.Section').each(function(){//节
				var sId = $(this).find('#docuid').val();
				var sRandomid = $(this).find('#randomid').val();
				var sTitle = $(this).find('a').text();
				var sAuthor = $(this).find('#author').val();
				var sDocutype = $(this).find('#docutype').val();
				var sClasstype = $(this).find('#classtype').val();
				sectionArray.push({docuId: sId, docuTitle: sTitle, docuAuthor: sAuthor, docuType: sDocutype, classType: sClasstype, randomId:sRandomid});
			});
			chapterArray.push({chapterTitle: cTitle, sections: JSON.stringify(sectionArray)});
		});
		$('#listContent').val(JSON.stringify(chapterArray));
		$('#fromList').attr('action','../content/stepFourChapter.do');
		$('#fromList').submit();
	}else{//不分章节
		var sectionArray = new Array();
		$('#list').find('.Section').each(function(){//节
			var sId = $(this).find('#docuid').val();
			var sRandomid = $(this).find('#randomid').val();
			var sTitle = $(this).find('a').text();
			var sAuthor = $(this).find('#author').val();
			var sDocutype = $(this).find('#docutype').val();
			var sClasstype = $(this).find('#classtype').val();
			sectionArray.push({docuId: sId, docuTitle: sTitle, docuAuthor: sAuthor, docuType: sDocutype, classType: sClasstype, randomId:sRandomid});
		});
		$('#listContent').val(JSON.stringify(sectionArray));
		$('#fromList').attr('action','../content/stepFourNoChapter.do');
		$('#fromList').submit();
	}
	
}
function global_docuClick(){
	var num1 = $('input[name=global_docu]').length;
	var num2 = $('input[name=global_docu]:checked').length;
	if(num1 == num2){//全部选中
		$('#wb_all_global').prop('checked',true);
	}else{
		$('#wb_all_global').removeAttr('checked');
	}
}








