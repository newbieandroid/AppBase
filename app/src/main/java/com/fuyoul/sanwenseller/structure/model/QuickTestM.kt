package com.fuyoul.sanwenseller.structure.model

import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reqhttp.ReqUpDateCountInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo.FINDFLASHORDERCOUNT
import com.fuyoul.sanwenseller.configs.UrlInfo.UPDATEFLASHORDERCOUNT
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\11\4 0004
 *  @Desc:
 */
class QuickTestM : BaseM {


    fun getCountInfo(listener: HttpReqListener) {
        OkGo.post<ResHttpResult>(FINDFLASHORDERCOUNT).execute(listener)
    }

    fun upDateCountInfo(reqData: ReqUpDateCountInfo, listener: HttpReqListener) {
        OkGo.post<ResHttpResult>(UPDATEFLASHORDERCOUNT)
                .upJson(JSON.toJSONString(reqData)).execute(listener)
    }
}