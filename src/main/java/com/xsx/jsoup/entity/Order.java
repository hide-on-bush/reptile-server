package com.xsx.jsoup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:夏世雄
 * @Date: 2022/07/25/17:14
 * @Version: 1.0
 * @Discription:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private Long id;
    private String name;
    private Long messageId;   //存储消息发送的唯一标识
}
