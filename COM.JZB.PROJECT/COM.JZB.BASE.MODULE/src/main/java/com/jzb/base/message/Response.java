package com.jzb.base.message;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 返回数据
 *
 * @param <T>
 * @author Chad
 */
public class Response<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1738103914765001323L;

    /**
     * 返回实体类
     */
    private T entity;

    /**
     * 分页实体对角
     */
    private PageInfo<T> pageInfo;

    /**
     * 接口返回結果码
     */
    private ServerResult serverResult;

    /**
     * Token
     */
    private String token;

    /**
     * session
     */
    private String session;

    /**
     * 构造对象
     */
    public Response() {
        this.serverResult = getServerResult();
    } // End Response

    /**
     * 构造对象
     *
     * @param t
     */
    public Response(T t) {
        this.entity = t;
        this.serverResult = getServerResult();
    } // End Response

    /**
     * 构对对象
     *
     * @param list
     */
    public Response(List<T> list) {
        if (list instanceof Page) {
            this.pageInfo = new PageInfo<T>(list);
        } else {
            this.pageInfo = new PageInfo<T>();
            this.pageInfo.setList(list);
            this.pageInfo.setTotal(null == list ? 0 : list.size());
        }
        this.serverResult = getServerResult();
    } // End Response

    /**
     * 构造对象
     *
     * @param resultCode
     * @param resultMessage
     */
    public Response(int resultCode, String resultMessage) {
        this.serverResult = new ServerResult(resultCode);
        serverResult.setMessage(resultMessage);
    } // End Response

    /**
     * 获取服务的结果集
     *
     * @return
     */
    public ServerResult getServerResult() {
        /**
         * 默认为成功
         */
        if (serverResult == null) {
            serverResult = new ServerResult(200);
        }
        return serverResult;
    } // End getServerResult

    /**
     * 成功请求响应
     * @return
             */
    public static Response getResponseSuccess() {
        return new Response(JzbReturnCode.HTTP_200, "OK");
    } // End getResponseSuccess

    /**
     * 成功请求响应
     * @return
     */
    public static Response getResponseSuccess(Map<String, Object> userInfo) {
        Response res = new Response(JzbReturnCode.HTTP_200, "OK");
        res.setSession(userInfo.containsKey("session") ? userInfo.get("session").toString() : "");
        res.setToken(userInfo.containsKey("token") ? userInfo.get("token").toString() : "");
        return res;
    } // End getResponseSuccess

    /**
     * 成功请求响应
     * @return
     */
    public static Response getResponseSuccess(String token, String session) {
        Response res = new Response(JzbReturnCode.HTTP_200, "OK");
        res.setSession(session);
        res.setToken(token);
        return res;
    } // End getResponseSuccess

    /**
     * 失败请求响应
     * @return
     */
    public static Response getResponseError() {
        return new Response(JzbReturnCode.HTTP_404, "FAILED");
    } // End getResponseError

    /**
     * 请求超时响应
     * @return
     */
    public static Response getResponseTimeout() {
        return new Response(JzbReturnCode.HTTP_410, "FAILED");
    } // End getResponseTimeout

    /**
     * 设置结果集
     *
     * @param serverResult
     */
    public void setServerResult(ServerResult serverResult) {
        this.serverResult = serverResult;
    } // End setServerResult

    /**
     * 获取结果实体
     *
     * @return
     */
    public T getResponseEntity() {
        return entity;
    } // End getResponseEntity

    /**
     * 设置结果实体
     *
     * @param entity
     */
    public void setResponseEntity(T entity) {
        this.entity = entity;
    } // End setResponseEntity

    /**
     * 获取页面信息
     *
     * @return
     */
    public PageInfo<T> getPageInfo() {
        return pageInfo;
    } // End getPageInfo

    /**
     * 设置页面信息
     *
     * @param pageInfo
     */
    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
    } // End setPageInfo

    /**
     * 设置Token
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    } // End setToken

    /**
     * 获取Token
     * @return
     */
    public String getToken() {
        return token;
    } // End getToken

    /**
     * 设置Session
     * @param session
     */
    public void setSession(String session) {
        this.session = session;
    } // End setSession

    /**
     * 获取Session
     * @return
     */
    public String getSession() {
        return session;
    } // End getSession
} // End class Response
