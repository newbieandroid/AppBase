package com.fuyoul.sanwenseller.listener

import com.fuyoul.sanwenseller.bean.reshttp.ResQiNiuBean

/**
 *  Auther: chen
 *  Creat at: 2017\10\12 0012
 *  Desc:
 */
interface QiNiuUpLoadListener {

    fun complete(path: List<ResQiNiuBean>)

    fun error(error: String)
}