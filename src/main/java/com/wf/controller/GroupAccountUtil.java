package com.wf.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanfangdata.model.BalanceLimitAccount;
import com.wanfangdata.model.CountLimitAccount;
import com.wanfangdata.model.TimeLimitAccount;
import com.wanfangdata.model.UserAccount;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfks.accounting.account.Account;
import wfks.accounting.account.AccountDao;
import wfks.accounting.setting.PayChannelModel;
import wfks.accounting.setting.SettingPayChannels;
import wfks.accounting.transaction.TransactionProcess;
import wfks.accounting.transaction.TransactionRequest;
import wfks.accounting.transaction.TransactionResponse;
import wfks.authentication.AccountId;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构操作类
 */
@Service
public class GroupAccountUtil {
    @Autowired
    private TransactionProcess accountingService;
    @Autowired
    private AccountDao accountDao;

    /**
     * 生效时间
     */
    private static final String BEGIN_DATE_TIME_KEY = "begindatetime";
    /**
     * 失效时间
     */
    private static final String END_DATE_TIME_KEY = "enddatetime";
    /**
     *  转换前的项目
     */
    private static final String PAYTAG_STRING_KEY = "solr_payTag";
    /**
     * 操作机构名称
     */
    public static final String ORGANNAME_KEY = "organname";
    /**
     * 操作key
     */
    public static final String OPERATE_KEY = "operate";
    /**
     * 更新key
     */
    public static final String UPDATE_KEY = "update";
    /**
     * 删除key
     */
    public static final String DELETE_KEY = "delete";
    //注册购买项目
    public static final String GROUPACCOUNT_BEHAVIOR_ADD = "修改机构购买项目(add)";
    //修改购买项目
    public static final String GROUPACCOUNT_BEHAVIOR_UPDATE = "修改机构购买项目(update)";
    //删除购买项目
    public static final String GROUPACCOUNT_BEHAVIOR_DELETE = "删除机构购买项目(delete)";
    
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger log = Logger.getLogger(GroupAccountUtil.class);
    

    /**
     * 为机构充值准备的账号信息数据
     *
     * @param organName 机构名称
     * @param begin     有效期开始时间
     * @param end       有效期结束时间
     * @return
     */
    private Map<String, String> createExtraData(String organName, Date begin, Date end, List<String> change) {
        Map<String, String> extraData = new HashMap<String, String>();
        extraData.put(ORGANNAME_KEY, organName);
        //对生效时间和失效时间进行格式化"yyyy-MM-dd HH:mm:ss"
        String begin_date_time = format.format(begin);
        String end_date_time = format.format(end);
        extraData.put(BEGIN_DATE_TIME_KEY, begin_date_time);
        extraData.put(END_DATE_TIME_KEY, end_date_time);
        if(change != null && change.size() > 0){
            extraData.put(PAYTAG_STRING_KEY, JSONObject.toJSONString(change));
        }
        return extraData;
    }

    /**
     * 创建ProductDetail串
     *
     * @param begin 有效期开始时间
     * @param end   过期时间
     * @return
     * @throws IOException
     */
    private String createProductDetail(Long count, Date begin, Date end) throws IOException {
        Map<String, Object> productDetail = new HashMap<String, Object>();
        String begin_date_time = format.format(begin);
        String end_date_time = format.format(end);
        productDetail.put("count", count);
        productDetail.put(BEGIN_DATE_TIME_KEY, begin_date_time);
        productDetail.put(END_DATE_TIME_KEY, end_date_time);
        try {
            return mapper.writeValueAsString(productDetail);
        } catch (IOException e) {
            log.error("序列化product_detail失败", e);
            throw e;
        }
    }

    /**
     * 获取交易请求
     *
     * @param account   要充值或注册的账户信息
     * @param userIP    用户ip
     * @param authToken adminToken
     * @param updateKey 操作Key
     * @return 交易请求
     * @throws IOException
     */

