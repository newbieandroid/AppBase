package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reshttp.ResTagBean

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
abstract class TagSelectV : BaseV {
    abstract fun setTag(datas: List<ResTagBean>)

    abstract fun getAdapter(): BaseAdapter
}