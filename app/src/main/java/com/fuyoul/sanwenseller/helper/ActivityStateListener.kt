package com.fuyoul.sanwenseller.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.fuyoul.sanwenseller.MyApplication

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:监听整个app页面的状态
 */
class ActivityStateListener : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity?) {
    }

    override fun onActivityResumed(p0: Activity?) {
    }

    override fun onActivityStarted(p0: Activity?) {
    }

    override fun onActivityDestroyed(p0: Activity?) {
        ActivityStateHelper.removeToList(p0!!)
        MyApplication.getRefWatcher(p0).watch(p0)
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(p0: Activity?) {
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {

        ActivityStateHelper.addToList(p0!!)
    }
}