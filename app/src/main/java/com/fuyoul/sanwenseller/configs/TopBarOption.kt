package com.fuyoul.sanwenseller.configs

import android.view.View
import com.fuyoul.sanwenseller.R

/**
 *  @author: chen
 *  @CreatDate: 2017\10\25 0025
 *  @Desc:
 */
class TopBarOption {

    var contentBackRes: Int = 0
    var isShowBar = false//默认显示标题栏
    var navigationIcon: Int = R.mipmap.ic_yb_top_back

    var navigationListener: View.OnClickListener? = null//左边标题的点击事件

    var mainTitle: String? = null
    var childTitle: String? = null
    var childTitleColor: Int = 0
    var childIcon: Int = 0
    var childListener: View.OnClickListener? = null//子标题的点击事件
}