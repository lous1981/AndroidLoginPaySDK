package com.yuan.shi.lonng.dao;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaPayItemData {
    private String payType;
    private String payName;
    private boolean isSelected;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
