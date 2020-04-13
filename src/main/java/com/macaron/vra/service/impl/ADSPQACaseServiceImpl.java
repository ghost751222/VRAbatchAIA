package com.macaron.vra.service.impl;

import com.macaron.vra.dao.impl.ADSPQACaseDaoImpl;
import com.macaron.vra.entity.ADSPQACase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ADSPQACaseServiceImpl {

    @Autowired
    private ADSPQACaseDaoImpl adspqaCaseDao;

    public int[] insertBatch(List<ADSPQACase> adspqaCases) {
        return adspqaCaseDao.insertBatch(adspqaCases);
    }
}
