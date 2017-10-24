package com.app.base.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import com.app.base.R
import com.app.base.listener.OnDialogPositiveListener
import com.app.base.widgets.dialog.DialogViewHolder
import com.app.base.widgets.dialog.MyDialog
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


    /**带有确定和取消功能的对话框**/
    fun showNormalDialog(title: String, info: String, listener: OnDialogPositiveListener?) {
        showNormalDialog(title, info, "确定", listener)
    }

    fun showNormalDialog(title: String, info: String, sureText: String, listener: OnDialogPositiveListener?) {

        object : MyDialog(this, R.layout.msgdialog) {
            override fun convert(holder: DialogViewHolder?) {


                holder?.setText(R.id.title, title)
                holder?.setText(R.id.content, info)
                holder?.setText(R.id.sure, sureText)
                holder?.setOnClick(R.id.cancle, { dismiss() })
                holder?.setOnClick(R.id.sure, {
                    dismiss()
                    listener?.onClicked(this@BaseActivity)
                })
            }

        }.fullWidth().setCancelAble(false).showDialog(R.style.dialog_scale_animstyle)

    }

    /**只要一个按钮的对话框**/
    fun showSingleDialog(title: String, info: String, listener: OnDialogPositiveListener?) {
        showSingleDialog(title, info, "确定", listener)
    }

    fun showSingleDialog(title: String, info: String, sureText: String, listener: OnDialogPositiveListener?) {
        object : MyDialog(this, R.layout.singmsgdialog) {
            override fun convert(holder: DialogViewHolder?) {

                holder!!.setText(R.id.title, title)
                holder.setText(R.id.content, info)
                holder.setText(R.id.sure, sureText)
                holder.setOnClick(R.id.sure, View.OnClickListener {
                    dismiss()
                    listener?.onClicked(this@BaseActivity)
                })
            }

        }.fullWidth().setCancelAble(false).showDialog(R.style.dialog_scale_animstyle)

    }


}