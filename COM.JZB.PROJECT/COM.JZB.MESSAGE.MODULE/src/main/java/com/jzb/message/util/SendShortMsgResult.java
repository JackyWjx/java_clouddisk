package com.jzb.message.util;

import java.util.Map;

/**
 * @Description: 返回信息
 * @Author Han Bin
 */
public class SendShortMsgResult {

    /**
     * 返回状态码
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private Map<String , Object> data ;

    /**
     * 成功
     */
    public static SendShortMsgResult  getSuccess(){
        SendShortMsgResult result  =  new  SendShortMsgResult();
        result.setMessage("success");
        result.setCode("200");
        return result;
    }

    /**
     * 失败
     */
    public static SendShortMsgResult  getRerror(){
        SendShortMsgResult result  =  new  SendShortMsgResult();
        result.setMessage("error");
        result.setCode("500");
        return result;
    }

    @Override
    public String toString() {
        return "SendShortMsgResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
