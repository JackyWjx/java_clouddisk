package com.jzb.message.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.jzb.base.util.JzbTools;
import com.jzb.message.message.MssageInfo;
import com.sun.nio.sctp.MessageInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Description: 待发送短信
 * @Author Han Bin
 */
public class JzbSendMsg {

    /**
     * logback日志
     */
    private final static Logger logger = LoggerFactory.getLogger(JzbSendMsg.class);

    /**
     *  发送短息
     */
    public static String sendShortMessage(MssageInfo info){
        String result = "error";
        try{
            Map<String , Object> config = (Map<String , Object>)info.getItem("config").getObject();
            JSONObject parajson = JSONObject.fromObject(config.get("context"));
            DefaultProfile profile = DefaultProfile.getProfile("default", parajson.getString("appid"), parajson.getString("sercet"));
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "default");
            request.putQueryParameter("PhoneNumbers",info.getItem("receive").getString());
            request.putQueryParameter("SignName", parajson.getString("title"));
            request.putQueryParameter("TemplateCode",parajson.getString("sms_no"));
            if(!JzbTools.isEmpty(info.getItem("temp").getString())){
                request.putQueryParameter("TemplateParam",info.getItem("temp").getString());
            }
            try {
                CommonResponse response = client.getCommonResponse(request);
                result = response.getData();
                logger.info("发送状态result    =====>"+result);
            }catch (Exception e){
                e.printStackTrace();
                logger.info(" 发送错误  =====>");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info(" 短信参数错误    =====>");
        }
        return result;
    }

}
