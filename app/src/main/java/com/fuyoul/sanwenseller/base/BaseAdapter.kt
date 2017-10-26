package com.fuyoul.sanwenseller.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var datas = arrayListOf<MultBaseBean>()//数据
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

    /**数据的操作**/
    abstract fun convert(holder: BaseViewHolder, position: Int, atas: List<MultBaseBean>?)

    /**添加布局**/
    abstract fun addMultiType(multiItems: ArrayList<AdapterMultiItem>)

    /**item点击事件**/
    abstract fun onItemClicked(view: View, position: Int)

    fun <D : MultBaseBean> setNewData(newDatas: List<D>) {
        this.datas.clear()
        this.datas.addAll(newDatas)
        notifyDataSetChanged()
    }


    fun <D : MultBaseBean> addData(addDatas: List<D>) {
        if (datas == null) {
            setNewData(addDatas)
        } else {
            this.datas.addAll(addDatas)
            notifyItemRangeInserted(this.datas.size - addDatas.size, addDatas.size)
        }
    }

    fun changeData(item: MultBaseBean, position: Int) {

        datas?.set(position, item)
        notifyItemChanged(position)
    }

    fun remove(position: Int) {
        this.datas?.removeAt(position)
        notifyItemRemoved(position)
    }

    fun remove(item: MultBaseBean) {

        val index = this.datas?.indexOf(item) ?: -1
        this.datas?.remove(item)
        if (index >= 0) {
            notifyItemRemoved(index)
        }
    }


    /**这里用于显示空页面**/
    fun setEmptyView(emptyLayourRes: Int) {

        this.datas.clear()
        datas.add(object : MultBaseBean {
            override fun itemType(): Int = VIEWTYPE_EMPTY
        })
        val item = AdapterMultiItem(VIEWTYPE_EMPTY, emptyLayourRes)
        if (multiItems.contains(item)) {
            multiItems.remove(item)
        }
        multiItems.add(item)
        notifyDataSetChanged()

    }

}