package com.fuyoul.sanwenseller.bean.reshttp;


import com.fuyoul.sanwenseller.bean.MultBaseBean;
import com.fuyoul.sanwenseller.configs.Code;

import java.io.Serializable;

/**
 * @author: chen
 * @CreatDate: 2017\10\27 0027
 * @Desc:
 */

public class ResHttpBabyItem implements MultBaseBean, Serializable {
    /**
     * comentCount : 0
     * price : 141
     * saleCount : 0
     * introduce : 简介
     * serviceTime : 50
     * grade : 4.0
     * disCountPrice : 1
     * goodsId : 1708080808080
     * imgs :
     * goodsClassifyId : 2
     * masterId : 1234567890122
     * goodsName : 缘分探寻
     */

    private int comentCount;
    private int price;
    private int saleCount;
    private String introduce;
    private int serviceTime;
    private double grade;
    private int disCountPrice;
    private long goodsId;
    private String imgs;
    private int goodsClassifyId;
    private long masterId;
    private String goodsName;

    @Override
    public int itemType() {
        return Code.INSTANCE.getVIEWTYPE_BABYMANAGER();
    }


    public int getComentCount() {
        return comentCount;
    }

    public void setComentCount(int comentCount) {
        this.comentCount = comentCount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getDisCountPrice() {
        return disCountPrice;
    }

    public void setDisCountPrice(int disCountPrice) {
        this.disCountPrice = disCountPrice;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public int getGoodsClassifyId() {
        return goodsClassifyId;
    }

    public void setGoodsClassifyId(int goodsClassifyId) {
        this.goodsClassifyId = goodsClassifyId;
    }

    public long getMasterId() {
        return masterId;
    }

    public void setMasterId(long masterId) {
        this.masterId = masterId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
