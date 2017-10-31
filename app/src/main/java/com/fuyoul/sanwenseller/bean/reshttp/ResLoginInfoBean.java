package com.fuyoul.sanwenseller.bean.reshttp;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author: chen
 * @CreatDate: 2017\10\25 0025
 * @Desc:
 */

public class ResLoginInfoBean extends DataSupport {


    /**
     * masterLable : [{"labelId":1,"outerBorderColor":"#44449999","label":"商家安居客","labelColor":"#55446622","innerBorderColor":"#22334455"},{"labelId":1,"outerBorderColor":"#44449999","label":"商家安居客","labelColor":"#55446622","innerBorderColor":"#22334455"},{"labelId":1,"outerBorderColor":"#44449999","label":"商家安居客","labelColor":"#55446622","innerBorderColor":"#22334455"}]
     * imToken : 81834a66-1629-4a8f-8ed9-3d87bdf7f32f
     * token : +dGL+ZAl9BvCaKaJG8L/yfN/5KzTKm3p2kyLI5snlyYK0GsxPHyUO757eX1lrKWzipmGzLqnGf+HyMIuDB52S5nMmPV6W8DoDCiILgfW8sazi2EtFyEc/WQHKYXORv+i
     * nickname : 匿名热门
     * avatar : http://oxon40mnh.bkt.clouddn.com/FgG8KOY721_vmWY4Q6qiyHq6d7n_
     * userInfoId : 1234567890122
     * selfExp : 个人经历4
     * selfInfo : 自我介绍2
     * gender : -1
     * auditStatus : 1
     */

    private String provinces;
    private String city;
    private String imToken;
    private String token;
    private String nickname;
    private String avatar;
    private long userInfoId;
    private String selfExp;
    private String selfInfo;
    private int gender;
    private int auditStatus;
    private List<MasterLableBean> masterLable;


    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(long userInfoId) {
        this.userInfoId = userInfoId;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public List<MasterLableBean> getMasterLable() {
        return masterLable;
    }

    public void setMasterLable(List<MasterLableBean> masterLable) {
        this.masterLable = masterLable;
    }

    public static class MasterLableBean {
        /**
         * labelId : 1
         * outerBorderColor : #44449999
         * label : 商家安居客
         * labelColor : #55446622
         * innerBorderColor : #22334455
         */

        private int labelId;
        private String outerBorderColor;
        private String label;
        private String labelColor;
        private String innerBorderColor;

        public int getLabelId() {
            return labelId;
        }

        public void setLabelId(int labelId) {
            this.labelId = labelId;
        }

        public String getOuterBorderColor() {
            return outerBorderColor;
        }

        public void setOuterBorderColor(String outerBorderColor) {
            this.outerBorderColor = outerBorderColor;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabelColor() {
            return labelColor;
        }

        public void setLabelColor(String labelColor) {
            this.labelColor = labelColor;
        }

        public String getInnerBorderColor() {
            return innerBorderColor;
        }

        public void setInnerBorderColor(String innerBorderColor) {
            this.innerBorderColor = innerBorderColor;
        }
    }
}
