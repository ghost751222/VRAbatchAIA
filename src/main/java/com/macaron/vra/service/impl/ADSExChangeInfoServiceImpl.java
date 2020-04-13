package com.macaron.vra.service.impl;

import com.macaron.vra.dao.impl.ADSExChangeInfoDaoImpl;
import com.macaron.vra.entity.ADSExChangeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ADSExChangeInfoServiceImpl {

    @Autowired
    ADSExChangeInfoDaoImpl adsExChangeInfoDao;

    public List<ADSExChangeInfo> findByAppId(String id) {
        return adsExChangeInfoDao.findByAppId(id);
    }

    public int update(ADSExChangeInfo adsExChangeInfo) {

        return adsExChangeInfoDao.update(adsExChangeInfo);
    }

    public List<ADSExChangeInfo> findByDateRange(Date startDate, Date endDate) {
        return adsExChangeInfoDao.findByDateRange(startDate, endDate);
    }
}
