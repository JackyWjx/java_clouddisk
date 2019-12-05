package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelInfoMapper;
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
public class TbTravelInfoService {
	@Autowired
	private TbTravelInfoMapper travelInfoDao;
	
	
	public Map<String,Object> get(Integer id){
		return travelInfoDao.get(id);
	}
	
	
	public List<Map<String,Object>> list(Map<String, Object> map){
		return travelInfoDao.list(map);
	}
	
	
	public int count(Map<String, Object> map){
		return travelInfoDao.count(map);
	}
	
	
	public int save(Map<String,Object> travelInfo){
		return travelInfoDao.save(travelInfo);
	}
	
	
	public int update(Map<String,Object> travelInfo){
		return travelInfoDao.update(travelInfo);
	}
	
	
	public int remove(Integer id){
		return travelInfoDao.remove(id);
	}
	
	
	public int batchRemove(Integer[] ids){
		return travelInfoDao.batchRemove(ids);
	}
	
}
