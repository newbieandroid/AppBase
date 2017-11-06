package com.fuyoul.sanwenseller.ui.fragment.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.fuyoul.sanwenseller.R
import com.fuyoul.sanwenseller.base.BaseFragment
import com.fuyoul.sanwenseller.enuminfo.OrderType
import com.fuyoul.sanwenseller.structure.model.EmptyM
import com.fuyoul.sanwenseller.structure.presenter.EmptyP
import com.fuyoul.sanwenseller.structure.view.EmptyV
import com.fuyoul.sanwenseller.utils.AddFragmentUtils
import kotlinx.android.synthetic.main.orderlayout.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
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
class MainFragment : BaseFragment<EmptyM, EmptyV, EmptyP>() {


    private var addFragmentUtils: AddFragmentUtils? = null

    private val mFragmentContainerHelper = FragmentContainerHelper()
    private var fragments = ArrayList<OrderItemFragment>()
    private var tags = ArrayList<String>()

    override fun getPresenter(): EmptyP = EmptyP(initViewImpl())

    override fun initViewImpl(): EmptyV = EmptyV()

    override fun setLayoutRes(): Int = R.layout.orderlayout

    override fun init(view: View?, savedInstanceState: Bundle?) {

        for (index in 0 until 5) {
            val item = OrderItemFragment()
            val bund = Bundle()
            bund.putInt("orderType", when (index) {
                0 -> {
                    OrderType.ALL.orderType
                }
                1 -> {
                    OrderType.TOBEPREDICTED.orderType
                }
                2 -> {
                    OrderType.INPREDICTED.orderType
                }
                3 -> {
                    OrderType.PREDICTED.orderType
                }
                4 -> {
                    OrderType.SELLED.orderType
                }
                else -> {
                    OrderType.ALL.orderType
                }
            })
            item.arguments = bund
            fragments.add(item)
            tags.add("argOf$index")
        }

        addFragmentUtils = AddFragmentUtils(this, R.id.orderContent)
        val commonNavigator = CommonNavigator(context)

        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(p0: Context?, p1: Int): IPagerTitleView {

                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.width = UIUtil.getScreenWidth(context) / 5
                colorTransitionPagerTitleView.normalColor = resources.getColor(R.color.color_666666)
                colorTransitionPagerTitleView.selectedColor = resources.getColor(R.color.color_3CC5BC)
                colorTransitionPagerTitleView.text =
                        when (p1) {
                            0 -> "全部 "
                            1 -> "待预测 "
                            2 -> "预测中 "
                            3 -> "已预测 "
                            else -> "售后 "
                        }
                colorTransitionPagerTitleView.setOnClickListener({
                    mFragmentContainerHelper.handlePageSelected(p1, true)
                    addFragmentUtils?.showFragment(fragments[p1], tags[p1])
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
        orderAllIndicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(orderAllIndicator)

        mFragmentContainerHelper.handlePageSelected(0, false)
        addFragmentUtils?.showFragment(fragments[0], tags[0])
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        addFragmentUtils?.currentFragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun setListener() {


    }

}