package com.fuyoul.sanwenseller.bean.reshttp;


import java.util.List;

/**
 * @author: chen
 * @CreatDate: 2017\10\30 0030
 * @Desc:获取预测师的排班信息
 */

public class ResAppointMentT {
    /**
     * workerTime : 0
     * offWorkerTime : 0
     * todayStatusDetail : {"todayStatus":0,"times":[]}
     * tomorrowStatusDetail : {"tomorrowStatus":0,"times":[]}
     * afterTomorrowStatusDetail : {"afterTomorrowStatus":0,"times":[]}
     */

    private int workerTime;
    private int offWorkerTime;
    private TodayStatusDetailBean todayStatusDetail;
    private TomorrowStatusDetailBean tomorrowStatusDetail;
    private AfterTomorrowStatusDetailBean afterTomorrowStatusDetail;

    public int getWorkerTime() {
        return workerTime;
    }

    public void setWorkerTime(int workerTime) {
        this.workerTime = workerTime;
    }

    public int getOffWorkerTime() {
        return offWorkerTime;
    }

    public void setOffWorkerTime(int offWorkerTime) {
        this.offWorkerTime = offWorkerTime;
    }

    public TodayStatusDetailBean getTodayStatusDetail() {
        return todayStatusDetail;
    }

    public void setTodayStatusDetail(TodayStatusDetailBean todayStatusDetail) {
        this.todayStatusDetail = todayStatusDetail;
    }

    public TomorrowStatusDetailBean getTomorrowStatusDetail() {
        return tomorrowStatusDetail;
    }

    public void setTomorrowStatusDetail(TomorrowStatusDetailBean tomorrowStatusDetail) {
        this.tomorrowStatusDetail = tomorrowStatusDetail;
    }

    public AfterTomorrowStatusDetailBean getAfterTomorrowStatusDetail() {
        return afterTomorrowStatusDetail;
    }

    public void setAfterTomorrowStatusDetail(AfterTomorrowStatusDetailBean afterTomorrowStatusDetail) {
        this.afterTomorrowStatusDetail = afterTomorrowStatusDetail;
    }

    public static class TodayStatusDetailBean {
        /**
         * todayStatus : 0
         * times : []
         */

        private int todayStatus;
        private List<Item> times;

        public int getTodayStatus() {
            return todayStatus;
        }

        public void setTodayStatus(int todayStatus) {
            this.todayStatus = todayStatus;
        }

        public List<Item> getTimes() {
            return times;
        }

        public void setTimes(List<Item> times) {
            this.times = times;
        }
    }


    public static class Item {
        private String time;
        private long userInfoId;
        private String avatar;
        private String nickname;

        public long getUserInfoId() {
            return userInfoId;
        }

        public void setUserInfoId(long userInfoId) {
            this.userInfoId = userInfoId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static class TomorrowStatusDetailBean {
        /**
         * tomorrowStatus : 0
         * times : []
         */

        private int tomorrowStatus;
        private List<Item> times;

        public int getTomorrowStatus() {
            return tomorrowStatus;
        }

        public void setTomorrowStatus(int tomorrowStatus) {
            this.tomorrowStatus = tomorrowStatus;
        }

        public List<Item> getTimes() {
            return times;
        }

        public void setTimes(List<Item> times) {
            this.times = times;
        }
    }

    public static class AfterTomorrowStatusDetailBean {
        /**
         * afterTomorrowStatus : 0
         * times : []
         */

        private int afterTomorrowStatus;
        private List<Item> times;

        public int getAfterTomorrowStatus() {
            return afterTomorrowStatus;
        }

        public void setAfterTomorrowStatus(int afterTomorrowStatus) {
            this.afterTomorrowStatus = afterTomorrowStatus;
        }

        public List<Item> getTimes() {
            return times;
        }

        public void setTimes(List<Item> times) {
            this.times = times;
        }
    }
}
