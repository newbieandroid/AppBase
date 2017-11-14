package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.ui.order.SystemMsgListActivity

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
abstract class SystemMsgV : BaseV {

    abstract fun getAdapter(): SystemMsgListActivity.ThisAdapter
}