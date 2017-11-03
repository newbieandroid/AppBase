package com.fuyoul.sanwenseller.configs

/**
 *  @author: chen
 *  @CreatDate: 2017\10\24 0024
 *  @Desc:
 */
object Code {


    /**网络请求**/
    val HTTP_NODATA = 100//没有数据
    val HTTP_SUCCESS = 0//请求成功
    val HTTP_ERROR = 404// 请求失败

    /**adapter多布局的viewType**/
    val VIEWTYPE_ORDER = 0//订单
    val VIEWTYPE_EMPTY = 1//空页面
    val VIEWTYPE_BABYMANAGER = 2//宝贝管理列表
    val VIEWTYPE_MULTDIALOG = 3//仿苹果底部对话框
    val VIEWTYPE_MONEY = 4//收入
    val VIEWTYPE_APPOINTMENT = 5 //预约
    val VIEWTYPE_WAITSETTLEMENT = 6 //待结算
    val VIEWTYPE_TAG = 7 //标签

    /**宝贝管理状态**/
    val SELL = 0// 0上架
    val NOSELL = 1//1下架
    val HOTSELL = 2 //热门

    /**页面请求码**/
    val REQ_EDITBABYINFO = 100//编辑宝贝
    val REQ_SELECTIMGS = 88//默认选择图片
    val REQ_CAMERA = 87//默认拍照
    val REQ_IMGPREVIEW = 86//图片预览
    val REQ_USERINFO = 85//图片预览

    /**选择身份照正面**/
    val REQ_CAMERA_IDCARD_Z = 89
    val REQ_SELECTIMGS_IDCARD_Z = 90

    /**选择身份照反面**/
    val REQ_CAMERA_IDCARD_F = 91
    val REQ_SELECTIMGS_IDCARD_F = 92


    /**编辑昵称**/
    val REQ_EDITNICK = 85//编辑昵称

    /**默认数据**/
    val SERVICETIME = 60//默认服务时间


    /**预测师状态**/
    val MASTERSTATE_DEFAULT = -1//第一次登录
    val MASTERSTATE_WAIT = 0//审核中
    val MASTERSTATE_SUCCESS = 1//已通过
    val MASTERSTATE_REFUSE = 2//未通过

}