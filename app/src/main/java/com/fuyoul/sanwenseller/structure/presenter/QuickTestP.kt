package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reqhttp.ReqUpDateCountInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResQuickTestCount
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.QuickTestM
import com.fuyoul.sanwenseller.structure.view.QuickTestV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\11\4 0004
 *  @Desc:
 */
class QuickTestP(v: QuickTestV) : BaseP<QuickTestM, QuickTestV>(v) {
    override fun getModelImpl(): QuickTestM = QuickTestM()


    fun getCountInfo(context: Context) {

        getModelImpl().getCountInfo(object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                viewImpl?.setViewInfo(JSON.parseObject(result.data.toString(), ResQuickTestCount::class.java))
            }

            override fun withoutData(code: Int, msg: String) {
                NormalFunUtils.showToast(context, msg)
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(context, errorInfo)
            }

        })
    }


    fun upDateCountInfo(context: Context, count: Int, isChanged: Boolean) {

        if (isChanged) {
            MsgDialogHelper.showSingleDialog(context, true, "温馨提示", "闪测接单数一天只能设置一次", "我知道了", null)
            return
        }

        val reqData = ReqUpDateCountInfo()
        reqData.maxOrdersCount = count
        getModelImpl().upDateCountInfo(reqData, object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                NormalFunUtils.showToast(context, result.msg.toString())

                viewImpl?.setIsChangeState(true)

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