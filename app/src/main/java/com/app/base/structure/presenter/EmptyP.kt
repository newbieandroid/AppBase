package com.app.base.structure.presenter

import com.app.base.base.BaseP
import com.app.base.structure.model.EmptyM
import com.app.base.structure.view.EmptyV

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