package com.jzb.org.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbDeptUserMapper {

    List<Map<String, Object>> queryUsernameBydept(Map<String, Object> map);

    List<Map<String, Object>> queryOtherPersonBycid(Map<String, Object> param);

    List<Map<String, Object>> queryPersonNameByuid(Map<String, Object> param);

    /**
     *  @author: gongWei
     *  @Date:  2019/12/13 10:14
     *  @Description:
     *  @Param:
     *  @Return:
     *  @Exception:
     */
    String getUsernameByUids(Map<String, Object> whereMap);
}
