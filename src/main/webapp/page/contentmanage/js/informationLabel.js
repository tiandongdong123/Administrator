$(function () {
    //修改标签
    $('#infoLabelData').on('click', '.modify-style', function () {
        var _this = $(this);
        var parentEle = _this.parents('.queryData');
        var labelDataEel = parentEle.find('.label_data');
        var modifyLabelEle = parentEle.find('.modify-label');
        labelDataEel.hide();
        modifyLabelEle.show();
    });

    //修改取消
    $('#infoLabelData').on('click', '.modify-label-cancel', function () {
        var _this = $(this);
        var parentEle = _this.parents('.queryData');
        var labelDataEel = parentEle.find('.label_data');
        var modifyLabelEle = parentEle.find('.modify-label');
        labelDataEel.show();
        modifyLabelEle.hide();
    });

    //修改确认
    $('#infoLabelData').on('click', '.modify-label-confirm', function () {
        var _this = $(this);
        var labelData = $.trim(_this.siblings('input').val());
        var labelDataID = _this.parents('.ar').find('.label_data').attr('data-id');
        var dataJSON = {'label': labelData, 'id': labelDataID};
        updataLabel(dataJSON, "../content/updateInformationLabel.do", _this.siblings('input'));
    });
    $('#infoLabelData').on('keypress', '.modify-label input', function () {
        var labelData = $.trim($(this).val());
        var labelDataID = $(this).parents('.ar').find('.label_data').attr('data-id');
        var dataJSON = {'label': labelData, 'id': labelDataID};
        if (event.keyCode == 13) {
            updataLabel(dataJSON, "../content/updateInformationLabel.do", $(this));
        }
    });

    //添加标签
    $('#infoLabelData').on('click', '.add_label-btn', function () {
        var _this = $(this);
        var labelData = _this.siblings('.label_input');
        var tip = _this.siblings('.add_label-tip');
        addLabel("../content/addInformationLabel.do", labelData,tip);
    });
    $('#infoLabelData').on('keypress', '.label_input', function () {
        var labelData = $(this);
        var tip = $(this).siblings('.add_label-tip');
        if (event.keyCode == 13) {
            addLabel("../content/addInformationLabel.do", labelData,tip);
        }
    });


    //批量删除
    $('#infoLabelData').on('click', '.delete_batch', function () {
        var _this = $(this);
        var parentEle = _this.parents('#infoLabelData');
        var labelData = [];
        parentEle.find("input[name='commonid']:checked").each(function (index, value) {
            labelData.push($(value).attr('data-id'));
        })
        layer.alert('确定删除该数据吗？', {
            icon: 1,
            skin: 'layui-layer-molv',
            btn: ['确定', '取消'], //按钮
            yes: function () {
                if (!labelData.length) {
                    layer.msg("请选择删除的内容");
                } else {
                    delelLabel(labelData)
                }
            }, btn2: function () {
                layer.closeAll();
            }
        });
    });
    //单条删除标签
    $('#infoLabelData').on('click', '.delete_label', function () {
        var _this = $(this);
        var parentEle = _this.parents('.queryData');
        var labelDataEel = parentEle.find('.label_data');
        var labelArr = [];
        labelArr.push(labelDataEel.attr('data-id'))
        layer.alert('确定删除该数据吗？', {
            icon: 1,
            skin: 'layui-layer-molv',
            btn: ['确定', '取消'], //按钮
            yes: function () {
                delelLabel(labelArr)
            }, btn2: function () {
                layer.closeAll();
            }
        });
    });

    //全选
    $('#infoLabelData').on('click', '.allId', function () {
        var _this = $(this);
        if (_this.is(':checked')) {
            $("input[name=commonid]").each(function () {
                $(this).prop("checked", "checked");
            });
        } else {
            $("input[name=commonid]").each(function () {
                $(this).removeAttr("checked");
            });
        }
    });
    $('#infoLabelData').on('change', 'input[name=commonid]', function () {
        var checkbedLen = $('input[name=commonid]:checked').length;
        var noCheckedLen = $('input[name=commonid]').length;
        if (checkbedLen == noCheckedLen) {
            $('.allId').prop("checked", "checked");
        } else {
            $('.allId').removeAttr("checked");
        }
    });
    //点击查询
    $('.query-confirm').on('click', function () {
        queryHandle(1);
    });
})

