package com.fuyoul.sanwenseller.ui

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.configs.Action
import com.fuyoul.sanwenseller.configs.Key.wxLoginResult
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.LoginM
import com.fuyoul.sanwenseller.structure.presenter.LoginP
import com.fuyoul.sanwenseller.structure.view.LoginV
import com.fuyoul.sanwenseller.ui.normal.AgreeMentActivity
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import kotlinx.android.synthetic.main.loginactivity.*
import org.litepal.crud.DataSupport
import java.util.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
class LoginActivity : BaseActivity<LoginM, LoginV, LoginP>() {


    private var isDestory = false


    companion object {
        /**
         * @param isAutoJump是否自动跳转到登录页
         */
        fun checkLogin(isAutoJump: Boolean, activity: Activity): Boolean {

            if (DataSupport.findFirst(ResLoginInfoBean::class.java) != null) {
                return true
            }
            if (isAutoJump) {
                NormalFunUtils.showToast(activity, "请登录")
                activity.startActivity(Intent(activity, LoginActivity::class.java).putExtra("isAutoJump", isAutoJump))
            }
            return false

        }
    }


    override fun initTopBar(): TopBarOption {

        val topBar = TopBarOption()
        topBar.isShowBar = true
        topBar.mainTitle = "登录"

        return topBar
    }

    override fun setLayoutRes(): Int = R.layout.loginactivity

    override fun initData(savedInstanceState: Bundle?) {

        val filter = IntentFilter()
        filter.addAction(Action.ACTION_WXLOGIN)
        this.registerReceiver(receiver, filter)


        val tips = agreeTips.text
        val sps = SpannableString(tips)
        sps.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_3CC5BC)), tips.length - 8, tips.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sps.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View?) {
                startActivity(Intent(this@LoginActivity, AgreeMentActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint?) {
                ds!!.isLinearText = false
                ds.bgColor = resources.getColor(R.color.color_white)

            }

        }, tips.length - 8, tips.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        agreeTips.text = sps
        agreeTips.movementMethod = LinkMovementMethod.getInstance()

    }

    override fun setListener() {

        getSmsCode.setOnClickListener {
            getPresenter().getSms(this, etAccount.text.toString())
        }

        loginBtn.setOnClickListener {
            getPresenter().doLogin(this, null, etAccount.text.toString(), etPass.text.toString())
        }

        wChatLayout.setOnClickListener {
            getPresenter().wxLogin(this)
        }
    }

    override fun getPresenter(): LoginP = LoginP(initViewImpl())

    override fun initViewImpl(): LoginV = object : LoginV() {
        override fun deleteDbInfo() {
            DataSupport.deleteAll(ResLoginInfoBean::class.java)
        }

        override fun isCanSendSms(): Boolean = TextUtils.equals("获取验证码", getSmsCode.text)

        override fun saveDbInfo(loginInfo: ResLoginInfoBean) {

            val dbInfo: ResLoginInfoBean? = DataSupport.findFirst(ResLoginInfoBean::class.java)

            if (dbInfo == null) {
                loginInfo.save()
            } else {
                loginInfo.updateAll()
            }

        }

        override fun changeGetSmsState() {

            if (isCanSendSms()) {
                var time = 60//倒计时
                Timer().schedule(object : TimerTask() {
                    override fun run() {


                        if (!isDestory) {


                            runOnUiThread {

                                if (time == 0) {
                                    cancel()
                                    getSmsCode.text = "获取验证码"

                                } else {
                                    getSmsCode.text = "稍等${time}秒"
                                    time--
                                }

                            }
                        }

                    }

                }, 0, 1000)
            }

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        isDestory = true
        unregisterReceiver(receiver)
    }


    private var receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            if (p1?.action == Action.ACTION_WXLOGIN) {

                val code = p1.getStringExtra(wxLoginResult)

                getPresenter().doLogin(this@LoginActivity, code, null, null)

            }

        }

    }
}