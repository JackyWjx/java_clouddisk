package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TbResourceVotesMapper {

    /**
     * 点赞
     * @param param
     * @return
     */
    int addResourceVotes(Map<String ,Object> param);

    /**
     * 查询已点赞次数
     * @param param
     * @return
     */
    Map<String ,Integer> queryResourceVotes(Map<String ,Object> param);

    /**
     * 查询是否已经点赞
     * @param param
     * @return
     */
    int queryIsAlreadyVotes(Map<String ,Object> param);
}
