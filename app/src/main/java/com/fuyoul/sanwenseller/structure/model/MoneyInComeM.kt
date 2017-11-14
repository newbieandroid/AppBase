package com.fuyoul.sanwenseller.structure.model

import android.util.Log
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reqhttp.ReqInCome
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo.INCOME
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\11\14 0014
 *  @Desc:
 */
class MoneyInComeM : BaseM {

    fun getData(req: ReqInCome, listener: HttpReqListener) {

        Log.e("csl", "${JSON.toJSONString(req)}")

        OkGo.post<ResHttpResult>(INCOME)
                .upJson(JSON.toJSONString(req)).execute(listener)
    }

}