    private final static String CHANGE_BEGIN_TIME = "changeBeginTime";
    private final static String CHANGE_END_TIME = "changeEndTime";
    private final static String CHANGE_BALANCE = "balance";
    public TransactionRequest createTransactionRequest(UserAccount account, Long count,
                                                              String userIP, String authToken, String updateKey,List<String> change,Map<String,Object> changeFront) throws IOException {
        TransactionRequest request = new TransactionRequest();
        request.setTransferIn(new AccountId(account.getPayChannelId(), account.getUserId()));
        request.setUserIP(userIP);
        request.setAuthToken(authToken);
        
        //2017.6.23新增：在request中指定transferOut,指定金额为null
        Map<AccountId, BigDecimal> transferOut = new HashMap<>();
        transferOut.put(new AccountId("Operational", "Manual"), null);
        request.setTransferOut(transferOut);
        
        Map<String, String> extraData = createExtraData(account.getOrganName(), account.getBeginDateTime(), account.getEndDateTime(),change);
        extraData.put(OPERATE_KEY, updateKey);
        if(changeFront != null && changeFront.size() > 1){
            extraData.put(CHANGE_BEGIN_TIME,format.format(changeFront.get("beginDateTime")));
            extraData.put(CHANGE_END_TIME,format.format(changeFront.get("endDateTime")));
            if(changeFront.get("balance") != null){
                extraData.put(CHANGE_BALANCE, String.valueOf(changeFront.get("balance")));
            }
        }
        request.setExtraData(extraData);

        request.setProductDetail(createProductDetail(count, account.getBeginDateTime(), account.getEndDateTime()));
        setTransactionRequestProductTitle(request, updateKey, account.getPayChannelId(), account.getUserId(),change);
        return request;
    }

    /**
     * 提交交易
     *
     * @param request      交易请求
     * @param payChannelId 充值渠道
     * @return
     * @throws Exception
     */
    public boolean submitRequest(TransactionRequest request, String payChannelId, String updateKey) throws Exception {
        try {
            TransactionResponse response = accountingService.submit(request);
            if (!response.isSuccess()) {
                if (log.isInfoEnabled()) {
                    log.info(payChannelId +
                            (updateKey.equals(UPDATE_KEY) ? "充值" : updateKey.equals(DELETE_KEY) ? "删除" : "")
                            + "失败, response:" + response);
                }
            }
            return response.isSuccess();
        } catch (Exception ex) {
            log.error(payChannelId + updateKey + "过程出错, transactionRequest:" + request);
            throw ex;
        }
    }

    /**
     * 为余额限时用户充值
     *
     * @param before    充值前账户信息，注册余额限时账户可以传null
     * @param after     将要充值的信息
     * @param userIP    用户当前ip
     * @param authToken adminToken
     * @return 交易是否成功
     * @throws Exception
     */
    public boolean addBalanceLimitAccount(BalanceLimitAccount before, BalanceLimitAccount after, String userIP, String authToken, boolean reset, List<String> change, Map<String,Object> changeFront) throws Exception {

        validate(before, after);

        TransactionRequest request = createTransactionRequest(after, null, userIP, authToken, UPDATE_KEY,change,changeFront);

        //request.setTurnover(after.getBalance());        	
    	
        if(reset){
        	request.setTurnover(after.getBalance().subtract(before.getBalance()));
    	}else{
    		request.setTurnover(after.getBalance());
    	}

        return submitRequest(request, after.getPayChannelId(), UPDATE_KEY);
    }


    /**
     * 注册或充值给机构限时账户
     */
    public boolean addTimeLimitAccount(TimeLimitAccount account, String userIP, String authToken,List<String> change,Map<String,Object> changeFront) throws Exception {

        validate(null, account);

        TransactionRequest request = createTransactionRequest(account, null, userIP, authToken, UPDATE_KEY,change,changeFront);
        request.setTurnover(BigDecimal.ZERO);
        return submitRequest(request, account.getPayChannelId(), UPDATE_KEY);
    }

    /**
     * 注册或充值给次数计费用户
     */
    public boolean addCountLimitAccount(CountLimitAccount before, CountLimitAccount after, String userIP, String authToken, List<String> change,boolean reset) throws Exception {

        validate(before, after);

        long count = 0;
        if(reset){
        	count = (after.getBalance() - before.getBalance());
        }else{        	
        	count = after.getBalance();
        }
        
        TransactionRequest request = createTransactionRequest(after, count, userIP, authToken, UPDATE_KEY,change,new HashMap<String, Object>());
        request.setTurnover(BigDecimal.valueOf(count));

        return submitRequest(request, after.getPayChannelId(), UPDATE_KEY);
    }

    /**
     * 删除账户信息
     */
    public boolean deleteAccount(UserAccount account, String userIP, String authToken,List<String> change) throws Exception {
        //创建交易request
        TransactionRequest request = createTransactionRequest(account, null, userIP, authToken, DELETE_KEY,change,new HashMap<String, Object>());
        BigDecimal turnover = getAccountCountOrBalance(account.getPayChannelId(), account.getUserId());
        request.setTurnover(new BigDecimal(BigInteger.ZERO).subtract(turnover));
        return submitRequest(request, account.getPayChannelId(), DELETE_KEY);
    }

