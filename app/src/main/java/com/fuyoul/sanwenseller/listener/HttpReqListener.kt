package com.fuyoul.sanwenseller.listener

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.Code.HTTP_ERROR
import com.fuyoul.sanwenseller.configs.Code.HTTP_SUCCESS
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


    constructor(context: Context, isCancleable: Boolean) : this(context, true, isCancleable)

    constructor(context: Context) : this(context, true)


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
        Log.e("csl", "请求信息:${response?.rawResponse?.request()?.url()}\n接口请求信息:${JSON.toJSONString(response?.body())}\n状态：${response?.exception}")
    }

    override fun onCacheSuccess(response: Response<ResHttpResult>?) {
        super.onCacheSuccess(response)
        checkData(false, response)

        Log.e("csl", "缓存信息:${response?.rawResponse?.request()?.url()}\n接口缓存信息:${JSON.toJSONString(response?.body())}\n状态：${response?.exception}")

    }

    override fun onError(response: Response<ResHttpResult>?) {
        super.onError(response)
        checkData(true, response)
        Log.e("csl", "请求地址:${response?.rawResponse?.request()?.url()}\n异常:${JSON.toJSONString(response?.body())}\n状态：${response?.exception}")

    }

    override fun convertResponse(response: okhttp3.Response?): ResHttpResult = JSON.parseObject(response?.body()?.string(), ResHttpResult::class.java)

    /**请求成功并且正常返回数据**/
    abstract fun reqOk(result: ResHttpResult)

    /**请求成功但是没有数据**/
    abstract fun withoutData(code: Int, msg: String)

    /**请求失败(包括没有请求成功和服务端异常)**/
    abstract fun error(errorInfo: String)

    /**检查数据**/
    private fun checkData(isError: Boolean, response: Response<ResHttpResult>?) {


        if (isError) {
            error("和服务器通信失败")
        } else {
            val result = response?.body()

            Log.e("csl", "-------接口返回数据------:${result?.data.toString()}---")

            if (result?.errorCode == Code.HTTP_NODATA || TextUtils.isEmpty(result?.data.toString()) || TextUtils.equals("[]", result?.data.toString()) || TextUtils.equals("{}", result?.data.toString())) {
                withoutData(result?.errorCode ?: HTTP_SUCCESS, "${result?.msg}")
            } else if (result?.errorCode == HTTP_ERROR) {
                error("${result.msg}")
            } else if (result?.errorCode == HTTP_SUCCESS) {

                reqOk(result)

            } else {
                error("${result?.msg}")
            }

        }


    }
}