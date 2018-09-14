package com.job;

import com.wf.bean.userStatistics.UserStatistics;
import com.wf.service.UserStatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Component
public class UserStatisticsJob {

    private static Logger log = Logger.getLogger(UserStatisticsJob.class);
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private UserStatisticsService userStatisticsService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void userStatistics() {
        //获取当前时间
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String dateTime = format.format(calendar.getTime());

        try {
            UserStatistics userStatistics = userStatisticsService.selectStatisticsByDate(dateTime);
            userStatisticsService.insert(userStatistics);

        } catch (Exception e) {
            log.error("定时统计用户数据失败，统计日期：" + dateTime, e);
        }
    }

}
