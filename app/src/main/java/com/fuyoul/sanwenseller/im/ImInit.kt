package com.fuyoul.sanwenseller.im

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.configs.Path
import com.fuyoul.sanwenseller.im.conifig.ImConfigs
import com.fuyoul.sanwenseller.im.helper.SessionHelper
import com.fuyoul.sanwenseller.im.mixpush.DemoMixPushMessageHandler
import com.fuyoul.sanwenseller.im.receiver.PhoneCallStateObserver
import com.fuyoul.sanwenseller.im.session.activity.avchat.AVChatActivity
import com.fuyoul.sanwenseller.im.session.activity.avchat.AVChatProfile
import com.fuyoul.sanwenseller.ui.SplashActivity
import com.lzy.okgo.OkGo
import com.netease.nim.uikit.NimUIKit
import com.netease.nim.uikit.UserPreferences
import com.netease.nim.uikit.custom.DefaultUserInfoProvider
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase
import com.netease.nimlib.sdk.*
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.avchat.AVChatManager
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment
import com.netease.nimlib.sdk.avchat.model.AVChatData
import com.netease.nimlib.sdk.mixpush.NIMPushClient
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum
import com.netease.nimlib.sdk.team.model.IMMessageFilter
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment
import org.litepal.crud.DataSupport


/**
 *  Auther: chen
 *  Creat at: 2017\9\11 0011
 *  Desc:初始化网易sdk
 */
object ImInit {

