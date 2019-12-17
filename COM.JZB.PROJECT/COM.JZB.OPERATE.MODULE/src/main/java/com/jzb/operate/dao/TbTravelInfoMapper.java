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
public interface TbTravelInfoMapper {

	Map<String,Object> get(Integer id);
	
	List<Map<String,Object>> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(Map<String,Object> travelInfo);
	
	int update(Map<String,Object> travelInfo);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

    int setStatusByTravelid(Map<String, Object> param);
}
