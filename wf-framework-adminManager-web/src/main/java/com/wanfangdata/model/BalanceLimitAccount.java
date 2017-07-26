package com.wanfangdata.model;


import java.math.BigDecimal;

/**
 * 机构余额账户实体类
 */
public class BalanceLimitAccount extends UserAccount {
    /**
     * 余额
     */
    private BigDecimal balance;
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BalanceLimitAccount{" +
                "userId='" + getUserId() + '\'' +
                ", payChannelId='" + getPayChannelId() + '\'' +
                ", organName='" + getOrganName() + '\'' +
                ", beginDateTime=" + getBeginDateTime() +
                ", endDateTime=" + getEndDateTime() + '\'' +
                ", balance=" + balance +
                '}';
    }
}
