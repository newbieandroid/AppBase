package com.fuyoul.sanwenseller.bean.reshttp;

import com.fuyoul.sanwenseller.bean.MultBaseBean;
import com.fuyoul.sanwenseller.configs.Code;

import java.util.List;

/**
 * @author: chen
 * @CreatDate: 2017\10\31 0031
 * @Desc:
 */

public class ResMoneyItem {
    /**
     * totalPrice : 209
     * incomeList : [{"totalPrice":11,"introduce":"扭扭捏捏呢","nickname":"不好解决就","ordersDate":1509607654,"avatar":"http://oxon40mnh.bkt.clouddn.com/1509528047782_IMG_20171012_142547.jpg","ordersId":1711021527341,"NAME":"宝贝","IMGS":"[{\"url\":\"http://oxon40mnh.bkt.clouddn.com/1509593481481_JPEG_20171012_142212.jpg\"}]"},{"totalPrice":11,"introduce":"扭扭捏捏呢","nickname":"不好解决就","ordersDate":1509607691,"avatar":"http://oxon40mnh.bkt.clouddn.com/1509528047782_IMG_20171012_142547.jpg","ordersId":1711021528111,"NAME":"宝贝","IMGS":"[{\"url\":\"http://oxon40mnh.bkt.clouddn.com/1509593481481_JPEG_20171012_142212.jpg\"}]"},{"totalPrice":11,"introduce":"扭扭捏捏呢","nickname":"不好解决就","ordersDate":1509607788,"avatar":"http://oxon40mnh.bkt.clouddn.com/1509528047782_IMG_20171012_142547.jpg","ordersId":1711021529481,"NAME":"宝贝","IMGS":"[{\"url\":\"http://oxon40mnh.bkt.clouddn.com/1509593481481_JPEG_20171012_142212.jpg\"}]"},{"totalPrice":77,"introduce":"吧扭扭捏捏","nickname":"不好解决就","ordersDate":1509609339,"avatar":"http://oxon40mnh.bkt.clouddn.com/1509528047782_IMG_20171012_142547.jpg","ordersId":1711021555391,"NAME":"股海护航","IMGS":"[{\"url\":\"http://oxon40mnh.bkt.clouddn.com/1509594213789_JPEG_20171012_141205.jpg\"}]"},{"totalPrice":11,"introduce":"扭扭捏捏呢","nickname":"不好解决就","ordersDate":1509611075,"avatar":"http://oxon40mnh.bkt.clouddn.com/1509528047782_IMG_20171012_142547.jpg","ordersId":1711021624351,"NAME":"宝贝","IMGS":"[{\"url\":\"http://oxon40mnh.bkt.clouddn.com/1509593481481_JPEG_20171012_142212.jpg\"}]"},{"totalPrice":11,"introduce":"扭扭捏捏呢","nickname":"不好解决就","ordersDate":1509611318,"avatar":"http://oxon40mnh.bkt.clouddn.com/1509528047782_IMG_20171012_142547.jpg","ordersId":1711021628381,"NAME":"宝贝","IMGS":"[{\"url\":\"http://oxon40mnh.bkt.clouddn.com/1509593481481_JPEG_20171012_142212.jpg\"}]"},{"totalPrice":77,"introduce":"吧扭扭捏捏","nickname":"不好解决就","ordersDate":1509696275,"avatar":"http://oxon40mnh.bkt.clouddn.com/1509528047782_IMG_20171012_142547.jpg","ordersId":1711031604351,"NAME":"股海护航","IMGS":"[{\"url\":\"http://oxon40mnh.bkt.clouddn.com/1509594213789_JPEG_20171012_141205.jpg\"}]"}]
     * notSccountsCount : 7
     * totalCount : 7
     * notSccountsPrice : 209
     */

    private int totalPrice;
    private int notSccountsCount;
    private int totalCount;
    private int notSccountsPrice;
    private List<IncomeListBean> incomeList;


    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNotSccountsCount() {
        return notSccountsCount;
    }

    public void setNotSccountsCount(int notSccountsCount) {
        this.notSccountsCount = notSccountsCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getNotSccountsPrice() {
        return notSccountsPrice;
    }

    public void setNotSccountsPrice(int notSccountsPrice) {
        this.notSccountsPrice = notSccountsPrice;
    }

    public List<IncomeListBean> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<IncomeListBean> incomeList) {
        this.incomeList = incomeList;
    }


    public static class IncomeListBean implements MultBaseBean {
        /**
         * totalPrice : 11
         * introduce : 扭扭捏捏呢
         * nickname : 不好解决就
         * ordersDate : 1509607654
         * avatar : http://oxon40mnh.bkt.clouddn.com/1509528047782_IMG_20171012_142547.jpg
         * ordersId : 1711021527341
         * NAME : 宝贝
         * IMGS : [{"url":"http://oxon40mnh.bkt.clouddn.com/1509593481481_JPEG_20171012_142212.jpg"}]
         * user_info_id:1710281442061,
         * type:0,
         */

        private long user_info_id;
        private int type;
        private int totalPrice;
        private String introduce;
        private String nickname;
        private long ordersDate;
        private String avatar;
        private long ordersId;
        private String NAME;
        private String IMGS;

        public long getUser_info_id() {
            return user_info_id;
        }

        public void setUser_info_id(long user_info_id) {
            this.user_info_id = user_info_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public long getOrdersDate() {
            return ordersDate;
        }

        public void setOrdersDate(long ordersDate) {
            this.ordersDate = ordersDate;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public long getOrdersId() {
            return ordersId;
        }

        public void setOrdersId(long ordersId) {
            this.ordersId = ordersId;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getIMGS() {
            return IMGS;
        }

        public void setIMGS(String IMGS) {
            this.IMGS = IMGS;
        }

        @Override
        public int itemType() {
            return Code.INSTANCE.getVIEWTYPE_MONEY();
        }
    }
}
