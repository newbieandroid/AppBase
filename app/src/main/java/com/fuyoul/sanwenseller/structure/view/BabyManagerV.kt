package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpBabyItem
import com.fuyoul.sanwenseller.ui.main.main.BabyManagerItemFragment

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
abstract class BabyManagerV : BaseV {
    abstract fun getBaseAdapter(): BabyManagerItemFragment.ThisAdapter

    abstract fun editBaby(item: ResHttpBabyItem)
}