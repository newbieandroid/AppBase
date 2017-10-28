package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
abstract class LoginV : BaseV {

    /**是否可以发送验证码请求**/
    abstract fun isCanSendSms(): Boolean

    /**改变获取验证码的文字**/
    abstract fun changeGetSmsState()

    /**登录信息保存到数据库**/
    abstract fun saveDbInfo(loginInfo: ResLoginInfoBean)

    abstract fun deleteDbInfo()

}