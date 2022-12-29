package com.xsx.jsoup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:夏世雄
 * @Date: 2022/10/17/18:12
 * @Version: 1.0
 * @Discription:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String message;
    private int code;
    private Object data;
}
