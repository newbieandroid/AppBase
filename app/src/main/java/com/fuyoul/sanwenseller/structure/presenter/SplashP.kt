package com.fuyoul.sanwenseller.structure.presenter

import android.app.Activity
import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResSplash
import com.fuyoul.sanwenseller.configs.Key
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.SplashM
import com.fuyoul.sanwenseller.structure.view.SplashV
import com.fuyoul.sanwenseller.utils.SpUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
class SplashP(v: SplashV) : BaseP<SplashM, SplashV>(v) {
    override fun getModelImpl(): SplashM = SplashM()


    fun getData(context: Context) {

        getModelImpl().getData(object : HttpReqListener(context, false, false) {
            override fun reqOk(result: ResHttpResult) {

                val data = JSON.parseObject(result.data.toString(), ResSplash::class.java)

                if (SpUtils.getBoolean(Key.isFirst)) {
                    viewImpl?.setImgs(data.firstImgs)
                } else {
                    viewImpl?.setImgs(listOf(data.secondImg))
                }


                viewImpl?.setContactInfo(data.imInfo)
            }

            override fun withoutData(code: Int, msg: String) {
                viewImpl?.gotoMain()
            }

            override fun error(errorInfo: String) {
                viewImpl?.gotoMain()
            }

        })
    }
}