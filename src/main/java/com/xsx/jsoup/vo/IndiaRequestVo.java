package com.xsx.jsoup.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:41
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndiaRequestVo {
    /**
     * 商户自行生成的订单号，建议商户保证唯一性，最大64位
     */
    private String aliasId;

    /**
     * 扩展参数，商户可根据自己的业务需求传输
     */
    private String extend;

    /**
     * 回调地址,商户需保证可以正常请求(为空使用商户开户时的回调地址)
     */
    private String callUrl;

    /**
     * 手机号（不需要区号）
     */
    private String phone;

    /**
     * 手机序列号
     */
    private String serialNumber;

    /**
     * 谷歌id
     */
    private String googleId;

    /**
     * 渠道来源 1:app 2:api 3:其他
     */
    private String channel;

    /**
     * 包名（APP名）
     */
    private String packageName;

    /**
     * 产品名（申请产品名）
     */
    private String appName;

    /**
     * 产品利率
     */
    private BigDecimal loanRate;

    /**
     * 贷款天数
     */
    private int loanDay;

    /**
     * 基本信息
     */
    private Ztdata ztdata;

    /**
     * 短信
     */
    private Msgs msgs;

    /**
     * 通讯录
     */
    private Txls txls;

    /**
     * 相册
     */
    private Albs albs;

    /**
     * 应用程序
     */
    private Apps apps;
}
