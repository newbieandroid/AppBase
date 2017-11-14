package com.fuyoul.sanwenseller.ui.others

import android.os.Bundle
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\28 0028
 *  @Desc:
 */
class AboutUsActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {
    override fun setLayoutRes(): Int = R.layout.aboutus

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {

        val op = TopBarOption()
        op.isShowBar = true
        op.mainTitle = "关于我们"
        return op
    }
}