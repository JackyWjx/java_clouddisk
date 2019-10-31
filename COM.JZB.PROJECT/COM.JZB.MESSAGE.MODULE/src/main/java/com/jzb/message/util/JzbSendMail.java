package com.jzb.message.util;

import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.util.JzbTools;
import com.jzb.message.message.MssageInfo;
import net.sf.json.JSONObject;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @Description: 邮件工具类
 * @Author Han Bin
 */
public final class JzbSendMail {
    /**
     * 私有构造 不允许实例
     */
    private JzbSendMail(){

    }

    /***
     *  发送邮件
     *
     *  info   邮件消息体
     */
    public static boolean sendMime(MssageInfo info)throws Exception{
        boolean result;
        try{
            Map<String , Object> configjson = (Map)info.getItem("config").getObject();
            JSONObject parajson = JSONObject.fromObject(configjson.get("context"));            //连接邮件服务器的参数配置
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", parajson.get("validate").toString());
            props.setProperty("mail.transport.protocol", parajson.get("smtp").toString());
            props.setProperty("mail.smtp.host", parajson.get("smtphost").toString());
            props.setProperty("mail.smtp.port", parajson.get("port").toString());
//            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
            props.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
            Session session = Session.getInstance(props);
            Message msg = getMimeMessage(session,info);
            if(!JzbTools.isEmpty(msg)){
                Transport transport = session.getTransport();
                //设置发件人的账户名和密码
                String name =  parajson.get("username").toString();
                String pwd = parajson.get("password").toString();
                transport.connect(name,pwd);
                transport.sendMessage(msg,msg.getAllRecipients());
                transport.close();
                result = true;
            }else{
                result = false;
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    /***
     *  创建一封邮件
     *
     *  session 邮件配置
     *  info  邮件消息体
     */
    private static MimeMessage getMimeMessage(Session session,MssageInfo info) throws Exception{
        MimeMessage msg = new MimeMessage(session);
        try{
            Map<String , Object> parajson = (Map)info.getItem("para").getObject();
            Map<String , Object> config = (Map)info.getItem("config").getObject();
            JSONObject configjson = JSONObject.fromObject(config.get("context"));            //连接邮件服务器的参数配置
            msg.setFrom(new InternetAddress(configjson.get("username").toString()));
            if(!JzbTools.isEmpty(configjson.get("receiver").toString())){
                String[] strings = configjson.get("receiver").toString().split(",");
                for(int i = 0; i <strings.length ;i++){
                    msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(strings[i]));
                }
            }
            if(!JzbTools.isEmpty(configjson.get("copier").toString())){
                String[] strings = configjson.get("copier").toString().split(",");
                for(int i = 0; i < strings.length ;i++){
                    msg.setRecipient(MimeMessage.RecipientType.CC,new InternetAddress(strings[i]));
                }
            }
            if(!JzbTools.isEmpty(configjson.get("secretSendOff").toString())){
                String[] strings = configjson.get("secretSendOff").toString().split(",");
                for(int i = 0; i < strings.length ;i++){
                    msg.setRecipient(MimeMessage.RecipientType.BCC,new InternetAddress(strings[i]));
                }
            }
            //设置参数
            msg.setSubject(configjson.get("title").toString(),"UTF-8");
            MimeBodyPart text = new MimeBodyPart();
            if(configjson.get("ishtml").toString().equals("true")){
                text.setContent(info.getItem("temp").getString(), "text/html;charset=UTF-8");
            }else{
                text.setText(info.getItem("temp").getString());
            }
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text);
            msg.setContent(mm);
            msg.setSentDate(new Date());
            if(!JzbTools.isEmpty(parajson.get("datetime"))){
                msg.setSentDate(JzbDateUtil.getDate(parajson.get("datetime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
            }
        }catch (Exception e){
            msg = null;
        }
        return msg;
    }

}
