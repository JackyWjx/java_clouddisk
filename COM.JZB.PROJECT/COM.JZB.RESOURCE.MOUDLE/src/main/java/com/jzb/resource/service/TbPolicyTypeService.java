package com.jzb.resource.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.resource.dao.TbPolicyTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbPolicyTypeService {

    @Autowired
    private TbPolicyTypeMapper tbPolicyTypeMapper;


    /**
     *     查询政策父子级
     * @param param
     * @return
     */
    public JSONArray queryPolicyType(Map<String, Object> param) {

        // get datas
        List<Map<String, Object>> records = tbPolicyTypeMapper.queryPolicyType(param);

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
            node.put("idx", record.get("idx").toString());
            node.put("parentid", parentId);
            node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("addtime")), JzbDateStr.yyyy_MM_dd));
            node.put("children", new JSONArray());

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
}
