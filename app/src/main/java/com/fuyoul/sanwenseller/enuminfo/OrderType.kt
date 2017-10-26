package com.fuyoul.sanwenseller.enuminfo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
enum class OrderType {

    ALL("全部", 0),
    TOBEPREDICTED("待预测", 1),
    INPREDICTED("预测中", 2),
    TOBEPAY("待付款", 3),
    TOREPLAY("待评价", 4),
    PAYMENT("交易成功", 5),
    WAITTINGSELLER("等待卖家处理", 6),
    INREFUND("退款处理中", 7),
    REFUND("已退款", 8),
    REFUNDREFUSE("拒绝退款", 9),
    SERVICEIN("客服介入", 10),
    PREDICTED("已预测", 11),
    SELLED("售后", 12);


    var orderTitle: String = ""
    var orderType: Int = 0

    constructor(orderTitle: String, orderType: Int) {
        this.orderTitle = orderTitle
        this.orderType = orderType
    }
}