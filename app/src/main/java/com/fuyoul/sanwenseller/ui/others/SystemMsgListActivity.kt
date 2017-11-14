package com.fuyoul.sanwenseller.ui.others

import android.os.Bundle
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
import com.fuyoul.sanwenseller.bean.reshttp.ResSystemMsg
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.SystemMsgM
import com.fuyoul.sanwenseller.structure.presenter.SystemMsgP
import com.fuyoul.sanwenseller.structure.view.SystemMsgV
import com.fuyoul.sanwenseller.utils.TimeDateUtils
import kotlinx.android.synthetic.main.systemmsglistactivity.*

/**
 *  @author: chen
 *  @CreatDate: 2017\11\9 0009
 *  @Desc:
 */
class SystemMsgListActivity : BaseActivity<SystemMsgM, SystemMsgV, SystemMsgP>() {


    private var adapter: ThisAdapter? = null

    override fun setLayoutRes(): Int = R.layout.systemmsglistactivity

    override fun initData(savedInstanceState: Bundle?) {

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        sysMsgList.layoutManager = manager
        sysMsgList.adapter = initViewImpl().getAdapter()

        getPresenter().getData(true, this)
    }

    override fun setListener() {

        sysMsgListRefreshLayout.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout?) {
                getPresenter().getData(true, this@SystemMsgListActivity)
            }

            override fun onLoadmore(refreshlayout: RefreshLayout?) {
                getPresenter().getData(false, this@SystemMsgListActivity)
            }
        })
    }

    override fun getPresenter(): SystemMsgP = SystemMsgP(initViewImpl())

    override fun initViewImpl(): SystemMsgV = object : SystemMsgV() {
        override fun getAdapter(): ThisAdapter {

            if (adapter == null) {
                adapter = ThisAdapter()
            }

            return adapter!!
        }

    }

    override fun initTopBar(): TopBarOption {
        val op = TopBarOption()

        op.isShowBar = true
        op.mainTitle = "系统消息"
        return op
    }

    inner class ThisAdapter : BaseAdapter(this) {
        override fun convert(holder: BaseViewHolder, position: Int, allDatas: List<MultBaseBean>) {

            val item = allDatas[position] as ResSystemMsg

            holder.itemView.findViewById<TextView>(R.id.time).text = "${TimeDateUtils.stampToDate(System.currentTimeMillis())}"
            holder.itemView.findViewById<TextView>(R.id.content).text = "测试"
        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {

            multiItems.add(AdapterMultiItem(Code.VIEWTYPE_SYSMSG, R.layout.sysmsgitem))
        }

        override fun onItemClicked(view: View, position: Int) {
        }

        override fun onEmpryLayou(view: View, layoutResId: Int) {
        }

        override fun getSmartRefreshLayout(): SmartRefreshLayout = sysMsgListRefreshLayout

        override fun getRecyclerView(): RecyclerView = sysMsgList

    }
}