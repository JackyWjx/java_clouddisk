package com.jzb.base.util;

import com.jzb.base.data.code.JzbBase64;
import okhttp3.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 认证识别操作
 * <p>
 * 针对身份证,营业执照读取识别,根据企业名称获取企业数据的一些常用工具实现。<br>
 *
 * @author kuangbin
 * @date 2019年8月12日
 * @see
 * @since 1.0
 */
public class JzbIdEntification {
    /**
     * 获取身份证信息
     *
     * @param image
     * @return
     * @throws Exception
     */
    public static String getIdCardData(String image, String front) throws Exception {
        OkHttpClient client = new OkHttpClient();
        // 编辑请求体内容
        RequestBody body = new FormBody.Builder()
                .add("detect_direction", "false")
                .add("detect_risk", "false")
                .add("id_card_side", front)
                .add("image", image)
                .build();
        // 发送请求
        Request request = new Request.Builder()
                .url("http://shenfenzhe.market.alicloudapi.com/do")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "APPCODE 911fbd59868f4791919dfb91dd384fc6")
                .build();
        Response response = client.newCall(request).execute();
        // 获取返回对象正文中的信息
        String responseBody = response.body().string();
        return responseBody;
    }

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回Base64编码过的字节数组字符串
        return JzbBase64.encode(data);
    }

    /**
     * 根据企业名称获取企业注册信息
     *
     * @param name
     * @return
     */
    public static String getCompanyData(String name) {
        String responseBody = "";
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            // 请求企业信息的地址
            String url = " http://cardnotwo.market.alicloudapi.com/company";

            // 编辑请求体内容
            RequestBody body = new FormBody.Builder()
                    .add("com", name)
                    .build();

            // .Builder是Request内部类 .url()返回Builder .build()返回Request对象  .addHeader添加键值对header信息
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "APPCODE 911fbd59868f4791919dfb91dd384fc6")
                    .build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            // 获取响应正文中的数据
            responseBody = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    /**
     * 根据营业执照获取企业信息
     *
     * @param image
     * @return
     */
    public static String getLicenseData(String image) throws IOException {
        OkHttpClient  client  =  new  OkHttpClient();
        // 设置请求实体内容
        RequestBody body=new FormBody.Builder()
                .add("AI_BUSINESS_LICENSE_IMAGE",image)
                .add("AI_BUSINESS_LICENSE_IMAGE_TYPE","0")
                .build();
        // 设置请求内容
        Request  request  =  new  Request.Builder()
                .url("http://blicence.market.alicloudapi.com/ai_business_license")
                .post(body)
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("Authorization","APPCODE 911fbd59868f4791919dfb91dd384fc6")
                .build();
        Response  response  =  client.newCall(request).execute();
        // 获取返回实体内容
        String  responseBody  =  response.body().string();
        return  responseBody;
    }
}
