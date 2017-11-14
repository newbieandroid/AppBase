package com.fuyoul.sanwenseller.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
class SpUtils {

    companion object {

        private var sp: SharedPreferences? = null

        fun init(context: Context) {
            sp = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        }


        fun putBoolean(key: String, result: Boolean) {
            sp?.edit()?.putBoolean(key, result)?.apply()
        }

        fun getBoolean(key: String): Boolean = sp?.getBoolean(key, true) ?: true
    }


}