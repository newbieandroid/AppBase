package com.fuyoul.sanwenseller.bean.reshttp;

import com.fuyoul.sanwenseller.bean.MultBaseBean;
import com.fuyoul.sanwenseller.configs.Code;

/**
 * @author: chen
 * @CreatDate: 2017\11\6 0006
 * @Desc:
 */

public class ResActivityListInfo implements MultBaseBean {

    private long time = System.currentTimeMillis();

    private String imgPath = "http://f.hiphotos.baidu.com/image/h%3D300/sign=460e12d768d9f2d33f1122ef99ed8a53/3bf33a87e950352afba6083e5943fbf2b3118bc4.jpg";

    private String title = "活动标题";
    private String content = "活动大概内容";


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int itemType() {
        return Code.INSTANCE.getVIEWTYPE_ACTIVITYLISTINFO();
    }
}
