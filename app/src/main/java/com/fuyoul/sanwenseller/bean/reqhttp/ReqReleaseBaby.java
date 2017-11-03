package com.fuyoul.sanwenseller.bean.reqhttp;

/**
 * @author: chen
 * @CreatDate: 2017\10\28 0028
 * @Desc:
 */

public class ReqReleaseBaby {

    private String goodsName;
    private int category;
    private int price;
    private int serviceTime = 50;
    private String introduce;
    private String imgs;


    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
