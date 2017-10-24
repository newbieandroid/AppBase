package com.app.base

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.app.base.helper.ActivityStateListener
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class MyApplication : Application() {

    var refWatcher: RefWatcher? = null

    companion object {
        fun getRefWatcher(context: Context): RefWatcher {
            val application = context.applicationContext as MyApplication
            return application.refWatcher!!
        }

    }


    override fun onCreate() {
        super.onCreate()
        /**注册页面管理器**/
        registerActivityLifecycleCallbacks(ActivityStateListener())

        /**内存泄漏监控**/
        refWatcher = LeakCanary.install(this)
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        System.gc()
    }
}