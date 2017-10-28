package com.fuyoul.sanwenseller.structure.model

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reqhttp.ReqEditBaby
import com.fuyoul.sanwenseller.bean.reqhttp.ReqReleaseBaby
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo.BABYEDIT
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
class EditBabyM : BaseM {

    /**编辑商品**/
    fun editBaby(data: ReqEditBaby, listener: HttpReqListener) {
        OkGo.post<ResHttpResult>(BABYEDIT)
                .upJson(JSON.toJSONString(data))
                .execute(listener)

    }

    /**发布商品**/
    fun releaseBaby(data: ReqReleaseBaby, listener: HttpReqListener) {
        OkGo.post<ResHttpResult>(BABYEDIT)
                .upJson(JSON.toJSONString(data))
                .execute(listener)

    }
}