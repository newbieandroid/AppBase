package com.fuyoul.sanwenseller.ui

import android.os.Bundle
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.base.BaseShareActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.ui.fragment.main.BabyManagerFragment
import com.fuyoul.sanwenseller.ui.fragment.main.MainFragment
import com.fuyoul.sanwenseller.ui.fragment.main.MyFragment
import com.fuyoul.sanwenseller.utils.AddFragmentUtils
import com.fuyoul.sanwenseller.utils.StatusBarUtils
import com.netease.nim.uikit.recent.RecentContactsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.includetopbar.*

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class MainActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    private var currentTag = ""
    private val fragments = arrayListOf(MainFragment(), BabyManagerFragment(), RecentContactsFragment(), MyFragment())

    private var addFragmentUtils: AddFragmentUtils? = null

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption = TopBarOption()

    override fun setLayoutRes(): Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {

        StatusBarUtils.setTranslucentForImageView(this, titBarLayout)
        StatusBarUtils.StatusBarLightMode(this, R.color.color_white)

        addFragmentUtils = AddFragmentUtils(this, R.id.mainContentLayout)

        currentTag = fragments[0].javaClass.name
        addFragmentUtils?.showFragment(fragments[0], currentTag)

    }

    override fun setListener() {

        mainBottomLayout.setOnCheckedChangeListener { radioGroup, i ->

            when (i) {
                R.id.mainItem -> {
                    msgItem.isChecked = false
                    currentTag = fragments[0].javaClass.name
                    addFragmentUtils?.showFragment(fragments[0], currentTag)
                }
                R.id.babyItem -> {
                    msgItem.isChecked = false
                    currentTag = fragments[1].javaClass.name
                    addFragmentUtils?.showFragment(fragments[1], currentTag)
                }
                R.id.msgItem -> {
                    currentTag = fragments[2].javaClass.name
                    addFragmentUtils?.showFragment(fragments[2], currentTag)
                }
                R.id.myItem -> {
                    msgItem.isChecked = false
                    currentTag = fragments[3].javaClass.name
                    addFragmentUtils?.showFragment(fragments[3], currentTag)
                }
            }

        }

        msgItem.setOnCheckedChangeListener { _, b ->


            if (b) {
                mainItem.isChecked = false
                babyItem.isChecked = false
                myItem.isChecked = false
                mainBottomLayout.check(R.id.msgItem)

            }
        }
    }


}