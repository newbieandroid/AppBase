package com.app.base.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 *  Auther: chen
 *  Creat at: 2017\10\18 0018
 *  Desc:基本功能集合类
 */
object NormalFunUtils {

    /**消息提示**/
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    /**控制软键盘**/
    fun changeKeyBord(context: Context, isShow: Boolean, focusView: View) {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED)
        if (isShow) {
            imm.showSoftInputFromInputMethod(focusView.windowToken, 0)
        } else {
            imm.hideSoftInputFromWindow(focusView.windowToken, 0)
        }

    }
}