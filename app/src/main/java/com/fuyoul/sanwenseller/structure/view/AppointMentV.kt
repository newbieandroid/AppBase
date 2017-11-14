package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.ui.order.fragment.NormalTestItemFragment

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
abstract class AppointMentV : BaseV {

    abstract fun getAdapter(): NormalTestItemFragment.ThisAdapter


    /**
     * @param state 修改大师整天接单状态
     * @param isRefreshData 是否刷新数据
     */
    abstract fun setAllDaySatate(state: Int, isRefreshData: Boolean)


    /**
     * 改变大师某一个时间点的接单状态
     */
    abstract fun setItemState(position: Int)
}