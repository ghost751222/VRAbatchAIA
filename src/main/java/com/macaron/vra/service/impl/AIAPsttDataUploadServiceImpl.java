package com.macaron.vra.service.impl;

import com.macaron.vra.constant.AppConst;
import com.macaron.vra.dao.impl.*;
import com.macaron.vra.entity.ADSPolicyInfo;
import com.macaron.vra.entity.ADSUsers;
import com.macaron.vra.entity.ADSVoiceDetail;
import com.macaron.vra.entity.ADSVoiceMaster;
import com.macaron.vra.util.FileSystemUtil;
import com.macaron.vra.util.FtpUtil;
import com.macaron.vra.util.WavUtil;
import com.macaron.vra.vo.AIAProcessVo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.Selectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service
public class AIAPsttDataUploadServiceImpl {

    @Value("${spring.file.delete:true}")
    boolean IsFileDelete;

    @Value("${AIAPsttTempDir}")
    String AIAPsttTempDir;

    private static final Logger logger = LoggerFactory.getLogger(AIAPsttDataUploadServiceImpl.class);

    public static final String PSTT_TASK_SERIAL_PREFIX_DATE_PATTERN = "yyyy-MM-dd-HH-mm";

    public static final String DIMESION = "Dimension";

    public static final String SPEECH = "Speech";

    public static final String LOCALBATCHWORKTMP = "batchWorkTmp";

    public static final String DATEFORMAT = "yyyy-MM-dd";

    public static String flagfileName = "flag.new";

    // private ADSVoiceMaster adsVoiceMasterVo = null;

    @Autowired
    private ADSVoiceMasterImpl adsVoiceMasterImpl;

    @Autowired
    private ADSVoiceDetailDaoImpl adsVoiceDetailDaoImpl;

    @Autowired
    private ADSPolicyInfoDaoImpl adsPolicyInfoDaoImpl;

    @Autowired
    private ADSPendingCodeDaoImpl adsPendingCodeDaoImpl;

    @Autowired
    private ADSUsersDaoImpl adsUsersDaoImpl;


