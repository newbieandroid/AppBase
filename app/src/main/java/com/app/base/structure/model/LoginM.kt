package com.app.base.structure.model

import com.app.base.bean.reqhttp.LoginBean
import com.app.base.base.BaseM

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class LoginM : BaseM {

    fun getLoginData(): LoginBean = LoginBean()
}