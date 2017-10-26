package com.fuyoul.sanwenseller.bean;

/**
 * @author: chen
 * @CreatDate: 2017\10\26 0026
 * @Desc:
 */

public class CircleProgress {

    private float percentage = 0;    //进度百分比
    private float current;   //当前半径
    private float radius;  //最大半径
    private int colorIndex;   //颜色下标

    public CircleProgress(float radius, int colorIndex) {
        this.current = percentage * radius;
        this.radius = radius;
        this.colorIndex = colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public float getCurrent() {
        return current;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        this.current = percentage * radius;
    }
}
