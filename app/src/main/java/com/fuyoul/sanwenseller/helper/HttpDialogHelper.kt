package com.fuyoul.sanwenseller.helper

import android.content.Context
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.widgets.WaitLoadingView
import com.fuyoul.sanwenseller.widgets.dialog.AbstractDialog
import com.fuyoul.sanwenseller.widgets.dialog.DialogViewHolder

/**
 *  @author: chen
 *  @CreatDate: 2017\10\24 0024
 *  @Desc: 网络请求显示的等待对话框
 */
object HttpDialogHelper {

    private var dialog: AbstractDialog? = null
    private var httpLoadingView: WaitLoadingView? = null

    /**
     * @param isShowDialog 是否显示等待对话框
     * @param isCancleable 是否可以手动点击取消
     */
    fun showDialog(context: Context, isShowDialog: Boolean, isCancleable: Boolean) {

        if (!isShowDialog) {
            return
        }

        if (dialog == null) {
            dialog = object : AbstractDialog(context, R.layout.httploading) {
                override fun convert(holder: DialogViewHolder?) {
                    httpLoadingView = holder?.getView(R.id.httpLoadingView)
                    httpLoadingView?.startAnimator()
                }
            }.setDialogDismissListener({
                httpLoadingView?.stopAnimator()
            }).setCancelAble(isCancleable).showDialog()
        } else {

            if (dialog?.isShow == false) {
                dialog?.showDialog()
            }

        }


    }


    fun dismisss() {
        if (dialog?.isShow == true) {
            dialog?.dismiss()
        }

        dialog = null
        httpLoadingView = null
    }
}