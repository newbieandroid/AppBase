package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reshttp.ResAppointMentInfo

/**
 *  @author: chen
 *  @CreatDate: 2017\11\2 0002
 *  @Desc:
 */
abstract class AppointMentInfoV : BaseV {

    abstract fun setData(result: ResAppointMentInfo)

    abstract fun startChat(userInfoId: Long)
}