package com.fuyoul.sanwenseller.ui.main

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
import com.fuyoul.sanwenseller.ui.main.main.BabyManagerFragment
import com.fuyoul.sanwenseller.ui.main.main.MainFragment
import com.fuyoul.sanwenseller.ui.main.main.MyFragment
import com.fuyoul.sanwenseller.utils.AddFragmentUtils
import com.netease.nim.uikit.StatusBarUtils
import com.netease.nim.uikit.recent.RecentContactsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.includetopbar.*
import android.Manifest
import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.helper.ActivityStateHelper
import com.fuyoul.sanwenseller.helper.LoginOutHelper
import com.fuyoul.sanwenseller.helper.MsgDialogHelper
import com.fuyoul.sanwenseller.im.reminder.ReminderItem
import com.fuyoul.sanwenseller.im.reminder.ReminderManager
import com.fuyoul.sanwenseller.im.reminder.SystemMessageUnreadManager
import com.fuyoul.sanwenseller.im.session.extension.StickerAttachment
import com.fuyoul.sanwenseller.ui.others.ActivityMsgListActivity
import com.fuyoul.sanwenseller.ui.others.SystemMsgListActivity
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import com.fuyoul.sanwenseller.utils.NormalFunUtils.showToast
import com.netease.nim.uikit.NimUIKit
import com.netease.nim.uikit.common.badger.Badger
import com.netease.nim.uikit.recent.RecentContactsCallback
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.SystemMessageService
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.UserInfoProvider
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import permissions.dispatcher.*
import java.util.*

/**
 *  Auther: chen
 *  Creat at: 2017\10\10 0010
 *  Desc:
 */
@RuntimePermissions
class MainActivity : BaseActivity<EmptyM, EmptyV, EmptyP>(), ReminderManager.UnreadNumChangedCallback {


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


        registIm()
        requestSystemMessageUnreadCount()

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

                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount)

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

    var isFinish = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isFinish) {

                ActivityStateHelper.removeAll()

            } else {
                showToast(this, "提示：再按一次推出程序")
                isFinish = true
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        isFinish = false
                    }
                }, 1500)

            }
            return true
        }

        return super.onKeyDown(keyCode, event)

    }

    //=========================消息相关


    override fun onDestroy() {

        unregistIm()
        registMsgReveiver(false)
        super.onDestroy()
    }

    /**
     * 注册未读消息数量观察者
     */
    private fun registerMsgUnreadInfoObserver(register: Boolean) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this)
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this)
        }
    }

    override fun onUnreadNumChanged(item: ReminderItem?) {
        setMsgNum(item!!.unread)
    }

    private fun registIm() {

        NIMClient.getService(AuthServiceObserver::class.java).observeOnlineStatus(userStatusObserver, true)
        registerMsgUnreadInfoObserver(true)
        registMsgReveiver(true)
        NIMClient.getService(MsgService::class.java).queryRecentContacts().setCallback(object : RequestCallback<List<RecentContact>> {
            override fun onException(p0: Throwable?) {
            }

            override fun onFailed(p0: Int) {
            }

            override fun onSuccess(p0: List<RecentContact>?) {

                val count = (0 until p0!!.size).sumBy { p0[it].unreadCount }

                Log.e("csl", "----首页查询最近联系人未读消息条数------$count--")
                setMsgNum(count)
            }

        })
    }

    private fun unregistIm() {
        NIMClient.getService(AuthServiceObserver::class.java).observeOnlineStatus(userStatusObserver, false)
        registerMsgUnreadInfoObserver(false)
        registMsgReveiver(false)

    }


    //如果是异地登录
    private val userStatusObserver = com.netease.nimlib.sdk.Observer<StatusCode> { code ->

        if (code.wontAutoLogin()) {
            LoginOutHelper.accountLoginOut(this@MainActivity, true)
        }
    }

    /**
     * 消息接收监听
     */
    fun registMsgReveiver(isReg: Boolean) {
        NIMClient.getService(MsgServiceObserve::class.java)
                .observeReceiveMessage(incomingMessageObserver, isReg)
    }

    private fun requestSystemMessageUnreadCount() {
        val unread = NIMClient.getService(SystemMessageService::class.java).querySystemMessageUnreadCountBlock()
        SystemMessageUnreadManager.getInstance().sysMsgUnreadCount = unread
        ReminderManager.getInstance().updateContactUnreadNum(unread)
    }

    private val incomingMessageObserver =
            com.netease.nimlib.sdk.Observer<List<IMMessage>> { list ->

                var unreadNum = getUnReadCount()
                (0 until list.size)
                        .map { list[it] }
                        .filter { it.direct == MsgDirectionEnum.In && it.status == MsgStatusEnum.unread }
                        .forEach { unreadNum += 1 }

                Log.e("csl", "---------首页收到新消息未读消息条数--$unreadNum-")
                setMsgNum(unreadNum)
            }


    private fun getUnReadCount(): Int {

        return try {
            Integer.parseInt(if (TextUtils.isEmpty(msgCount.text.toString())) "0" else msgCount.text.toString())
        } catch (e: NumberFormatException) {
            100
        }

    }

    private fun setMsgNum(num: Int) {

        when {
            num > 99 -> {
                msgCount.visibility = View.VISIBLE
                msgCount.text = "99+"
            }
            num in 1..99 -> {
                msgCount.visibility = View.VISIBLE
                msgCount.text = "$num"
            }
            else -> msgCount.visibility = View.GONE
        }
        Badger.updateBadgerCount(num)//桌面添加未读消息条数
    }

}