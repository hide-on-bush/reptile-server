package com.xsx.jsoup.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:47
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg {

    /**
     * 短信内容
     */
    private String content;
    /**
     * 短信接收时间（yyyy/M/dd HH:mm:ss）
     */
    @JSONField(format = "yyyy/M/dd HH:mm:ss")
    private Date time;

    /**
     * 发送人或者接收人名字(无发送人或者接收人的时候使用发送号码或者接收号码)
     */
    private String name;

    /**
     * 1：发送 2：接收
     */
    private int type;
}
