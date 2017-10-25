package com.fuyoul.sanwenseller.helper

import android.content.Context
import android.text.TextUtils
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.widgets.dialog.AbstractDialog
import com.fuyoul.sanwenseller.widgets.dialog.DialogViewHolder

/**
 *  @author: chen
 *  @CreatDate: 2017\10\24 0024
 *  @Desc: 提示信息的对话框
 */
object MsgDialogHelper {


    interface DialogListener {
        fun onPositive()
        fun onNagetive()
    }


    /**显示带有确定和取消的按钮**/
    fun showNormalDialog(context: Context, cancleable: Boolean, title: String, msgInfo: String, listener: DialogListener?) {
        showNormalDialog(context, cancleable, title, msgInfo, null, null, listener)
    }

    fun showNormalDialog(context: Context, cancleable: Boolean, title: String, msgInfo: String, sureText: String?, listener: DialogListener?) {
        showNormalDialog(context, cancleable, title, msgInfo, null, sureText, listener)
    }

    fun showNormalDialog(context: Context, cancleable: Boolean, title: String, msgInfo: String, cancleText: String?, sureText: String?, listener: DialogListener?) {

        object : AbstractDialog(context, R.layout.msgdialog) {
            override fun convert(holder: DialogViewHolder?) {

                holder?.setText(R.id.title, title)
                holder?.setText(R.id.content, msgInfo)
                holder?.setText(R.id.sure, if (TextUtils.isEmpty(sureText)) "确定" else sureText)
                holder?.setText(R.id.cancle, if (TextUtils.isEmpty(cancleText)) "取消" else cancleText)

                holder?.setOnClick(R.id.sure, {
                    dismiss()
                    listener?.onPositive()
                })
                holder?.setOnClick(R.id.cancle, {
                    dismiss()
                    listener?.onNagetive()
                })
            }

        }.backgroundLight(0.7).setCancelAble(cancleable).showDialog(R.style.dialog_scale_animstyle)

    }


    /**底部只有一个按钮的弹框**/
    fun showSingleDialog(context: Context, cancleable: Boolean, title: String, msgInfo: String, sureText: String?, listener: DialogListener?) {

        object : AbstractDialog(context, R.layout.singmsgdialog) {
            override fun convert(holder: DialogViewHolder?) {

                holder?.setText(R.id.title, title)
                holder?.setText(R.id.content, msgInfo)
                holder?.setText(R.id.sure, if (TextUtils.isEmpty(sureText)) "确定" else sureText)

                holder?.setOnClick(R.id.sure, {
                    dismiss()
                    listener?.onPositive()
                })
            }

        }.backgroundLight(0.7).setCancelAble(cancleable).showDialog(R.style.dialog_scale_animstyle)

    }


}