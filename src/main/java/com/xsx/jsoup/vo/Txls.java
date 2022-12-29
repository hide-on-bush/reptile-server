package com.xsx.jsoup.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/13:48
 * @Version: 1.0
 * @Discription: 通讯录
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Txls {

    /**
     * 通讯录数据
     */
    private List<Txl> dataList;

    /**
     * 其他参数
     */
    private String otherData;
}
