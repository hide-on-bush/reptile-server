package com.xsx.jsoup.service.forest;

import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Get;

import java.util.Map;

/**
 * @Author:夏世雄
 * @Date: 2022/12/05/14:15
 * @Version: 1.0
 * @Discription: forest框架可以简化okHttpClient和HttpClient调用第三方接口的操作，只需要引入依赖，在yml中配置forest相关配置，
 * 项目启动类上使用@ForestScan(basePackages = "com.xsx.jsoup.service.forest")扫描forest客户端及可使用
 **/
public interface MyForestService {

    /**
     * 高德地图API 但是只支持国内
     */
    @Get(url = "http://ditu.amap.com/service/regeo?longitude=${longitude}&latitude=${latitude}")
    Map getLocation(@DataVariable("longitude") String longitude, @DataVariable("latitude") String latitude);

}
