package com.fuyoul.sanwenseller.listener

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.helper.HttpDialogHelper
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request

/**
 *  Auther: chen
 *  Creat at: 2017\10\18 0018
 *  Desc:网络请求监听
 */
abstract class HttpReqListener(context: Context, isShowDialog: Boolean, isCancleable: Boolean) : AbsCallback<ResHttpResult>() {

    private var context: Context? = null
    private var isShowDialog: Boolean = true
    private var isCancleable: Boolean = true

    init {
        this.context = context
        this.isCancleable = isCancleable
        this.isShowDialog = isShowDialog
    }


    override fun onStart(request: Request<ResHttpResult, out Request<Any, Request<*, *>>>?) {
        super.onStart(request)
        HttpDialogHelper.showDialog(context!!, isShowDialog, isCancleable)
    }

    /**如果网络请求后还有别的操作需要显示对话框可以覆盖这个方法**/
    override fun onFinish() {
        super.onFinish()
        HttpDialogHelper.dismisss()
    }

    override fun onSuccess(response: Response<ResHttpResult>?) {
        checkData(false, response)
    }

    override fun onCacheSuccess(response: Response<ResHttpResult>?) {
        super.onCacheSuccess(response)
        checkData(false, response)
    }

    override fun onError(response: Response<ResHttpResult>?) {
        super.onError(response)
        checkData(true, response)
    }

    override fun convertResponse(response: okhttp3.Response?): ResHttpResult = JSON.parseObject(response?.body()?.string(), ResHttpResult::class.java)

    /**请求成功并且正常返回数据**/
    abstract fun reqOk(result: ResHttpResult)

    /**请求成功但是没有数据**/
    abstract fun withoutData()

    /**请求失败(包括没有请求成功和服务端异常)**/
    abstract fun withoutError(errorInfo: String)

    /**检查数据**/
    private fun checkData(isError: Boolean, response: Response<ResHttpResult>?) {

        if (isError) {
            Log.e("csl", "网络请求失败：${response?.exception}")
            withoutError("和服务器通信异常")
        } else {
            val result = response?.body()
            if (result?.errorCode == Code.HTTP_NODATA || TextUtils.isEmpty(result?.data)) {
                withoutData()
            } else if (result?.errorCode == Code.HTTP_ERROR) {
                withoutError("${result?.msg}")
            } else if (result?.errorCode == Code.HTTP_SUCCESS) {
                reqOk(result)
            }

        }


    }
}