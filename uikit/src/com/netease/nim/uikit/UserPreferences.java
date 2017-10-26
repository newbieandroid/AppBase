package com.netease.nim.uikit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hzxuwen on 2015/10/21.
 */
public class UserPreferences {

    // 测试过滤通知
    private final static String KEY_MSG_IGNORE = "KEY_MSG_IGNORE";
    private final static String KEY_SB_NOTIFY_TOGGLE = "sb_notify_toggle";
    private final static String KEY_SUBSCRIBE_TIME = "KEY_SUBSCRIBE_TIME";

    private final static String KEY_EARPHONE_MODE = "KEY_EARPHONE_MODE";

    public static void setEarPhoneModeEnable(boolean on) {
        saveBoolean(KEY_EARPHONE_MODE, on);
    }

    public static boolean isEarPhoneModeEnable() {
        return getBoolean(KEY_EARPHONE_MODE, true);
    }

    private static boolean getBoolean(String key, boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    private static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setOnlineStateSubsTime(long time) {
        saveLong(KEY_SUBSCRIBE_TIME, time);
    }

    public static boolean getNotificationToggle() {
        return getBoolean(KEY_SB_NOTIFY_TOGGLE, true);
    }

    static SharedPreferences getSharedPreferences() {
        return NimUIKit.getContext().getSharedPreferences("UIKit." + NimUIKit.getAccount(), Context.MODE_PRIVATE);
    }

    public static long getOnlineStateSubsTime() {
        return getLong(KEY_SUBSCRIBE_TIME, 0);
    }

    public static boolean getMsgIgnore() {
        return getBoolean(KEY_MSG_IGNORE, false);
    }

    static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    static long getLong(String key, long value) {
        return getSharedPreferences().getLong(key, value);
    }
}
