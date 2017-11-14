package com.fuyoul.sanwenseller.ui.others

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpOrderItem
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.Data
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.enuminfo.OrderType
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.model.OrderM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.fuyoul.sanwenseller.utils.TimeDateUtils
import kotlinx.android.synthetic.main.orderdetail.*

/**
 *  @author: chen
 *  @CreatDate: 2017\11\6 0006
 *  @Desc:
 */
class OrderDetailActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    var item: ResHttpOrderItem? = null

    companion object {
        fun start(activity: Activity, item: ResHttpOrderItem) {

            val intent = Intent(activity, OrderDetailActivity::class.java)
            val bund = Bundle()
            bund.putSerializable("data", item)

            intent.putExtras(bund)
            activity.startActivityForResult(intent, Code.REQ_ORDERDETAIL)
        }
    }

    override fun setLayoutRes(): Int = R.layout.orderdetail

    override fun initData(savedInstanceState: Bundle?) {

        item = intent.extras.getSerializable("data") as ResHttpOrderItem


        //如果是闪测商品
        if (item?.type == Data.QUICKTESTBABY) {

            orderDetail_PointTimeLayout.visibility = View.GONE
            orderDetail_ServiceTimeLayout.visibility = View.GONE
        }

        orderDetail_OtherInfoLayout.visibility = View.GONE
        orderDetail_FuncLayout.visibility = View.GONE

        orderdetail_State.text =
                when (item?.status) {

                    OrderType.TOBEPREDICTED.orderType -> {

                        orderDetail_FuncLayout.visibility = View.VISIBLE
                        orderDetail_FuncBottom_Left.visibility = View.GONE
                        orderDetail_FuncBottom_Middle.visibility = View.GONE
                        orderDetail_FuncBottom_Right.visibility = View.VISIBLE
                        orderDetail_FuncBottom_Right.text = "去回复"
                        orderDetail_FuncBottom_Right.setOnClickListener {
                            //TODO 去回复
                        }

                        OrderType.TOBEPREDICTED.orderTitle
                    }
                    OrderType.INPREDICTED.orderType -> {
                        OrderType.INPREDICTED.orderTitle
                    }
                    OrderType.TOBEPAY.orderType -> {
                        OrderType.TOBEPAY.orderTitle
                    }
                    OrderType.TOREPLAY.orderType -> {
                        OrderType.TOREPLAY.orderTitle
                    }
                    OrderType.PAYMENT.orderType -> {
                        OrderType.PAYMENT.orderTitle
                    }
                    OrderType.WAITTINGSELLER.orderType -> {

                        orderDetail_OtherInfoLayout.visibility = View.VISIBLE
                        orderDetail_OtherInfoContent.text = "买家申请退款\n24小时后不操作自动退款给买家"

                        orderDetail_FuncLayout.visibility = View.VISIBLE
                        orderDetail_FuncBottom_Left.visibility = View.VISIBLE
                        orderDetail_FuncBottom_Middle.visibility = View.VISIBLE
                        orderDetail_FuncBottom_Right.visibility = View.VISIBLE

                        orderDetail_FuncBottom_Left.text = "同意退款"
                        orderDetail_FuncBottom_Left.setOnClickListener {
                            //TODO 同意退款

                            refundOrder(true)

                        }

                        orderDetail_FuncBottom_Right.text = "拒绝退款"
                        orderDetail_FuncBottom_Right.setOnClickListener {
                            //TODO 拒绝退款
                            refundOrder(false)
                        }

                        OrderType.WAITTINGSELLER.orderTitle
                    }
                    OrderType.INREFUND.orderType -> {
                        orderDetail_OtherInfoLayout.visibility = View.VISIBLE
                        orderDetail_OtherInfoContent.text = "同意退款\n银行退款处理中"

                        OrderType.INREFUND.orderTitle
                    }
                    OrderType.REFUND.orderType -> {

                        orderDetail_OtherInfoLayout.visibility = View.VISIBLE

                        when (item?.refundStatus) {
                            Data.REFUND -> {
                                orderDetail_OtherInfoContent.text = "同意退款"
                            }

                            Data.REFUNDTOSELLER -> {
                                orderDetail_OtherInfoContent.text = "客服介入交易成功"

                            }
                            Data.REFUNDTOUSER -> {
                                orderDetail_OtherInfoContent.text = "客服介入已退款"

                            }
                        }
                        OrderType.REFUND.orderTitle

                    }
                    OrderType.REFUNDREFUSE.orderType -> {

                        orderDetail_OtherInfoLayout.visibility = View.VISIBLE
                        orderDetail_OtherInfoContent.text = "拒绝退款"

                        OrderType.REFUNDREFUSE.orderTitle
                    }
                    OrderType.SERVICEIN.orderType -> {

                        orderDetail_OtherInfoLayout.visibility = View.VISIBLE
                        orderDetail_OtherInfoContent.text = "客服介入处理中"
                        OrderType.SERVICEIN.orderTitle
                    }
                    OrderType.PREDICTED.orderType -> {
                        OrderType.PREDICTED.orderTitle
                    }
                    OrderType.SELLED.orderType -> {
                        OrderType.SELLED.orderTitle
                    }
                    OrderType.CLOSED.orderType -> {
                        OrderType.CLOSED.orderTitle
                    }
                    else -> {
                        ""
                    }

                }


        GlideUtils.loadCircleImg(this, item?.avatar, orderDetail_Head)

        orderDetail_OrderNum.text = item?.orderCode

        orderDetail_OrderTime.text = "${TimeDateUtils.stampToDate(item?.orderDate ?: 0)}"

        orderDetail_PointTime.text = item?.appointmentTime
        orderDetail_ServiceTime.text = "${item?.serviceTime}分钟"
        orderDetail_Money.text = "￥${item?.price}"
        orderDetail_PayMoney.text = "￥${item?.realPrice}"
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {
        val op = TopBarOption()
        op.isShowBar = true
        op.mainTitle = "订单详情"
        return op
    }


    private fun refundOrder(isAgree: Boolean) {


        MsgDialogHelper.showNormalDialog(this, true, "是否${if (isAgree) "同意" else "拒绝"}退款?", "", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {


                OrderM().orderRefund(item?.orderId ?: 0, isAgree, object : HttpReqListener(this@OrderDetailActivity) {
                    override fun reqOk(result: ResHttpResult) {
                        item?.status = if (isAgree) OrderType.INREFUND.orderType else OrderType.REFUNDREFUSE.orderType

                        MsgDialogHelper.showStateDialog(this@OrderDetailActivity, "操作成功", true, object : MsgDialogHelper.DialogOndismissListener {
                            override fun onDismiss(context: Context) {

                                val result = Intent()
                                val bund = Bundle()
                                bund.putSerializable("item", item)
                                result.putExtras(bund)
                                setResult(Activity.RESULT_OK, result)
                                finish()
                            }

                        })

                    }

                    override fun withoutData(code: Int, msg: String) {
                        NormalFunUtils.showToast(this@OrderDetailActivity, msg)
                    }

                    override fun error(errorInfo: String) {
                        MsgDialogHelper.showStateDialog(this@OrderDetailActivity, "操作失败", true)
                    }
                })


            }

            override fun onNagetive() {
            }

        })

    }
}