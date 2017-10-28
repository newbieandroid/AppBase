package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpBabyItem
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.BabyManagerM
import com.fuyoul.sanwenseller.structure.view.BabyManagerV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
class BabyManagerP(babyManagerV: BabyManagerV) : BaseP<BabyManagerM, BabyManagerV>(babyManagerV) {
    override fun getModelImpl(): BabyManagerM = BabyManagerM()

    fun getData(context: Context, isRefresh: Boolean, index: Int, masterId: Long, status: Int) {
        getModelImpl().getData(index, masterId, status, object : HttpReqListener(context, isRefresh, true) {
            override fun reqOk(result: ResHttpResult) {

                viewImpl?.getBaseAdapter()?.setData(isRefresh, JSON.parseArray(result.data.toString(), ResHttpBabyItem::class.java))
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

    fun deleteBaby(context: Context, position: Int) {

        val item = viewImpl?.getBaseAdapter()?.datas?.get(position) as ResHttpBabyItem

        getModelImpl().deleteBbay(context, item.goodsId, object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                viewImpl?.getBaseAdapter()?.remove(position)
            }

            override fun withoutData(code: Int, msg: String) {

                if (code == Code.HTTP_SUCCESS) {
                    viewImpl?.getBaseAdapter()?.remove(position)
                }
                NormalFunUtils.showToast(context, msg)
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(context, errorInfo)
            }

        })
    }

    fun upToShop(context: Context, position: Int) {

        val item = viewImpl?.getBaseAdapter()?.datas?.get(position) as ResHttpBabyItem
        getModelImpl().upToShop(context, item.goodsId, object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                viewImpl?.getBaseAdapter()?.remove(position)
            }

            override fun withoutData(code: Int, msg: String) {
                if (code == Code.HTTP_SUCCESS) {
                    viewImpl?.getBaseAdapter()?.remove(position)
                }
                NormalFunUtils.showToast(context, msg)
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(context, errorInfo)
            }

        })

    }

    fun downToShop(context: Context, position: Int) {
        val item = viewImpl?.getBaseAdapter()?.datas?.get(position) as ResHttpBabyItem
        getModelImpl().downToShop(context, item.goodsId, object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                viewImpl?.getBaseAdapter()?.remove(position)
            }

            override fun withoutData(code: Int, msg: String) {
                if (code == Code.HTTP_SUCCESS) {
                    viewImpl?.getBaseAdapter()?.remove(position)
                }
                NormalFunUtils.showToast(context, msg)
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(context, errorInfo)
            }

        })
    }
}