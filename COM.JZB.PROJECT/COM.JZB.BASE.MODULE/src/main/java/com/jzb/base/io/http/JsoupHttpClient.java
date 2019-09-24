package com.jzb.base.io.http;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP / HTTPS 请求数据操作封装
 *
 * @author Chad
 * @date 2019年08月23日
 */
public final class JsoupHttpClient {
    /**
     * 私有构造方法
     */
    private JsoupHttpClient() {
    } // End JsoupHttpClient

    /**
     * 执行请求，返回结果
     *
     * @param url     请求URL
     * @param head    请求消息头
     * @param param   请求参数
     * @param timeout 请求超时时间
     * @return Map 返回请求结果
     */
    public static Map<String, String> request(String url, JzbHttpMethod method, Map<String, String> head, Map<String, String> cookies, Map<String, String> param, int timeout) {
        Map<String, String> result;
        try {
            // 创建连接
            Connection connect = Jsoup.connect(url);

            // 设置消息头
            if (head != null && head.size() > 0) {
                connect.headers(head);
            }

            // 设置Cookies
            if (cookies != null && cookies.size() > 0) {
                connect.cookies(cookies);
            }

            // 请求并获取响应
            connect.ignoreContentType(true).timeout(timeout);
            if (param != null && param.size() > 0) {
                connect.data(param);
            }

            switch (method) {
                case GET:
                    connect.method(Method.GET);
                    break;
                case POST:
                    connect.method(Method.POST);
                    break;
                case DELETE:
                    connect.method(Method.DELETE);
                    break;
                case HEAD:
                    connect.method(Method.HEAD);
                    break;
                case PUT:
                    connect.method(Method.PUT);
                    break;
                case PATCH:
                    connect.method(Method.PATCH);
                    break;
                case OPTIONS:
                    connect.method(Method.OPTIONS);
                    break;
                case TRACE:
                    connect.method(Method.TRACE);
                    break;
                default:
                    break;
            }

            // 获取Cookies和Body数据进行返回
            Response response = connect.execute();
            result = response.cookies();
            result.put("body", response.body());
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new HashMap<>();
        }
        return result;
    } // End request

    public static Map<String, String> httpGet(String url, Map<String, String> heads, Map<String, String> cookies, Map<String, String> params) {
        Map<String, String> result;
        try {
//            Connection connect = Jsoup.connect("http://localhost:7102/oauth/authorize?client_id=UserManagement&redirect_uri=http://localhost:8082/memberSystem/login&response_type=code");
//            connect.header("User-Agent",
//                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
//            // 设置cookie和post上面的map数据
//            Response token = connect.ignoreContentType(true).method(Method.GET).cookies(cookies).execute();
            result = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    }
} // End class JsoupHttpClient
