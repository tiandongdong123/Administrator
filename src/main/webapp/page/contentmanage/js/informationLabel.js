$(function () {
    //单条删除标签
    $('#labelInfo').on('click','.delete_label',function () {
        var _this = $(this);
        var parentEle = _this.parents('.queryData');
        var labelDataEel = parentEle.find('.label_data');
        var labelArr = [];
        labelArr.push(labelDataEel.attr('data-id'))
        layer.alert('确定删除该数据吗？',{
            icon: 1,
            skin: 'layui-layer-molv',
            btn: ['确定','取消'], //按钮
            yes: function(){
                delelLabel(labelArr)
            },btn2:function () {
                layer.closeAll();
            }
        });
    });

    //修改标签
    $('#labelInfo').on('click','.modify-style',function () {
        var _this = $(this);
        var parentEle = _this.parents('.queryData');
        var labelDataEel = parentEle.find('.label_data');
        var modifyLabelEle = parentEle.find('.modify-label');
        labelDataEel.hide();
        modifyLabelEle.show();
    });

    //修改取消
    $('#labelInfo').on('click','.modify-label-cancel',function () {
        var _this = $(this);
        var parentEle = _this.parents('.queryData');
        var labelDataEel = parentEle.find('.label_data');
        var modifyLabelEle = parentEle.find('.modify-label');
        labelDataEel.show();
        modifyLabelEle.hide();
    });

    //修改确认
    $('#labelInfo').on('click','.modify-label-confirm',function () {
        var _this = $(this);
        var labelData = $.trim(_this.siblings('input').val());
        var labelDataID = _this.parents('.ar').find('.label_data').attr('data-id');
        var dataJSON = {'labelData':labelData,'labelDataID':labelDataID};
        addLabel(dataJSON, "../content/updateIssue.do","修改失败");
    });

    $('#infoLabelData').on('keypress','.modify-label input',function () {
        var labelData = $.trim($(this).val());
        var labelDataID = $(this).parents('.ar').find('.label_data').attr('data-id');
        var dataJSON = {'labelData':labelData,'labelDataID':labelDataID};
        if(event.keyCode == 13){
            addLabel(dataJSON, "../content/updateIssue.do","修改失败");
        }
    });

    //添加标签
    $('#infoLabelData').on('click','.add_label-btn',function () {
        var _this = $(this);
        var labelData = $.trim(_this.siblings('.label_input').val());
        var dataJSON = {'labelData':labelData};
        addLabel(dataJSON, "../content/updateIssue.do","添加失败");
    });
    $('#infoLabelData').on('keypress','.label_input',function () {
        var labelData = $.trim($(this).val());
        var dataJSON = {'labelData':labelData};
        if(event.keyCode == 13){
            addLabel(dataJSON, "../content/updateIssue.do","添加失败");
        }
    });


    //批量删除
    $('#infoLabelData').on('click','.delete_batch',function () {
        var _this = $(this);
        var parentEle = _this.parents('#infoLabelData');
        var labelData = [];
        parentEle.find("input[name='commonid']:checked").each(function (index,value) {
            labelData.push($(value).attr('data-id'));
        })
        if(!labelData.length){
            layer.msg("请选择删除的内容");
        }else{
            delelLabel(labelData)
        }
    });

    //全选
    $('#labelInfo').on('click','.allId',function () {
        var _this = $(this);
        if (_this.is(':checked')) {
            $("input[name=commonid]").each(function() {
                $(this).prop("checked", "checked");
            });
        } else {
            $("input[name=commonid]").each(function() {
                $(this).removeAttr("checked");
            });
        }
    });
    $('#labelInfo').on('change','input[name=commonid]',function () {
        var checkbedLen = $('input[name=commonid]:checked').length;
        var noCheckedLen = $('input[name=commonid]').length;
        if(checkbedLen == noCheckedLen){
            $('.allId').prop("checked", "checked");
        }else{
            $('.allId').removeAttr("checked");
        }
    });

})

function delelLabel(labelArr) {
    $.ajax({
        type : "post",
        url : "../content/updateIssue.do",
        data :{
            "label" : labelArr
        },
        dataType : "json",
        success : function(data){
            layer.closeAll();
            if(data.status == 200){
                window.location.href();
            }else{
                layer.msg("删除失败!");
            }
        },
        error : function(err){
            layer.closeAll();
            layer.msg("删除失败!");
        }
    });
}

function addLabel(labelData,url,tip) {
    if(labelData == ''){
        layer.msg("请填写标签!")
    }else{
        $.ajax({
            type : "post",
            url :url,
            data :labelData,
            dataType : "json",
            success : function(data){
                if(data.status == 200){
                    window.location.href();
                }else if(data.status == 401){
                    layer.msg("标签已存在，请填写别的标签！");
                }else{
                    layer.msg(tip);
                }
            },
            error : function(err){
                layer.msg(tip);
            }
        })
    }
}
