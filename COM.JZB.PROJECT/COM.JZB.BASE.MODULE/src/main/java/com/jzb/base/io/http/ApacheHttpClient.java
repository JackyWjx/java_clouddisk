package com.jzb.base.io.http;

import com.jzb.base.util.JzbTools;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Apache Http Request Operater
 * @author Chad
 * @date 2019-08-24
 */
public final class ApacheHttpClient {
    /**
     * Private Constructor
     */
    private ApacheHttpClient() {
    } // End ApacheHttpClient

    /**
     * http get method request
     * @return Map response data
     */
    public static Map<String, String> httpGet(String url, Map<String, String> heads, Map<String, String> cookies, Map<String, String> param) {
        Map<String, String> result;
        try {
            // Create new httpClient instance
            CookieStore cookie = new BasicCookieStore();
            CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookie).build();

            // Set request parameter
            String paramStr = "";
            if (param != null && param.size() > 0) {
                List<NameValuePair> reqParam = new ArrayList<>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    reqParam.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                paramStr = EntityUtils.toString(new UrlEncodedFormEntity(reqParam, Charset.forName("UTF-8")));
            }
            HttpGet httpGet = new HttpGet(url + (JzbTools.isEmpty(paramStr) ? "" : "?" + paramStr));

            // Set message head
            if (heads != null && heads.size() > 0) {
                for (Map.Entry<String, String> entry : heads.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            // Set request cookies
            if (cookies != null && cookies.size() > 0) {
                for (Map.Entry<String, String> entry : cookies.entrySet()) {
                    BasicClientCookie cook = new BasicClientCookie(entry.getKey(), entry.getValue());
                    cookie.addCookie(cook);
                }
            }

            // Send request message. Get return response
            HttpClientContext context = HttpClientContext.create();
            CloseableHttpResponse response = client.execute(httpGet, context);

//            if (response.getStatusLine().getStatusCode())
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = new HashMap<>();

                // Get response body, And response cookies
                result.put("body", EntityUtils.toString(entity, Charset.forName("UTF-8")));
                List<Cookie> resCookies = context.getCookieStore().getCookies();
                for (int i = 0, l = resCookies.size(); i < l; i++) {
                    Cookie ck = resCookies.get(i);
                    result.put(ck.getName(), ck.getValue());
                }
                EntityUtils.consume(entity);
            } else {
                result = null;
            }
            response.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    } // End httpGet

    /**
     * Http post method request
     * @return Map response data
     */
    public static Map<String, String> httpPost(String url, Map<String, String> heads, Map<String, String> cookies, Map<String, String> param) {
        Map<String, String> result;
        try {
            // Create new httpClient instance
            CookieStore cookie = new BasicCookieStore();
            CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookie).build();
            HttpPost httpPost = new HttpPost(url);

            // Set message head
            if (heads != null && heads.size() > 0) {
                for (Map.Entry<String, String> entry : heads.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            // Set request cookies
            if (cookies != null && cookies.size() > 0) {
                for (Map.Entry<String, String> entry : cookies.entrySet()) {
                    BasicClientCookie cook = new BasicClientCookie(entry.getKey(), entry.getValue());
                    cookie.addCookie(cook);
                }
            }

            // Set request parameter
            if (param != null && param.size() > 0) {
                List<NameValuePair> reqParam = new ArrayList<>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    reqParam.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(reqParam, Charset.forName("UTF-8")));
            }

            // Send request message. Get return response
            HttpClientContext context = HttpClientContext.create();
            CloseableHttpResponse response = client.execute(httpPost, context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = new HashMap<>();

                // Get response body, And response cookies
                result.put("body", EntityUtils.toString(entity, Charset.forName("UTF-8")));
                List<Cookie> resCookies = context.getCookieStore().getCookies();
                for (int i = 0, l = resCookies.size(); i < l; i++) {
                    Cookie ck = resCookies.get(i);
                    result.put(ck.getName(), ck.getValue());
                }
                EntityUtils.consume(entity);
            } else {
                result = null;
            }
            response.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    } // End httpPost
} // End class ApacheHttpClient
