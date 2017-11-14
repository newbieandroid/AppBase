package com.fuyoul.sanwenseller.ui.money

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.csl.refresh.SmartRefreshLayout
import com.csl.refresh.api.RefreshLayout
import com.csl.refresh.listener.OnRefreshLoadmoreListener
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.reqhttp.ReqInCome
import com.fuyoul.sanwenseller.bean.reshttp.ResMoneyItem
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.listener.AppBarStateChangeListener
import com.fuyoul.sanwenseller.structure.model.MoneyInComeM
import com.fuyoul.sanwenseller.structure.presenter.MoneyInComeP
import com.fuyoul.sanwenseller.structure.view.MoneyInComeV
import com.fuyoul.sanwenseller.utils.TimeDateUtils
import com.netease.nim.uikit.StatusBarUtils
import kotlinx.android.synthetic.main.moneyinfolayout.*
import kotlinx.android.synthetic.main.waitforsettlementlayout.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class MoneyInfoActivity : BaseActivity<MoneyInComeM, MoneyInComeV, MoneyInComeP>() {

    private val req = ReqInCome()
    private var index = 0

    private var adapter: ThisAdapter? = null

    private var defaultState = AppBarStateChangeListener.State.EXPANDED//默认是展开状态

    override fun setLayoutRes(): Int = R.layout.moneyinfolayout

    override fun initData(savedInstanceState: Bundle?) {

        StatusBarUtils.setTranslucentForCoordinatorLayout(this, 30)
        setSupportActionBar(toolbar)
        getPresenter().viewImpl?.initAdapter(this)
        getPresenter().getData(this, req, true)

    }

    override fun setListener() {

        moneyRefreshLayout.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout?) {
                index = 0
                req.index = "$index"
                getPresenter().getData(this@MoneyInfoActivity, req, true)
            }

            override fun onLoadmore(refreshlayout: RefreshLayout?) {
                index++
                req.index = "$index"
                getPresenter().getData(this@MoneyInfoActivity, req, false)
            }

        })


        appBarLayoutOfMoneyInfo.addOnOffsetChangedListener(listener)

        waitForSettleMentBtn.setOnClickListener {

            SettlementTypeActivity.start(this@MoneyInfoActivity, false, null, null)

        }

        historySettleMentBtn.setOnClickListener {
            startActivity(Intent(this@MoneyInfoActivity, HistorySettlementTimeActivity::class.java))
        }

        toolbarOfMoneyInfo.setNavigationOnClickListener({
            finish()
        })
    }

    override fun getPresenter(): MoneyInComeP = MoneyInComeP(initViewImpl())

    override fun initViewImpl(): MoneyInComeV = object : MoneyInComeV() {
        override fun setViewInfo(data: ResMoneyItem) {

            inComePriceCount.text = "${data.totalPrice}"
            inComeOrderCount.text = "${data.totalCount}"

            waitForTotalPrice.text = "${data.countPrice}"
            waitForTotalCount.text = "${data.countOrders}"
        }

        override fun getAdapter(): ThisAdapter {

            if (adapter == null) {
                adapter = ThisAdapter()
            }
            return adapter!!
        }

        override fun initAdapter(context: Context) {

            val manager = LinearLayoutManager(this@MoneyInfoActivity)
            manager.orientation = LinearLayoutManager.VERTICAL
            moneyDataList.layoutManager = manager
            moneyDataList.adapter = getAdapter()

        }
    }

    override fun initTopBar(): TopBarOption = TopBarOption()

    inner class ThisAdapter : BaseAdapter(this) {
        override fun convert(holder: BaseViewHolder, position: Int, atas: List<MultBaseBean>) {

            val item = atas[position] as ResMoneyItem.IncomeListBean
            holder.itemView.findViewById<TextView>(R.id.moneyTime).text = TimeDateUtils.stampToDate(item.ordersDate)
            holder.itemView.findViewById<TextView>(R.id.moneyNum).text = "+${item.totalPrice}"

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

    private val listener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {

            when (state) {
                State.EXPANDED -> {
                    //展开状态
                    StatusBarUtils.StatusBarDarkMode(this@MoneyInfoActivity)
                    StatusBarUtils.setTranslucentForCoordinatorLayout(this@MoneyInfoActivity, 30)
                    toolbarOfMoneyInfo.setBackgroundColor(resources.getColor(R.color.transparent))

                }
                State.COLLAPSED -> {
                    //折叠状态

                    toolbarOfMoneyInfo.setNavigationIcon(R.mipmap.ic_yb_top_back)
                    StatusBarUtils.StatusBarLightMode(this@MoneyInfoActivity, R.color.color_white)
                    toolbarOfMoneyInfo.setBackgroundResource(R.drawable.back_titlebar)
                }
                else -> {
                    //中间状态

                    //如果之前是折叠状态,则表示在向下滑动
                    if (defaultState == State.COLLAPSED) {
                        StatusBarUtils.StatusBarDarkMode(this@MoneyInfoActivity)
                        StatusBarUtils.setTranslucentForCoordinatorLayout(this@MoneyInfoActivity, 30)
                        toolbarOfMoneyInfo.setBackgroundColor(resources.getColor(R.color.transparent))
                    }

                }
            }

            defaultState = state

        }

    }

    override fun onStop() {
        super.onStop()
        appBarLayoutOfMoneyInfo.removeOnOffsetChangedListener(listener)
    }

    override fun onResume() {
        super.onResume()
        appBarLayoutOfMoneyInfo.addOnOffsetChangedListener(listener)
    }
}