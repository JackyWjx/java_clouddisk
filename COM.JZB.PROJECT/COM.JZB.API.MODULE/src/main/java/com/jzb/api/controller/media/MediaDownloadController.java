package com.jzb.api.controller.media;

import com.jzb.api.api.media.MediaApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * @Description: 媒体服务
 * @Author Han Bin
 */
@RestController
@RequestMapping(value = "/api/media")
public class MediaDownloadController {

    @Autowired
    MediaApi api;

    @RequestMapping("/upDownload")
    @ResponseBody
    public void  upDownload( HttpServletRequest req,HttpServletResponse res){
        Map<String , Object> paraMap =   new HashMap<>();
        paraMap.put("fileid",req.getParameter("fileId"));
        api.upDownload(paraMap);
    }

}
