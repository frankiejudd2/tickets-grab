package com.dewbay;

import com.dewbay.util.HttpClientUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketsGrabApplicationTests {

    @Test
    public void contextLoads() {
    }

    private static void setHeaders(Map<String, String> headers, HttpRequestBase httpPost) {
        if (headers != null) {
            Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void main(String[] args) {


        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN");
        headers.put("Connection", "keep-alive");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("DNT", "1");
        headers.put("Host", "api.12306.com");
        headers.put("Origin", "http://www.12306.com");
        headers.put("Referer", "http://www.12306.com/");
        String maskIp = ((int) (Math.random() * 200) + 50) + "." + ((int) (Math.random() * 200) + 50) + "." + ((int) (Math.random() * 200) + 50) + "." + ((int) (Math.random() * 200) + 50);
        headers.put("X-Real-IP", maskIp);
        //headers.put("X-Forwarded-For", maskIp);
        headers.put("User-Agent", HttpClientUtils.pcUserAgentArray[new Random().nextInt(HttpClientUtils.pcUserAgentArray.length)]);


        CloseableHttpClient httpCilent = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();
        HttpGet httpGet = new HttpGet("http://api.12306.com/v1/train/trainInfos?arrStationCode=BJP&deptDate=2018-10-09&deptStationCode=WFK&findGD=true");
        httpGet.setConfig(requestConfig);
        setHeaders(headers, httpGet);
        String srtResult = "";
        try {
            HttpResponse httpResponse = httpCilent.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
                System.out.println(srtResult);
            } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                //..........
            } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                //.............
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpCilent.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
