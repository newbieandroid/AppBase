package com.fuyoul.sanwenseller.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.ui.baby.EditBabyInfoActivity
import com.fuyoul.sanwenseller.ui.fragment.main.BabyManagerFragment
import com.fuyoul.sanwenseller.ui.fragment.main.MainFragment
import com.fuyoul.sanwenseller.ui.fragment.main.MyFragment
import com.fuyoul.sanwenseller.utils.AddFragmentUtils
import com.netease.nim.uikit.StatusBarUtils
import com.netease.nim.uikit.recent.RecentContactsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.includetopbar.*
import android.Manifest
import android.annotation.SuppressLint
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.im.session.extension.StickerAttachment
import com.fuyoul.sanwenseller.ui.order.ActivityMsgListActivity
import com.fuyoul.sanwenseller.ui.order.SystemMsgListActivity
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.netease.nim.uikit.NimUIKit
import com.netease.nim.uikit.recent.RecentContactsCallback
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.model.RecentContact
import permissions.dispatcher.*
import java.util.ArrayList

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
@RuntimePermissions
class MainActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    private var currentTag = ""
    private val fragments = arrayListOf(MainFragment(), BabyManagerFragment(), RecentContactsFragment(), MyFragment())

    private var addFragmentUtils: AddFragmentUtils? = null

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption = TopBarOption()

    override fun setLayoutRes(): Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        toolbarBack.visibility = View.GONE

        noThingWithPermissionCheck()

        addFragmentUtils = AddFragmentUtils(this, R.id.mainContentLayout)
    }

    override fun setListener() {

        mainBottomLayout.setOnCheckedChangeListener { radioGroup, i ->

            when (i) {
                R.id.mainItem -> {
                    titBarLayout.visibility = View.VISIBLE
                    toolbarTitle.text = "三问"
                    toolbarChildTitle.visibility = View.GONE
                    StatusBarUtils.setTranslucentForImageView(this, titBarLayout)
                    StatusBarUtils.StatusBarLightMode(this, R.color.color_white)
                    msgItem.isChecked = false
                    currentTag = fragments[0].javaClass.name
                    addFragmentUtils?.showFragment(fragments[0], currentTag)
                }
                R.id.babyItem -> {
                    titBarLayout.visibility = View.VISIBLE
                    toolbarChildTitle.visibility = View.VISIBLE
                    toolbarTitle.text = "宝贝管理"
                    toolbarChildTitle.text = "发布宝贝"
                    toolbarChildTitle.setTextColor(resources.getColor(R.color.color_3CC5BC))
                    toolbarChildTitle.setOnClickListener {
                        EditBabyInfoActivity.start(this, null)
                    }

                    StatusBarUtils.setTranslucentForImageView(this, titBarLayout)
                    StatusBarUtils.StatusBarLightMode(this, R.color.color_white)
                    msgItem.isChecked = false
                    currentTag = fragments[1].javaClass.name
                    addFragmentUtils?.showFragment(fragments[1], currentTag)
                }
                R.id.msgItem -> {

                    titBarLayout.visibility = View.VISIBLE
                    toolbarTitle.text = "消息"
                    toolbarChildTitle.visibility = View.GONE
                    StatusBarUtils.setTranslucentForImageView(this, titBarLayout)
                    StatusBarUtils.StatusBarLightMode(this, R.color.color_white)
                    currentTag = fragments[2].javaClass.name
                    addFragmentUtils?.showFragment(fragments[2], currentTag)
                }
                R.id.myItem -> {
                    titBarLayout.visibility = View.GONE
                    msgItem.isChecked = false
                    currentTag = fragments[3].javaClass.name
                    addFragmentUtils?.showFragment(fragments[3], currentTag)
                }
            }

        }

        msgItem.setOnCheckedChangeListener { _, b ->


            if (b) {
                mainItem.isChecked = false
                babyItem.isChecked = false
                myItem.isChecked = false
                mainBottomLayout.check(R.id.msgItem)

            }
        }

        mainItem.isChecked = true


        (fragments[2] as RecentContactsFragment).setCallback(object : RecentContactsCallback {
            override fun onRecentContactsLoaded() {
            }

            override fun onUnreadCountChange(unreadCount: Int) {


            }

            override fun onItemClick(recent: RecentContact?) {

                //如果是系统通知或者活动公告
                if (recent?.contactId.equals(NimUIKit.ACTIVITYCONTACTID)) {

                    startActivity(Intent(this@MainActivity, ActivityMsgListActivity::class.java))

                } else if (recent?.contactId.equals(NimUIKit.NOTIFYCONTACTID)) {

                    startActivity(Intent(this@MainActivity, SystemMsgListActivity::class.java))

                } else {

                    if (PermissionUtils.hasSelfPermissions(this@MainActivity, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {
                        NimUIKit.startP2PSession(this@MainActivity, recent?.contactId)
                    } else {

                        MsgDialogHelper.showNormalDialog(this@MainActivity, true, "缺少必要权限,是否前往开启?", "", "前往", object : MsgDialogHelper.DialogListener {
                            override fun onPositive() {
                                NormalFunUtils.goToAppDetailSettingIntent(this@MainActivity)
                            }

                            override fun onNagetive() {
                            }

                        })

                    }
                }
            }

            override fun getDigestOfAttachment(recent: RecentContact?, attachment: MsgAttachment?): String? {

                if (attachment is StickerAttachment) {
                    return "[贴图]"
                }

                return null
            }

            override fun getDigestOfTipMsg(recent: RecentContact?): String? {

                val msgId = recent!!.recentMessageId
                val uuids = ArrayList<String>(1)
                uuids.add(msgId)
                val msgs = NIMClient.getService(MsgService::class.java).queryMessageListByUuidBlock(uuids)
                if (msgs != null && !msgs.isEmpty()) {
                    val msg = msgs[0]
                    val content = msg.remoteExtension
                    if (content != null && !content.isEmpty()) {
                        return content["content"] as String
                    }
                }

                return null
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        addFragmentUtils?.currentFragment?.onActivityResult(requestCode, resultCode, data)
    }


    @NeedsPermission(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    fun noThing() {


    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    fun noThingR(request: PermissionRequest) {

        MsgDialogHelper.showSingleDialog(this@MainActivity, false, "温馨提示", "缺少必要权限,是否进行授权?", "确定", object : MsgDialogHelper.DialogListener {
            override fun onPositive() {
                request.proceed()
            }

            override fun onNagetive() {
                request.cancel()
            }
        })

    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    fun noThingD() {
        NormalFunUtils.showToast(this@MainActivity, "缺少必要权限,请前往权限管理中心开启对应权限")
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    fun noThingN() {
        NormalFunUtils.showToast(this@MainActivity, "缺少必要权限,请前往权限管理中心开启对应权限")
    }
}