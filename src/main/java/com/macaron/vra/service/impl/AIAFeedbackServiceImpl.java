package com.macaron.vra.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.macaron.vra.dao.impl.ADSPendingCodeDaoImpl;
import com.macaron.vra.dao.impl.ADSPolicyInfoDaoImpl;
import com.macaron.vra.dao.impl.QaDesignQatermhitDaoImpl;
import com.macaron.vra.dao.impl.QaUsermanageUserDaoImpl;
import com.macaron.vra.entity.*;
import com.macaron.vra.util.DateUtil;
import com.macaron.vra.util.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIAFeedbackServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(AIAFeedbackServiceImpl.class);

    @Value("${violation.schedule.hour:8}")
    private Integer hour;
    @Value("${violation.schedule.isaddinserttime:false}")
    private boolean isAddInsertTime;

    @Autowired
    private ADSExChangeInfoServiceImpl adsExChangeInfoService;

    @Autowired
    private ADSPolicyInfoDaoImpl adsPolicyInfoDaoImpl;

    @Autowired
    private ADSPendingCodeDaoImpl adsPendingCodeDaoImpl;

    @Autowired
    private QaDesignQatermhitDaoImpl qaDesignQatermhit;

    @Autowired
    private ViolationServiceImpl violationService;

    @Autowired
    private QaTaskJobServiceImpl qaTaskJobService;

    @Autowired
    private QaCaseLabelJobsService qaCaseLabelJobsService;
    @Autowired
    QaUsermanageUserDaoImpl qaUsermanageUserDao;

    @Autowired
    ADSPQACaseServiceImpl adspqaCaseService;

    private final String STATE = "1003";

    //刪除未成交紀錄
    private void del() {

        try {
            List<QaTaskJob> qa_task_jobs = new ArrayList<>();

            qa_task_jobs.addAll(qaTaskJobService.findByApplicationFormID("None"));
            qa_task_jobs.addAll(qaTaskJobService.findByIsDealDone("N"));
            qa_task_jobs.addAll(qaTaskJobService.findByIsDealDone("C"));


            if (qa_task_jobs.size() > 0) {
                qa_task_jobs.forEach(q -> {
                    qaDesignQatermhit.delByJobId(q.getId());
                    qaCaseLabelJobsService.delByJobId(q.getId());
                });
                qaTaskJobService.delListById(qa_task_jobs);
            }

        } catch (Exception e) {
            logger.error("del error={}", e);
        }
    }

    public void feedBackToADS(Date dataTimeStart, Date dataTimeEnd) {
        logger.info("feedBackToADS begin");
        try {
            del();
            List<QaTaskJob> vos = qaTaskJobService.findAllBySendDateAndListenEndTimeRange(dataTimeStart,
                    dataTimeEnd);
            Map<Integer, String> qaUsers = qaUsermanageUserDao.findAlltoMap();
            for (QaTaskJob vo : vos) {

                try {

                    String qa_id = vo.getQa_id() == null ? "" : vo.getQa_id().toString();
                    String qa_name = qaUsers.get(!StringUtils.isEmpty(qa_id) ? Integer.parseInt(qa_id) : qa_id);
                    String qa_remark = vo.getQa_remark(); // 質檢備註
                    String qa_result = vo.getQa_result(); // 質檢結果
                    String state = vo.getState();
                    if (qa_remark == null) {
                        qa_remark = "";
                    }
                    String case_content = vo.getCase_content() == null ? "" : vo.getCase_content();
                    List<ADSExChangeInfo> adsExChangeInfos = adsExChangeInfoService.findByAppId(vo.getPsae_AppID());

                    for (ADSExChangeInfo adsExChangeInfo : adsExChangeInfos) {

                        try {
                            boolean IsChange = false, IsBetween = false;
                            String adsResult = adsExChangeInfo.getADSResult();
                            String adsRemark = adsExChangeInfo.getADSRemark();
                            Date listen_end_time = vo.getListen_end_time();
                            if (listen_end_time != null)
                                IsBetween = DateUtil.IsBetween(listen_end_time, dataTimeStart, dataTimeEnd);

                            if (!qa_name.equals(adsExChangeInfo.getSTTSCFAgent())) {
                                adsExChangeInfo.setSTTSCFAgent(qa_name);
                                adsExChangeInfo.setSTTSCFAgentUpdateTime(new Date());
                                IsChange = true;
                            }


                            if (state.equalsIgnoreCase(STATE) && IsBetween) {
                                if (
                                        (!case_content.equals(adsExChangeInfo.getSTTResult()) && !case_content.equals(adsResult)) ||
                                                (!qa_remark.equals(adsExChangeInfo.getSTTRemark()) && !qa_remark.equals(adsRemark)) ||
                                                (!qa_result.equals(adsExChangeInfo.getSTTTypeResult()))
                                ) {
                                    adsExChangeInfo.setSTTResult(case_content);
                                    adsExChangeInfo.setSTTRemark(qa_remark);
                                    adsExChangeInfo.setSTTTypeResult(qa_result);
                                    adsExChangeInfo.setSTTResultUpdateTime(new Date());
                                    IsChange = true;
                                }

                            }

//                            if (vo.getListen_end_time() != null) {
//                                adsExChangeInfo.setSTTResult(case_content);
//                                adsExChangeInfo.setSTTRemark(qa_remark);
//                                adsExChangeInfo.setSTTResultUpdateTime(vo.getListen_end_time());
//                                IsChange=true;
//                            }
                            if (IsChange) adsExChangeInfoService.update(adsExChangeInfo);
                        } catch (Exception e) {
                            logger.error("feedBackToADS ADSExChangeInfo vo {}", e);
                        }
                    }
                } catch (Exception e) {
                    logger.error("feedBackToADS Qa_Task_Job vo {}", e);

                }
            }
        } catch (Exception e) {
            logger.error("feedBackToADS Qa_Task_Job  {}", e);
        }
        logger.info("feedBackToADS finish");

    }

    public void feedBackToPQA(Date dataTimeStart, Date dataTimeEnd) {
        logger.info("feedBackToPQA begin");
        try {
            List<ADSExChangeInfo> vos = adsExChangeInfoService.findByDateRange(dataTimeStart, dataTimeEnd);
            Map<String, String> codes = adsPendingCodeDaoImpl.queryAllToMap();

            for (ADSExChangeInfo vo : vos) {

                try {
                    String appId = vo.getAppId();
                    String policyNo = vo.getADSPolicyNo();
                    String adsTMRPendingCode = vo.getADSTMRPendingCode();
                    String adsTMRPendingCode1 = vo.getADSTMRPendingCode1();
                    String adsTMRPendingCode2 = vo.getADSTMRPendingCode2();
                    String adsSCFPendingCode = vo.getADSSCFPendingCode();
                    String adsSCFPendingCode1 = vo.getADSSCFPendingCode1();
                    String adsSCFPendingCode2 = vo.getADSSCFPendingCode2();
                    String adsSCFRejectRemedyPendingCode1 = vo.getADSSCFRejectRemedyPendingCode1();
                    String adsSCFRejectRemedyPendingCode2 = vo.getADSSCFRejectRemedyPendingCode2();

                    String adsTMRPendingCodeName = codes.get(adsTMRPendingCode);
                    String adsTMRPendingCode1Name = codes.get(adsTMRPendingCode1);
                    String adsTMRPendingCode2Name = codes.get(adsTMRPendingCode2);
                    String adsSCFPendingCodeName = codes.get(adsSCFPendingCode);
                    String adsSCFPendingCode1Name = codes.get(adsSCFPendingCode1);
                    String adsSCFPendingCode2Name = codes.get(adsSCFPendingCode2);

                    String adsSCFRejectRemedyPendingCode1Name = codes.get(adsSCFRejectRemedyPendingCode1);
                    String adsSCFRejectRemedyPendingCode2Name = codes.get(adsSCFRejectRemedyPendingCode2);

                    String adsResult = vo.getADSResult();
                    String adsRemark = vo.getADSRemark();

                    ADSPolicyInfo adsPolicyInfo = adsPolicyInfoDaoImpl.queryPolicyInfoByAppId(appId);
                    String applicationFormId = adsPolicyInfo.getApplicationFormId();

                    List<QaTaskJob> qa_task_jobs = qaTaskJobService.findByApplicationFormID(applicationFormId);

                    for (QaTaskJob qa_task_job : qa_task_jobs) {
                        try {
                            qa_task_job.setPsae_TMRPendingCode(adsTMRPendingCode);
                            qa_task_job.setPsae_TMRPendingCode1(adsTMRPendingCode1);
                            qa_task_job.setPsae_TMRPendingCode2(adsTMRPendingCode2);

                            qa_task_job.setPsae_SCFPendingCode(adsSCFPendingCode);
                            qa_task_job.setPsae_SCFPendingCode1(adsSCFPendingCode1);
                            qa_task_job.setPsae_SCFPendingCode2(adsSCFPendingCode2);

                            qa_task_job.setPsae_SCFRejectRemedyPendingCode1(adsSCFRejectRemedyPendingCode1);
                            qa_task_job.setPsae_SCFRejectRemedyPendingCode2(adsSCFRejectRemedyPendingCode2);

                            qa_task_job.setPsae_TMRPendingCodeName(adsTMRPendingCodeName);
                            qa_task_job.setPsae_TMRPendingCode1Name(adsTMRPendingCode1Name);
                            qa_task_job.setPsae_TMRPendingCode2Name(adsTMRPendingCode2Name);

                            qa_task_job.setPsae_SCFPendingCodeName(adsSCFPendingCodeName);
                            qa_task_job.setPsae_SCFPendingCode1Name(adsSCFPendingCode1Name);
                            qa_task_job.setPsae_SCFPendingCode2Name(adsSCFPendingCode2Name);

                            qa_task_job.setPsae_SCFRejectRemedyPendingCode1Name(adsSCFRejectRemedyPendingCode1Name);
                            qa_task_job.setPsae_SCFRejectRemedyPendingCode2Name(adsSCFRejectRemedyPendingCode2Name);
                            qa_task_job.setPsae_PolicyNo(policyNo);

                            if (!StringUtils.isEmpty(adsRemark)) qa_task_job.setQa_remark(adsRemark);

                            if (!StringUtils.isEmpty(adsResult)) {
                                qa_task_job.setCase_content(adsResult);
                                qaCaseLabelJobsService.insertByCaseLabel(adsResult, qa_task_job.getId());
                            }

                            qaTaskJobService.update(qa_task_job);

                        } catch (Exception e) {
                            logger.error("feedBackToPQA Qa_Task_Job vo {}", e);
                        }

                    }
                } catch (Exception e) {
                    logger.error("feedBackToPQA ADSExChangeInfo vo {}", e);

                }
            }
        } catch (Exception e) {
            logger.error("feedBackToPQA Qa_Task_Job  {}", e);
        }
        logger.info("feedBackToPQA finish");
    }

    public void dispatchOrder() {

        try {
            logger.info("dispatchOrder begin");
            Calendar cal = Calendar.getInstance();
            List<QaTaskJob> qa_task_jobs = qaTaskJobService.findByTaskDate(cal.getTime());

            for (QaTaskJob qa_task_job : qa_task_jobs) {
                try {
                    Integer qa_id = qa_task_job.getQa_id();
                    String dataType = "1";
                    if (qa_id == null) {
                        dataType = "0"; //未派單
                    }
                    JsonNode jNode = JacksonUtil.toJsonNode(qa_task_job.getTask());
                    if (!jNode.isNull()) {
                        String applicationFormID = jNode.get("psae_ApplicationFormID").asText(); // 要保書編號
                        ADSPolicyInfo adsPolicyInfo = adsPolicyInfoDaoImpl.queryPolicyInfoByApplicationFormID(applicationFormID);
                        String appId = adsPolicyInfo.getAppId();
                        List<ADSExChangeInfo> adsExChangeInfos = adsExChangeInfoService.findByAppId(appId);

                        if (adsExChangeInfos.size() == 1) {
                            adsExChangeInfos.get(0).setSTTDispatchDataType(dataType);
                            adsExChangeInfos.get(0).setSTTDispatchDataTypeUpdateTime(new Date());
                            adsExChangeInfoService.update(adsExChangeInfos.get(0));
                        }
                    }
                } catch (Exception e) {
                    logger.error("dispatchOrder Qa_Task_Job vo {}", e);
                }

            }
        } catch (Exception e) {
            logger.error("dispatchOrder {}", e);
        } finally {
            logger.info("dispatchOrder end");
        }

    }


    public void violationTask(Date taskDate) {
        try {
            del();
        } finally {

        }
        try {
            logger.info("violationTask begin");
            List<QaTaskJob> qa_task_jobs = qaTaskJobService.findByTaskDate(taskDate);
            if (isAddInsertTime)
                qa_task_jobs.addAll(qaTaskJobService.findByInsertTime(Calendar.getInstance().getTime()));
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == hour) {
                if (qa_task_jobs != null) {
                    qa_task_jobs.addAll(qaTaskJobService.findByTaskDate(DateUtils.addDays(taskDate, -1)));
                }
            }
            Collections.sort(qa_task_jobs);
            Integer _executionId = null;
            for (QaTaskJob vo : qa_task_jobs) {
                logger.info("violationTask execute begin  QaTaskJob Id={}", vo.getId());
                String qa_name = vo.getQa_name();
                try {
                    if (!StringUtils.isEmpty(qa_name)) continue;
                    if (!vo.getId().equals(_executionId)) {

                        String appId = vo.getPsae_AppID();
                        violationService.setRetry(1);

                        //尋找違規話術
//                        String violationKeyContext = violationService.searchViolationKeyContext(vo);
//                        ObjectNode objectNode = (ObjectNode) taskNode;
//                        objectNode.put("qa_remark", violationKeyContext);
//                        vo.setTask(taskNode.toString());


                        String status = violationService.dispatchOrderCaseContent(vo);
                        vo.setCase_content(status);


                        qaCaseLabelJobsService.insertByCaseLabel(status, vo.getId());
                        qaTaskJobService.update(vo);
                        List<ADSExChangeInfo> adsExChangeInfos = adsExChangeInfoService.findByAppId(appId);
                        adsExChangeInfos.forEach(adsExChangeInfo -> {
                            boolean IsChange = false;

                            if (!status.equals(adsExChangeInfo.getSTTResult())) {
                                adsExChangeInfo.setSTTResult(status);
                                IsChange = true;
                            }


//                            if (!violationKeyContext.equals(adsExChangeInfo.getSTTRemark())) {
//                                adsExChangeInfo.setSTTRemark(violationKeyContext);
//                                IsChange = true;
//                            }

                            if (IsChange) {
                                adsExChangeInfo.setSTTResultUpdateTime(new Date());
                                adsExChangeInfoService.update(adsExChangeInfo);
                            }

                        });
                    }
                    _executionId = vo.getId();

                } catch (Exception e) {
                    logger.error("violationTask Qa_Task_Job vo error = {}", e);
                }

                logger.info("violationTask execute finish QaTaskJob Id={}", vo.getId());
            }
        } catch (Exception e) {
            logger.error("violationTask error ={}", e);
        } finally {
            logger.info("violationTask end");
        }
    }


    public void feedBackQaModelsToADSTask(Date dataTimeStart, Date dataTimeEnd) {
        try {
            logger.info("Begin feedBackQaModelsToADSTask dataTimeStart={},dataTimeEnd={}", dataTimeStart, dataTimeEnd);
            del();
            List<ADSPQACase> adspqaCaseList = new ArrayList<>();
            List<QaTaskJob> vos = qaTaskJobService.findAllByListenEndTimeRange(dataTimeStart, dataTimeEnd);

            for (QaTaskJob vo : vos) {

                String state = vo.getState();
                if (state.equalsIgnoreCase(STATE)) {
                    try {


                        String appID = vo.getPsae_AppID();

                        List<QaDesignQatermhit> qaDesignQatermhits = qaDesignQatermhit.findByJobId(vo.getId());
                        for (QaDesignQatermhit qaDesignQatermhit : qaDesignQatermhits) {
                            String checkResult = null;
                            if ("true".equalsIgnoreCase(qaDesignQatermhit.getHuman_Hit())) {
                                checkResult = "Y";
                            } else {
                                checkResult = "N";
                            }
                            ADSPQACase adspqaCase = new ADSPQACase();
                            adspqaCase.setAppID(appID);
                            adspqaCase.setCheckItem(qaDesignQatermhit.getTerm_Name());
                            adspqaCase.setCheckResult(checkResult);
                            adspqaCase.setCreateTime(new Date());

                            adspqaCaseList.add(adspqaCase);
                        }


                    } catch (Exception e) {
                        logger.error("feedBackQaModelsToADSTask inner Qa_Task_Job  {}", e.toString());
                    }
                }
            }
            adspqaCaseService.insertBatch(adspqaCaseList);

        } catch (Exception e) {
            logger.error("feedBackQaModelsToADSTask Qa_Task_Job {}", e.toString());
        } finally {
            logger.info("End feedBackQaModelsToADSTask dataTimeStart={},dataTimeEnd={}", dataTimeStart, dataTimeEnd);
        }
    }

}
