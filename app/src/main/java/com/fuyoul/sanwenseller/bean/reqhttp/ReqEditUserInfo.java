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
    private String self_info;
    private String self_exp;

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

    public String getSelf_info() {
        return self_info;
    }

    public void setSelf_info(String self_info) {
        this.self_info = self_info;
    }

    public String getSelf_exp() {
        return self_exp;
    }

    public void setSelf_exp(String self_exp) {
        this.self_exp = self_exp;
    }
}
