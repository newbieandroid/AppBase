package com.fuyoul.sanwenseller.ui

import android.os.Bundle
import com.csl.share.bean.ShareBean
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseShareActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import kotlinx.android.synthetic.main.activity_main.*

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
class MainActivity : BaseShareActivity<EmptyM, EmptyV, EmptyP>() {
    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption = TopBarOption()


    override fun setLayoutRes(): Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {

//        val shabean = ShareBean()
//        shabean.imgPath = "http://images2015.cnblogs.com/blog/684826/201612/684826-20161210185433866-1304570791.png"
//        shabean.description = "描述内容"
//        shabean.shareTitle = "标题"
//        shabean.url = "http://www.baidu.com"
//        doShare(shabean)

        test.startAnimator()
    }

    override fun setListener() {
    }


}