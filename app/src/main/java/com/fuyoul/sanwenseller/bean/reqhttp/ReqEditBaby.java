package com.fuyoul.sanwenseller.bean.reqhttp;

import java.util.List;

/**
 * @author: chen
 * @CreatDate: 2017\10\28 0028
 * @Desc:
 */

public class ReqEditBaby {


    /**
     * goodsId : 1708080808080
     * name : 名称
     * price : 123
     * goodsClassifyId : 1
     * imgs : {"add":["a","b"],"delete":["hjsad/jsad"]}
     * detail : 描述
     */

    private long goodsId;
    private String name;
    private int price;
    private int goodsClassifyId;
    private ImgsBean imgs;
    private String detail;

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getGoodsClassifyId() {
        return goodsClassifyId;
    }

    public void setGoodsClassifyId(int goodsClassifyId) {
        this.goodsClassifyId = goodsClassifyId;
    }

    public ImgsBean getImgs() {
        return imgs;
    }

    public void setImgs(ImgsBean imgs) {
        this.imgs = imgs;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static class ImgsBean {
        private List<String> add;
        private List<String> delete;

        public List<String> getAdd() {
            return add;
        }

        public void setAdd(List<String> add) {
            this.add = add;
        }

        public List<String> getDelete() {
            return delete;
        }

        public void setDelete(List<String> delete) {
            this.delete = delete;
        }
    }
}
