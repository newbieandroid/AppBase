package com.fuyoul.sanwenseller.bean.reshttp;

import com.fuyoul.sanwenseller.bean.MultBaseBean;
import com.fuyoul.sanwenseller.configs.Code;

import java.io.Serializable;

/**
 * @author: chen
 * @CreatDate: 2017\10\26 0026
 * @Desc:
 */

public class ResHttpOrderItem implements MultBaseBean, Serializable {
    /**
     * nickname : 哈哈哈
     * introduce : 简介
     * avatar : http://oxon40mnh.bkt.clouddn.com/FihfxM6BoWl2w_HqWFiL3Vyic1Ly
     * orderId : 1710191651171
     * serviceTime : 50
     * goodsId : 1234567890122
     * fillInformation : 0
     * appointmentTime : 2017/10/20 10:00
     * realPrice : 1.0
     * orderDate : 1508403077
     * orderPrice : 1
     * imToken : 1fd76250-170c-4a7a-801c-d6090add292d
     * goodsImgs :
     * price : 2.0
     * status : 1
     * goodsNum : 1
     * userInfoId : 1709281041521
     * goodsClassifyId : 2
     * type : 0
     * goodsName : 缘分探寻
     * orderCode : SW1508403077407000001
     */

    private int refundStatus;//退款结果
    private String nickname;
    private String introduce;
    private String avatar;
    private long orderId;
    private int serviceTime;
    private long goodsId;
    private int fillInformation;
    private String appointmentTime;
    private double realPrice;
    private long orderDate;
    private int orderPrice;
    private String imToken;
    private String goodsImgs;
    private double price;
    private int status;
    private int goodsNum;
    private long userInfoId;
    private int goodsClassifyId;
    private int type;
    private String goodsName;
    private String orderCode;

    @Override
    public int itemType() {
        return Code.INSTANCE.getVIEWTYPE_ORDER();
    }


    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public int getFillInformation() {
        return fillInformation;
    }

    public void setFillInformation(int fillInformation) {
        this.fillInformation = fillInformation;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getGoodsImgs() {
        return goodsImgs;
    }

    public void setGoodsImgs(String goodsImgs) {
        this.goodsImgs = goodsImgs;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public int getGoodsClassifyId() {
        return goodsClassifyId;
    }

    public void setGoodsClassifyId(int goodsClassifyId) {
        this.goodsClassifyId = goodsClassifyId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
