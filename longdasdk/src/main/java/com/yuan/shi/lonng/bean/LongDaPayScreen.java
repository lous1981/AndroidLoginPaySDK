package com.yuan.shi.lonng.bean;

import android.graphics.Color;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public enum LongDaPayScreen {
    PORTRAIT("portrait", 1),  // 竖屏
    LANDSCAPE("landscrpe", 2),  //横屏
    SENSOR_LANDSCAPE("sensor", 3); // 横屏自动切换

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private LongDaPayScreen(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (LongDaPayScreen c : LongDaPayScreen.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    /**
     * 根据名称获取序号
     * @param name 名称
     * @return 序号
     */
    public static int getIndex(String name) {
        for (LongDaPayScreen c : LongDaPayScreen.values()) {
            if (c.getName().equals(name)) {
                return c.getIndex();
            }
        }
        return -1;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
