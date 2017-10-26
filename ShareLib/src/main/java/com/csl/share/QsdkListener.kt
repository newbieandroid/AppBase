package com.csl.share

import android.app.Activity
import android.widget.Toast
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError

/**
 *  Auther: chen
 *  Creat at: 2017\9\4 0004
 *  Desc:
 */
class QsdkListener(activity: Activity) : IUiListener {

    var activity: Activity? = null

    init {
        this.activity = activity
    }

    override fun onComplete(p0: Any?) {
        Toast.makeText(activity, "QQ分享完成", Toast.LENGTH_SHORT).show()
    }

    override fun onCancel() {
        Toast.makeText(activity, "用户取消QQ分享", Toast.LENGTH_SHORT).show()
    }

    override fun onError(p0: UiError?) {
        Toast.makeText(activity, "QQ分享失败:${p0!!.errorMessage}", Toast.LENGTH_SHORT).show()
    }

}