package com.xsx.jsoup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author:夏世雄
 * @Date: 2022/10/27/15:27
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppKeyConfig {

    private long id;

    private String keyWord;

    private String value;

    private String stay;

    private int status;
    private String space;
    private Date createTime;

    @Override
    public String toString() {
        return "AppKeyConfig{" +
                "id=" + id +
                ", keyWord='" + keyWord + '\'' +
                ", value='" + value + '\'' +
                ", stay='" + stay + '\'' +
                ", status=" + status +
                ", space='" + space + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
