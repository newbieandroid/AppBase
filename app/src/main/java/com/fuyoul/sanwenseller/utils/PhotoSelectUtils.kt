package com.fuyoul.sanwenseller.utils

import android.app.Activity
import android.content.Intent
import android.os.Build
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.configs.Code
import com.youquan.selector.Matisse
import com.youquan.selector.MimeType
import com.youquan.selector.engine.impl.GlideEngine
import com.youquan.selector.internal.entity.CaptureStrategy
import com.youquan.selector.internal.utils.MediaStoreCompat

/**
 *  Auther: chen
 *  Creat at: 2017\8\18 0018
 *  Desc:图片选择器
 */
class PhotoSelectUtils {

    //选择图片
    fun doSelect(context: Activity, selectPath: List<String>?) {

        Matisse.from(context)
                .choose(MimeType.ofImage()) // 选择 mime 的类型
                .capture(true)//不显示拍照
                .lastSelectPath(selectPath)//之前选中的图片
                .countable(true)//选择后的显示方式
                .maxSelectable(9) // 图片选择的最多数量
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(GlideEngine()) // 使用的图片加载引擎
                .captureStrategy(CaptureStrategy(true, context.resources.getString(R.string.zhifprovider)))
                .forResult(Code.REQ_SELECTIMGS) // 设置作为标记的请求码


    }


    //选择图片

    fun doSelect(context: Activity, maxCount: Int, selectPath: List<String>?) {
        doSelect(context, maxCount, selectPath, Code.REQ_SELECTIMGS)
    }

    fun doSelect(context: Activity, maxCount: Int, selectPath: List<String>?, reqCode: Int) {

        Matisse.from(context)
                .choose(MimeType.ofImage()) // 选择 mime 的类型
                .capture(false)//不显示拍照
                .lastSelectPath(selectPath)//之前选中的图片
                .countable(maxCount != 1)//选择后的显示方式
                .maxSelectable(maxCount) // 图片选择的最多数量
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(GlideEngine()) // 使用的图片加载引擎
                .captureStrategy(CaptureStrategy(true, context.getString(R.string.zhifprovider)))
                .forResult(reqCode) // 设置作为标记的请求码

    }

    //拍照

    fun doCapture(activity: Activity) {
        doCapture(activity, Code.REQ_CAMERA)
    }

    fun doCapture(activity: Activity, reqCode: Int) {
        mediaStoreCompat = MediaStoreCompat(activity)
        mediaStoreCompat?.setCaptureStrategy(CaptureStrategy(true, activity.getString(R.string.zhifprovider)))
        mediaStoreCompat?.dispatchCaptureIntent(activity, reqCode)
    }

    fun getCapturePath(activity: Activity): String {
        val path = mediaStoreCompat!!.currentPhotoPath
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.revokeUriPermission(mediaStoreCompat!!.currentPhotoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        return path
    }

    private var mediaStoreCompat: MediaStoreCompat? = null

}