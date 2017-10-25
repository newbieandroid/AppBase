package com.fuyoul.sanwenseller.helper

import android.app.Activity
import java.util.*

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
object ActivityStateHelper {
    private val activitys = ArrayList<Activity>()

    /**添加到管理列表**/
    fun addToList(activity: Activity) {
        activitys.add(activity)
    }

    /**从列表中移除某一个**/
    fun removeToList(activity: Activity) {
        activitys.remove(activity)
        activity.finish()
    }


    /**移除所有**/
    fun removeAll() {

        for (index in 0 until activitys.size) {
            activitys[index].finish()
        }
        activitys.clear()
    }
}