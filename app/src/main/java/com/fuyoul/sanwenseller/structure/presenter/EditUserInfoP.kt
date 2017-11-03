package com.fuyoul.sanwenseller.structure.presenter

import android.app.Activity
import android.content.Context
import android.util.Log
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reqhttp.ReqEditUserInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.bean.reshttp.ResQiNiuBean
import com.fuyoul.sanwenseller.configs.UrlInfo.EDITUSERINFO
import com.fuyoul.sanwenseller.helper.HttpDialogHelper
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.helper.QiNiuHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.listener.QiNiuUpLoadListener
import com.fuyoul.sanwenseller.structure.model.EditUserInfoM
import com.fuyoul.sanwenseller.structure.view.EditUserInfoV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.lzy.okgo.OkGo
import org.litepal.crud.DataSupport

/**
 *  @author: chen
 *  @CreatDate: 2017\10\28 0028
 *  @Desc:
 */
class EditUserInfoP(editUserInfoV: EditUserInfoV) : BaseP<EditUserInfoM, EditUserInfoV>(editUserInfoV) {
    override fun getModelImpl(): EditUserInfoM = EditUserInfoM()


    fun upInfo(activity: Activity, info: ResLoginInfoBean) {


        HttpDialogHelper.showDialog(activity, true, false)

        //如果是本地图片,先上传到七牛
        if (info.avatar != null && !info.avatar.startsWith("http")) {
            val imgs = ArrayList<String>()
            imgs.add(info.avatar)
            QiNiuHelper.multQiNiuUpLoad(activity, imgs, object : QiNiuUpLoadListener {
                override fun complete(path: List<ResQiNiuBean>) {
                    doReq(activity, info, path[0].key)
                }

                override fun error(error: String) {
                    HttpDialogHelper.dismisss()
                    NormalFunUtils.showToast(activity, error)
                }

            })
        } else {
            doReq(activity, info, null)
        }
    }


    private fun doReq(activity: Activity, info: ResLoginInfoBean, avatar: String?) {

        val data = ReqEditUserInfo()
        data.avatar = avatar ?: info.avatar
        data.nickname = info.nickname
        data.gender = info.gender
        data.provinces = info.provinces
        data.city = info.city
        data.selfExp = info.selfExp
        data.selfInfo = info.selfInfo


        Log.e("csl", "更新个人资料:${JSON.toJSONString(data)}")

        OkGo.post<ResHttpResult>(EDITUSERINFO)
                .upJson(JSON.toJSONString(data))
                .execute(object : HttpReqListener(activity) {
                    override fun reqOk(result: ResHttpResult) {

                        val backInfo = JSON.parseObject(result.data.toString(), ResLoginInfoBean::class.java)
                        backInfo.updateAll()

                        MsgDialogHelper.showStateDialog(activity, "资料更新成功", true, object : MsgDialogHelper.DialogOndismissListener {
                            override fun onDismiss(context: Context) {

                                activity.setResult(Activity.RESULT_OK)
                                activity.finish()

                            }

                        })

                    }

                    override fun withoutData(code: Int, msg: String) {

                        NormalFunUtils.showToast(activity, msg)
                    }

                    override fun error(errorInfo: String) {
                        NormalFunUtils.showToast(activity, errorInfo)
                    }

                })

    }

}