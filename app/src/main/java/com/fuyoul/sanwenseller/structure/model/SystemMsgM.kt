package com.fuyoul.sanwenseller.structure.model

import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.listener.HttpReqListener

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
class SystemMsgM : BaseM {

    fun getSysMsgList(listener: HttpReqListener) {
        listener.reqOk(ResHttpResult())
    }
}