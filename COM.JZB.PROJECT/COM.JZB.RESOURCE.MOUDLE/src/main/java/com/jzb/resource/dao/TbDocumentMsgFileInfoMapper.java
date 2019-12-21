package com.jzb.resource.dao;

import com.jzb.base.entity.uploader.FileInfo;
import com.jzb.base.message.Response;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbDocumentMsgFileInfoMapper {

    int insertFileInfo(FileInfo fileInfo);
    //修改文件与文件夹，判断标准：type的值
    int updateFileInfo(Map<String, Object> param);

    int deleteFileInfo(Map<String, Object> param);

    List<FileInfo> selectFileInfo(Map<String,Object> param);

    Integer insertFileMenu(Map<String, Object> param);

    Map<String,Object> selectFileInfoPath(Map<String, Object> param);

    List<Map<String, Object>> queryAllInfo(Map<String, Object> param);

    Integer batchHierarchyUpdate(List<Map<String, Object>> param);

    Integer batchHierarchyDelete(List<Map<String, Object>> param);

    Integer updateItselfFileInfo(Map<String, Object> param);

    int fileIsNotExist(Map<String, Object> param);

    void deleteRepeatMenu();

}
