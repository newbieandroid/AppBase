package com.fuyoul.sanwenseller.structure.presenter

import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.structure.model.EditBabyM
import com.fuyoul.sanwenseller.structure.view.EditBabyV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
class EditBabyP(editBabyV: EditBabyV) : BaseP<EditBabyM, EditBabyV>(editBabyV) {
    override fun getModelImpl(): EditBabyM = EditBabyM()
}