package com.jzb.resource.service;

import com.jzb.base.entity.uploader.FileInfo;
import com.jzb.base.message.Response;
import com.jzb.resource.dao.TbDocumentMsgFileInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class TbDocumentMsgFileInfoService {

    @Resource
    private TbDocumentMsgFileInfoMapper tbDocumentMsgFileInfoMapper;

    public List<FileInfo> getFileInfo (Map<String,Object> param){
        return tbDocumentMsgFileInfoMapper.selectFileInfo(param);
    }

    public  Integer delFileInfo(Map<String, Object> param){
        return tbDocumentMsgFileInfoMapper.deleteFileInfo(param);
    }

    public Integer addFileInfo(FileInfo fileInfo){
        return tbDocumentMsgFileInfoMapper.insertFileInfo(fileInfo);
    }

    public Integer addFileMenu(Map<String, Object> param) {
        return tbDocumentMsgFileInfoMapper.insertFileMenu(param);
    }

    public Integer putFileMenu(Map<String, Object> param) {
        return tbDocumentMsgFileInfoMapper.updateFileInfo(param);
    }

    public Map<String, Object> getFileInfoPath(Map<String, Object> param) {
        return tbDocumentMsgFileInfoMapper.selectFileInfoPath(param);
    }

    public List<Map<String, Object>> queryAllInfo(Map<String, Object> param) {
        return tbDocumentMsgFileInfoMapper.queryAllInfo(param);
    }

    public Integer batchHierarchyUpdate(List<Map<String, Object>> param) {
        return tbDocumentMsgFileInfoMapper.batchHierarchyUpdate(param);
    }

    public Integer updateItselfFileInfo(Map<String, Object> param) {
        return tbDocumentMsgFileInfoMapper.updateItselfFileInfo(param);
    }

    public Integer batchHierarchyDelete(List<Map<String, Object>> param) {
        return tbDocumentMsgFileInfoMapper.batchHierarchyDelete(param);
    }

    public int fileIsNotExist(Map<String, Object> param) {
        return tbDocumentMsgFileInfoMapper.fileIsNotExist(param);
    }

    public void deleteRepeatMenu() {
        tbDocumentMsgFileInfoMapper.deleteRepeatMenu();
    }
}
