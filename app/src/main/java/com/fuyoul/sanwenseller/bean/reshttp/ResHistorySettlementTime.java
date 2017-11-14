package com.fuyoul.sanwenseller.bean.reshttp;

import com.fuyoul.sanwenseller.bean.MultBaseBean;
import com.fuyoul.sanwenseller.configs.Code;

/**
 * @author: chen
 * @CreatDate: 2017\11\14 0014
 * @Desc:
 */

public class ResHistorySettlementTime implements MultBaseBean {
    @Override
    public int itemType() {
        return Code.INSTANCE.getVIEWTYPE_SETTLEMENT();
    }

    private String ordersDate;

    private String year;
    private String month;

    private int showType = Code.INSTANCE.getBOTHSHOW();

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getOrdersDate() {
        return ordersDate;
    }

    public void setOrdersDate(String ordersDate) {
        this.ordersDate = ordersDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
