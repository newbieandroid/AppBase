package com.fuyoul.sanwenseller.wxapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.csl.share.OpenSdkHelper
import com.fuyoul.sanwenseller.configs.Action
import com.fuyoul.sanwenseller.configs.Key.wxLoginResult
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        OpenSdkHelper.wxApi?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        OpenSdkHelper.wxApi?.handleIntent(intent, this)
    }

    override fun onResp(p0: BaseResp?) {

        when (p0?.errCode) {

            BaseResp.ErrCode.ERR_OK -> {

                when {
                    p0.type == ConstantsAPI.COMMAND_SENDAUTH -> {
                        //这里必须强转
                        // 官网文档说明：用户点击授权后，微信客户端会被拉起，跳转至授权界面，用户在该界面点击允许或取消，SDK通过SendAuth的Resp返回数据给调用方。
                        val resp = p0 as SendAuth.Resp
                        sendBroadcast(Intent().putExtra(wxLoginResult, resp.code).setAction(Action.ACTION_WXLOGIN))
                    }
                    p0.type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> //分享
                        Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show()
                }
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                when {
                    p0.type == ConstantsAPI.COMMAND_SENDAUTH -> //登录
                        Toast.makeText(this, "用户取消登录", Toast.LENGTH_SHORT).show()
                    p0.type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> //分享
                        Toast.makeText(this, "用户取消分享", Toast.LENGTH_SHORT).show()

                }
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                when {
                    p0.type == ConstantsAPI.COMMAND_SENDAUTH -> //登录
                        Toast.makeText(this, "用户拒绝登录授权", Toast.LENGTH_SHORT).show()
                    p0.type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> //分享
                        Toast.makeText(this, "用户拒绝分享", Toast.LENGTH_SHORT).show()
                }
            }
        }

        finish()
    }

    override fun onReq(p0: BaseReq?) {
    }
}