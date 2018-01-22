var username;
var ip;
var module;
var behavior;
var startTime;
var endTime;

$(function(){
    tabulation();
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
        $(".backdrop").hide();
        $(".pop").hide();
    });
});

/**
 * 加载列表数据--liuYong
 */



function tabulation(curr){
    username=$("#user_id").val();
    ip=$("#institution_name").val();
    module=$("#model").find("option:selected").val();
    behavior=$("#restype").find("option:selected").val();
    startTime=$("#startTime").val();
    endTime=$("#endTime").val();
    $.ajax({
        type:"POST",
        data:{
            "username":username,
            "ip":ip,
            "module":module,
            "behavior":behavior,
            "startTime":startTime,
            "endTime":endTime,
            "pageNum":curr||1,
        },
        url:"../log/getLogJson.do",
        dataType:"json",
        success:function(data){

            var pagerow=data.pageRow;
            var html="";

            if("机构用户信息管理"==module){
                $.each(pagerow, function(i, obj) {
                    html+="<tr>" +
                            "<td>"+(10*(curr||1-1)+i+1)+"</td>" +
                            "<td>"+pagerow[i].username+"</td>" +
                            "<td>"+pagerow[i].ip+"</td>" +
                            "<td>"+timeStamp2String(pagerow[i].time)+"</td>" +
                            "<td>"+pagerow[i].module+"</td>" +
                            "<td>"+pagerow[i].behavior+"</td>" +
                            "<td>"+pagerow[i].userId+"</td>" +
                            "<td>"+pagerow[i].projectname+"</td>" +
                            "<td>"+pagerow[i].totalMoney+"</td>" +
                            "<td>"+pagerow[i].purchaseNumber+"</td>" +
                            "<td>"+pagerow[i].validityStarttime+"</td>" +
                            "<td>"+pagerow[i].validityEndtime+"</td>" +
                            "</tr>";
                });
                $("#tbody_").html(html);
            }else{
                $.each(pagerow, function(i, obj) {
                    html+="<tr>" +
                            "<td>"+(10*(curr||1-1)+i+1)+"</td>" +
                            "<td>"+pagerow[i].username+"</td>" +
                            "<td>"+pagerow[i].ip+"</td>" +
                            "<td>"+timeStamp2String(pagerow[i].time)+"</td>" +
                            "<td>"+pagerow[i].module+"</td>" +
                            "<td>"+pagerow[i].behavior+"</td>" +
                            "<td>"+pagerow[i].operation_content+"</td>" +
                            "</tr>";
                });
                $("#tbody").html(html);
            }

            var pageTotal=data.pageTotal;
            var pageSize=data.pageSize;
            var pages=pageTotal%pageSize==0?pageTotal/pageSize:pageTotal/pageSize+1;
            var groups=pages>=4?4:pages;

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


