package com.fuyoul.sanwenseller.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhy.autolayout.utils.AutoUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
abstract class BaseFragment<out M : BaseM, V : BaseV, out P : BaseP<M, V>> : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(setLayoutRes(), container, false)
        AutoUtils.auto(view)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view, savedInstanceState)
        setListener()
        getPresenter()
    }


    /**设置资源文件**/
    abstract fun setLayoutRes(): Int

    abstract fun init(view: View?, savedInstanceState: Bundle?)

    abstract fun setListener()

    /**MVP的中转器**/
    protected abstract fun getPresenter(): P

    /**MVP的view层**/
    protected abstract fun initViewImpl(): V
}