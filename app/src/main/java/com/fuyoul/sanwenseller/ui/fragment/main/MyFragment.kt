package com.fuyoul.sanwenseller.ui.fragment.main

import android.os.Bundle
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class MyFragment : BaseFragment<EmptyM, EmptyV, EmptyP>() {
    override fun setLayoutRes(): Int = R.layout.orderlayout

    override fun init(view: View?, savedInstanceState: Bundle?) {
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()
}