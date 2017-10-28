package com.fuyoul.sanwenseller.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpBabyItem
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.structure.model.BabyManagerM
import com.fuyoul.sanwenseller.structure.presenter.BabyManagerP
import com.fuyoul.sanwenseller.structure.view.BabyManagerV
import com.fuyoul.sanwenseller.ui.baby.EditBabyInfoActivity
import com.fuyoul.sanwenseller.ui.LoginActivity
import com.fuyoul.sanwenseller.utils.GlideUtils
import kotlinx.android.synthetic.main.babymanageritem.*
import org.litepal.crud.DataSupport

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
class BabyManagerItemFragment : BaseFragment<BabyManagerM, BabyManagerV, BabyManagerP>() {
    private var loginInfo = DataSupport.findFirst(ResLoginInfoBean::class.java)//登录信息
    private var adapter: ThisAdapter? = null

    override fun setLayoutRes(): Int = R.layout.babymanageritem

    override fun init(view: View?, savedInstanceState: Bundle?) {

        adapter = initViewImpl().getBaseAdapter()
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL

        adapter?.getRecyclerView()?.layoutManager = manager
        adapter?.getRecyclerView()?.adapter = adapter

        getData(true)
    }

    override fun setListener() {

        babyManagerRefresh.setOnRefreshListener {
            getData(true)
        }

        babyManagerRefresh.setOnLoadmoreListener {
            getData(false)
        }
    }


    private var index = 0
    private fun getData(isRefresh: Boolean) {
        if (isRefresh) {
            index = 0
        } else {
            index++
        }


        if (loginInfo == null) {
            initViewImpl().getBaseAdapter().setRefreshAndLoadMoreEnable(false)
            initViewImpl().getBaseAdapter().setReqLayoutInfo(isRefresh, false)
            initViewImpl().getBaseAdapter().setEmptyView(R.layout.notsignstatelayout)
        } else {
            initViewImpl().getBaseAdapter().setRefreshAndLoadMoreEnable(true)

            getPresenter().getData(context, isRefresh, index, loginInfo.userInfoId, arguments.getInt("status", Code.HOTSELL))
        }


    }

    override fun getPresenter(): BabyManagerP = BabyManagerP(initViewImpl())

    override fun initViewImpl(): BabyManagerV = object : BabyManagerV() {
        override fun editBaby(item: ResHttpBabyItem) {

            val bund = Bundle()
            bund.putSerializable("item", item)
            val intent = Intent(context, EditBabyInfoActivity::class.java)
            intent.putExtras(bund)
            startActivityForResult(intent, Code.REQ_EDITBABYINFO)
        }

        override fun getBaseAdapter(): BabyManagerItemFragment.ThisAdapter = adapter ?: ThisAdapter()


    }

    inner class ThisAdapter : BaseAdapter(context) {
        override fun convert(holder: BaseViewHolder, position: Int, datas: List<MultBaseBean>) {

            val item = datas[position] as ResHttpBabyItem
            GlideUtils.loadNormalImg(context, JSON.parseArray(item.imgs).getJSONObject(0).getString("url"), holder.itemView.findViewById(R.id.babyItemIcon))

            holder.itemView.findViewById<TextView>(R.id.babyItemTitle).text = item.goodsName
            holder.itemView.findViewById<TextView>(R.id.babyItemDes).text = item.introduce
            holder.itemView.findViewById<TextView>(R.id.babyItemPrice).text = "￥${item?.price}"


            val babyItemLeftFunc = holder.itemView.findViewById<TextView>(R.id.babyItemLeftFunc)
            val babyItemMiddleFunc = holder.itemView.findViewById<TextView>(R.id.babyItemMiddleFunc)
            val babyItemRightFunc = holder.itemView.findViewById<TextView>(R.id.babyItemRightFunc)
            if (arguments.getInt("status") == Code.SELL) {
                babyItemRightFunc.visibility = View.GONE
                babyItemMiddleFunc.text = "下架"
            } else {
                babyItemRightFunc.visibility = View.VISIBLE
                babyItemMiddleFunc.text = "上架"
            }

            babyItemLeftFunc.setOnClickListener {
                initViewImpl().editBaby(item)

            }
            babyItemMiddleFunc.setOnClickListener {
                if (arguments.getInt("status") == Code.SELL) {
                    getPresenter().downToShop(context, position)
                } else {
                    getPresenter().upToShop(context, position)
                }
            }
            babyItemRightFunc.setOnClickListener {
                getPresenter().deleteBaby(context, position)

            }

        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {
            multiItems.add(AdapterMultiItem(Code.VIEWTYPE_BABYMANAGER, R.layout.babymanagerdataitem))
        }

        override fun onItemClicked(view: View, position: Int) {
        }

        override fun onEmpryLayou(view: View, layoutResId: Int) {
            when (layoutResId) {
                R.layout.emptylayout -> {
                }
                R.layout.errorstatelayout -> {
                }
                R.layout.notsignstatelayout -> {
                    view.findViewById<TextView>(R.id.errorLoginStateView).setOnClickListener {
                        startActivity(Intent(context, LoginActivity::class.java))
                    }
                }
            }
        }

        override fun getSmartRefreshLayout(): SmartRefreshLayout = babyManagerRefresh

        override fun getRecyclerView(): RecyclerView = babyManagerDataList

    }

}