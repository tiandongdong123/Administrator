package com.wanfangdata.model;

/**
 * 机构余额账户实体类
 */
public class CountLimitAccount extends UserAccount {

    /**
     * 剩余次数
     */
    private long balance;


    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "CountLimitAccount{" +
                "userId='" + getUserId() + '\'' +
                ", payChannelId='" + getPayChannelId() + '\'' +
                ", organName='" + getOrganName() + '\'' +
                ", beginDateTime=" + getBeginDateTime() +
                ", endDateTime=" + getEndDateTime() + '\'' +
                ", balance=" + balance +
                '}';
    }
}
