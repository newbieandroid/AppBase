package com.fuyoul.sanwenseller.structure.model

import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo.APPOIINTMENTINFO
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\11\2 0002
 *  @Desc:
 */
class AppointMentInfoM : BaseM {

    fun getData(ordersId: Long, listener: HttpReqListener) {
        OkGo.post<ResHttpResult>(APPOIINTMENTINFO).upJson("{\"ordersId\":$ordersId}").execute(listener)
    }

}