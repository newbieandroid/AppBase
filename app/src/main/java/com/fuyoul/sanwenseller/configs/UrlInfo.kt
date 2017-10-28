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


    /**商品相关**/
    val BABYLIST = "$base/goods/master/getMasterGoodsList"//获取预测师的宝贝礼拜
    val BABYDELETE = "$base/goods/deleteGoods"//删除商品
    val BABYDOWNTOSHOP = "$base/goods/master/soldOut"//商品下架
    val BABYUPTOSHOP = "$base/goods/master/goodsPutaway"//商品上架
    val BABYRELEASE = "$base/goods/master/releaseGoods"//发布商品
    val BABYEDIT = "$base/goods/editGoods"//编辑商品

    /**用户信息相关**/
    val EDITUSERINFO = "$base/account/user/editUserInfo"//更新个人资料


    /**七牛**/
    val GETIMGTOKEN = "$base/qiniu/getQiniuUploadToken"//获取七牛的token
}