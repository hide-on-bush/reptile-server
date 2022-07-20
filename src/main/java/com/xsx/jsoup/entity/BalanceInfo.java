package com.xsx.jsoup.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceInfo {

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 余额
     */
    private BigDecimal balance;

    private Date fetchTime;

    public Date getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Date fetchTime) {
        this.fetchTime = fetchTime;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BalanceInfo() {
    }


    @Override
    public String toString() {
        return "BalanceInfo{" +
                "bankName='" + bankName + '\'' +
                ", loginName='" + loginName + '\'' +
                ", balance=" + balance +
                ", fetchTime=" + fetchTime +
                '}';
    }
}