function delelLabel(labelArr) {
    $.ajax({
        type: "post",
        url: "../content/deleteInformationLabel.do",
        data: {
            ids: labelArr
        },
        dataType: "json",
        cache:false,
        success: function (data) {
            layer.closeAll();
            if (data) {
                layer.msg("删除成功");
                queryHandle(1);
            } else {
                layer.msg("删除失败!");
            }
        },
        error: function (err) {
            layer.closeAll();
            layer.msg("删除失败!");
        }
    });
}

function addLabel(url, labelDataEle,tip) {
    var labelData = $.trim(labelDataEle.val());
    if (labelData == '') {
        tip.show().text('请填写标签!');
    } else {
        $.ajax({
            url: url,
            data: {'label': labelData},
            dataType: "json",
            cache:false,
            success: function (data) {
                if (data.isSuccess == 'Success') {
                    layer.msg('添加成功');
                    $.trim($('#label').val(''));
                    $.trim($('#operation').val(''));
                    $.trim($('#startTime').val(''));
                    $.trim($('#endTime').val(''));
                    queryHandleLabel(1);
                } else if (data.isSuccess == 'exist') {
                    tip.show().text('标签已存在，请填写别的标签！');
                } else if (data.isSuccess == 'notEmpty') {
                    tip.show().text('请填写标签!');
                } else {
                    layer.msg('添加失败');
                }
            },
            error: function (err) {
                layer.msg("添加失败");
            }
        })
    }
}

function updataLabel(dataJSON, url, labelDataEle) {
    var labelData = $.trim(labelDataEle.val());
    if (labelData == '') {
        layer.msg("请填写标签!")
    } else {
        $.ajax({
            url: url,
            data: dataJSON,
            dataType: "json",
            cache:false,
            success: function (data) {
                if (data.isSuccess == 'success') {
                    layer.msg('修改成功');
                    queryHandle(1);
                } else if (data.isSuccess == 'exist') {
                    layer.msg("标签已存在，请填写别的标签！");
                } else {
                    layer.msg('修改失败');
                }
            },
            error: function (err) {
                layer.msg('修改失败');
            }
        })
    }
}

function queryHandle(page) {
    var infoLabelData = $('#infoLabelData');
    var label = $.trim($('#label').val());
    var operation = $.trim($('#operation').val());
    var startTime = $.trim($('#startTime').val());
    var endTime = $.trim($('#endTime').val());
    var page = page;
    var operatTimeStart = '';
    var operatTimeEnd = '';
    if (!startTime) {
        operatTimeStart = '';
    } else {
        operatTimeStart = startTime + ' 00:00:00';
    }
    if (!endTime) {
        operatTimeEnd = '';
    } else {
        operatTimeEnd = endTime + ' 23:59:59';
    }
    $.ajax({
        url: "../content/searchInformationLabel.do",
        data: {
            "label": label,
            "operator": operation,
            "operatingTimeStart": operatTimeStart,
            "operatingTimeEnd": operatTimeEnd,
            "page": page
        },
        cache:false,
        success: function (data) {
            infoLabelData.html(data);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function queryHandleLabel(page) {
    var infoLabelData = $('#infoLabelData');
    var page = page;
    $.ajax({
        url: "../content/searchInformationLabel.do",
        data: {
            "label": '',
            "operator": '',
            "operatingTimeStart": '',
            "operatingTimeEnd": '',
            "page": page
        },
        cache:false,
        success: function (data) {
            infoLabelData.html(data);
        },
        error: function (err) {
            console.log(err)
        }
    })
}