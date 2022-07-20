package com.xsx.jsoup.entity;

import java.util.Date;

public class BankUser {

    /**
     *  数据库自增ID
     */
    private Integer id;

    /**
     * 银行系统中用户唯一ID
     */
    private String userId;

    /**
     *  登陆名称
     */
    private String loginName;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 银行名称，小写
     */
    private String bankName;

    /**
     * 卡来源渠道，非必填
     */
    private String channelFrom;

    /**
     * 密保问题答案，具体格式待商议
     */
    private String securityQuestionsAnswer;

    /**
     * 是否可用
     */
    private Boolean enable;

    private Date createTime;

    private Date updateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getChannelFrom() {
        return channelFrom;
    }

    public void setChannelFrom(String channelFrom) {
        this.channelFrom = channelFrom == null ? null : channelFrom.trim();
    }

    public String getSecurityQuestionsAnswer() {
        return securityQuestionsAnswer;
    }

    public void setSecurityQuestionsAnswer(String securityQuestionsAnswer) {
        this.securityQuestionsAnswer = securityQuestionsAnswer == null ? null : securityQuestionsAnswer.trim();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BankUser{" +
                "userId='" + userId + '\'' +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", bankName='" + bankName + '\'' +
                ", channelFrom='" + channelFrom + '\'' +
                ", securityQuestionsAnswer='" + securityQuestionsAnswer + '\'' +
                ", id=" + id +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
