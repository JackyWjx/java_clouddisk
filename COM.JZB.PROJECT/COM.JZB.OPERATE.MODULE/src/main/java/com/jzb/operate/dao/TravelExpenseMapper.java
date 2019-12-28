package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface TravelExpenseMapper {

	List<Map<String,Object>> queryTravelExpenseByid(Map<String, Object> map);

	int saveTravelExpense(Map<String, Object> param);

	int updateTravelExpense(Map<String, Object> param);

	int setExpenseDeleteStatus(Map<String, Object> map);
}
