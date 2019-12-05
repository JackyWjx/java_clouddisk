package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelProduceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 出差产出
 * @author Champ_Ping
 * @date 2019-12-04 16:21:59
 */
@Service
public class TbTravelProduceService {
	@Autowired
	private TbTravelProduceMapper travelProduceDao;
	
	 
	public Map<String,Object> get(Integer id){
		return travelProduceDao.get(id);
	}
	
	 
	public List<Map<String,Object>> list(Map<String, Object> map){
		return travelProduceDao.list(map);
	}
	
	 
	public int count(Map<String, Object> map){
		return travelProduceDao.count(map);
	}
	
	 
	public int save(Map<String,Object> travelProduce){
		return travelProduceDao.save(travelProduce);
	}
	
	 
	public int update(Map<String,Object> travelProduce){
		return travelProduceDao.update(travelProduce);
	}
	
	 
	public int remove(Integer id){
		return travelProduceDao.remove(id);
	}
	
	 
	public int batchRemove(Integer[] ids){
		return travelProduceDao.batchRemove(ids);
	}
	
}
