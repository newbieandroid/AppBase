package com.csl.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.csl.share.bean.ShareBean
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.api.BaseMediaObject
import com.sina.weibo.sdk.api.WebpageObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.share.WbShareHandler
import com.tencent.connect.common.UIListenerManager
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.open.a.f
import com.tencent.tauth.Tencent
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
object OpenSdkHelper {


    var shareHandler: WbShareHandler? = null

    var wxApi: IWXAPI? = null
    var mTencent: Tencent? = null

    var wxAppId: String? = null
    var wxSecret: String? = null

    /**在application中初始化**/
    fun initSdk(context: Context) {


        //QQ注册
        mTencent = Tencent.createInstance(context.resources.getString(R.string.qqKey), context)
        //微信注册
        wxAppId = context.resources.getString(R.string.wxKey)
        wxSecret = context.resources.getString(R.string.wxSecret)
        wxApi = WXAPIFactory.createWXAPI(context, wxAppId)
        wxApi!!.registerApp(wxAppId)

        //微博注册
        WbSdk.install(context, AuthInfo(context, context.resources.getString(R.string.wbKey),
                "http://sns.whalecloud.com/sina2/callback",
                "email," +
                        "direct_messages_read," +
                        "direct_messages_write," +
                        "friendships_groups_read," +
                        "friendships_groups_write," +
                        "statuses_to_me_read," +
                        "follow_app_official_microblog," +
                        "invitation_write"))


    }


    /**
     * 登录
     */
    fun wxLogin(context: Context) {


        if (wxApi!!.isWXAppInstalled) {

            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "time${System.currentTimeMillis()}"
            wxApi!!.sendReq(req)
        } else {
            Toast.makeText(context, "请安装微信客户端", Toast.LENGTH_SHORT).show()
        }


    }


    /**QQ分享的回调**/
    fun onQQresult(requestCode: Int, resultCode: Int, data: Intent?) {
        f.c("openSDK_LOG.Tencent", "onActivityResult() deprecated, will do nothing")
        UIListenerManager.getInstance().onActivityResult(requestCode, resultCode, data, null)
    }


    /**
     * 分享
     */
    fun shareToQQ(activity: Activity, shareBean: ShareBean) {
        val bundle = Bundle()
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO)// //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareBean.url)// //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareBean.shareTitle)// //分享的标题
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareBean.imgPath)////分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareBean.description)// //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))



        mTencent?.shareToQQ(activity, bundle, QsdkListener(activity))

    }


    fun shareToQZONE(activity: Activity, shareBean: ShareBean) {

        val bundle = Bundle()
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT)// //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareBean.url)
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareBean.shareTitle)// //分享的标题

        val imgList = ArrayList<String>()
        imgList.add(shareBean.imgPath)
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgList)////分享的图片URL
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareBean.description)// //分享的消息摘要，最长50个字
        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
        mTencent?.shareToQzone(activity, bundle, QsdkListener(activity))
    }


    //微信分享的图片显示大小32kb
    fun shareToWx(activity: Activity, isWxCircle: Boolean, shareBean: ShareBean) {

        if (wxApi?.isWXAppInstalled == false) {
            Toast.makeText(activity, "请安装微信客户端", Toast.LENGTH_SHORT).show()
            return
        }

        Glide.with(activity)
                .load(shareBean.imgPath)
                .asBitmap() //必须
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        Toast.makeText(activity, "图片解析失败", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResourceReady(bitmap: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {


                        activity.runOnUiThread {

                            val wxObj = WXWebpageObject()
                            wxObj.webpageUrl = shareBean.url

                            val wxMsg = WXMediaMessage()
                            wxMsg.title = shareBean.shareTitle
                            wxMsg.description = shareBean.description
                            wxMsg.mediaObject = wxObj

                            val maxkb = 32//微信限制的图片的最大值：kb
                            val output = ByteArrayOutputStream()
                            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, output)
                            var options = 100
                            while (output.toByteArray().size > maxkb && options != 10) {
                                output.reset() //清空output
                                bitmap?.compress(Bitmap.CompressFormat.JPEG, options, output)//这里压缩options%，把压缩后的数据存放到output中
                                options -= 10
                            }
                            wxMsg.thumbData = output.toByteArray()

//                            wxMsg.setThumbImage(bitmap)
                            val req = SendMessageToWX.Req()
                            req.transaction = System.currentTimeMillis().toString()
                            req.message = wxMsg

                            if (isWxCircle) {
                                req.scene = SendMessageToWX.Req.WXSceneTimeline
                            } else {
                                req.scene = SendMessageToWX.Req.WXSceneSession
                            }

                            wxApi?.sendReq(req)

                        }
                    }

                })

    }


    fun shareToSine(activity: Activity, shareBean: ShareBean) {


        Glide.with(activity)
                .load(shareBean.imgPath)
                .asBitmap() //必须
                .into(object : SimpleTarget<Bitmap>() {

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        Toast.makeText(activity, "图片解析失败", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResourceReady(bitmap: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {

                        val webPage = WebpageObject()
                        webPage.title = shareBean.shareTitle
                        webPage.actionUrl = shareBean.url
                        webPage.description = shareBean.description


                        val baos = ByteArrayOutputStream()
                        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos)

                        webPage.thumbData = baos.toByteArray()

                        val wbMessage = WeiboMultiMessage()
                        wbMessage.msgType = BaseMediaObject.MEDIA_TYPE_WEBPAGE
                        wbMessage.mediaObject = webPage


                        //clientOnly - 是否只用微博客户端.true:只用微博客户端进行分享，false：如果有客户端，使用客户端，否则使用web进行分享
                        shareHandler = WbShareHandler(activity)
                        shareHandler?.registerApp()
                        shareHandler?.shareMessage(wbMessage, false)
                    }

                })


    }
}