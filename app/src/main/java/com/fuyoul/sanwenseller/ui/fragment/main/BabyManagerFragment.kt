package com.fuyoul.sanwenseller.ui.fragment.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.configs.Code
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.AddFragmentUtils
import kotlinx.android.synthetic.main.babymanager.*
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
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
class BabyManagerFragment : BaseFragment<EmptyM, EmptyV, EmptyP>() {

    private var addFragmentUtils: AddFragmentUtils? = null
    private val mFragmentContainerHelper = FragmentContainerHelper()
    private val fragments = arrayListOf<BabyManagerItemFragment>()

    override fun setLayoutRes(): Int = R.layout.babymanager

    override fun init(view: View?, savedInstanceState: Bundle?) {


        (0..1).forEach {
            val item = BabyManagerItemFragment()
            val bund = Bundle()
            bund.putInt("status", if (it == 0) Code.SELL else Code.NOSELL)
            item.arguments = bund
            fragments.add(item)
        }


        addFragmentUtils = AddFragmentUtils(this, R.id.babyManagerContent)
        val commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(p0: Context?, p1: Int): IPagerTitleView {

                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.width = UIUtil.getScreenWidth(context) / 2
                colorTransitionPagerTitleView.normalColor = resources.getColor(R.color.color_666666)
                colorTransitionPagerTitleView.selectedColor = resources.getColor(R.color.color_3CC5BC)
                colorTransitionPagerTitleView.text =
                        when (p1) {
                            0 -> "在售商品"
                            1 -> "下架商品"
                            else -> "在售商品"
                        }
                colorTransitionPagerTitleView.setOnClickListener({
                    mFragmentContainerHelper.handlePageSelected(p1, true)
                    addFragmentUtils?.showFragment(fragments[p1], fragments[p1].javaClass.name + p1)
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
                indicator.setColors(resources.getColor(R.color.color_3CC5BC))
                return indicator
            }

        }
        babyManagerIndicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(babyManagerIndicator)

        mFragmentContainerHelper.handlePageSelected(0, false)
        addFragmentUtils?.showFragment(fragments[0], fragments[0].javaClass.name + 0)
    }

    override fun setListener() {
    }

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        addFragmentUtils?.currentFragment?.onActivityResult(requestCode, resultCode, data)
    }
}