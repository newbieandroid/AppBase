package com.fuyoul.sanwenseller.ui.order

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.reshttp.ResMoneyItem
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.netease.nim.uikit.StatusBarUtils
import kotlinx.android.synthetic.main.moneyinfolayout.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class MoneyInfoActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {
    override fun setLayoutRes(): Int = R.layout.moneyinfolayout


    override fun initData(savedInstanceState: Bundle?) {

        StatusBarUtils.setTranslucentForCoordinatorLayout(this, 30)


        setSupportActionBar(toolbar)


        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL

        moneyDataList.layoutManager = manager

        val adapter = ThisAdapter()
        moneyDataList.adapter = adapter

        adapter.setData(true, arrayListOf(ResMoneyItem(), ResMoneyItem(), ResMoneyItem(), ResMoneyItem(), ResMoneyItem(), ResMoneyItem(), ResMoneyItem(), ResMoneyItem(), ResMoneyItem()))

    }

    override fun setListener() {

        waitForSettleMentBtn.setOnClickListener {
            startActivity(Intent(this@MoneyInfoActivity, WaitForSettlementActivity::class.java))
        }

        toolbar.setNavigationOnClickListener({
            finish()
        })
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption = TopBarOption()

    inner class ThisAdapter : BaseAdapter(this) {
        override fun convert(holder: BaseViewHolder, position: Int, atas: List<MultBaseBean>) {
        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {

            multiItems.add(AdapterMultiItem(Code.VIEWTYPE_MONEY, R.layout.inmoneyitem))
        }

        override fun onItemClicked(view: View, position: Int) {
        }

        override fun onEmpryLayou(view: View, layoutResId: Int) {
        }

        override fun getSmartRefreshLayout(): SmartRefreshLayout = moneyRefreshLayout

        override fun getRecyclerView(): RecyclerView = moneyDataList

    }
}