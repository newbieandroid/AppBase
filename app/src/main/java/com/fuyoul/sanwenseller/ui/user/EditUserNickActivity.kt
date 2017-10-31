package com.fuyoul.sanwenseller.ui.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.NormalFunUtils
import kotlinx.android.synthetic.main.editusernick.*
import kotlinx.android.synthetic.main.includetopbar.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\28 0028
 *  @Desc:
 */
class EditUserNickActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {
    private var isCanSubmit = true//是否可以提交

    companion object {
        fun start(activity: Activity, nick: String) {

            activity.startActivityForResult(Intent(activity, EditUserNickActivity::class.java)
                    .putExtra("nick", nick), Code.REQ_EDITNICK)
        }
    }

    override fun setLayoutRes(): Int = R.layout.editusernick

    override fun initData(savedInstanceState: Bundle?) {

        editUserNick.setText(intent.getStringExtra("nick"))
        editUserNick.setSelection(if (editUserNick.text.isNotEmpty()) editUserNick.text.length else 0)

    }

    override fun setListener() {

        editUserNickClear.setOnClickListener {
            editUserNick.setText("")
        }

        editUserNick.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                if (TextUtils.equals(p0?.toString(), "填写昵称")) {
                    p0?.clear()
                    editUserNickClear.visibility = View.GONE
                    isCanSubmit = false
                    toolbarChildTitle.setTextColor(resources.getColor(R.color.color_888888))
                } else {

                    if (p0?.isNotEmpty() == true) {
                        editUserNickClear.visibility = View.VISIBLE
                        isCanSubmit = true
                        toolbarChildTitle.setTextColor(resources.getColor(R.color.color_3CC5BC))
                    } else {
                        editUserNickClear.visibility = View.GONE
                        isCanSubmit = false
                        toolbarChildTitle.setTextColor(resources.getColor(R.color.color_888888))
                    }
                }

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


        op.navigationListener = View.OnClickListener {
            NormalFunUtils.changeKeyBord(this, false, editUserNick)
            finish()
        }

        op.mainTitle = "修改昵称"
        op.childTitleColor = R.color.color_888888
        op.childTitle = "完成"
        op.childListener = View.OnClickListener {
            if (isCanSubmit) {
                NormalFunUtils.changeKeyBord(this, false, editUserNick)
                setResult(Activity.RESULT_OK, Intent().putExtra("nick", editUserNick.text.toString()))
                finish()
            }
        }
        return op
    }

}