package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResTagBean
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.TagSelectM
import com.fuyoul.sanwenseller.structure.view.TagSelectV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
class TagSelectP(v: TagSelectV) : BaseP<TagSelectM, TagSelectV>(v) {
    override fun getModelImpl(): TagSelectM = TagSelectM()


    fun getTagList(context: Context) {
        getModelImpl().getTagList(object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                viewImpl?.setTag(JSON.parseArray(result.data.toString(), ResTagBean::class.java))
            }

            override fun withoutData(code: Int, msg: String) {
                NormalFunUtils.showToast(context, msg)
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(context, errorInfo)
            }

        })
    }

}