package com.fuyoul.sanwenseller.structure.presenter

import android.app.Activity
import android.content.Context
import android.util.Log
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reqhttp.ReqEditBaby
import com.fuyoul.sanwenseller.bean.reqhttp.ReqReleaseBaby
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpBabyItem
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResQiNiuBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.helper.HttpDialogHelper
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
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
        data.imgs = ReqEditBaby.ImgsBean()
        //如果是本地图片
        if (addImgs.isNotEmpty() && !addImgs[0].startsWith("http")) {


            data.imgs.add = addImgs
            data.imgs.delete = deleteImgs


            HttpDialogHelper.showDialog(activity, true, false)

            QiNiuHelper.multQiNiuUpLoad(activity, addImgs, object : QiNiuUpLoadListener {
                override fun complete(path: List<ResQiNiuBean>) {

                    val result = ArrayList<String>()
                    result.add(path[0].key!!)
                    data.imgs.add = result

                    edit(activity, data)
                }

                override fun error(error: String) {

                    NormalFunUtils.showToast(activity, "更改图片失败,请重试")
                    HttpDialogHelper.dismisss()

                }

            })

        } else {
            edit(activity, data)
        }
    }

    private fun edit(activity: Activity, data: ReqEditBaby) {

        getModelImpl().editBaby(data, object : HttpReqListener(activity) {
            override fun reqOk(result: ResHttpResult) {

                MsgDialogHelper.showStateDialog(activity, "编辑成功", true, object : MsgDialogHelper.DialogOndismissListener {
                    override fun onDismiss(context: Context) {

                        activity.setResult(Activity.RESULT_OK)
                        activity.finish()

                    }

                })

            }

            override fun withoutData(code: Int, msg: String) {

                if (code == Code.HTTP_SUCCESS) {
                    MsgDialogHelper.showStateDialog(activity, "编辑成功", true, object : MsgDialogHelper.DialogOndismissListener {
                        override fun onDismiss(context: Context) {

                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()

                        }

                    })
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

        HttpDialogHelper.showDialog(activity, true, false)

        val localPath = ArrayList<String>()
        localPath.add(data.imgs)
        QiNiuHelper.multQiNiuUpLoad(activity, localPath, object : QiNiuUpLoadListener {
            override fun complete(path: List<ResQiNiuBean>) {
                data.imgs = path[0].key
                doReleaseBaby(activity, data)
            }

            override fun error(error: String) {
                HttpDialogHelper.dismisss()
                NormalFunUtils.showToast(activity, "图片上传失败,请重试")
            }

        })

    }

    private fun doReleaseBaby(activity: Activity, data: ReqReleaseBaby) {

        Log.e("csl", "发布宝贝实体：${JSON.toJSONString(data)}")

        getModelImpl().releaseBaby(data, object : HttpReqListener(activity) {
            override fun reqOk(result: ResHttpResult) {

                MsgDialogHelper.showStateDialog(activity, "发布成功", true, object : MsgDialogHelper.DialogOndismissListener {
                    override fun onDismiss(context: Context) {

                        activity.setResult(Activity.RESULT_OK)
                        activity.finish()

                    }

                })

            }

            override fun withoutData(code: Int, msg: String) {

                if (code == Code.HTTP_SUCCESS) {

                    MsgDialogHelper.showStateDialog(activity, "发布成功", true, object : MsgDialogHelper.DialogOndismissListener {
                        override fun onDismiss(context: Context) {

                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()

                        }

                    })
                }
                NormalFunUtils.showToast(activity, msg)
            }

            override fun error(errorInfo: String) {
                NormalFunUtils.showToast(activity, errorInfo)
            }

        })
    }
}