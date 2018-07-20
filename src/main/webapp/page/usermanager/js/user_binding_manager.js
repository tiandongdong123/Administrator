var already = true;
var prevNum;
$(function(){
    var page_show=20;
    redq();
    if($(".parameter").val()!=""){
       $("#userId").val($(".parameter").val());
        inquiry();
    }
    yesChoose();
    //弹出框
    $(".institutionId").click(function(){
        $(".backdrop").css({
            display:"block"  //遮盖层显示
        });
        $(".pop").show();
    });
    $(".x").click(function(){
        yesChoose();
        $(".mechanism_id").css("border-color","#eee");
        $(".revise").text("修改");
        $(".more_userId").css("display","none");
        $(".backdrop").hide();
        $(".pop").hide();
        dislodge();
    });
    //绑定模式判断并展示二维码
    var choose = true;
    var reset;
    var relocate;

    $(document).on("change",".evey-page",function(){
        //选择多少条数据
        var userId = $("#userId").val();
        var institutionName = $("#institutionName").val();
        var startDay = $("#startDay").val();
        var endDay = $("#endDay").val();
        var pageSize = $(".evey-page option:selected").text();
        var page = $(".laypage_curr").text();
        $.ajax({
            type:"POST",
            data:{
                userId:userId,
                institutionName:institutionName,
                startDay:startDay,
                endDay:endDay,
                pageSize:pageSize,
                page: page,
            },
            url:"../bindAuhtority/searchBindInfo.do",
            success:function(data){
                $('.sync-html').html(data);
                $('.evey-page option').each(function(){
                    var value = +$(this).val();
                    if(value == pageSize){
                        $(this).attr('selected',true);
                    }
                });
                redq();
            }
        });
    });

    $('#database-table').on('click','.bindtype',function(e){
        e = e || window.event;
        e.stopPropagation();
        e.preventDefault();
        if($(this).text()=="线下扫描"){
            var num = $(this).data('num');
            $.ajax({
                url:'../bindAuhtority/showBindInfo.do',
                type:'post',
                dataType:'json',
                data:{
                    userId:$(this).siblings(".userID").text()
                },
                async:true,
                success:function(data){
                    var email = data.email;
                    $('.email-text').text(email);
                }
            })
            if(num != prevNum){
                $(".qr").show();
                relocate = $(this).siblings(".userID").text();
                $('.picture').attr('src','../bindAuhtority/getQRCode.do?userId='+relocate+'&time='+(new Date()));
                prevNum = num;
            }else{
                $('.qr').stop(true,true).toggle();
            }
        }
    })
    $('html,body').click(function(){
        $(".qr").hide();
    });
    //点击重置二维码
    $(document).on("click",".reset",function(){
        $(".qr").show();
        var userId_qr = relocate;
        $('.picture').attr('src','../bindAuhtority/resetQRCode.do?userId='+userId_qr+'&time='+(new Date()));
    });
    $(document).on('click','.sendEmail',function(){
        var userId = $("#userId").val();
        var bindEmail = $(this).next().find('.email-text').text();
        $.ajax({
            url:'../bindAuhtority/sendMailQRCode.do',
            data:{
                userId:userId,
                bindEmail:bindEmail,
            },
            success:function(){
                layer.msg('发送成功', {icon: 1});
            },
            error:function(){
                layer.msg('发送失败', {icon: 2});
            }
        });
    });
    //机构ID弹出框
    $(document).on("click",".userID",function(){
        var userId = $.trim($(this).text());
        $.ajax({
            url:'../bindAuhtority/showBindInfo.do',
            type:'post',
            dataType:'json',
            data:{
                userId:userId
            },
            async:true,
            success:function(data){
                var openBindStart = meGetDate(data.openBindEnd);
                var openBindEnd = meGetDate(data.openBindEnd);
                var email = data.email;
                $('#openBindStart').val(openBindStart);
                $('#openBindEnd').val(openBindEnd);
                $('#email').val(email);
            }
        })
        $(".mechanism_id").css("border-color","#d2d6de");
        $(".backdrop").show();
        $(".pop").show();
        $("#institution").val($(this).siblings(".username").text());
        $(".enshrine").text($(this).text());
        var bindtype = $(this).siblings(".bindtype").text();
        if(bindtype=="机构个人同时登录"){
            $("#bindType option:eq(0)").prop("selected","selected");
            $('.qrEmail-box').hide();
        }else if(bindtype=="机构登录"){
            $("#bindType option:eq(1)").prop("selected","selected");
            $('.qrEmail-box').hide();
        }else if(bindtype=="线下扫描"){
            $("#bindType option:eq(2)").prop("selected","selected");
            $('.qrEmail-box').show();
        }
        $("#bindLimit").val($(this).siblings(".bindLimit").text());
        $("#bindValidity").val($(this).siblings(".bindValidity").text());
        $("#downloadLimit").val($(this).siblings(".downloadLimit").text());
        var authority = $(this).siblings(".authority").text();
        if(authority=="万方分析、资源下载"){
            $("#allInherited").prop("checked",true);
            $(".selFirst").prop("checked",true);
        }
        else if(authority=="资源下载"){
            $("#allInherited").prop("checked",false);
            $("#wanInherited").prop("checked",false);
            $("#resourceInherited").prop("checked",true);
        }
        else if(authority=="万方分析"){
            $("#allInherited").prop("checked",false);
            $("#wanInherited").prop("checked",true);
            $("#resourceInherited").prop("checked",false);
        }
    });
    //机构id点击全部
    $(".tol_quota").click(function(){
        $(".allSelectText").text("");
        $("input[name='quotaName']").prop("checked",$(".tol_quota").prop("checked"));
        if($(".tol_quota").is(":checked")){
            $(".enshrine").text("全部");
            $(".mechanism_id").css("border-color","#00a65a");
            $(".bind_numm").css("color","#00a65a");
            $(".wrongm").css("background","url(../img/t.png)");
            $(".wrongm").css("margin-left","10px");
            $(".wrongm").css("display","inline");
            $(".mistakenm").css("display","none");
        }
        else{
            $(".enshrine").text("");
            if($("enshrine").text()==""){
                $(".mechanism_id").css("border-color","#dd4b39");
                $(".bind_numm").css("color","#dd4b39");
                $(".wrongm").css("background","url(../img/f.png)");
                $(".wrongm").css("margin-left","9px");
                $(".mistakenm").css("display","inline");
                $(".wrongm").css("display","inline");
                $(".mistakenm").text("机构ID不能为空");
            }else {
                $(".mechanism_id").css("border-color","#00a65a");
                $(".bind_numm").css("color","#00a65a");
                $(".wrongm").css("background","url(../img/t.png)");
                $(".wrongm").css("margin-left","10px");
                $(".wrongm").css("display","inline");
                $(".mistakenm").css("display","none");
            }
        }
    });
    //机构id点击其他
    $(".quota").on("click",".index",function(){
        commonCaption($(this));
    });
    //绑定个人上限的提示  userid_choose
    $("#bindLimit").keyup(function(){
        var userId = new Array();
        $(".quota input[class='index']:checked").each(function () {
            userId.push($(this).val());
        });
        var bindLimit = $("#bindLimit").val();
        var reg = /^[1-9]\d*$/;
        if($("#bindLimit").val()==""){
            $(".mistaken").text("绑定个人上限不能为空，请填写正确的数字");
            $(".wrong").css("margin-left","5px");
            $(".more_userId").css("display","none");
            style();
            return;
        }
        if(!reg.test($("#bindLimit").val())){
            $(".mistaken").text("绑定个人上限是大于0的整数，请填写正确的数字");
            $(".more_userId").css("display","none");
            $(".wrong").css("margin-left","5px");
            style();
            return;
        }
        $.ajax({
            url: '../bindAuhtority/checkAllBindLimit.do',
            type: 'POST',
            dateType:"json",
            async:false,
            data:{
                userId: userId.join(),
                bindLimit:bindLimit,
            },
            success: function(data){
                if(data.length>0){
                    $(".more_userId").text(data);
                    $(".mistaken").text("已绑定人数超过修改后的个人上限，请联系管理员解绑");
                    $(".more_userId").css("display","inline");
                    style();
                    already = false;
                }else {
                    $(".bind_num").css("color","#00a65a");
                    $("#bindLimit").css("border-color","#00a65a");
                    $(".wrong").css("background","url(../img/t.png)");
                    $(".wrong").css("margin-left","10px");
                    $(".wrong").css("display","inline");
                    $(".mistaken").css("display","none");
                    $(".more_userId").css("display","none");
                    already = true;
                }
            }
        });
    });
});
//移除disabled属性
function noChoose(){
    $(".mechanism_id").attr("disabled",false);
    $("#bindType").attr("disabled",false);
    $("#bindLimit").attr("disabled",false);
    $("#bindValidity").attr("disabled",false);
    $("#downloadLimit").attr("disabled",false);
    $("#allInherited").attr("disabled",false);
    $(".selFirst").attr("disabled",false);
    $('#openBindStart').attr('disabled',false);
    $('#openBindEnd').attr('disabled',false);
    $('#email').attr('disabled',false);
}
//设置disabled属性
function yesChoose() {
    $(".mechanism_id").attr("disabled",true);
    $("#bindType").attr("disabled",true);
    $("#bindLimit").attr("disabled",true);
    $("#bindValidity").attr("disabled",true);
    $("#downloadLimit").attr("disabled",true);
    $("#allInherited").attr("disabled",true);
    $(".selFirst").attr("disabled",true);
    $('#openBindStart').attr('disabled',true);
    $('#openBindEnd').attr('disabled',true);
    $('#email').attr('disabled',true);
}
//修改
function revise(){
    var reg = /^[1-9]\d*$/;
    var bool = false;
    if($(".revise").text()=="修改"){
        noChoose();
        $(".revise").text("提交");
        $(".jg_index").remove();
        $(".enshrine").text("");
        var institution = $("#institution").val();
        $.ajax({
            type : "post",
            url : "../bindAuhtority/allUserId.do",
            dataType : "json",
            data:{
                institutionName: institution,
            },
            success: function(data){
                length = data.length;
                for(var i=0;i<length;i++){
                    var bindType = '<li class="jg_index"><label><input value='+data[i]+' name="quotaName" class="index" checked="checked" type="checkbox"><span class="userid_choose">'+data[i]+'</span></label></li>';
                    $(".quota").append(bindType);
                }
                if(length==1){
                    $(".data_first").css("display","none");
                    $(".enshrine ").text(data[0]);
                    showFont();
                }else {
                    $(".data_first").css("display","block");
                    $(".tol_quota").prop("checked","checked");
                    $(".enshrine").text($(".quota li:first").text());
                    showFont();
                }
            },
        });
    }else {
        if($("#bindLimit").val()==""){
            $(".mistaken").text("绑定个人上限不能为空，请填写正确的数字");
            style();
            bool = true;
        }else if(!reg.test($("#bindLimit").val())){
            $(".mistaken").text("绑定个人上限是大于0的整数，请填写正确的数字");
            style();
            bool = true;
        }else if(!already){
            $(".mistaken").text("已绑定人数超过修改后的个人上限，请联系管理员解绑");
            style();
            bool = true;
        }else{
            $(".bind_num").css("color","#00a65a");
            $("#bindLimit").css("border-color","#00a65a");
            $(".wrong").css("background","url(../img/t.png)");
            $(".wrong").css("margin-left","10px");
            $(".wrong").css("display","inline");
            $(".mistaken").css("display","none");
        }
        if($(".enshrine").text()=="")
        {
            $(".mechanism_id").css("border-color","#dd4b39");
            $(".bind_numm").css("color","#dd4b39");
            $(".wrongm").css("background","url(../img/f.png)");
            $(".wrongm").css("margin-left","6px");
            $(".mistakenm").css("display","inline");
            $(".wrongm").css("display","inline");
            $(".mistakenm").text("机构ID不能为空");
            bool = true;
        }else{
            $(".mechanism_id").css("border-color","#00a65a");
            $(".bind_numm").css("color","#00a65a");
            $(".wrongm").css("background","url(../img/t.png)");
            $(".wrongm").css("margin-left","10px");
            $(".wrongm").css("display","inline");
            $(".mistakenm").css("display","none");
        }
        if($('#bindType').find("option:selected").val() == '2'){
            $('#fromList').bootstrapValidator('addField','email',{
                validators : {
                    notEmpty : {
                        message : '请输入邮箱'
                    },
                    emailAddress: {/* 只需加此键值对，包含正则表达式，和提示 */
                        message: '请输入正确的邮箱地址'
                    }
                }
            });
        }else{
            $('#fromList').bootstrapValidator('removeField','email');
        }
        if(!validateFrom()){
            $("#submit").removeAttr("disabled");
            bool = true;
        }else{
            //机构ID
            var mechanism_id = new Array();
            $("input[class='index']:checked").each(function () {
                mechanism_id.push($(this).val());
            });
            //绑定模式
            var bindType = $("#bindType").val();
            //绑定个人上限
            var bindLimit = $("#bindLimit").val();
            //绑定个人下载量有效期
            var bindValidity = $("#bindValidity").val();
            //绑定个人下载量上限
            var downloadLimit = $("#downloadLimit").val();
            //绑定个人继承权限
            var bindAuthority = new Array();
            $("input[class='selFirst']:checked").each(function () {
                bindAuthority.push($(this).val());
            });
            var meEmail = $('#email').val();
            var openBindStart = $('#openBindStart').val()+' 00:00:00';
            var openBindEnd = $('#openBindEnd').val()+' 23:59:59';
            var isCheckedMe;
            if($('#bindType').find("option:selected").val() == '2'){
                isCheckedMe = $('#isPublishEmail').is(':checked');
            }else{
                isCheckedMe = false;
            }
            if(bool){
                return ;
            }
            $.ajax({
                type : "post",
                url : "../bindAuhtority/updateAuthority.do",
                data:{
                    userId:mechanism_id.join(),
                    bindType:bindType,
                    bindLimit:bindLimit,
                    bindValidity:bindValidity,
                    downloadLimit:downloadLimit,
                    bindAuthority:bindAuthority.join(),
                    email:meEmail,
                    openBindStart:openBindStart,
                    openBindEnd:openBindEnd,
                    send:isCheckedMe
                },
                success: function(data){
                    yesChoose();
                    $(".revise").text("修改");
                    $(".backdrop").hide();
                    $(".pop").hide();
                    dislodge();
                    $(".mechanism_id").css("border-color","#d2d6de");
                    inquiry();
                }
            });
        }
    }
}

