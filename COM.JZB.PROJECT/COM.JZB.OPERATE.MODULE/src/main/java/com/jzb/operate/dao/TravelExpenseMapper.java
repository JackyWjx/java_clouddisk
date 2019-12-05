package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface TravelExpenseMapper {


	int saveTravelExpense(Map<String, Object> map);

	int updateTravelExpense(List<Map<String, Object>> list);

}
