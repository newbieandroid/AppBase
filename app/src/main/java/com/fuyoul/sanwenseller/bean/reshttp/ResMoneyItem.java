package com.fuyoul.sanwenseller.bean.reshttp;

import com.fuyoul.sanwenseller.bean.MultBaseBean;
import com.fuyoul.sanwenseller.configs.Code;

/**
 * @author: chen
 * @CreatDate: 2017\10\31 0031
 * @Desc:
 */

public class ResMoneyItem implements MultBaseBean {
    @Override
    public int itemType() {
        return Code.INSTANCE.getVIEWTYPE_MONEY();
    }
}
