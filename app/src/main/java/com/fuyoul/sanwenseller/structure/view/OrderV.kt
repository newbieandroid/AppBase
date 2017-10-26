package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reshttp.ResOrderItem

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
abstract class OrderV : BaseV {
    abstract fun setReqLayoutInfo(isRefresh: Boolean, isSuccess: Boolean)
    abstract fun setData(isRefresh: Boolean, datas: List<ResOrderItem>)
    abstract fun setEmptyView(resLayoutId: Int)
    abstract fun setRefreshAndLoadMoreEnable(isEnableRefresh: Boolean, isEnableLoadMore: Boolean)
}