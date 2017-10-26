package com.fuyoul.sanwenseller.ui.fragment.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseRefreshFragmen
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.bean.reshttp.ResOrderItem
import com.fuyoul.sanwenseller.configs.Code.VIEWTYPE_ORDER
import com.fuyoul.sanwenseller.structure.model.OrderM
import com.fuyoul.sanwenseller.structure.presenter.OrderP
import com.fuyoul.sanwenseller.structure.view.OrderV
import kotlinx.android.synthetic.main.ordertypelayout.*
import org.litepal.crud.DataSupport

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class OrderItemFragment : BaseRefreshFragmen<OrderM, OrderV, OrderP>() {
    override fun getRefreshLayout(): SmartRefreshLayout = orderItemRefreshLayout
    private var loginInfo = DataSupport.findFirst(ResLoginInfoBean::class.java)
    private var index = 0
    private var adapter: ThisAdapter? = null

    override fun setLayoutRes(): Int = R.layout.ordertypelayout

    override fun init(view: View?, savedInstanceState: Bundle?) {

        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        orderDataList.layoutManager = manager

        adapter = ThisAdapter()
        orderDataList.adapter = adapter

        getData(true, true)
    }

    override fun setListener() {

        orderItemRefreshLayout.setOnRefreshListener {
            getData(true, false)
        }
        orderItemRefreshLayout.setOnLoadmoreListener {
            getData(false, false)
        }
    }

    override fun getPresenter(): OrderP = OrderP(initViewImpl())

    override fun initViewImpl(): OrderV = object : OrderV() {
        override fun setRefreshAndLoadMoreEnable(isEnableRefresh: Boolean, isEnableLoadMore: Boolean) {
            orderItemRefreshLayout.isEnableRefresh = isEnableRefresh
            orderItemRefreshLayout.isEnableLoadmore = isEnableLoadMore
        }

        override fun setEmptyView(resLayoutId: Int) {
            adapter?.setEmptyView(resLayoutId)
        }

        override fun setData(isRefresh: Boolean, datas: List<ResOrderItem>) {

            if (isRefresh) {
                adapter?.setNewData(datas)
            } else {
                adapter?.addData(datas)
            }
            setRefreshAndLoadMoreEnable(true, true)
        }

        override fun setReqLayoutInfo(isRefresh: Boolean, isSuccess: Boolean) {
            onReqFinish(isRefresh, isSuccess)
        }

    }

    inner class ThisAdapter : BaseAdapter(context) {
        override fun onItemClicked(view: View, position: Int) {
        }

        override fun convert(holder: BaseViewHolder, position: Int, atas: List<MultBaseBean>?) {
        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {

            multiItems.add(AdapterMultiItem(VIEWTYPE_ORDER, R.layout.orderitem))
        }

    }

    private fun getData(isRefresh: Boolean, isShowDialog: Boolean) {
        //TODO 这里是测试数据 ,正式环境注取消下面的注释
        if (isRefresh) {
            index = 0
        } else {
            index++
        }
        getPresenter().getOrderData(context, isShowDialog, isRefresh, index, 1234567890122, arguments.getInt("orderType"))

//        if (loginInfo == null) {
//            initViewImpl().setRefreshAndLoadMoreEnable(false)
//            onReqFinish(isRefresh, false)
//
//        } else {
//            initViewImpl().setRefreshAndLoadMoreEnable(true)
//
//            if (isRefresh) {
//                index = 0
//            } else {
//                index++
//            }
//            getPresenter().getOrderData(context, isRefresh, index, loginInfo.userInfoId, arguments.getInt("orderType"))
//        }
    }

}