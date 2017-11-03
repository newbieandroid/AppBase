package com.fuyoul.sanwenseller.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reshttp.ResAppointMentInfo
import com.fuyoul.sanwenseller.configs.Data
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.enuminfo.OrderType
import com.fuyoul.sanwenseller.structure.model.AppointMentInfoM
import com.fuyoul.sanwenseller.structure.presenter.AppointMentInfoP
import com.fuyoul.sanwenseller.structure.view.AppointMentInfoV
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.TimeDateUtils
import com.netease.nim.uikit.NimUIKit
import kotlinx.android.synthetic.main.appointmentinfo.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class AppointMentInfoActivity : BaseActivity<AppointMentInfoM, AppointMentInfoV, AppointMentInfoP>() {

    companion object {

        val ORDERID = "orderId"

        fun start(context: Context, orderId: Long) {
            context.startActivity(Intent(context, AppointMentInfoActivity::class.java).putExtra(ORDERID, orderId))
        }
    }

    override fun setLayoutRes(): Int = R.layout.appointmentinfo

    override fun initData(savedInstanceState: Bundle?) {

        getPresenter().getData(this, intent.getLongExtra(ORDERID, 0))
    }

    override fun setListener() {


    }

    override fun getPresenter(): AppointMentInfoP = AppointMentInfoP(initViewImpl())

    override fun initViewImpl(): AppointMentInfoV = object : AppointMentInfoV() {
        override fun startChat(userInfoId: Long) {
            NimUIKit.startP2PSession(this@AppointMentInfoActivity, "user_$userInfoId")
        }

        override fun setData(result: ResAppointMentInfo) {

            GlideUtils.loadCircleImg(this@AppointMentInfoActivity, result.avatar, avatar, false, 0, R.drawable.nim_avatar_default, R.drawable.nim_avatar_default)
            nickName.text = result.nickname
            state.text = when (result.status) {
                OrderType.TOBEPREDICTED.orderType -> {
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
                    OrderType.WAITTINGSELLER.orderTitle
                }
                OrderType.INREFUND.orderType -> {
                    OrderType.INREFUND.orderTitle
                }
                OrderType.REFUND.orderType -> {
                    OrderType.REFUND.orderTitle
                }
                OrderType.REFUNDREFUSE.orderType -> {
                    OrderType.REFUNDREFUSE.orderTitle
                }
                OrderType.SERVICEIN.orderType -> {
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
            //个人预测信息
            appointTimeInfo.text = if (TextUtils.isEmpty(result.date)) "暂无信息" else TimeDateUtils.solarToLunar(result.date)
            realNameInfo.text = if (TextUtils.isEmpty(result.username)) "暂无信息" else result.username
            sexInfo.text = if (result.sex == Data.WOMAN) "女" else if (result.sex < 0) "暂无信息" else "男"
            addressInfo.text = if (TextUtils.isEmpty(result.birthPlace)) "暂无信息" else result.birthPlace
            birthInfo.text = "阳历：${if (TextUtils.isEmpty(result.birthday)) "暂无信息" else result.birthday}\n" +
                    "阴历：${if (TextUtils.isEmpty(result.birthday)) "暂无信息" else TimeDateUtils.solarToLunar(result.birthday)}"

            GlideUtils.loadNormalImg(this@AppointMentInfoActivity, JSON.parseArray(result.imgs).getJSONObject(0).getString("url"), babyIcon)
            babyTitle.text = result.name
            babyDes.text = result.introduce
            orderNumInfo.text = result.orderCode
            orderTimeInfo.text = TimeDateUtils.stampToDate(result.ordersDate)
            serviceTimeInfo.text = "${result.serviceTime}分钟"
            realPriceInfo.text = "￥${result.totalPrice}"
            salePriceInfo.text = "￥0"
            payPriceInfo.text = "￥${result.totalPayPrice}"

            sendMsgBtn.setOnClickListener {
                initViewImpl().startChat(result.userInfoId)
            }
        }

    }

    override fun initTopBar(): TopBarOption {

        val op = TopBarOption()
        op.isShowBar = true
        op.mainTitle = "预约信息"
        return op
    }
}