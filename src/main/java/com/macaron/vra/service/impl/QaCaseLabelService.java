package com.macaron.vra.service.impl;

import com.macaron.vra.dao.impl.QaCaseLabelDaoImpl;
import com.macaron.vra.entity.QaCaseLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QaCaseLabelService {

    @Autowired
    QaCaseLabelDaoImpl qaCaseLabelDao;


    public List<QaCaseLabel> findAll() {
        return qaCaseLabelDao.findAll();
    }

}
