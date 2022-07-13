package com.xsx.jsoup.common.util;

import okhttp3.*;

public class OkHttpUtil {

    //private  OkHttpUtil(){}

    private volatile static OkHttpUtil okHttpUtil = null;
    private static OkHttpClient okHttpClient = null;

    public static class OkHttpClientHolder{
        //private static OkHttpUtil okHttpUtil = new OkHttpUtil();
        private static OkHttpClient okHttpClient = new OkHttpClient();

    }

    public static OkHttpClient getInstance() {
        return OkHttpClientHolder.okHttpClient;
    }

    public void doGet(String url, Headers headers, Callback callback){
        Request.Builder builder = new Request.Builder().url(url);
        //添加响应头集合
        if(headers!=null) {
            builder.headers(headers);
        }
        Request okRequest = builder.build();
        OkHttpUtil.getInstance().newCall(okRequest).enqueue(callback);
    }

    public void doGet(String url, Callback callback){
        this.doGet(url,null,callback);
    }

    public  void doSyncGet(String url) throws Exception{
        Request okRequest = new Request.Builder().url(url).build();

        //GET请求，用Response接受相应结果
        Response response = OkHttpUtil.getInstance().newCall(okRequest).execute();
    }

}