    private void process(ADSVoiceMaster adsVoiceMasterVo) throws IOException {

        if (adsVoiceMasterVo != null) {
            FileObject cvtWavTmp = null;
            Path basePath = null;

            boolean IsSuccess = false;
            String status = "01";
            Path source = null, destination = null, AIAPsttTempDirPath = null;
            try {

                logger.info("AIAPsttDataUploadServiceImpl. start BatchNo {}", adsVoiceMasterVo.getBatchNo());

                basePath = Paths.get(LOCALBATCHWORKTMP, adsVoiceMasterVo.getBatchNo());


                Path dimesionPath = Paths.get(basePath.toString(), DIMESION);
                Path speechPath = Paths.get(basePath.toString(), SPEECH);

                String outfileName = adsVoiceMasterVo.getBatchNo()
                        .replaceAll("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1-$2-$3-$4-$5-$6");

                Path executeDimesionPath = Paths.get(dimesionPath.toString(), outfileName);
                Path executeSpeechPath = Paths.get(speechPath.toString(), outfileName);

                Path cvtWavTmpPath = Paths.get(executeSpeechPath.toString(), "cvtWavTmp");

                Path speechSubPath = Paths.get(executeSpeechPath.toString(), "file");

                SimpleDateFormat sdFormat = new SimpleDateFormat(DATEFORMAT);
                String dimExtension = "dim";
                String dimfileName = null;
                String expectDim = outfileName.substring(0, 10);
                Date currentDate = Calendar.getInstance().getTime();
                if (sdFormat.parse(expectDim).after(currentDate)) {
                    dimfileName = String.format("%s.%s", sdFormat.format(currentDate), dimExtension);
                } else {
                    dimfileName = String.format("%s.%s", expectDim, dimExtension);
                }

                File dimensionFile = new File(executeDimesionPath.toFile(), dimfileName);
                File speechFile = new File(executeSpeechPath.toFile(), dimfileName);
                File flagDimFile = new File(executeDimesionPath.toFile(), flagfileName);
                File flagSpeechFile = new File(executeSpeechPath.toFile(), flagfileName);

                // ex :\batchWorkTmp\201905020930\Dimension
                FileSystemUtil.createFolder(executeDimesionPath.toFile().getCanonicalPath());

                // ex :\batchWorkTmp\201905020930\Speech
                FileSystemUtil.createFolder(executeSpeechPath.toFile().getCanonicalPath());

                // ex :\batchWorkTmp\201905020930\cvtWavTmp
                cvtWavTmp = FileSystemUtil.createFolder(cvtWavTmpPath.toFile().getCanonicalPath());

                // ex : \batchWorkTmp\201905020930\Speech\2019-05-02-09-30\file
                FileSystemUtil.createFolder(speechSubPath.toFile().getCanonicalPath());

                // ex : \batchWorkTmp\201905020930\Dimension\2019-05-02-09-30\2019-05-02.dim
                FileObject dimesionObject = FileSystemUtil.createFile(dimensionFile.getAbsolutePath());

                // ex : \batchWorkTmp\201905020930\Speech\2019-05-02-09-30\2019-05-02.dim
                FileObject speechObject = FileSystemUtil.createFile(speechFile.getAbsolutePath());

                String remoteDimession = Paths.get(AppConst.AIAPsttFtpDataSourceDirPath, DIMESION, outfileName)
                        .toString();
                remoteDimession = remoteDimession.replace("\\", "/");

//                if (exists(remoteDimession)) {
//                    throw new RuntimeException(" remoteDimession file exists:" + remoteDimession);
//                }
                String remoteSpeech = Paths.get(AppConst.AIAPsttFtpDataSourceDirPath, SPEECH, outfileName).toString();
                remoteSpeech = remoteSpeech.replace("\\", "/");
//                if (exists(remoteDimession)) {
//                    throw new RuntimeException("remoteSpeech file exists:" + remoteSpeech);
//                }

                adsVoiceMasterVo.setSTTProcessStatus(status);
                adsVoiceMasterVo.setSTTStartTime(Calendar.getInstance().getTime());
                adsVoiceMasterVo.setSTTCompleteTime(null);
                updateADSVoiceMaster(adsVoiceMasterVo);

                List<ADSVoiceDetail> adsVoiceDetails = adsVoiceDetailDaoImpl
                        .queryAllVoiceDetailByBatchNo(adsVoiceMasterVo.getBatchNo());

                Map<String, AIAProcessVo> groupMap = new HashMap<String, AIAProcessVo>();
                Map<String, String> codes = adsPendingCodeDaoImpl.queryAllToMap();
                List<ADSUsers> users = adsUsersDaoImpl.findAll();


                AIAPsttTempDirPath = Paths.get(AIAPsttTempDir, adsVoiceMasterVo.getBatchNo());
                String mapKeyId = null;
                for (ADSVoiceDetail adsVoiceDetail : adsVoiceDetails) {

                    String appId = adsVoiceDetail.getAppId();
                    String contactId = adsVoiceDetail.getContactId();
                    String isDealDone = adsVoiceDetail.getIsDealDone();
                    ADSPolicyInfo adsPolicyInfo = adsPolicyInfoDaoImpl.queryPolicyInfoByAppId(appId);

                    if (adsPolicyInfo != null) {
                        mapKeyId = appId;
                    } else {
                        mapKeyId = contactId;
                        adsPolicyInfo = new ADSPolicyInfo();
                    }


                    AIAProcessVo vo = groupMap.get(mapKeyId);
                    if (vo == null) {
                        vo = new AIAProcessVo();
                        vo.setAdsPolicyInfo(adsPolicyInfo);
                        vo.setAdsVoiceDetails(new ArrayList<>());
                        vo.getAdsVoiceDetails().add(adsVoiceDetail);
                        groupMap.put(mapKeyId, vo);
                    } else {
                        vo.getAdsVoiceDetails().add(adsVoiceDetail);
                    }


                    switch (isDealDone) {
                        case "C": //補call
                            vo.setIsDealDone(isDealDone);
                            break;
                        case "N": //未成交
                            vo.setIsDealDone(isDealDone);
                            vo.getAdsPolicyInfo().setHolderMobile(adsVoiceDetail.getDialPhone());
                            break;
                    }

                    source = Paths.get(AIAPsttTempDirPath.toString(), adsVoiceDetail.getFileName());
                    destination = Paths.get(cvtWavTmpPath.toString(), adsVoiceDetail.getFileName());
                    copyAudioToWavTmp(source, destination);


//                    String appId = adsVoiceDetail.getAppId();
//                    String contactId = adsVoiceDetail.getContactId();
//                    AIAProcessVo vo = groupMap.get(appId);
//
//                    if (vo == null) {
//                        ADSPolicyInfo adsPolicyInfo = adsPolicyInfoDaoImpl.queryPolicyInfoByAppId(appId);
//                        vo = new AIAProcessVo();
//                        vo.setAdsPolicyInfo(adsPolicyInfo);
//                        vo.setAdsVoiceDetails(new ArrayList<>());
//                        vo.getAdsVoiceDetails().add(adsVoiceDetail);
//                        groupMap.put(appId, vo);
//                    } else {
//                        vo.getAdsVoiceDetails().add(adsVoiceDetail);
//                    }
//
//                    source = Paths.get(AIAPsttTempDirPath.toString(), adsVoiceDetail.getFileName());
//                    destination = Paths.get(cvtWavTmpPath.toString(), adsVoiceDetail.getFileName());
//                    copyAudioToWavTmp(source, destination);
                }

                setPendingCode(groupMap, codes);
                setUserName(groupMap, users);
                WavUtil.convertAllAudioToWav(cvtWavTmpPath, speechSubPath);
                writeDim(groupMap, adsVoiceMasterVo, dimesionObject, speechObject);
                deleteWavTmp(cvtWavTmp);

                upload(executeDimesionPath.toAbsolutePath().toString(), remoteDimession);
                upload(executeSpeechPath.toAbsolutePath().toString(), remoteSpeech);

                // ex : \batchWorkTmp\201905020930\Dimension\2019-05-02-09-30\flag.new
                FileSystemUtil.createFile(flagDimFile.getAbsolutePath());

                // ex : \batchWorkTmp\201905020930\Speech\2019-05-02-09-30\flag.new
                FileSystemUtil.createFile(flagSpeechFile.getAbsolutePath());

                upload(flagDimFile.getAbsolutePath(), remoteDimession + "/" + flagfileName);
                upload(flagSpeechFile.getAbsolutePath(), remoteSpeech + "/" + flagfileName);
                IsSuccess = true;
                status = "00";
                logger.info("AIAPsttDataUploadServiceImpl. end BatchNo {}", adsVoiceMasterVo.getBatchNo());

            } catch (Exception ex) {
                logger.error(" process ADSVoiceMaster failed {}", ex);
                status = "03"; //error
            } finally {
                adsVoiceMasterVo.setSTTProcessStatus(status);
                adsVoiceMasterVo.setSTTCompleteTime(Calendar.getInstance().getTime());
                updateADSVoiceMaster(adsVoiceMasterVo);
                deleteWavTmp(cvtWavTmp);
                if (basePath != null) {
                    deleteDirectory(basePath.toFile());
                }

                if (IsFileDelete && IsSuccess) {
                    if (AIAPsttTempDirPath != null) {
                        deleteDirectory(AIAPsttTempDirPath.toFile());
                    }
                }
            }
        }

    }

