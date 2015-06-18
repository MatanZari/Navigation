package com.zari.matan.navigationdrawerexample.Network;

import android.os.AsyncTask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matan on 6/3/2015
 */
public class Executor {

    public final int corePoolSize = 10;
    public final int maxPoolSize = 10;
    public final long keepAliveTime = 15;
    LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>(corePoolSize);
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime, TimeUnit.SECONDS,linkedBlockingQueue);

    public void execute(MainHttpTask task,Request request){

        if (linkedBlockingQueue.size() == maxPoolSize){
            linkedBlockingQueue.poll();


        }

        task.executeOnExecutor(poolExecutor,request);
    }

    public void execute(AsyncTask<String,Void,String> task,String url){
        if (linkedBlockingQueue.size() == maxPoolSize){
            linkedBlockingQueue.poll();


        }
        task.executeOnExecutor(poolExecutor,url);
    }
}
