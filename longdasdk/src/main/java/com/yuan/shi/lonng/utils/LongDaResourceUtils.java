package com.yuan.shi.lonng.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaResourceUtils {
    /**
      * 对于context.getResources().getIdentifier无法获取的数据,或者数组
      * 资源反射值
      * @param context
      * @param name
      * @param type
      * @return
      */
    private static int getResourceId(Context context,String name, String type) {
        int i = -1;
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".R$"+type);
            i = clazz.getField(name).getInt(null);
        } catch (Exception e) {
            LongDaLog.d("没有找到"+  context.getPackageName() +".R$"+type+"类型资源 "+name+"请copy相应文件到对应的目录.");
            return i;
        }
        return i;
    }

    /**
      *context.getResources().getIdentifier无法获取到styleable的数据
      * @param context
      * @param name
      * @return
      */
    public static int getStyleable(Context context, String name) {
        return getResourceId(context, name,"styleable");
    }


    /**
      * 获取layout的ID号
      * @param context
      * @param name
      * @return
      */
    public static int getLayout(Context context, String name) {
        return (int)getResourceId(context, name,"layout");
    }

    /**
      * 获取string的ID号
      * @param context
      * @param name
      * @return
      */
    public static int getString(Context context, String name) {
        return (int)getResourceId(context, name,"string");
    }

    /**
      * 获取drawable的ID号
      * @param context
      * @param name
      * @return
      */
    public static int getDrawable(Context context, String name) {
        return (int)getResourceId(context, name,"drawable");
    }

    /**
      * 获取id的ID号
      * @param context
      * @param name
      * @return
      */
    public static int getId(Context context, String name) {
        return (int)getResourceId(context, name,"id");
    }
}
