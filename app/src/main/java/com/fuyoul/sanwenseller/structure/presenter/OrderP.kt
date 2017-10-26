package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpOrderItem
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
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

                viewImpl?.getBaseAdapter()?.setData(isRefresh, JSON.parseArray(result.data.toString(), ResHttpOrderItem::class.java))
                viewImpl?.getBaseAdapter()?.setReqLayoutInfo(isRefresh, true)
            }

            override fun withoutData(msg: String) {
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


}