    private void deleteWavTmp(FileObject cvtWavTmp) {
        if (cvtWavTmp != null) {
            try {
                cvtWavTmp.delete(Selectors.SELECT_ALL);
            } catch (org.apache.commons.vfs2.FileSystemException e) {
                logger.error("cvtWavTmp delete {}", e);
            }
        }

    }

    private void setUserName(Map<String, AIAProcessVo> groupMap, List<ADSUsers> users) {
        for (Entry<String, AIAProcessVo> entry : groupMap.entrySet()) {
            AIAProcessVo vo = entry.getValue();
            ADSPolicyInfo adsPolicyInfo = vo.getAdsPolicyInfo();
            List<ADSVoiceDetail> details = vo.getAdsVoiceDetails();
            String agentId = adsPolicyInfo.getScfagentId();
            //	String tmrSellerId = adsPolicyInfo.getTmrsellerId();
            users.forEach(u -> {
                if (u.getUserID().equals(agentId))
                    vo.setSCFAgentName(u.getUserName());

//				if (u.getUserID().equals(tmrSellerId))
//					vo.setTMRSellerName(u.getUserName());

                details.forEach(d -> {
                    if (u.getUserID().equals(d.getTmrId()))
                        d.setTmrName(u.getUserName());
                });
            });
        }
    }

    private void setPendingCode(Map<String, AIAProcessVo> groupMap, Map<String, String> codes) {
        for (Entry<String, AIAProcessVo> entry : groupMap.entrySet()) {
            setPendingCode(entry.getValue(), codes);
        }
    }

