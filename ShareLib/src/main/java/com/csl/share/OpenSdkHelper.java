package com.csl.share;

import android.content.Context;

import com.sina.weibo.sdk.WbSdk;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * @author: chen
 * @CreatDate: 2017\10\25 0025
 * @Desc: 分享相关
 */

public class OpenSdkHelper {

    private Tencent mTencent;
    private IWXAPI wxApi;

    private static OpenSdkHelper instance;

    private OpenSdkHelper() {
    }


    public static OpenSdkHelper getInstance() {

        if (instance == null) {
            instance = new OpenSdkHelper();
        }

        return instance;
    }

    public void init(Context context) {

        //QQ注册
        mTencent = Tencent.createInstance(context.getResources().getString(R.string.qqKey), context);
        //微信注册
        String wxAppId = context.getResources().getString(R.string.wxKey);
        wxApi = WXAPIFactory.createWXAPI(context, wxAppId);
        wxApi.registerApp(wxAppId);

        //微博注册
        WbSdk.install(context, new AuthInfo(context, context.getResources().getString(R.string.wbKey),
                "http://sns.whalecloud.com/sina2/callback",
                "email," +
                        "direct_messages_read," +
                        "direct_messages_write," +
                        "friendships_groups_read," +
                        "friendships_groups_write," +
                        "statuses_to_me_read," +
                        "follow_app_official_microblog," +
                        "invitation_write"));

    }

    public IWXAPI getWxApi() {
        return wxApi;
    }
}
