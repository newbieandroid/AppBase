package com.fuyoul.sanwenseller.base

import com.csl.refresh.SmartRefreshLayout


/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
abstract class BaseRefreshFragmen<out M : BaseM, V : BaseV, out P : BaseP<M, V>> : BaseFragment<M, V, P>() {


    abstract fun getRefreshLayout(): SmartRefreshLayout


    fun onReqFinish(isRefresh: Boolean, isSuccess: Boolean) {

        if (isRefresh) {
            getRefreshLayout().finishRefresh(isSuccess)
        } else {
            getRefreshLayout().finishLoadmore(isSuccess)
        }

    }

}