package com.xsx.jsoup.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:44
 * @Version: 1.0
 * @Discription: 基本信息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ztdata {

    /**
     * A卡号码
     */
    private String aadhaar;

    /**
     * P卡号码
     */
    private String pan;

    /**
     * 申请时IMEI与注册是否一致，1：一致 0：不一致
     */
    private int imeiIsSame;

    /**
     * 手机信息(手机品牌_手机型号) 例：vivo_1818
     */
    private String brandModel;

    /**
     * 年龄
     */
    private int age;

    /**
     * 性别；1：男，0：女
     */
    private int sex;
    /**
     * 申请时间 yyyy/M/dd HH:mm:ss
     */
    @JSONField(format = "yyyy/M/dd HH:mm:ss")
    private Date applyTime;
    /**
     * 薪资范围，详见薪资范围code对应说明
     */
    private int salaryRange;

    /**
     * 婚恋状态，详见婚恋状态code对应说明
     */
    private int marryState;

    /**
     * 工作职位，详见工作职位code对应说明
     */
    private String jobPosition;
}