    private void setPendingCode(AIAProcessVo aiaProcessVo, Map<String, String> codes) {
        ADSPolicyInfo adsPolicyInfo = aiaProcessVo.getAdsPolicyInfo();
        String tmrPendingCode = adsPolicyInfo.getTmrpendingCode();
        String tmrPendingCode1 = adsPolicyInfo.getTmrpendingCode1();
        String tmrPendingCode2 = adsPolicyInfo.getTmrpendingCode2();

        String scfPendingCode = adsPolicyInfo.getScfpendingCode();
        String scfPendingCode1 = adsPolicyInfo.getScfpendingCode1();
        String scfPendingCode2 = adsPolicyInfo.getScfpendingCode2();

        String processCode1 = adsPolicyInfo.getProcessCode1();
        String processCode2 = adsPolicyInfo.getProcessCode2();
        String scfrejectRemedyPendingCode1 = adsPolicyInfo.getScfrejectRemedyPendingCode1();
        String scfrejectRemedyPendingCode2 = adsPolicyInfo.getScfrejectRemedyPendingCode2();

        aiaProcessVo.setSCFPendingCodeName(codes.get(scfPendingCode));
        aiaProcessVo.setSCFPendingCode1Name(codes.get(scfPendingCode1));
        aiaProcessVo.setSCFPendingCode2Name(codes.get(scfPendingCode2));

        aiaProcessVo.setTMRPendingCodeName(codes.get(tmrPendingCode));
        aiaProcessVo.setTMRPendingCode1Name(codes.get(tmrPendingCode1));
        aiaProcessVo.setTMRPendingCode2Name(codes.get(tmrPendingCode2));

        aiaProcessVo.setProcessCode1Name(codes.get(processCode1));
        aiaProcessVo.setProcessCode2Name(codes.get(processCode2));

        aiaProcessVo.setSCFRejectRemedyPendingCode1Name(codes.get(scfrejectRemedyPendingCode1));
        aiaProcessVo.setSCFRejectRemedyPendingCode2Name(codes.get(scfrejectRemedyPendingCode2));
    }

    public void process(String batchNo) {

        try {
            ADSVoiceMaster adsVoiceMasterVo = null;
            if (!StringUtils.isEmpty(batchNo)) {
                adsVoiceMasterVo = adsVoiceMasterImpl.queryVoiceMasterByBatchNo(batchNo);
            } else {
                adsVoiceMasterVo = adsVoiceMasterImpl.queryVoiceMaster();
            }
            process(adsVoiceMasterVo);
        } catch (Exception e) {
            logger.error("process batchNo failed {}", e);
        }
    }

