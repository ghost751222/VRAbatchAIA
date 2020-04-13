package com.macaron.vra.entity;


public class QaDesignQaterm {
    private long id;
    private long isBoundModel;
    private String name;
    private long parentID;
    private long qatemplateID;
    private Object relation;
    private Object remark;
    private Object score;
    private Object termProperty;
    private long termType;

    public long getID() {
        return id;
    }

    public void setID(long value) {
        this.id = value;
    }

    public long getIsBoundModel() {
        return isBoundModel;
    }

    public void setIsBoundModel(long value) {
        this.isBoundModel = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long value) {
        this.parentID = value;
    }

    public long getQatemplateID() {
        return qatemplateID;
    }

    public void setQatemplateID(long value) {
        this.qatemplateID = value;
    }

    public Object getRelation() {
        return relation;
    }

    public void setRelation(Object value) {
        this.relation = value;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object value) {
        this.remark = value;
    }

    public Object getScore() {
        return score;
    }

    public void setScore(Object value) {
        this.score = value;
    }

    public Object getTermProperty() {
        return termProperty;
    }

    public void setTermProperty(Object value) {
        this.termProperty = value;
    }

    public long getTermType() {
        return termType;
    }

    public void setTermType(long value) {
        this.termType = value;
    }
}

