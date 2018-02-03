$(function(){
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
        $(".revise").text("修改");
        $(".backdrop").hide();
        $(".pop").hide();
        dislodge();
    });
    //绑定模式判断并展示二维码
    var choose = true;
    var reset;
    $(".bind_table").on("click",".bindtype",function(){
        if($(this).text()=="线下扫描"){
            if(choose){
                 $(".qr").show();
                 choose = false;
                 var userId = $(this).siblings(".userID").text();
                reset=userId;
                 $.ajax({
                     type : "post",
                     url : "../bindAuhtority/getQRCode.do",
                     dataType : "json",
                     data:{
                         userId:userId,
                     },
                     success: function(data){
                         $('.picture').attr('src',data);
                     }
                 });
            }else{
                $(".qr").hide();
                choose = true;
            }
        }
    });
    //点击重置二维码
    $(".bind_table").on("click",".reset",function(){
        var userId = reset;
        $.ajax({
            type : "post",
            url : "../bindAuhtority/resetQRCode.do",
            dataType : "json",
            data:{
                userId:userId,
            },
            success: function(data){
                $('.picture').attr('src',data);
                $(".qr").toggle();
            }
        });
    });
    //机构ID弹出框
    $(".bind_table").on("click",".userID",function(){
        $(".backdrop").show();
        $(".pop").show();
        $("#institution").val($(this).siblings(".username").text());
        $(".enshrine").text($(this).text());

        var bindtype = $(this).siblings(".bindtype").text();
        if(bindtype=="机构个人同时登录"){
            $("#bindType option:eq(0)").prop("selected","selected");
        }
        else if(bindtype=="机构登陆"){
            $("#bindType option:eq(1)").prop("selected","selected");
        }else if(bindtype="线下扫描"){
            $("#bindType option:eq(2)").prop("selected","selected");
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
            $(".bind_numm").css("color","#333");
            $(".mistakenm").css("display","none");
            $(".wrongm").css("display","none");
            $(".data_first").css("display","block");
        }
        else{
            $(".enshrine").text("");
            if($("enshrine").text()==""){
                $(".mechanism_id").css("border-color","#dd4b39");
                $(".bind_numm").css("color","#dd4b39");
                $(".wrongm").css("background","url(../img/f.png)");
                $(".mistakenm").css("display","inline");
                $(".wrongm").css("display","inline");
                $(".mistakenm").text("机构ID不能为空");
            }else {
                $(".mechanism_id").css("border-color","#00a65a");
                $(".bind_numm").css("color","#00a65a");
                $(".wrongm").css("background","url(../img/t.png)");
                $(".wrongm").css("display","inline");
                $(".mistakenm").css("display","none");
            }
        }
    });
    //机构id点击其他
    $(".quota").on("click",".index",function(){
        commonCaption($(this));
    });
    //绑定个人上限的提示
    $("#bindLimit").keyup(function(){
        check();
    });


});
//移除disabled属性
function noChoose(){
    $(".mechanism_id").attr("disabled",false);
    $("#bindType").attr("disabled",false);
    $("#bindType").attr("disabled",false);
    $("#bindLimit").attr("disabled",false);
    $("#bindValidity").attr("disabled",false);
    $("#downloadLimit").attr("disabled",false);
    $("#allInherited").attr("disabled",false);
    $(".selFirst").attr("disabled",false);
}
//设置disabled属性
function yesChoose() {
    $(".mechanism_id").attr("disabled",true);
    $("#bindType").attr("disabled",true);
    $("#bindType").attr("disabled",true);
    $("#bindLimit").attr("disabled",true);
    $("#bindValidity").attr("disabled",true);
    $("#downloadLimit").attr("disabled",true);
    $("#allInherited").attr("disabled",true);
    $(".selFirst").attr("disabled",true);
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
                    var bindType = '<li class="jg_index"><label><input value='+data[i]+' name="quotaName" class="index" checked="checked" type="checkbox"><span>'+data[i]+'</span></label></li>';
                    $(".quota").append(bindType);
                }
                if(length==1){
                    $(".data_first").css("display","none");
                    $(".enshrine ").text(data[0]);
                    showFont();
                }else {
                    $(".data_first").css("display","block");
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
        }else{
            $(".bind_num").css("color","#00a65a");
            $("#bindLimit").css("border-color","#00a65a");
            $(".wrong").css("background","url(../img/t.png)");
            $(".wrong").css("display","inline");
            $(".mistaken").css("display","none");
        }
        if($(".enshrine").text()=="")
        {
            $(".mechanism_id").css("border-color","#dd4b39");
            $(".bind_numm").css("color","#dd4b39");
            $(".wrongm").css("background","url(../img/f.png)");
            $(".mistakenm").css("display","inline");
            $(".wrongm").css("display","inline");
            $(".mistakenm").text("机构ID不能为空");
            bool = true;
        }else{
            $(".mechanism_id").css("border-color","#00a65a");
            $(".bind_numm").css("color","#00a65a");
            $(".wrongm").css("background","url(../img/t.png)");
            $(".wrongm").css("display","inline");
            $(".mistakenm").css("display","none");
            bool = false;
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
                },
                success: function(data){
                    yesChoose();
                    $(".revise").text("修改");
                    $(".backdrop").hide();
                    $(".pop").hide();
                    dislodge();
                    refresh();
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
    $(".revise").text("修改");
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
            $('#bind_table').html(data);
            redq();
        }
    });
}
function redq() {
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


//选择多少条数据
function pageChange() {
    var userId = $("#userId").val();
    var institutionName = $("#institutionName").val();
    var startDay = $("#startDay").val();
    var endDay = $("#endDay").val();
    var pageSize = $(".evey-page").val();
    var changePage = $(".evey-page option:selected").text();
    if(pageSize==null){
        pageSize=20;
    }
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
        dataType:"json",
        success:function(data){
            $('#bind_table').html(data);
            $('.evey-page option').each(function(){
                var value = +$(this).val();
                if(value == changePage){
                    $(this).attr('selected',true);
                }
            });
        }
    });
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


