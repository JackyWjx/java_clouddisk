package com.jzb.resource.service;

import com.jzb.resource.dao.TbProductSystemDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbProductSystemDocumentService {

    @Autowired
    private TbProductSystemDocumentMapper tbProductSystemDocumentMapper;
}
