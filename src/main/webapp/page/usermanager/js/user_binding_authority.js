/**
 * Created by 01 on 2018/1/18.
 */
var length=0;
$(document).ready(function(){
    $("#institution").blur(function(){
        $(".jg_index").remove();
        $(".enshrine").text("");
        var institution = $("#institution").val();
        $.ajax({
            type : "post",
            url : "../bindAuhtority/userId.do",
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
                if(length==0){
                    $(".data_first").css("display","none");
                    $(".enshrine ").text(data[0]);
                    $(".bind_numm").css("color","#dd4b39");
                    $(".wrongm").css("background","url(../img/f.png)");
                    $(".mistakenm").css("display","inline");
                    $(".wrongm").css("display","inline");
                    $(".mistakenm").text("无匹配机构ID");
                    $(".mechanism_id").css("border-color","#dd4b39");
                }
                else if(length==1){
                    $(".data_first").css("display","none");
                    $(".bind_numm").css("color","#333");
                    $(".mistakenm").css("display","none");
                    $(".wrongm").css("display","none");
                    $(".mechanism_id").css("border-color","#d2d6de");
                    $(".enshrine ").text(data[0]);
                    showFont();
                } else {
                    //个人绑定机构   测试机构
                    $(".mechanism_id").css("border-color","#d2d6de");
                    $(".bind_numm").css("color","#333");
                    $(".mistakenm").css("display","none");
                    $(".wrongm").css("display","none");
                    $(".data_first").css("display","block");
                    $(".enshrine").text($(".quota li:first").text());
                    showFont();
                }
            },
        });
    });
    $('#bindType').change(function(){
        if($(this).find("option:selected").val() == '2'){
            $('.qrEmail-box').show();
        }else{
            $('.qrEmail-box').hide();
        }
    });
    //机构id点击全部
    $(".tol_quota").click(function(){
        $(".allSelectText").text("");
        $("input[name='quotaName']").prop("checked",$(".tol_quota").prop("checked"));
        if($(".tol_quota").is(":checked")){
            $(".enshrine").text("全部");
            $(".bind_numm").css("color","#00a65a");
            $(".wrongm").css("background","url(../img/t.png)");
            $(".wrongm").css("display","inline");
            $(".mechanism_id").css("border-color","#00a65a");
            $(".mistakenm").css("display","none");
        }
        else{
            $(".enshrine").text("");
            if($("enshrine").text()==""){
                $(".bind_numm").css("color","#dd4b39");
                $(".wrongm").css("background","url(../img/f.png)");
                $(".mistakenm").css("display","inline");
                $(".wrongm").css("display","inline");
                $(".mechanism_id").css("border-color","#dd4b39");
                $(".mistakenm").text("机构ID不能为空");
            }else {
                $(".bind_numm").css("color","#00a65a");
                $(".wrongm").css("background","url(../img/t.png)");
                $(".wrongm").css("display","inline");
                $(".mechanism_id").css("border-color","#00a65a");
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

function submitNew(){
    var reg = /^[1-9]\d*$/;
    var bool = false;
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
    }
    if($('#bindType').find("option:selected").val() == '2'){
        $('#fromList').bootstrapValidator('addField','email',{
            validators : {
                notEmpty : {
                    message : '请输入邮箱'
                },
                regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                    regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
                    message: '请输入正确的邮箱地址'
                },
            }
        });
    }else{
        $('#fromList').bootstrapValidator('addField','email');
    }
    if(!validateFrom()){
        $("#submit").removeAttr("disabled");
        bool = true;
    }
    else{
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
            url : "../bindAuhtority/openAuthority.do",
            data:{
                userId:mechanism_id.join(),
                bindType:bindType,
                bindLimit:bindLimit,
                bindValidity:bindValidity,
                downloadLimit:downloadLimit,
                bindAuthority:bindAuthority.join(),
            },
            success: function(data){
                $("#institution").val("");
                $("input[name='quotaName']").prop("checked",true);
                $(".enshrine").text("全部");
                $("#bindType").val("1");
                $("#bindLimit").val("100");
                $("#bindValidity").val("180");
                $("#downloadLimit").val("30");
                $("#bindAuthority").val("resource");
                $("input[name='resourceType']").prop("checked",false);
                $("#resourceInherited").prop("checked",true);
                window.location.reload();
            }
        });
    }

}
function sunmit(){
    var reg = /^[1-9]\d*$/;
    var bool = false;
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
    if($('#bindType').find("option:selected").val() == '2'){
        $('#fromList').bootstrapValidator('addField','email',{
            validators : {
                notEmpty : {
                    message : '请输入邮箱'
                },
                regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                    regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
                    message: '请输入正确的邮箱地址'
                },
            }
        });
    }else{
        $('#fromList').bootstrapValidator('addField','email');
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
        var userId = mechanism_id.toString();
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
        var meEmail = $('#email').val();
        var openBindStart = $('#openBindStart').val()+'00:00:00';
        var openBindEnd = $('#openBindEnd').val()+'23:59:59';
        var isCheckedMe = $('#isPublishEmail').is(':checked');
        if(bool){
            return ;
        }
        $.ajax({
            type : "post",
            url : "../bindAuhtority/openAuthority.do",
            data:{
                userId:mechanism_id.join(),
                bindType:bindType,
                bindLimit:bindLimit,
                bindValidity:bindValidity,
                downloadLimit:downloadLimit,
                bindAuthority:bindAuthority,
                email:meEmail,
                openBindStart:openBindStart,
                openBindEnd:openBindEnd,
                send:isCheckedMe
            },
            success: function(data){
                $("#institution").val("");
                $("input[name='quotaName']").prop("checked",true);
                $(".enshrine").text("全部");
                $("#bindType").val("1");
                $("#bindLimit").val("100");
                $("#bindValidity").val("180");
                $("#downloadLimit").val("30");
                $("#bindAuthority").val("resource");
                $("input[name='resourceType']").prop("checked",false);
                $("#resourceInherited").prop("checked",true);
                window.location.href="../bindAuhtority/bindInfo.do";
            }
        });
    }
}
function abolish(){
    window.location.href="../bindAuhtority/bindInfo.do";
}

