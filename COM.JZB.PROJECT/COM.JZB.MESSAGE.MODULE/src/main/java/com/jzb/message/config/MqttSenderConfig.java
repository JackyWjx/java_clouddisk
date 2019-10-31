package com.jzb.message.config;

import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageHandler;

/**
 * MQTT配置
 *
 * @author han bin
 */
@Configuration
public class MqttSenderConfig {

    /**
     * 用户名
     */
    @Value("${spring.mqtt.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${spring.mqtt.password}")
    private String password;

    /**
     * 连接地址
     */
    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(20);
        return mqttConnectOptions;
    }

    /**
     * client 工厂
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * 推送消息的配置
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        // 避免clientId重复
        String clientOutId = "PUB_"+System.currentTimeMillis() + RandomStringUtils.randomAlphanumeric(3);
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientOutId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(2);
        return messageHandler;
    }

//    /**
//     * 订阅消息的配置
//     *
//     * @return
//     */
//    @Bean
//    public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
//        // 避免clientId重复
//        String clientInId = "SUB_"+System.currentTimeMillis()+ RandomStringUtils.randomAlphanumeric(3);
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientInId,
//                mqttClientFactory());
//        adapter.setCompletionTimeout(10 * 1000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(2);
//        adapter.addTopic(defaultTopic.trim());
//
//        System.out.println("MQTT-订阅 MqttPahoMessageDrivenChannelAdapter初始化完毕,clientInId={}"+ clientInId);
//        return adapter;
//    }
//    @Bean
//    public IntegrationFlow mqttInFlow() {
//        return IntegrationFlows.from(mqttInbound()).handle(new MessageHandler() {
//            @Override
//            public void handleMessage(Message<?> message)  {
//                System.out.println("主题：{}，消息接收到的数据：{}"+ message.getHeaders().get("mqtt_receivedTopic")+message.getPayload());
//            }
//        }).get();
//    }
}