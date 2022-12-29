package com.xsx.jsoup.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:55
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Apps {

    /**
     * 应用程序数据
     */
    private List<App> dataList;

    /**
     * 其他参数
     */
    private String otherData;
}
