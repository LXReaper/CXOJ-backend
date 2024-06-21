package com.yp.CXOJ.utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * @author 余炎培
 * @since 2024-06-08 星期六 20:28:29
 */
public class HttpRequestUtil {
    public static HttpResponse doGet(String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);//生成一个请求
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return httpResponse;
    }
}
