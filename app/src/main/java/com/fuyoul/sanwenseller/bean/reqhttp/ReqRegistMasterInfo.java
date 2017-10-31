package com.fuyoul.sanwenseller.bean.reqhttp;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chen
 * @CreatDate: 2017\10\31 0031
 * @Desc:
 */

public class ReqRegistMasterInfo implements Serializable {

    private String masterName;//姓名
    private long masterId;//id
    private String bankName;//开户银行名称
    private String idCard;//身份证号码
    private String bankNum;//银行卡号
    private String imgPathZ;//正面身份照
    private String imgPathF;//反面身份照

    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public long getMasterId() {
        return masterId;
    }

    public void setMasterId(long masterId) {
        this.masterId = masterId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getImgPathZ() {
        return imgPathZ;
    }

    public void setImgPathZ(String imgPathZ) {
        this.imgPathZ = imgPathZ;
    }

    public String getImgPathF() {
        return imgPathF;
    }

    public void setImgPathF(String imgPathF) {
        this.imgPathF = imgPathF;
    }
}