    private void writeDim(Map<String, AIAProcessVo> groupMap, ADSVoiceMaster adsVoiceMaster, FileObject dimesionObject,
                          FileObject speechObject) {
        StringBuilder dimensionText = new StringBuilder();
        StringBuilder speechText = new StringBuilder();
        String split = "|";

//        speechText.append("电话流水号|FilePath|").append("@filename@").append(split).append("@ContactID@").append(split)
//                .append("@AppID@").append(split).append("@starttime@").append(split).append("@endtime@").append(split)
//                .append("@dialphone@").append(split).append("@TmrID@").append(split).append("@TeamCode@").append(split)
//                .append("@DeskCode@").append(split).append("@CampaignCode@").append(split).append("@CampaignName@")
//                .append(split).append("@CampaignBatch@").append(split).append("@TmrName@").append(split).append("@IsDealDone@")
//
//        ;
        speechText.append("电话流水号").append(split)
                .append("FilePath").append(split)
                .append("@filename@").append(split)
                .append("@ContactID@").append(split)
                .append("@AppID@").append(split)
                .append("@starttime@").append(split)
                .append("@endtime@").append(split)
                .append("@dialphone@").append(split)
                .append("@TmrID@").append(split)
                .append("@TeamCode@").append(split)
                .append("@DeskCode@").append(split)
                .append("@CampaignCode@").append(split)
                .append("@CampaignName@").append(split)
                .append("@CampaignBatch@").append(split)
                .append("@TmrName@").append(split)
                .append("@IsDealDone@").append(split)
                .append("@TermCode@").append(split)
                .append("@TermCodeName@")
        ;

        dimensionText.append("任务流水号").append(split).append("录音列表").append(split)
                .append("@Time@").append(split)
                .append("@AppID@").append(split)
                .append("@ApplicationFormID@").append(split).append("@PolicyNo@").append(split).append("@DealTime@")
                .append(split).append("@CampaignCode@").append(split).append("@CampaignName@").append(split)
                .append("@CampaignNameA@").append(split).append("@CampaignNameB@").append(split).append("@ProductCode@")
                .append(split).append("@ProductName@").append(split).append("@IplanKind@").append(split)
                .append("@GroupName@").append(split).append("@DeskCode@").append(split).append("@TeamCode@")
                .append(split).append("@TMRID@").append(split).append("@TMRName@").append(split)


                .append("@SCFAgentID@").append(split).append("@TMRSellerID@").append(split).append("@TMRSite@")
                .append(split).append("@TLSubmitTime@").append(split).append("@SCFSubmitTime@").append(split)
                .append("@HolderName@").append(split).append("@TMRPendingCode@").append(split)
                .append("@TMRPendingCode1@").append(split).append("@TMRPendingCode2@").append(split)
                .append("@SCFPendingCode@").append(split).append("@SCFPendingCode1@").append(split)
                .append("@SCFPendingCode2@").append(split).append("@SCFRemark@").append(split).append("@ProcessCode1@")
                .append(split).append("@ProcessCode2@").append(split).append("@TMRLeaderRemark@").append(split)
                .append("@TMRRemark@").append(split).append("@TMRReplyTime@").append(split)

                .append("@SCFRejectRemedyExecTime@").append(split).append("@SCFRejectRemedyPendingCode1@").append(split)
                .append("@SCFRejectRemedyPendingCode2@").append(split).append("@SCFRejectRemedyRemark@").append(split)
                .append("@SCFGetEmail@").append(split)

                .append("@DealWay@").append(split).append("@InsuredName@").append(split).append("@HolderHomeTel@")
                .append(split).append("@HolderCompanyTel@").append(split).append("@HolderMobile@").append(split)
                .append("@TARP@").append(split).append("@TMRPendingCodeName@").append(split)
                .append("@TMRPendingCode1Name@").append(split).append("@TMRPendingCode2Name@").append(split)
                .append("@SCFPendingCodeName@").append(split).append("@SCFPendingCode1Name@").append(split)
                .append("@SCFPendingCode2Name@").append(split).append("@ProcessCode1Name@").append(split)
                .append("@ProcessCode2Name@").append(split).append("@SCFRejectRemedyPendingCode1Name@").append(split)
                .append("@SCFRejectRemedyPendingCode2Name@").append(split).append("@SCFAgentName@").append(split)
                .append("@EQRejection@").append(split)
                .append("@IsDealDone@").append(split)
                .append("@ReSales@").append(split)

                .append("@IVRPersonalInfo@").append(split)
                .append("@IVRProtection@").append(split)
                .append("@IVRBeneficyPayment@").append(split)
                .append("@IVRHolderIsContact@").append(split)
                .append("@CashSurrender70up@").append(split)
                .append("@RegisterID@").append(split)
                .append("@AbandonOldAndNew@")
        ;


        BufferedWriter speechBw = null;
        BufferedWriter dimensionBw = null;
        try {

            speechBw = new BufferedWriter(
                    new OutputStreamWriter(speechObject.getContent().getOutputStream(), StandardCharsets.UTF_8));
            dimensionBw = new BufferedWriter(
                    new OutputStreamWriter(dimesionObject.getContent().getOutputStream(), StandardCharsets.UTF_8));
            // speech header
            speechBw.write(speechText.toString());
            speechBw.newLine();
            speechText.delete(0, speechText.length());
            // dimension header
            dimensionBw.write(dimensionText.toString());
            dimensionBw.newLine();
            dimensionText.delete(0, dimensionText.length());

            int count = 0;
            int detailConut = 0;
            String taskTime = adsVoiceMaster.getBatchNo().replaceAll("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})",
                    "$1-$2-$3 $4:$5:$6");
            List<Integer> speechIndex = new ArrayList<>();

            for (Entry<String, AIAProcessVo> entry : groupMap.entrySet()) {

                AIAProcessVo aiaProcessVo = entry.getValue();
                ADSPolicyInfo adsPolicyInfo = aiaProcessVo.getAdsPolicyInfo();
                List<ADSVoiceDetail> adsVoiceDetails = aiaProcessVo.getAdsVoiceDetails();
                speechIndex.clear();
                for (ADSVoiceDetail adsVoiceDetail : adsVoiceDetails) {
                    String wavName = adsVoiceDetail.getFileName().replace(".vox", ".wav");
//                    speechText.append(detailConut).append(split).append("file/").append(wavName).append(split)
//                            .append(wavName).append(split).append(adsVoiceDetail.getContactId()).append(split)
//                            .append(adsVoiceDetail.getAppId()).append(split).append(adsVoiceDetail.getStarttime())
//                            .append(split).append(adsVoiceDetail.getEndtime()).append(split)
//                            .append(adsVoiceDetail.getDialPhone()).append(split).append(adsVoiceDetail.getTmrId())
//                            .append(split).append(adsVoiceDetail.getTeamCode()).append(split)
//                            .append(adsVoiceDetail.getDeskCode()).append(split).append(adsVoiceDetail.getCampaignCode())
//                            .append(split).append(adsVoiceDetail.getCampaignName()).append(split)
//                            .append(adsVoiceDetail.getCampaignBatch()).append(split)
//                            .append(adsVoiceDetail.getTmrName()).append(split)
//                            .append(adsVoiceDetail.getIsDealDone())
                    speechText.append(detailConut).append(split)
                            .append("file/").append(wavName).append(split)
                            .append(wavName).append(split)
                            .append(adsVoiceDetail.getContactId()).append(split)
                            .append(adsVoiceDetail.getAppId()).append(split)
                            .append(adsVoiceDetail.getStarttime()).append(split)
                            .append(adsVoiceDetail.getEndtime()).append(split)
                            .append(adsVoiceDetail.getDialPhone()).append(split)
                            .append(adsVoiceDetail.getTmrId()).append(split)
                            .append(adsVoiceDetail.getTeamCode()).append(split)
                            .append(adsVoiceDetail.getDeskCode()).append(split)
                            .append(adsVoiceDetail.getCampaignCode()).append(split)
                            .append(adsVoiceDetail.getCampaignName()).append(split)
                            .append(adsVoiceDetail.getCampaignBatch()).append(split)
                            .append(adsVoiceDetail.getTmrName()).append(split)
                            .append(adsVoiceDetail.getIsDealDone()).append(split)
                            .append(adsVoiceDetail.getTermCode() ==null ? "" :adsVoiceDetail.getTermCode() ).append(split)
                            .append(adsVoiceDetail.getTermCodeName() == null ? "":adsVoiceDetail.getTermCodeName())
;
                    speechBw.write(speechText.toString());
                    speechBw.newLine();
                    speechText.delete(0, speechText.length());

                    speechIndex.add(detailConut);
                    detailConut++;
                }

                dimensionText.append(count).append(split).append(StringUtils.join(speechIndex, ",")).append(split)
                        .append(taskTime).append(split)
                        .append(adsPolicyInfo.getAppId() == null ? "" : adsPolicyInfo.getAppId()).append(split)
                        .append((adsPolicyInfo.getApplicationFormId() == null) ? "" : adsPolicyInfo.getApplicationFormId()).append(split)
                        .append((adsPolicyInfo.getPolicyNo() == null) ? "" : adsPolicyInfo.getPolicyNo()).append(split)
                        .append((adsPolicyInfo.getDealTime() == null) ? "" : adsPolicyInfo.getDealTime()).append(split)
                        .append((adsPolicyInfo.getCampaignCode() == null) ? "" : adsPolicyInfo.getCampaignCode()).append(split)
                        .append((adsPolicyInfo.getCampaignName() == null) ? "" : adsPolicyInfo.getCampaignName()).append(split)
                        .append((adsPolicyInfo.getCampaignA() == null) ? "" : adsPolicyInfo.getCampaignA()).append(split)
                        .append((adsPolicyInfo.getCampaignB() == null) ? "" : adsPolicyInfo.getCampaignB()).append(split)
                        .append((adsPolicyInfo.getProductCode() == null) ? "" : adsPolicyInfo.getProductCode()).append(split)
                        .append((adsPolicyInfo.getProductName() == null) ? "" : adsPolicyInfo.getProductName()).append(split)
                        .append((adsPolicyInfo.getIplanKind() == null) ? "" : adsPolicyInfo.getIplanKind()).append(split)
                        .append((adsPolicyInfo.getTmgroup() == null) ? "" : adsPolicyInfo.getTmgroup()).append(split)
                        .append((adsPolicyInfo.getDeskCode() == null) ? "" : adsPolicyInfo.getDeskCode()).append(split)
                        .append((adsPolicyInfo.getTeamCode() == null) ? "" : adsPolicyInfo.getTeamCode()).append(split)

                        .append((adsPolicyInfo.getTmrid() == null) ? "" : adsPolicyInfo.getTmrid()).append(split)
                        .append((adsPolicyInfo.getTmrname() == null) ? "" : adsPolicyInfo.getTmrname()).append(split)


                        .append((adsPolicyInfo.getScfagentId() == null) ? "" : adsPolicyInfo.getScfagentId()).append(split)
                        .append((adsPolicyInfo.getTmrsellerId() == null) ? "" : adsPolicyInfo.getTmrsellerId()).append(split)
                        .append((adsPolicyInfo.getTmrsite() == null) ? "" : adsPolicyInfo.getTmrsite()).append(split)
                        .append((adsPolicyInfo.getTlsubmitTime() == null) ? "" : adsPolicyInfo.getTlsubmitTime()).append(split)

                        .append((adsPolicyInfo.getScfsubmitTime() == null) ? "" : adsPolicyInfo.getScfsubmitTime()).append(split)
                        .append((adsPolicyInfo.getHolderName() == null) ? "" : adsPolicyInfo.getHolderName()).append(split)
                        .append((adsPolicyInfo.getTmrpendingCode() == null) ? "" : adsPolicyInfo.getTmrpendingCode()).append(split)
                        .append((adsPolicyInfo.getTmrpendingCode1() == null) ? "" : adsPolicyInfo.getTmrpendingCode1()).append(split)
                        .append((adsPolicyInfo.getTmrpendingCode2() == null) ? "" : adsPolicyInfo.getTmrpendingCode2()).append(split)

                        .append((adsPolicyInfo.getScfpendingCode() == null) ? "" : adsPolicyInfo.getScfpendingCode()).append(split)
                        .append((adsPolicyInfo.getScfpendingCode1() == null) ? "" : adsPolicyInfo.getScfpendingCode1()).append(split)
                        .append((adsPolicyInfo.getScfpendingCode2() == null) ? "" : adsPolicyInfo.getScfpendingCode2()).append(split)
                        .append((adsPolicyInfo.getScfremark() == null) ? "" : adsPolicyInfo.getScfremark()).append(split)

                        .append((adsPolicyInfo.getProcessCode1() == null) ? "" : adsPolicyInfo.getProcessCode1()).append(split)
                        .append((adsPolicyInfo.getProcessCode2() == null) ? "" : adsPolicyInfo.getProcessCode2()).append(split)

                        .append((adsPolicyInfo.getTmrleaderRemark() == null) ? "" : adsPolicyInfo.getTmrleaderRemark()).append(split)
                        .append((adsPolicyInfo.getTmrremark() == null) ? "" : adsPolicyInfo.getTmrremark()).append(split)
                        .append((adsPolicyInfo.getTmrreplyTime() == null) ? "" : adsPolicyInfo.getTmrreplyTime()).append(split)

                        .append((adsPolicyInfo.getScfrejectRemedyExecTime() == null) ? "" : adsPolicyInfo.getScfrejectRemedyExecTime()).append(split)
                        .append((adsPolicyInfo.getScfrejectRemedyPendingCode1() == null) ? "" : adsPolicyInfo.getScfrejectRemedyPendingCode1()).append(split)
                        .append((adsPolicyInfo.getScfrejectRemedyPendingCode2() == null) ? "" : adsPolicyInfo.getScfrejectRemedyPendingCode2()).append(split)
                        .append((adsPolicyInfo.getScfrejectRemedyRemark() == null) ? "" : adsPolicyInfo.getScfrejectRemedyRemark()).append(split)
                        .append((adsPolicyInfo.getScfgetEmail() == null) ? "" : adsPolicyInfo.getScfgetEmail()).append(split)
                        .append((adsPolicyInfo.getDealWay() == null) ? "" : adsPolicyInfo.getDealWay()).append(split)

                        .append((adsPolicyInfo.getInsuredName() == null) ? "" : adsPolicyInfo.getInsuredName()).append(split)
                        .append((adsPolicyInfo.getHolderHomeTel() == null) ? "" : adsPolicyInfo.getHolderHomeTel()).append(split)
                        .append((adsPolicyInfo.getHolderCompanyTel() == null) ? "" : adsPolicyInfo.getHolderCompanyTel()).append(split)
                        .append((adsPolicyInfo.getHolderMobile() == null) ? "" : adsPolicyInfo.getHolderMobile()).append(split)
                        .append(adsPolicyInfo.getTarp()).append(split)

                        .append((aiaProcessVo.getTMRPendingCodeName() == null) ? "" : aiaProcessVo.getTMRPendingCodeName()).append(split)
                        .append((aiaProcessVo.getTMRPendingCode1Name() == null) ? "" : aiaProcessVo.getTMRPendingCode2Name()).append(split)
                        .append((aiaProcessVo.getTMRPendingCode2Name() == null) ? "" : aiaProcessVo.getTMRPendingCode2Name()).append(split)
                        .append((aiaProcessVo.getSCFPendingCodeName() == null) ? "" : aiaProcessVo.getSCFPendingCodeName()).append(split)
                        .append((aiaProcessVo.getSCFPendingCode1Name() == null) ? "" : aiaProcessVo.getSCFPendingCode1Name()).append(split)
                        .append((aiaProcessVo.getSCFPendingCode2Name() == null) ? "" : aiaProcessVo.getSCFPendingCode2Name()).append(split)
                        .append((aiaProcessVo.getProcessCode1Name() == null) ? "" : aiaProcessVo.getProcessCode1Name()).append(split)
                        .append((aiaProcessVo.getProcessCode2Name() == null) ? "" : aiaProcessVo.getProcessCode2Name()).append(split)
                        .append((aiaProcessVo.getSCFRejectRemedyPendingCode1Name() == null) ? "" : aiaProcessVo.getSCFRejectRemedyPendingCode1Name()).append(split)
                        .append((aiaProcessVo.getSCFRejectRemedyPendingCode2Name() == null) ? "" : aiaProcessVo.getSCFRejectRemedyPendingCode2Name()).append(split)
                        .append(aiaProcessVo.getSCFAgentName() == null ? "" : aiaProcessVo.getSCFAgentName()).append(split)

                        .append(adsPolicyInfo.getEQRejection() == null ? "" : adsPolicyInfo.getEQRejection()).append(split)
                        .append(aiaProcessVo.getIsDealDone() == null ? "" : aiaProcessVo.getIsDealDone()).append(split)
                        .append(adsPolicyInfo.getReSales() == null ? "" : adsPolicyInfo.getReSales()).append(split)

                        .append(adsPolicyInfo.getIVRPersonalInfo() == null ? "" : adsPolicyInfo.getIVRPersonalInfo()).append(split)
                        .append(adsPolicyInfo.getIVRProtection() == null ? "" : adsPolicyInfo.getIVRProtection()).append(split)
                        .append(adsPolicyInfo.getIVRBeneficyPayment() == null ? "" : adsPolicyInfo.getIVRBeneficyPayment()).append(split)
                        .append(adsPolicyInfo.getIVRHolderIsContact() == null ? "" : adsPolicyInfo.getIVRHolderIsContact()).append(split)
                        .append(adsPolicyInfo.getCashSurrender70up() == null ? "" : adsPolicyInfo.getCashSurrender70up()).append(split)
                        .append(adsPolicyInfo.getRegisterID() == null ? "" : adsPolicyInfo.getRegisterID()).append(split)
                        .append(adsPolicyInfo.getAbandonOldAndNew() == null ? "" : adsPolicyInfo.getAbandonOldAndNew())
                ;


                String text = dimensionText.toString().replaceAll("\n", "").replaceAll("\r", "").replaceAll("\r\n", "");
                dimensionBw.write(text);
                dimensionBw.newLine();
                dimensionText.delete(0, dimensionText.length());

                count++;
            }

        } catch (Exception e) {
            logger.error("{}", e);
            throw new RuntimeException("write dim file failed");
        } finally {
            if (speechBw != null) {
                try {
                    speechBw.close();
                } catch (IOException e) {
                    logger.error("{}", e);
                    throw new RuntimeException("close speech dim file failed");
                }
            }
            if (dimensionBw != null) {
                try {
                    dimensionBw.close();
                } catch (IOException e) {
                    logger.error("{}", e);
                    throw new RuntimeException("close dimension dim file failed");
                }
            }
        }
    }