    fun initIm(context: Context) {


        // 注册小米推送，参数：小米推送证书名称（需要在云信管理后台配置）、appID 、appKey，该逻辑放在 NIMClient init 之前
        NIMPushClient.registerMiPush(context, context.resources.getString(R.string.mipushhname), context.resources.getString(R.string.xiaomiappid), context.resources.getString(R.string.xiaomiappkey))

        // 注册自定义推送消息处理，这个是可选项
        NIMPushClient.registerMixPushMessageHandler(DemoMixPushMessageHandler())

        NIMClient.init(context, getLoginInfo(), getOptions(context))
        if (inMainProcess(context)) {

            NimUIKit.init(context)
            SessionHelper.init()

            // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
            //NimUIKit.CustomPushContentProvider(DemoPushContentProvider())

            // 注册通知消息过滤器
            registerIMMessageFilter()

            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle())

            // 注册网络通话来电
            registerAVChatIncomingCallObserver(true)

            // 注册语言变化监听
            registerLocaleReceiver(context, true)


        }

    }

    private fun registerLocaleReceiver(context: Context, register: Boolean) {
        if (register) {
            updateLocale(context)
            val filter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
            context.registerReceiver(localeReceiver, filter)
        } else {
            context.unregisterReceiver(localeReceiver)
        }
    }

    private val localeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_LOCALE_CHANGED) {
                updateLocale(OkGo.getInstance().context)
            }
        }
    }

    private fun updateLocale(context: Context) {
        val strings = NimStrings()
        strings.status_bar_multi_messages_incoming = context.resources.getString(R.string.nim_status_bar_multi_messages_incoming)
        strings.status_bar_image_message = context.resources.getString(R.string.nim_status_bar_image_message)
        strings.status_bar_audio_message = context.resources.getString(R.string.nim_status_bar_audio_message)
        strings.status_bar_custom_message = context.resources.getString(R.string.nim_status_bar_custom_message)
        strings.status_bar_file_message = context.resources.getString(R.string.nim_status_bar_file_message)
        strings.status_bar_location_message = context.resources.getString(R.string.nim_status_bar_location_message)
        strings.status_bar_notification_message = context.resources.getString(R.string.nim_status_bar_notification_message)
        strings.status_bar_ticker_text = context.resources.getString(R.string.nim_status_bar_ticker_text)
        strings.status_bar_unsupported_message = context.resources.getString(R.string.nim_status_bar_unsupported_message)
        strings.status_bar_video_message = context.resources.getString(R.string.nim_status_bar_video_message)
        strings.status_bar_hidden_message_content = context.resources.getString(R.string.nim_status_bar_hidden_msg_content)
        NIMClient.updateStrings(strings)
    }

    private fun registerAVChatIncomingCallObserver(register: Boolean) {
        AVChatManager.getInstance().observeIncomingCall(Observer<AVChatData> { data ->
            if (PhoneCallStateObserver.getInstance().phoneCallState !== PhoneCallStateObserver.PhoneCallStateEnum.IDLE
                    || AVChatProfile.getInstance().isAVChatting
                    || AVChatManager.getInstance().currentChatId != 0L) {
                AVChatManager.getInstance().sendControlCommand(data.chatId, AVChatControlCommand.BUSY, null)
                return@Observer
            }
            // 有网络来电打开AVChatActivity
            AVChatProfile.getInstance().isAVChatting = true
            AVChatProfile.getInstance().launchActivity(data, AVChatActivity.FROM_BROADCASTRECEIVER)
        }, register)
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private fun registerIMMessageFilter() {
        NIMClient.getService(MsgService::class.java).registerIMMessageFilter(IMMessageFilter { message ->
            if (UserPreferences.getMsgIgnore() && message.attachment != null) {
                if (message.attachment is UpdateTeamAttachment) {
                    val attachment = message.attachment as UpdateTeamAttachment
                    for ((key) in attachment.updatedFields) {
                        if (key == TeamFieldEnum.ICON) {
                            return@IMMessageFilter true
                        }
                    }
                } else if (message.attachment is AVChatAttachment) {
                    return@IMMessageFilter true
                }
            }
            false
        })
    }

    private fun initStatusBarNotificationConfig(context: Context, options: SDKOptions) {
        val config: StatusBarNotificationConfig = loadStatusBarNotificationConfig(context)
        val userConfig = StatusBarNotificationConfig()
        userConfig.notificationEntrance = config.notificationEntrance
        userConfig.notificationFolded = config.notificationFolded
        userConfig.notificationColor = config.notificationColor
        options.statusBarNotificationConfig = userConfig
    }

    private fun loadStatusBarNotificationConfig(context: Context): StatusBarNotificationConfig {
        val config = StatusBarNotificationConfig()
        // 点击通知需要跳转到的界面
        config.notificationEntrance = SplashActivity::class.java
        config.notificationSmallIconId = R.mipmap.ic_launcher_round
        config.notificationColor = context.resources.getColor(R.color.color_activity_blue_bg)
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.fuyoul.sanwenseller/raw/msg"

        // 呼吸灯配置
        config.ledARGB = Color.GREEN
        config.ledOnMs = 1000
        config.ledOffMs = 1500

        ImConfigs.notificationConfig = config
        return config
    }

    private fun getOptions(context: Context): SDKOptions {
        val options = SDKOptions()
        initStatusBarNotificationConfig(context, options)
        options.sdkStorageRootPath = Path.IM

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "sanwen.yijing"

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge()

        // 用户信息提供者
        options.userInfoProvider = DefaultUserInfoProvider(context)

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = object : MessageNotifierCustomization {
            override fun makeNotifyContent(p0: String?, p1: IMMessage?): String? {
                return null
            }

            override fun makeTicker(p0: String?, p1: IMMessage?): String? {
                return null
            }

        }

        // 在线多端同步未读数
        options.sessionReadAck = true
        return options

    }

    private fun getLoginInfo(): LoginInfo? {

        val resHttpLoginBean = DataSupport.findFirst(ResLoginInfoBean::class.java)


        Log.e("csl", "网易自动登录:${JSON.toJSONString(resHttpLoginBean)}")

        if (resHttpLoginBean != null) {
            val loginInfo = LoginInfo("user_${resHttpLoginBean.userInfoId}", resHttpLoginBean.imToken)
            NimUIKit.setAccount(loginInfo.account)

            return loginInfo
        }
        return null
    }


    private fun inMainProcess(context: Context): Boolean {
        var packageName = context.packageName
        var processName = getProcessName(context)
        return packageName.equals(processName)
    }

    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    private fun getProcessName(context: Context): String {
        var processName: String? = null

        // ActivityManager
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        while (true) {
            for (info in am.runningAppProcesses) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName

                    break
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName!!
            }

            // take a rest and again
            try {
                Thread.sleep(100L)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }

        }
    }

}