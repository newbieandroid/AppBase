package com.app.base.ui

import android.os.Bundle
import com.app.base.base.BaseActivity
import com.app.base.R
import com.app.base.listener.LoginBackListener
import com.app.base.structure.model.LoginM
import com.app.base.structure.presenter.LoginP
import com.app.base.structure.view.LoginV
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.annotation.SuppressLint
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
class MainActivity : BaseActivity<LoginM, LoginV, LoginP>() {

    override fun setLayoutRes(): Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {

        aaWithPermissionCheck()
    }

    override fun setListener() {
        login.setOnClickListener {


            getPresenter().doLogin(object : LoginBackListener {
                override fun loginOk() {
                }

                override fun loginError() {
                }

            })
        }

        getSms.setOnClickListener {
            getPresenter().getSms()
        }
    }

    override fun initViewImpl(): LoginV = object : LoginV() {

        override fun showDialog() {
        }

        override fun dismissDialog() {
        }

    }

    override fun getPresenter(): LoginP = LoginP(initViewImpl())


    @NeedsPermission(Manifest.permission.CAMERA)
    fun aa() {
    }


    @OnShowRationale(Manifest.permission.CAMERA)
    fun bb(request: PermissionRequest) {
        request.proceed()
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun cc() {
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun dd() {
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)

    }
}