package com.jzb.base.tree;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;

import java.util.*;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/4 11:48
 */
public class JzbTree {

    /**
     * 获取树形结构map
     *
     * @param data        需要处理的数据(字段data是子级保留字段，传入的数据包含的字段不可命名为data)
     * @param id          单条数据的id
     * @param pid         单条数据的父id
     * @param firstParent 根节点，第一个父id
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     * @DateTime: 2019/9/4 10:29
     */
    public static List<Map<String, Object>> getTreeMap(List<Map<String, Object>> data,
                                                       String id, String pid, String firstParent) {
        List<Map<String, Object>> result = new LinkedList<>();
        Map<String, Map<String, Object>> tree = new HashMap<>(6);
        //临时表
        Map<String, Map<String, Object>> tempMap = new HashMap<>(6);
        //遍历数据成树形
        int size = data.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> record = data.get(i);
            String pmId;
            if (JzbTools.isEmpty(record.get(pid))) {
                // TODO
                pmId = firstParent;
            } else {
                pmId = JzbDataType.getString(record.get(pid));
            }
            //添加子级list
            List<Map<String, Object>> childrenNew =  new LinkedList<>();
            record.put("children",childrenNew);
            //判断一级菜单
            if (pmId.equals(firstParent)) {
                result.add(record);
                tree.put(record.get(id).toString(), record);
            } else if (tree.containsKey(pmId)) {
                //获取父级的children
                List<Map<String, Object>> children = getChildren(tree,pmId);
                children.add(record);
                tree.put(record.get(id).toString(), record);
            } else {
                String mid = JzbDataType.getString(record.get(id));
                if (tempMap.containsKey(pmId)) {
                    List<Map<String, Object>> children = getChildren(tempMap,pmId);
                    children.add(record);
                    tree.put(mid, record);
                } else {
                    // 寻找子级
                    for (Map.Entry<String, Map<String, Object>> entry : tempMap.entrySet()) {
                        Map<String, Object> tempNode = entry.getValue();
                        if (tempNode.get(pid).equals(mid)) {
                            List<Map<String, Object>> children = (List<Map<String, Object>>) record.get("children");
                            children.add(tempNode);
                            tree.put(tempNode.get(id).toString(), tempNode);
                            tempMap.remove(tempNode.get(id).toString());
                            break;
                        }
                    }
                    //都没有匹配上的就加到tempMap中
                    tempMap.put(mid, record);
                }
            }
        }
        //将tempMap中的剩余数据与tree匹配。
        for (Map.Entry<String, Map<String, Object>> entry : tempMap.entrySet()) {
            Map<String, Object> tempNode = entry.getValue();
            String pmId = JzbDataType.getString(tempNode.get(pid));
            if (tree.containsKey(pmId)) {
                List<Map<String, Object>> children = getChildren(tree,pmId);
                children.add(tempNode);
                tree.put(tempNode.get(id).toString(), tempNode);
                tempMap.remove(tempNode.get(id).toString());
            }
        }
        return result;
    }

    private static  List<Map<String, Object>> getChildren(Map<String, Map<String, Object>> tree,String pmId){
        List<Map<String, Object>> children = (List<Map<String, Object>>) tree.get(pmId).get("children");
        return children;
    }

    /**
     * 将数据相同的合并字段存入list中
     *
     * @param list     需要处理的数据
     * @param main     主干字段, main的key从0开始累计，value是字段名称
     * @param id       主干id
     * @param merge    需要合并的字段,merge的key从0开始累计，value是字段名称
     * @param listName 生成的list姓名
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/4 9:48
     */
    public static List<Map<String, Object>> toSame(List<Map<String, Object>> list,
                                                   Map<Integer, String> main, String id,
                                                   Map<Integer, String> merge, String listName) {
        int lSize = list.size();
        List<Map<String, Object>> temp = new ArrayList<>(lSize);
        Map<String, Map<String, Object>> result = new HashMap<>(lSize);
        for (int i = 0, s = list.size(); i < s; i++) {
            Map<String, Object> map = list.get(i);
            String mid = JzbDataType.getString(map.get(id));
            if ((!JzbTools.isEmpty(mid)) && result.containsKey(mid)) {
                Map<String, Object> value = result.get(mid);
                List<Map<String, Object>> pageList = (List<Map<String, Object>>) value.get(listName);
                //合并字段添加
                Map<String, Object> pageMap = toPageMap(map, merge);
                pageList.add(pageMap);
            } else {
                //主干字段添加
                Map<String, Object> value = toPageMap(map,main);
                List<Map<String, Object>> pageList = new ArrayList<>();
                //合并字段添加
                Map<String, Object> pageMap = toPageMap(map, merge);
                pageList.add(pageMap);
                value.put(listName, pageList);
                //id值不为空就加入result中
                if (!JzbTools.isEmpty(mid)) {
                    result.put(mid, value);
                }
                //返回的list
                temp.add(value);
            }
        }
        return temp;
    }

    /**
     * 将合并的字段放入map中
     *
     * @param map   需要处理的数据
     * @param merge 需要添加的字段
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     * @DateTime: 2019/9/4 9:59
     */
    private static Map<String, Object> toPageMap(Map<String, Object> map, Map<Integer, String> merge) {
        Map<String, Object> pageMap = new HashMap(merge.size());
        //合并字段的个数
        int m = merge.size();
        for (int i = 0; i < m; i++) {
            if (JzbTools.isEmpty(map.get(merge.get(i)))) {
                pageMap.put(merge.get(i),"");
            }else{
                pageMap.put(merge.get(i), map.get(merge.get(i)));
            }
        }
        return pageMap;
    }

    /**
     * 将字符串转换为map，字符串用户','分割，获取toSame需要的map
     *
     * @param str
     * @return java.util.Map<java.lang.Integer, java.lang.String>
     * @Author: DingSC
     * @DateTime: 2019/9/6 14:36
     */
    public static Map<Integer, String> toMap(String str) {
        Map<Integer, String> result = new HashMap<>(2);
        if (!JzbTools.isEmpty(str)) {
            String regex = ",";
            String[] array = str.split(regex);
            int size = array.length;
            for (int i = 0; i < size; i++) {
                result.put(i, array[i]);
            }
        }
        return result;
    }

    /**
    * 将字符串转换为字符串数组，字符串数据用','分割，
    * @Author: DingSC
    * @DateTime: 2019/9/20 11:34
    * @param str
    * @return java.lang.String[]
    */
    public static String[] toStringArray(String str) {
        String[] result;
        if (!JzbTools.isEmpty(str)) {
            String regex = ",";
            result = str.split(regex);
        }else{
            result = new String[0];
        }
        return result;
    }

}