//去除bootstrop
function dislodge(){
    $('.form-group').each(function(){ $(this).removeClass('has-success  has-error') });
    $(".help-block").css("display","none");
    $(".form-control-feedback").css("display","none");
    $(".bind_num").css("color","#333");
    $(".mistaken").css("display","none");
    $(".wrong").css("display","none");
    $(".bind_numm").css("color","#333");
    $(".mistakenm").css("display","none");
    $(".wrongm").css("display","none");
    $("#bindLimit").css("border-color","#ccc");
}
//取消
function abolish(){
    yesChoose();
    $(".mechanism_id").css("border-color","#d2d6de");
    $(".revise").text("修改");
    $(".more_userId").css("display","none");
    $(".backdrop").hide();
    $(".pop").hide();
    dislodge();
}
function inquiry(){
    var userId = $("#userId").val();
    var institutionName = $("#institutionName").val();
    var startDay = $("#startDay").val();
    var endDay = $("#endDay").val();
    var pageSize = $(".evey-page").val();
    if (pageSize == null) {
        pageSize = 20;
    }
    prevNum = 0;
    $.ajax({
        type:"POST",
        data:{
            userId:userId,
            institutionName:institutionName,
            startDay:startDay,
            endDay:endDay,
            pageSize:pageSize,
            page: 1,
        },
        url:"../bindAuhtority/searchBindInfo.do",
        success:function(data){
            $('.sync-html').html(data);
            redq();
        }
    });
}
function redq(){
    if($(".bindtype").length>0){
        $(".bindtype").each(function (index,value) {
            if($(value).text()=="线下扫描"){
                $(value).css({
                    "color":"#0c60c2",
                    "cursor":"pointer",
                });
            }
        })
    }
}
//刷新页面
function refresh(){
    window.location.href="../bindAuhtority/bindInfo.do";
}
/**
 * 日期转换
 * @param time
 * @returns {String}
 */
