package com.app.base.helper

import com.app.base.bean.reshttp.ResHttpResult

/**
 *  Auther: chen
 *  Creat at: 2017\10\18 0018
 *  Desc:网络请求监听
 */
object HttpReqHelper {

    /**请求成功并且正常返回数据**/
    fun reqOk(result: ResHttpResult) {

    }

    /**请求成功但是没有数据**/
    fun withoutData(isRefresh: Boolean) {

    }

    /**请求失败(包括没有请求成功和服务端异常)**/
    fun withoutError(isRefresh: Boolean) {

    }

}