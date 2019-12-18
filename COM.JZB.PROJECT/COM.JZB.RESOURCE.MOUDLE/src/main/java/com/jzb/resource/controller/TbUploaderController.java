package com.jzb.resource.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import com.jzb.base.entity.uploader.*;
@RestController
@RequestMapping(value = "/operate/uploader")
public class TbUploaderController {
    private String uploadFolder="./files";//配置文件存放路径
//    @Autowired
//    private FileInfoService fileInfoService;
//    @Autowired
//    private ChunkService chunkService;


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
            //chunkService.saveChunk(chunk);
            System.out.println(chunk.toString());
            return "文件上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "后端异常...";
        }
    }

    @GetMapping("/chunk")
    @CrossOrigin
    public Object checkChunk(Chunk chunk, HttpServletResponse response) {
        //去数据库查询是否有该条信息
        //if (chunkService.checkChunk(chunk.getIdentifier(), chunk.getChunkNumber())) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
       // }

        return chunk;
    }


    //@RequestMapping(value = "/mergeFile",method = RequestMethod.POST)
    @PostMapping("mergeFile")
    @CrossOrigin
    public String mergeFile(FileInfo fileInfo) {
        String filename = fileInfo.getFilename();
        String file = uploadFolder + "/" + fileInfo.getIdentifier() + "/" + filename;
        String folder = uploadFolder + "/" + fileInfo.getIdentifier();
        merge(file, folder, filename);
        fileInfo.setLocation(file);
        //fileInfoService.addFileInfo(fileInfo);

        return "合并成功";
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
