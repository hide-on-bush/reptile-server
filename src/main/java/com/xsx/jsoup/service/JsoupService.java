package com.xsx.jsoup.service;

import com.xsx.jsoup.common.util.OkHttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsoupService {

    /**
     * 从URL加载文档，使用Jsoup.connect()方法从URL加载HTML。
     *
     * @return
     */
    public String jsoupConnection() {
        try {
            Document document = Jsoup.connect("http://www.baidu.com").get();
            return document.title();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String littleRedBook() throws Exception {

        OkHttpClient okHttpClient = OkHttpUtil.getInstance();
        Request okRequest = new Request.Builder().url("https://www.xiaohongshu.com/").build();

        //GET请求，用Response接受相应结果
        Response response = okHttpClient.newCall(okRequest).execute();
        String html = response.body().string();
        getLinksFromHtml(html);
        System.out.println("=========================================================");
        this.getImagesFromHtml(html);
        return html;
    }

    /**
     * 获取HTML页面中的所有链接
     *
     * @param html
     */
    public void getLinksFromHtml(String html) {
        Document document = Jsoup.parse(html);
        Elements links = document.select("a[href]");
        for (Element link : links) {
            System.out.println("link : " + link.attr("href"));
            System.out.println("text : " + link.text());
        }
    }

    public void getImagesFromHtml(String html) {
        Document document = Jsoup.parse(html);
        //this.getById(document);
        Elements images = document.select("img[src]");
        for (Element image : images) {
            System.out.println("src : " + image.attr("src"));
            System.out.println("height : " + image.attr("height"));
            System.out.println("width : " + image.attr("width"));
            System.out.println("alt : " + image.attr("alt"));
        }
    }

    public void getById(Document document) {
        Element element = document.getElementById("app");
        System.out.println(element);
    }
}
