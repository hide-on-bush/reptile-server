package com.xsx.jsoup.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:夏世雄
 * @Date: 2022/10/31/17:56
 * @Version: 1.0
 * @Discription:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDTO {

    private Long id;
    private String name;
    private String mobile;
}