    /**
     * 当执行充值行为，则需要将充值前账户信息 和 将要充值的账户信息进行匹配验证，
     * 匹配内容为：机构ID，充值渠道ID，机构名称
     * 如果信息不匹配将记录日志并抛出异常
     *
     * @param before 充值前账户信息
     * @param after  将要充值的信息
     */
    public void validate(UserAccount before, UserAccount after) throws Exception {
        if (after == null) {
            //验证after不存在的情况
            log.error("将要充值用户为null");
            throw new Exception("将要充值用户为null");
        }

        if (after != null) {
            //验证after存在但参数为null的情况
            if (after.getUserId() == null || after.getUserId().isEmpty()) {
                log.error("将要充值用户UserId为null，UserAccount:" + after.toString());
                throw new Exception("将要充值用户UserId为null");
            }
            if (after.getPayChannelId() == null || after.getPayChannelId().isEmpty()) {
                log.error("将要充值用户PayChannelId为null，UserAccount:" + after.toString());
                throw new Exception("将要充值用户PayChannelId为null");
            }
            if (after.getOrganName() == null || after.getOrganName().isEmpty()) {
                log.error("将要充值用户OrganName为null，UserAccount:" + after.toString());
                throw new Exception("将要充值用户OrganName为null");
            }
        }

        if (before == null) {
            //验证before 为null的情况
            return;
        }

        if (before != null) {
            //验证before存在但参数为null的情况
            if (before.getUserId() == null || before.getUserId().isEmpty()) {
                log.error("充值前用户UserId为null，UserAccount:" + before.toString());
                throw new Exception("充值前用户UserId为null");
            }
            if (after.getPayChannelId() == null || before.getPayChannelId().isEmpty()) {
                log.error("充值前用户PayChannelId为null，UserAccount:" + before.toString());
                throw new Exception("充值前用户PayChannelId为null");
            }
            if (after.getOrganName() == null || before.getOrganName().isEmpty()) {
                log.error("充值前用户OrganName为null，UserAccount:" + before.toString());
                throw new Exception("充值前用户OrganName为null");
            }
            //如果是充值行为，充值前账户信息与将要充值的账户信息不符，则抛出异常。
            if (!before.getUserId().equals(after.getUserId())) {
                log.error("充值前账户信息与当前充值账户信息不符");
                throw new Exception("充值前账户信息与当前充值账户信息不符");
            }
            if (!before.getPayChannelId().equals(after.getPayChannelId())) {
                log.error("充值前账户信息与当前充值账户信息不符");
                throw new Exception("充值前账户信息与当前充值账户信息不符");
            }
            if (!before.getOrganName().equals(after.getOrganName())) {
                log.error("充值前账户信息与当前充值账户信息不符");
                throw new Exception("充值前账户信息与当前充值账户信息不符");
            }
        }
    }
    
    public BigDecimal getAccountCountOrBalance(String payChannelId, String user_id) throws Exception {
        try {
            AccountId id = new AccountId(payChannelId, user_id);
            Account account = accountDao.get(id, null);

            PayChannelModel payChannel = SettingPayChannels.getPayChannel(payChannelId);
            String payChannelType = payChannel.getType();

            if (payChannelType.equals("balance")) {
                return ((wfks.accounting.handler.entity.BalanceLimitAccount) account).getBalance();
            } else if (payChannelType.equals("count")) {
                long totalConsume = ((wfks.accounting.handler.entity.CountLimitAccount) account).getBalance();
                return new BigDecimal(totalConsume);
            } else if (payChannelType.equals("time")) {
                return BigDecimal.ZERO;
            }
            return BigDecimal.ZERO;
        } catch (Exception e) {
            log.error("根据user_id获取机构账户失败", e);
            throw e;
        }
    }
    
    public Account getAccount(String payChannelId, String user_id) {
        try {
            AccountId id = new AccountId(payChannelId, user_id);
            Account account = accountDao.get(id, null);
            return account;
        } catch (Exception e) {
            log.error("根据user_id获取机构账户失败", e);
            throw e;
        }

    }
    private final static String OLD_FORMAL = "OF";
    private final static String OLD_TRICAL = "OR";
    public void setTransactionRequestProductTitle(TransactionRequest request, String updateKey, String payChannelId, String user_id,List<String> change) {
        Account account = getAccount(payChannelId, user_id);
        if (UPDATE_KEY.equals(updateKey)) {
            if (account == null) {
                request.setProductTitle(GROUPACCOUNT_BEHAVIOR_ADD);
            } else {
                request.setProductTitle(GROUPACCOUNT_BEHAVIOR_UPDATE);
            }
        } else if (DELETE_KEY.equals(updateKey)) {
            request.setProductTitle(GROUPACCOUNT_BEHAVIOR_DELETE);
        }
        String title = request.getProductTitle();
        StringBuffer rule = new StringBuffer();
        if(change != null && change.contains(OLD_FORMAL)){
            rule.append("(trial)");
        }else if(change != null && change.contains(OLD_TRICAL)) {
            rule.append("(formal)");
        }
        title += rule.toString();
        request.setProductTitle(title);
    }
}
