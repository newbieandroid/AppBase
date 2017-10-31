package com.fuyoul.sanwenseller.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class AppointMentInfoActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    companion object {

        val USERINFOID = "userInfoId"

        fun start(context: Context, userInfoId: Long) {
            context.startActivity(Intent(context, AppointMentInfoActivity::class.java).putExtra(USERINFOID, userInfoId))
        }
    }

    override fun setLayoutRes(): Int = R.layout.appointmentinfo

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {

        val op = TopBarOption()
        op.isShowBar = true
        op.mainTitle = "预约信息"
        return op
    }
}