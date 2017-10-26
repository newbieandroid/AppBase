package com.fuyoul.sanwenseller.bean.reshttp

import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.configs.Code.VIEWTYPE_ORDER

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class ResOrderItem : MultBaseBean {
    override fun itemType(): Int = VIEWTYPE_ORDER
}