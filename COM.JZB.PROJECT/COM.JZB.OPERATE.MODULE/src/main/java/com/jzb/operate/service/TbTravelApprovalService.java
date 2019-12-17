package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelApprovalMapper;
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
public class TbTravelApprovalService {
	
	@Autowired
	private TbTravelApprovalMapper travelApprovalDao;
	
	
	public Map<String,Object> get(String id){
		return travelApprovalDao.get(id);
	}
	
	
	public List<Map<String,Object>> list(Map<String, Object> map){
		return travelApprovalDao.list(map);
	}
	
	
	public int count(Map<String, Object> map){
		return travelApprovalDao.count(map);
	}
	
	
	public int save(Map<String,Object> travelApproval){
		return travelApprovalDao.save(travelApproval);
	}
	
	
	public int update(Map<String,Object> travelApproval){
		return travelApprovalDao.update(travelApproval);
	}
	
	
	public int remove(String id){
		return travelApprovalDao.remove(id);
	}

	/**
	 * 通过travelId获取审批级别最高的Apid
	 * @param map
	 * @return
	 */
	public String getMaxIdxApid(Map<String, Object> map){
		return travelApprovalDao.getMaxApid(map);
	}
	
	public int batchRemove(String[] ids){
		return travelApprovalDao.batchRemove(ids);
	}
	
}
