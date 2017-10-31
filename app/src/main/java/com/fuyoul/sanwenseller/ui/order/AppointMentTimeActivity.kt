package com.fuyoul.sanwenseller.ui.order

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseActivity
import com.fuyoul.sanwenseller.configs.TopBarOption
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.ui.fragment.appointment.NormalTestFragment
import com.fuyoul.sanwenseller.ui.fragment.appointment.QuickTestFragment
import com.fuyoul.sanwenseller.utils.AddFragmentUtils
import kotlinx.android.synthetic.main.appointmentlayout.*
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class AppointMentTimeActivity : BaseActivity<EmptyM, EmptyV, EmptyP>() {
    private val mFragmentContainerHelper = FragmentContainerHelper()
    override fun setLayoutRes(): Int = R.layout.appointmentlayout

    override fun initData(savedInstanceState: Bundle?) {

        val fragments = ArrayList<Fragment>()
        fragments.add(NormalTestFragment())
        fragments.add(QuickTestFragment())


        val addFragmentUtils = AddFragmentUtils(this, R.id.appointmentContent)
        val commonNavigator = CommonNavigator(this)

        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(p0: Context?, p1: Int): IPagerTitleView {

                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(this@AppointMentTimeActivity)
                colorTransitionPagerTitleView.width = UIUtil.getScreenWidth(this@AppointMentTimeActivity) / 4
                colorTransitionPagerTitleView.normalColor = resources.getColor(R.color.color_666666)
                colorTransitionPagerTitleView.selectedColor = resources.getColor(R.color.color_3CC5BC)
                colorTransitionPagerTitleView.text =
                        when (p1) {
                            0 -> "详测"
                            1 -> "闪测"
                            else -> "闪测"
                        }
                colorTransitionPagerTitleView.setOnClickListener({
                    mFragmentContainerHelper.handlePageSelected(p1, true)
                    addFragmentUtils.showFragment(fragments[p1], fragments[p1].javaClass.name)
                })
                return colorTransitionPagerTitleView
            }

            override fun getCount(): Int = fragments.size

            override fun getIndicator(p0: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(this@AppointMentTimeActivity)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(this@AppointMentTimeActivity, 4.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(this@AppointMentTimeActivity, 15.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(this@AppointMentTimeActivity, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.color_3CC5BC))
                return indicator
            }

        }
        appointmentIndicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(appointmentIndicator)

        mFragmentContainerHelper.handlePageSelected(0, false)
        addFragmentUtils.showFragment(fragments[0], fragments[0].javaClass.name)
    }

    override fun setListener() {

        toolbarBack.setOnClickListener {
            finish()
        }
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun initTopBar(): TopBarOption = TopBarOption()
}