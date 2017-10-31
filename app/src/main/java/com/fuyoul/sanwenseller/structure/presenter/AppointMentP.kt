package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.others.AppointMentItemBean
import com.fuyoul.sanwenseller.bean.reshttp.ResAppointMentT
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.Data
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.AppointMentM
import com.fuyoul.sanwenseller.structure.view.AppointMentV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class AppointMentP(appointMentV: AppointMentV) : BaseP<AppointMentM, AppointMentV>(appointMentV) {
    override fun getModelImpl(): AppointMentM = AppointMentM()

    fun changeAllDayState(context: Context, type: Int) {

        var dateInfo = getModelImpl().getTodayBean()

        when (type) {
            Data.TODAY -> {
                dateInfo = getModelImpl().getTodayBean()
            }
            Data.TOMORROW -> {
                dateInfo = getModelImpl().getTomorrowBean()
            }
            Data.AFTERTOMORROW -> {
                dateInfo = getModelImpl().getAfterTomorrowBean()
            }
        }

        MsgDialogHelper.showNormalDialog(context, true, "接单状态变更", "变更为暂不接单后，仍需处理完已预约过的订单，防止用户投诉，影响你的口碑", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {

                getModelImpl().changeAllDayState(object : HttpReqListener(context) {
                    override fun reqOk(result: ResHttpResult) {

                        MsgDialogHelper.showStateDialog(context, "修改接单状态成功", true)
                    }

                    override fun withoutData(code: Int, msg: String) {
                        MsgDialogHelper.showStateDialog(context, "修改接单状态失败", false)
                    }

                    override fun error(errorInfo: String) {
                        MsgDialogHelper.showStateDialog(context, "修改接单状态失败", false)
                    }

                })

            }

            override fun onNagetive() {
            }

        })
    }

    fun changeItemState(context: Context, type: Int) {


        var dateInfo = getModelImpl().getTodayBean()

        when (type) {
            Data.TODAY -> {
                dateInfo = getModelImpl().getTodayBean()
            }
            Data.TOMORROW -> {
                dateInfo = getModelImpl().getTomorrowBean()
            }
            Data.AFTERTOMORROW -> {
                dateInfo = getModelImpl().getAfterTomorrowBean()
            }
        }

        MsgDialogHelper.showNormalDialog(context, true, "接单状态变更", "变更为暂不接单后，仍需处理完已预约过的订单，防止用户投诉，影响你的口碑", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {

                getModelImpl().changeItemState(object : HttpReqListener(context) {
                    override fun reqOk(result: ResHttpResult) {

                        MsgDialogHelper.showStateDialog(context, "修改接单状态成功", true)
                    }

                    override fun withoutData(code: Int, msg: String) {
                        MsgDialogHelper.showStateDialog(context, "修改接单状态失败", false)
                    }

                    override fun error(errorInfo: String) {
                        MsgDialogHelper.showStateDialog(context, "修改接单状态失败", false)
                    }

                })

            }

            override fun onNagetive() {
            }

        })

    }

    fun getData(context: Context, type: Int) {

        getModelImpl().getData(object : HttpReqListener(context, false, false) {
            override fun reqOk(result: ResHttpResult) {

                //  1.设置默认数据
                val defaultDatas = getModelImpl().getDefaultListData()


                //2.根据获取到的数据修改默认数据
                val info = JSON.parseObject(result.data.toString(), ResAppointMentT::class.java)

                when (type) {
                    Data.TODAY -> {
                        val today = info.todayStatusDetail

                        if (today.todayStatus == Data.BUSY) {
                            (0 until defaultDatas.size).forEach { defaultDatas[it].isBusy = true }
                        } else {

                            for (index in 0 until defaultDatas.size) {
                                for (i in 0 until today.times.size) {

                                    if (defaultDatas[index].time == today.times[i].time) {
                                        defaultDatas[index].isSelect = true
                                        defaultDatas[index].avatar = today.times[i].avatar
                                        defaultDatas[index].userInfoId = today.times[i].userInfoId
                                    }

                                }

                            }
                        }


                    }
                    Data.TOMORROW -> {
                        val tomorrow = info.tomorrowStatusDetail
                        if (tomorrow.tomorrowStatus == Data.BUSY) {
                            (0 until defaultDatas.size).forEach { defaultDatas[it].isBusy = true }
                        } else {


                            for (index in 0 until defaultDatas.size) {
                                for (i in 0 until tomorrow.times.size) {

                                    if (defaultDatas[index].time == tomorrow.times[i].time) {
                                        defaultDatas[index].isSelect = true
                                        defaultDatas[index].avatar = tomorrow.times[i].avatar
                                        defaultDatas[index].userInfoId = tomorrow.times[i].userInfoId
                                    }

                                }

                            }

                        }
                    }
                    Data.AFTERTOMORROW -> {
                        val aftertomorrow = info.afterTomorrowStatusDetail
                        if (aftertomorrow.afterTomorrowStatus == Data.BUSY) {
                            (0 until defaultDatas.size).forEach { defaultDatas[it].isBusy = true }
                        } else {
                            for (index in 0 until defaultDatas.size) {
                                for (i in 0 until aftertomorrow.times.size) {

                                    if (defaultDatas[index].time == aftertomorrow.times[i].time) {
                                        defaultDatas[index].isSelect = true
                                        defaultDatas[index].avatar = aftertomorrow.times[i].avatar
                                        defaultDatas[index].userInfoId = aftertomorrow.times[i].userInfoId
                                    }

                                }

                            }
                        }

                    }
                }


                //3.显示数据
                viewImpl?.getAdapter()?.setData(true, defaultDatas)

                viewImpl?.getAdapter()?.setReqLayoutInfo(true, true)
            }

            override fun withoutData(code: Int, msg: String) {

                viewImpl?.getAdapter()?.setReqLayoutInfo(true, false)
                viewImpl?.getAdapter()?.setRefreshAndLoadMoreEnable(false)

                NormalFunUtils.showToast(context, msg)
            }

            override fun error(errorInfo: String) {

                viewImpl?.getAdapter()?.setReqLayoutInfo(true, false)
                viewImpl?.getAdapter()?.setRefreshAndLoadMoreEnable(false)

                NormalFunUtils.showToast(context, errorInfo)
            }

        })


    }


}