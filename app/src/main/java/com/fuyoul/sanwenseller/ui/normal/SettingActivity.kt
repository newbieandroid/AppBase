package com.fuyoul.sanwenseller.ui.normal

import android.content.Intent
import android.os.Bundle
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.helper.LoginOutHelper
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import kotlinx.android.synthetic.main.setting.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\28 0028
 *  @Desc:
 */
class SettingActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {
    override fun setLayoutRes(): Int = R.layout.setting

    override fun initData(savedInstanceState: Bundle?) {
        versionText.text = "${this.packageManager.getPackageInfo(this.packageName, 0).versionName}"
    }

    override fun setListener() {

        aboutAppBtn.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }

        helpBtn.setOnClickListener {
            WebViewActivity.startWebView(this, "帮助", "https://www.baidu.com")
        }

        exitBtn.setOnClickListener {


            MsgDialogHelper.showNormalDialog(this, true, "退出登录", "退出登录后，将无法收到消息提醒，确定退出吗？", object : MsgDialogHelper.DialogListener {
                override fun onPositive() {
                    LoginOutHelper.accountLoginOut(this@SettingActivity, false)
                }

                override fun onNagetive() {
                }

            })

        }
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {

        val op = TopBarOption()

        op.isShowBar = true
        op.mainTitle = "设置"
        return op

    }
}