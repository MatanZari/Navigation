package com.zari.matan.navigationdrawerexample.Network;

/**
 * Created by Matan on 6/3/2015
 */
public abstract class Callback {

    private String response;
    public abstract void handleResponse(String response);


    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
