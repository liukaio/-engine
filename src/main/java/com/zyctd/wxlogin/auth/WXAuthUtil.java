package com.zyctd.wxlogin.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 公用网络请求工具类
 */

public class WXAuthUtil {

    public static CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    public static JSONObject doGetJson(String url) throws ClientProtocolException, IOException {
        JSONObject jsonObject = null;
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if(entity != null)
        {
            //把返回的结果转换为JSON对象
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSON.parseObject(result);
        }

        return jsonObject;
    }

}
