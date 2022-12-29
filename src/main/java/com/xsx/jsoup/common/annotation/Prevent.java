package com.xsx.jsoup.common.annotation;

/**
 * @Author:夏世雄
 * @Date: 2022/10/09/17:34
 * @Version: 1.0
 * @Discription:
 **/

import com.xsx.jsoup.aop.PreventStrategy;

import java.lang.annotation.*;

/**
 * 接口防刷注解
 * 使用：
 * 在相应需要防刷的方法上加上
 * 该注解，即可
 *
 * @author: zetting
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Prevent {

    /**
     * 限制的时间值（秒）
     *
     * @return
     */
    String value() default "60";

    /**
     * 提示
     */
    String message() default "";

    /**
     * 策略
     *
     * @return
     */
    PreventStrategy strategy() default PreventStrategy.DEFAULT;
}
