package com.jzb.media.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.media.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
