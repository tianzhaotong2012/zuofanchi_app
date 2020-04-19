package com.zfc.app.zuofanchi.model;

/**
 * Created by tian on 2019/12/3.
 */

public class PostItem {
    String postId;
    String cover;
    String postTitle;
    TargetModel target;
    String shareUrl;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public TargetModel getTarget() {
        return target;
    }

    public void setTarget(TargetModel target) {
        this.target = target;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
