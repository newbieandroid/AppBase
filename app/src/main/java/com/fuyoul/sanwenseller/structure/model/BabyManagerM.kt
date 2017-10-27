package com.fuyoul.sanwenseller.structure.model

import android.content.Context
import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
class BabyManagerM : BaseM {

    fun getData(index: Int, masterId: Long, status: Int, listener: HttpReqListener) {
        OkGo.get<ResHttpResult>(UrlInfo.BABYLIST)
                .params("masterid", masterId)
                .params("status", status)
                .params("index", index)
                .execute(listener)
    }

    /**删除商品**/
    fun deleteBbay(context: Context, babyId: Long, masterId: Long, listener: HttpReqListener) {
        MsgDialogHelper.showNormalDialog(context, true, "确定删除此宝贝", "确定后该宝贝将永久删除", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {

            }

            override fun onNagetive() {
            }

        })

    }

    /**上架商品**/
    fun upToShop(context: Context, babyId: Long, masterId: Long, listener: HttpReqListener) {
        MsgDialogHelper.showNormalDialog(context, true, "确定上架此宝贝", "确定上架该宝贝将重新出售", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {

            }

            override fun onNagetive() {
            }

        })
    }

    /**下架商品**/
    fun downToShop(context: Context, babyId: Long, masterId: Long, listener: HttpReqListener) {
        MsgDialogHelper.showNormalDialog(context, true, "确定下架此宝贝", "确定后该宝贝将暂停出售", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {

            }

            override fun onNagetive() {
            }

        })
    }

}