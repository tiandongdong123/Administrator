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
                    var bindType = '<li class="jg_index"><label><input value='+data[i]+' name="quotaName" class="index" checked="checked" type="checkbox"><span>'+data[i]+'</span></label></li>';
                    $(".quota").append(bindType);
                }
                if(length==0){
                    $(".data_first").css("display","none");
                    $(".enshrine ").text(data[0]);
                }
                else if(length==1){
                    $(".data_first").css("display","none");
                    $(".enshrine ").text(data[0]);
                } else {
                    $(".data_first").css("display","block");
                    $(".enshrine").text($(".quota li:first").text());
                    showFont();
                }
            },
        });

    });
    //机构id点击全部
    $(".tol_quota").click(function(){
        $(".allSelectText").text("");
        $("input[name='quotaName']").prop("checked",$(".tol_quota").prop("checked"));
        if($(".tol_quota").is(":checked")){
            $(".enshrine").text("全部");
        }
        else{
            $(".enshrine").text("");
        }
    })
    //机构id点击其他
    $(".quota").on("click",".index",function(){
        commonCaption($(this));
    });
    //绑定个人上限的提示
    $("#bindLimit").keyup(function(){
        check();
    })
});
//鼠标经过有提示
function  showFont() {
    $(".mechanism_id").hover(function(){
        if($('.index:checked').length!=0){
            $(".show_mechanism").text($(".enshrine").text());
            $(".show_mechanism").show();
        }
    },function(){
         $(".show_mechanism").hide();
    });
}

function commonCaption(e) {
    var all_index= $('.jg_index').length;
    var num= $('.index:checked').length;
    var curText = e.next().text();
    if(e.is(':checked')){
        $('.enshrine').append('<span class="indexitemText" data-text='+curText+'>'+curText+","+'</span>');
        if(all_index==num){
            if(all_index==1){
                $(".enshrine ").text($(".quota li:first").text());
            }else {
                $(".tol_quota").prop("checked",true);
                $('.indexitemText').remove();
                $(".enshrine").text("全部");
            }
        }
    }else {
        $('.tol_quota').prop("checked","");
        $('.allSelectText').text('');
        $('.enshrine span').each(function () {
            if(curText == $(this).data('text')){
                $(this).remove();
            }
        });
        $('.enshrine').text('');
        $('.index:checked').each(function(){
            curText = $(this).next().text();
            $('.enshrine').append('<span class="indexitemText" data-text='+curText+'>'+curText+","+'</span>');
        });
    }
}
//点击箭头变化
function icont(){
    if(length==0){
        $(".arrow").css({"background-position-x":"-10px"});
        $(".quota").hide();
    }
    else {
        $(".arrow").css({"background-position-x":"-39px"});
        $(".quota").toggle();
    }
    if($.trim($(".quota").css("display"))=="none"){
        $(".arrow").css({"background-position-x":"-10px"});
    }
}

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
        var downlaodLimit = $("#downlaodLimit").val();
        //绑定个人继承权限
        var bindAuthority = $("#bindAuthority").val();
        if(bool){
            return ;
        }
        $.ajax({
            type : "post",
            url : "../bindAuhtority/openAuthority",
            data:{
                userId:mechanism_id,
                bindType:bindType,
                bindLimit:bindLimit,
                bindValidity:bindValidity,
                downlaodLimit:downlaodLimit,
                bindAuthority:bindAuthority,
            },
            success: function(data){
                // $("#institution").val("");
                // $("input[name='quotaName']").prop("checked",true);
                // $(".enshrine").text("全部");
                // $("#bindType").val("1");
                // $("#bindLimit").val("100");
                // $("#bindValidity").val("180");
                // $("#downlaodLimit").val("30");
                // $("#bindAuthority").val("resource");
                // $("input[name='resourceType']").prop("checked",false);
                // $("#resourceInherited").prop("checked",true);
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
        var downlaodLimit = $("#downlaodLimit").val();
        //绑定个人继承权限
        var bindAuthority = $("#bindAuthority").val();
        if(bool){
            return ;
        }
        $.ajax({
            type : "post",
            url : "../bindAuhtority/openAuthority",
            data:{
                userId:mechanism_id,
                bindType:bindType,
                bindLimit:bindLimit,
                bindValidity:bindValidity,
                downlaodLimit:downlaodLimit,
                bindAuthority:bindAuthority,
            },
            success: function(data){
                $("#institution").val("");
                $("input[name='quotaName']").prop("checked",true);
                $(".enshrine").text("全部");
                $("#bindType").val("1");
                $("#bindLimit").val("100");
                $("#bindValidity").val("180");
                $("#downlaodLimit").val("30");
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

