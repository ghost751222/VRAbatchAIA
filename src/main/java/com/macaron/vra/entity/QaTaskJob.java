package com.macaron.vra.entity;

import java.util.Date;

public class QaTaskJob implements Comparable<QaTaskJob> {
    private Integer id;
    private String state;
    private String show_state;
    private Integer qa_id;
    private String qa_name;
    private Integer machine_score;
    private Integer human_score;
    private Integer hit_term_num;
    private Date send_date;
    private Date finish_date;
    private Date listen_start_time;
    private Date listen_end_time;
    private Integer listen_time;
    private String is_qa;
    private String is_appeal;
    private Integer appeal_count;
    private String is_human_end;
    private Integer qatemplate_id;
    private String agent_id;
    private String remark;
    private String task;
    private Date task_date;
    private String case_content;
    private Date insert_time;
    private String send_type;
    private String is_back;
    private String send_people;
    private String qa_remark;
    private String qa_result;
    private String psae_ApplicationFormID;
    private String psae_AppID;
    private String psae_TMRPendingCode;
    private String psae_TMRPendingCode1;
    private String psae_TMRPendingCode2;
    private String psae_SCFPendingCode;
    private String psae_SCFPendingCode1;
    private String psae_SCFPendingCode2;
    private String psae_SCFRejectRemedyPendingCode1;
    private String psae_SCFRejectRemedyPendingCode2;
    private String psae_TMRPendingCodeName;
    private String psae_TMRPendingCode1Name;
    private String psae_TMRPendingCode2Name;
    private String psae_SCFPendingCodeName;
    private String psae_SCFPendingCode1Name;
    private String psae_SCFPendingCode2Name;
    private String psae_SCFRejectRemedyPendingCode1Name;
    private String psae_SCFRejectRemedyPendingCode2Name;
    private String psae_PolicyNo;

