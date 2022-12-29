package com.xsx.jsoup.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class OkHttpClientService {


    /**
     * 同步GET方法，execute()方法会抛出IOException异常
     *
     * @return
     */
    public String syncGet() {
        try {
            //创建OkHttpClient实例，主要用于请求网络
            OkHttpClient okHttpClient = new OkHttpClient();

            //创建Request实例，可以配置接口地址和请求头
            Request okRequest = new Request.Builder().url("https://www.baidu.com/").build();

            //GET请求，用Response接受相应结果
            Response response = okHttpClient.newCall(okRequest).execute();

            //response.body()为请求返回数据 JSON格式
            //注意 okHttpClient的response.body().string()只能调用一次否则报
            // java.lang.IllegalStateException: closed
            //log.info("+++++++++++++++++++++", response.body().string());

            return response.body().string();
        } catch (Exception e) {
            log.error("http client call error， error={}", e.getLocalizedMessage());
            return null;
        }

    }


    /**
     * 异步GET方法
     *
     * @throws IOException
     */
    public void asyncGet() throws IOException {

        //创建OkHttpClient实例，主要用于请求网络
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建Request实例，这里配置了请求头 addHeader()
        Request okRequest = new Request.Builder().url("http://toutiao-ali.juheapi.com/toutiao/index?type=caijing").addHeader("name", "value").build();

        //GET请求，回调函数中获取相应结果
        okHttpClient.newCall(okRequest).enqueue(new Callback() {

            //失败回调
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("+++++++++++++++++++++", e.getMessage());
            }

            //成功回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.info("+++++++++++++++++++++", response.body().string());
            }
        });
    }

    /**
     * 异步post请求
     */
    public void asyncPost() {

        //创建OkHttpClient实例，主要用于请求网络
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();
        //传递键值对参数
        formBody.add("name", "shuaige");

        //创建Request实例，设置POST参数
        Request okRequest = new Request.Builder().url("http://toutiao-ali.juheapi.com/toutiao/index?type=caijing").post(formBody.build()).build();

        //POST异步请求，回调函数中获取相应结果
        okHttpClient.newCall(okRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("+++++++++++++++++++++ err={}", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.info("+++++++++++++++++++++", response.body().string());
            }
        });

    }


    /**
     * 同步post请求
     */
    public void syncPost() throws Exception {

        //创建OkHttpClient实例，主要用于请求网络
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();
        //传递键值对参数
        formBody.add("name", "shuaige");

        //创建Request实例，设置POST参数
        Request okRequest = new Request.Builder().url("http://toutiao-ali.juheapi.com/toutiao/index?type=caijing").post(formBody.build()).build();
        Response response = okHttpClient.newCall(okRequest).execute();
        log.info("body =", response.body().string());
    }
}
