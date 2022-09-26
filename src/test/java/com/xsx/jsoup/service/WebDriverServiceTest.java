package com.xsx.jsoup.service;

import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
class WebDriverServiceTest {

    @Autowired
    private WebDriverService webDriverService;

    @Test
    void useWebDriver() throws Exception{
        webDriverService.useWebDriver();
    }

    @Test
    void testGetHeaders() throws Exception{
        webDriverService.getHeaders();
    }

    /**
     * 使用jsuop解析html，并获取下拉列表的name和value，构成成map
     * @throws Exception
     */
    @Test
    void jsoupTest() throws Exception{
        Document doc = Jsoup.parse(new File("C:\\Users\\Administrator\\Desktop\\test.html"), "UTF-8");
        Elements select = doc.select("#app");
        if (ObjectUtils.isEmpty(select)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        select.forEach(x->{
            Elements children = x.children();
            if (ObjectUtils.isEmpty(children)) {
                return;
            }
            children.forEach(n->{
                map.put(n.text(), n.val());
            });
        });
        Elements select1 = doc.select("option[value=volvo]");
        String text = select1.text();
        String value = select1.attr("value");
        String val = select.val();

        System.out.println(map);
    }
}