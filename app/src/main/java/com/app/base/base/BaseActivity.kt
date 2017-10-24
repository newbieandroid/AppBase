package com.app.base.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.zhy.autolayout.AutoLayoutActivity

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
abstract class BaseActivity<out M : BaseM, V : BaseV, out P : BaseP<M, V>> : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)//去掉标题栏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//强制竖屏
        setContentView(setLayoutRes())
        initData(savedInstanceState)
        setListener()
        getPresenter()
    }

    /**设置布局资源文件**/
    protected abstract fun setLayoutRes(): Int

    /**初始化各种数据**/
    protected abstract fun initData(savedInstanceState: Bundle?)

    /**设置各种监听**/
    protected abstract fun setListener()

    /**MVP的中转器**/
    protected abstract fun getPresenter(): P

    /**MVP的view层**/
    protected abstract fun initViewImpl(): V


}