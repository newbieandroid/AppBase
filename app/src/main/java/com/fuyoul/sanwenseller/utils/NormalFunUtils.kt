package com.fuyoul.sanwenseller.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import android.os.Build


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


    /**跳转到app信息页**/
    fun goToAppDetailSettingIntent(context: Context) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        context.startActivity(localIntent)
    }
}