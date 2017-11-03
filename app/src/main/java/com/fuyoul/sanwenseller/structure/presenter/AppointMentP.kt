package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.others.AppointMentItemBean
import com.fuyoul.sanwenseller.bean.reqhttp.ReqChangeAllDayState
import com.fuyoul.sanwenseller.bean.reqhttp.ReqChangeDayOfItemState
import com.fuyoul.sanwenseller.bean.reshttp.ResAppointMentT
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.Code
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

    /**
     * 设置当前不接单
     * @param whichDay 今天明天或者后天
     * @param state 改变后的状态
     */
    fun changeAllDayState(context: Context, whichDay: Int, state: Int) {

        var dateInfo = getModelImpl().getTodayBean()

        when (whichDay) {
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


        MsgDialogHelper.showNormalDialog(context, true, "当天接单状态变更", "变更为暂不接单后，仍需处理完已预约过的订单，防止用户投诉，影响你的口碑", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {

                val reqData = ReqChangeAllDayState()
                reqData.exclude = "${dateInfo.year}/${if (dateInfo.month > 9) dateInfo.month else "0${dateInfo.month}"}/${if (dateInfo.day > 9) dateInfo.day else "0${dateInfo.day}"}"
                reqData.opt = state //0:全天接单，1：全天不接单
                getModelImpl().changeAllDayState(reqData, object : HttpReqListener(context) {
                    override fun reqOk(result: ResHttpResult) {
                        viewImpl?.setAllDaySatate(state, true)

                        MsgDialogHelper.showStateDialog(context, "修改接单状态成功", true)
                    }

                    override fun withoutData(code: Int, msg: String) {

                        if (code == Code.HTTP_SUCCESS) {

                            viewImpl?.setAllDaySatate(state, true)

                            MsgDialogHelper.showStateDialog(context, "修改接单状态成功", true)
                        } else {

                            MsgDialogHelper.showStateDialog(context, "修改接单状态失败", false)

                        }
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

    /**
     * 设置某个时间节点不接单
     * @param  position 某一个节点的位置
     */
    fun changeItemState(context: Context, whichDay: Int, position: Int) {

        var dateInfo = getModelImpl().getTodayBean()

        when (whichDay) {
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
                val reqData = ReqChangeDayOfItemState()
                reqData.date = "${dateInfo.year}/${if (dateInfo.month > 9) dateInfo.month else "0${dateInfo.month}"}/${if (dateInfo.day > 9) dateInfo.day else "0${dateInfo.day}"}"
                reqData.time = (viewImpl?.getAdapter()?.datas?.get(position) as AppointMentItemBean).time
                getModelImpl().changeItemState(reqData, object : HttpReqListener(context) {
                    override fun reqOk(result: ResHttpResult) {


                        viewImpl?.setItemState(position)

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

    /**获取数据,并且根据显示某一天的数据**/
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

                        viewImpl?.setAllDaySatate(today.todayStatus, false)

                        for (index in 0 until defaultDatas.size) {

                            defaultDatas[index].isBusy = today.todayStatus == Data.BUSY

                            defaultDatas[index].canOrder = true
                            for (i in 0 until today.times.size) {

                                if (defaultDatas[index].time == today.times[i].time) {
                                    defaultDatas[index].canOrder = false
                                    /**判断不可接单的原因是被预约还是预测师手动设置不接单**/
                                    if (today.times[i].orderId == 0L) {
                                        //预测师手动设置不接单
                                        defaultDatas[index].isSelect = false
                                    } else {
                                        //预测师被预约了
                                        defaultDatas[index].isSelect = true
                                        defaultDatas[index].avatar = today.times[i].avatar
                                        defaultDatas[index].orderId = today.times[i].orderId
                                        defaultDatas[index].userInfoId = today.times[i].userInfoId
                                    }


                                }

                            }
                        }


                    }
                    Data.TOMORROW -> {
                        val tomorrow = info.tomorrowStatusDetail

                        viewImpl?.setAllDaySatate(tomorrow.tomorrowStatus, false)


                        for (index in 0 until defaultDatas.size) {

                            defaultDatas[index].isBusy = tomorrow.tomorrowStatus == Data.BUSY
                            defaultDatas[index].canOrder = true
                            for (i in 0 until tomorrow.times.size) {

                                if (defaultDatas[index].time == tomorrow.times[i].time) {
                                    defaultDatas[index].canOrder = false
                                    /**判断不可接单的原因是被预约还是预测师手动设置不接单**/
                                    if (tomorrow.times[i].orderId == 0L) {
                                        //预测师手动设置不接单
                                        defaultDatas[index].isSelect = false
                                    } else {
                                        //预测师被预约了
                                        defaultDatas[index].isSelect = true
                                        defaultDatas[index].avatar = tomorrow.times[i].avatar
                                        defaultDatas[index].orderId = tomorrow.times[i].orderId
                                        defaultDatas[index].userInfoId = tomorrow.times[i].userInfoId
                                    }


                                }

                            }


                        }
                    }
                    Data.AFTERTOMORROW -> {
                        val afterTomorrow = info.afterTomorrowStatusDetail
                        viewImpl?.setAllDaySatate(afterTomorrow.afterTomorrowStatus, false)
                        for (index in 0 until defaultDatas.size) {

                            defaultDatas[index].isBusy = afterTomorrow.afterTomorrowStatus == Data.BUSY
                            defaultDatas[index].canOrder = true
                            for (i in 0 until afterTomorrow.times.size) {

                                if (defaultDatas[index].time == afterTomorrow.times[i].time) {

                                    defaultDatas[index].canOrder = false

                                    /**判断不可接单的原因是被预约还是预测师手动设置不接单**/
                                    if (afterTomorrow.times[i].avatar == null || TextUtils.equals("null", afterTomorrow.times[i].avatar)) {
                                        //预测师手动设置不接单
                                        defaultDatas[index].isSelect = false
                                    } else {
                                        //预测师被预约了
                                        defaultDatas[index].isSelect = true
                                        defaultDatas[index].avatar = afterTomorrow.times[i].avatar
                                        defaultDatas[index].orderId = afterTomorrow.times[i].orderId
                                        defaultDatas[index].userInfoId = afterTomorrow.times[i].userInfoId
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