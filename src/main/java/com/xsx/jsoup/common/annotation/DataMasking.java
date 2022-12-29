package com.xsx.jsoup.common.annotation;

import com.xsx.jsoup.common.enums.DataMaskingFunc;

import java.lang.annotation.*;

/**
 * @Author:夏世雄
 * @Date: 2022/10/21/11:38
 * @Version: 1.0
 * @Discription:
 **/
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataMasking {

    DataMaskingFunc maskFunc() default DataMaskingFunc.NO_MASK;

}
