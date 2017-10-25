package com.fuyoul.sanwenseller.base

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
open abstract class BaseP<out M : BaseM, V : BaseV>(view: V) {

    var viewImpl: V? = null

    init {
        this.viewImpl = view
    }


    abstract fun getModelImpl(): M

}