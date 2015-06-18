package com.zari.matan.navigationdrawerexample.Network;

/**
 * Created by Matan on 6/3/2015
 */
public class Request {

    public final int POST = 1;
    public final int PUT = 1;
    public final int DELETE = 1;
    public final int GET = 1;

    public int METHOD = GET;

    public String urlStr;

    public Callback callback;
    Error error;

    String responseStr;



}
