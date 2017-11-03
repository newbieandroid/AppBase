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
    val GETTAGLIST = "$base/account/master/labelList"//获取标签
    val SUBMITMASTERINFO = "$base/account/master/apply"//提交预测师申请资料


    /**订单相关**/
    val ORDERLIST = "$base/order/master/orderList"//获取订单列表
    val APPOINTMENTLIST = "$base/masterSchedulingHandler/getMasterSchedulingHandler"//获取大师盘班表
    val CHANGEAPPOINTMENT = "$base/masterSchedulingHandler/updateMasterScheduling"//更改大师排班表
    val CHANGEITEMAPPOINTMENT = "$base/masterSchedulingHandler/modifyAppointmentStatus"//修改某天某个时间点的接单状态
    val APPOIINTMENTINFO = "$base/masterSchedulingHandler/getAppointInfo"//获取预约信息

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