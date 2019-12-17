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
public interface TbTravelProduceMapper {

	Map<String,Object> get(Integer id);
	
	List<Map<String,Object>> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(Map<String,Object> travelProduce);
	
	int update(Map<String,Object> travelProduce);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	List<Map<String,Object>> queryProduce(Map<String, Object> map);
}
