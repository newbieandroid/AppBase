package com.fuyoul.sanwenseller.structure.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.bean.reqhttp.ReqEditUserInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.bean.reshttp.ResQiNiuBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.UrlInfo.EDITUSERINFO
import com.fuyoul.sanwenseller.helper.HttpDialogHelper
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


    fun upInfo(context: Context, info: ResLoginInfoBean) {


        //如果是本地图片,先上传到七牛
        if (!info.avatar.startsWith("http")) {
            val imgs = ArrayList<String>()
            imgs.add(info.avatar)
            QiNiuHelper.multQiNiuUpLoad(context, imgs, object : QiNiuUpLoadListener {
                override fun complete(path: List<ResQiNiuBean>) {

                    info.avatar = path[0].key
                    doReq(context, info)
                }

                override fun error(error: String) {
                    HttpDialogHelper.dismisss()
                    NormalFunUtils.showToast(context, error)
                }

            })
        } else {
            doReq(context, info)
        }


    }


    private fun doReq(context: Context, info: ResLoginInfoBean) {

        val data = ReqEditUserInfo()
        data.avatar = info.avatar
        data.provinces = info.provinces
        data.gender = info.gender
        data.nickname = info.nickname
        data.city = info.city
        data.self_exp = info.self_exp
        data.self_info = info.self_info

        OkGo.post<ResHttpResult>(EDITUSERINFO).upJson(JSON.toJSONString(data)).execute(object : HttpReqListener(context) {
            override fun reqOk(result: ResHttpResult) {
                NormalFunUtils.showToast(context, result.msg ?: "更新成功")
                info.updateAll()
            }

            override fun withoutData(code: Int, msg: String) {

                if (code == Code.HTTP_SUCCESS) {
                    info.updateAll()
                }

                NormalFunUtils.showToast(context, msg)
            }

            override fun error(errorInfo: String) {

                NormalFunUtils.showToast(context, errorInfo)
            }

        })

    }

}