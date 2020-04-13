package com.macaron.vra.service.impl;


import com.macaron.vra.config.PsaeConfig;
import com.macaron.vra.dao.impl.QaDesignQatermBoundpsaemodelDaoImpl;
import com.macaron.vra.dao.impl.QaDesignQatermhitDaoImpl;
import com.macaron.vra.dao.impl.QaInterfacePsaemodelDaoImpl;
import com.macaron.vra.entity.QaDesignQatermBoundpsaemodel;
import com.macaron.vra.entity.QaDesignQatermhit;
import com.macaron.vra.entity.QaInterfacePsaemodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QaModelService {

    @Autowired
    PsaeConfig psaeConfig;

    @Autowired
    QaDesignQatermhitDaoImpl qaDesignQatermhitDao;


    @Autowired
    QaInterfacePsaemodelDaoImpl qaInterfacePsaemodelDao;

    @Autowired
    QaDesignQatermBoundpsaemodelDaoImpl qaDesignQatermBoundpsaemodelDao;


    //取得質檢模版的所有模型
    public List<String> getAllQaModel(int qa_task_job_id) {
        List<String> models = new ArrayList<>();
        List<QaDesignQatermhit> qaDesignQatermhits = qaDesignQatermhitDao.findByJobId(qa_task_job_id);
        for (QaDesignQatermhit qaDesignQatermhit : qaDesignQatermhits) {

            List<QaDesignQatermBoundpsaemodel> qaDesignQatermBoundpsaemodels = qaDesignQatermBoundpsaemodelDao.findByQaTermId(qaDesignQatermhit.getTerm_ID());
            for (QaDesignQatermBoundpsaemodel qaDesignQatermBoundpsaemodel : qaDesignQatermBoundpsaemodels) {
                List<QaInterfacePsaemodel> qaInterfacePsaemodels = qaInterfacePsaemodelDao.findById(qaDesignQatermBoundpsaemodel.getPsaemodel_id());
                for (QaInterfacePsaemodel qaInterfacePsaemodel : qaInterfacePsaemodels) {
                    if (qaInterfacePsaemodel.isOnline()) {
                        models.add(qaInterfacePsaemodel.getName());
                    }
                }
            }

        }
        return models;
    }

    //取得質檢模版未命中的所有模型
    public List<String> getAllHitFalseQaModel(int qa_task_job_id) {
        List<String> models = new ArrayList<>();
        List<QaDesignQatermhit> qaDesignQatermhits = qaDesignQatermhitDao.findByJobIdAndMachinehit(qa_task_job_id, "false");
        for (QaDesignQatermhit qaDesignQatermhit : qaDesignQatermhits) {
            if (qaDesignQatermhit.getMachine_Hit().equalsIgnoreCase("false")) {
                List<QaDesignQatermBoundpsaemodel> qaDesignQatermBoundpsaemodels = qaDesignQatermBoundpsaemodelDao.findByQaTermId(qaDesignQatermhit.getTerm_ID());
                for (QaDesignQatermBoundpsaemodel qaDesignQatermBoundpsaemodel : qaDesignQatermBoundpsaemodels) {
                    List<QaInterfacePsaemodel> qaInterfacePsaemodels = qaInterfacePsaemodelDao.findById(qaDesignQatermBoundpsaemodel.getPsaemodel_id());
                    for (QaInterfacePsaemodel qaInterfacePsaemodel : qaInterfacePsaemodels) {
                        if (qaInterfacePsaemodel.isOnline()) {
                            models.add(qaInterfacePsaemodel.getName());
                        }
                    }
                }
            }
        }

        return models;
    }

    //取得質檢模版命中的所有模型
    public List<String> getAllHitQaModel(int qa_task_job_id) {
        List<String> models = new ArrayList<>();
        List<QaDesignQatermhit> qaDesignQatermhits = qaDesignQatermhitDao.findByJobIdAndMachinehit(qa_task_job_id, "true");
        for (QaDesignQatermhit qaDesignQatermhit : qaDesignQatermhits) {
            if (qaDesignQatermhit.getMachine_Hit().equalsIgnoreCase("true")) {
                List<QaDesignQatermBoundpsaemodel> qaDesignQatermBoundpsaemodels = qaDesignQatermBoundpsaemodelDao.findByQaTermId(qaDesignQatermhit.getTerm_ID());
                for (QaDesignQatermBoundpsaemodel qaDesignQatermBoundpsaemodel : qaDesignQatermBoundpsaemodels) {
                    List<QaInterfacePsaemodel> qaInterfacePsaemodels = qaInterfacePsaemodelDao.findById(qaDesignQatermBoundpsaemodel.getPsaemodel_id());
                    for (QaInterfacePsaemodel qaInterfacePsaemodel : qaInterfacePsaemodels) {
                        if (qaInterfacePsaemodel.isOnline()) {
                            models.add(qaInterfacePsaemodel.getName());
                        }
                    }
                }
            }
        }

        return models;
    }

    //取得質檢模版命中的所有模型並取需要尋找違規文本內容模型
    public List<String> getViolationModel(int qa_task_job_id) {
        List<String> models = new ArrayList<>();
        List<String> qaModels = this.getAllHitQaModel(qa_task_job_id);
        for (String m : qaModels) {
            if (psaeConfig.getViolationModels().contains(m)) {
                models.add(m);
            }
        }
        return models;

    }

}
