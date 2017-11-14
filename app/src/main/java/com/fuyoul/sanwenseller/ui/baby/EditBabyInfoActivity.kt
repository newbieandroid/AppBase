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
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.fuyoul.sanwenseller.bean.reqhttp.ReqReleaseBaby
import com.fuyoul.sanwenseller.configs.Code.SERVICETIME
import com.fuyoul.sanwenseller.listener.KeyBordChangerListener
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnNeverAskAgain

/**
 *  @author: chen
 *  @CreatDate: 2017\10\27 0027
 *  @Desc:发布宝贝和编辑宝贝
 */
@RuntimePermissions
class EditBabyInfoActivity : BaseActivity<EditBabyM, EditBabyV, EditBabyP>() {


    private var goodsClassifyId = -1
    private val selectPath = ArrayList<String>()//图片
    private var photoUtils: PhotoSelectUtils = PhotoSelectUtils()


    companion object {
        fun start(activity: Activity, item: ResHttpBabyItem?) {

            val intent = Intent(activity, EditBabyInfoActivity::class.java)
            if (item != null) {
                val bund = Bundle()
                bund.putSerializable("item", item)
                intent.putExtras(bund)
            }
            activity.startActivityForResult(intent, Code.REQ_EDITBABYINFO)

        }
    }

    override fun setLayoutRes(): Int = R.layout.editbabyinfo
    override fun initData(savedInstanceState: Bundle?) {

        if (intent.extras != null && intent.extras.getSerializable("item") != null) {
            val data = intent.extras.getSerializable("item") as ResHttpBabyItem


            selectPath.add(JSON.parseArray(data.imgs).getJSONObject(0).getString("url"))
            initViewImpl().addImg(this, selectPath[0], editBabyPic)

            editBabyName.setText(data.goodsName)
            editBabyPrice.setText("${data.price}")
            editBabyDes.setText("${data.introduce}")
            editBabyServiceTime.setText("${data.serviceTime}")
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

        registKeyBordListener(editBabyCotent, editBabyDes, object : KeyBordChangerListener {
            override fun onShow(keyBordH: Int, srollHeight: Int) {
                if (editBabyDes.isFocused) {
                    editBabyCotent.scrollBy(0, keyBordH)
                }
            }

            override fun onHidden() {
                if (editBabyDes.isFocused) {
                    editBabyCotent.scrollTo(0, 0)
                }
            }

        })

        editBabyBtn.setOnClickListener {

            if (TextUtils.isEmpty(editBabyName.text)) {
                NormalFunUtils.showToast(this, "请输入宝贝名称")
            } else if (TextUtils.isEmpty(editBabyPrice.text)) {
                NormalFunUtils.showToast(this, "请输入宝贝价格")
            } else if (editBabyPrice.text.toString().toInt() == 0) {
                NormalFunUtils.showToast(this, "宝贝价格必须大于0")
            } else if (goodsClassifyId < 0) {
                NormalFunUtils.showToast(this, "请选择宝贝分类")
            } else if (selectPath.size == 0) {
                NormalFunUtils.showToast(this, "请选择宝贝图片")
            } else if (TextUtils.isEmpty(editBabyDes.text)) {
                NormalFunUtils.showToast(this, "请输入宝贝描述")
            } else {
                //编辑
                if (intent.extras != null && intent.extras.getSerializable("item") != null) {

                    val item = intent.extras.getSerializable("item") as ResHttpBabyItem
                    val array = JSON.parseArray((intent.extras.getSerializable("item") as ResHttpBabyItem).imgs)
                    val before = (0 until array.size).map { array.getJSONObject(it).getString("url") }

                    item.goodsName = editBabyName.text.toString()
                    item.introduce = editBabyDes.text.toString()
                    item.goodsClassifyId = goodsClassifyId
                    item.price = editBabyPrice.text.toString().toInt()
                    item.serviceTime = if (TextUtils.isEmpty(editBabyServiceTime.text.toString())) SERVICETIME else editBabyServiceTime.text.toString().toInt()
                    getPresenter().editBaby(this, item, selectPath, before)
                } else {
                    //发布
                    val data = ReqReleaseBaby()
                    data.imgs = selectPath[0]
                    data.goodsName = editBabyName.text.toString()
                    data.price = editBabyPrice.text.toString().toInt()
                    data.category = goodsClassifyId
                    data.introduce = editBabyDes.text.toString()
                    data.serviceTime = if (TextUtils.isEmpty(editBabyServiceTime.text.toString())) SERVICETIME else editBabyServiceTime.text.toString().toInt()
                    getPresenter().releaseBaby(this, data)
                }
            }
        }


        editBabyPic.setOnClickListener {
            initViewImpl().doSelectImg(this)
        }

        editBabyType.setOnClickListener {
            if (editBabyTypeList.visibility == View.GONE) {

                NormalFunUtils.changeKeyBord(this, false, editBabyName)
                NormalFunUtils.changeKeyBord(this, false, editBabyPrice)
                NormalFunUtils.changeKeyBord(this, false, editBabyServiceTime)
                NormalFunUtils.changeKeyBord(this, false, editBabyDes)

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
        option.mainTitle = if (intent.extras == null || intent.extras.getSerializable("item") == null) {
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
                        photoUtils.doSelect(activity, 1, selectPath)
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