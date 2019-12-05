package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



/**
 *
 * @author Champ_Ping
 * @date 2019-12-04 16:21:59
 */
@Service
public class TbTravelDataService {
	@Autowired
	private TbTravelDataMapper travelDataDao;
	
	
	public Map<String,Object> get(Integer id){
		return travelDataDao.get(id);
	}
	
	
	public List<Map<String,Object>> list(Map<String, Object> map){
		return travelDataDao.list(map);
	}
	
	
	public int count(Map<String, Object> map){
		return travelDataDao.count(map);
	}
	
	
	public int save(Map<String,Object> travelData){
		return travelDataDao.save(travelData);
	}
	
	
	public int update(Map<String,Object> travelData){
		return travelDataDao.update(travelData);
	}
	
	
	public int remove(Integer id){
		return travelDataDao.remove(id);
	}
	
	
	public int batchRemove(Integer[] ids){
		return travelDataDao.batchRemove(ids);
	}
	
}
