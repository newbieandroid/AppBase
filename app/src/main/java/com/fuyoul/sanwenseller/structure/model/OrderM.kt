package com.fuyoul.sanwenseller.structure.model

import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reqhttp.ReqOrderList
import com.fuyoul.sanwenseller.bean.reqhttp.ReqOrderRefund
import com.fuyoul.sanwenseller.bean.reqhttp.ReqSingleOrderId
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo
import com.fuyoul.sanwenseller.configs.UrlInfo.CONFIRMORDER
import com.fuyoul.sanwenseller.configs.UrlInfo.ORDERDELETE
import com.fuyoul.sanwenseller.configs.UrlInfo.ORDERREFUND
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class OrderM : BaseM {

    /**获取不同状态的订单列表**/
    fun getData(index: Int, status: Int, ilistener: HttpReqListener) {

        val reqData = ReqOrderList()
        reqData.index = "$index"
        reqData.status = "$status"

        OkGo.post<ResHttpResult>(UrlInfo.ORDERLIST)
                .upJson(JSON.toJSONString(reqData))
                .execute(ilistener)
    }

    /**确认订单**/
    fun confirmOrder(orderId: Long, listener: HttpReqListener) {
        val reqData = ReqSingleOrderId()
        reqData.orderId = orderId
        OkGo.post<ResHttpResult>(CONFIRMORDER).upJson(JSON.toJSONString(reqData))
                .execute(listener)
    }

    /**同意或者拒绝退款**/
    fun orderRefund(orderId: Long, isAgree: Boolean, listener: HttpReqListener) {

        val reqData = ReqOrderRefund()
        reqData.orderId = orderId
        reqData.processingMode = if (isAgree) 0 else 1
        OkGo.post<ResHttpResult>(ORDERREFUND)
                .upJson(JSON.toJSONString(reqData))
                .execute(listener)
    }

    /**删除订单**/
    fun deleteOrder(orderId: Long, listener: HttpReqListener) {

        val reqData = ReqSingleOrderId()
        reqData.orderId = orderId
        OkGo.post<ResHttpResult>(ORDERDELETE)
                .upJson(JSON.toJSONString(reqData))
                .execute(listener)

    }

}