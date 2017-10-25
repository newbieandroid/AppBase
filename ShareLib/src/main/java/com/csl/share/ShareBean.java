package com.csl.share;

/**
 * @author: chen
 * @CreatDate: 2017\10\25 0025
 * @Desc:
 */

public class ShareBean {

    private String shareTitle;
    private String shareContent;
    private String imgPath;
    private String url;


    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
