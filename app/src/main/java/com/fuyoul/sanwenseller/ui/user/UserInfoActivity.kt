package com.fuyoul.sanwenseller.ui.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.Data
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.utils.GlideUtils
import com.youquan.selector.Matisse
import kotlinx.android.synthetic.main.userinfolayout.*
import org.litepal.crud.DataSupport
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import com.fuyoul.sanwenseller.bean.others.MultDialogBean
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.listener.KeyBordChangerListener
import com.fuyoul.sanwenseller.listener.MultDialogListener
import com.fuyoul.sanwenseller.structure.model.EditUserInfoM
import com.fuyoul.sanwenseller.structure.presenter.EditUserInfoP
import com.fuyoul.sanwenseller.structure.view.EditUserInfoV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.fuyoul.sanwenseller.utils.PhotoSelectUtils
import com.fuyoul.sanwenseller.widgets.pickerview.InitCityDataHelper
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
class UserInfoActivity : BaseActivity<EditUserInfoM, EditUserInfoV, EditUserInfoP>() {


    private var photoUtils: PhotoSelectUtils = PhotoSelectUtils()

    private val userInfo = DataSupport.findFirst(ResLoginInfoBean::class.java)
    private var headPath: String? = ""


    companion object {
        fun start(activity: Activity) {
            activity.startActivityForResult(Intent(activity, UserInfoActivity::class.java), Code.REQ_USERINFO)
        }
    }


    override fun setLayoutRes(): Int = R.layout.userinfolayout

    override fun initData(savedInstanceState: Bundle?) {
        headPath = userInfo.avatar
        GlideUtils.loadCircleImg(this, headPath, userHeadInfo, false, 0, R.mipmap.ic_my_wdltx, R.mipmap.ic_my_wdltx)

        userNickInfo.text = userInfo.nickname
        editExp.setText(userInfo.selfExp)
        editDes.setText(userInfo.selfInfo)

        if (userInfo.gender < 0 || userInfo.gender == Data.MAN) {
            userSexInfo_Man.isChecked = true
        } else {
            userSexInfo_Woman.isChecked = true
        }


        if (TextUtils.isEmpty(userInfo.provinces) || TextUtils.isEmpty(userInfo.city)) {
            userAddressInfo.text = "请选择地址"
        } else {
            userAddressInfo.text = "${userInfo.provinces}${userInfo.city}"
        }


    }

    override fun setListener() {

        registKeyBordListener(userInfoContent, editExp, object : KeyBordChangerListener {
            override fun onShow(height: Int, srollHeight: Int) {
                if (editExp.isFocused) {
                    userInfoContent.scrollBy(0, srollHeight + height)

                } else if (editDes.isFocused) {
                    userInfoScroll.scrollBy(0, height)
                }
            }

            override fun onHidden() {

                if (editExp.isFocused) {
                    userInfoContent.scrollTo(0, 0)

                } else if (editDes.isFocused) {
                    userInfoScroll.scrollTo(0, 0)
                }

            }

        })

        userAddressInfo.setOnClickListener {


            NormalFunUtils.changeKeyBord(this, false, editExp)
            NormalFunUtils.changeKeyBord(this, false, editDes)

            InitCityDataHelper.getInstance().showAddress(this,
                    { options1, options2, options3, v ->
                        val province = InitCityDataHelper.getInstance().city[options1].name
                        val city = InitCityDataHelper.getInstance().province[options1][options2]
                        val address = InitCityDataHelper.getInstance().address[options1][options2][options3]
                        userAddressInfo.text = "$province$city$address"

                        userInfo.provinces = province
                        userInfo.city = city
                    })
        }

        userNickInfo.setOnClickListener {
            EditUserNickActivity.start(this, userNickInfo.text.toString())
        }

        if (userInfo.gender < 0) {//性别只能设置一次,如果之前没有设置过默认值为-1
            userSexInfo.setOnCheckedChangeListener { radioGroup, i ->

                when (i) {
                    R.id.userSexInfo_Man -> {
                        userInfo.gender = Data.MAN
                        userInfo.gender = Data.MAN
                    }
                    R.id.userSexInfo_Woman -> {
                        userInfo.gender = Data.WOMAN
                        userInfo.gender = Data.WOMAN
                    }
                }

            }
        }

        userHeadInfo.setOnClickListener {
            initViewImpl().doSelectPic(this)
        }
    }

    override fun getPresenter(): EditUserInfoP = EditUserInfoP(initViewImpl())

    override fun initViewImpl(): EditUserInfoV = object : EditUserInfoV() {
        override fun doSelectPic(activity: Activity) {
            tackPicWithPermissionCheck(activity)
        }
    }

    override fun initTopBar(): TopBarOption {

        val op = TopBarOption()

        op.isShowBar = true
        op.mainTitle = "个人资料"
        op.childTitle = "完成"
        op.childTitleColor = R.color.color_3CC5BC
        op.childListener = View.OnClickListener {
            userInfo.avatar = headPath
            userInfo.nickname = userNickInfo.text.toString()
            userInfo.selfExp = editExp.text.toString()
            userInfo.selfInfo = editDes.text.toString()
            getPresenter().upInfo(this, userInfo)
        }
        return op
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && Code.REQ_SELECTIMGS == requestCode) {
            headPath = Matisse.obtainPathResult(data)[0]
            GlideUtils.loadCircleImg(this, headPath, userHeadInfo)
        } else if (Code.REQ_CAMERA == requestCode && resultCode == Activity.RESULT_OK) {
            headPath = photoUtils.getCapturePath(this)
            GlideUtils.loadCircleImg(this, headPath, userHeadInfo)
        } else if (Code.REQ_EDITNICK == requestCode && resultCode == Activity.RESULT_OK) {
            userInfo.nickname = data!!.getStringExtra("nick")
            userNickInfo.text = userInfo.nickname
        }
    }


    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun tackPic(activity: Activity) {

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
                        photoUtils.doSelect(activity, 1, null)
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
    fun tackPicR(request: PermissionRequest) {

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
    fun tackPicD() {
        NormalFunUtils.showToast(this, "缺少必要权限,请前往权限管理中心开启对应权限")
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun tackPicN() {
        NormalFunUtils.showToast(this, "缺少必要权限,请前往权限管理中心开启对应权限")
    }
}