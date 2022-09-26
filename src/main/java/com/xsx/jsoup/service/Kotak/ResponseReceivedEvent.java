package com.xsx.jsoup.service.Kotak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:夏世雄
 * @Date: 2022/09/14/12:20
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseReceivedEvent {
    private String frameId;
    private String requestId;
    private String response;
    private String loaderId;
    private String type;
    private String timestamp;
}

