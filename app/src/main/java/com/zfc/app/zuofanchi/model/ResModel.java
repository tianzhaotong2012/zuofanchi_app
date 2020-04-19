package com.zfc.app.zuofanchi.model;

/**
 * Created by tian on 2019/12/3.
 */

public class ResModel {
    String errNo;
    String errMsg;
    PostListModel data;

    public String getErrNo() {
        return errNo;
    }

    public void setErrNo(String errNo) {
        this.errNo = errNo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public PostListModel getData() {
        return data;
    }

    public void setData(PostListModel data) {
        this.data = data;
    }
}
