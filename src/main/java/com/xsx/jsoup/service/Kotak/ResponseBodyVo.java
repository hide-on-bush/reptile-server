package com.xsx.jsoup.service.Kotak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:夏世雄
 * @Date: 2022/09/14/12:19
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBodyVo {
    private String sessionId;
    private String value;
    private int status;
}

