package com.fuyoul.sanwenseller.structure.presenter

import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.view.EmptyV

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class EmptyP : BaseP<EmptyM, EmptyV>(EmptyV()) {
    override fun getModelImpl(): EmptyM {
        return EmptyM()
    }
}