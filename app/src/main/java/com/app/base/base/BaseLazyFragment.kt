package com.app.base.base

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View


/**
 *  Auther: chen
 *  Creat at: 2017\10\18 0018
 *  Desc:
 */
abstract class BaseLazyFragment : Fragment() {
    private var mRootView: View? = null

    /**
     * 是否对用户可见
     */
    private var mIsVisible: Boolean = false
    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    private var mIsPrepare: Boolean = false

    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    private var mIsImmersion: Boolean = false


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater!!.inflate(setLayoutId(), container, false)
        return mRootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLazyLoad()) {
            mIsPrepare = true
            mIsImmersion = true
            onLazyLoad()
        } else {
            initData()
        }
        initView()
        setListener()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (userVisibleHint) {
            mIsVisible = true
            onVisible()
        } else {
            mIsVisible = false
            onInvisible()
        }
    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    private fun isLazyLoad(): Boolean {
        return true
    }


    /**
     * 用户可见时执行的操作
     */
    private fun onVisible() {
        onLazyLoad()
    }

    private fun onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false
            initData()
        }
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    abstract fun setLayoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()


    /**
     * view与数据绑定
     */
    abstract fun initView()

    /**
     * 设置监听
     */
    abstract fun setListener()

    /**
     * 用户不可见执行
     */
    private fun onInvisible() {

    }

}