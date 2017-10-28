package com.fuyoul.sanwenseller.helper

import android.content.Context
import android.text.TextUtils
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResQiNiuBean
import com.fuyoul.sanwenseller.configs.UrlInfo.GETIMGTOKEN
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.listener.QiNiuUpLoadListener
import com.lzy.okgo.OkGo
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager

/**
 *  Auther: chen
 *  Creat at: 2017\9\18 0018
 *  Desc:七牛上传
 */
object QiNiuHelper {
    private val uploadManager = UploadManager(Configuration.Builder()
            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
            .connectTimeout(10)           // 链接超时。默认10秒
            .useHttps(true)               // 是否使用https上传域名
            .responseTimeout(60)          // 服务器响应超时。默认60秒
            .build())

    private var index = 0

    private val result = ArrayList<ResQiNiuBean>()


    private var token = ""


    /**七牛多图上传**/


    fun multQiNiuUpLoad(context: Context, localPath: List<String>, listener: QiNiuUpLoadListener) {

        if (TextUtils.isEmpty(token)) {

            OkGo.get<ResHttpResult>(GETIMGTOKEN).execute(object : HttpReqListener(context) {
                override fun reqOk(result: ResHttpResult) {

                    token = result.data.toString()
                    doUp(context, localPath, listener)
                }

                override fun withoutData(code: Int, msg: String) {
                    listener.error(msg)
                }

                override fun error(errorInfo: String) {
                    listener.error(errorInfo)
                }

                override fun onFinish() {
                }
            })

        } else {
            doUp(context, localPath, listener)
        }


    }

    private fun doUp(context: Context, localPath: List<String>, listener: QiNiuUpLoadListener) {
        uploadManager.put(
                localPath[index],
                "${System.currentTimeMillis()}_${getFileName(localPath[index])}",
                token,
                { key, info, jsonObject ->

                    if (info.isOK) {

                        info.error

                        val item = ResQiNiuBean()
                        item.key = key
                        item.info = info
                        item.res = jsonObject
                        result.add(item)

                        index++
                        if (index == localPath.size) {//全部图片上传完成

                            val data = ArrayList<ResQiNiuBean>()
                            data.addAll(result)
                            listener.complete(data)

                            clear()

                        } else {
                            multQiNiuUpLoad(context, localPath, listener)
                        }

                    } else {
                        clear()
                        listener.error(info.error)

                    }

                }, null)
    }


    private fun getFileName(filePath: String): String {

        val array = filePath.split("/")

        return array[array.size - 1]

    }

    private fun clear() {
        index = 0
        result.clear()
    }

}