function timeStamp2String(time){
    var year = 1900+time.year;
    var month = time.month + 1 < 10 ? "0" + (time.month + 1) : time.month + 1;
    var date = time.date < 10 ? "0" + time.date : time.date;
    var hour = time.hours< 10 ? "0" + time.hours : time.hours;
    var minute = time.minutes< 10 ? "0" + time.minutes : time.minutes;
    var second = time.seconds< 10 ? "0" + time.seconds : time.seconds;
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}

//翻页跳转
(function () {
    var getPager = function (url, $container) {
        var evey = $(".evey-page").val();
        $.get(url, function (html) {
            $container.html(html);
            $(".evey-page").val(evey);
            redq();
        });
    };
    //page-a异步跳转
    $(document).on('click', '.sync-html .page_bind a', function () {
        var evey = $(".evey-page").val();
        $('.sync-html').empty('');
        var href = $(this).attr('href');
        $(this).removeAttr('href');
        $.get(href, function (html) {
            $('.sync-html').html(html);
            $(".evey-page").val(evey);
            redq();
        });

    });
    //page-form异步跳转
    $(document).on('submit', '.sync-html .page_bind form', function () {
        var evey = $(".evey-page").val();
        var action = $(this).attr('action');
        var inputPage = parseInt($(this).find('.laypage_skip').val());
        var allPage = $(this).attr('data-all');
        if (inputPage > 0 && inputPage <= allPage) {
            var href = action + inputPage;
            getPager(href, $(this).closest('.sync-html'));
            $(".evey-page").val(evey);
            redq();
        } else {
            alert('请输入正确页码');
        }
        return false;
    });
    //page-form同步跳转
    $(document).on('submit', '.no-sync .page_bind form', function () {
        var evey = $(".evey-page").val();
        var action = $(this).attr('action');
        var inputPage = parseInt($(this).find('.laypage_skip').val());
        var allPage = $(this).attr('data-all');
        if (inputPage > 0 && inputPage <= allPage) {
            window.location.href = action + encodeURIComponent(inputPage);
            $(".evey-page").val(evey);
            redq();
        } else {
            alert('请输入正确页码');
        }
        return false;
    });
})();
