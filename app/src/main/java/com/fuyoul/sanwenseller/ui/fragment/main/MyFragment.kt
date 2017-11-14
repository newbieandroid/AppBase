package com.fuyoul.sanwenseller.ui.fragment.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.ui.LoginActivity
import com.fuyoul.sanwenseller.ui.normal.SettingActivity
import com.fuyoul.sanwenseller.ui.normal.SuggestActivity
import com.fuyoul.sanwenseller.ui.order.AppointMentTimeActivity
import com.fuyoul.sanwenseller.ui.order.MoneyInfoActivity
import com.fuyoul.sanwenseller.ui.order.RewardRuleActivity
import com.fuyoul.sanwenseller.ui.user.UserInfoActivity
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.netease.nim.uikit.StatusBarUtils
import kotlinx.android.synthetic.main.myfragmentlayout.*
import org.litepal.crud.DataSupport

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class MyFragment : BaseFragment<EmptyM, EmptyV, EmptyP>() {

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            StatusBarUtils.setTransparentForImageViewInFragment(activity, contentLayout)
        }
    }

    override fun setLayoutRes(): Int = R.layout.myfragmentlayout

    override fun init(view: View?, savedInstanceState: Bundle?) {
        onHiddenChanged(false)
        setUserInfo()
    }


    private fun setUserInfo() {
        val loginInfo = DataSupport.findFirst(ResLoginInfoBean::class.java)

        if (loginInfo != null) {
            val draw = resources.getDrawable(R.mipmap.ic_yb_ycsicon)
            draw.setBounds(0, 0, draw.minimumWidth, draw.minimumHeight)
            nickName.setCompoundDrawables(null, null, draw, null)
            nickName.text = loginInfo.nickname

            GlideUtils.loadCircleImg(context, loginInfo.avatar, myHeadIcon, false, 0, R.mipmap.ic_my_wdltx, R.mipmap.ic_my_wdltx)

        } else {
            nickName.text = "立即登录"
            nickName.setCompoundDrawables(null, null, null, null)
            GlideUtils.loadCircleImg(context, R.mipmap.ic_my_wdltx, myHeadIcon, false, 0, R.mipmap.ic_my_wdltx, R.mipmap.ic_my_wdltx)

        }
    }

    override fun setListener() {


        rewardRoleLayout.setOnClickListener {

            if (LoginActivity.checkLogin(true, activity)) {
                startActivity(Intent(context, RewardRuleActivity::class.java))
            }

        }

        inMoneyLayout.setOnClickListener {
            if (LoginActivity.checkLogin(true, activity)) {
                startActivity(Intent(context, MoneyInfoActivity::class.java))
            }
        }

        appointmentTimeLayout.setOnClickListener {
            if (LoginActivity.checkLogin(true, activity)) {
                startActivity(Intent(context, AppointMentTimeActivity::class.java))
            }
        }

        editInfoArrorwLayout.setOnClickListener {
            if (LoginActivity.checkLogin(true, activity)) {
                UserInfoActivity.start(activity)
            }
        }

        headLayout.setOnClickListener {
            if (LoginActivity.checkLogin(true, activity)) {
                UserInfoActivity.start(activity)
            }
        }


        reportLayout.setOnClickListener {
            startActivity(Intent(activity, SuggestActivity::class.java))
        }

        settingLayout.setOnClickListener {
            startActivity(Intent(activity, SettingActivity::class.java))
        }

        callLayout.setOnClickListener {
            MsgDialogHelper.showNormalDialog(activity, true, "客服电话", callNumText.text.toString(), "呼叫", object : MsgDialogHelper.DialogListener {
                override fun onNagetive() {
                }

                override fun onPositive() {
                    if (!TextUtils.isEmpty(callNumText.text.toString())) {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${callNumText.text}"))
                        startActivity(intent)
                    } else {
                        NormalFunUtils.showToast(activity, "电话号码不正确")
                    }
                }

            })
        }
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Code.REQ_USERINFO && resultCode == Activity.RESULT_OK) {
            setUserInfo()
        }
    }
}