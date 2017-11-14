package com.fuyoul.sanwenseller.ui.others

import android.os.Bundle
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResRewardRule
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.configs.UrlInfo.REWARDRULE
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.lzy.okgo.OkGo
import kotlinx.android.synthetic.main.rewardrulelayout.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
class RewardRuleActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {
    override fun setLayoutRes(): Int = R.layout.rewardrulelayout

    override fun initData(savedInstanceState: Bundle?) {

        OkGo.post<ResHttpResult>(REWARDRULE).execute(object : HttpReqListener(this) {
            override fun reqOk(result: ResHttpResult) {

                val data = JSON.parseObject(result.data.toString(), ResRewardRule::class.java)
                currentCount.text = "${data.currentMonth}"
                totalCount.text = "${data.threeMonth}"
            }

            override fun withoutData(code: Int, msg: String) {
            }

            override fun error(errorInfo: String) {
            }

        })
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {
        val op = TopBarOption()

        op.isShowBar = true
        op.mainTitle = "奖励制度"
        return op
    }
}