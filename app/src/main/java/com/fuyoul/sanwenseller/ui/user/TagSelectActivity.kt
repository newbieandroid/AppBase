package com.fuyoul.sanwenseller.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.reqhttp.ReqRegistMasterInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResTagBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.TagSelectM
import com.fuyoul.sanwenseller.structure.presenter.TagSelectP
import com.fuyoul.sanwenseller.structure.view.TagSelectV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import kotlinx.android.synthetic.main.tagselectlayout.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
class TagSelectActivity : BaseActivity<TagSelectM, TagSelectV, TagSelectP>() {


    private val selectTags = ArrayList<ResTagBean>()

    private var adapter: BaseAdapter? = null

    private var reqRegistMasterInfo: ReqRegistMasterInfo? = null

    companion object {
        fun start(context: Context, reqRegistMasterInfo: ReqRegistMasterInfo) {
            val intent = Intent(context, TagSelectActivity::class.java)
            val bund = Bundle()
            bund.putSerializable("data", reqRegistMasterInfo)
            intent.putExtras(bund)
            context.startActivity(intent)
        }

    }

    override fun setLayoutRes(): Int = R.layout.tagselectlayout

    override fun initData(savedInstanceState: Bundle?) {

        reqRegistMasterInfo = intent.extras.getSerializable("data") as ReqRegistMasterInfo
        getPresenter().getTagList(this)
    }

    override fun setListener() {

        tagSelectBtn.setOnClickListener {

            if (selectTags.isNotEmpty() && selectTags.size > 3) {

                reqRegistMasterInfo?.labelList = selectTags

                RegistMasterInfoActivity.start(this, reqRegistMasterInfo!!)
            } else {
                NormalFunUtils.showToast(this, "请选择4~6个合适的标签")
            }

        }
    }

    override fun getPresenter(): TagSelectP = TagSelectP(initViewImpl())

    override fun initViewImpl(): TagSelectV = object : TagSelectV() {
        override fun getAdapter(): BaseAdapter {

            if (adapter == null) {
                adapter = object : BaseAdapter(this@TagSelectActivity) {
                    override fun convert(holder: BaseViewHolder, position: Int, allDatas: List<MultBaseBean>) {

                        val item = allDatas[position] as ResTagBean
                        val tagTitle = holder.itemView.findViewById<CheckBox>(R.id.tagTitle)
                        tagTitle.text = item.label

                        tagTitle.setOnCheckedChangeListener { _, isChecked ->

                            if (isChecked) {
                                selectTags.add(item)
                            } else {
                                selectTags.remove(item)
                            }
                        }
                    }

                    override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {
                        multiItems.add(AdapterMultiItem(Code.VIEWTYPE_TAG, R.layout.alltagitem))
                    }

                    override fun onItemClicked(view: View, position: Int) {
                    }

                    override fun onEmpryLayou(view: View, layoutResId: Int) {
                    }

                    override fun getSmartRefreshLayout(): SmartRefreshLayout = SmartRefreshLayout(this@TagSelectActivity)

                    override fun getRecyclerView(): RecyclerView = tagDataList

                }
            }
            return adapter!!
        }

        override fun setTag(datas: List<ResTagBean>) {

            val manager = GridLayoutManager(this@TagSelectActivity, 3)
            tagDataList.layoutManager = manager
            tagDataList.adapter = getAdapter()

            getAdapter().setData(true, datas)
        }

    }

    override fun initTopBar(): TopBarOption {
        val op = TopBarOption()

        op.isShowBar = true
        op.mainTitle = "选择您的个人标签"

        return op
    }
}