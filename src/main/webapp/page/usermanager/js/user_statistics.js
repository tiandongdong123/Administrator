$(function () {
    var userStatistics = (function () {
        var time_quantum = $(".data_wrapper a");//最近时间段
        var switch_data = $(".switch_data a"); //日/周/月
        var selected_data = $(".switch_data"); //日/周/月wrapper
        var target_selected = $(".target_selected");//指标
        var target_list = $(".target_list");//供选择的指标
        var index_icon = $(".index_icon");//指标图标
        var indexList = $(".target_list ul li input");//各个指标checked框
        var all_num = $(".all_num");//总数
        var new_num = $(".new_num");//新增
        var new_increased_num = $(".new_increased_num");//新增数目展示框
        var content_nav_list = $(".content_nav");//总数/新增wrap
        var content_nav = $(".content_nav a");
        var aRecent7Days = $("#aRecent7Days");//最近7天
        var lineContainer = $("#lineContainer");//单个分析图wrap
        var target_item_hidden = $(".target_selected .target_item_hidden");//选取指标隐藏域
        var target_item = $(".target_item");//显示指标
        var switch_board_week = $(".switch_board_week");//周的遮罩层
        var switch_board_month = $(".switch_board_month");//月的遮罩层
        var week_month = $(".switch_board_week,.switch_board_month");
        var nameSingle, nameCompare, nameArray;//单个/对比/分析图title


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
                                _this.css("color", "#fff");
                                _this.siblings().css("color", "#000");
                                _this.siblings().css("background", "transparent");
                                $(".switch_data a").eq(0).css("color","#fff");
                                $(".switch_data a").eq(0).siblings().css("color","#000");
                                that.dayWeekMonth(_this.text()); //日周月随最近天数的变化
                                return;
                            })
                        });
                        if (!$(".gri_pc").get(0).checked) {
                            $("#user_statistics_dataCompare").val("")
                        }
                        $(".switch_data_hidden").val(1);
                        that.getTime();
                    },
                });
                aRecent7Days.click();
                that.newData();
            },
            //点击最近天数table/新增指标的变化
            recentDays: function () {
                var that = this;
                time_quantum.each(function () {
                    var _this = $(this);
                    _this.click(function () {
                        that.totalOrNew();//table表的变化
                        that.newData();
                        if (!$(".gri_pc").get(0).checked) {
                            $("#user_statistics_dataCompare").val("")
                        }
                        that.getTime();
                        return;
                    })
                });
            },
            getTime: function () {
                var that = this;
                var selectedVal, indexType, navTitle, data_compare_array, startCompareDate, endCompareDate, data_array,
                    startDate, endDate;
                selectedVal = selected_data.children(".switch_data_hidden").val();//按日/周/月参数
                indexType = target_item_hidden.val();//指标参数
                navTitle = $("#contentNavHidden").val();//总数/新增参数
                data_array = $("#user_statistics_data").val().split("至");
                startDate = data_array[0].trim();
                endDate = data_array[1].trim();
                data_compare_array = $("#user_statistics_dataCompare").val().split("至");
                startCompareDate = data_compare_array[0].trim();
                endCompareDate = data_compare_array[1] ? data_compare_array[1].trim() : "";
                //判断对比时间发送不同的请求
                if (startCompareDate && endCompareDate) {
                    (navTitle == 'total') ? that.comparisonEChartsAjax(startDate, endDate, startCompareDate, endCompareDate, selectedVal, indexType) : that.newComparisonEChartsAjax(startDate, endDate, startCompareDate, endCompareDate, selectedVal, indexType);
                } else {
                    (navTitle == 'total') ? that.eChartsAjax(startDate, endDate, selectedVal, indexType) : that.newEChartsAjax(startDate, endDate, selectedVal, indexType);
                }
            },
            //最近天数的重置
            recentReset: function (id) {
                selected_data.children(".switch_data_hidden").val(1);
                $(".gri_data_compare").hide();
                if ($("#user_statistics_dataCompare").val() != "") {
                    $("#" + id).click();
                }
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
            //判断是否跨周/跨月
            stepMonthOrWeek: function () {
                var data_arry, startDate, endDate, startArray_data, endArray_data, myDate, differDay, endMyDate;
                data_arry = $("#user_statistics_data").val().split("至");
                startDate = data_arry[0].trim();
                endDate = data_arry[1].trim();
                startArray_data = startDate.split("-");
                endArray_data = endDate.split("-");
                myDate = new Date(startArray_data[0], startArray_data[1] - 1, startArray_data[2]);
                endMyDate = new Date(endArray_data[0], endArray_data[1] - 1, endArray_data[2]);
                myDate.getDay();
                differDay = parseInt((new Date(endDate).getTime() - new Date(startDate).getTime()) / (1000 * 60 * 60 * 24));
                if (myDate.getTime() == endMyDate.getTime()) {
                    switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                    switch_data.eq(0).addClass("switch_bg");
                    week_month.show();
                    return;
                }
                if ((myDate.getDay() + differDay) <= 6) {
                    if (myDate.getDay() == 0) {
                        //跨周
                        switch_data.removeClass("switch_bg").not(":eq(0)").removeClass("disable_btn");
                        switch_board_week.hide();
                        switch_board_month.show();
                        switch_data.eq(0).addClass("switch_bg");
                        switch_data.eq(2).addClass("disable_btn");
                        return;
                    } else {
                        if (startArray_data[1] != endArray_data[1]) {
                            //跨月（不跨周）
                            switch_data.eq(1).addClass("disable_btn");
                            switch_data.eq(2).removeClass("disable_btn");
                            switch_board_week.show();
                            switch_board_month.hide();
                            return;
                        }
                        //按日
                        switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                        switch_data.eq(0).addClass("switch_bg");
                        week_month.show();
                        return;
                    }
                }
                if ((myDate.getDay() + differDay) >= 7) {
                    if (startArray_data[1] != endArray_data[1]) {
                        //跨月
                        switch_data.removeClass("switch_bg").eq(0).addClass("switch_bg");
                        switch_data.not(":eq(0)").removeClass("disable_btn");
                        week_month.hide();
                        return;
                    }
                    if (myDate.getDay() == 1 && differDay == 6) {
                        //按日
                        switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                        switch_data.eq(0).addClass("switch_bg");
                        week_month.show();
                        return;
                    } else {
                        //跨周(起点不是周一)
                        switch_data.removeClass("switch_bg").not(":eq(0)").removeClass("disable_btn");
                        switch_board_week.hide();
                        switch_board_month.show();
                        switch_data.eq(0).addClass("switch_bg");
                        switch_data.eq(2).addClass("disable_btn");
                        return;
                    }
                } else {//小于七
                    if (startArray_data[1] != endArray_data[1]) {
                        //跨月
                        switch_data.removeClass("switch_bg").eq(0).addClass("switch_bg");
                        switch_data.not(":eq(0)").removeClass("disable_btn");
                        week_month.hide();
                        return;
                    }
                    //按日
                    switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                    switch_data.eq(0).addClass("switch_bg");
                    week_month.show();
                    return;
                }
            },
            stepMonthOrWeekCompare: function () {
                var data_arry, startDate, endDate, startArray_data, endArray_data, myDate, differDay, endMyDate;
                data_arry = $("#user_statistics_dataCompare").val().split("至");
                startDate = data_arry[0].trim();
                endDate = data_arry[1].trim();
                startArray_data = startDate.split("-");
                endArray_data = endDate.split("-");
                myDate = new Date(startArray_data[0], startArray_data[1] - 1, startArray_data[2]);
                endMyDate = new Date(endArray_data[0], endArray_data[1] - 1, endArray_data[2]);
                myDate.getDay();
                differDay = parseInt((new Date(endDate).getTime() - new Date(startDate).getTime()) / (1000 * 60 * 60 * 24));
                if (myDate.getTime() == endMyDate.getTime()) {
                    switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                    switch_data.eq(0).addClass("switch_bg");
                    week_month.show();
                    return;
                }
                if ((myDate.getDay() + differDay) <= 6) {
                    if (myDate.getDay() == 0) {
                        //跨周
                        switch_data.removeClass("switch_bg").not(":eq(0)").removeClass("disable_btn");
                        switch_board_week.hide();
                        switch_board_month.show();
                        switch_data.eq(0).addClass("switch_bg");
                        switch_data.eq(2).addClass("disable_btn");
                        return;
                    } else {
                        if (startArray_data[1] != endArray_data[1]) {
                            //跨月
                            switch_data.eq(1).addClass("disable_btn");
                            switch_data.eq(2).removeClass("disable_btn");
                            switch_board_week.show();
                            switch_board_month.hide();
                            return;
                        }
                        //按日
                        switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                        switch_data.eq(0).addClass("switch_bg");
                        week_month.show();
                        return;
                    }
                }
                if ((myDate.getDay() + differDay) >= 7) {
                    if (startArray_data[1] != endArray_data[1]) {
                        //跨月
                        switch_data.removeClass("switch_bg").eq(0).addClass("switch_bg");
                        switch_data.not(":eq(0)").removeClass("disable_btn");
                        week_month.hide();
                        return;
                    }
                    if (myDate.getDay() == 1 && differDay == 6) {
                        //按日
                        switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                        switch_data.eq(0).addClass("switch_bg");
                        week_month.show();
                        return;
                    } else {
                        //跨周(起点不是周一)
                        switch_data.removeClass("switch_bg").not(":eq(0)").removeClass("disable_btn");
                        switch_board_week.hide();
                        switch_board_month.show();
                        switch_data.eq(0).addClass("switch_bg");
                        switch_data.eq(2).addClass("disable_btn");
                        return;
                    }
                } else {//小于七
                    if (startArray_data[1] != endArray_data[1]) {
                        //跨月
                        switch_data.removeClass("switch_bg").eq(0).addClass("switch_bg");
                        switch_data.not(":eq(0)").removeClass("disable_btn");
                        week_month.hide();
                        return;
                    }
                    //按日
                    switch_data.removeClass("switch_bg").not(":eq(0)").addClass("disable_btn");
                    switch_data.eq(0).addClass("switch_bg");
                    week_month.show();
                    return;
                }
            },
            dayWeekMonth: function (recent) {
                var that = this;
                $(".gri_data_compare").hide();
                selected_data.children(".switch_data_hidden").val(1);
                if (recent == "昨天") {
                    that.stepMonthOrWeek();//显示按日
                    that.recentReset("aYesterday");
                    return;
                }
                if (recent == "最近7天") {
                    that.stepMonthOrWeek();//看是否跨周
                    that.recentReset("aRecent7Days");
                    return;
                }
                if (recent == "最近30天") {
                    that.stepMonthOrWeek();//看是否跨月
                    that.recentReset("aRecent30Days");
                    return;
                }
            },
            //单个与对比分析图的切换(点击对比)
            dataChecked: function () {
                var that = this, week = false, month = false, week_compare = false, month_compare = false;
                $(".gri_pc").click(function () {
                    selected_data.children(".switch_data_hidden").val(1);
                    $(".switch_data a").eq(0).css("color","#fff");
                    $(".switch_data a").eq(0).siblings().css("color","#000");
                    that.getTime();
                    if ($(".gri_pc").get(0).checked) {
                        that.totalOrNew();
                        $(".gri_data_compare").show();
                        week = (switch_board_week.css("display") == "none") ? true : false;
                        month = (switch_board_month.css("display") == "none") ? true : false;
                        that.stepMonthOrWeekCompare();
                        week_compare = (switch_board_week.css("display") == "none") ? true : false;
                        month_compare = (switch_board_month.css("display") == "none") ? true : false;
                        if (week && week_compare) {
                            switch_data.eq(1).removeClass("disable_btn");
                            switch_board_week.hide();
                        } else {
                            switch_data.eq(1).addClass("disable_btn");
                            switch_board_week.show();
                        }
                        if (month && month_compare) {
                            switch_data.eq(2).removeClass("disable_btn");
                            switch_board_month.hide();
                        } else {
                            switch_data.eq(2).addClass("disable_btn");
                            switch_board_month.show();
                        }
                    } else {
                        $("#user_statistics_dataCompare").val(" ");
                        that.totalOrNew();
                        $(".gri_data_compare").css('background-position', '-10px -10px');
                        $(".gri_data_compare").hide();
                        that.stepMonthOrWeek();
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
                var that = this, week = false, month = false, week_compare = false, month_compare = false;
                var data_icon = $(".gri_data_compare,.gri_data");
                $(".gri_submit_btn").click(function () {
                    time_quantum.css("background", "transparent");
                    time_quantum.css("color","#000");
                    data_icon.css('background-position', '-10px -10px');
                    $(".switch_data a").eq(0).css("color","#fff");
                    $(".switch_data a").eq(0).siblings().css("color","#000");
                    selected_data.children(".switch_data_hidden").val(1);
                    that.totalOrNew();
                    that.newData();
                    that.getTime();
                    if ($(".gri_submit_btn").parent().siblings().eq(0).css("display") == "inline-block") {
                        that.stepMonthOrWeek();//跨月/周的判断
                        week = (switch_board_week.css("display") == "none") ? true : false;
                        month = (switch_board_month.css("display") == "none") ? true : false;
                        if ($("#user_statistics_dataCompare").val()) {
                            that.stepMonthOrWeekCompare();
                            week_compare = (switch_board_week.css("display") == "none") ? true : false;
                            month_compare = (switch_board_month.css("display") == "none") ? true : false;
                            if (week && week_compare) {
                                switch_data.eq(1).removeClass("disable_btn");
                                switch_board_week.hide();
                            } else {
                                switch_data.eq(1).addClass("disable_btn");
                                switch_board_week.show();
                            }
                            if (month && month_compare) {
                                switch_data.eq(2).removeClass("disable_btn");
                                switch_board_month.hide();
                            } else {
                                switch_data.eq(2).addClass("disable_btn");
                                switch_board_month.show();
                            }
                        }
                        return;
                    }
                    if ($(".gri_submit_btn").parent().siblings().eq(1).css("display") == "inline-block") {
                        that.stepMonthOrWeekCompare();
                        week_compare = (switch_board_week.css("display") == "none") ? true : false;
                        month_compare = (switch_board_month.css("display") == "none") ? true : false;
                        that.stepMonthOrWeek();
                        week = (switch_board_week.css("display") == "none") ? true : false;
                        month = (switch_board_month.css("display") == "none") ? true : false;
                        if (week && week_compare) {
                            switch_data.eq(1).removeClass("disable_btn");
                            switch_board_week.hide();
                        } else {
                            switch_data.eq(1).addClass("disable_btn");
                            switch_board_week.show();
                        }
                        if (month && month_compare) {
                            switch_data.eq(2).removeClass("disable_btn");
                            switch_board_month.hide();
                        } else {
                            switch_data.eq(2).addClass("disable_btn");
                            switch_board_month.show();
                        }
                    }
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
                obj.each(function () {
                    var _this = $(this);
                    _this.click(function () {
                        _this.addClass(className).siblings().removeClass(className);//样式的变化
                        //判断是总数/新增OR按日/周/月
                        if (obj == switch_data) {
                            selected_data.children(".switch_data_hidden").val(_this.children("input").val());
                            _this.css("color", "#fff");
                            _this.siblings().css("color", "#000");
                            that.totalOrNew();
                        }
                        if (obj == content_nav) {
                            content_nav_list.children("#contentNavHidden").val(_this.children("input").val());
                            _this.css("color", "#fff");
                            _this.siblings().css("color", "#000")
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
                startDate = data_arry[0].trim();
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
                            var str = '<li><span class="person_num">' + data[i] + '</span></li>';
                            $(".new_data").append(str);
                        }
                    }
                });
            },
            //手动输入时间的校验
            testData: function (test_data) {
                var dateRangeSelected;
                $("." + test_data).focus(function () {
                    dateRangeSelected = $(this).val();
                });
                $("." + test_data).blur(function () {
                    var test_data_now = $(this).val();
                    var test_result = test_data_now.match(/^(\d{4})(-)(\d{2})(-)(\d{2})$/);
                    if (test_result == null) {
                        (test_data == "dateRangeInputSingle") ? alert("开始日期输入有误，日期格式YYYY-MM-DD") : alert("结束日期输入有误，日期格式YYYY-MM-DD");
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
                ($("#contentNavHidden").val() == "total") ? that.totalTable() : that.newTable();
            },
            //各指标的选取分析图的变化
            indexSelect: function () {
                var that = this;
                indexList.each(function () {
                    var _this = $(this), indexListVal;
                    _this.on("click", function () {
                        indexListVal = $(this).val();
                        target_item.text(indexListVal);
                        (target_item.text() == "有效机构账号") ? $(".target_selected p").show() : $(".target_selected p").hide();
                        target_item_hidden.val(_this.siblings("input").val());
                        that.getTime();
                    })
                })
            },
            // 查找数组中最小值
            arrayMin: function (arrs) {
                var min = arrs[0];
                for (var i = 1, ilen = arrs.length; i < ilen; i += 1) {
                    if (arrs[i] < min) {
                        min = arrs[i];
                    }
                }
                return min;
            },
            // 在数组中查找最大值
            arrayMax: function (arrs) {
                var max = arrs[0];
                for (var i = 1, ilen = arrs.length; i < ilen; i++) {
                    if (arrs[i] > max) {
                        max = arrs[i];
                    }
                }
                return max;
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
                            var flag = 0;
                            data.totalData.map(function (value) {
                                if (value > 10000) {
                                    flag = flag+1;
                                }
                            });
                            if(flag>0){
                                var totalData = data.totalData.map(function (value) {
                                    value = (value - value % 100) / 10000;
                                    return value;
                                });
                            }else{
                                totalData = data.totalData;
                            }
                            nameSingle = (data.totalData.join("") == totalData.join("")) ? (target_item.text() + " " + "(单位：个)") : (target_item.text() + " " + "(单位：万)")
                        }
                        var selectMin = that.arrayMin(totalData);
                        var selectMax = that.arrayMax(totalData);
                        var count = selectMax - selectMin > 0.1 ? 5 : 0.01;
                        selectMin = selectMin - count;
                        if(selectMin<0){
                            selectMin = 0;
                        }
                        myEcharsCommon.commonLine("lineContainer", totalData, data.dateTime, nameSingle, selectMin, selectMax)
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
                            var flag = 0;
                           data.selectData.map(function (value) {
                                if (value > 10000) {
                                    flag = flag+1;
                                }
                            });
                            if(flag>0){
                                var selectData = data.selectData.map(function (value) {
                                    value = (value - value % 100) / 10000;
                                    return value;
                                });
                            }else{
                                selectData = data.selectData;
                            }
                            nameSingle = (data.selectData.join("") == selectData.join("")) ? ($("#user_statistics_data").val() + " " + target_item.text() + "(单位：个)") : ($("#user_statistics_data").val() + " " + target_item.text() + "(单位：万)")
                        }
                        if (data.compareData) {
                            var compareFlag = 0;
                            data.compareData.map(function (value) {
                                if (value > 10000) {
                                    compareFlag = compareFlag+1;
                                }
                            });
                            if(compareFlag>0){
                                var compareData = data.compareData.map(function (value) {
                                    value = (value - value % 100) / 10000;
                                    return value;
                                });
                            }else{
                                compareData = data.compareData
                            }
                            nameCompare = (data.compareData.join("") == compareData.join("")) ? ($("#user_statistics_dataCompare").val() + " " + target_item.text() + "(单位：个)") : ($("#user_statistics_dataCompare").val() + " " + target_item.text() + "(单位：万)")
                        }
                        nameArray = new Array();
                        nameArray[0] = nameSingle;
                        nameArray[1] = nameCompare;
                        var selectMin = that.arrayMin(selectData);
                        var compareMin = that.arrayMin(compareData);
                        var selectMax = that.arrayMax(selectData);
                        var compareMax = that.arrayMax(compareData);
                        if (compareData != 0) {
                            var max = selectMax >= compareMax ? selectMax : compareMax;
                            var min = selectMin >= compareMin ? compareMin : selectMin;
                            var count = Math.abs(selectMin - compareMin) > 0.1 ? 5 : 0.01;
                            var min = min - count;
                        } else {
                            min = selectMin;
                            max = selectMax;
                        }
                        if(min<0){
                            min = 0;
                        }
                        myEcharsCommon.lineComparison('lineContainer', selectData, compareData, data.dateTime, nameArray, nameSingle, nameCompare, max, min)
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
                            var flag = 0;
                           data.totalData.map(function (value) {
                                if (value > 10000) {
                                    flag = flag+1;
                                }
                            });
                            if(flag>0){
                                var totalData = data.totalData.map(function (value) {
                                    value = (value - value % 100) / 10000;
                                    return value;
                                });
                            }else{
                                totalData = data.totalData
                            }
                            nameSingle = (data.totalData.join("") == totalData.join("")) ? (target_item.text() + " " + "(单位：个)") : (target_item.text() + " " + "(单位：万)")
                        }
                        var selectMin = that.arrayMin(totalData);
                        var selectMax = that.arrayMax(totalData);
                        var count = selectMax - selectMin > 0.1 ? 5 : 0.01;
                        count = selectMin - count> 0.01 ? 5 : 0.01;
                        selectMin = selectMin - count;
                        if(selectMin<=0){
                            selectMin = 0;
                        }
                        if(selectMin>100){
                            selectMin = 100;
                        }
                        myEcharsCommon.commonLine('lineContainer', totalData, data.dateTime, nameSingle,selectMin,selectMax)
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
                            var flag = 0;
                             data.selectData.map(function (value) {
                                if (value > 10000) {
                                    flag =flag+1 ;
                                }
                            });
                             if(flag>0){
                                 var selectData = data.selectData.map(function (value) {
                                     value = (value - value % 100) / 10000;
                                     return value;
                                 });
                             }else{
                                 selectData = data.selectData
                             }
                            nameSingle = (data.selectData.join("") == selectData.join("")) ? ($("#user_statistics_data").val() + " " + target_item.text() + "(单位：个)") : ($("#user_statistics_data").val() + " " + target_item.text() + "(单位：万)")
                        }
                        if (data.compareData) {
                            var compareFlag = 0;
                           data.compareData.map(function (value) {
                                if (value > 10000) {
                                    compareFlag = compareFlag+1;
                                }
                            });
                           if(compareFlag>0){
                               var compareData = data.compareData.map(function (value) {
                                   value = (value - value % 100) / 10000;
                                   return value;
                               });
                           }else{
                               compareData = data.compareData;
                           }
                            nameCompare = (data.compareData.join("") == compareData.join("")) ? ($("#user_statistics_dataCompare").val() + " " + target_item.text() + " " + "(单位：个)") : ($("#user_statistics_dataCompare").val() + " " + target_item.text() + " " + "(单位：万)")
                        }
                        nameArray = new Array();
                        nameArray[0] = nameSingle;
                        nameArray[1] = nameCompare;
                        var selectMin = that.arrayMin(selectData);
                        var compareMin = that.arrayMin(compareData);
                        var selectMax = that.arrayMax(selectData);
                        var compareMax = that.arrayMax(compareData);
                        if (compareData != 0) {
                            var max = selectMax >= compareMax ? selectMax : compareMax;
                            var min = selectMin >= compareMin ? compareMin : selectMin;
                            var count = Math.abs(selectMin - compareMin) > 0.1 ? 5 : 0.01;
                            if(min<count){
                                count = 1
                            }
                            var min = min - count;
                        } else {
                            min = selectMin;
                            max = selectMax;
                        }
                        if(min<0){
                            min = 0;
                        }
                        if(min>100){
                            min = 100;
                        }
                        myEcharsCommon.lineComparison('lineContainer', selectData, compareData, data.dateTime, nameArray, nameSingle, nameCompare,max,min)
                    },
                });
            },
            //总数table表
            totalTable: function () {
                var data_array, startDate, endDate, indexType, pageSize, data_compare_array, startCompareDate,
                    endCompareDate;
                var that = this;
                data_array = $("#user_statistics_data").val().split("至");
                startDate = data_array[0].trim();
                endDate = data_array[1].trim();
                indexType = target_item_hidden.val();//指标参数
                data_compare_array = $("#user_statistics_dataCompare").val().split("至");
                startCompareDate = data_compare_array[0].trim();
                endCompareDate = data_compare_array[1] ? data_compare_array[1].trim() : "";
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
                        compareStartTime: startCompareDate,
                        compareEndTime: endCompareDate,
                        timeUnit: 1,
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
                var data_array, startDate, endDate, indexType, pageSize, data_compare_array, startCompareDate,
                    endCompareDate;
                var that = this;
                data_array = $("#user_statistics_data").val().split("至");
                startDate = data_array[0].trim();
                endDate = data_array[1].trim();
                indexType = target_item_hidden.val();//指标参数
                data_compare_array = $("#user_statistics_dataCompare").val().split("至");
                startCompareDate = data_compare_array[0].trim();
                endCompareDate = data_compare_array[1] ? data_compare_array[1].trim() : "";
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
                        compareStartTime: startCompareDate,
                        compareEndTime: endCompareDate,
                        timeUnit: 1,
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
                    if ($(".target_item").text() == "有效机构账号") {
                        $(".target_item").text("个人用户");
                        $(".target_item_hidden").val("person_user");
                        $(".target_selected p").hide();
                    }
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
                this.testData("dateRangeInputSingle");
                this.testData("dateRangeInputCompare");
                this.griIcon("gri_data", "user_statistics_data");
                this.griIcon("gri_data_compare", "user_statistics_dataCompare");
                this.recentDays();
                this.stepMonthOrWeek();//是否跨周/月
            }
        }
    })().init();
});
//echarts图
var myEcharsCommon = (function () {
    return {
        commonLine: function (id, seriesData, dateTime, nameSingle, selectMin, selectMax) {
            (seriesData.length == 0) ? $(".no_data").show() : $(".no_data").hide();
            var idObj = document.getElementById(id);
            if (idObj.hasAttribute("_echarts_instance_")) {
                idObj.removeAttribute("_echarts_instance_");
            }
            $(idObj).empty();
            var myChart = echarts.init(idObj);
            myChart.clear();
            var option = {
                tooltip: {
                    trigger: 'axis',
                    formatter: function (data) {
                        var res;
                        if (data[0].seriesName.split("：")[1].split("")[0] == "万") {
                            res = '<div style="width: 320px;height:45px;">'
                                +'<div style="width:185px;height:15px;margin-left:15px;float:left;">时间</div>'
                                +'<div style="width:114px;height:15px;margin-right:6px;float:right;">'+data[0].seriesName.split("(")[0]+'</div>'+'<br/>'
                                +'<div style="width:200px;height:20px;float: left;">'
                                +'<span style="display:inline-block;margin-top:6px;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#c23531;"></span>'
                                + data[0].axisValueLabel +'</div>'
                                +'<div style="width:115px;height:20px;margin-right:5px;float: right;">' + data[0].value + data[0].seriesName.split("：")[1].split("")[0]+'</div>'
                            '</div>'
                        } else {
                            res = '<div style="width: 320px;height:45px;">'
                                +'<div style="width:185px;height:15px;margin-left:15px;float:left;">时间</div>'
                                +'<div style="width:114px;height:15px;margin-right:6px;float:right;">'+data[0].seriesName.split("(")[0]+'</div>'+'<br/>'
                                +'<div style="width:200px;height:20px;float: left;">'
                                +'<span style="display:inline-block;margin-top:6px;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#c23531;"></span>'
                                + data[0].axisValueLabel +'</div>'
                                +'<div style="width:115px;height:20px;margin-right:5px;float: right;">' + data[0].value+'</div>'
                            '</div>'
                        }
                        return res;
                    }
                },
                legend: {
                    y: "bottom",
                    data: [{
                        name: nameSingle,
                    }],
                },
                grid: {
                    left: '8%',
                    right: '8%',
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
                    min: selectMin,
                    max: selectMax,
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
                        showAllSymbol: false,//不标注所有数据点,
                        symbolSize: 3,
                        label: {
                            show: true,
                            position: 'top',
                            distance: 10
                        },
                    }
                ]
            };
            if (option && typeof option === "object") {
                myChart.setOption(option, true);
                window.onresize = function () { myChart.resize({width:$(".line_wrap").width()}); }
            }
        },

        lineComparison: function (idName, selectData, compareData, dateTime, nameArray, nameSingle, nameCompare, max, min) {
            (selectData.length == 0 && compareData.length == 0) ? $(".no_data").show() : $(".no_data").hide();
            var dom = document.getElementById(idName);
            if (dom.hasAttribute("_echarts_instance_")) {
                dom.removeAttribute("_echarts_instance_");
            }
            $(dom).empty();
            var myChart = echarts.init(dom);
            myChart.clear();

            var option = {
                tooltip: {
                    trigger: 'axis',
                    formatter: function (data) {
                        var res;
                        var dataTime = data[0].axisValueLabel.split("与");
                        var dataTitle = data[0].seriesName.split(" ");
                        if (data[1]) {
                            if (dataTitle[3].split("：")[1].split("")[0] == "万") {
                                res = '<div style="width:320px;height:70px;">'
                                    +'<div style="width:185px;height:15px;margin-left:15px;float:left;">时间</div>'
                                    +'<div style="width:114px;height:15px;margin-right:6px;float:right;">'+data[0].seriesName.split(" ")[3].split("(")[0]+'</div>'+'<br/>'
                                    +'<div style="width:200px;height:20px;float: left;">'
                                    +'<span style="display:inline-block;margin-top:6px;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#c23531;"></span>'
                                    +dataTime[0]+'</div>'
                                    +'<div style="width: 115px;height:20px;margin-right:5px;float: right;">' + data[0].value+dataTitle[3].split("：")[1].split("")[0] +'</div>'
                                    +'<div style="width: 200px;height:20px;float: left;">'
                                    +'<span style="display:inline-block;margin-top:6px;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#0D8AE6;"></span>'
                                    +dataTime[1]+'</div>'
                                    +'<div style="width: 115px;height:20px;margin-right:5px;float: right;">' + data[1].value+dataTitle[3].split("：")[1].split("")[0] +'</div>'
                                    '</div>'
                            } else {
                                res = '<div style="width: 320px;height:70px;">'
                                    +'<div style="width: 185px;height:15px;margin-left:15px;float:left;">时间</div>'
                                    +'<div style="width: 114px;height:15px;margin-right:6px;float:right;">'+data[0].seriesName.split(" ")[3].split("(")[0]+'</div>'+'<br/>'
                                    +'<div style="width: 200px;height:20px;float: left;">'
                                    +'<span style="display:inline-block;margin-top:6px;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#c23531;"></span>'
                                    +dataTime[0]+'</div>'
                                    +'<div style="width: 115px;height:20px;margin-right:5px;float: right;">' + data[0].value+'</div>'
                                    +'<div style="width: 200px;height:20px;float: left;">'
                                    +'<span style="display:inline-block;margin-top:6px;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#0D8AE6;"></span>'
                                    +dataTime[1]+'</div>'
                                    +'<div style="width: 115px;height:20px;margin-right:5px;float: right;">' + data[1].value+'</div>'
                                '</div>'
                            }
                        } else {
                            if (dataTitle[3].split("：")[1].split("")[0] == "万") {
                                res = '<div style="width: 320px;height:50px;">'
                                    +'<div style="width: 185px;height:15px;margin-left:15px;float:left;">时间</div>'
                                    +'<div style="width: 114px;height:15px;margin-right:6px;float:right;">'+data[0].seriesName.split(" ")[3].split("(")[0]+'</div>'+'<br/>'
                                    +'<div style="width: 200px;height:20px;float: left;">'
                                    +'<span style="display:inline-block;margin-top:6px;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#c23531;"></span>'
                                    +dataTime[0]+'</div>'
                                    +'<div style="width: 115px;height:20px;margin-right:5px;float: right;">' + data[0].value+ dataTitle[3].split("：")[1].split("")[0]+'</div>'
                                    '</div>'
                                }else{
                                res = '<div style="width: 320px;height:50px;">'
                                    +'<div style="width: 185px;height:15px;margin-left:15px;float:left;">时间</div>'
                                    +'<div style="width: 114px;height:15px;margin-right:6px;float:right;">'+data[0].seriesName.split(" ")[3].split("(")[0]+'</div>'+'<br/>'
                                    +'<div style="width: 200px;height:20px;float: left;">'
                                    +'<span style="display:inline-block;margin-top:6px;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#c23531;"></span>'
                                    +dataTime[0]+'</div>'
                                    +'<div style="width: 115px;height:20px;margin-right:5px;float: right;">' + data[0].value+'</div>'
                                    '</div>'
                                 }
                        }
                        return res
                    }
                },
                legend: {
                    data: nameArray,
                    selectedMode: false,
                    y: "bottom",
                    orient: 'vertical',  //垂直显示
                    padding: 10,//调节legend的位置
                },
                grid: {
                    left: '8%',
                    right: '8%',
                    height: '300'
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
                    boundaryGap: false,
                    splitLine: {
                        show: false,
                    },
                    axisTick: {
                        alignWithLabel: true
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
                    min: min,
                    max: max,
                    splitLine: {
                        show: false,
                    }
                },
                series: [
                    {
                        name: nameSingle,
                        type: 'line',
                        data: selectData,
                        showAllSymbol: false,//不标注所有数据点,
                        symbolSize: 3,
                        label: {
                            show: true,
                            position: 'top',
                            distance: 10
                        },
                    },
                    {
                        name: nameCompare,
                        type: 'line',
                        data: compareData,
                        showAllSymbol: false,//不标注所有数据点,
                        symbolSize: 3,
                        itemStyle: {
                            borderColor: "#0D8AE6",//圆点颜色
                            color: "#0D8AE6" //折线图颜色设置
                        },
                        label: {
                            show: true,
                            distance: 10,
                            position: 'top'
                        },
                    }
                ]
            };
            if (option && typeof option === "object") {
                myChart.setOption(option, true);
                window.onresize = function () { myChart.resize({width:$(".line_wrap").width()}); }
            }
        }
    }
})();
