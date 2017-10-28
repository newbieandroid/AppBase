package com.fuyoul.sanwenseller.bean.reshttp;

import org.litepal.crud.DataSupport;

/**
 * @author: chen
 * @CreatDate: 2017\10\25 0025
 * @Desc:
 */

public class ResLoginInfoBean extends DataSupport {

    /**
     * imToken : 3a31c533-3f29-4cd8-8ff4-74f3d02aa831
     * token : 93SC6f2GuMUKRu82U+BGII8wlckkY+j4S3Nykz1YC3e5ze7pbMbwq7R0aCOWkLbuo4PHbqMA7/puINRizYmz8qcwNsp5HHtzKqNQaC8sDayQeQ+JRF8Uj1/J3D89g/fx
     * nickname : 1822**
     * userInfoId : 1710251508401
     * gender : 0
     * auditStatus : -1
     * avatar
     */

    private String avatar;
    private String imToken;
    private String token;
    private String nickname;
    private long userInfoId;
    private int gender;
    private int auditStatus;
    private String provinces;
    private String city;
    private String self_exp;
    private String self_info;


    public String getSelf_exp() {
        return self_exp;
    }

    public void setSelf_exp(String self_exp) {
        this.self_exp = self_exp;
    }

    public String getSelf_info() {
        return self_info;
    }

    public void setSelf_info(String self_info) {
        this.self_info = self_info;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(long userInfoId) {
        this.userInfoId = userInfoId;
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
}
