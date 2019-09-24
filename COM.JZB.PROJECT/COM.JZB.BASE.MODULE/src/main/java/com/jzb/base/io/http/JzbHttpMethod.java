package com.jzb.base.io.http;

/**
 * HTTP请求方式
 * @author Chad
 * @date 2019年08月23日
 */
public enum JzbHttpMethod {
    GET(1),
    POST(2),
    PUT(3),
    DELETE(4),
    PATCH(5),
    HEAD(6),
    OPTIONS(7),
    TRACE(8);

    /**
     * 存储值
     */
    private final int method;

    /**
     * 请求方法
     * @param value
     */
    private JzbHttpMethod(int value) {
        method = value;
    } // End JzbHttpMethod

    /**
     * 是否合法
     * @return
     */
    public final boolean hasBody() {
        return method >= 1 && method <= 8;
    } // End hasBody
} // End enum JzbHttpMethod
