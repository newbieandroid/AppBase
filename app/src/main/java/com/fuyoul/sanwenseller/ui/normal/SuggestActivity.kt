package com.fuyoul.sanwenseller.ui.normal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import kotlinx.android.synthetic.main.suggest.*
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import com.fuyoul.sanwenseller.bean.others.MultDialogBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.MultDialogListener
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.fuyoul.sanwenseller.utils.PhotoSelectUtils
import com.youquan.selector.Matisse
import kotlinx.android.synthetic.main.includetopbar.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnNeverAskAgain

/**
 *  @author: chen
 *  @CreatDate: 2017\10\28 0028
 *  @Desc:
 */
@RuntimePermissions
class SuggestActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {
    private var photoUtils: PhotoSelectUtils = PhotoSelectUtils()
    private val selectPath = ArrayList<String>()//图片
    override fun setLayoutRes(): Int = R.layout.suggest
    override fun initData(savedInstanceState: Bundle?) {
        addIcon(selectPath)
    }


    private var addImg: ImageView? = null
    private var params: LinearLayout.LayoutParams? = null
    private fun addIcon(path: ArrayList<String>) {
        val width = UIUtil.getScreenWidth(this) / 4

        if (addImg == null) {
            params = LinearLayout.LayoutParams(width, width)
            addImg = ImageView(this)
            addImg!!.scaleType = ImageView.ScaleType.FIT_CENTER
            addImg!!.layoutParams = params
            addImg!!.setImageResource(R.mipmap.icon_addimg)
            addImg!!.setOnClickListener {
                if (path.size < 3) {
                    takePicWithPermissionCheck()
                }
            }
        }


        if (path.isEmpty()) {
            suggestImgsLayout.addView(addImg)
        } else {

            suggestImgsLayout.removeAllViews()

            if (path.size < 3) {
                suggestImgsLayout.addView(addImg)
            }


            for (i in 0 until path.size) {

                val itemImg = ImageView(this)
                itemImg.scaleType = ImageView.ScaleType.FIT_CENTER
                itemImg.layoutParams = params
                params!!.setMargins(0, 0, width / 10, 0)
                GlideUtils.loadNormalImg(this, path[i], itemImg)

                itemImg.setOnClickListener {
                    ImgPreviewActivity.startPreviewWithDelete(this, i, path)
                }
                suggestImgsLayout.addView(itemImg, i)
            }

        }


        imgRlease.text = "${path.size}/3"

    }

    override fun setListener() {

        suggestInfo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                suggestInfoRelease.text = "${if (p0?.isEmpty() == true) {
                    toolbarChildTitle.setTextColor(resources.getColor(R.color.color_888888))

                    0
                } else {
                    toolbarChildTitle.setTextColor(resources.getColor(R.color.color_3CC5BC))
                    400 - p0!!.length / 400
                }
                }"
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption {

        val op = TopBarOption()
        op.isShowBar = true
        op.mainTitle = "意见反馈"
        op.navigationListener = View.OnClickListener {
            NormalFunUtils.changeKeyBord(this, false, suggestInfo)
            finish()
        }
        op.childTitle = "完成"
        op.childTitleColor = R.color.color_888888
        op.childListener = View.OnClickListener {
            if (TextUtils.isEmpty(suggestInfo.text)) {
                NormalFunUtils.showToast(this, "请输入返回内容")
            } else {
                //TODO 提交
            }

        }
        return op
    }


    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun takePic() {

        val multItem = ArrayList<MultDialogBean>()

        val camera = MultDialogBean()
        camera.title = "拍照获取"
        multItem.add(camera)
        val gallary = MultDialogBean()
        gallary.title = "相册获取"
        multItem.add(gallary)

        MsgDialogHelper.showMultItemDialog(this, multItem, "取消", object : MultDialogListener {
            override fun onItemClicked(position: Int) {

                when (position) {

                    0 -> {
                        photoUtils.doCapture(this@SuggestActivity)
                    }
                    1 -> {
                        photoUtils.doSelect(this@SuggestActivity, 3, selectPath)
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

        MsgDialogHelper.showNormalDialog(this, true, "温馨提示", "缺少必要权限,是否进行授权?", object : MsgDialogHelper.DialogListener {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                Code.REQ_SELECTIMGS -> {
                    selectPath.clear()
                    selectPath.addAll(Matisse.obtainPathResult(data))
                }
                Code.REQ_CAMERA -> {
                    selectPath.add(photoUtils!!.getCapturePath(this))
                }
                Code.REQ_IMGPREVIEW -> {
                    selectPath.clear()
                    selectPath.addAll(data!!.getStringArrayListExtra(ImgPreviewActivity.RESULT))
                }
            }

            addIcon(selectPath)

        }
    }
}