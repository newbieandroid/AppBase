package com.fuyoul.sanwenseller.structure.view

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.fuyoul.sanwenseller.base.BaseV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
abstract class EditBabyV : BaseV {


    abstract fun doSelectImg(activity: Activity)

    abstract fun addImg(context: Context, path: Any, view: ImageView)

}