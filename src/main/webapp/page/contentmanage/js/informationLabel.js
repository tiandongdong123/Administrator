$(function () {
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
        var dataJSON = {'label':labelData,'id':labelDataID};
        updataLabel(dataJSON, "../content/updateInformationLabel.do",_this.siblings('input'));
    });
    $('#infoLabelData').on('keypress','.modify-label input',function () {
        var labelData = $.trim($(this).val());
        var labelDataID = $(this).parents('.ar').find('.label_data').attr('data-id');
        var dataJSON = {'label':labelData,'id':labelDataID};
        if(event.keyCode == 13){
            updataLabel(dataJSON, "../content/updateInformationLabel.do",$(this));
        }
    });

    //添加标签
    $('#infoLabelData').on('click','.add_label-btn',function () {
        var _this = $(this);
        var labelData = _this.siblings('.label_input');;
        addLabel( "../content/addInformationLabel.do",labelData);
    });
    $('#infoLabelData').on('keypress','.label_input',function () {
        var labelData = $(this);
        if(event.keyCode == 13){
            addLabel("../content/addInformationLabel.do",labelData);
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
        layer.alert('确定删除该数据吗？',{
            icon: 1,
            skin: 'layui-layer-molv',
            btn: ['确定','取消'], //按钮
            yes: function(){
                if(!labelData.length){
                    layer.msg("请选择删除的内容");
                }else{
                    delelLabel(labelData)
                }
            },btn2:function () {
                layer.closeAll();
            }
        });
    });
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
        url : "../content/deleteInformationLabel.do",
        data :{
            "ids" : labelArr
        },
        dataType : "json",
        success : function(data){
            layer.closeAll();
            if(data){
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

function addLabel(url,labelDataEle) {
    var labelData = $.trim(labelDataEle.val());
    labelDataEle.val('');
    if(labelData == ''){
        layer.msg("请填写标签!")
    }else{
        $.ajax({
            url :url,
            data :{'label':labelData},
            dataType : "json",
            success : function(data){
                if(data.isSuccess == 'Success'){
                    layer.msg('添加成功');
                    setTimeout(function () {
                        window.location.reload();
                    },1000);
                }else if(data.isSuccess == 'exist'){
                    layer.msg("标签已存在，请填写别的标签！");
                }else if(data.isSuccess == 'notEmpty'){
                    layer.msg('请填写标签');
                }else{
                    layer.msg('添加失败');
                }
            },
            error : function(err){
                layer.msg("添加失败");
            }
        })
    }
}

function updataLabel(dataJSON,url,labelDataEle) {
    var labelData = $.trim(labelDataEle.val());
    labelDataEle.val('');
    if(labelData == ''){
        layer.msg("请填写标签!")
    }else{
        $.ajax({
            url :url,
            data :dataJSON,
            dataType : "json",
            success : function(data){
                if(data.isSuccess == 'Success'){
                    layer.msg('修改成功');
                    setTimeout(function () {
                        window.location.reload();
                    },1000);
                }else if(data.isSuccess == 'exist'){
                    layer.msg("标签已存在，请填写别的标签！");
                }else{
                    layer.msg('修改失败');
                }
            },
            error : function(err){
                layer.msg('修改失败');
            }
        })
    }
}