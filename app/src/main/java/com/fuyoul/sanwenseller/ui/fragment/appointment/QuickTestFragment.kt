package com.fuyoul.sanwenseller.ui.fragment.appointment

import android.os.Bundle
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import kotlinx.android.synthetic.main.quicktestfragment.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class QuickTestFragment : BaseFragment<EmptyM, EmptyV, EmptyP>() {
    override fun setLayoutRes(): Int = R.layout.quicktestfragment

    override fun init(view: View?, savedInstanceState: Bundle?) {
        resutInfo.text = "0"
    }

    override fun setListener() {

        reduceBtn.setOnClickListener {

            var result = resutInfo.text.toString().toInt()

            if (result > 0) {
                result--
                resutInfo.text = "$result"
            } else {
                NormalFunUtils.showToast(context, "超过最少接单数")
            }
        }

        addBtn.setOnClickListener {

            var result = resutInfo.text.toString().toInt()

            if (result < 99) {
                result++
                resutInfo.text = "$result"
            } else {
                NormalFunUtils.showToast(context, "超过最大接单数")
            }

        }


    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()
}