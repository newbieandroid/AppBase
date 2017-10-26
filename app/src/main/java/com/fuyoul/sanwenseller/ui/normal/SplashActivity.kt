package com.fuyoul.sanwenseller.ui.normal

import android.os.Bundle
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
class SplashActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {
    override fun initTopBar(): TopBarOption = TopBarOption()

    override fun setLayoutRes(): Int = R.layout.splash

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

}