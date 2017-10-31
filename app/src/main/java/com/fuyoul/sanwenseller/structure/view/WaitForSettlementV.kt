package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.ui.order.WaitForSettlementActivity

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
abstract class WaitForSettlementV : BaseV {

    abstract fun getAdatper(): WaitForSettlementActivity.ThisAdapter

    abstract fun  initRecycleView()
}