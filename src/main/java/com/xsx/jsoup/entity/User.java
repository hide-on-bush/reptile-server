package com.xsx.jsoup.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsx.jsoup.common.annotation.DataMasking;
import com.xsx.jsoup.common.enums.DataMaskingFunc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author:夏世雄
 * @Date: 2022/10/21/15:02
 * @Version: 1.0
 * @Discription:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    @DataMasking(maskFunc = DataMaskingFunc.ALL_MASK)
    private String name;

    @DataMasking(maskFunc = DataMaskingFunc.MOBLIE_MASK)
    private String mobile;
    private int age;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
