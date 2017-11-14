package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpOrderItem
import com.fuyoul.sanwenseller.ui.main.main.OrderItemFragment

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
abstract class OrderV : BaseV {

    abstract fun getBaseAdapter(): OrderItemFragment.ThisAdapter

    abstract fun changeOrderState(item: ResHttpOrderItem)

    abstract fun deleteItem(item: ResHttpOrderItem)
}