package com.xsx.jsoup.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:55
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class App {

    /**
     * APP安装时间（yyyy/M/dd HH:mm:ss）
     */
    @JSONField(format = "yyyy/M/dd HH:mm:ss")
    private Date installTime;

    /**
     * APP名称
     */
    private String name;
}
