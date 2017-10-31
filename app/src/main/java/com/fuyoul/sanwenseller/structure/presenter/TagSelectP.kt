package com.fuyoul.sanwenseller.structure.presenter

import com.fuyoul.sanwenseller.base.BaseP
import com.fuyoul.sanwenseller.structure.model.TagSelectM
import com.fuyoul.sanwenseller.structure.view.TagSelectV

/**
 *  @author: chen
 *  @CreatDate: 2017\10\31 0031
 *  @Desc:
 */
class TagSelectP(v: TagSelectV) : BaseP<TagSelectM, TagSelectV>(v) {
    override fun getModelImpl(): TagSelectM = TagSelectM()
}