package com.fuyoul.sanwenseller.ui.main.main

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.csl.refresh.SmartRefreshLayout
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseAdapter
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.bean.AdapterMultiItem
import com.fuyoul.sanwenseller.bean.MultBaseBean
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpOrderItem
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.Code.VIEWTYPE_ORDER
import com.fuyoul.sanwenseller.configs.Data
import com.fuyoul.sanwenseller.configs.Data.QUICKTESTBABY
import com.fuyoul.sanwenseller.enuminfo.OrderType
import com.fuyoul.sanwenseller.structure.model.OrderM
import com.fuyoul.sanwenseller.structure.presenter.OrderP
import com.fuyoul.sanwenseller.structure.view.OrderV
import com.fuyoul.sanwenseller.ui.user.LoginActivity
import com.fuyoul.sanwenseller.ui.others.OrderDetailActivity
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.widgets.CountdownView.CountdownView
import kotlinx.android.synthetic.main.ordertypelayout.*
import org.litepal.crud.DataSupport
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class OrderItemFragment : BaseFragment<OrderM, OrderV, OrderP>() {
    private var loginInfo = DataSupport.findFirst(ResLoginInfoBean::class.java)
    private var index = 0
    private var adapter: ThisAdapter? = null

    override fun setLayoutRes(): Int = R.layout.ordertypelayout

    override fun init(view: View?, savedInstanceState: Bundle?) {

        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        orderDataList.layoutManager = manager

        adapter = initViewImpl().getBaseAdapter()
        adapter?.getRecyclerView()?.adapter = adapter

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
        override fun deleteItem(item: ResHttpOrderItem) {

            getBaseAdapter().remove(item)
        }

        override fun changeOrderState(item: ResHttpOrderItem) {
            getBaseAdapter().changeData(item)
        }

        override fun getBaseAdapter(): ThisAdapter {

            if (adapter == null) {
                adapter = ThisAdapter()
            }
            return adapter!!
        }
    }

    inner class ThisAdapter : BaseAdapter(context) {
        override fun getSmartRefreshLayout(): SmartRefreshLayout = orderItemRefreshLayout

        override fun getRecyclerView(): RecyclerView = orderDataList


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

        override fun onItemClicked(view: View, position: Int) {
        }


        override fun onViewAttachedToWindow(holder: BaseViewHolder?) {
            super.onViewAttachedToWindow(holder)
            startTimer(holder!!, false)
        }

        override fun onViewDetachedFromWindow(holder: BaseViewHolder?) {
            super.onViewDetachedFromWindow(holder)
            val countDownView = holder?.itemView?.findViewById<CountdownView>(R.id.countDownView)
            if (countDownView?.visibility == View.VISIBLE) {
                countDownView.stop()
            }
        }

        /**开始倒计时**/
        private fun startTimer(holder: BaseViewHolder, isFromItem: Boolean) {
            val countDownView = holder.itemView.findViewById<CountdownView>(R.id.countDownView)

            if (countDownView != null && countDownView.visibility == View.VISIBLE && datas.isNotEmpty()) {

                val position = holder.adapterPosition

                val release: Long = try {

                    SimpleDateFormat("yyyy/MM/dd HH:mm").parse((datas[position] as ResHttpOrderItem).appointmentTime).time - System.currentTimeMillis()

                } catch (e: ParseException) {
                    SimpleDateFormat("yyyy-MM-dd HH:mm").parse((datas[position] as ResHttpOrderItem).appointmentTime).time - System.currentTimeMillis()

                }


                if (release > 0) {
                    countDownView.start(release)
                    countDownView.setOnCountdownEndListener {
                        (datas[position] as ResHttpOrderItem).status = OrderType.INPREDICTED.orderType

                        if (isFromItem) {
                            notifyItemChanged(position)
                        }
                    }
                } else {

                    countDownView.stop()
                    countDownView.allShowZero()

                }

            }
        }

        override fun convert(holder: BaseViewHolder, position: Int, datas: List<MultBaseBean>) {


            val orderItemFuncLeft = holder.itemView.findViewById<TextView>(R.id.orderItem_Func_Left)
            val orderItemFuncMiddle = holder.itemView.findViewById<TextView>(R.id.orderItem_Func_Middle)
            val orderItemFuncRight = holder.itemView.findViewById<TextView>(R.id.orderItem_Func_Right)
            val countDownViewInfo = holder.itemView.findViewById<TextView>(R.id.countDownViewInfo)
            val countDownView = holder.itemView.findViewById<CountdownView>(R.id.countDownView)


            val item = datas[position] as ResHttpOrderItem
            holder.itemView.findViewById<TextView>(R.id.orderItemTime).text = item.appointmentTime

            GlideUtils.loadNormalImg(context, JSON.parseArray(item.goodsImgs).getJSONObject(0).getString("url"), holder.itemView.findViewById(R.id.orderItemIcon))
            holder.itemView.findViewById<TextView>(R.id.orderItemPrice).text = "¥${item.price}"
            holder.itemView.findViewById<TextView>(R.id.orderItemTitle).text = item.goodsName

            var typeIcon: Drawable? = null
            if (item.type == Data.QUICKTESTBABY) {
                typeIcon = resources.getDrawable(R.mipmap.icon_quicktesttype)
                typeIcon.setBounds(0, 0, typeIcon.minimumWidth, typeIcon.minimumHeight)
            }

            holder.itemView.findViewById<TextView>(R.id.orderItemTitle).setCompoundDrawables(null, null, typeIcon, null)


            holder.itemView.findViewById<TextView>(R.id.orderItemDes).text = item.introduce
            GlideUtils.loadCircleImg(context, item.avatar, holder.itemView.findViewById(R.id.orderItemHead))
            holder.itemView.findViewById<TextView>(R.id.orderItemNick).text = item.nickname
            when (item.status) {
                OrderType.TOBEPAY.orderType -> {
                    //待付款
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.GONE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.TOBEPAY.orderTitle
                }
                OrderType.TOBEPREDICTED.orderType -> {
                    //待预测
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.VISIBLE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.VISIBLE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.TOBEPREDICTED.orderTitle


                    if (item.type == QUICKTESTBABY) {//如果是闪测

                        countDownViewInfo.visibility = View.VISIBLE
                        countDownView.visibility = View.GONE

                        val spanString = SpannableString("*必须在${item.appointmentTime}前回复用户")
                        spanString.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_3CC5BC)), 4, spanString.length - 4, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        countDownViewInfo.text = "*必须在${spanString}前回复用户"

                        orderItemFuncLeft.visibility = View.GONE
                        orderItemFuncMiddle.visibility = View.GONE
                        orderItemFuncRight.visibility = View.VISIBLE

                        orderItemFuncRight.setBackgroundResource(R.drawable.back_order_press_full)
                        orderItemFuncRight.setTextColor(resources.getColor(R.color.color_white))
                        orderItemFuncRight.text = "去预测"
                        orderItemFuncRight.setOnClickListener {
                            //TODO 去预测
                        }

                    } else {
                        startTimer(holder, true)

                        countDownViewInfo.visibility = View.VISIBLE
                        countDownView.visibility = View.VISIBLE

                        countDownViewInfo.text = "预测开始时间还剩："

                        orderItemFuncLeft.visibility = View.GONE
                        orderItemFuncMiddle.visibility = View.VISIBLE
                        orderItemFuncRight.visibility = View.VISIBLE

                        orderItemFuncMiddle.setBackgroundResource(R.drawable.back_order_normal)
                        orderItemFuncMiddle.setTextColor(resources.getColor(R.color.color_888888))
                        orderItemFuncMiddle.text = "联系买家"
                        orderItemFuncMiddle.setOnClickListener {
                            //TODO 联系买家
                        }

                        orderItemFuncRight.setBackgroundResource(R.drawable.back_order_press_full)
                        orderItemFuncRight.setTextColor(resources.getColor(R.color.color_white))
                        orderItemFuncRight.text = "去预测"
                        orderItemFuncRight.setOnClickListener {
                            //TODO 去预测
                        }
                    }
                }
                OrderType.INPREDICTED.orderType -> {
                    //预测中
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.VISIBLE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.VISIBLE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.INPREDICTED.orderTitle
                    orderItemFuncLeft.visibility = View.VISIBLE
                    orderItemFuncMiddle.visibility = View.VISIBLE
                    orderItemFuncRight.visibility = View.VISIBLE
                    countDownViewInfo.visibility = View.VISIBLE
                    countDownView.visibility = View.GONE

                    countDownViewInfo.text = "*预测完成后，可提前确认订单完成交易"


                    orderItemFuncLeft.setBackgroundResource(R.drawable.back_order_normal)
                    orderItemFuncLeft.setTextColor(resources.getColor(R.color.color_888888))
                    orderItemFuncLeft.text = "联系买家"
                    orderItemFuncLeft.setOnClickListener {
                        //TODO 联系买家
                    }

                    orderItemFuncMiddle.setBackgroundResource(R.drawable.back_order_press_full)
                    orderItemFuncMiddle.setTextColor(resources.getColor(R.color.color_white))
                    orderItemFuncMiddle.text = "去预测"
                    orderItemFuncMiddle.setOnClickListener {
                        //TODO 去预测
                    }

                    orderItemFuncRight.setBackgroundResource(R.drawable.back_order_press_full_other)
                    orderItemFuncRight.setTextColor(resources.getColor(R.color.color_3CC5BC))
                    orderItemFuncRight.text = "确认订单"
                    orderItemFuncRight.setOnClickListener {
                        //TODO 确认订单

                        getPresenter().confirmOrder(context, item)
                    }

                }
                OrderType.PREDICTED.orderType -> {
                    //已预测
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.GONE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE

                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.TOREPLAY.orderTitle

                }

                OrderType.TOREPLAY.orderType -> {
                    //待评价
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.GONE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.TOREPLAY.orderTitle

                }

                OrderType.PAYMENT.orderType -> {
                    //交易成功
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.GONE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.PAYMENT.orderTitle

                }

                OrderType.WAITTINGSELLER.orderType -> {
                    //买家申请退款
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.VISIBLE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.WAITTINGSELLER.orderTitle

                    orderItemFuncLeft.visibility = View.VISIBLE
                    orderItemFuncMiddle.visibility = View.VISIBLE
                    orderItemFuncRight.visibility = View.VISIBLE

                    orderItemFuncLeft.setBackgroundResource(R.drawable.back_order_normal)
                    orderItemFuncLeft.setTextColor(resources.getColor(R.color.color_888888))
                    orderItemFuncLeft.text = "客服介入"
                    orderItemFuncLeft.setOnClickListener {
                        //TODO 客服介入
                    }

                    orderItemFuncMiddle.setBackgroundResource(R.drawable.back_order_press_full_other)
                    orderItemFuncMiddle.setTextColor(resources.getColor(R.color.color_3CC5BC))
                    orderItemFuncMiddle.text = "同意退款"
                    orderItemFuncMiddle.setOnClickListener {
                        //TODO 同意退款
                        getPresenter().orderRefund(context, item, true)
                    }

                    orderItemFuncRight.setBackgroundResource(R.drawable.back_order_press_full_other)
                    orderItemFuncRight.setTextColor(resources.getColor(R.color.color_3CC5BC))
                    orderItemFuncRight.text = "拒绝退款"
                    orderItemFuncRight.setOnClickListener {
                        //TODO 拒绝退款
                        getPresenter().orderRefund(context, item, false)
                    }


                }
                OrderType.CLOSED.orderType -> {
                    //交易关闭
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.VISIBLE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.CLOSED.orderTitle

                    orderItemFuncLeft.visibility = View.GONE
                    orderItemFuncMiddle.visibility = View.GONE
                    orderItemFuncRight.visibility = View.VISIBLE


                    orderItemFuncRight.setBackgroundResource(R.drawable.back_order_normal)
                    orderItemFuncRight.setTextColor(resources.getColor(R.color.color_888888))
                    orderItemFuncRight.text = "删除订单"
                    orderItemFuncRight.setOnClickListener {
                        //TODO 删除订单
                        getPresenter().deleteOrder(context, item)
                    }
                }
                OrderType.INREFUND.orderType -> {
                    //退款处理中
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.GONE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.INREFUND.orderTitle

                }
                OrderType.REFUND.orderType -> {
                    //已退款
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.VISIBLE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.REFUND.orderTitle
                    orderItemFuncLeft.visibility = View.GONE
                    orderItemFuncMiddle.visibility = View.GONE
                    orderItemFuncRight.visibility = View.VISIBLE


                    orderItemFuncRight.setBackgroundResource(R.drawable.back_order_normal)
                    orderItemFuncRight.setTextColor(resources.getColor(R.color.color_888888))
                    orderItemFuncRight.text = "删除订单"
                    orderItemFuncRight.setOnClickListener {
                        //TODO 删除订单
                        getPresenter().deleteOrder(context, item)
                    }
                }
                OrderType.REFUNDREFUSE.orderType -> {
                    //拒绝退款

                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.VISIBLE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.REFUNDREFUSE.orderTitle
                    orderItemFuncLeft.visibility = View.VISIBLE
                    orderItemFuncMiddle.visibility = View.VISIBLE
                    orderItemFuncRight.visibility = View.VISIBLE


                    orderItemFuncLeft.setBackgroundResource(R.drawable.back_order_normal)
                    orderItemFuncLeft.setTextColor(resources.getColor(R.color.color_888888))
                    orderItemFuncLeft.text = "客服介入"
                    orderItemFuncLeft.setOnClickListener {
                        //TODO 客服介入
                    }


                    orderItemFuncMiddle.setBackgroundResource(R.drawable.back_order_normal)
                    orderItemFuncMiddle.setTextColor(resources.getColor(R.color.color_CCCCCC))
                    orderItemFuncMiddle.text = "同意退款"
                    orderItemFuncMiddle.setOnClickListener {
                        //什么都不做，消耗点击事件
                    }


                    orderItemFuncRight.setBackgroundResource(R.drawable.back_order_normal)
                    orderItemFuncRight.setTextColor(resources.getColor(R.color.color_CCCCCC))
                    orderItemFuncRight.text = "拒绝退款"
                    orderItemFuncRight.setOnClickListener {
                        //什么都不做，消耗点击事件
                    }

                }
                OrderType.SERVICEIN.orderType -> {
                    //客服介入
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemFuncLayout).visibility = View.GONE
                    holder.itemView.findViewById<LinearLayout>(R.id.orderItemReleaseTime).visibility = View.GONE
                    holder.itemView.findViewById<TextView>(R.id.orderItemState).text = OrderType.SERVICEIN.orderTitle

                }

            }

            holder.itemView.findViewById<TextView>(R.id.orderItemPrice).setOnClickListener {
                OrderDetailActivity.start(activity, item)
            }
        }

        override fun addMultiType(multiItems: ArrayList<AdapterMultiItem>) {

            multiItems.add(AdapterMultiItem(VIEWTYPE_ORDER, R.layout.orderitem))
        }

    }

    private fun getData(isRefresh: Boolean, isShowDialog: Boolean) {

        if (loginInfo == null) {
            initViewImpl().getBaseAdapter().setRefreshAndLoadMoreEnable(false)
            initViewImpl().getBaseAdapter().setReqLayoutInfo(isRefresh, false)
            initViewImpl().getBaseAdapter().setEmptyView(R.layout.notsignstatelayout)
        } else {
            initViewImpl().getBaseAdapter().setRefreshAndLoadMoreEnable(true)

            if (isRefresh) {
                index = 0
            } else {
                index++
            }
            getPresenter().getOrderData(context, isShowDialog, isRefresh, index, arguments.getInt("orderType"))

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Code.REQ_ORDERDETAIL && resultCode == Activity.RESULT_OK) {
            //如果是订单详情返回的


            val item = data?.extras?.getSerializable("item") as ResHttpOrderItem

            var position = 0

            for (index in 0 until (adapter?.datas?.size ?: 0)) {
                if (item.orderId == (adapter?.datas?.get(index) as ResHttpOrderItem).orderId) {
                    (adapter?.datas?.get(index) as ResHttpOrderItem).status = item.status

                    position = index
                    break
                }

            }


            adapter?.changeData(position)

        }
    }

}