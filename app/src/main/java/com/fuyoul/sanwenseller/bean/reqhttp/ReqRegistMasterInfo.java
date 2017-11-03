package com.fuyoul.sanwenseller.bean.reqhttp;

import com.fuyoul.sanwenseller.bean.reshttp.ResTagBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chen
 * @CreatDate: 2017\10\31 0031
 * @Desc:
 */

public class ReqRegistMasterInfo implements Serializable {

    private String realName;//姓名
    private long userInfoId;//id
    private String bankName;//开户银行名称
    private String idCard;//身份证号码
    private String bankCard;//银行卡号
    private String idCardFront;//正面身份照
    private String idCardBack;//反面身份照
    private String selfExp;//个人经历
    private String selfInfo;//个人简介
    private List<ResTagBean> labelList;

    public long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(long userInfoId) {
        this.userInfoId = userInfoId;
    }


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getSelfExp() {
        return selfExp;
    }

    public void setSelfExp(String selfExp) {
        this.selfExp = selfExp;
    }

    public String getSelfInfo() {
        return selfInfo;
    }

    public void setSelfInfo(String selfInfo) {
        this.selfInfo = selfInfo;
    }

    public List<ResTagBean> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<ResTagBean> labelList) {
        this.labelList = labelList;
    }
}
