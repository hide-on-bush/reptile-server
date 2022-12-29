package com.xsx.jsoup.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:51
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alb {

    /**
     * 照片名称
     */
    private String name;

    /**
     * 拍摄者(无拍摄者则获取手机品牌)
     */
    private String author;

    /**
     * 照片高度，单位像素
     */
    private int height;

    /**
     * 照片宽度，单位像素
     */
    private int width;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 维度
     */
    private BigDecimal latitude;

    /**
     * 拍摄时间（yyyy/M/dd HH:mm:ss）
     */
    @JSONField(format = "yyyy/M/dd HH:mm:ss")
    private Date date;

    /**
     * 该条数据首次获取时间 （yyyy/M/dd HH:mm:ss）
     */
    @JSONField(format = "yyyy/M/dd HH:mm:ss")
    private Date createTime;

    /**
     * 拍摄照片的手机机型
     */
    private String model;
}
