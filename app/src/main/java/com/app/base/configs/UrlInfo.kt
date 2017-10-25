package com.app.base.configs

import com.app.base.R
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
object UrlInfo {
    private var base = OkGo.getInstance().context.getString(R.string.baseUrl)

    val LOGIN = "$base/account/master/signIn"//登录
}