package com.fuyoul.sanwenseller.helper

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.others.MultDialogBean
import com.fuyoul.sanwenseller.configs.Code.VIEWTYPE_MULTDIALOG
import com.fuyoul.sanwenseller.listener.MultDialogListener
import com.fuyoul.sanwenseller.widgets.dialog.AbstractDialog
import com.fuyoul.sanwenseller.widgets.dialog.DialogViewHolder

/**
 *  @author: chen
 *  @CreatDate: 2017\10\24 0024
 *  @Desc: 提示信息的对话框
 */
object MsgDialogHelper {


    interface DialogListener {
        fun onPositive()
        fun onNagetive()
    }

    interface DialogOndismissListener {
        fun onDismiss(context: Context)
    }


    /**显示带有确定和取消的按钮**/
    fun showNormalDialog(context: Context, cancleable: Boolean, title: String, msgInfo: String, listener: DialogListener?) {
        showNormalDialog(context, cancleable, title, msgInfo, null, null, listener)
    }

    fun showNormalDialog(context: Context, cancleable: Boolean, title: String, msgInfo: String, sureText: String?, listener: DialogListener?) {
        showNormalDialog(context, cancleable, title, msgInfo, null, sureText, listener)
    }

    fun showNormalDialog(context: Context, cancleable: Boolean, title: String, msgInfo: String, cancleText: String?, sureText: String?, listener: DialogListener?) {

        object : AbstractDialog(context, R.layout.msgdialog) {
            override fun convert(holder: DialogViewHolder?) {

                holder?.setText(R.id.title, title)
                holder?.setText(R.id.content, msgInfo)
                holder?.setText(R.id.sure, if (TextUtils.isEmpty(sureText)) "确定" else sureText)
                holder?.setText(R.id.cancle, if (TextUtils.isEmpty(cancleText)) "取消" else cancleText)

                holder?.setOnClick(R.id.sure, {
                    dismiss()
                    listener?.onPositive()
                })
                holder?.setOnClick(R.id.cancle, {
                    dismiss()
                    listener?.onNagetive()
                })
            }

        }.backgroundLight(0.7).setCancelAble(cancleable).showDialog(R.style.dialog_scale_animstyle)

    }

    /**底部只有一个按钮的弹框**/
    fun showSingleDialog(context: Context, cancleable: Boolean, title: String, msgInfo: String, sureText: String?, listener: DialogListener?) {

        object : AbstractDialog(context, R.layout.singmsgdialog) {
            override fun convert(holder: DialogViewHolder?) {

                holder?.setText(R.id.title, title)
                holder?.setText(R.id.content, msgInfo)
                holder?.setText(R.id.sure, if (TextUtils.isEmpty(sureText)) "确定" else sureText)

                holder?.setOnClick(R.id.sure, {
                    dismiss()
                    listener?.onPositive()
                })
            }

        }.backgroundLight(0.7).setCancelAble(cancleable).showDialog(R.style.dialog_scale_animstyle)

    }


    fun showMultItemDialog(context: Context, items: List<MultDialogBean>, bottomText: String, listener: MultDialogListener) {
        object : AbstractDialog(context, R.layout.iosselectdialog) {
            override fun convert(holder: DialogViewHolder?) {

                holder?.convertView?.setOnClickListener {
                    dismiss()
                }

                val selectItem = holder?.convertView?.findViewById<RecyclerView>(R.id.selectItem)

                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                selectItem?.layoutManager = manager
                val adapter = object : BaseAdapter(context) {
                    override fun convert(holder: BaseViewHolder, position: Int, datas: List<MultBaseBean>) {
                        holder.itemView.findViewById<TextView>(R.id.iosItemText).text = items[position].title
                    }

                    override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {

                        multiItems.add(AdapterMultiItem(VIEWTYPE_MULTDIALOG, R.layout.item_iosdialog))
                    }

                    override fun onItemClicked(view: View, position: Int) {
                        listener.onItemClicked(position)
                        dismiss()
                    }

                    override fun onEmpryLayou(view: View, layoutResId: Int) {
                    }

                    override fun getSmartRefreshLayout(): SmartRefreshLayout = SmartRefreshLayout(context)

                    override fun getRecyclerView(): RecyclerView = selectItem!!

                }
                selectItem?.adapter = adapter
                adapter.setData(true, items)

                val cancle = holder!!.convertView.findViewById<TextView>(R.id.cancle)
                cancle.text = bottomText
                cancle.setOnClickListener {
                    dismiss()
                }
            }

        }.fromBottom()
                .setCancelAble(true)
                .setCanceledOnTouchOutside(true)
                .backgroundLight(0.7)
                .fullWidth()
                .showDialog()
    }


    /**显示成功或失败的对话框**/


    fun showStateDialog(context: Context, content: String, state: Boolean) {
        showStateDialog(context, content, state, null)
    }

    fun showStateDialog(context: Context, content: String, state: Boolean, listener: DialogOndismissListener?) {

        object : AbstractDialog(context, R.layout.statedialog) {
            override fun convert(holder: DialogViewHolder?) {

                holder?.convertView?.findViewById<ImageView>(R.id.stateIcon)?.setImageResource(if (state) R.mipmap.ic_all_successful else R.mipmap.ic_all_fail)
                holder?.convertView?.findViewById<TextView>(R.id.stateText)?.text = content
            }

        }
                .setOnCancelListener {
                    listener?.onDismiss(context)
                }
                .backgroundLight(0.7).setCancelAble(true).showDialog()
    }


}