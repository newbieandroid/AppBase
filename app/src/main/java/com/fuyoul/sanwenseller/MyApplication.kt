package com.fuyoul.sanwenseller

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.csl.share.OpenSdkHelper
import com.fuyoul.sanwenseller.helper.ActivityStateListener
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.smtt.sdk.QbSdk
import com.fuyoul.sanwenseller.im.ImInit
import com.zhy.autolayout.config.AutoLayoutConifg
import okhttp3.OkHttpClient
import org.litepal.LitePal
import java.util.concurrent.TimeUnit

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

        /**适配**/
        AutoLayoutConifg.getInstance().useDeviceSize()

        /**数据库**/
        LitePal.initialize(this)
        LitePal.getDatabase()

        /**X5内核浏览器**/
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(this, null)//初始化腾讯x5内核浏览器


        /**网络请求**/
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(60, TimeUnit.SECONDS)
        builder.writeTimeout(60, TimeUnit.SECONDS)



        OkGo
                .getInstance()
                .init(this)
                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .setOkHttpClient(builder.build())

        /**初始化网易聊天**/
        ImInit.initIm(this)

        /**分享**/
        OpenSdkHelper.initSdk(this)
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