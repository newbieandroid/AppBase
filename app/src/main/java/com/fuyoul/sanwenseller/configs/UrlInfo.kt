package com.fuyoul.sanwenseller.configs

import com.fuyoul.sanwenseller.R
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
object UrlInfo {
    private var base = OkGo.getInstance().context.getString(R.string.baseUrl)

    val LOGIN = "${base}/account/master/signIn"//登录
}