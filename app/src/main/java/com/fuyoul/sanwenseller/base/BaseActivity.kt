package com.fuyoul.sanwenseller.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.utils.StatusBarUtils
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.includetopbar.*

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
abstract class BaseActivity<out M : BaseM, V : BaseV, out P : BaseP<M, V>> : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)//去掉标题栏
        setContentView(setLayoutRes())
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//强制竖屏,在显示view之后调用
        setBar(initTopBar())
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

    protected abstract fun initTopBar(): TopBarOption

    private fun setBar(option: TopBarOption) {
        if (option.isShowBar) {


            StatusBarUtils.setTranslucentForImageView(this, titBarLayout)
            StatusBarUtils.StatusBarLightMode(this, R.color.color_white)


            if (option.contentBackRes != 0) {
                titBarLayout.setBackgroundResource(option.contentBackRes)
            }

            if (option.navigationIcon != 0) {
                toolbarBack.setImageResource(option.navigationIcon)
                toolbarBack.setOnClickListener {
                    finish()
                }
                if (option.navigationListener != null) {
                    toolbarBack.setOnClickListener(option.navigationListener)
                }
            }

            if (option.mainTitle != null) {
                toolbarTitle.text = option.mainTitle
            }

            if (option.childTitle != null) {
                toolbarChildTitle.visibility = View.VISIBLE
                toolbarChildImg.visibility = View.GONE
                toolbarChildTitle.text = option.childTitle
                toolbarChildTitle.setOnClickListener(option.childListener)


                if (option.childTitleColor != 0) {
                    toolbarChildTitle.setTextColor(resources.getColor(option.childTitleColor))
                }

            }
            if (option.childIcon != 0) {
                toolbarChildTitle.visibility = View.GONE
                toolbarChildImg.visibility = View.VISIBLE
                toolbarChildImg.setImageResource(option.childIcon)
                toolbarChildImg.setOnClickListener(option.childListener)

            }

        }
    }

}