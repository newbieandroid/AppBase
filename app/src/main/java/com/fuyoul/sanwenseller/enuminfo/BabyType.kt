package com.fuyoul.sanwenseller.enuminfo

import com.fuyoul.sanwenseller.R

/**
 *  @author: chen
 *  @CreatDate: 2017\10\26 0026
 *  @Desc:
 */
enum class BabyType {
    YFTX("缘份探寻", R.mipmap.ic_index_topmenu_yftx, 2),
    BZPP("八字匹配", R.mipmap.ic_index_topmenu_bzpp, 3),
    PJCY("破镜重圆", R.mipmap.ic_index_topmenu_pjcy, 4),
    HYJT("婚姻家庭", R.mipmap.ic_index_topmenu_yyjt, 1),
    LCZR("良辰择日", R.mipmap.ic_index_topmenu_lczr, 5),
    SYCY("事业财运", R.mipmap.ic_index_topmenu_sycy, 6),
    ZYQM("周易取名", R.mipmap.ic_index_topmenu_zyqm, 7),
    ZHYC("综合预测", R.mipmap.ic_index_topmenu_zhyc, 8);


    var title: String? = null
    var resId: Int = 0
    var typeId: Int = 0

    constructor(title: String?, resId: Int, typeId: Int) {

        this.title = title
        this.resId = resId
        this.typeId = typeId
    }
}