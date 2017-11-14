package com.fuyoul.sanwenseller.ui.money

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.reshttp.ResHistorySettlementTime
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.configs.UrlInfo
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.lzy.okgo.OkGo
import kotlinx.android.synthetic.main.historysettlementtimeactivity.*

/**
 *  @author: chen
 *  @CreatDate: 2017\11\14 0014
 *  @Desc:提示结算的日期列表
 */
class HistorySettlementTimeActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    private var adapter: ThisAdapter? = null

    override fun setLayoutRes(): Int = R.layout.historysettlementtimeactivity

    override fun initData(savedInstanceState: Bundle?) {
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        adapter = ThisAdapter()
        historySettlementTimeInfo.layoutManager = manager
        historySettlementTimeInfo.adapter = adapter!!



        OkGo.post<ResHttpResult>(UrlInfo.SETTLEMENTDATE).execute(object : HttpReqListener(this) {
            override fun reqOk(result: ResHttpResult) {


                val listData = ArrayList<ResHistorySettlementTime>()


                val jsonArray = JSON.parseArray(result.data.toString(), ResHistorySettlementTime::class.java)

                var indexYear = "0"
                var indexMonth = "0"

                for (index in 0 until jsonArray.size) {

                    val item = jsonArray[index]

                    var year = ""
                    var month = ""

                    if (item.ordersDate.length == 6) {
                        year = item.ordersDate.trim().substring(0, 4)
                        month = item.ordersDate.trim().substring(4, 6)
                    } else {
                        year = item.ordersDate.trim().substring(0, 4)
                        month = item.ordersDate.trim().substring(4, 5)
                    }

                    if (index == 0) {
                        indexYear = year
                        indexMonth = month
                        item.showType = Code.BOTHSHOW
                        listData.add(item)
                    } else {
                        if (TextUtils.equals(year, indexYear)) {
                            if (!TextUtils.equals(month, indexMonth)) {
                                item.showType = Code.SINGLEBOTTOM
                                indexMonth = month
                                listData.add(item)
                            }
                        } else {
                            item.showType = Code.BOTHSHOW
                            indexYear = year
                            indexMonth = month
                            listData.add(item)
                        }
                    }

                    item.year = year
                    item.month = month

                }


                when {
                    listData.isEmpty() -> {
                        adapter?.setEmptyView(R.layout.emptylayout)
                    }
                    listData.size == 1 -> {
                        listData[0].showType = Code.BOTHSHOW
                        adapter?.setData(true, listData)
                    }
                    else -> {
                        adapter?.setData(true, listData)
                    }
                }


            }

            override fun withoutData(code: Int, msg: String) {
                NormalFunUtils.showToast(this@HistorySettlementTimeActivity, msg)
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(this@HistorySettlementTimeActivity, errorInfo)
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
        op.mainTitle = "历史结算"
        return op
    }

    inner class ThisAdapter : BaseAdapter(this) {
        override fun convert(holder: BaseViewHolder, position: Int, allDatas: List<MultBaseBean>) {
            val item = allDatas[position] as ResHistorySettlementTime

            val settlementOfYear = holder.itemView.findViewById<TextView>(R.id.settlementOfYear)
            val settlementOfMonth = holder.itemView.findViewById<TextView>(R.id.settlementOfMonth)


            when (item.showType) {
                Code.BOTHSHOW -> {
                    settlementOfYear.visibility = View.VISIBLE
                    settlementOfMonth.visibility = View.VISIBLE
                    settlementOfYear.text = item.year
                    settlementOfMonth.text = item.month
                }
                Code.SINGLETOP -> {
                    settlementOfYear.visibility = View.VISIBLE
                    settlementOfMonth.visibility = View.GONE
                    settlementOfYear.text = item.year

                }
                Code.SINGLEBOTTOM -> {
                    settlementOfYear.visibility = View.GONE
                    settlementOfMonth.visibility = View.VISIBLE
                    settlementOfMonth.text = item.month
                }
            }

        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {
            multiItems.add(AdapterMultiItem(Code.VIEWTYPE_SETTLEMENT, R.layout.yearofsettlementlayout))
        }

        override fun onItemClicked(view: View, position: Int) {

            val item = datas[position] as ResHistorySettlementTime

            if (item.showType != Code.SINGLETOP) {

                SettlementTypeActivity.start(this@HistorySettlementTimeActivity, true, item.year, item.month)
            }
        }

        override fun onEmpryLayou(view: View, layoutResId: Int) {
        }

        override fun getSmartRefreshLayout(): SmartRefreshLayout = SmartRefreshLayout(this@HistorySettlementTimeActivity)

        override fun getRecyclerView(): RecyclerView = historySettlementTimeInfo

    }


}