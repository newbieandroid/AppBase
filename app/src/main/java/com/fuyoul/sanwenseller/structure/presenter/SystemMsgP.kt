package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResSystemMsg
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.SystemMsgM
import com.fuyoul.sanwenseller.structure.view.SystemMsgV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
class SystemMsgP(v: SystemMsgV) : BaseP<SystemMsgM, SystemMsgV>(v) {
    override fun getModelImpl(): SystemMsgM = SystemMsgM()


    fun getData(isRefresh: Boolean, context: Context) {

        getModelImpl().getSysMsgList(object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {

                val datas = listOf(ResSystemMsg(), ResSystemMsg(), ResSystemMsg(), ResSystemMsg(), ResSystemMsg(), ResSystemMsg())
                viewImpl?.getAdapter()?.setData(true, datas)
                viewImpl?.getAdapter()?.setReqLayoutInfo(isRefresh, true)

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