package com.zari.matan.navigationdrawerexample.Network;

/**
 * Created by Matan on 6/3/2015
 */
public abstract class Error {

    public static final int ERR_NO_INTERNET = -1;
    public static final int ERR_EX = -2;
    public static final int ERR_DATA = -3;
    public static final int ERR_OOM = -4;
    public static final int ERR_LOGIN = -5;
    public static final int ERR_NOT_JSON = -6;
    public static final int ERR_UNKNOWN = -7;

    private int errorReason;
    public abstract void handleError();


    public int getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(int errorReason) {
        this.errorReason = errorReason;
    }
}
