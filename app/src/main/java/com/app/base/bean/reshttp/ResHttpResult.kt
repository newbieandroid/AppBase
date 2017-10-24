package com.app.base.bean.reshttp

import java.io.Serializable

/**
 *  Auther: chen
 *  Creat at: 2017\10\18 0018
 *  Desc:
 */
class ResHttpResult : Serializable {

    var errorCode: Int = 0
    var msg: String? = null
    var data: String? = null

}