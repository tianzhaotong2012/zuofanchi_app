package com.zfc.app.zuofanchi.model;

/**
 * Created by tian on 2019/12/14.
 */

public class ResDetailModel {
    String errNo;
    String errMsg;
    DetailModel data;

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

    public DetailModel getData() {
        return data;
    }

    public void setData(DetailModel data) {
        this.data = data;
    }
}
