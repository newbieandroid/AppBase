package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reqhttp.ReqInCome
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResMoneyItem
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.MoneyInComeM
import com.fuyoul.sanwenseller.structure.view.MoneyInComeV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\11\14 0014
 *  @Desc:
 */
class MoneyInComeP(v: MoneyInComeV) : BaseP<MoneyInComeM, MoneyInComeV>(v) {
    override fun getModelImpl(): MoneyInComeM = MoneyInComeM()

    fun getData(context: Context, req: ReqInCome, isRefresh: Boolean) {


        getModelImpl().getData(req, object : HttpReqListener(context, false, true) {
            override fun reqOk(result: ResHttpResult) {

                val data = JSON.parseObject(result.data.toString(), ResMoneyItem::class.java)
                viewImpl?.setViewInfo(data)

                if (data.incomeList.isEmpty()) {
                    withoutData(200, "暂无数据")
                } else {
                    viewImpl?.getAdapter()?.setData(isRefresh, data.incomeList)
                    viewImpl?.getAdapter()?.setReqLayoutInfo(isRefresh, true)
                }

            }

            override fun withoutData(code: Int, msg: String) {
                viewImpl?.getAdapter()?.setReqLayoutInfo(isRefresh, false)
                viewImpl?.getAdapter()?.setRefreshAndLoadMoreEnable(false)

                if (isRefresh) {
                    viewImpl?.getAdapter()?.setEmptyView(R.layout.emptylayout)
                } else {
                    NormalFunUtils.showToast(context, "全部数据加载完成")
                }
            }

            override fun error(errorInfo: String) {
                viewImpl?.getAdapter()?.setReqLayoutInfo(isRefresh, false)
                viewImpl?.getAdapter()?.setRefreshAndLoadMoreEnable(false)
                if (isRefresh) {
                    viewImpl?.getAdapter()?.setEmptyView(R.layout.errorstatelayout)
                }
                NormalFunUtils.showToast(context, errorInfo)
            }

        })

    }
}