package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reshttp.ResQuickTestCount

/**
 *  @author: chen
 *  @CreatDate: 2017\11\4 0004
 *  @Desc:
 */
abstract class QuickTestV : BaseV {

    abstract fun setViewInfo(data: ResQuickTestCount)
}
