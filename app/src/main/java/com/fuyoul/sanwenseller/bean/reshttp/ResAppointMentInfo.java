package com.fuyoul.sanwenseller.bean.reshttp;

/**
 * @author: chen
 * @CreatDate: 2017\11\2 0002
 * @Desc:
 */

public class ResAppointMentInfo {


    /**
     * ordersId : 1711021628381
     * status : 1
     * avatar : http://oxon40mnh.bkt.clouddn.com/FgG8KOY721_vmWY4Q6qiyHq6d7n_
     * nickname : 1822**
     * date : 2017/11/04
     * hour : 16
     * username : 请问
     * sex : 1
     * birthPlace : 内蒙古自治区兴安盟其他
     * birthday : 1965/12/16
     * name : 宝贝
     * introduce : 扭扭捏捏呢
     * imgs : [{"url":"http://oxon40mnh.bkt.clouddn.com/1509593481481_JPEG_20171012_142212.jpg"}]
     * orderCode : SW1509611318509000001
     * ordersDate : 1509611318
     * serviceTime : 50
     * totalPrice : 11
     * totalPayPrice : 11
     */

    private long userInfoId;
    private long ordersId;
    private int status;
    private String avatar;
    private String nickname;
    private String date;
    private int hour;
    private String username;
    private int sex = -1;
    private String birthPlace;
    private String birthday;
    private String name;
    private String introduce;
    private String imgs;
    private String orderCode;
    private long ordersDate;
    private int serviceTime;
    private int totalPrice;
    private int totalPayPrice;


    public long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public long getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(long ordersId) {
        this.ordersId = ordersId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public long getOrdersDate() {
        return ordersDate;
    }

    public void setOrdersDate(long ordersDate) {
        this.ordersDate = ordersDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }


    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalPayPrice() {
        return totalPayPrice;
    }

    public void setTotalPayPrice(int totalPayPrice) {
        this.totalPayPrice = totalPayPrice;
    }
}
