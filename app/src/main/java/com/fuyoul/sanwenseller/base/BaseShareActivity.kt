package com.fuyoul.sanwenseller.base

import android.content.Intent
import com.csl.share.OpenSdkHelper
import com.csl.share.bean.ShareBean
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.fuyoul.sanwenseller.widgets.dialog.AbstractDialog
import com.fuyoul.sanwenseller.widgets.dialog.DialogViewHolder
import com.sina.weibo.sdk.share.WbShareCallback

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:分享页面
 */
abstract class BaseShareActivity<out M : BaseM, V : BaseV, out P : BaseP<M, V>> : BaseActivity<M, V, P>(), WbShareCallback {
    override fun onWbShareFail() {
        NormalFunUtils.showToast(this, "微博分享失败")
    }

    override fun onWbShareCancel() {
        NormalFunUtils.showToast(this, "用户取消分享")
    }

    override fun onWbShareSuccess() {
        NormalFunUtils.showToast(this, "微博分享成功")
    }


    fun doShare(shareBean: ShareBean) {

        object : AbstractDialog(this, R.layout.sharedialog) {
            override fun convert(holder: DialogViewHolder?) {

                holder?.setOnClick(R.id.shareToQzone, {

                    OpenSdkHelper.shareToQZONE(this@BaseShareActivity, shareBean)

                    dismiss()
                })
                holder?.setOnClick(R.id.shareToQq, {
                    OpenSdkHelper.shareToQQ(this@BaseShareActivity, shareBean)
                    dismiss()
                })
                holder?.setOnClick(R.id.shareToWx, {
                    OpenSdkHelper.shareToWx(this@BaseShareActivity, false, shareBean)
                    dismiss()
                })
                holder?.setOnClick(R.id.shareToWxCircle, {
                    OpenSdkHelper.shareToWx(this@BaseShareActivity, true, shareBean)
                    dismiss()
                })
                holder?.setOnClick(R.id.shareToWb, {
                    OpenSdkHelper.shareToSine(this@BaseShareActivity, shareBean)
                    dismiss()
                })

            }
        }.backgroundLight(0.7).fullWidth().fromBottom().setCancelAble(true).showDialog()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        OpenSdkHelper.onQQresult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        OpenSdkHelper.shareHandler?.doResultIntent(intent, this)
    }
}