package com.fuyoul.sanwenseller.bean.reshttp;

import com.fuyoul.sanwenseller.bean.reqhttp.ResDefaultImInfo;

import java.util.List;

/**
 * @author: chen
 * @CreatDate: 2017\11\9 0009
 * @Desc:
 */

public class ResSplash {


    /**
     * firstImgs :
     * secondImg :
     * imInfo : [{"neckName":"w","password":"1","description":"d","avatar":"s","account":"d","sysConfigId":123,"type":1},{"neckName":"d","password":"2","description":"d","avatar":"f","account":"f","sysConfigId":234,"type":1}]
     */

    private List<ImageBean> firstImgs;
    private ImageBean secondImg;
    private List<ResDefaultImInfo> imInfo;

    public List<ImageBean> getFirstImgs() {
        return firstImgs;
    }

    public void setFirstImgs(List<ImageBean> firstImgs) {
        this.firstImgs = firstImgs;
    }

    public ImageBean getSecondImg() {
        return secondImg;
    }

    public void setSecondImg(ImageBean secondImg) {
        this.secondImg = secondImg;
    }

    public List<ResDefaultImInfo> getImInfo() {
        return imInfo;
    }

    public void setImInfo(List<ResDefaultImInfo> imInfo) {
        this.imInfo = imInfo;
    }


    public static class ImageBean {


        /**
         * id : 2
         * jumpAddress : www.baidu.com
         * imgs : http://xxx.com
         * note : 备注0
         * type : 1
         */

        private int id;
        private String jumpAddress;
        private String imgs;
        private String note;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJumpAddress() {
            return jumpAddress;
        }

        public void setJumpAddress(String jumpAddress) {
            this.jumpAddress = jumpAddress;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
