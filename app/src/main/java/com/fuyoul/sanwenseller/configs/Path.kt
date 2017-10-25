package com.fuyoul.sanwenseller.configs

import android.os.Environment
import com.lzy.okgo.OkGo
import java.io.File

/**
 *  @author: chen
 *  @CreatDate: 2017\10\24 0024
 *  @Desc:各种文件夹的路径
 */
object Path {

    private val root = Environment.getExternalStorageDirectory().absolutePath + File.separator + OkGo.getInstance().context.packageName

    /**缓存路径**/
    val CACHE = "${root}${File.separator}Cache"
    /**下载路径**/
    val DOWNLOAD = "${root}${File.separator}Download"
    /**聊天数据路径**/
    val IM = "${root}${File.separator}Im"
    /**图片剪裁路径**/
    val CAPTURE = "${root}${File.separator}Capture"
}