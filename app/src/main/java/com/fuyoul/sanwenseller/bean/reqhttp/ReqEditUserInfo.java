package com.fuyoul.sanwenseller.bean.reqhttp;

/**
 * @author: chen
 * @CreatDate: 2017\10\28 0028
 * @Desc:
 */

public class ReqEditUserInfo {


    /**
     * avatar : 头像
     * nickname : 昵称
     * gender : 0
     * provinces : 所在省
     * city : 所在市
     * self_info : 个人简历
     * self_exp : 个人经历
     */

    private String avatar;
    private String nickname;
    private int gender;
    private String provinces;
    private String city;
    private String selfInfo;
    private String selfExp;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

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

    public String getSelfInfo() {
        return selfInfo;
    }

    public void setSelfInfo(String selfInfo) {
        this.selfInfo = selfInfo;
    }

    public String getSelfExp() {
        return selfExp;
    }

    public void setSelfExp(String selfExp) {
        this.selfExp = selfExp;
    }
}
