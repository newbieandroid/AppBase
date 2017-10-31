package com.fuyoul.sanwenseller.ui.fragment.appointment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.others.AppointMentItemBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.Data
import com.fuyoul.sanwenseller.configs.Key
import com.fuyoul.sanwenseller.structure.model.AppointMentM
import com.fuyoul.sanwenseller.structure.presenter.AppointMentP
import com.fuyoul.sanwenseller.structure.view.AppointMentV
import com.fuyoul.sanwenseller.ui.order.AppointMentInfoActivity
import com.fuyoul.sanwenseller.utils.GlideUtils
import kotlinx.android.synthetic.main.normaltestitem.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class NormalTestItemFragment : BaseFragment<AppointMentM, AppointMentV, AppointMentP>() {


    private var TYPE = Data.TODAY

    private var adapter: ThisAdapter? = null
    override fun setLayoutRes(): Int = R.layout.normaltestitem

    override fun init(view: View?, savedInstanceState: Bundle?) {

        TYPE = arguments.getInt(Key.appointmentTypeKey, Data.TODAY)

        val manager = GridLayoutManager(context, 4)
        normalTestList.layoutManager = manager
        normalTestList.adapter = initViewImpl().getAdapter()
        getPresenter().getData(context, TYPE)

    }

    override fun setListener() {
        normalTestRefreshLayout.setOnRefreshListener {
            getPresenter().getData(context, TYPE)
        }
        stateView.setOnClickListener {
            getPresenter().changeAllDayState(context, TYPE)
        }
    }

    override fun getPresenter(): AppointMentP = AppointMentP(initViewImpl())

    override fun initViewImpl(): AppointMentV = object : AppointMentV() {
        override fun getAdapter(): ThisAdapter {

            if (adapter == null) {
                adapter = ThisAdapter()
            }
            return adapter!!
        }


    }

    inner class ThisAdapter : BaseAdapter(context) {
        override fun getSmartRefreshLayout(): SmartRefreshLayout {

            normalTestRefreshLayout.isEnableLoadmore = false

            return normalTestRefreshLayout
        }

        override fun convert(holder: BaseViewHolder, position: Int, datas: List<MultBaseBean>) {

            val item = datas[position] as AppointMentItemBean
            val time = holder.itemView.findViewById<TextView>(R.id.time)

            val state = holder.itemView.findViewById<TextView>(R.id.state)
            val infoLayout = holder.itemView.findViewById<RelativeLayout>(R.id.infoLayout)
            time.text = item.time


            if (item.isBusy) {//如果全天不可接单
                infoLayout.visibility = View.VISIBLE

                state.text = "不可约"
                time.setTextColor(resources.getColor(R.color.color_888888))
                state.setTextColor(resources.getColor(R.color.color_888888))

            } else {

                if (item.isSelect) {//如果被预约了
                    infoLayout.visibility = View.VISIBLE
                    GlideUtils.loadCircleImg(context, item.avatar, holder.itemView.findViewById(R.id.avatar), R.drawable.nim_avatar_default, R.drawable.nim_avatar_default)
                    state.text = "已约"

                    infoLayout.setOnClickListener {
                        AppointMentInfoActivity.start(context, item.userInfoId)
                    }

                    time.setTextColor(resources.getColor(R.color.color_333333))
                    state.setTextColor(resources.getColor(R.color.color_FF627B))

                } else {
                    infoLayout.visibility = View.GONE
                    state.text = "可约"
                    time.setTextColor(resources.getColor(R.color.color_333333))
                    state.setTextColor(resources.getColor(R.color.color_3CC5BC))
                }
            }
        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {
            multiItems.add(AdapterMultiItem(Code.VIEWTYPE_APPOINTMENT, R.layout.apointmentitem))
        }

        override fun onItemClicked(view: View, position: Int) {

            val item = datas[position] as AppointMentItemBean
            if (!item.isBusy && !item.isSelect) {//
                getPresenter().changeItemState(context, TYPE)

            }
        }

        override fun onEmpryLayou(view: View, layoutResId: Int) {
        }


        override fun getRecyclerView(): RecyclerView = normalTestList

    }
}