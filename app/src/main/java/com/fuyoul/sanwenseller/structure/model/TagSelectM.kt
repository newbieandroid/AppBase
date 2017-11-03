package com.fuyoul.sanwenseller.structure.model

import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo.GETTAGLIST
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
class TagSelectM : BaseM {


    fun getTagList(listen: HttpReqListener) {
        OkGo.get<ResHttpResult>(GETTAGLIST).execute(listen)
    }


}