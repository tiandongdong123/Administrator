$(function(){
    yseChoose();
    //弹出框
    $(".institutionId").click(function(){
        var h = $("body").height();  //获取当前浏览器界面的高度
        var backwidth = $("body").width();   //获取当前浏览器界面的宽度
        var backheight=h+42;
        $(".backdrop").css({
            height:backheight,      //给遮盖层的div的高宽度赋值
            width:backwidth,
            display:"block"  //遮盖层显示
        });
        $(".pop").show();
    });
    $(".x").click(function(){
        noChoose();
        $(".revise").text("修改");
        $(".backdrop").hide();
        $(".pop").hide();
    });
    //机构ID弹出框
    $(".userID").click(function(){
        $(".backdrop").show();
        $(".pop").show();
        $("#institution").val($(this).siblings(".username").val());
        $(".enshrine").val($(this).val());
        var bindtype = $(".bindtype").val();
        if(bindtype=="机构个人同时登录"){
            $("#bindType").val()=="0";
        }
        else if(bindtype=="机构登陆"){
            $("#bindType").val()=="1";
        }else if(bindtype="线下扫描"){
            $("#bindType").val()=="2";
        }
        $("#bindLimit").val($(this).siblings(".bindLimit").val());
        $("#bindValidity").val($(this).siblings(".bindValidity").val());
        $("#downloadLimit").val($(this).siblings(".downloadLimit").val());
        var authority = $(".authority").val();
        if(authority=="资源下载,万方分析"){
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
                $(".bind_numm").css("color","#dd4b39");
                $(".wrongm").css("background","url(../img/f.png)");
                $(".mistakenm").css("display","inline");
                $(".wrongm").css("display","inline");
                $(".mistakenm").text("机构ID不能为空");
            }else {
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
function yseChoose() {
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
            url : "",
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
            $(".bind_numm").css("color","#dd4b39");
            $(".wrongm").css("background","url(../img/f.png)");
            $(".mistakenm").css("display","inline");
            $(".wrongm").css("display","inline");
            $(".mistakenm").text("机构ID不能为空");
            bool = true;
        }else{
            $(".bind_numm").css("color","#00a65a");
            $(".wrongm").css("background","url(../img/t.png)");
            $(".wrongm").css("display","inline");
            $(".mistakenm").css("display","none");
        }
        if(!validateFrom()){
            $("#submit").removeAttr("disabled");
            bool = true;
        }    else{
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
            var bindAuthority = $("#bindAuthority").val();
            if(bool){
                return ;
            }
            $.ajax({
                type : "post",
                url : "",
                data:{
                    userId:mechanism_id.join(),
                    bindType:bindType,
                    bindLimit:bindLimit,
                    bindValidity:bindValidity,
                    downloadLimit:downloadLimit,
                    bindAuthority:bindAuthority,
                },
                success: function(data){
                    yseChoose();
                    $(".revise").text("修改");
                    $(".backdrop").hide();
                    $(".pop").hide();
                }
            });
        }
    }
}
//取消
function abolish(){
    noChoose();
    $(".revise").text("修改");
    $(".backdrop").hide();
    $(".pop").hide();
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
        dataType:"json",
        success:function(data){
            $('#bind_table').html(data);
            // 显示分页
            laypage({
                cont: 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
                // id="page1"></div>
                pages: pages, // 通过后台拿到的总页数
                curr: curr, // 当前页
                skip: true, // 是否开启跳页
                skin: 'molv',// 当前页颜色，可16进制
                groups: groups, // 连续显示分页数
                first: '首页', // 若不显示，设置false即可
                last: '尾页', // 若不显示，设置false即可
                prev: '上一页', // 若不显示，设置false即可
                next: '下一页', // 若不显示，设置false即可
                jump: function(obj, first){ // 触发分页后的回调
                    if(!first){ // 点击跳页触发函数自身，并传递当前页：obj.curr
                        tabulation(obj.curr);
                    }
                }
            });
        }
    });
}





//刷新页面
function refresh(){
    window.location.href="../log/getLog.do";
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


