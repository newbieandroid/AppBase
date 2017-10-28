package com.fuyoul.sanwenseller.ui.normal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.base.BaseViewHolder
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.github.chrisbanes.photoview.PhotoView
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.imgpreview.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\28 0028
 *  @Desc:
 */
class ImgPreviewActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {


    private var position = 0
    private var datas: ArrayList<String>? = null


    companion object {


        val RESULT = "result"

        val INDEX = "INDEX"
        val DATAS = "DATAS"
        val DELETE = "delete"

        fun startPreview(context: Context, index: Int, datas: ArrayList<String>) {
            val intent = Intent(context, ImgPreviewActivity::class.java)
            intent.putExtra(INDEX, index)
            intent.putStringArrayListExtra(DATAS, datas)
            intent.putExtra(DELETE, false)
            context.startActivity(intent)

            (context as Activity).overridePendingTransition(R.anim.scale_dialog_in_anim, R.anim.scale_dialog_out_anim)
        }

        fun startPreviewWithDelete(context: Activity, index: Int, datas: ArrayList<String>) {
            val intent = Intent(context, ImgPreviewActivity::class.java)
            intent.putExtra(INDEX, index)
            intent.putStringArrayListExtra(DATAS, datas)
            intent.putExtra(DELETE, true)
            context.startActivityForResult(intent, Code.REQ_IMGPREVIEW)

            context.overridePendingTransition(R.anim.scale_dialog_in_anim, R.anim.scale_dialog_out_anim)
        }
    }

    override fun setLayoutRes(): Int = R.layout.imgpreview

    override fun initData(savedInstanceState: Bundle?) {

        datas = intent.getStringArrayListExtra(DATAS)
        position = intent.getIntExtra(INDEX, 0)


        if (intent.getBooleanExtra(DELETE, false)) {
            preview_Delete.visibility = View.VISIBLE
        } else {
            preview_Delete.visibility = View.GONE
        }


        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        imgOfPreview.layoutManager = manager


        imgOfPreview.adapter = object : RecyclerView.Adapter<BaseViewHolder>() {

            override fun getItemCount(): Int {

                return if (datas == null) {
                    0
                } else {
                    datas!!.size
                }
            }

            override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {

                val view = holder?.itemView?.findViewById<PhotoView>(R.id.previewImg)

                Glide.with(this@ImgPreviewActivity)
                        .load(datas!![position])
                        .thumbnail(0.1f)
                        .error(R.mipmap.ic_launcher)
                        .into(view)

            }

            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                val view = layoutInflater.inflate(R.layout.item_imgpreview, parent, false)
                AutoUtils.auto(view)
                return BaseViewHolder(view)
            }

        }
        PagerSnapHelper().attachToRecyclerView(imgOfPreview)
        imgOfPreview.scrollToPosition(position)
    }

    override fun setListener() {

        preview_Delete.setOnClickListener {
            datas!!.removeAt(position)
            imgOfPreview.adapter.notifyDataSetChanged()
            setIndexText()

        }

        imgOfPreview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                setIndexText()
            }

        })
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption = TopBarOption()

    override fun isFullScreen(): Boolean = true

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {


        if (keyCode == KeyEvent.KEYCODE_BACK) {


            setResult(Activity.RESULT_OK, Intent().putStringArrayListExtra(RESULT, datas))
            finish()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    fun setIndexText() {
        val manager = imgOfPreview!!.layoutManager as LinearLayoutManager
        val total = manager.itemCount

        if (total == 0) {
            setResult(Activity.RESULT_OK, Intent().putStringArrayListExtra(RESULT, datas))
            finish()
        } else {
            position = manager.findFirstVisibleItemPosition()
            previewIndex.text = "${position + 1}/$total"
        }

    }
}