    private boolean exists(String destFilePath) {
        try {
            return FtpUtil.exists(AppConst.AIAPsttFtpUsername, AppConst.AIAPsttFtpPassword, AppConst.AIAPsttFtpHost,
                    AppConst.AIAPsttFtpPort, destFilePath);
        } catch (Exception e) {
            logger.error("{}", e);
            throw new RuntimeException("upload failed");
        }
    }

    private void upload(String srcDirPath, String destDirPath) {
        try {
            FtpUtil.uploadDir(AppConst.AIAPsttFtpUsername, AppConst.AIAPsttFtpPassword, AppConst.AIAPsttFtpHost,
                    AppConst.AIAPsttFtpPort, destDirPath, srcDirPath);
        } catch (Exception e) {
            logger.error("{}", e);
            throw new RuntimeException("upload failed");
        }
    }

    private boolean copyAudioToWavTmp(Path source, Path destination) throws IOException {
        boolean isCopy = false;

        if (source.toFile().exists()) {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            isCopy = true;
        }
        return isCopy;
    }

//    private Path copyAudioToWavTmp(ADSVoiceDetail adsVoiceDetail, Path cvtWavTmpPath) throws IOException {
//
//        Path source = Paths.get(adsVoiceDetail.getFileNamePath(), adsVoiceDetail.getFileName());
//        Path destination = Paths.get(cvtWavTmpPath.toString(), adsVoiceDetail.getFileName());
//        if (source.toFile().exists())
//            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
//
//        return source;
//    }

    private void deleteDirectory(File destination) throws IOException {
        FileUtils.deleteDirectory(destination);
    }

    private void updateADSVoiceMaster(ADSVoiceMaster adsVoiceMasterVo) {
        if (adsVoiceMasterVo != null) {
            adsVoiceMasterImpl.update(adsVoiceMasterVo);
        }
    }
}
