package com.xsx.jsoup.common.util;

import okhttp3.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {

	public static Response sent(String method, String url, String mediaType, String body, Map<String, String> headers) throws IOException {
		return sent(method, url, mediaType, body, headers, false);
	}

	public static Response sent(String method, String url, String mediaType, String body, Map<String, String> headers, boolean proxy) throws IOException {
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		if (proxy) {
			SocketAddress sa = new InetSocketAddress("127.0.0.1", 10809);
			clientBuilder.proxy(new Proxy(Proxy.Type.HTTP, sa));
		}
		OkHttpClient client = clientBuilder.connectTimeout(50, TimeUnit.SECONDS)
				.readTimeout(50, TimeUnit.SECONDS).
				build();
		Request.Builder builder = new Request.Builder().url(url);
		if (StringUtils.isNotBlank(mediaType) && StringUtils.isNotBlank(body)) {
			RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), body);
			builder.method(method, requestBody);
		}
		builder.addHeader("Content-Type", "application/json;charset=UTF-8");
		if (!CollectionUtils.isEmpty(headers)) {
			for (String key : headers.keySet()) {

				builder.addHeader(key, headers.get(key));
			}
		}
		Request request = builder.build();
		return client.newCall(request).execute();
	}


}
