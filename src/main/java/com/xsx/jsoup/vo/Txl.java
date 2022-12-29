package com.xsx.jsoup.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:49
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Txl {

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy/M/dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy/M/dd HH:mm:ss")
    private Date createTime;

    /**
     * 姓名
     */
    private String contacts;

    /**
     * “device”手机号存储在手机上的数据 “sim”手机号存储在sim卡上的数据 “” 未抓取到
     */
    private String source;
}
