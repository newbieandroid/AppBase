package com.fuyoul.sanwenseller.bean.reshttp;

import com.fuyoul.sanwenseller.bean.MultBaseBean;
import com.fuyoul.sanwenseller.configs.Code;

import java.io.Serializable;

/**
 * @author: chen
 * @CreatDate: 2017\10\31 0031
 * @Desc:
 */

public class ResTagBean implements MultBaseBean, Serializable {

    /**
     * masterLabelId : 2
     * label : 绱井鏂楁暟
     * labelColor : d1dee8
     * innerBorderColor : d1dee8
     * outerBorderColor : d1dee8
     */

    private long labelId;
    private String label;
    private String labelColor;
    private String innerBorderColor;
    private String outerBorderColor;


    public long getLabelId() {
        return labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public String getInnerBorderColor() {
        return innerBorderColor;
    }

    public void setInnerBorderColor(String innerBorderColor) {
        this.innerBorderColor = innerBorderColor;
    }

    public String getOuterBorderColor() {
        return outerBorderColor;
    }

    public void setOuterBorderColor(String outerBorderColor) {
        this.outerBorderColor = outerBorderColor;
    }

    @Override
    public int itemType() {
        return Code.INSTANCE.getVIEWTYPE_TAG();
    }
}
