package com.macaron.vra.service.impl;

import com.macaron.vra.dao.impl.QaTaskJobDaoImpl;
import com.macaron.vra.entity.QaTaskJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QaTaskJobServiceImpl  {

    @Autowired
    private QaTaskJobDaoImpl qaTaskJobDao;



    public int[] delListById(List<QaTaskJob> qa_task_jobs) {
        return qaTaskJobDao.delListById(qa_task_jobs);
    }


    public int update(QaTaskJob vo) {
        return qaTaskJobDao.update(vo);
    }


    public List<QaTaskJob> findByIsDealDone(String isDealDone) {
        return qaTaskJobDao.findByIsDealDone(isDealDone);
    }


    public List<QaTaskJob> findByInsertTime(Date insertTime) {
        return qaTaskJobDao.findByInsertTime(insertTime);
    }


    public List<QaTaskJob> findByApplicationFormID(String applicationFormID) {
        return qaTaskJobDao.findByApplicationFormID(applicationFormID);
    }


    public List<QaTaskJob> findByTaskDate(Date taskDate) {
        return qaTaskJobDao.findByTaskDate(taskDate);
    }


    public List<QaTaskJob> findAllBySendDateAndListenEndTimeRange(Date start_date, Date end_date) {
        return qaTaskJobDao.findAllBySendDateAndListenEndTimeRange(start_date, end_date);
    }



    public List<QaTaskJob> findAllByListenEndTimeRange(Date start_date, Date end_date) {
        return qaTaskJobDao.findAllByListenEndTimeRange(start_date, end_date);
    }

}
