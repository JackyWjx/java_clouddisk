package com.jzb.api.api.media;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description: 媒体服务
 * @Author Han Bin
 */
@FeignClient(name = "jzb-media")
@RequestMapping(value = "/media")
public interface MediaApi {

    @RequestMapping("/upDownload")
    @ResponseBody
    public void  upDownload(@RequestBody Map<String , Object> paraMap);

}
