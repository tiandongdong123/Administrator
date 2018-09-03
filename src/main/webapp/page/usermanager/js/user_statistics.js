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
        var content_nav = $(".content_nav a");
        var aRecent7Days = $("#aRecent7Days");
        var lineContainerComparison = $("#lineContainerComparison");
        var lineContainer = $("#lineContainer");
        var target_item_hidden = $(".target_selected .target_item_hidden");

        return {
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
                            })
                        });
                        var selectedVal = selected_data.children(".switch_data_hidden").val();//按日/周/月参数
                        var indexType = target_item_hidden.val();//指标参数
                        //判断对比时间发送不同的请求
                        if (obj.startCompareDate && obj.endCompareDate) {
                            console.log(888)
                            that.comparisonEChartsAjax(obj.startDate, obj.endDate, obj.startCompareDate, obj.endCompareDate, selectedVal, indexType);
                        } else {
                            that.eChartsAjax(obj.startDate, obj.endDate, selectedVal, indexType);
                        }


                    },
                });
                //默认显示最近7天
                aRecent7Days.click();
            },
            //日周月随最近天数的变化
            dayWeekMonth: function (recent) {
                var switch_board_week = $(".switch_board_week");
                var switch_board_month = $(".switch_board_month");
                var week_month = $(".switch_board_week,.switch_board_month");
                if (recent == "昨天") {
                    switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                    switch_data.eq(0).addClass("switch_bg");
                    selected_data.children(".switch_data_hidden").val(1);
                    week_month.show()
                }
                if (recent == "最近7天") {
                    switch_data.removeClass("switch_bg").not(":eq(0)").removeClass("disable_btn");
                    switch_board_week.hide();
                    switch_board_month.show();
                    switch_data.eq(0).addClass("switch_bg");
                    switch_data.eq(2).addClass("disable_btn");
                    selected_data.children(".switch_data_hidden").val(1)
                }
                if (recent == "最近30天") {
                    switch_data.removeClass("switch_bg").eq(0).addClass("switch_bg");
                    switch_data.not(":eq(0)").removeClass("disable_btn");
                    week_month.hide();
                    selected_data.children(".switch_data_hidden").val(1)
                }
            },
            //单个与对比分析图的切换
            dataChecked: function () {
                $(".gri_pc").click(function () {
                    if ($(".gri_pc").get(0).checked) {
                        lineContainerComparison.show();
                        lineContainer.hide();
                    }else {
                        lineContainerComparison.hide();
                        lineContainer.show();
                    }
                });
            },
            //日历确定按钮是否点击
            dataSubmitBtn: function () {
                $(".gri_submit_btn").click(function () {
                    time_quantum.css("background", "transparent")
                })
            },
            //样式的切换(日/周/月的切换时分析图的改变)
            switchBg: function (obj, className) {
                var that = this;
                obj.each(function () {
                    var _this = $(this);
                    _this.click(function () {
                        _this.addClass(className).siblings().removeClass(className);

                        if (obj == switch_data) {
                            selected_data.children(".switch_data_hidden").val(_this.children("input").val());
                            that.getparms();
                        }
                        if (obj == content_nav) {
                            console.log(111111)
                        }

                    })
                })
            },
            //获取开始日期，指标，按日/周/月的参数
            getparms: function () {
                var that = this;
                var startDate, endDate, indexType, data_arry, switch_hidden;
                switch_hidden = selected_data.children(".switch_data_hidden").val();//按日/周/月参数
                data_arry = $("#user_statistics_data").val().split("至");
                startDate = data_arry[0];
                endDate = data_arry[1];
                indexType = target_item_hidden.val();//指标参数
                that.eChartsAjax(startDate, endDate, switch_hidden, indexType);
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
            //各指标的选取分析图的变化
            indexSelect: function () {
                var that = this;
                indexList.each(function () {
                    var _this = $(this), indexListVal;
                    _this.click(function () {
                        indexListVal = $(this).val();
                        target_item.text(indexListVal);
                        target_item_hidden.val(_this.siblings("input").val());
                        that.getparms();
                    })
                })
            },
            //单个分析图公共ajax
            eChartsAjax: function (startDate, endDate, timeUnit, indexType) {
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
                        console.log(data.dateTime[3])
                        myEcharsCommon.commonLine('lineContainer', data.totalData, data.dateTime, data.dateTime[0], data.dateTime[3])
                    },
                    error: function () {
                    }
                });
            },
            //对比分析图公共ajax
            comparisonEChartsAjax: function (startDate, endDate, compareStartDate, compareEndDate, timeUnit, indexType) {
                $.ajax({
                    type: 'post',
                    url: "../userStatistics/compareTotalCharts.do",
                    async: true,
                    data: {
                        "startTime": startDate,
                        "endTime": endDate,
                        "compareStartDate": compareStartDate,
                        "compareEndDate": compareEndDate,
                        "timeUnit": timeUnit,
                        "type": indexType
                    },
                    success: function (data) {
                        console.log(data)
                        /* myEcharsCommon.commonLine('lineContainer',data.totalData,data.dateTime,startDate,endDate)*/
                        /* myEcharsCommon.lineComparison('lineContainerComparison',)*/
                    },
                    error: function () {
                    }
                });
            },

            //总数和新增的切换
            allOrNew: function () {
                new_num.click(function () {
                    new_increased_num.show();
                });
                all_num.click(function () {
                    new_increased_num.hide();
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
            }
        }
    })().init();

    //echarts图
    var myEcharsCommon = (function () {
        return {
            commonLine: function (id, seriesData, dateTime, startValue, endValue) {
                var idObj = document.getElementById(id);
                if (idObj.hasAttribute("_echarts_instance_")) {
                    idObj.removeAttribute("_echarts_instance_");
                }
                $(idObj).empty();
                if (myChart) {
                    myChart.clear();
                }
                var myChart = echarts.init(idObj);
                var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: [{
                            name: '个人用户（单位：元）',
                        }],
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: 20,
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
                                excludeComponents: ['toolbox'],
                                pixelRatio: 2
                            }
                        }
                    },
                    xAxis: {
                        type: 'category',
                        splitLine: {
                            show: false,
                        },
                        axisLabel: {interval:0},//坐标轴刻度标签的显示间隔，
                        splitArea: {
                            show: false
                        },
                        axisTick: {
                            show: false
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
                            name: '个人用户（单位：元）',
                            type: 'line',
                            stack: '总量',
                            data: seriesData,
                            symbolSize: 4,
                            itemStyle: {normal: {label: {show: true}}}
                        }
                    ],
                    dataZoom: {
                        type: 'slider',
                        show: true,
                        backgroundColor: '#e0e3e8',
                        handleColor: '#7cb6e1',
                        fillerColor: '#7cb6e1',
                        start:0,
                        xAxisIndex: 0,
                        end:31 ,
                        left: 10,
                        right: 10,
                        bottom: 4,
                        height: 6,
                        textStyle: {
                            color: 'transparent'
                        },
                        handleSize: 16,
                        showDataShadow:false
                    }
                };

                if (option && typeof option === "object") {
                    myChart.setOption(option, true);
                }

            },

            lineComparison: function (idName) {
                var dom = document.getElementById(idName);
                if (dom.hasAttribute("_echarts_instance_")) {
                    dom.removeAttribute("_echarts_instance_");
                }
                $(dom).empty();
                if (myChart) {
                    myChart.clear();
                }
                var myChart = echarts.init(dom);
                var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['个人用户（单位：元）', '个人用户（对比）（单位：元）'],
                        orient: 'vertical',  //垂直显示
                        selectedMode: 'single',
                        padding: -0.5 //调节legend的位置
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: 100,
                        containLabel: true,
                        show: 'true',
                        borderWidth: '0'
                    },
                    toolbox: {
                        show: true,
                        right: '20',
                        feature: {
                            saveAsImage: {
                                show: true,
                                excludeComponents: ['toolbox'],
                                pixelRatio: 2
                            }
                        }
                    },
                    xAxis: {
                        type: 'category',
                        splitLine: {
                            show: false,
                        },
                        data: ['2018/1/6', '2018/1/7', '2018/1/8', '2018/1/9', '2018/1/10', '2018/1/11', '2018/1/12'],
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
                            name: '个人用户（单位：元）',
                            type: 'line',
                            stack: '总量',
                            data: [820, 932, 901, 934, 1290, 1330, 3320],
                            itemStyle: {normal: {label: {show: true}}}
                        },
                        {
                            name: '个人用户（对比）（单位：元）',
                            type: 'line',
                            stack: '总量',
                            data: [220, 182, 191, 234, 290, 330, 310],
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
    /* myEcharsCommon.commonLine('lineContainer');*/
    /* myEcharsCommon.lineComparison('lineContainerComparison')*/
});
