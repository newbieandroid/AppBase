package com.fuyoul.sanwenseller.ui.fragment.appointment

import android.os.Bundle
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.bean.reshttp.ResQuickTestCount
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.structure.model.QuickTestM
import com.fuyoul.sanwenseller.structure.presenter.QuickTestP
import com.fuyoul.sanwenseller.structure.view.QuickTestV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import kotlinx.android.synthetic.main.quicktestfragment.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class QuickTestFragment : BaseFragment<QuickTestM, QuickTestV, QuickTestP>() {


    private var isChanged = false
    private var count = 0

    override fun setLayoutRes(): Int = R.layout.quicktestfragment

    override fun init(view: View?, savedInstanceState: Bundle?) {
        resutInfo.text = "0"
        getPresenter().getCountInfo(context)
    }

    override fun setListener() {

        reduceBtn.setOnClickListener {

            if (count > 0) {
                count--
                resutInfo.text = "$count"
            } else {
                NormalFunUtils.showToast(context, "超过最少接单数")
            }
        }

        addBtn.setOnClickListener {

            if (count < 30) {
                count++
                resutInfo.text = "$count"
            } else {
                NormalFunUtils.showToast(context, "超过最大接单数")
            }
        }
        quickTestBtn.setOnClickListener {

            getPresenter().upDateCountInfo(context, count, isChanged)

        }
    }

    override fun getPresenter(): QuickTestP = QuickTestP(initViewImpl())

    override fun initViewImpl(): QuickTestV = object : QuickTestV() {
        override fun setIsChangeState(isChanged: Boolean) {
            this@QuickTestFragment.isChanged = isChanged
        }

        override fun setViewInfo(data: ResQuickTestCount) {

            count = data.maxOrdersCount
            resutInfo.text = "${data.maxOrdersCount}"
            releaseCount.text = "${data.restOrdersCount}"

            setIsChangeState(data.isChanged == Code.ISCHANGED)
        }
    }
}