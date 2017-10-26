package com.fuyoul.sanwenseller.structure.model

import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class OrderM : BaseM {

    fun getData(index: Int, masterId: Long, status: Int, ilistener: HttpReqListener) {
        OkGo.get<ResHttpResult>(UrlInfo.ORDERLIST)
                .params("masterid", masterId)
                .params("status", status)
                .params("index", index)
                .execute(ilistener)
    }

}