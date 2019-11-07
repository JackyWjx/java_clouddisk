package com.jzb.logger.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.logger.service.ElasticService;
import com.jzb.logger.service.ErrorService;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/10/30 20:54
 */
@RestController
@RequestMapping(value = "/logger")
public class ElasticController {

    @Autowired
    private ElasticService elasticService;

    @Autowired
    private ErrorService errorService;

    /**
     * 处理elastic中的数据
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/addDealElastic", method = RequestMethod.POST)
    @CrossOrigin
    public Response addDealElastic(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            param.put("index", "dsclogstash-");
            param.put("isdeal", "1");
            param.put("scheme", "http");
            ElasticSearch elasticSearch = new ElasticSearch(param, 1);
            elasticSearch.start();
            result = Response.getResponseSuccess();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 告警启动接口
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/queryElasticErrorLog", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryElasticErrorLog(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            param.put("index", "dsclogstash-");
            param.put("scheme", "http");
            ElasticSearch elasticSearch = new ElasticSearch(param, 2);
            elasticSearch.start();
            //发送短信
            ElasticSearch elastic = new ElasticSearch(param, 3);
            elastic.start();
            result = Response.getResponseSuccess();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 开启线程
     *
     * @param
     * @Author: DingSC
     * @return
     */
    class ElasticSearch extends Thread {
        private Map<String, Object> param;

        private int type;

        ElasticSearch(Map<String, Object> maps, int sign) {
            param = maps;
            type = sign;
        }

        @Override
        public void run() {
            while (true) {
                //查询告警日志
                int sel = 2;
                //短信
                int send = 3;
                if (type == send) {
                    try {
                        errorService.sendMessage(param);
                        Thread.sleep(3600000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (type == 1) {
                    try {
                        elasticService.dealElValue(param);
                        Thread.sleep(180000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (type == sel) {
                    //查询告警日志
                    String index = JzbDataType.getString(param.get("index"));
                    int port = 9200;
                    String hostName = JzbDataType.getString(param.get("hostName"));
                    String scheme = JzbDataType.getString(param.get("scheme"));
                    String email = JzbDataType.getString(param.get("email"));
                    HttpHost httpHost = new HttpHost(hostName, port, scheme);
                    //创建RestHighLevelClient
                    //如果是集群，添加所有的HttpHost对象（以英文逗号分隔），RestClient.builder()函数的参数是数组。
                    RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(httpHost));
                    try {
                        String nowDay = errorService.getNowDay(System.currentTimeMillis(), "yyyy-MM-dd");
                        String yesterday = errorService.getYesterday(nowDay);
                        String[] str = {index + nowDay, index + yesterday};
                        for (int i = 0, size = str.length; i < size; i++) {
                            boolean flag = elasticService.isExists(client, str[i]);
                            if (flag) {
                                errorService.warnErrorLog(client, str[i], email);
                            }
                        }
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (client != null) {
                            try {
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            }
        }
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    @CrossOrigin
    public Response hello(@RequestBody Map<String, Object> param) {
        Response result;
        try {
           /* ThreadGroup currentGroup =
                    Thread.currentThread().getThreadGroup();
            int noThreads = currentGroup.activeCount();
            Thread[] lstThreads = new Thread[noThreads];
            currentGroup.enumerate(lstThreads);
            for (int i = 0; i < noThreads; i++) {
                System.out.println("线程号：" + i + " = " + lstThreads[i].getName());
            }*/
            if (JzbDataType.getInteger(param.get("type")) == 1) {
                errorService.sendMessage(param);
            } else {
                errorService.sendEmail(param, JzbDataType.getString(param.get("email")));
            }

            result = Response.getResponseSuccess();
            result.setResponseEntity("hello world");
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
