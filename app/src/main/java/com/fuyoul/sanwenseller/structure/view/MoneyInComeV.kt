package com.fuyoul.sanwenseller.structure.view

import android.content.Context
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reshttp.ResMoneyItem

/**
 *  @author: chen
 *  @CreatDate: 2017\11\14 0014
 *  @Desc:
 */
abstract class MoneyInComeV : BaseV {


    abstract fun initAdapter(context: Context)

    abstract fun getAdapter(): BaseAdapter

    abstract fun setViewInfo(data: ResMoneyItem)
}