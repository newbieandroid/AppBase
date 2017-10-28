package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import android.content.Intent
import com.alibaba.fastjson.JSON
import com.csl.share.OpenSdkHelper
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reqhttp.ReqLoginBean
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.helper.HttpDialogHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.LoginM
import com.fuyoul.sanwenseller.structure.view.LoginV
import com.fuyoul.sanwenseller.ui.MainActivity
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.netease.nim.uikit.NimUIKit
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class LoginP(loginV: LoginV) : BaseP<LoginM, LoginV>(loginV) {
    override fun getModelImpl(): LoginM = LoginM()

    /**登录**/
    fun doLogin(context: Context, thirdPartyId: String?, phone: String?, code: String?) {

        val req = ReqLoginBean()
        req.verifyCode = code ?: ""
        req.phone = phone ?: ""
        req.thirdPartyId = thirdPartyId ?: ""
        getModelImpl().getLoginData(context, req, object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {

                val loginInfo = JSON.parseObject(result.data.toString(), ResLoginInfoBean::class.java)
                viewImpl?.saveDbInfo(loginInfo)
                imLogin(context, LoginInfo("user_${loginInfo.userInfoId}", loginInfo.imToken))
            }

            override fun withoutData(code: Int, msg: String) {
                NormalFunUtils.showToast(context, msg)
                HttpDialogHelper.dismisss()
                viewImpl?.deleteDbInfo()
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(context, errorInfo)
                HttpDialogHelper.dismisss()
                viewImpl?.deleteDbInfo()
            }

            override fun onFinish() {
            }

        })
    }

    /**获取验证码**/
    fun getSms(context: Context, phone: String) {

        if (viewImpl?.isCanSendSms() == false) {
            return
        }

        getModelImpl().getSmsCode(context, phone, object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                NormalFunUtils.showToast(context, "${result.msg}")
                viewImpl?.changeGetSmsState()
            }

            override fun withoutData(code: Int, msg: String) {
                NormalFunUtils.showToast(context, msg)

            }

            override fun error(errorInfo: String) {

                NormalFunUtils.showToast(context, errorInfo)
            }

        })
    }


    fun wxLogin(context: Context) {
        OpenSdkHelper.wxLogin(context)
    }

    fun imLogin(context: Context, loginInfo: LoginInfo) {
        NimUIKit.doLogin(loginInfo, object : RequestCallback<LoginInfo> {
            override fun onFailed(p0: Int) {
                HttpDialogHelper.dismisss()
                NormalFunUtils.showToast(context, "登录失败,错误码：$p0")
                viewImpl?.deleteDbInfo()

            }

            override fun onSuccess(p0: LoginInfo?) {
                HttpDialogHelper.dismisss()
                NimUIKit.setAccount(p0?.account)
                context.startActivity(Intent(context, MainActivity::class.java))
            }

            override fun onException(p0: Throwable?) {
                HttpDialogHelper.dismisss()
                NormalFunUtils.showToast(context, "登录失败")
                viewImpl?.deleteDbInfo()
            }

        })
    }


}