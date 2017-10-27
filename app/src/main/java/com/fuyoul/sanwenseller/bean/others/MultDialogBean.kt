package com.fuyoul.sanwenseller.bean.others

import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.configs.Code

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
class MultDialogBean : MultBaseBean {
    override fun itemType(): Int = Code.VIEWTYPE_MULTDIALOG

    var title = ""
}