package com.fuyoul.sanwenseller.structure.model

import com.alibaba.fastjson.JSON
import com.fuyoul.sanwenseller.base.BaseM
import com.fuyoul.sanwenseller.bean.others.AppointMentItemBean
import com.fuyoul.sanwenseller.bean.others.DataTimeBean
import com.fuyoul.sanwenseller.bean.reqhttp.ReqChangeAllDayState
import com.fuyoul.sanwenseller.bean.reqhttp.ReqChangeDayOfItemState
import com.fuyoul.sanwenseller.bean.reshttp.ResHttpResult
import com.fuyoul.sanwenseller.configs.UrlInfo.APPOINTMENTLIST
import com.fuyoul.sanwenseller.configs.UrlInfo.CHANGEAPPOINTMENT
import com.fuyoul.sanwenseller.configs.UrlInfo.CHANGEITEMAPPOINTMENT
import com.fuyoul.sanwenseller.listener.HttpReqListener
import com.lzy.okgo.OkGo
import java.util.*

/**
 *  @author: chen
 *  @CreatDate: 2017\10\30 0030
 *  @Desc:
 */
class AppointMentM : BaseM {


    fun getDefaultListData(): List<AppointMentItemBean> {

        val defaultDatas = ArrayList<AppointMentItemBean>()

        var startTime = 9

        (0 until 16).forEach {
            val item = AppointMentItemBean()
            item.time = "$startTime:00"
            defaultDatas.add(item)
            startTime++
        }

        return defaultDatas
    }


    /**改变全天接单状态**/
    fun changeAllDayState(reqData: ReqChangeAllDayState, listener: HttpReqListener) {

        OkGo.post<ResHttpResult>(CHANGEAPPOINTMENT)
                .upJson(JSON.toJSONString(reqData))
                .execute(listener)
    }

    /**设置某一天的某个时间节点不接单**/
    fun changeItemState(reqData: ReqChangeDayOfItemState, listener: HttpReqListener) {
        OkGo.post<ResHttpResult>(CHANGEITEMAPPOINTMENT)
                .upJson(JSON.toJSONString(reqData))
                .execute(listener)
    }

    fun getData(listener: HttpReqListener) {

        OkGo.get<ResHttpResult>(APPOINTMENTLIST).execute(listener)

    }


    fun getTodayBean(): DataTimeBean {

        val result = DataTimeBean()
        val calendar = Calendar.getInstance()
        calendar.time = Date() //今天

        result.year = calendar.get(Calendar.YEAR)
        result.month = calendar.get(Calendar.MONTH) + 1
        result.day = calendar.get(Calendar.DAY_OF_MONTH)

        return result

    }

    fun getTomorrowBean(): DataTimeBean {

        val result = DataTimeBean()
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        //明天
        calendar.add(Calendar.DATE, 1)

        result.year = calendar.get(Calendar.YEAR)
        result.month = calendar.get(Calendar.MONTH) + 1
        result.day = calendar.get(Calendar.DAY_OF_MONTH)

        return result

    }

    fun getAfterTomorrowBean(): DataTimeBean {

        val result = DataTimeBean()
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        //后天
        calendar.add(Calendar.DATE, 2)

        result.year = calendar.get(Calendar.YEAR)
        result.month = calendar.get(Calendar.MONTH) + 1
        result.day = calendar.get(Calendar.DAY_OF_MONTH)

        return result

    }
}