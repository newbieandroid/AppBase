package com.fuyoul.sanwenseller.im.conifig

import android.app.Notification
import android.util.SparseArray
import com.netease.nimlib.sdk.StatusBarNotificationConfig

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
object ImConfigs {

    val notifications = SparseArray<Notification>()

    var notificationConfig: StatusBarNotificationConfig? = null
}