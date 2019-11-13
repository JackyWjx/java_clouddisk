package com.jzb.media.service;

import com.jzb.base.entity.media.FileType;
import com.jzb.media.config.MediaProperties;
import com.jzb.media.util.JzbFileUtil;
import com.jzb.base.util.JzbTools;
import com.jzb.media.dao.MediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 媒体业务
 */
@Service
public class MediaService {

    @Autowired
    MediaMapper mediaMapper;


    /**
     *  添加文件
     */
    public Map< String , Object > saveMedia(MultipartFile file,String uid){
        Map< String , Object > map = new HashMap<>();
        try{
            map.put("filesize",file.getSize());
            // 获取文件尾缀
            String fileName = file.getOriginalFilename();
            String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
            //获取文件基本信息
            InputStream inputStream =  file.getInputStream();
            String fileHead = JzbFileUtil.getFileContent(inputStream);
            FileType type = JzbFileUtil.getType(fileHead);
            String  fileType = JzbFileUtil.isFileType(type);
            String fileId= JzbFileUtil.getFileId();
            String filePath= JzbFileUtil.getFilePath();
            String fileMd5 = JzbFileUtil.SHA256(new String (file.getBytes(),"UTF-8"));
            map.put("filemd5",fileMd5);
            List<Map<String , Object>> list =  mediaMapper.queryMedia(map);
            if(list.size() == 0){
                //保存文件
                String fileFullName = MediaProperties.getImageadderss() +  filePath + "." + fileFormat;
                File resourceInfoFile = new File(fileFullName);
                resourceInfoFile.getParentFile().mkdirs();
                file.transferTo(resourceInfoFile);
                resourceInfoFile.setExecutable(true, false);
                resourceInfoFile.setReadable(true, false);
                filePath = filePath.replace("\\","/");
                // 添加文件信息
                map.put("fileid", fileId);
                map.put("filepath",filePath+ "." + fileFormat);
                map.put("fileformat", fileFormat);
                map.put("filename", file.getOriginalFilename());
                map.put("filetype", fileType);
                map.put("addtime",System.currentTimeMillis());
                map.put("status", "1");
                map.put("uid", uid);
                // 插入数据库
                int i = mediaMapper.insertMeduia(map);
                map =  i > 0 ? map : null ;
            }else{
                map =   list.get(list.size() -1);
            }
        }catch (IOException e){
            JzbTools.logInfo(e);
            map.clear();
            map.put("errpr","file is error");
        }catch (Exception e){
            JzbTools.logError(e);
            map.clear();
            map.put("error","para is error");
        }
        return map;
    }

    /**
     * 查询
     */
    public List<Map<String , Object>>  queryMedia(Map<String , Object> map){
        List<Map<String , Object>> resumap;
        try {
            resumap = mediaMapper.queryMedia(map);
        }catch (Exception e){
            JzbTools.logError(e);
            resumap = null;
        }
        return resumap;
    }

}
