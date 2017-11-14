package com.fuyoul.sanwenseller.ui.web

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.FrameLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.webview.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\28 0028
 *  @Desc:
 */
class WebViewActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {

    private val COVER_SCREEN_PARAMS: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    private var customView: View? = null
    private var fullscreenContainer: FrameLayout? = null
    private var customViewCallback: IX5WebChromeClient.CustomViewCallback? = null

    companion object {

        fun startWebView(context: Context, title: String, urlPath: String) {

            if (TextUtils.isEmpty(urlPath)) {
                return
            } else if (!urlPath.contains("http")) {
                NormalFunUtils.showToast(context, "地址有误")
                return
            }
            context.startActivity(Intent(context, WebViewActivity::class.java).putExtra("title", title).putExtra("urlPath", urlPath))
        }

    }

    override fun setLayoutRes(): Int = R.layout.webview

    override fun initData(savedInstanceState: Bundle?) {

        normalWeb.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                p0!!.loadUrl(p1)
                return false
            }

        })

        normalWeb.setWebChromeClient(object : WebChromeClient() {

            override fun getVideoLoadingProgressView(): View {
                val frameLayout = FrameLayout(this@WebViewActivity)
                frameLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                return frameLayout


            }

            override fun onShowCustomView(view: View?, customViewCallback: IX5WebChromeClient.CustomViewCallback?) {
                super.onShowCustomView(view, customViewCallback)

                showCustomView(view!!, customViewCallback!!)


            }

            override fun onHideCustomView() {
                super.onHideCustomView()

                hideCustomView()
            }


        })


        val webSetting = normalWeb.settings
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(false)
        webSetting.setAppCacheEnabled(true)
        webSetting.domStorageEnabled = true
        webSetting.javaScriptEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        webSetting.setAppCachePath(this.getDir("appcache", 0).path)
        webSetting.databasePath = this.getDir("databases", 0).path
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).path)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND

        normalWeb.loadUrl(intent.getStringExtra("urlPath"))
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {
        val op = TopBarOption()


        if (TextUtils.isEmpty(intent.getStringExtra("title"))) {
            return op
        }
        op.isShowBar = true
        op.mainTitle = intent.getStringExtra("title")

        return op
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (customView != null) {
                hideCustomView()
                return true
            } else if (normalWeb != null && normalWeb.canGoBack()) {
                normalWeb.goBack()
                return true
            } else {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        normalWeb.clearCache(true)
        normalWeb.clearFormData()
        normalWeb.clearHistory()
        normalWeb.clearMatches()
        normalWeb.clearSslPreferences()
        normalWeb.destroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFormat(PixelFormat.TRANSLUCENT)
        super.onCreate(savedInstanceState)
    }


    /** 视频播放全屏  */
    private fun showCustomView(view: View, callback: IX5WebChromeClient.CustomViewCallback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden()
            return
        }

        val decor = window.decorView as FrameLayout
        fullscreenContainer = FullscreenHolder(this)
        fullscreenContainer!!.addView(view, COVER_SCREEN_PARAMS)
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS)
        customView = view
        setStatusBarVisibility(false)
        customViewCallback = callback
    }

    /** 隐藏视频全屏  */
    private fun hideCustomView() {
        if (customView == null) {
            return
        }
        setStatusBarVisibility(true)
        val decor = window.decorView as FrameLayout
        decor.removeView(fullscreenContainer)
        fullscreenContainer = null
        customView = null
        customViewCallback!!.onCustomViewHidden()
        normalWeb.visibility = View.VISIBLE
    }

    /** 全屏容器界面  */
    internal class FullscreenHolder(ctx: Context) : FrameLayout(ctx) {

        init {
            setBackgroundColor(ctx.resources.getColor(android.R.color.black))
        }


        override fun onTouchEvent(evt: MotionEvent): Boolean {
            return true
        }
    }

    private fun setStatusBarVisibility(visible: Boolean) {
        //标题栏
        val flag = if (visible) 0 else WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //虚拟按键
        try {
            if (visible) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}