package com.app.base.bean.reshttp;

import org.litepal.crud.DataSupport;

/**
 * @author: chen
 * @CreatDate: 2017\10\25 0025
 * @Desc:
 */

public class ResLoginInfoBean extends DataSupport {


    /**
     * auditStatus : -1
     * userInfoId : 1709271640361
     * nickname : ??6290
     * avatar : http://img3.imgtn.bdimg.com/it/u=1388120186,3336399575&fm=27&gp=0.jpg
     * gender : -1
     * token : 72KlhUaBtWv0aBffGVLQzuy029whtuD0YWn+KYbHBuUUzyc7U6CFiQmrpyGMHDMHApItgEPZzAPZ3SWD0tV05Al7J8LPeyEbu/+d4m9Y7B0sV7GAJ88YAmEGVnx/wc5f
     * imToken : 39b48198-49b8-4473-85da-bffca6eb46f5
     */

    private int auditStatus;
    private long userInfoId;
    private String nickname;
    private String avatar;
    private int gender;
    private String token;
    private String imToken;

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(long userInfoId) {
        this.userInfoId = userInfoId;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }
}
