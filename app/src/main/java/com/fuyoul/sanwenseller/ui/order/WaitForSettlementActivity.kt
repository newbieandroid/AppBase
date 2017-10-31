package com.fuyoul.sanwenseller.ui.order

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.reshttp.ResSettlementItem
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.WaitForSettlementM
import com.fuyoul.sanwenseller.structure.presenter.WaitForSettlementP
import com.fuyoul.sanwenseller.structure.view.WaitForSettlementV
import com.netease.nim.uikit.StatusBarUtils
import kotlinx.android.synthetic.main.waitforsettlementlayout.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:待结算
 */
class WaitForSettlementActivity : BaseActivity<WaitForSettlementM, WaitForSettlementV, WaitForSettlementP>() {


    private var adapter: ThisAdapter? = null

    override fun setLayoutRes(): Int = R.layout.waitforsettlementlayout

    override fun initData(savedInstanceState: Bundle?) {
        StatusBarUtils.setTranslucentForCoordinatorLayout(this, 30)
        setSupportActionBar(toolbar)

        initViewImpl().initRecycleView()

        getPresenter().getData(true, this)
    }

    override fun setListener() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun getPresenter(): WaitForSettlementP = WaitForSettlementP(initViewImpl())

    override fun initViewImpl(): WaitForSettlementV = object : WaitForSettlementV() {
        override fun initRecycleView() {
            val manager = LinearLayoutManager(this@WaitForSettlementActivity)
            manager.orientation = LinearLayoutManager.VERTICAL
            waitForSettlementDataList.layoutManager = manager
            waitForSettlementDataList.adapter = getAdatper()
        }

        override fun getAdatper(): ThisAdapter {

            if (adapter == null) {
                adapter = ThisAdapter()
            }

            return adapter!!
        }

    }

    override fun initTopBar(): TopBarOption = TopBarOption()


    inner class ThisAdapter : BaseAdapter(this) {
        override fun convert(holder: BaseViewHolder, position: Int, datas: List<MultBaseBean>) {

            val item = datas[position] as ResSettlementItem


            val orderItemState = holder.itemView.findViewById<TextView>(R.id.orderItemState)
            orderItemState.text = "交易完成"

            val orderItemFuncLayout = holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout)
            orderItemFuncLayout.visibility = View.VISIBLE
            val orderItemReleaseTime = holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime)
            orderItemReleaseTime.visibility = View.GONE


            val orderItemFuncRight = holder.itemView.findViewById<TextView>(R.id.orderItem_Func_Right)
            orderItemFuncRight.text = "联系买家"
            orderItemFuncRight.setOnClickListener {
                //TODO 联系买家
            }


        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {

            multiItems.add(AdapterMultiItem(Code.VIEWTYPE_WAITSETTLEMENT, R.layout.orderitem))
        }

        override fun onItemClicked(view: View, position: Int) {
        }

        override fun onEmpryLayou(view: View, layoutResId: Int) {
        }

        override fun getSmartRefreshLayout(): SmartRefreshLayout = waitForSettlementRefreshLayout

        override fun getRecyclerView(): RecyclerView = waitForSettlementDataList

    }
}