package com.fuyoul.sanwenseller.structure.model

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reqhttp.ReqLoginBean
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo.LOGIN
import com.fuyoul.sanwenseller.configs.UrlInfo.SMS
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.lzy.okgo.OkGo

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class LoginM : BaseM {

    /**普通登录**/
    fun getLoginData(context: Context, data: ReqLoginBean, listener: HttpReqListener) {


        if (TextUtils.isEmpty(data.thirdPartyId)) {


            if (TextUtils.isEmpty(data.phone)) {

                NormalFunUtils.showToast(context, "请输入手机号")
                return
            }

            if (TextUtils.isEmpty(data.verifyCode)) {
                NormalFunUtils.showToast(context, "请输入验证码")
                return
            }
        }



        OkGo.post<ResHttpResult>(LOGIN)
                .upJson(JSON.toJSONString(data))
                .execute(listener)

    }


    /**获取验证码**/
    fun getSmsCode(context: Context, phone: String, listener: HttpReqListener) {

        if (TextUtils.isEmpty(phone)) {
            NormalFunUtils.showToast(context, "请输入手机号")
            return
        }

        OkGo.get<ResHttpResult>("$SMS$phone").execute(listener)

    }


}