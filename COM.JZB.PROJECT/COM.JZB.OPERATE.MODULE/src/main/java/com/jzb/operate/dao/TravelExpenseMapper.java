package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface TravelExpenseMapper {

	List<Map<String,Object>> queryTravelExpenseByid(Map<String, Object> map);

	int saveTravelExpense(List<Map<String, Object>> list);

	int updateTravelExpense(List<Map<String, Object>> list);


}
