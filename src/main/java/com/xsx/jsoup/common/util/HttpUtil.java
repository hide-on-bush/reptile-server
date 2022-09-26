package com.xsx.jsoup.common.util;

import okhttp3.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {

	public static Response sent(String method, String url, String mediaType, String body, Map<String, String> headers) throws IOException {
		return sent(method, url, mediaType, body, headers, false);
	}

	public static Response sent(String method, String url, String mediaType, String body, Map<String, String> headers, boolean proxy) throws IOException {
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		clientBuilder.proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),4780)))
				.readTimeout(10L, TimeUnit.SECONDS);
		clientBuilder.cookieJar(new CookieJar() {
			private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

			@Override
			public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
				cookieStore.put(url.host(), cookies);
			}
			@Override
			public List<Cookie> loadForRequest(HttpUrl url) {
				List<Cookie> cookies = cookieStore.get(url.host());
				return cookies != null ? cookies : new ArrayList<Cookie>();
			}
		});
//		if (proxy) {
//			SocketAddress sa = new InetSocketAddress("192.168.1.6", 4780);
//			clientBuilder.proxy(new Proxy(Proxy.Type.HTTP, sa));
//		}
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
