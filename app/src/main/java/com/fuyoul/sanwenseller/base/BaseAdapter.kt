package com.fuyoul.sanwenseller.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.Code.VIEWTYPE_EMPTY
import com.zhy.autolayout.utils.AutoUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
abstract class BaseAdapter(context: Context) : RecyclerView.Adapter<BaseViewHolder>() {

    private var context: Context? = null
    var datas = arrayListOf<MultBaseBean>()//数据
    private var multiItems = ArrayList<AdapterMultiItem>()//多布局

    init {
        this.datas = datas
        this.context = context
        addMultiType(multiItems)
    }


    override fun getItemCount(): Int = datas.size

    override fun getItemViewType(position: Int): Int = datas.get(position).itemType()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {

        var view: View? = null
        for (index in 0 until multiItems.size) {
            when (viewType) {
                multiItems[index].typeId -> {
                    view = LayoutInflater.from(context).inflate(multiItems[index].typeLayoutRes, parent, false)
                    AutoUtils.auto(view)
                    if (viewType != Code.VIEWTYPE_EMPTY) {
                        view.setOnClickListener {
                            onItemClicked(view!!, view?.tag as Int)
                        }
                    } else {
                        onEmpryLayou(view, multiItems[index].typeLayoutRes)
                    }

                }
            }
        }

        return BaseViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        if (holder.itemViewType != Code.VIEWTYPE_EMPTY) {
            holder.itemView.tag = position
            convert(holder, position, datas)
        }

    }


    private fun <D : MultBaseBean> setNewData(newDatas: List<D>) {
        this.datas.clear()
        this.datas.addAll(newDatas)
        notifyDataSetChanged()
    }

    private fun <D : MultBaseBean> addData(addDatas: List<D>) {
        if (datas.isEmpty()) {
            setNewData(addDatas)
        } else {
            this.datas.addAll(addDatas)
            notifyItemRangeInserted(this.datas.size - addDatas.size, addDatas.size)
        }
    }

    /**更改某一条数据**/
    fun changeData(item: MultBaseBean) {
        notifyItemChanged(datas.indexOf(item))
    }

    fun changeData(position: Int) {
        notifyItemChanged(position)
    }

    /**移除某一条数据**/
    fun remove(position: Int) {
        this.datas.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, 1)
    }

    /**移除某一条数据**/
    fun remove(item: MultBaseBean) {
        this.datas.remove(item)

        val index = this.datas.indexOf(item)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, 1)
    }


    /**这里用于显示空页面**/
    fun setEmptyView(emptyLayourRes: Int) {

        this.datas.clear()
        datas.add(object : MultBaseBean {
            override fun itemType(): Int = VIEWTYPE_EMPTY
        })

        val item = AdapterMultiItem(VIEWTYPE_EMPTY, emptyLayourRes)
        (0 until multiItems.size)
                .filter { multiItems[it].typeId == VIEWTYPE_EMPTY }
                .forEach { multiItems.removeAt(it) }
        multiItems.add(item)
        notifyDataSetChanged()

    }


    /**数据的操作**/
    abstract fun convert(holder: BaseViewHolder, position: Int, allDatas: List<MultBaseBean>)

    /**添加布局**/
    abstract fun addMultiType(multiItems: ArrayList<AdapterMultiItem>)

    /**item点击事件**/
    abstract fun onItemClicked(view: View, position: Int)

    /**空数据页的点击事件**/
    abstract fun onEmpryLayou(view: View, layoutResId: Int)

    abstract fun getSmartRefreshLayout(): SmartRefreshLayout

    abstract fun getRecyclerView(): RecyclerView


    /**控制刷新和加载更多是否可用**/
    fun setRefreshAndLoadMoreEnable(isEnableLoadMore: Boolean) {
        getSmartRefreshLayout().isEnableLoadmore = isEnableLoadMore
    }

    /**增加或者刷新数据**/
    fun <D : MultBaseBean> setData(isRefresh: Boolean, datas: List<D>) {
        if (isRefresh) {
            setNewData(datas)
        } else {
            addData(datas)
        }
        setRefreshAndLoadMoreEnable(true)
    }

    /**通过网络请求获取数据的状态来设置刷新控件的状态**/
    fun setReqLayoutInfo(isRefresh: Boolean, isSuccess: Boolean) {
        if (isRefresh) {
            getSmartRefreshLayout().finishRefresh(isSuccess)
        } else {
            getSmartRefreshLayout().finishLoadmore(isSuccess)
        }
    }
}