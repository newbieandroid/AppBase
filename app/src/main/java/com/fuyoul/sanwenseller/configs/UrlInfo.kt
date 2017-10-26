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

    /**登录相关**/
    val LOGIN = "$base/account/master/signIn"//登录
    val SMS = "$base/account/sendVerifyCode?phone="//获取验证码

    /**订单相关**/
    val ORDERLIST = "$base/order/master/orderList"//获取订单列表
}