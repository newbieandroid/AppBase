package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResOrderItem
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
                     index: Int, masterId: Long, status: Int) {

        getModelImpl().getData(index, masterId, status, object : HttpReqListener(context, isShowDialog, true) {
            override fun reqOk(result: ResHttpResult) {
                viewImpl?.setData(isRefresh, JSON.parseArray(result.data.toString(), ResOrderItem::class.java))
                viewImpl?.setReqLayoutInfo(isRefresh, true)
            }

            override fun withoutData(msg: String) {
                viewImpl?.setReqLayoutInfo(isRefresh, false)
                viewImpl?.setRefreshAndLoadMoreEnable(isRefresh, false)
                viewImpl?.setEmptyView(R.layout.emptylayout)

                if (isRefresh) {
//                    viewImpl?.setEmptyView(R.layout.emptylayout)
                } else {
                    NormalFunUtils.showToast(context, "全部数据加载完成")
                }
            }

            override fun error(errorInfo: String) {
                viewImpl?.setReqLayoutInfo(isRefresh, false)
                viewImpl?.setRefreshAndLoadMoreEnable(isRefresh, false)
                if (isRefresh) {
                    viewImpl?.setEmptyView(R.layout.emptylayout)
                }
                NormalFunUtils.showToast(context, errorInfo)
            }

        })
    }


}