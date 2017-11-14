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
import com.fuyoul.sanwenseller.bean.reshttp.ResActivityListInfo
import com.fuyoul.sanwenseller.configs.Code.VIEWTYPE_ACTIVITYLISTINFO
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.ui.web.WebViewActivity
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.TimeDateUtils
import kotlinx.android.synthetic.main.activitylistinfo.*

/**
 *  @author: chen
 *  @CreatDate: 2017\11\6 0006
 *  @Desc:
 */
class ActivityMsgListActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    private var adapter: ThisAdapter? = null

    override fun setLayoutRes(): Int = R.layout.activitylistinfo

    override fun initData(savedInstanceState: Bundle?) {

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        activityListInfoList.layoutManager = manager

        adapter = ThisAdapter()
        activityListInfoList.adapter = adapter
        adapter?.setData(true, arrayListOf(ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo()))

    }

    override fun setListener() {

        activityListInfoRefreshLayout.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout?) {
                adapter?.setData(true, arrayListOf(ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo()))

                refreshlayout?.finishRefresh(true)
            }

            override fun onLoadmore(refreshlayout: RefreshLayout?) {
                adapter?.setData(false, arrayListOf(ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo(), ResActivityListInfo()))

                refreshlayout?.finishLoadmore(true)
            }

        })
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {
        val op = TopBarOption()

        op.isShowBar = true
        op.mainTitle = "活动公告"
        return op
    }


    private inner class ThisAdapter : BaseAdapter(this) {
        override fun convert(holder: BaseViewHolder, position: Int, allDatas: List<MultBaseBean>) {

            val item = allDatas[position] as ResActivityListInfo

            holder.itemView.findViewById<TextView>(R.id.time).text = "${TimeDateUtils.stampToDate(item.time)}"

            holder.itemView.findViewById<TextView>(R.id.title).text = item.title
            holder.itemView.findViewById<TextView>(R.id.content).text = item.content
            GlideUtils.loadNormalImg(this@ActivityMsgListActivity, item.imgPath, holder.itemView.findViewById(R.id.imgIcon))

        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {

            multiItems.add(AdapterMultiItem(VIEWTYPE_ACTIVITYLISTINFO, R.layout.itemofactivitylistinfo))
        }

        override fun onItemClicked(view: View, position: Int) {

            WebViewActivity.startWebView(this@ActivityMsgListActivity, "公告详情", "http://www.baidu.com")

        }

        override fun onEmpryLayou(view: View, layoutResId: Int) {
        }

        override fun getSmartRefreshLayout(): SmartRefreshLayout = activityListInfoRefreshLayout

        override fun getRecyclerView(): RecyclerView = activityListInfoList
    }
}