package com.fuyoul.sanwenseller.ui.fragment.appointment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.configs.Data
import com.fuyoul.sanwenseller.configs.Key
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.AddFragmentUtils
import kotlinx.android.synthetic.main.normaltestfragment.*
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
class NormalTestFragment : BaseFragment<EmptyM, EmptyV, EmptyP>() {

    private val mFragmentContainerHelper = FragmentContainerHelper()

    override fun setLayoutRes(): Int = R.layout.normaltestfragment

    override fun init(view: View?, savedInstanceState: Bundle?) {
        val addFragmentUtils = AddFragmentUtils(this, R.id.normalTestConten)

        val fragments = ArrayList<Fragment>()
        for (index in 0 until 3) {

            val item = NormalTestItemFragment()
            val bund = Bundle()
            when (index) {
                0 -> {
                    bund.putInt(Key.appointmentTypeKey, Data.TODAY)
                }
                1 -> {
                    bund.putInt(Key.appointmentTypeKey, Data.TOMORROW)
                }
                2 -> {
                    bund.putInt(Key.appointmentTypeKey, Data.AFTERTOMORROW)
                }
            }

            item.arguments = bund
            fragments.add(item)
        }

        val commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(p0: Context?, p1: Int): IPagerTitleView {

                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.width = UIUtil.getScreenWidth(context) / 3
                colorTransitionPagerTitleView.normalColor = resources.getColor(R.color.color_666666)
                colorTransitionPagerTitleView.selectedColor = resources.getColor(R.color.color_FF627B)
                colorTransitionPagerTitleView.text =
                        when (p1) {
                            0 -> "今天"
                            1 -> "明天"
                            2 -> "后天"
                            else -> "今天"
                        }
                colorTransitionPagerTitleView.setOnClickListener({
                    mFragmentContainerHelper.handlePageSelected(p1, true)
                    addFragmentUtils.showFragment(fragments[p1], fragments[p1].javaClass.name + p1)
                })
                return colorTransitionPagerTitleView
            }

            override fun getCount(): Int = fragments.size

            override fun getIndicator(p0: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 4.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 15.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.color_FF627B))
                return indicator
            }

        }
        normalTestIndicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(normalTestIndicator)


        mFragmentContainerHelper.handlePageSelected(0, false)
        addFragmentUtils.showFragment(fragments[0], fragments[0].javaClass.name + 0)
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()
}