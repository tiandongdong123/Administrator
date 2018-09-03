package com.wf.service.impl;

import com.google.protobuf.util.Timestamps;
import com.wanfangdata.grpcchannel.BindAccountChannel;
import com.wanfangdata.rpc.bindauthority.SearchBindDetailsRequest;
import com.wanfangdata.rpc.bindauthority.SearchBindDetailsResponse;
import com.wf.bean.WfksPayChannelResources;
import com.wf.bean.userStatistics.*;
import com.wf.dao.PersonMapper;
import com.wf.dao.UserStatisticsMapper;
import com.wf.dao.WfksPayChannelResourcesMapper;
import com.wf.service.UserStatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfks.accounting.account.Account;
import wfks.accounting.account.AccountDao;
import wfks.authentication.AccountId;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class UserStatisticsServiceImpl implements UserStatisticsService {

    private static final Logger log = Logger.getLogger(UserStatisticsServiceImpl.class);
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private UserStatisticsMapper userStatisticsMapper;
    @Autowired
    private BindAccountChannel bindAccountChannel;
    @Autowired
    private WfksPayChannelResourcesMapper payChannelResourcesMapper;
    @Autowired
    private AccountDao accountDao;


    @Override
    public UserStatistics selectStatisticsByDate(String dateTime) {

        UserStatistics userStatistics = personMapper.selectStatisticsByDate(dateTime);
        String today = format.format(new Date());
        try {
            //个人绑定机构新增个人账号
            SearchBindDetailsRequest countRequest = SearchBindDetailsRequest.newBuilder()
                    .setStartAddTime(Timestamps.fromMillis(format.parse(dateTime).getTime()))
                    .setEndAddTime(Timestamps.fromMillis(format.parse(today).getTime()))
                    .build();
            SearchBindDetailsResponse countResponse = bindAccountChannel.getBlockingStub().searchBindDetailsOrderUser(countRequest);
            int bindCount = countResponse.getTotalCount();
            userStatistics.setPersonBindInstitution(bindCount);

        } catch (Exception e) {
            log.error("查询个人绑定机构新增数量失败，时间：" + dateTime);
        }

        Integer institutionCountNumber = personMapper.getInstitutionCount(dateTime);
        userStatistics.setInstitution(institutionCountNumber);

        List<String> unFreezeInstitutionAccountList = personMapper.selectunFreezeInstitutionAccount();
        int vaildIntitutionAccountNumber = 0;

        for (String unFreezeInstitutionAccount : unFreezeInstitutionAccountList) {
            List<WfksPayChannelResources> payChannelResourcesList = payChannelResourcesMapper.selectByUserId(unFreezeInstitutionAccount);
            if (payChannelResourcesList == null || payChannelResourcesList.size() < 1) {
                continue;
            }

            for (WfksPayChannelResources payChannelResource : payChannelResourcesList) {
                AccountId accountId = new AccountId();
                accountId.setAccountType(payChannelResource.getPayChannelid());
                accountId.setKey(unFreezeInstitutionAccount);
                Account account = null;
                try {
                    account = accountDao.get(accountId, null);
                    if (account == null) {
                        continue;
                    }

                    Method method = account.getClass().getMethod("getEndDateTime");
                    if (method == null) {
                        continue;
                    }
                    Date endDateTime = (Date) method.invoke(account);
                    if (endDateTime.compareTo(format.parse(today)) == 1) {
                        if (payChannelResource.getPayChannelid().contains("Time")) {
                            vaildIntitutionAccountNumber++;
                            continue;
                        }
                        method = account.getClass().getMethod("getBalance");
                        BigDecimal balance = null;
                        if (method.invoke(account) instanceof BigDecimal) {
                            balance = (BigDecimal) method.invoke(account);
                        } else {
                            balance = new BigDecimal((Long) method.invoke(account));
                        }

                        if (balance != null && balance.compareTo(BigDecimal.ZERO) == 1) {
                            vaildIntitutionAccountNumber++;
                            continue;
                        }

                    }

                } catch (Exception e) {
                    log.error("获取指定资源账号失败，AccountId:" + accountId, e);
                }

            }
        }

        userStatistics.setValidInstitutionAccount(vaildIntitutionAccountNumber);
        userStatistics.setDate(dateTime);
        return userStatistics;
    }

    @Override
    public int insert(UserStatistics userStatistics) {
        return userStatisticsMapper.insert(userStatistics);
    }

    @Override
    public int selectSingleTypePreviousSum(String type, String dateTime) {
        return userStatisticsMapper.selectSingleTypePreviousSum(type,dateTime);
    }

    @Override
    public List<Integer> selectSingleTypeNewData(StatisticsParameter parameter) {

        return userStatisticsMapper.selectSingleTypeNewData(parameter);
    }
    @Override
    public StatisticsModel selectSumByExample(UserStatisticsExample example) {

        return userStatisticsMapper.selectSumByExample(example);
    }

    @Override
    public List<UserStatistics> selectByExample(UserStatisticsExample example) {

        return userStatisticsMapper.selectByExample(example);
    }


    @Override
    public List<StatisticsModel> selectNewData(StatisticsParameter parameter) {
            return  userStatisticsMapper.selectNewDate(parameter);

    }
}
