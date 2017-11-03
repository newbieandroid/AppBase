package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResAppointMentInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.AppointMentInfoM
import com.fuyoul.sanwenseller.structure.view.AppointMentInfoV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\11\2 0002
 *  @Desc:
 */
class AppointMentInfoP(v: AppointMentInfoV) : BaseP<AppointMentInfoM, AppointMentInfoV>(v) {
    override fun getModelImpl(): AppointMentInfoM = AppointMentInfoM()


    fun getData(context: Context, orderId: Long) {


        getModelImpl().getData(orderId, object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                viewImpl?.setData(JSON.parseObject(result.data.toString(), ResAppointMentInfo::class.java))
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