package com.jzb.message.util;

import com.jzb.base.util.JzbTools;
import com.jzb.message.config.MqttGateway;
import com.jzb.message.config.MqttSenderConfig;
import com.jzb.message.message.MssageInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Description: 推送消息
 * @Author Han Bin
 */
public class JzbSendSys {

    /**
     * logback日志
     */
    private final static Logger logger = LoggerFactory.getLogger(JzbSendSys.class);

    public static boolean sendSysMq(MssageInfo msg, MqttGateway mqttGateway){
        boolean result ;
        try{
            mqttGateway.sendToMqtt(msg.getItem("temp").getString(),msg.getItem("receive").getString());
            result = true;
        }catch (Exception e){
            JzbTools.logError(e);
            result = false;
        }
        return result;
    }

}
