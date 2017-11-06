package com.fuyoul.sanwenseller.bean.reqhttp

/**
 *  @author: chen
 *  @CreatDate: 2017\11\6 0006
 *  @Desc:是否同意退款
 */
class ReqOrderRefund {

    var processingMode = -1 //0 同意退款  1 ,拒绝退款
    var orderId = 0L
}