package com.fuyoul.sanwenseller.helper

import android.content.Context
import android.content.Intent
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.ui.LoginActivity
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import org.litepal.crud.DataSupport

/**
 *  @author: chen
 *  @CreatDate: 2017\10\28 0028
 *  @Desc:退出登录
 */
object LoginOutHelper {

    /**
     * 返回到桌面
     */
    fun exitApp() {
        ActivityStateHelper.removeAll()
    }

    /**
     * 退出当前账号
     */
    fun accountLoginOut(context: Context, isKitchOut: Boolean) {
        if (DataSupport.findFirst(ResLoginInfoBean::class.java) != null) {
            DataSupport.deleteAll(ResLoginInfoBean::class.java)
            NIMClient.getService(AuthService::class.java).logout()

            val intent = Intent(context, LoginActivity::class.java)
            if (isKitchOut) {
                intent.putExtra("isKitchOut", true)
            }
            context.startActivity(intent)
            ActivityStateHelper.finishAllExcept(LoginActivity::class.java)

        }
    }
}