package com.fuyoul.sanwenseller.bean.reshttp

import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.configs.Code

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
class ResSystemMsg : MultBaseBean {
    override fun itemType(): Int = Code.VIEWTYPE_SYSMSG

    var time: Long = 0
    var content = ""
}