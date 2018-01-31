$(function(){
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
        $(".backdrop").hide();
        $(".pop").hide();
    });
});

function inquiry(){
    var userId = $("#userId").val();
    var institutionName = $("#institutionName").val();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    var pageSize = $(".evey-page").val();
    if (pageSize == null) {
        pageSize = 20;
    }
    $.ajax({
        type:"POST",
        data:{
            userId:userId,
            institutionName:institutionName,
            startTime:startTime,
            endTime:endTime,
            pageSize:pageSize,
            page: 1,
        },
        url:"../bindAuhtority/searchBindInfo.do",
        dataType:"json",
        success:function(data){
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


