package com.fuyoul.sanwenseller.structure.view

import com.fuyoul.sanwenseller.base.BaseV
import com.fuyoul.sanwenseller.bean.reqhttp.ResDefaultImInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResSplash
import com.fuyoul.sanwenseller.bean.reshttp.ResSplashImg
import com.fuyoul.sanwenseller.ui.SplashActivity

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
abstract class SplashV : BaseV {

    abstract fun setImgs(imgs: List<ResSplash.ImageBean>)

    abstract fun setContactInfo(contacts: List<ResDefaultImInfo>)

    abstract fun gotoMain()


}