$(function () {

    var userStatistics = (function () {
        var time_quantum = $(".data_wrapper a");//最近时间段
        var switch_data = $(".switch_data a"); //日/周/月
        var selected_data = $(".switch_data");
        var target_selected = $(".target_selected");//指标
        var target_list = $(".target_list");//供选择的指标
        var target_item = $(".target_item");
        var index_icon = $(".index_icon");
        var indexList = $(".target_list ul li input");
        var all_num = $(".all_num");
        var new_num = $(".new_num");
        var new_increased_num = $(".new_increased_num");
        var content_nav_list = $(".content_nav");
        var content_nav = $(".content_nav a");
        var aRecent7Days = $("#aRecent7Days");
        var lineContainerComparison = $("#lineContainerComparison");
        var lineContainer = $("#lineContainer");
        var target_item_hidden = $(".target_selected .target_item_hidden");
        var target_item = $(".target_item");
        var nameSingle, nameCompare, nameArray;

        return {
            onclick_: function () {
                var that = this;
                time_quantum.each(function () {
                    var _this = $(this);
                    _this.click(function () {
                        //_this.css("background", "#4AA6FC");
                        //_this.siblings().css("background", "transparent");
                        //that.dayWeekMonth(_this.text()); //日周月随最近天数的变化
                        that.totalOrNew();//table表的变化
                        that.newData();

                        /* $(".model").show();*/


                        return;

                    })
                });
            },
            //日历插件
            dataPicker: function () {
                var that = this;
                var dateRange = new pickerDateRange('user_statistics_data', {
                    aYesterday: 'aYesterday', //昨天
                    aRecent7Days: 'aRecent7Days', //最近7天
                    aRecent30Days: 'aRecent30Days', //最近30
                    startDate: '',
                    stopToday: true,
                    isTodayValid: false,
                    endDate: '',
                    startCompareDate: '',
                    endCompareDate: '',
                    needCompare: true,
                    calendars: 3,
                    success: function (obj) {

                        //最近几天背景的切换
                        time_quantum.each(function () {
                            var _this = $(this);
                            _this.click(function () {
                                _this.css("background", "#4AA6FC");
                                _this.siblings().css("background", "transparent");
                                that.dayWeekMonth(_this.text()); //日周月随最近天数的变化
                                /* that.totalOrNew();//table表的变化
                                 that.newData();*/
                                //$(".model").show(200).delay(1000).hide(200);
                                return;

                            })
                        });
                        if (!$(".gri_pc").get(0).checked) {
                            $("#user_statistics_dataCompare").val("")
                        }
                        that.getTime();
                    },
                });
                //默认显示最近7天
                aRecent7Days.click();
                that.newData();
            },
            getTime: function () {
                var that = this;
                var selectedVal, indexType, navTitle, data_compare_arry, startCompareDate, endCompareDate, data_arry,
                    startDate, endDate;
                selectedVal = selected_data.children(".switch_data_hidden").val();//按日/周/月参数
                indexType = target_item_hidden.val();//指标参数
                navTitle = $("#contentNavHidden").val();//总数/新增参数

                data_arry = $("#user_statistics_data").val().split("至");
                startDate = data_arry[0];
                endDate = data_arry[1].trim();

                data_compare_arry = $("#user_statistics_dataCompare").val().split("至");
                startCompareDate = data_compare_arry[0];
                endCompareDate = data_compare_arry[1] ? data_compare_arry[1].trim() : "";
                //判断对比时间发送不同的请求
                if (startCompareDate && endCompareDate) {
                    if (navTitle == 'total') {
                        that.comparisonEChartsAjax(startDate, endDate, startCompareDate, endCompareDate, selectedVal, indexType);
                    }
                    if (navTitle == 'new') {
                        that.newComparisonEChartsAjax(startDate, endDate, startCompareDate, endCompareDate, selectedVal, indexType);
                    }
                } else {
                    if (navTitle == 'total') {
                        that.eChartsAjax(startDate, endDate, selectedVal, indexType);
                    }
                    if (navTitle == 'new') {
                        that.newEChartsAjax(startDate, endDate, selectedVal, indexType);
                    }
                }
            },
            //最近天数的重置
            recentReset: function (id) {
                selected_data.children(".switch_data_hidden").val(1);
                $(".gri_data_compare").hide();
                if ($("#user_statistics_dataCompare").val() != "") {
                    $("#" + id).click();
                }
                lineContainerComparison.hide();
                lineContainer.show();
            },
            //禁止多次发送
            ajaxPrefilterEvent: function (url) {
                var pendingRequests = {};
                $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
                    var key = options.url;
                    if (key == url) {
                        if (!pendingRequests[key]) {
                            pendingRequests[key] = jqXHR;
                        } else {
                            pendingRequests[key].abort(); // 放弃先触发的提交
                        }
                    }
                });
            },
            //日周月随最近天数的变化
            dayWeekMonth: function (recent) {
                var that = this;
                var switch_board_week = $(".switch_board_week");
                var switch_board_month = $(".switch_board_month");
                var week_month = $(".switch_board_week,.switch_board_month");

                $(".gri_data_compare").hide();
                selected_data.children(".switch_data_hidden").val(1);


                if (recent == "昨天") {
                    switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                    switch_data.eq(0).addClass("switch_bg");
                    week_month.show();
                    that.recentReset("aYesterday");
                    return;
                }
                if (recent == "最近7天") {
                    switch_data.removeClass("switch_bg").not(":eq(0)").removeClass("disable_btn");
                    switch_board_week.hide();
                    switch_board_month.show();
                    switch_data.eq(0).addClass("switch_bg");
                    switch_data.eq(2).addClass("disable_btn");
                    that.recentReset("aRecent7Days");
                    return;
                }
                if (recent == "最近30天") {

                    switch_data.removeClass("switch_bg").eq(0).addClass("switch_bg");
                    switch_data.not(":eq(0)").removeClass("disable_btn");
                    week_month.hide();
                    that.recentReset("aRecent30Days");
                    return;
                }
            },
            //单个与对比分析图的切换(点击对比)
            dataChecked: function () {
                $(".gri_pc").click(function () {
                    if ($(".gri_pc").get(0).checked) {
                        lineContainerComparison.show();
                        lineContainer.hide();
                        $(".gri_data_compare").show();

                    } else {
                        lineContainerComparison.hide();
                        lineContainer.show();
                        $(".gri_data_compare").css('background-position', '-10px -10px');
                        $(".gri_data_compare").hide();
                    }
                });
                //日历图标的变化
                $("#user_statistics_data").click(function () {
                    $(".gri_data").css('background-position', '-68px -10px');
                });
                $("#user_statistics_dataCompare").click(function () {
                    $(".gri_data_compare").css('background-position', '-68px -10px');
                })
            },
            //日历确定/取消按钮是否点击
            dataSubmitBtn: function () {
                var that = this;
                var data_icon = $(".gri_data_compare,.gri_data");

                $(".gri_submit_btn").click(function () {
                    time_quantum.css("background", "transparent");
                    data_icon.css('background-position', '-10px -10px');
                    that.totalOrNew();
                    that.newData();
                    $(".switch_data a").not(":eq(0)").removeClass("disable_btn");
                    $(".switch_board_week").hide();
                    $(".switch_board_month").hide();
                });

                $(".closeBtn").click(function () {
                    data_icon.css('background-position', '-10px -10px');
                });
            },
            //点击icon日历的显示与隐藏
            griIcon: function (iconBtn, dataInput) {
                var coun = 0;
                $("." + iconBtn).click(function (event) {
                    event.stopPropagation();
                    console.log(iconBtn)
                    coun++;
                    if (coun % 2 == 0) {
                        $(".closeBtn").click();
                        $(this).css('background-position', '-10px -10px');
                    } else {
                        $("#" + dataInput).click();
                    }
                })
            },
            //样式的切换(日/周/月的切换时分析图的改变)
            switchBg: function (obj, className) {
                var that = this;
                var data_arry, startDate, endDate
                obj.each(function () {
                    var _this = $(this);
                    _this.click(function () {
                        _this.addClass(className).siblings().removeClass(className);//样式的变化
                        //判断是总数/新增OR按日/周/月
                        if (obj == switch_data) {
                            selected_data.children(".switch_data_hidden").val(_this.children("input").val());
                            that.totalOrNew();
                        }
                        if (obj == content_nav) {
                            content_nav_list.children("#contentNavHidden").val(_this.children("input").val());
                        }
                        that.getTime();

                    })
                })
            },
            //新增数据的显示
            newData: function () {
                var startDate, endDate, data_arry;
                var that = this;
                data_arry = $("#user_statistics_data").val().split("至");
                startDate = data_arry[0];
                endDate = data_arry[1].trim();
                that.ajaxPrefilterEvent("../userStatistics/newDataSum.do");
                $.ajax({
                    type: "POST",
                    data: {
                        "startTime": startDate,
                        "endTime": endDate,
                    },
                    url: "../userStatistics/newDataSum.do",
                    success: function (data) {
                        $(".new_data").html("");
                        for (var i in data) {
                            var str = '<li><span class="person_num">' + data[i] + '</span> </li>';
                            $(".new_data").append(str);
                        }
                    }
                });
            },
            //手动输入时间的校验
            testData: function () {
                var dateRangeSelected;
                $(".test_data").focus(function () {
                    dateRangeSelected = $(this).val();
                });
                $(".test_data").blur(function () {
                    var test_data_now = $(this).val();
                    var test_result = test_data_now.match(/^(\d{4})(-)(\d{2})(-)(\d{2})$/);
                    if (test_result == null) {
                        alert("请输入正确的开始时间格式,如:2018-01-01");
                        $(this).val(dateRangeSelected)
                    }
                });
            },
            //点击指标弹框&空白隐藏
            indexMothed: function (clickDom, ComboBox, icon) {
                var that = this;
                clickDom.on("click", function (e) {
                    e = e || window.event;
                    e.stopPropagation();
                    ComboBox.toggle();
                    that.triangle(ComboBox, icon);//三角图标的变化
                    /*点击空白，下拉框隐藏*/
                    $(document).on("click", function (e) {
                        var target = $(e.target);
                        if (target.closest(ComboBox).length == 0) {
                            ComboBox.hide();
                            that.triangle(ComboBox, icon);//三角图标的变化
                        }
                    })
                })
            },
            /*指标图标变化*/
            triangle: function (className, icon) {
                if (className.css('display') == 'none') {//如果当前隐藏
                    icon.css('background-position', '-10px -10px')
                }
                if (className.css('display') == 'block') {
                    icon.css('background-position', '-68px -10px');
                }
            },
            //总数/新增table表的变化
            totalOrNew: function () {
                var that = this;
                if ($("#contentNavHidden").val() == "total") {
                    that.totalTable();
                }
                if ($("#contentNavHidden").val() == "new") {
                    that.newTable();
                }
            },
            //各指标的选取分析图的变化
            indexSelect: function () {
                var that = this;
                indexList.each(function () {
                    var _this = $(this), indexListVal;
                    _this.on("click", function () {
                        indexListVal = $(this).val();
                        target_item.text(indexListVal);
                        target_item_hidden.val(_this.siblings("input").val());
                        /*  that.getparms();*/
                        that.getTime();
                    })
                })
            },
            //单个分析图公共ajax
            eChartsAjax: function (startDate, endDate, timeUnit, indexType) {
                var that = this;
                that.ajaxPrefilterEvent("../userStatistics/totalCharts.do");
                $.ajax({
                    type: 'post',
                    url: '../userStatistics/totalCharts.do',
                    async: true,
                    data: {
                        "startTime": startDate,
                        "endTime": endDate,
                        "timeUnit": timeUnit,
                        "type": indexType
                    },
                    success: function (data) {
                        if (data.totalData) {
                            var totalData = data.totalData.map(function (value) {
                                value = value > 10000 ? (((value - value % 100) / 10000)) : value;
                                return value;
                            });
                            if (data.totalData.join("") == totalData.join("")) {
                                nameSingle = target_item.text() + " " + "(单位：个)";
                            } else {
                                nameSingle = target_item.text() + " " + "(单位：万)";
                            }
                        }
                        myEcharsCommon.commonLine("lineContainer", totalData, data.dateTime, nameSingle)
                    },
                });
            },
            //对比分析图公共ajax
            comparisonEChartsAjax: function (startDate, endDate, compareStartDate, compareEndDate, timeUnit, indexType) {
                var that = this;
                that.ajaxPrefilterEvent("../userStatistics/compareTotalCharts.do");
                $.ajax({
                    type: 'post',
                    url: "../userStatistics/compareTotalCharts.do",
                    async: true,
                    data: {
                        "startTime": startDate,
                        "endTime": endDate,
                        "compareStartTime": compareStartDate,
                        "compareEndTime": compareEndDate,
                        "timeUnit": timeUnit,
                        "type": indexType
                    },
                    success: function (data) {
                        if (data.selectData) {
                            var selectData = data.selectData.map(function (value) {
                                value = value > 10000 ? (((value - value % 100) / 10000)) : value;
                                return value;
                            });
                            if (data.selectData.join("") == selectData.join("")) {
                                nameSingle = target_item.text() + " " + "(单位：个)" + " " + $("#user_statistics_data").val();
                            } else {
                                nameSingle = target_item.text() + " " + "(单位：万)" + " " + $("#user_statistics_data").val();
                            }
                        }
                        if (data.compareData) {
                            var compareData = data.compareData.map(function (value) {
                                value = value > 10000 ? (((value - value % 100) / 10000)) : value;
                                return value;
                            });
                            if (data.compareData.join("") == compareData.join("")) {
                                nameCompare = target_item.text() + " " + "(单位：个)" + "" + $("#user_statistics_dataCompare").val();
                            } else {
                                nameCompare = target_item.text() + " " + "(单位：万)" + "" + $("#user_statistics_dataCompare").val();
                            }
                        }
                        nameArray = new Array();
                        nameArray[0] = nameSingle;
                        nameArray[1] = nameCompare;
                        myEcharsCommon.lineComparison('lineContainerComparison', selectData, compareData, data.dateTime, nameArray, nameSingle, nameCompare)
                    },
                });
            },
            //新增折线图公共ajax
            newEChartsAjax: function (startDate, endDate, timeUnit, indexType) {
                var that = this;
                that.ajaxPrefilterEvent("../userStatistics/newCharts.do");
                $.ajax({
                    type: 'post',
                    url: '../userStatistics/newCharts.do',
                    async: true,
                    data: {
                        "startTime": startDate,
                        "endTime": endDate,
                        "timeUnit": timeUnit,
                        "type": indexType
                    },
                    success: function (data) {
                        if (data.totalData) {
                            var totalData = data.totalData.map(function (value) {
                                value = value > 10000 ? (((value - value % 100) / 10000)) : value;
                                return value;
                            });
                            if (data.totalData.join("") == totalData.join("")) {
                                nameSingle = target_item.text() + " " + "(单位：个)";
                            } else {
                                nameSingle = target_item.text() + " " + "(单位：万)";
                            }
                        }
                        myEcharsCommon.commonLine('lineContainer', totalData, data.dateTime, nameSingle)
                    },
                });
            },
            //新增对比折线图公共ajax
            newComparisonEChartsAjax: function (startDate, endDate, compareStartDate, compareEndDate, timeUnit, indexType) {
                var that = this;
                that.ajaxPrefilterEvent("../userStatistics/compareNewCharts.do");
                $.ajax({
                    type: 'post',
                    url: "../userStatistics/compareNewCharts.do ",
                    async: true,
                    data: {
                        "startTime": startDate,
                        "endTime": endDate,
                        "compareStartTime": compareStartDate,
                        "compareEndTime": compareEndDate,
                        "timeUnit": timeUnit,
                        "type": indexType
                    },
                    success: function (data) {

                        if (data.selectData) {
                            var selectData = data.selectData.map(function (value) {
                                value = value > 10000 ? (((value - value % 100) / 10000)) : value;
                                return value;
                            });
                            if (data.selectData.join("") == selectData.join("")) {
                                nameSingle = target_item.text() + " " + "(单位：个)" + " " + $("#user_statistics_data").val();
                            } else {
                                nameSingle = target_item.text() + " " + "(单位：万)" + " " + $("#user_statistics_data").val();
                            }
                        }
                        if (data.compareData) {
                            var compareData = data.compareData.map(function (value) {
                                value = value > 10000 ? (((value - value % 100) / 10000)) : value;
                                return value;
                            });
                            if (data.compareData.join("") == compareData.join("")) {
                                nameCompare = target_item.text() + " " + "(单位：个)" + "" + $("#user_statistics_dataCompare").val();
                            } else {
                                nameCompare = target_item.text() + " " + "(单位：万)" + "" + $("#user_statistics_dataCompare").val();
                            }
                        }
                        nameArray = new Array();
                        nameArray[0] = nameSingle;
                        nameArray[1] = nameCompare;

                        myEcharsCommon.lineComparison('lineContainerComparison', selectData, compareData, data.dateTime, nameArray, nameSingle, nameCompare)
                    },
                    error: function () {
                    }
                });
            },
            //总数table表
            totalTable: function () {
                var data_arry, startDate, endDate, indexType, timeUnit, pageSize,data_compare_arry,startCompareDate,endCompareDate;
                var that = this;
                data_arry = $("#user_statistics_data").val().split("至");
                startDate = data_arry[0].trim();
                endDate = data_arry[1].trim();
                indexType = target_item_hidden.val();//指标参数
                timeUnit = selected_data.children(".switch_data_hidden").val();//按日/周/月参数

                data_compare_arry = $("#user_statistics_dataCompare").val().split("至");
                startCompareDate = data_compare_arry[0];
                endCompareDate = data_compare_arry[1] ? data_compare_arry[1].trim() : "";



                pageSize = $(".evey-page").val();
                if (pageSize == null) {
                    pageSize = 20;
                }
                prevNum = 0;

                that.ajaxPrefilterEvent("../userStatistics/totalDatasheets.do");
                $.ajax({
                    type: "POST",
                    data: {
                        startTime: startDate,
                        endTime: endDate,
                        compareStartTime:startCompareDate,
                        compareEndTime:endCompareDate,
                        timeUnit: timeUnit,
                        type: indexType,
                        pageSize: pageSize,
                        page: 1,
                        sort: 0
                    },
                    url: "../userStatistics/totalDatasheets.do",
                    success: function (data) {
                        $('.sync-html').html(data);
                    }
                });

            },
            //新增table表
            newTable: function () {
                var data_arry, startDate, endDate, indexType, timeUnit, pageSize,data_compare_arry,startCompareDate,endCompareDate;
                var that = this;
                data_arry = $("#user_statistics_data").val().split("至");
                startDate = data_arry[0].trim();
                endDate = data_arry[1].trim();
                indexType = target_item_hidden.val();//指标参数
                timeUnit = selected_data.children(".switch_data_hidden").val();//按日/周/月参数

                data_compare_arry = $("#user_statistics_dataCompare").val().split("至");
                startCompareDate = data_compare_arry[0];
                endCompareDate = data_compare_arry[1] ? data_compare_arry[1].trim() : "";

                pageSize = $(".evey-page").val();
                if (pageSize == null) {
                    pageSize = 20;
                }
                prevNum = 0;
                that.ajaxPrefilterEvent("../userStatistics/newDatasheets.do");
                $.ajax({
                    type: "POST",
                    data: {
                        startTime: startDate,
                        endTime: endDate,
                        compareStartTime:startCompareDate,
                        compareEndTime:endCompareDate,
                        timeUnit: timeUnit,
                        type: indexType,
                        pageSize: pageSize,
                        page: 1,
                        sort: 0
                    },
                    url: "../userStatistics/newDatasheets.do",
                    success: function (data) {
                        $('.sync-html').html(data);
                    }
                });

            },
            //总数和新增的切换
            allOrNew: function () {
                var that = this;
                new_num.click(function () {
                    new_increased_num.show();
                    $(".valid_institution_account").hide();
                    that.newTable();
                    that.getTime();
                });
                all_num.click(function () {
                    new_increased_num.hide();
                    $(".valid_institution_account").show();
                    that.totalTable();
                    that.getTime();
                })
            },
            init: function () {
                this.dataPicker();
                this.switchBg(switch_data, 'switch_bg');
                this.switchBg(content_nav, 'active');
                this.indexMothed(target_selected, target_list, index_icon);
                this.indexSelect();
                this.allOrNew();
                this.dataChecked();
                this.dataSubmitBtn();
                this.totalTable();
                this.testData();
                this.griIcon("gri_data", "user_statistics_data");
                this.griIcon("gri_data_compare", "user_statistics_dataCompare");
                this.onclick_();
            }
        }
    })().init();
});
//echarts图
var myEcharsCommon = (function () {
    return {
        commonLine: function (id, seriesData, dateTime, nameSingle) {
            console.log(nameSingle)
            var idObj = document.getElementById(id);
            if (idObj.hasAttribute("_echarts_instance_")) {
                idObj.removeAttribute("_echarts_instance_");
            }
            $(idObj).empty();
            var myChart = echarts.init(idObj);
            myChart.clear();
            var option = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    y: "bottom",
                    data: [{
                        name: nameSingle,
                    }],
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    height: '300',
                    y: 80,
                    containLabel: true,
                    show: true,
                    borderWidth: '0'
                },
                toolbox: {
                    show: true,
                    right: '20',
                    feature: {
                        saveAsImage: {
                            show: true,
                        }
                    }
                },
                xAxis: {
                    type: 'category',
                    splitLine: {
                        show: false,
                    },
                    boundaryGap: false,
                    axisTick: {
                        alignWithLabel: true
                    },
                    data: dateTime,

                },
                yAxis: {
                    show: false,//隐藏坐标轴
                    type: 'value',
                    splitLine: {
                        show: false,
                    },
                    axisTick: {
                        show: false
                    },
                },
                series: [
                    {
                        name: nameSingle,
                        type: 'line',
                        data: seriesData,
                        symbolSize: 4,
                        itemStyle: {normal: {label: {show: true}}}
                    }
                ]
            };
            if (option && typeof option === "object") {
                myChart.setOption(option, true);
            }
            window.onresize = function () {
                myChart.resize();//若有多个图表变动，可多写

            }


        },

        lineComparison: function (idName, selectData, compareData, dateTime, nameArray, nameSingle, nameCompare) {
            console.log(nameArray);
            console.log(nameSingle);
            console.log(nameCompare);
            var dom = document.getElementById(idName);
            if (dom.hasAttribute("_echarts_instance_")) {
                dom.removeAttribute("_echarts_instance_");
            }
            $(dom).empty();
            var myChart = echarts.init(dom);
            myChart.clear();
            var option = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: nameArray,
                    y: "bottom",
                    orient: 'vertical',  //垂直显示
                    selectedMode: 'single',
                    padding: 10 //调节legend的位置
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    y: 60,
                    height: '300',
                    containLabel: true,
                    show: 'true',
                    borderWidth: '0'
                },
                toolbox: {
                    show: true,
                    right: '20',
                    feature: {
                        saveAsImage: {
                            show: true
                        }
                    }
                },
                xAxis: {
                    type: 'category',
                    position: 'bottom',
                    offset: '20',
                    splitLine: {
                        show: false,
                    },
                    axisLabel: {
                        interval: 'auto',//坐标轴刻度标签的显示间隔，
                        formatter: function (value) {//坐标过长时省略
                            var v = value.substring(0, 10) + '...';
                            return value.length > 10 ? v : value;
                        }
                    },
                    data: dateTime,
                },
                yAxis: {
                    show: false,//隐藏坐标轴
                    type: 'value',
                    splitLine: {
                        show: false,
                    }
                },
                series: [
                    {
                        name: nameSingle,
                        type: 'line',
                        data: selectData,
                        itemStyle: {normal: {label: {show: true}}}
                    },
                    {
                        name: nameCompare,
                        type: 'line',
                        data: compareData,
                        itemStyle: {normal: {label: {show: true}}}
                    }
                ]
            };
            if (option && typeof option === "object") {
                myChart.setOption(option, true);
            }
        },

    }
})();
