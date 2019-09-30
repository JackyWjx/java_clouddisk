package com.jzb.media.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.media.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 媒体服务controller
 */
@RestController
@RequestMapping("/media")
@CrossOrigin
public class MediaController {

    @Autowired
    MediaService mediaService;

    @RequestMapping("/upDownload")
    @ResponseBody
    public void  upDownload(@RequestBody Map<String , Object> paraMap){
        File file;
        FileInputStream fis = null;
        ServletOutputStream out = null;
        HttpServletResponse res = null;
        try{
            JzbTools.logInfo("fileId ==============>>",paraMap.get("fileid"));
            List<Map<String , Object>> list = mediaService.queryMedia(paraMap);
            JzbTools.logInfo("path ==============>>",list.get(0).get("filepath"));
            JzbTools.logInfo("fileName ==============>>",list.get(0).get("filename"));
            res.setCharacterEncoding("UTF-8");
            res.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(list.get(0).get("filename").toString(),"UTF-8"));
            file = new File("/opt/jzb/v3/software/nginx/html/static"+list.get(0).get("filepath"));
//            file = new File("D:\\nginx\\nginx-1.17.3\\nginx-1.17.3\\html\\images\\短信平台相关.xls");
            fis =  new FileInputStream(file);
            //写出
            out = res.getOutputStream();
            //定义读取缓冲区
            byte buffer[] = new byte[1024];
            //定义读取长度
            int len = 1024;
            //循环读取
            while((len = fis.read(buffer))!=-1){
                out.write(buffer,0,len);
            }
        }catch (Exception e){

        }finally {
            try{
                JzbTools.logInfo("资源 ==============>> 释放");
                //释放资源
                fis.close();
                out.close();
            }catch (Exception e){
                JzbTools.logError(e);
            }
        }
    }

    /**
     *  文件上传
     */
    @RequestMapping(value = "/upToCache" ,method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response fileUpAdderss(@RequestBody MultipartFile file,@RequestParam(value = "uid" ,required = false) String uid){
        Response response;
        try{
            Map< String , Object > map =   mediaService.saveMedia(file,uid);
            response = !JzbTools.isEmpty(map) ? Response.getResponseSuccess() : Response.getResponseError();
            response.setResponseEntity(map);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }
}
