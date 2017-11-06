package com.fuyoul.sanwenseller.configs


/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
object Data {


    /**性别**/
    val MAN = 0
    val WOMAN = 1

    /**预测师状态**/
    val FREE = 0//可预约
    val BUSY = 1//不预约


    /**宝贝类别**/
    val NORMALBABY = 0//普通宝贝类型
    val QUICKTESTBABY = 1//闪测
    val PERSONALBABY = 2//自测


    /**今天明天后天**/
    val TODAY = 123
    val TOMORROW = 124
    val AFTERTOMORROW = 125

    /**退款结果**/
    val REFUNDTOUSER = 2//客服判给买家
    val REFUNDTOSELLER = 3//判给卖家
    val REFUND = 4//商家同意退款
}