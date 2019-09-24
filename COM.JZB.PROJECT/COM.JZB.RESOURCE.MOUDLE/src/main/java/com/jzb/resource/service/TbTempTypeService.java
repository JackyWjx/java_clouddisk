package com.jzb.resource.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbTempTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbTempTypeService {

    @Autowired
    private TbTempTypeMapper tbTempTypeMapper;

    /*
     * 1.添加模板
     */
    public int saveTempType(Map<String, Object> param) {
        Long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("typeid", JzbRandom.getRandomCharCap(7));
        param.put("idx", getTempTypeIdx());
        return tbTempTypeMapper.saveTempType(param);
    }

    /*
     * 2.修改模板
     */
    public int updateTempType(Map<String, Object> param) {
        param.put("updtime", System.currentTimeMillis());
        return tbTempTypeMapper.updateTempType(param);
    }

    /*
     * 3.获取排序
     * */
    public int getTempTypeIdx() {
        return tbTempTypeMapper.getTempTypeIdx();
    }

    /*
     * 4.获取模板类型（父子级）
     * */
    public JSONArray queryTempType() {

        // get datas
        List<Map<String, Object>> records = tbTempTypeMapper.queryTempType();

        // Result JSON
        JSONArray result = new JSONArray();

        // record temp json
        JSONObject recordJson = new JSONObject();

        // Unknown json
        JSONObject unknownRecord = new JSONObject();

        // root id
        String firstParent = "0000000";

        for (int i = 0, l = records.size(); i < l; i++) {
            Map<String, Object> record = records.get(i);

            // if parentid is null.
            String parentId;
            if (record.get("parentid") == null) {

                parentId = "0000000";
            } else {
                parentId = record.get("parentid").toString();
            }

            // set default JSON and childern node
            JSONObject node = new JSONObject();
            node.put("typeid", record.get("typeid").toString());
            node.put("cname", record.get("cname").toString());
            node.put("typecode", record.get("typecode").toString());
            node.put("standdata", record.get("standdata").toString());
            node.put("parentid", parentId);
            node.put("children", new JSONArray());
            if(record.get("typeid").toString().equals("K000001")){
                node.put("logourl", "crm4.png");
            }
            if(record.get("typeid").toString().equals("K000002")){
                node.put("logourl", "crm3.png");
            }

            // if root node
            if (parentId.equals(firstParent)) {
                result.add(node);
                recordJson.put(record.get("typeid").toString(), node);

                // if parent exist
            } else if (recordJson.containsKey(parentId)) {
                // add children
                recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                recordJson.put(record.get("typeid").toString(), node);
                // Unknown relation node
            } else {
                String nodeId = record.get("typeid").toString();
                if (unknownRecord.containsKey(parentId)) {
                    // add children
                    unknownRecord.getJSONObject(parentId).getJSONArray("children").add(node);
                    recordJson.put(nodeId, node);
                } else {
                    // find subnode
                    for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
                        JSONObject tempNode = (JSONObject) entry.getValue();
                        if (tempNode.getString("parentid").equals(nodeId)) {
                            node.getJSONArray("children").add(tempNode);
                            recordJson.put(tempNode.get("typeid").toString(), tempNode);
                            unknownRecord.remove(tempNode.get("typeid").toString());
                            break;
                        }
                    }
                    unknownRecord.put(nodeId, node);
                }
            }
        }

        // unknownRecord add to result
        // find subnode
        for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
            JSONObject tempNode = (JSONObject) entry.getValue();
            String tempNodeId = tempNode.getString("parentid");
            if (recordJson.containsKey(tempNodeId)) {
                // add children
                recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);
            } else {
                // Error node
                System.out.println("========================ERROR>> " + tempNodeId + "\t\t" + tempNode.toString());
            }
        }
        return result;
    }

    /*
     * 5.根据父级id查询模板类型
     * */
    public List<Map<String, Object>> queryTempTypeById(Map<String, Object> param) {
        return tbTempTypeMapper.queryTempTypeById(param);
    }

    /*
     * 6.修改状态  1正常  2不正常  4 删除
     * */
    public int setDelete(Map<String, Object> param) {
        param.put("status", "4");
        return tbTempTypeMapper.updateStatus(param);
    }
}
