package com.app.base.ui

import android.os.Bundle
import com.app.base.base.BaseActivity
import com.app.base.R
import com.app.base.bean.reshttp.ResLoginInfoBean
import com.app.base.listener.LoginBackListener
import com.app.base.structure.model.LoginM
import com.app.base.structure.presenter.LoginP
import com.app.base.structure.view.LoginV
import kotlinx.android.synthetic.main.activity_main.*
import org.litepal.crud.DataSupport

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class MainActivity : BaseActivity<LoginM, LoginV, LoginP>() {

    override fun setLayoutRes(): Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {

        val b = DataSupport.findFirst(ResLoginInfoBean::class.java)

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


}