    public Date getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Date insert_time) {
        this.insert_time = insert_time;
    }

    public String getMachine_hits() {
        return machine_hits;
    }

    public void setMachine_hits(String machine_hits) {
        this.machine_hits = machine_hits;
    }

    private String machine_hits;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShow_state() {
        return show_state;
    }

    public void setShow_state(String show_state) {
        this.show_state = show_state;
    }

    public Integer getQa_id() {
        return qa_id;
    }

    public void setQa_id(Integer qa_id) {
        this.qa_id = qa_id;
    }

    public String getQa_name() {
        return qa_name;
    }

    public void setQa_name(String qa_name) {
        this.qa_name = qa_name;
    }

    public Integer getMachine_score() {
        return machine_score;
    }

    public void setMachine_score(Integer machine_score) {
        this.machine_score = machine_score;
    }

    public Integer getHuman_score() {
        return human_score;
    }

    public void setHuman_score(Integer human_score) {
        this.human_score = human_score;
    }

    public Integer getHit_term_num() {
        return hit_term_num;
    }

    public void setHit_term_num(Integer hit_term_num) {
        this.hit_term_num = hit_term_num;
    }

    public Date getSend_date() {
        return send_date;
    }

    public void setSend_date(Date send_date) {
        this.send_date = send_date;
    }

    public Date getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(Date finish_date) {
        this.finish_date = finish_date;
    }

    public Date getListen_start_time() {
        return listen_start_time;
    }

    public void setListen_start_time(Date listen_start_time) {
        this.listen_start_time = listen_start_time;
    }

    public Date getListen_end_time() {
        return listen_end_time;
    }

    public void setListen_end_time(Date listen_end_time) {
        this.listen_end_time = listen_end_time;
    }

    public Integer getListen_time() {
        return listen_time;
    }

    public void setListen_time(Integer listen_time) {
        this.listen_time = listen_time;
    }

    public String getIs_qa() {
        return is_qa;
    }

    public void setIs_qa(String is_qa) {
        this.is_qa = is_qa;
    }

    public String getIs_appeal() {
        return is_appeal;
    }

    public void setIs_appeal(String is_appeal) {
        this.is_appeal = is_appeal;
    }

    public Integer getAppeal_count() {
        return appeal_count;
    }

    public void setAppeal_count(Integer appeal_count) {
        this.appeal_count = appeal_count;
    }

    public String getIs_human_end() {
        return is_human_end;
    }

    public void setIs_human_end(String is_human_end) {
        this.is_human_end = is_human_end;
    }

    public Integer getQatemplate_id() {
        return qatemplate_id;
    }

    public void setQatemplate_id(Integer qatemplate_id) {
        this.qatemplate_id = qatemplate_id;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Date getTask_date() {
        return task_date;
    }

    public void setTask_date(Date task_date) {
        this.task_date = task_date;
    }

    public String getSend_type() {
        return send_type;
    }

    public void setSend_type(String send_type) {
        this.send_type = send_type;
    }

    public String getIs_back() {
        return is_back;
    }

    public void setIs_back(String is_back) {
        this.is_back = is_back;
    }

    public String getSend_people() {
        return send_people;
    }

    public void setSend_people(String send_people) {
        this.send_people = send_people;
    }

    public String getQa_remark() {
        return qa_remark;
    }

    public void setQa_remark(String qa_remark) {
        this.qa_remark = qa_remark;
    }

    public String getQa_result() {
        return qa_result;
    }

    public void setQa_result(String qa_result) {
        this.qa_result = qa_result;
    }

    public String getPsae_ApplicationFormID() {
        return psae_ApplicationFormID;
    }

    public void setPsae_ApplicationFormID(String psae_ApplicationFormID) {
        this.psae_ApplicationFormID = psae_ApplicationFormID;
    }

    public String getCase_content() {
        return case_content;
    }

    public void setCase_content(String case_content) {
        this.case_content = case_content;
    }

    public String getPsae_AppID() {
        return psae_AppID;
    }

    public void setPsae_AppID(String psae_AppID) {
        this.psae_AppID = psae_AppID;
    }

    public String getPsae_TMRPendingCode() {
        return psae_TMRPendingCode;
    }

    public void setPsae_TMRPendingCode(String psae_TMRPendingCode) {
        this.psae_TMRPendingCode = psae_TMRPendingCode;
    }

    public String getPsae_TMRPendingCode1() {
        return psae_TMRPendingCode1;
    }

    public void setPsae_TMRPendingCode1(String psae_TMRPendingCode1) {
        this.psae_TMRPendingCode1 = psae_TMRPendingCode1;
    }

    public String getPsae_TMRPendingCode2() {
        return psae_TMRPendingCode2;
    }

    public void setPsae_TMRPendingCode2(String psae_TMRPendingCode2) {
        this.psae_TMRPendingCode2 = psae_TMRPendingCode2;
    }

    public String getPsae_SCFPendingCode() {
        return psae_SCFPendingCode;
    }

    public void setPsae_SCFPendingCode(String psae_SCFPendingCode) {
        this.psae_SCFPendingCode = psae_SCFPendingCode;
    }

    public String getPsae_SCFPendingCode1() {
        return psae_SCFPendingCode1;
    }

    public void setPsae_SCFPendingCode1(String psae_SCFPendingCode1) {
        this.psae_SCFPendingCode1 = psae_SCFPendingCode1;
    }

    public String getPsae_SCFPendingCode2() {
        return psae_SCFPendingCode2;
    }

    public void setPsae_SCFPendingCode2(String psae_SCFPendingCode2) {
        this.psae_SCFPendingCode2 = psae_SCFPendingCode2;
    }

    public String getPsae_SCFRejectRemedyPendingCode1() {
        return psae_SCFRejectRemedyPendingCode1;
    }

    public void setPsae_SCFRejectRemedyPendingCode1(String psae_SCFRejectRemedyPendingCode1) {
        this.psae_SCFRejectRemedyPendingCode1 = psae_SCFRejectRemedyPendingCode1;
    }

    public String getPsae_SCFRejectRemedyPendingCode2() {
        return psae_SCFRejectRemedyPendingCode2;
    }

    public void setPsae_SCFRejectRemedyPendingCode2(String psae_SCFRejectRemedyPendingCode2) {
        this.psae_SCFRejectRemedyPendingCode2 = psae_SCFRejectRemedyPendingCode2;
    }

    public String getPsae_TMRPendingCodeName() {
        return psae_TMRPendingCodeName;
    }

    public void setPsae_TMRPendingCodeName(String psae_TMRPendingCodeName) {
        this.psae_TMRPendingCodeName = psae_TMRPendingCodeName;
    }

    public String getPsae_TMRPendingCode1Name() {
        return psae_TMRPendingCode1Name;
    }

    public void setPsae_TMRPendingCode1Name(String psae_TMRPendingCode1Name) {
        this.psae_TMRPendingCode1Name = psae_TMRPendingCode1Name;
    }

    public String getPsae_TMRPendingCode2Name() {
        return psae_TMRPendingCode2Name;
    }

    public void setPsae_TMRPendingCode2Name(String psae_TMRPendingCode2Name) {
        this.psae_TMRPendingCode2Name = psae_TMRPendingCode2Name;
    }

    public String getPsae_SCFPendingCodeName() {
        return psae_SCFPendingCodeName;
    }

    public void setPsae_SCFPendingCodeName(String psae_SCFPendingCodeName) {
        this.psae_SCFPendingCodeName = psae_SCFPendingCodeName;
    }

    public String getPsae_SCFPendingCode1Name() {
        return psae_SCFPendingCode1Name;
    }

    public void setPsae_SCFPendingCode1Name(String psae_SCFPendingCode1Name) {
        this.psae_SCFPendingCode1Name = psae_SCFPendingCode1Name;
    }

    public String getPsae_SCFPendingCode2Name() {
        return psae_SCFPendingCode2Name;
    }

    public void setPsae_SCFPendingCode2Name(String psae_SCFPendingCode2Name) {
        this.psae_SCFPendingCode2Name = psae_SCFPendingCode2Name;
    }

    public String getPsae_SCFRejectRemedyPendingCode1Name() {
        return psae_SCFRejectRemedyPendingCode1Name;
    }

    public void setPsae_SCFRejectRemedyPendingCode1Name(String psae_SCFRejectRemedyPendingCode1Name) {
        this.psae_SCFRejectRemedyPendingCode1Name = psae_SCFRejectRemedyPendingCode1Name;
    }

    public String getPsae_SCFRejectRemedyPendingCode2Name() {
        return psae_SCFRejectRemedyPendingCode2Name;
    }

    public void setPsae_SCFRejectRemedyPendingCode2Name(String psae_SCFRejectRemedyPendingCode2Name) {
        this.psae_SCFRejectRemedyPendingCode2Name = psae_SCFRejectRemedyPendingCode2Name;
    }

    public String getPsae_PolicyNo() {
        return psae_PolicyNo;
    }

    public void setPsae_PolicyNo(String psae_PolicyNo) {
        this.psae_PolicyNo = psae_PolicyNo;
    }

    @Override
    public int compareTo(QaTaskJob o) {
        return getId().compareTo(o.getId());
    }
}
