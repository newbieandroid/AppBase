package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.ui.fragment.appointment.NormalTestItemFragment

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
abstract class AppointMentV : BaseV {


    abstract fun getAdapter(): NormalTestItemFragment.ThisAdapter


}