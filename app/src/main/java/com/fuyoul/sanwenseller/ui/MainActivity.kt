package com.fuyoul.sanwenseller.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.ui.baby.EditBabyInfoActivity
import com.fuyoul.sanwenseller.ui.fragment.main.BabyManagerFragment
import com.fuyoul.sanwenseller.ui.fragment.main.MainFragment
import com.fuyoul.sanwenseller.ui.fragment.main.MyFragment
import com.fuyoul.sanwenseller.utils.AddFragmentUtils
import com.netease.nim.uikit.StatusBarUtils
import com.netease.nim.uikit.recent.RecentContactsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.includetopbar.*
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.annotation.SuppressLint
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnNeverAskAgain

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
@RuntimePermissions
class MainActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    private var currentTag = ""
    private val fragments = arrayListOf(MainFragment(), BabyManagerFragment(), RecentContactsFragment(), MyFragment())

    private var addFragmentUtils: AddFragmentUtils? = null

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption = TopBarOption()

    override fun setLayoutRes(): Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        toolbarBack.visibility = View.GONE

        noThingWithPermissionCheck()

        addFragmentUtils = AddFragmentUtils(this, R.id.mainContentLayout)
    }

    override fun setListener() {

        mainBottomLayout.setOnCheckedChangeListener { radioGroup, i ->

            when (i) {
                R.id.mainItem -> {
                    titBarLayout.visibility = View.VISIBLE
                    toolbarTitle.text = "三问"
                    toolbarChildTitle.visibility = View.GONE
                    StatusBarUtils.setTranslucentForImageView(this, titBarLayout)
                    StatusBarUtils.StatusBarLightMode(this, R.color.color_white)
                    msgItem.isChecked = false
                    currentTag = fragments[0].javaClass.name
                    addFragmentUtils?.showFragment(fragments[0], currentTag)
                }
                R.id.babyItem -> {
                    titBarLayout.visibility = View.VISIBLE
                    toolbarChildTitle.visibility = View.VISIBLE
                    toolbarTitle.text = "宝贝管理"
                    toolbarChildTitle.text = "发布宝贝"
                    toolbarChildTitle.setTextColor(resources.getColor(R.color.color_3CC5BC))
                    toolbarChildTitle.setOnClickListener {
                        EditBabyInfoActivity.start(this, null)
                    }

                    StatusBarUtils.setTranslucentForImageView(this, titBarLayout)
                    StatusBarUtils.StatusBarLightMode(this, R.color.color_white)
                    msgItem.isChecked = false
                    currentTag = fragments[1].javaClass.name
                    addFragmentUtils?.showFragment(fragments[1], currentTag)
                }
                R.id.msgItem -> {

                    titBarLayout.visibility = View.VISIBLE
                    toolbarTitle.text = "消息"
                    toolbarChildTitle.visibility = View.GONE
                    StatusBarUtils.setTranslucentForImageView(this, titBarLayout)
                    StatusBarUtils.StatusBarLightMode(this, R.color.color_white)
                    currentTag = fragments[2].javaClass.name
                    addFragmentUtils?.showFragment(fragments[2], currentTag)
                }
                R.id.myItem -> {
                    titBarLayout.visibility = View.GONE
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

        mainItem.isChecked = true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        addFragmentUtils?.currentFragment?.onActivityResult(requestCode, resultCode, data)
    }


    @NeedsPermission(Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun noThing() {
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun noThingR(request: PermissionRequest) {

        MsgDialogHelper.showSingleDialog(this@MainActivity, false, "温馨提示", "缺少必要权限,是否进行授权?", "确定", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {
                request.proceed()
            }

            override fun onNagetive() {
                request.cancel()
            }
        })

    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun noThingD() {
        NormalFunUtils.showToast(this@MainActivity, "缺少必要权限,请前往权限管理中心开启对应权限")
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun noThingN() {
        NormalFunUtils.showToast(this@MainActivity, "缺少必要权限,请前往权限管理中心开启对应权限")
    }
}