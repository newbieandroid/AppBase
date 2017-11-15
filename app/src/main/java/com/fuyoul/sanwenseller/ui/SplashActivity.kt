package com.fuyoul.sanwenseller.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.alibaba.fastjson.JSON
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.Holder
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reqhttp.ResDefaultImInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResSplash
import com.fuyoul.sanwenseller.configs.Key
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.im.helper.SessionHelper
import com.fuyoul.sanwenseller.im.session.activity.avchat.AVChatActivity
import com.fuyoul.sanwenseller.im.session.activity.avchat.AVChatProfile
import com.fuyoul.sanwenseller.im.session.activity.avchat.Extras
import com.fuyoul.sanwenseller.structure.model.SplashM
import com.fuyoul.sanwenseller.structure.presenter.SplashP
import com.fuyoul.sanwenseller.structure.view.SplashV
import com.fuyoul.sanwenseller.ui.main.MainActivity
import com.fuyoul.sanwenseller.ui.web.WebViewActivity
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.SpUtils
import com.fuyoul.sanwenseller.utils.SysInfoUtil
import com.netease.nim.uikit.NimUIKit
import com.netease.nim.uikit.StatusBarUtils
import com.netease.nimlib.sdk.NimIntent
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import kotlinx.android.synthetic.main.splash.*
import java.util.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
class SplashActivity : BaseActivity<SplashM, SplashV, SplashP>() {


    override fun initTopBar(): TopBarOption = TopBarOption()

    override fun setLayoutRes(): Int = R.layout.splash

    override fun initData(savedInstanceState: Bundle?) {

        StatusBarUtils.setTransparentForImageView(this, null)
        StatusBarUtils.StatusBarLightMode(this, R.color.color_white)


        if (savedInstanceState != null) {
            intent = Intent()// 从堆栈恢复，不再重复解析之前的intent
        }

        val intent = intent


        Log.e("csl", "启动页收到的消息:\n${JSON.toJSONString(intent)}")


        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            parseNotifyIntent(intent)
            finish()
        } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P) || intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
            onParseIntent(intent)
            finish()
        } else {
            if (!SysInfoUtil.stackResumed(this)) {
                getPresenter().getData(this)
            }
        }


    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.clear()
    }

    //处理通知栏的请求
    private fun parseNotifyIntent(intent: Intent) {
        val messages = intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT) as ArrayList<IMMessage>
        if (messages == null || messages.size > 1) {
            onParseIntent(null)
        } else {
            val intent = Intent().putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, messages[0])
            onParseIntent(intent)
        }

    }


    //处理请求
    private fun onParseIntent(intent: Intent?) {

        if (intent == null) {
            return
        }


        Log.e("csl", "收到的通知：" + JSON.toJSONString(intent.extras))

        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            val message = getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT) as ArrayList<IMMessage>
            when (message[0].sessionType) {
                SessionTypeEnum.P2P -> SessionHelper.startP2PSession(this, message[0].sessionId)
            }
        } else if (intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
            if (AVChatProfile.getInstance().isAVChatting) {
                val localIntent = Intent()
                localIntent.setClass(this, AVChatActivity::class.java)
                startActivity(localIntent)
            }
        } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P)) {
            val data = intent.getParcelableExtra<Intent>(Extras.EXTRA_DATA)
            val account = data.getStringExtra(Extras.EXTRA_ACCOUNT)
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account)
            }
        }
    }

    override fun setListener() {

        splashJumpBtn.setOnClickListener {
            getPresenter().viewImpl?.gotoMain()
        }
    }

    override fun getPresenter(): SplashP = SplashP(initViewImpl())

    override fun initViewImpl(): SplashV = object : SplashV() {
        override fun gotoMain() {

            SpUtils.putBoolean(Key.isFirst, false)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }


        override fun setImgs(imgs: List<ResSplash.ImageBean>) {

            if (SpUtils.getBoolean(Key.isFirst) && imgs.size == 1) {
                splashJumpBtn.visibility = View.VISIBLE
            }

            splashBanner.isCanLoop = false
            splashBanner.setPointViewVisible(false)
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setOnItemClickListener {
                        WebViewActivity.startWebView(this@SplashActivity, imgs[it].note, imgs[it].jumpAddress)
                    }
                    .setPages({ ImageHolderView() }, imgs).onPageChangeListener = object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {

                    if (imgs.size - 1 == position) {
                        splashJumpBtn.visibility = View.VISIBLE
                    } else {
                        splashJumpBtn.visibility = View.GONE
                    }
                }

            }

        }

        override fun setContactInfo(contacts: List<ResDefaultImInfo>) {

            (0 until contacts.size)
                    .map { contacts[it] }
                    .forEach {
                        when (it.type) {

                            NimUIKit.SYSCONTACTTYPE -> {
                                NimUIKit.NOTIFYCONTACTID = it.account
                            }

                            NimUIKit.ACTIVITYCONTACTTYPE -> {
                                NimUIKit.ACTIVITYCONTACTID = it.account
                            }

                            NimUIKit.KFCONTACTTYPE -> {
                                NimUIKit.SERVICECONTACTID = it.account
                            }

                        }
                    }

        }


    }

    inner class ImageHolderView : Holder<ResSplash.ImageBean> {

        private var imageView: ImageView? = null
        override fun createView(context: Context?): View {

            imageView = ImageView(context)
            imageView!!.scaleType = ImageView.ScaleType.FIT_XY

            return imageView!!
        }

        override fun UpdateUI(context: Context?, position: Int, data: ResSplash.ImageBean?) {

            GlideUtils.loadNormalImg(context!!, data?.imgs, imageView!!)
            if (!SpUtils.getBoolean(Key.isFirst)) {
                splashJumpBtn.visibility = View.VISIBLE
            }
        }
    }

}