package com.fuyoul.sanwenseller.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reqhttp.ReqRegistMasterInfo
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.bean.reshttp.ResQiNiuBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.configs.UrlInfo.SUBMITMASTERINFO
import com.fuyoul.sanwenseller.helper.ActivityStateHelper
import com.fuyoul.sanwenseller.helper.HttpDialogHelper
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.helper.QiNiuHelper
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.fuyoul.sanwenseller.listener.KeyBordChangerListener
import com.fuyoul.sanwenseller.listener.QiNiuUpLoadListener
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.lzy.okgo.OkGo
import kotlinx.android.synthetic.main.submitmasterinfo.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
class SubmitMasterInfoActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    companion object {
        fun start(context: Context, reqRegistMasterInfo: ReqRegistMasterInfo) {
            val intent = Intent(context, SubmitMasterInfoActivity::class.java)
            val bund = Bundle()
            bund.putSerializable("data", reqRegistMasterInfo)
            intent.putExtras(bund)
            context.startActivity(intent)
        }
    }

    override fun setLayoutRes(): Int = R.layout.submitmasterinfo

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun setListener() {


        registKeyBordListener(submitMasterContent, masterExpInfo, object : KeyBordChangerListener {
            override fun onShow(keyBordH: Int, srollHeight: Int) {

                if (masterDesInfo.isFocused) {
                    submitMasterScroll.scrollBy(0, keyBordH)
                } else if (masterExpInfo.isFocused) {
                    submitMasterContent.scrollBy(0, srollHeight + keyBordH)
                }

            }

            override fun onHidden() {

                if (masterExpInfo.isFocused) {
                    submitMasterContent.scrollTo(0, 0)

                } else if (masterDesInfo.isFocused) {
                    submitMasterScroll.scrollTo(0, 0)
                }
            }

        })

        val reqRegistMasterInfo = intent.extras.getSerializable("data") as ReqRegistMasterInfo
        val frontImg = reqRegistMasterInfo.idCardFront
        val backImg = reqRegistMasterInfo.idCardBack

        submitMasterInfoBtn.setOnClickListener {

            when {
                TextUtils.isEmpty(masterDesInfo.text) -> {

                    NormalFunUtils.showToast(this, "请输入个人简介")

                }
                TextUtils.isEmpty(masterExpInfo.text) -> {
                    NormalFunUtils.showToast(this, "请输入个人经历")
                }
                else -> {


                    reqRegistMasterInfo.selfInfo = masterDesInfo.text.toString()
                    reqRegistMasterInfo.selfExp = masterExpInfo.text.toString()


                    val imgs = ArrayList<String>()
                    imgs.add(frontImg)
                    imgs.add(backImg)


                    HttpDialogHelper.showDialog(this, true, false)

                    QiNiuHelper.multQiNiuUpLoad(this, imgs, object : QiNiuUpLoadListener {
                        override fun complete(path: List<ResQiNiuBean>) {

                            reqRegistMasterInfo.idCardFront = path[0].key
                            reqRegistMasterInfo.idCardBack = path[1].key

                            val reqData = JSON.toJSONString(reqRegistMasterInfo)
                            Log.e("csl", "提交审核参数:$reqData")

                            OkGo.post<ResHttpResult>(SUBMITMASTERINFO)
                                    .upJson(reqData)
                                    .execute(object : HttpReqListener(this@SubmitMasterInfoActivity) {
                                        override fun reqOk(result: ResHttpResult) {

                                            MsgDialogHelper.showSingleDialog(this@SubmitMasterInfoActivity, true,
                                                    "资料提交成功",
                                                    "请耐心等待客服人员审核,审核结果将通过短信告知", "我知道了", object : MsgDialogHelper.DialogListener {
                                                override fun onPositive() {
                                                    ActivityStateHelper.removeAll()
                                                }

                                                override fun onNagetive() {
                                                }

                                            })

                                        }

                                        override fun withoutData(code: Int, msg: String) {


                                            if (code == Code.HTTP_SUCCESS) {

                                                MsgDialogHelper.showSingleDialog(this@SubmitMasterInfoActivity, true,
                                                        "资料提交成功",
                                                        "请耐心等待客服人员审核,审核结果将通过短信告知", "我知道了", object : MsgDialogHelper.DialogListener {
                                                    override fun onPositive() {
                                                        ActivityStateHelper.removeAll()
                                                    }

                                                    override fun onNagetive() {
                                                    }

                                                })

                                            } else {

                                                NormalFunUtils.showToast(this@SubmitMasterInfoActivity, msg)
                                            }
                                        }

                                        override fun error(errorInfo: String) {

                                            NormalFunUtils.showToast(this@SubmitMasterInfoActivity, errorInfo)
                                        }

                                    })


                        }

                        override fun error(error: String) {

                            HttpDialogHelper.dismisss()
                            NormalFunUtils.showToast(this@SubmitMasterInfoActivity, "证件照上传失败,请重新上传")
                        }

                    })


                }
            }

        }
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {

        val op = TopBarOption()

        op.isShowBar = true
        op.mainTitle = "提交审核"

        return op
    }
}