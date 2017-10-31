package com.fuyoul.sanwenseller.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reqhttp.ReqRegistMasterInfo
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.TagSelectM
import com.fuyoul.sanwenseller.structure.presenter.TagSelectP
import com.fuyoul.sanwenseller.structure.view.TagSelectV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
class TagSelectActivity : BaseActivity<TagSelectM, TagSelectV, TagSelectP>() {


    private var reqRegistMasterInfo: ReqRegistMasterInfo? = null

    companion object {
        fun start(context: Context, reqRegistMasterInfo: ReqRegistMasterInfo) {
            val intent = Intent(context, TagSelectActivity::class.java)
            val bund = Bundle()
            bund.putSerializable("data", reqRegistMasterInfo)
            intent.putExtras(bund)
            context.startActivity(intent)
        }

    }


    override fun setLayoutRes(): Int = R.layout.tagselectlayout

    override fun initData(savedInstanceState: Bundle?) {

        reqRegistMasterInfo = intent.extras.getSerializable("data") as ReqRegistMasterInfo
    }

    override fun setListener() {
    }

    override fun getPresenter(): TagSelectP = TagSelectP(initViewImpl())

    override fun initViewImpl(): TagSelectV = object : TagSelectV() {

    }

    override fun initTopBar(): TopBarOption {
        val op = TopBarOption()

        op.isShowBar = true
        op.mainTitle = "选择您的个人标签"

        return op
    }
}