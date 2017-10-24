package com.app.base.structure.presenter

import com.app.base.listener.LoginBackListener
import com.app.base.base.BaseP
import com.app.base.structure.model.LoginM
import com.app.base.structure.view.LoginV

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class LoginP(loginV: LoginV) : BaseP<LoginM, LoginV>(loginV) {
    override fun getModelImpl(): LoginM = LoginM()

    /**登录**/
    fun doLogin(listener: LoginBackListener) {
        viewImpl?.showDialog()
    }

    /**获取验证码**/
    fun getSms() {
        getModelImpl().getLoginData().account
        viewImpl?.showDialog()
    }

}