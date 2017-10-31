package com.fuyoul.sanwenseller.bean.others

import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.configs.Code

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class AppointMentItemBean : MultBaseBean {
    override fun itemType(): Int = Code.VIEWTYPE_APPOINTMENT

    var isSelect = false//是否已预约
    var time: String = ""//时间
    var avatar: String = ""//预约用户的头像

    var userInfoId: Long = 0L//预约人的id
    var isBusy = false//是否全天不可约
}