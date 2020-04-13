package com.macaron.vra.entity;

import java.util.Date;

public class ADSPQACase {

    private String AppID;
    private String CheckItem;
    private String CheckResult;
    private Date CreateTime;


    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getCheckItem() {
        return CheckItem;
    }

    public void setCheckItem(String checkItem) {
        CheckItem = checkItem;
    }

    public String getCheckResult() {
        return CheckResult;
    }

    public void setCheckResult(String checkResult) {
        CheckResult = checkResult;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }
}
