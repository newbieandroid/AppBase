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

    /**宝贝管理状态**/
    val SELL = 0// 0上架
    val NOSELL = 1//1下架
    val HOTSELL = 2 //热门

    /**页面请求码**/
    val REQ_EDITBABYINFO = 100//编辑宝贝
    val REQ_SELECTIMGS = 88//选择图片
    val REQ_CAMERA = 87//拍照
    val REQ_IMGPREVIEW = 86//图片预览

    val REQ_EDITNICK = 85//编辑昵称

    /**默认数据**/
    val SERVICETIME = 60//默认服务时间
}