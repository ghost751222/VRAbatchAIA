package com.macaron.vra.service.impl;

import com.macaron.vra.dao.impl.QaCaseLabelJobsDaoImpl;
import com.macaron.vra.entity.QaCaseLabel;
import com.macaron.vra.entity.QaCaseLabelJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QaCaseLabelJobsService {

    @Autowired
    QaCaseLabelService qaCaseLabelService;

    @Autowired
    QaCaseLabelJobsDaoImpl qaCaseLabelJobsDao;

    public int[] delListByJobId(List<QaCaseLabelJobs> qa_case_label_jobs) {
        return qaCaseLabelJobsDao.delListByJobId(qa_case_label_jobs);
    }

    public int[] insertById(List<QaCaseLabelJobs> qa_case_label_jobs) {
        return qaCaseLabelJobsDao.insertById(qa_case_label_jobs);
    }

    public int delByJobId(int job_id){
        return qaCaseLabelJobsDao.delByJobId(job_id);
    }
    public List<QaCaseLabelJobs> findByJobId(Integer job_id) {
        return qaCaseLabelJobsDao.findByJobId(job_id);
    }


    public void insertByCaseLabel(String caseLabel, Integer qa_task_job_id) {
        List<QaCaseLabel> qaCaseLabels = qaCaseLabelService.findAll();

        Integer case_label_Id = null;
        for (QaCaseLabel q : qaCaseLabels) {
            if (q.getName().equals(caseLabel)) {
                case_label_Id = q.getId();
                break;
            }
        }
        if (case_label_Id != null) {
            QaCaseLabelJobs qaCaseLabelJobs = new QaCaseLabelJobs();
            qaCaseLabelJobs.setJob_id(qa_task_job_id);
            qaCaseLabelJobs.setCaselabel_id(case_label_Id);
            List<QaCaseLabelJobs> qaCaseLabelJobList = Collections.singletonList(qaCaseLabelJobs);
            delListByJobId(qaCaseLabelJobList);
            insertById(qaCaseLabelJobList);
        }
    }
}
