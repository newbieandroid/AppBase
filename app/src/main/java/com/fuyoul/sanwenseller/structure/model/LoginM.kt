package com.fuyoul.sanwenseller.structure.model

import com.fuyoul.sanwenseller.bean.reqhttp.LoginBean
import com.fuyoul.sanwenseller.base.BaseM

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class LoginM : BaseM {

    fun getLoginData(): LoginBean = LoginBean()
}