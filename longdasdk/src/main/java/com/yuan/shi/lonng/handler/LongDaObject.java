package com.yuan.shi.lonng.handler;


/**
 * Created by @author luyon
 *
 * @version 2.0  2018/5/11
 */
public class LongDaObject {
    public String wxAppId;
    public String qqAppId;
    public String currentChannel;
    public int wxErrCode;
    public int qpayErrCode;
    public String sdkType;
    public String qpayScheme;
    public boolean isShowDialog;

    private LongDaObject() {
        this.wxAppId = null;
        this.qqAppId = null;
        this.currentChannel = null;
        this.wxErrCode = -10;
        this.qpayErrCode = -10;
        this.qpayScheme = null;
        this.sdkType = null;
        this.isShowDialog = false;
    }

    public static LongDaObject getInstance() {
        return PpObject.instance;
    }

    private static class PpObject {
        private static final LongDaObject instance = new LongDaObject();
    }
}
