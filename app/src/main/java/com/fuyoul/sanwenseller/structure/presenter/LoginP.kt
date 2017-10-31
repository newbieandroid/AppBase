package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.csl.share.OpenSdkHelper
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reqhttp.ReqLoginBean
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.helper.ActivityStateHelper
import com.fuyoul.sanwenseller.helper.HttpDialogHelper
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.LoginM
import com.fuyoul.sanwenseller.structure.view.LoginV
import com.fuyoul.sanwenseller.ui.MainActivity
import com.fuyoul.sanwenseller.ui.user.RegistMasterInfoActivity
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.netease.nim.uikit.NimUIKit
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo
import kotlinx.android.synthetic.main.myfragmentlayout.*

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

                imLogin(context, JSON.parseObject(result.data.toString(), ResLoginInfoBean::class.java))
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

    fun imLogin(context: Context, loginInfo: ResLoginInfoBean) {

        when (loginInfo.auditStatus) {
            Code.MASTERSTATE_DEFAULT -> {
                HttpDialogHelper.dismisss()

                RegistMasterInfoActivity.start(context, loginInfo.userInfoId)

            }
            Code.MASTERSTATE_SUCCESS -> {
                NimUIKit.doLogin(LoginInfo("user_${loginInfo.userInfoId}", "${loginInfo.imToken}"), object : RequestCallback<LoginInfo> {
                    override fun onFailed(p0: Int) {
                        HttpDialogHelper.dismisss()
                        NormalFunUtils.showToast(context, "登录失败,错误码：$p0")
                        viewImpl?.deleteDbInfo()

                    }

                    override fun onSuccess(p0: LoginInfo?) {
                        viewImpl?.saveDbInfo(loginInfo)
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
            Code.MASTERSTATE_WAIT -> {
                HttpDialogHelper.dismisss()

                MsgDialogHelper.showSingleDialog(context, true, "温馨提示", "预测师信息正在审核中", "我知道了", object : MsgDialogHelper.DialogListener {
                    override fun onPositive() {
                        ActivityStateHelper.removeAll()
                    }

                    override fun onNagetive() {
                        ActivityStateHelper.removeAll()
                    }

                })

            }
            Code.MASTERSTATE_REFUSE -> {
                HttpDialogHelper.dismisss()

                MsgDialogHelper.showNormalDialog(context, true,
                        "温馨提示",
                        "预测师申请已被驳回,是否致电客服?\n${context.resources.getString(R.string.serviceTell)}",
                        "我知道了",
                        object : MsgDialogHelper.DialogListener {
                            override fun onPositive() {

                                val tellNum = context.resources.getString(R.string.serviceTell)

                                if (!TextUtils.isEmpty(tellNum)) {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tellNum"))
                                    context.startActivity(intent)
                                } else {
                                    NormalFunUtils.showToast(context, "电话号码不正确")
                                }
                            }

                            override fun onNagetive() {
                                ActivityStateHelper.removeAll()
                            }

                        })

            }

        }


    }


}