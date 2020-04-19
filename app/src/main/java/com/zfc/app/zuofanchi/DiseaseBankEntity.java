package com.zfc.app.zuofanchi;

/**
 * Created by tian on 2019/12/2.
 */

public class DiseaseBankEntity {
    private String id;
    private String title;
    private String imgUrl;
    private String shareUrl;
    //使用快捷键alt+insert获取get set方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}