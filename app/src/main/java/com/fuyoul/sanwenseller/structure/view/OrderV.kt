package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.ui.fragment.main.OrderItemFragment

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
abstract class OrderV : BaseV {

    abstract fun getBaseAdapter(): OrderItemFragment.ThisAdapter
}