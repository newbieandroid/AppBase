package com.fuyoul.sanwenseller.ui.money

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
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
import com.fuyoul.sanwenseller.configs.Data
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.listener.AppBarStateChangeListener
import com.fuyoul.sanwenseller.structure.model.MoneyInComeM
import com.fuyoul.sanwenseller.structure.presenter.MoneyInComeP
import com.fuyoul.sanwenseller.structure.view.MoneyInComeV
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.TimeDateUtils
import com.netease.nim.uikit.NimUIKit
import com.netease.nim.uikit.StatusBarUtils
import kotlinx.android.synthetic.main.moneyinfolayout.*
import kotlinx.android.synthetic.main.waitforsettlementlayout.*
import java.util.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:待结算
 */
class SettlementTypeActivity : BaseActivity<MoneyInComeM, MoneyInComeV, MoneyInComeP>() {


    companion object {

        fun start(context: Context, isHistoryType: Boolean, year: String?, month: String?) {
            val intent = Intent(context, SettlementTypeActivity::class.java)
            intent.putExtra("isHistoryType", isHistoryType)
            if (isHistoryType) {
                intent.putExtra("year", year)
                intent.putExtra("month", month)
            }
            context.startActivity(intent)
        }
    }

    private val req = ReqInCome()
    private var index = 0

    private var defaultState = AppBarStateChangeListener.State.EXPANDED//默认是展开状态

    private var adapter: ThisAdapter? = null

    override fun setLayoutRes(): Int = R.layout.waitforsettlementlayout

    override fun initData(savedInstanceState: Bundle?) {
        StatusBarUtils.setTranslucentForCoordinatorLayout(this, 30)
        setSupportActionBar(toolbar)


        //如果是结算历史
        if (intent.getBooleanExtra("isHistoryType", false)) {
            req.ordersDate = "${intent.getStringExtra("year")}${intent.getStringExtra("month")}"
        }

        getPresenter().viewImpl?.initAdapter(this)
        getPresenter().getData(this, req, true)
    }

    override fun setListener() {

        waitForSettlementRefreshLayout.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout?) {
                index = 0
                req.index = "$index"
                getPresenter().getData(this@SettlementTypeActivity, req, true)
            }

            override fun onLoadmore(refreshlayout: RefreshLayout?) {
                index++
                req.index = "$index"
                getPresenter().getData(this@SettlementTypeActivity, req, false)
            }

        })

        appBarLayoutOfWaitSettlement.addOnOffsetChangedListener(listener)

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        appBarLayoutOfWaitSettlement.removeOnOffsetChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        appBarLayoutOfWaitSettlement.removeOnOffsetChangedListener(listener)
    }

    override fun getPresenter(): MoneyInComeP = MoneyInComeP(initViewImpl())

    override fun initViewImpl(): MoneyInComeV = object : MoneyInComeV() {
        override fun getAdapter(): BaseAdapter {

            if (adapter == null) {
                adapter = ThisAdapter()
            }
            return adapter!!

        }

        override fun initAdapter(context: Context) {

            val manager = LinearLayoutManager(this@SettlementTypeActivity)
            manager.orientation = LinearLayoutManager.VERTICAL
            waitForSettlementDataList.layoutManager = manager
            waitForSettlementDataList.adapter = getAdapter()
        }

        override fun setViewInfo(data: ResMoneyItem) {

//            if (intent.getBooleanExtra("isHistoryType", false)) {
//                priceCount.text = "${data.totalPrice}"
//                orderCount.text = "${data.totalCount}"
//            } else {
            priceCount.text = "${data.notSccountsPrice}"
            orderCount.text = "${data.notSccountsCount}"
//            }

        }

    }

    override fun initTopBar(): TopBarOption = TopBarOption()


    inner class ThisAdapter : BaseAdapter(this) {
        override fun convert(holder: BaseViewHolder, position: Int, datas: List<MultBaseBean>) {

            val item = datas[position] as ResMoneyItem.IncomeListBean


            GlideUtils.loadNormalImg(this@SettlementTypeActivity, JSON.parseArray(item.imgs).getJSONObject(0).getString("url"), holder.itemView.findViewById(R.id.orderItemIcon))
            GlideUtils.loadNormalImg(this@SettlementTypeActivity, item.avatar, holder.itemView.findViewById(R.id.orderItemHead))

            holder.itemView.findViewById<TextView>(R.id.orderItemPrice).text = "￥${item.totalPrice}"
            holder.itemView.findViewById<TextView>(R.id.orderItemTitle).text = "${item.name}"
            holder.itemView.findViewById<TextView>(R.id.orderItemDes).text = "${item.introduce}"
            holder.itemView.findViewById<TextView>(R.id.orderItemTime).text = "${TimeDateUtils.stampToDate(item.ordersDate)}"
            holder.itemView.findViewById<TextView>(R.id.orderItemNick).text = "${item.nickname}"


            var typeIcon: Drawable? = null
            if (item.type == Data.QUICKTESTBABY) {
                typeIcon = resources.getDrawable(R.mipmap.icon_quicktesttype)
                typeIcon.setBounds(0, 0, typeIcon.minimumWidth, typeIcon.minimumHeight)
            }

            holder.itemView.findViewById<TextView>(R.id.orderItemTitle).setCompoundDrawables(null, null, typeIcon, null)


            val orderItemState = holder.itemView.findViewById<TextView>(R.id.orderItemState)
            orderItemState.text = "交易完成"

            val orderItemFuncLayout = holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout)
            orderItemFuncLayout.visibility = View.VISIBLE


            val orderItemReleaseTime = holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime)
            orderItemReleaseTime.visibility = View.GONE


            val orderItemFuncRight = holder.itemView.findViewById<TextView>(R.id.orderItem_Func_Right)
            orderItemFuncRight.text = "联系买家"
            orderItemFuncRight.setOnClickListener {
                NimUIKit.startP2PSession(this@SettlementTypeActivity, "user_${item.user_info_id}")
            }


        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {

            multiItems.add(AdapterMultiItem(Code.VIEWTYPE_MONEY, R.layout.orderitem))
        }

        override fun onItemClicked(view: View, position: Int) {
        }

        override fun onEmpryLayou(view: View, layoutResId: Int) {
        }

        override fun getSmartRefreshLayout(): SmartRefreshLayout = waitForSettlementRefreshLayout

        override fun getRecyclerView(): RecyclerView = waitForSettlementDataList

    }


    private val listener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {

            when (state) {
                State.EXPANDED -> {
                    //展开状态
                    StatusBarUtils.StatusBarDarkMode(this@SettlementTypeActivity)
                    StatusBarUtils.setTranslucentForCoordinatorLayout(this@SettlementTypeActivity, 30)
                    toolbar.setBackgroundColor(resources.getColor(R.color.transparent))
                }
                State.COLLAPSED -> {
                    //折叠状态
                    toolbarOfMoneyInfo.setNavigationIcon(R.mipmap.ic_yb_top_back)
                    StatusBarUtils.StatusBarLightMode(this@SettlementTypeActivity, R.color.color_white)
                    toolbar.setBackgroundResource(R.drawable.back_titlebar)

                }
                else -> {
                    //中间状态

                    //如果之前是折叠状态,则表示在向下滑动
                    if (defaultState == State.COLLAPSED) {
                        StatusBarUtils.StatusBarDarkMode(this@SettlementTypeActivity)
                        StatusBarUtils.setTranslucentForCoordinatorLayout(this@SettlementTypeActivity, 30)
                        toolbar.setBackgroundColor(resources.getColor(R.color.transparent))
                    }

                }
            }

            defaultState = state

        }

    }
}