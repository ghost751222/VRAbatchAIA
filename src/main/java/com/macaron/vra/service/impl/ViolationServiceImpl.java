package com.macaron.vra.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.macaron.vra.config.PsaeConfig;
import com.macaron.vra.entity.QaTaskJob;
import com.macaron.vra.util.JacksonUtil;
import com.macaron.vra.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ViolationServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(ViolationServiceImpl.class);
    @Autowired
    PsaeConfig psaeConfig;

    @Autowired
    private PsaeApiServiceImpl psaeApiService;

    @Autowired
    private QaModelService qaModelService;

    @Autowired
    private QaTaskJobServiceImpl qaTaskJobService;

    @Autowired
    private ADSExChangeInfoServiceImpl adsExChangeInfoService;


    private int retry;
    private int retryMax = 10;

    //查詢違規話術
    public String searchViolationKeyContext(@NonNull QaTaskJob vo) throws Exception {

        JsonNode jNode = null;
        StringBuilder remark = new StringBuilder();

        jNode = JacksonUtil.toJsonNode(vo.getTask());
        String taskId = jNode.get("psae_ID").asText();

        String machineHits = vo.getMachine_hits();
        if (!StringUtils.isEmpty(machineHits)) {
            List<String> violationModels = qaModelService.getViolationModel(vo.getId());
            if (violationModels.size() > 0) {
                Map<String, Object> resultMap = psaeApiService.getAnalysisResultByTaskId(taskId);
                if (resultMap.get("functionResult").equals("SUCCESS")) {
                    Map<String, Object> taskAnalyzeMap = (Map<String, Object>) ((List) resultMap.get("taskList")).get(0);
                    List<Map<String, Object>> modelAnalyzeResults = JacksonUtil.getMapList(taskAnalyzeMap, "modelAnalyzeResults");

                    for (int index = 0; index < modelAnalyzeResults.size(); index++) {
                        String modelName = (String) modelAnalyzeResults.get(index).get("modelName");
                        if (violationModels.contains(modelName)) {
                            Object hitKeywords = modelAnalyzeResults.get(index).get("hitKeywords");
                            if (hitKeywords != null) {
                                List<String> hitKeywordList = JacksonUtils.jsonStrToList((String) hitKeywords);
                                for (String hitKeyword : hitKeywordList) {
                                    String[] hitKeywordArray = hitKeyword.split(",");
                                    String hitKey = hitKeywordArray[0];
                                    Integer begin = Integer.parseInt(hitKeywordArray[1].replaceAll(" ", ""));
                                    Integer end = Integer.parseInt(hitKeywordArray[2].replaceAll(" ", ""));
                                    remark.append(modelName).append(" : ").append(getKeywordContextByTaskId(vo.getId(),taskId, hitKey, begin, end)).append(System.getProperty("line.separator"));




//                                    String keywordContext = (String) ((Map<String, Object>) psaeApiService.getKeywordContextByTaskId(taskId, hitKey, begin, end).get("data")).get("keywordContext");
//                                    remark.append(modelName).append(" : ").append(keywordContext).append(System.getProperty("line.separator"));

                                }
                            }
                        }
                    }
                } else {
                    if (retry <= retryMax) {
                        retry++;
                        logger.error("取得模型列表失敗 QaJobId ={},taskId={},重新嘗試次數 ={}", vo.getId(), taskId,retry);
                        return searchViolationKeyContext(vo);
                    } else {
                        logger.error("取得模型列表失敗 QaJobId ={},taskId={}", vo.getId(), taskId);
                        retry = 1;
                    }

                }
            }
        }
        return remark.toString();

    }

    //分配案例標籤
    public String dispatchOrderCaseContent(QaTaskJob vo) {
        String status = psaeConfig.getQualifiedKey();
        String machineHits = vo.getMachine_hits();
        //  if (!StringUtils.isEmpty(machineHits)) {
        // List<String> qaModels = qaModelService.getAllQaModel(vo.getId());
        List<String> qaHitFalseModels = qaModelService.getAllHitFalseQaModel(vo.getId());
        List<String> qaHitModels = qaModelService.getAllHitQaModel(vo.getId());
        //  if (qaModels.size() > 0) {

        qaHitFalseModels = removeViolatonModel(qaHitFalseModels);


        boolean IsCritical = false, IsConfirm = false;
        boolean IsCriticalPositive, IsCriticalNegative, IsConfirmPositive, IsConfirmNegative;


        if (psaeConfig.getCriticalPositiveModelGroup().size() > 0)
            IsCriticalPositive = positiveModelCheck(psaeConfig.getCriticalPositiveModelGroup(), qaHitFalseModels);
        else
            IsCriticalPositive = true;

        if (psaeConfig.getCriticalNegativeModelGroup().size() > 0)
            IsCriticalNegative = negativeModelCheck(psaeConfig.getCriticalNegativeModelGroup(), qaHitModels);
        else
            IsCriticalNegative = false;


        if (psaeConfig.getConfirmPositiveModelGroup().size() > 0)
            IsConfirmPositive = positiveModelCheck(psaeConfig.getConfirmPositiveModelGroup(), qaHitFalseModels);
        else
            IsConfirmPositive = true;

        if (psaeConfig.getConfirmNegativeModelGroup().size() > 0)
            IsConfirmNegative = negativeModelCheck(psaeConfig.getConfirmNegativeModelGroup(), qaHitModels);
        else
            IsConfirmNegative = false;


        //重大瑕疵:正向沒打勾 負向打勾
        if (!IsCriticalPositive || IsCriticalNegative) {
            IsCritical = true;
        }


        //待覆核:正向沒打勾 負向打勾
        if (!IsConfirmPositive || IsConfirmNegative) {
            IsConfirm = true;
        }

        if (IsCritical) {
            status = psaeConfig.getCriticalKey();
        } else if (IsConfirm) {
            status = psaeConfig.getConfirmKey();
        }

        //}
        //}
        return status;
    }

    private List<String> removeViolatonModel(List<String> qaModels) {
        Iterator<String> iterator = qaModels.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            boolean isExist = psaeConfig.getViolationModels().stream().anyMatch(vm -> vm.equalsIgnoreCase(temp));
            if (isExist) iterator.remove();
        }
        return qaModels;
    }

    public boolean negativeModelCheck(List<String> outer, List<String> inner) {
        boolean isContains = false;
        for (String o : outer) {
            isContains = inner.stream().anyMatch(i -> i.equalsIgnoreCase(o));
            if (isContains) break;

        }
        return isContains;
    }


    public boolean positiveModelCheck(List<String> outer, List<String> inner) {
        boolean isContains = false;
        for (String o : outer) {
            isContains = inner.stream().anyMatch(i -> i.equalsIgnoreCase(o));
            if (isContains) break;

        }
        return !isContains;
    }


    public String getKeywordContextByTaskId(Integer qaId,String taskId ,String hitKey ,Integer begin ,Integer end) throws Exception {
        String keywordContext="";
        Map<String, Object> keyWordMap = psaeApiService.getKeywordContextByTaskId(taskId, hitKey, begin, end);
        if(keyWordMap.get("functionResult").equals("SUCCESS")){
            retry = 1;
            logger.info("KeywordContextByTaskId taskId ={},result={}",taskId,keyWordMap.toString());
            Map<String, Object> dataMap = (Map<String, Object>) keyWordMap.get("data");
            keywordContext= dataMap.get("keywordContext").toString();
            return keywordContext;
        }else {
            if (retry <= retryMax) {
                logger.error("取得違規話術失敗 QaJobId ={},taskId={},重新嘗試次數 ={}", qaId, taskId,retry);
                return getKeywordContextByTaskId(qaId,taskId,hitKey,begin,end);
            } else {
                logger.error("取得違規話術失敗 QaJobId ={},taskId={}", qaId, taskId);
                retry = 1;
                return keywordContext;
            }
        }
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
