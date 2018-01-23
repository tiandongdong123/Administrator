/**
 * Created by 01 on 2018/1/18.
 */
$(document).ready(function(){
    $("#institution").blur(function(){
        var institution = $("#institution").val();
        $.ajax({
            type : "post",
            url : "../bindAuhtority/userId",
            data:{
                institution: institution,
            },
            success: function(data){

            }
        });
    })
});
function submitNew(){
    //机构名称
    var institution = $("#institution").val();
    //机构ID
    var mechanism_id = new Array();
    $("input[class='index']:checked").each(function () {
        mechanism_id.push($(this).val());
    });
    alert(mechanism_id);
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
    $.ajax({
        type : "post",
         url : "../bindAuhtority/openAuthority",
        data:{
            institution:institution,
            mechanism_id:mechanism_id,
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
        }
     });
}
