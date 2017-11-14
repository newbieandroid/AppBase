package com.fuyoul.sanwenseller.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.Holder
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reqhttp.ResDefaultImInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResSplash
import com.fuyoul.sanwenseller.configs.Key
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.SplashM
import com.fuyoul.sanwenseller.structure.presenter.SplashP
import com.fuyoul.sanwenseller.structure.view.SplashV
import com.fuyoul.sanwenseller.ui.main.MainActivity
import com.fuyoul.sanwenseller.ui.web.WebViewActivity
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.SpUtils
import com.netease.nim.uikit.NimUIKit
import com.netease.nim.uikit.StatusBarUtils
import kotlinx.android.synthetic.main.splash.*

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

        getPresenter().getData(this)

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