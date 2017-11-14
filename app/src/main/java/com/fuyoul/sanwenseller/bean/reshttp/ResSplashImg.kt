package com.fuyoul.sanwenseller.bean.reshttp

import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.configs.Code

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
class ResSplashImg : MultBaseBean {
    override fun itemType(): Int = Code.VIEWTYPE_SPLASH


    var img = ""
}