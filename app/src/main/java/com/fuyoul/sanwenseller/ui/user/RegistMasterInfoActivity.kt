package com.fuyoul.sanwenseller.ui.user

import android.os.Bundle
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.fuyoul.sanwenseller.bean.others.MultDialogBean
import com.fuyoul.sanwenseller.bean.reqhttp.ReqRegistMasterInfo
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.MultDialogListener
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.fuyoul.sanwenseller.utils.PhotoSelectUtils
import com.youquan.selector.Matisse
import kotlinx.android.synthetic.main.registmasterinfolayout.*
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnNeverAskAgain

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:填写预测师注册信息
 */
@RuntimePermissions
class RegistMasterInfoActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    private var reqRegistMasterInfo: ReqRegistMasterInfo? = null
    private var imgPathZ = ""//本地的正面图片地址
    private var imgPathF = ""//本地的反面图片地址

    private var photoUtils: PhotoSelectUtils = PhotoSelectUtils()

    companion object {
        fun start(context: Context, reqRegistMasterInfo: ReqRegistMasterInfo) {

            val intent = Intent(context, RegistMasterInfoActivity::class.java)
            val bund = Bundle()
            bund.putSerializable("data", reqRegistMasterInfo)
            intent.putExtras(bund)
            context.startActivity(intent)

        }
    }

    override fun setLayoutRes(): Int = R.layout.registmasterinfolayout

    override fun initData(savedInstanceState: Bundle?) {
        reqRegistMasterInfo = intent.extras.getSerializable("data") as ReqRegistMasterInfo
    }

    override fun setListener() {


        cardPicLayoutZ.setOnClickListener {

            takePicWithPermissionCheck(this, Code.REQ_CAMERA_IDCARD_Z, Code.REQ_SELECTIMGS_IDCARD_Z)
        }

        cardPicLayoutF.setOnClickListener {

            takePicWithPermissionCheck(this, Code.REQ_CAMERA_IDCARD_F, Code.REQ_SELECTIMGS_IDCARD_F)

        }

        /*下一步*/
        registMasterInfoBtn.setOnClickListener {


            when {
                TextUtils.isEmpty(masterName.text) -> {
                    NormalFunUtils.showToast(this, "请输入真实姓名")

                }
                TextUtils.isEmpty(idCard.text) -> {
                    NormalFunUtils.showToast(this, "请输入身份证号")
                }
                TextUtils.isEmpty(bankName.text) -> {
                    NormalFunUtils.showToast(this, "请输入开户银行名称")

                }
                TextUtils.isEmpty(bankNum.text) -> {
                    NormalFunUtils.showToast(this, "请输入银行开户卡号")

                }
                TextUtils.isEmpty(imgPathZ) -> {
                    NormalFunUtils.showToast(this, "请选择身份照正面照")
                }
                TextUtils.isEmpty(imgPathF) -> {
                    NormalFunUtils.showToast(this, "请选择身份照反面照")
                }
                else -> {
                    reqRegistMasterInfo?.realName = masterName.text.toString()
                    reqRegistMasterInfo?.idCard = idCard.text.toString()
                    reqRegistMasterInfo?.bankName = bankName.text.toString()
                    reqRegistMasterInfo?.bankCard = bankNum.text.toString()
                    reqRegistMasterInfo?.idCardFront = imgPathZ
                    reqRegistMasterInfo?.idCardBack = imgPathF

                    SubmitMasterInfoActivity.start(this, reqRegistMasterInfo!!)

                }
            }

        }
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {
        val op = TopBarOption()
        op.isShowBar = true
        op.mainTitle = "填写真实资料"
        return op
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                Code.REQ_CAMERA_IDCARD_Z -> {
                    imgPathZ = photoUtils.getCapturePath(this)

                    GlideUtils.loadNormalImg(this, imgPathZ, cardImgPicZ)
                }
                Code.REQ_SELECTIMGS_IDCARD_Z -> {

                    imgPathZ = Matisse.obtainPathResult(data)[0]

                    GlideUtils.loadNormalImg(this, imgPathZ, cardImgPicZ)
                }

                Code.REQ_CAMERA_IDCARD_F -> {
                    imgPathF = photoUtils.getCapturePath(this)
                    GlideUtils.loadNormalImg(this, imgPathF, cardImgPicF)
                }
                Code.REQ_SELECTIMGS_IDCARD_F -> {

                    imgPathF = Matisse.obtainPathResult(data)[0]

                    GlideUtils.loadNormalImg(this, imgPathF, cardImgPicF)
                }
            }

        }
    }


    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun takePic(activity: Activity, cameraReqCode: Int, imgsReqCode: Int) {


        NormalFunUtils.changeKeyBord(activity, false, masterName)
        NormalFunUtils.changeKeyBord(activity, false, idCard)
        NormalFunUtils.changeKeyBord(activity, false, bankName)
        NormalFunUtils.changeKeyBord(activity, false, bankNum)


        val multItem = ArrayList<MultDialogBean>()

        val camera = MultDialogBean()
        camera.title = "拍照获取"
        multItem.add(camera)
        val gallary = MultDialogBean()
        gallary.title = "相册获取"
        multItem.add(gallary)

        MsgDialogHelper.showMultItemDialog(activity, multItem, "取消", object : MultDialogListener {
            override fun onItemClicked(position: Int) {

                when (position) {
                    0 -> {
                        photoUtils.doCapture(activity, cameraReqCode)
                    }
                    1 -> {
                        photoUtils.doSelect(activity, 1, null, imgsReqCode)
                    }
                }

            }

        })
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun takePicR(request: PermissionRequest) {
        MsgDialogHelper.showNormalDialog(this, false, "温馨提示", "缺少必要权限,是否进行授权?", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {
                request.proceed()
            }

            override fun onNagetive() {
                request.cancel()
            }

        })
    }

    @OnPermissionDenied(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun takePicD() {
        NormalFunUtils.showToast(this, "缺少必要权限,请前往权限管理中心开启对应权限")
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun takePicN() {
        NormalFunUtils.showToast(this, "缺少必要权限,请前往权限管理中心开启对应权限")
    }
}