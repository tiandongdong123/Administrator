package com.wanfangdata.function;

import com.wf.bean.userStatistics.StatisticsModel;
import com.wf.bean.userStatistics.UserStatisticsExample;
import com.wf.dao.UserStatisticsMapper;
import org.apache.log4j.Logger;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticsNewSum implements Function {

    private static final Logger log = Logger.getLogger(UserStatisticsNewSum.class);

    @Autowired
    UserStatisticsMapper userStatisticsMapper;

    @Override
    public Object call(Object[] objects, Context context) {
        String startTime = (String) objects[0];
        String endTime = (String) objects[1];
        StatisticsModel statisticsModel = new StatisticsModel();
        try {
            UserStatisticsExample example = new UserStatisticsExample();
            UserStatisticsExample.Criteria criteria = example.createCriteria();
            if (startTime!=null&&!"".equals(startTime)&&endTime!=null&&!"".equals(endTime)){
                criteria.andDateGreaterThanOrEqualTo(startTime);
                criteria.andDateLessThanOrEqualTo(endTime);
            }
            statisticsModel = userStatisticsMapper.selectSumByExample(example);
        } catch (Exception e) {
            log.error("查询统计时间内新增数量之和失败", e);
        }
        return statisticsModel;
    }
}
