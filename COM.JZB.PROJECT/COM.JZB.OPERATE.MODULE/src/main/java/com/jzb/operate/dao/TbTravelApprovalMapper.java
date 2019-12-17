package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Champ_Ping
 * @date 2019-12-04 16:21:59
 */

@Mapper
@Repository
public interface TbTravelApprovalMapper {

	Map<String,Object> get(String id);
	
	List<Map<String,Object>> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(Map<String,Object> travelApproval);
	
	int update(Map<String,Object> travelApproval);
	
	int remove(String id);
	
	int batchRemove(String[] ids);

	String getMaxApid(Map<String, Object> map);
}
