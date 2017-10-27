package com.fuyoul.sanwenseller.ui.baby

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.others.MultDialogBean
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpBabyItem
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.enuminfo.BabyType
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.MultDialogListener
import com.fuyoul.sanwenseller.structure.model.EditBabyM
import com.fuyoul.sanwenseller.structure.presenter.EditBabyP
import com.fuyoul.sanwenseller.structure.view.EditBabyV
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.PhotoSelectUtils
import com.youquan.selector.Matisse
import kotlinx.android.synthetic.main.editbabyinfo.*
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.View
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnNeverAskAgain

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:
 */
@RuntimePermissions
class EditBabyInfoActivity : BaseActivity<EditBabyM, EditBabyV, EditBabyP>() {


    private var goodsClassifyId = 0

    private val selectPath = ArrayList<String>()//图片
    private val IMGCOUNT = 1

    private var photoUtils: PhotoSelectUtils = PhotoSelectUtils()

    override fun setLayoutRes(): Int = R.layout.editbabyinfo

    override fun initData(savedInstanceState: Bundle?) {

        if (intent.extras.getSerializable("item") != null) {
            val data = intent.extras.getSerializable("item") as ResHttpBabyItem

            initViewImpl().addImg(this, JSON.parseArray(data.imgs).getJSONObject(0).getString("url"), editBabyPic)

            editBabyName.setText(data.goodsName)
            editBabyPrice.setText("${data.price}")
            editBabyDes.setText("${data.introduce}")
            when (data.goodsClassifyId) {

                BabyType.YFTX.typeId -> {
                    yftxBabyType.isChecked = true
                    editBabyType.text = BabyType.YFTX.title
                    goodsClassifyId = BabyType.YFTX.typeId
                }
                BabyType.BZPP.typeId -> {
                    bzppBabyType.isChecked = true
                    editBabyType.text = BabyType.BZPP.title
                    goodsClassifyId = BabyType.BZPP.typeId

                }
                BabyType.PJCY.typeId -> {
                    pjcyBabyType.isChecked = true
                    editBabyType.text = BabyType.PJCY.title
                    goodsClassifyId = BabyType.PJCY.typeId
                }
                BabyType.HYJT.typeId -> {
                    hyjtBabyType.isChecked = true
                    editBabyType.text = BabyType.HYJT.title
                    goodsClassifyId = BabyType.HYJT.typeId
                }
                BabyType.LCZR.typeId -> {
                    lczrBabyType.isChecked = true
                    editBabyType.text = BabyType.LCZR.title
                    goodsClassifyId = BabyType.LCZR.typeId
                }
                BabyType.SYCY.typeId -> {
                    sycyBabyType.isChecked = true
                    editBabyType.text = BabyType.SYCY.title
                    goodsClassifyId = BabyType.SYCY.typeId
                }
                BabyType.ZYQM.typeId -> {
                    zyqmBabyType.isChecked = true
                    editBabyType.text = BabyType.ZYQM.title
                    goodsClassifyId = BabyType.ZYQM.typeId
                }
                BabyType.ZHYC.typeId -> {
                    zhycBabyType.isChecked = true
                    editBabyType.text = BabyType.ZHYC.title
                    goodsClassifyId = BabyType.ZHYC.typeId
                }

            }
        }
    }


    override fun setListener() {

        registKeyBordListener(editBabyContent, editBabyDes)

        editBabyPic.setOnClickListener {
            initViewImpl().doSelectImg(this)
        }

        editBabyType.setOnClickListener {
            if (editBabyTypeList.visibility == View.GONE) {
                editBabyTypeList.visibility = View.VISIBLE
            }
        }

        editBabyTypeList.setOnCheckedChangeListener { _, i ->

            editBabyTypeList.visibility = View.GONE

            when (i) {
                R.id.yftxBabyType -> {
                    editBabyType.text = BabyType.YFTX.title
                    goodsClassifyId = BabyType.YFTX.typeId
                }
                R.id.bzppBabyType -> {
                    editBabyType.text = BabyType.BZPP.title
                    goodsClassifyId = BabyType.BZPP.typeId
                }
                R.id.pjcyBabyType -> {
                    editBabyType.text = BabyType.PJCY.title
                    goodsClassifyId = BabyType.PJCY.typeId
                }
                R.id.hyjtBabyType -> {
                    editBabyType.text = BabyType.HYJT.title
                    goodsClassifyId = BabyType.HYJT.typeId
                }
                R.id.sycyBabyType -> {
                    editBabyType.text = BabyType.SYCY.title
                    goodsClassifyId = BabyType.SYCY.typeId
                }
                R.id.lczrBabyType -> {
                    editBabyType.text = BabyType.LCZR.title
                    goodsClassifyId = BabyType.LCZR.typeId
                }
                R.id.zyqmBabyType -> {
                    editBabyType.text = BabyType.ZYQM.title
                    goodsClassifyId = BabyType.ZYQM.typeId
                }
                R.id.zhycBabyType -> {
                    editBabyType.text = BabyType.ZHYC.title
                    goodsClassifyId = BabyType.ZHYC.typeId
                }

            }
        }

    }

    override fun getPresenter(): EditBabyP = EditBabyP(initViewImpl())

    override fun initViewImpl(): EditBabyV = object : EditBabyV() {
        override fun addImg(context: Context, path: Any, view: ImageView) {
            GlideUtils.loadNormalImg(context, path, view, R.mipmap.icon_imgloading, R.mipmap.icon_addimg)
        }

        override fun doSelectImg(activity: Activity) {
            doSelectWithPermissionCheck(activity)
        }
    }

    override fun initTopBar(): TopBarOption {
        val option = TopBarOption()
        option.isShowBar = true
        option.mainTitle = if (intent.extras.getSerializable("item") == null) {
            editBabyBtn.text = "发布"
            "发布宝贝"
        } else {
            editBabyBtn.text = "完成"
            "编辑宝贝"
        }
        return option

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Code.REQ_SELECTIMGS -> {
                    selectPath.clear()
                    selectPath.addAll(Matisse.obtainPathResult(data))
                    initViewImpl().addImg(this, selectPath[0], editBabyPic)
                }
                Code.REQ_CAMERA -> {
                    selectPath.clear()
                    selectPath.add(photoUtils.getCapturePath(this))
                    initViewImpl().addImg(this, photoUtils.getCapturePath(this), editBabyPic)
                }
            }

        }
    }


    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun doSelect(activity: Activity) {

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
                        photoUtils.doCapture(activity)
                    }
                    1 -> {
                        photoUtils.doSelect(activity, IMGCOUNT, selectPath)
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
    fun doSelectR(request: PermissionRequest) {

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
    fun doSelectD() {
        NormalFunUtils.showToast(this, "缺少必要权限,请前往权限管理中心开启对应权限")
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun doSelectN() {
        NormalFunUtils.showToast(this, "缺少必要权限,请前往权限管理中心开启对应权限")
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (editBabyTypeList.visibility == View.VISIBLE) {
                editBabyTypeList.visibility = View.GONE
                return true
            }
        }


        return super.onKeyDown(keyCode, event)
    }
}