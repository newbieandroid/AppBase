package com.fuyoul.sanwenseller.base

import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.includetopbar.*
import android.os.Build
import android.view.WindowManager
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.netease.nim.uikit.StatusBarUtils


/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
abstract class BaseActivity<out M : BaseM, V : BaseV, out P : BaseP<M, V>> : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)//去掉标题栏

        if (isFullScreen()) {
            this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
            this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        setContentView(setLayoutRes())
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//强制竖屏,在显示view之后调用
        setBar(initTopBar())
        initData(savedInstanceState)
        setListener()
        getPresenter()
    }

    open fun isFullScreen(): Boolean = false

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


    private var globalLayoutListener: CustomGlobalLayoutListener? = null


    inner class CustomGlobalLayoutListener(root: View, scrollToView: View) : ViewTreeObserver.OnGlobalLayoutListener {


        var root: View? = null
        var scrollToView: View? = null

        init {
            this.root = root
            this.scrollToView = scrollToView
        }

        override fun onGlobalLayout() {

            val rect = Rect()
            //获取root在窗体的可视区域
            root?.getWindowVisibleDisplayFrame(rect)
            //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
            val rootInvisibleHeight = (root?.rootView?.height ?: 0) - rect.bottom

            //若不可视区域高度大于100，则键盘显示
            if (rootInvisibleHeight > 100) {
                val location = IntArray(2)
                //获取scrollToView在窗体的坐标
                scrollToView?.getLocationInWindow(location)
                //计算root滚动高度，使scrollToView在可见区域的底部
                val srollHeight = location[1] + (scrollToView?.height ?: 0) - rect.bottom

                root?.scrollTo(0, srollHeight)
            } else {
                //键盘隐藏
                root?.scrollTo(0, 0)
            }
        }

    }

    fun registKeyBordListener(root: View, scrollToView: View) {

        if (globalLayoutListener == null) {
            globalLayoutListener = CustomGlobalLayoutListener(root, scrollToView)
        }

        root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }


    /**兼容软键盘控制布局的移动**/
    private fun ounRegistKeyBordListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            globalLayoutListener?.root?.viewTreeObserver?.removeGlobalOnLayoutListener(globalLayoutListener)
        } else {
            globalLayoutListener?.root?.viewTreeObserver?.removeOnGlobalLayoutListener(globalLayoutListener)

        }
    }


    override fun onRestart() {
        super.onRestart()
        if (globalLayoutListener != null) {
            registKeyBordListener(globalLayoutListener!!.root!!, globalLayoutListener!!.scrollToView!!)
        }
    }

    override fun onStop() {
        super.onStop()

        if (globalLayoutListener != null) {
            NormalFunUtils.changeKeyBord(this, false, globalLayoutListener!!.scrollToView!!)

        }

        ounRegistKeyBordListener()
    }
}