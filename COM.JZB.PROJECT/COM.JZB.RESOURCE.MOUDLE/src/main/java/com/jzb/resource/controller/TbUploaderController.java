package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.resource.service.TbDocumentMsgFileInfoService;
import com.jzb.resource.service.TbDocumentMsgUploadLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import com.jzb.base.entity.uploader.*;
/**
 * @Author wang jixiang
 * @Date 20191218
 * 体系建设-文件断点续传-上传文件
 */
@RestController
@RequestMapping(value = "/resource/uploader")
public class TbUploaderController {
    @Autowired
    private TbDocumentMsgUploadLogService tbDocumentMsgUploadLogService;

    @Autowired
    private TbDocumentMsgFileInfoService tbDocumentMsgFileInfoService;

    private String uploadFolder="F://files";//配置文件存放路径

    //白名单  上传文件
    @PostMapping("/chunk")
    @CrossOrigin
    public String uploadChunk(Chunk chunk) {
        MultipartFile file = chunk.getFile();
        System.out.println("file originName: {}, chunkNumber: {}"+ file.getOriginalFilename()+ chunk.getChunkNumber()+chunk.getRelativePath());
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(generatePath(uploadFolder, chunk));
            //文件写入指定路径
            Files.write(path, bytes);
            System.out.println("文件 {} 写入成功, uuid:{}"+ chunk.getFilename()+ chunk.getIdentifier());
            String fileName = chunk.getFilename();
            chunk.setType(fileName.substring(fileName.lastIndexOf(".") + 1));
            chunk.setAddtime(System.currentTimeMillis());
            tbDocumentMsgUploadLogService.saveChunk(chunk);
            System.out.println(chunk.toString());
            return "文件上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "后端异常...";
        }
    }

    //白名单  断点续传判断
    @GetMapping("/chunk")
    @CrossOrigin
    public Object checkChunk(Chunk chunk, HttpServletResponse response) {
        //去数据库查询是否有该条信息
        Map<String,Object> map = new HashMap<>();
        map.put("identifier",chunk.getIdentifier());
        map.put("chunkNumber",chunk.getChunkNumber());
        Chunk isNull = tbDocumentMsgUploadLogService.getBreakPointLog(map);
        if (isNull==null) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
        return chunk;
    }


    //@RequestMapping(value = "/mergeFile",method = RequestMethod.POST)
    @PostMapping("/mergeFile")
    @CrossOrigin
    public String mergeFile(FileInfo fileInfo) {
        String filename = fileInfo.getFilename();
        String file = uploadFolder + "/" + fileInfo.getIdentifier() + "/" + filename;
        String folder = uploadFolder + "/" + fileInfo.getIdentifier();
        merge(file, folder, filename);
        //看该请求传输的是文件夹还是文件
        String relativePath = fileInfo.getRelativePath();
        if(relativePath!=null){
            String[] split = relativePath.split("/");
            Map<String,Object> param = new HashMap<>();
            param.put("uid","前端传uid");
            param.put("addtime",System.currentTimeMillis());
            param.put("uniquefileid", JzbRandom.getRandomChar(19));
            for(int k = 0, c = split.length-1;k < c;k ++){
                param.put("name",split[k]);
                param.put("href",fileInfo.getHref());
                param.put("path",fileInfo.getHref()+"/"+split[k]);
                //判断该目录是否已经创建
                if(tbDocumentMsgFileInfoService.fileIsNotExist(param)==0){
                    tbDocumentMsgFileInfoService.addFileMenu(param);
                }
                fileInfo.setHref(JzbDataType.getString(param.get("path")));
            }
        }

        fileInfo.setLocation(file);
        fileInfo.setType(0);
        fileInfo.setAddtime(System.currentTimeMillis());
        tbDocumentMsgFileInfoService.deleteRepeatMenu();
        if(tbDocumentMsgFileInfoService.addFileInfo(fileInfo)>0){
            return "合并成功并保存数据";
        }else {
            return "合并成功,保存数据异常";
        }
    }

    //-----------------------------------------------工具类--------------------------------------------
    public static String generatePath(String uploadFolder, Chunk chunk) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(chunk.getIdentifier());
        //判断uploadFolder/identifier 路径是否存在，不存在则创建
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            //log.info("path not exist,create path: {}", sb.toString());
            try {
                Files.createDirectories(Paths.get(sb.toString()));
            } catch (IOException e) {
                //   log.error(e.getMessage(), e);
            }
        }

        return sb.append("/")
                .append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString();
    }

    /**
     * 文件合并
     *
     * @param targetFile
     * @param folder
     */
    public static void merge(String targetFile, String folder, String filename) {
        try {
            Files.createFile(Paths.get(targetFile));
            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
                            //  log.error(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
        }
    }
}
