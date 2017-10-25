package com.fuyoul.sanwenseller.structure.presenter

import com.fuyoul.sanwenseller.listener.LoginBackListener
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.structure.model.LoginM
import com.fuyoul.sanwenseller.structure.view.LoginV

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