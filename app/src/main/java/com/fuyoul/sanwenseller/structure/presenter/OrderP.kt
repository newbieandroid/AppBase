package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpOrderItem
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.enuminfo.OrderType
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.OrderM
import com.fuyoul.sanwenseller.structure.view.OrderV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class OrderP(orderV: OrderV) : BaseP<OrderM, OrderV>(orderV) {
    override fun getModelImpl(): OrderM = OrderM()

    fun getOrderData(context: Context, isShowDialog: Boolean, isRefresh: Boolean,
                     index: Int, status: Int) {

        getModelImpl().getData(index, status, object : HttpReqListener(context, isShowDialog, true) {
            override fun reqOk(result: ResHttpResult) {

                viewImpl?.getBaseAdapter()?.setData(isRefresh, JSON.parseArray(result.data.toString(), ResHttpOrderItem::class.java))
                viewImpl?.getBaseAdapter()?.setReqLayoutInfo(isRefresh, true)
            }

            override fun withoutData(code: Int, msg: String) {
                viewImpl?.getBaseAdapter()?.setReqLayoutInfo(isRefresh, false)
                viewImpl?.getBaseAdapter()?.setRefreshAndLoadMoreEnable(false)

                if (isRefresh) {
                    viewImpl?.getBaseAdapter()?.setEmptyView(R.layout.emptylayout)
                } else {
                    NormalFunUtils.showToast(context, "全部数据加载完成")
                }
            }

            override fun error(errorInfo: String) {
                viewImpl?.getBaseAdapter()?.setReqLayoutInfo(isRefresh, false)
                viewImpl?.getBaseAdapter()?.setRefreshAndLoadMoreEnable(false)
                if (isRefresh) {
                    viewImpl?.getBaseAdapter()?.setEmptyView(R.layout.errorstatelayout)
                }
                NormalFunUtils.showToast(context, errorInfo)
            }

        })
    }


    fun confirmOrder(context: Context, item: ResHttpOrderItem) {

        MsgDialogHelper.showNormalDialog(context, true, "是否确认完成该订单?", "", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {
                getModelImpl().confirmOrder(item.orderId, object : HttpReqListener(context) {
                    override fun reqOk(result: ResHttpResult) {

                        MsgDialogHelper.showStateDialog(context, "确认订单成功", true)
                        item.status = OrderType.PREDICTED.orderType
                        viewImpl?.changeOrderState(item)
                    }

                    override fun withoutData(code: Int, msg: String) {
                        NormalFunUtils.showToast(context, msg)
                    }

                    override fun error(errorInfo: String) {

                        MsgDialogHelper.showStateDialog(context, "确认订单失败", true)
                    }

                })
            }

            override fun onNagetive() {
            }

        })
    }

    fun orderRefund(context: Context, item: ResHttpOrderItem, isAgree: Boolean) {

        MsgDialogHelper.showNormalDialog(context, true, "是否${if (isAgree) "同意" else "拒绝"}退款?", "", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {


                getModelImpl().orderRefund(item.orderId, isAgree, object : HttpReqListener(context) {
                    override fun reqOk(result: ResHttpResult) {
                        MsgDialogHelper.showStateDialog(context, "操作成功", true)
                        item.status = if (isAgree) OrderType.INREFUND.orderType else OrderType.REFUNDREFUSE.orderType

                        viewImpl?.changeOrderState(item)
                    }

                    override fun withoutData(code: Int, msg: String) {
                        NormalFunUtils.showToast(context, msg)
                    }

                    override fun error(errorInfo: String) {
                        MsgDialogHelper.showStateDialog(context, "操作失败", true)
                    }
                })


            }

            override fun onNagetive() {
            }

        })
    }

    fun deleteOrder(context: Context, item: ResHttpOrderItem) {

        MsgDialogHelper.showNormalDialog(context, true, "是否删除该订单?", "", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {

                getModelImpl().deleteOrder(item.orderId, object : HttpReqListener(context) {

                    override fun reqOk(result: ResHttpResult) {

                        MsgDialogHelper.showStateDialog(context, "删除订单成功", true)

                        viewImpl?.deleteItem(item)


                    }

                    override fun withoutData(code: Int, msg: String) {
                        NormalFunUtils.showToast(context, msg)
                    }

                    override fun error(errorInfo: String) {

                        MsgDialogHelper.showStateDialog(context, "删除订单失败", true)
                    }

                })
            }

            override fun onNagetive() {
            }

        })
    }
}