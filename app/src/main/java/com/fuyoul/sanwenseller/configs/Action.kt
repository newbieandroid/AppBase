package com.fuyoul.sanwenseller.configs

import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:广播的名称
 */
object Action {

    /**微信登录发送的广播**/
    val ACTION_WXLOGIN = "${OkGo.getInstance().context.packageName}.wxlogin"
}