package com.jzb.api.filter;

import com.jzb.api.api.auth.UserAuthApi;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 全局路由
 */
@Component
public class JzbTokenGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 用户认证
     */
    @Autowired
    private UserAuthApi authApi;

    /**
     * 过滤器处理
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest httpRequest = exchange.getRequest();
            JzbTools.logInfo("======================>> Token Filter: >>", httpRequest.getPath().value());

            // 白名单
            if (isWhiteList(httpRequest.getPath().value())) {
                return chain.filter(exchange);
            } else {
                String method = httpRequest.getMethodValue();

                // 只处理POST的请求,GET请求待定
                if ("POST".equals(method)) {
                    // 从请求里获取Post请求体 验证TOKEN
                    String token = httpRequest.getHeaders().get("token").get(0);
                    Map<String, Object> tokenParam = new HashMap<>();
                    tokenParam.put("token", token);
                    Response tokenRes = authApi.checkToken(tokenParam);
                    if (tokenRes.getServerResult().getResultCode() == JzbReturnCode.HTTP_200) {
                        ServerRequest serverRequest = new DefaultServerRequest(exchange);
                        Mono<String> bodyToMono = serverRequest.bodyToMono(String.class);
                        return bodyToMono.flatMap(body -> {
                            exchange.getAttributes().put("cachedRequestBody", body);
                            JzbTools.logInfo("===================================cachedRequestBody>>", body);

                            JSONObject json = JzbTools.isEmpty(body) ? new JSONObject() : JSONObject.fromObject(body);
                            Map<String, Object> userInfo = (Map<String, Object>) tokenRes.getResponseEntity();
                            userInfo.put("token", tokenRes.getToken() == null ? "" : tokenRes.getToken());
                            userInfo.put("session", tokenRes.getSession() == null ? "" : tokenRes.getSession());
                            userInfo.put("msgTag", JzbRandom.getRandomCharCap(4));
                            userInfo.put("ip", httpRequest.getURI().getHost());
                            userInfo.put("tkn", token);
                            json.put("userinfo", userInfo);
                            byte[] newBody = json.toString().getBytes(Charset.forName("UTF-8"));

                            ServerHttpRequest newRequest = new ServerHttpRequestDecorator(httpRequest) {
                                @Override
                                public HttpHeaders getHeaders() {
                                    HttpHeaders headers = new HttpHeaders();
                                    List<String> cl = new ArrayList<>();
                                    cl.add(newBody.length + "");
                                    headers.put("Content-Length", cl);

                                    List<String> ct = new ArrayList<>();
                                    ct.add("application/json");
                                    headers.put("Content-Type", ct);

                                    headers.putAll(super.getHeaders());
                                    headers.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                                    return headers;
                                }

                                @Override
                                public Flux<DataBuffer> getBody() {
                                    NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                                    DataBuffer bodyDataBuffer = nettyDataBufferFactory.wrap(newBody);
                                    return Flux.just(bodyDataBuffer);
                                }
                            };
                            return chain.filter(exchange.mutate().request(newRequest).build());
                        });
                    } else {
                        JzbTools.logInfo("======================>>", "ERROR", token, "TOKEN IS NULL");
                        ServerHttpResponse serverHttpResponse = exchange.getResponse();
                        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                        JSONObject response = new JSONObject();
                        JSONObject result = new JSONObject();
                        result.put("resultCode", JzbReturnCode.HTTP_410);
                        result.put("message", "TIMEOUT");
                        response.put("serverResult", result);
                        byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                        return exchange.getResponse().writeWith(Flux.just(buffer));
                    }
                } else if ("GET".equals(method)) {
                    JzbTools.logInfo("=======================================>> GET ");
                    byte[] data = getErrorReturn(exchange.getResponse());
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(data);
                    return exchange.getResponse().writeWith(Flux.just(buffer));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            byte[] data = getErrorReturn(exchange.getResponse());
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(data);
            return exchange.getResponse().writeWith(Flux.just(buffer));
        }
        return chain.filter(exchange);
    } // End filter

    private static Map<String, String> GATEWAY_WHITE_LIST = new HashMap<>();

    static {
        // begin czd

        //---- begin activity
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/queryActivityList", "");
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/queryActpictureList", "");
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/getDiscuss", "");
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/findParticularsList", "");
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/findParticularsByList", "");
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/getLikeName", "");
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/addActivityVotes", "");
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/getResourceVotes", "");
        GATEWAY_WHITE_LIST.put("/JZB-ACTIVITY/activity/addActivityReads", "");
        //---- end activity

        //---- begin operate
        GATEWAY_WHITE_LIST.put("/JZB-OPERATE/resourceVotes/getResourceVotes", "");
        //---- end operate

        //---- begin resource
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/solutionType/getSolutionType", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/solutionDom/getSolutionDom", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/solutionDom/getSolutionDomByDomid", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/standardDocument/getFatherOne", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/policyType/getPolicyType", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/policyDom/getPolicyDomDesc", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/policyDom/getPolicyHotDom", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/policyDom/getPolicyDomList", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/standardDocument/getDocumentsList", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/standardDocument/getStandartDocumentDesc", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/standardDocument/getStandartHotDom", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/resource/systemMenu/getProductSystemMenuList", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/resource/systemMenu/addProductSystemMenuList", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/resource/systemMenu/editProductSystemMenuList", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/advertising/getAdvertListPass", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/ProductResList/saveFile", "");
        GATEWAY_WHITE_LIST.put("/JZB-RESOURCE/ProductResList/creatFile", "");

        //---- end resource

        //---- begin config
        GATEWAY_WHITE_LIST.put("/JZB-CONFIG/config/city/getCityList", "");
        //---- end config

        //---- begin org
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/getProductLsit", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/projectType/getProjectTypeList", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/projectType/getProjectMethodList", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/tender/getTenderList", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/tenderResult/getTenderResultList", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/tenderResultDesc/getTenderResultDesc", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/tenderDesc/getTenderDesc", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/company/importCompanyProject", "");

        //---- end org

        // end czd


        // begin dsc

        //---- begin org
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/getEnterpriseNames", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/dept/importUserInfo", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/dept/importUserAll", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/importMenuExcel", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/importPageExcel", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/importControlExcel", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/importCompanyTemplate", "");
        GATEWAY_WHITE_LIST.put("/JZB-ORG/org/importCompanyCommon", "");
        //---- end org

        //---- begin auth
        GATEWAY_WHITE_LIST.put("/JZB-AUTH/auth/modifyPassword", "");
        //---- end auth

        // end dsc


        // begin hb

        //---- begin media
        GATEWAY_WHITE_LIST.put("/JZB-MEDIA/media/upToCache", "");
        GATEWAY_WHITE_LIST.put("/JZB-MEDIA/media/upToUeditor", "");
        GATEWAY_WHITE_LIST.put("/JZB-MEDIA/media/upDownload", "");
        //---- end media


        //---- begin message
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/SMSService/SendMsg", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/queryMessageGroup", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/queryMessageUserGroup", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/queryMsgGroupConfigure", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/saveMessageGroup", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/saveMessageUserGroup", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/saveMsgGroupConfigure", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/upMessageGroup", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/upMessageUserGroup", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/upMsgGroupConfigure", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/removerMessageGroup", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/removerMessageUserGroup", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/removerMsgGroupConfigure", "");

        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/queryMsgTypePara", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/queryUserPara", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/queryServiceProviders", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/saveMsgTypePara", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/saveUserPara", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/saveServiceProviders", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/upMsgTypePara", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/upUserPara", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/upServiceProviders", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/removeMsgTypePara", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/removeUserPara", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/para/removeServiceProviders", "");

        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/sendShortMsg", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/cancelSend", "");


        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/template/queryMsgGroupTemplate", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/template/saveMsgGroupTemplate", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/template/upMsgGroupTemplate", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/group/template/removeMsgGroupTemplate", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/list/queryMsgList", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/type/queryMsgType", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/type/saveMsgType", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/type/upMsgType", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/type/removeMsgType", "");
        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/message/type/queryMsgType", "");


        GATEWAY_WHITE_LIST.put("/JZB-MESSAGE/senduser/message/querySendUserMessage", "");
        //---- end message

        // end hb
    }

    /**
     * 白名单URL
     *
     * @param url
     * @return
     */
    private boolean isWhiteList(String url) {
        return GATEWAY_WHITE_LIST.containsKey(url);
    } // End isWhileList

    /**
     * 获取错误返回数据
     *
     * @param serverHttpResponse
     * @return
     */
    private byte[] getErrorReturn(ServerHttpResponse serverHttpResponse) {
        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        JSONObject response = new JSONObject();
        JSONObject result = new JSONObject();
        result.put("resultCode", JzbReturnCode.HTTP_404);
        result.put("message", "ERROR");
        response.put("serverResult", result);
        return response.toString().getBytes(StandardCharsets.UTF_8);
    } // End getErrorReturn

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     *
     * @return 请求体
     */
    private String getRequestBody(ServerHttpRequest httpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = httpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            JzbTools.logInfo("========================>>", "getRequestBody", charBuffer.toString());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        // 获取request body
        return bodyRef.get();
    } // End requestBody

    /**
     * 字符串转请求参数
     *
     * @param value
     * @return
     */
    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    } // End stringBuffer

    @Override
    public int getOrder() {
        return 1;
    }
}