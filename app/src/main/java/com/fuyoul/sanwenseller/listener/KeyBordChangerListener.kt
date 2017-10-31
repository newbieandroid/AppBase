package com.fuyoul.sanwenseller.listener

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
interface KeyBordChangerListener {

    fun onShow(keyBordH: Int, srollHeight: Int)
    fun onHidden()
}