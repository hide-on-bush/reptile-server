package com.xsx.jsoup.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:45
 * @Version: 1.0
 * @Discription: 短信
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msgs {

    /**
     * 短信数据
     */
    private List<Msg> dataList;

    /**
     * 其他参数
     */
    private String otherData;
}
