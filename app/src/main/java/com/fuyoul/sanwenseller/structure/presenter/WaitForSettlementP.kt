package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResSettlementItem
import com.fuyoul.sanwenseller.structure.model.WaitForSettlementM
import com.fuyoul.sanwenseller.structure.view.WaitForSettlementV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
class WaitForSettlementP(v: WaitForSettlementV) : BaseP<WaitForSettlementM, WaitForSettlementV>(v) {
    override fun getModelImpl(): WaitForSettlementM = WaitForSettlementM()


    fun getData(isRefresh: Boolean, context: Context) {

        viewImpl?.getAdatper()?.setData(isRefresh, arrayListOf(ResSettlementItem(), ResSettlementItem(), ResSettlementItem(), ResSettlementItem(), ResSettlementItem(), ResSettlementItem(), ResSettlementItem(), ResSettlementItem(), ResSettlementItem(), ResSettlementItem()))

        //TODO 获取数据
//        getModelImpl().getData(object : HttpReqListener(context, false, false) {
//            override fun reqOk(result: ResHttpResult) {
//
//                viewImpl?.getAdatper()?.setData(isRefresh, JSON.parseArray(result.data.toString(), ResSettlementItem::class.java))
//                viewImpl?.getAdatper()?.setReqLayoutInfo(isRefresh, true)
//
//            }
//
//            override fun withoutData(code: Int, msg: String) {
//                viewImpl?.getAdatper()?.setReqLayoutInfo(isRefresh, false)
//                viewImpl?.getAdatper()?.setRefreshAndLoadMoreEnable(false)
//
//                if (isRefresh) {
//                    viewImpl?.getAdatper()?.setEmptyView(R.layout.emptylayout)
//                } else {
//                    NormalFunUtils.showToast(context, "全部数据加载完成")
//                }
//            }
//
//            override fun error(errorInfo: String) {
//
//                viewImpl?.getAdatper()?.setReqLayoutInfo(isRefresh, false)
//                viewImpl?.getAdatper()?.setRefreshAndLoadMoreEnable(false)
//                if (isRefresh) {
//                    viewImpl?.getAdatper()?.setEmptyView(R.layout.errorstatelayout)
//                }
//                NormalFunUtils.showToast(context, errorInfo)
//            }
//
//        })


    }
}