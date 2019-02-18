package com.yuan.shi.lonng.activity;

import android.app.Activity;
import android.content.Intent;

import com.yuan.shi.lonng.constant.LongDaConstant;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaAppManager {
    private List<Activity> mActivityList = new LinkedList<>();
    private static LongDaAppManager instance;

    private LongDaAppManager(){}
    /**
     * 单一实例
     */
    public static LongDaAppManager getAppManager(){
        if(instance == null){
            instance = new LongDaAppManager();
        }
        return instance;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity != null){
            mActivityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        while(mActivityList.size() > 0) {
            Activity activity = mActivityList.get(mActivityList.size() - 1);
            mActivityList.remove(mActivityList.size() - 1);
            activity.finish();
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) { }
    }
}
