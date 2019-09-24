package com.jzb.org.service;

import com.jzb.org.dao.TbResourceVotesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TbResourceVotesService {

    @Autowired
    private TbResourceVotesMapper tbResourceVotesMapper;

    /**
     * 点赞
     * chenzhengduan
     */
    public int addResourceVotes(Map<String ,Object> param){
        return tbResourceVotesMapper.addResourceVotes(param);
    }

    /**
     * 查询已点赞次数
     * @param param
     * @return
     */
    public Map<String ,Integer> queryResourceVotes(Map<String ,Object> param){
        return tbResourceVotesMapper.queryResourceVotes(param);
    }

    /**
     * 查询是否已经点赞
     * @param param
     * @return
     */
    public int queryIsAlreadyVotes(Map<String ,Object> param){
        return tbResourceVotesMapper.queryIsAlreadyVotes(param);
    }
}
