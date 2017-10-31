package com.fuyoul.sanwenseller.structure.presenter

import android.app.Activity
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reqhttp.ReqEditBaby
import com.fuyoul.sanwenseller.bean.reqhttp.ReqReleaseBaby
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpBabyItem
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResQiNiuBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.helper.HttpDialogHelper
import com.fuyoul.sanwenseller.helper.QiNiuHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.listener.QiNiuUpLoadListener
import com.fuyoul.sanwenseller.structure.model.EditBabyM
import com.fuyoul.sanwenseller.structure.view.EditBabyV
import com.fuyoul.sanwenseller.utils.NormalFunUtils

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
class EditBabyP(editBabyV: EditBabyV) : BaseP<EditBabyM, EditBabyV>(editBabyV) {
    override fun getModelImpl(): EditBabyM = EditBabyM()

    fun editBaby(activity: Activity, item: ResHttpBabyItem, addImgs: List<String>, deleteImgs: List<String>) {

        val data = ReqEditBaby()
        data.detail = item.introduce
        data.goodsClassifyId = item.goodsClassifyId
        data.name = item.goodsName
        data.goodsId = item.goodsId
        data.price = item.price

        val imageBean = ReqEditBaby.ImgsBean()
        imageBean.add = addImgs
        imageBean.delete = deleteImgs
        data.imgs = imageBean

        getModelImpl().editBaby(data, object : HttpReqListener(activity) {
            override fun reqOk(result: ResHttpResult) {
                NormalFunUtils.showToast(activity, result.msg ?: "编辑成功,请刷新")
                activity.finish()
            }

            override fun withoutData(code: Int, msg: String) {

                if (code == Code.HTTP_SUCCESS) {
                    NormalFunUtils.showToast(activity, "编辑成功,请刷新")
                    activity.finish()
                } else {
                    NormalFunUtils.showToast(activity, msg)
                }

            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(activity, errorInfo)
            }

        })
    }


    fun releaseBaby(activity: Activity, data: ReqReleaseBaby) {

        val imgs = ArrayList<String>()
        imgs.add(data.img)
        QiNiuHelper.multQiNiuUpLoad(activity, imgs, object : QiNiuUpLoadListener {
            override fun complete(path: List<ResQiNiuBean>) {
                data.img = path[0].key
                doReleaseBaby(activity, data)
            }

            override fun error(error: String) {
                HttpDialogHelper.dismisss()
                NormalFunUtils.showToast(activity, error)
            }

        })

    }

    private fun doReleaseBaby(activity: Activity, data: ReqReleaseBaby) {
        getModelImpl().releaseBaby(data, object : HttpReqListener(activity) {
            override fun reqOk(result: ResHttpResult) {

                NormalFunUtils.showToast(activity, result.msg ?: "发布成功,请刷新")
            }

            override fun withoutData(code: Int, msg: String) {

                if (code == Code.HTTP_SUCCESS) {

                    activity.finish()
                }
                NormalFunUtils.showToast(activity, msg)
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(activity, errorInfo)
            }

        })
    }
}