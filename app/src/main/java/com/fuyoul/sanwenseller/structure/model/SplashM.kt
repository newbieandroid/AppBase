package com.fuyoul.sanwenseller.structure.model

import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
class SplashM : BaseM {

    fun getData(listener: HttpReqListener) {
        OkGo.post<ResHttpResult>(UrlInfo.GETCONFIG).execute(listener)

    }
}