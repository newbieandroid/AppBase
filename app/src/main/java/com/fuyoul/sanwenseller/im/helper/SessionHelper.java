package com.fuyoul.sanwenseller.im.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.fuyoul.sanwenseller.bean.reshttp.ResLoginInfoBean;
import com.fuyoul.sanwenseller.im.session.action.AVChatAction;
import com.fuyoul.sanwenseller.im.session.extension.CustomAttachParser;
import com.fuyoul.sanwenseller.im.session.extension.CustomAttachment;
import com.fuyoul.sanwenseller.im.session.extension.StickerAttachment;
import com.fuyoul.sanwenseller.im.session.viewholder.MsgViewHolderAVChat;
import com.fuyoul.sanwenseller.im.session.viewholder.MsgViewHolderDefCustom;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.helper.MessageHelper;
import com.netease.nim.uikit.session.module.MsgForwardFilter;
import com.netease.nim.uikit.session.module.MsgRevokeFilter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderTip;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {


    private static SessionCustomization p2pCustomization;
    private static SessionCustomization myP2pCustomization;


    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();

        // 设置会话中点击事件响应处理
        setSessionListener();

        // 注册消息转发过滤器
        registerMsgForwardFilter();

        // 注册消息撤回过滤器
        registerMsgRevokeFilter();

        // 注册消息撤回监听器
        registerMsgRevokeObserver();

        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization());

    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null);
    }

    public static void startP2PSession(Context context, String account, IMMessage anchor) {


        ResLoginInfoBean resHttpLoginBean = DataSupport.findFirst(ResLoginInfoBean.class);


        if (!("user_" + resHttpLoginBean.getUserInfoId()).equals(account)) {
            NimUIKit.startP2PSession(context, account, anchor);
        } else {
            NimUIKit.startChatting(context, account, SessionTypeEnum.P2P, getMyP2pCustomization(), anchor);
        }
    }


    // 定制化单聊界面。如果使用默认界面，返回null即可
    private static SessionCustomization getP2pCustomization() {
        if (p2pCustomization == null) {
            p2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(activity, requestCode, resultCode, data);

                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 背景
//            p2pCustomization.backgroundColor = Color.BLUE;
//            p2pCustomization.backgroundUri = "file:///android_asset/xx/bk.jpg";
//            p2pCustomization.backgroundUri = "file:///sdcard/Pictures/bk.png";
//            p2pCustomization.backgroundUri = "android.resource://com.netease.nim.demo/drawable/bk"

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                actions.add(new AVChatAction(AVChatType.AUDIO));
                actions.add(new AVChatAction(AVChatType.VIDEO));
            }

            p2pCustomization.actions = actions;
            p2pCustomization.withSticker = true;

        }

        return p2pCustomization;
    }

    private static SessionCustomization getMyP2pCustomization() {
        if (myP2pCustomization == null) {
            myP2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 背景
//            p2pCustomization.backgroundColor = Color.BLUE;
//            p2pCustomization.backgroundUri = "file:///android_asset/xx/bk.jpg";
//            p2pCustomization.backgroundUri = "file:///sdcard/Pictures/bk.png";
//            p2pCustomization.backgroundUri = "android.resource://com.netease.nim.demo/drawable/bk"

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                actions.add(new AVChatAction(AVChatType.AUDIO));
                actions.add(new AVChatAction(AVChatType.VIDEO));
            }
            myP2pCustomization.actions = actions;
            myP2pCustomization.withSticker = true;
        }
        return myP2pCustomization;
    }


    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(AVChatAttachment.class, MsgViewHolderAVChat.class);
        NimUIKit.registerMsgItemViewHolder(CustomAttachment.class, MsgViewHolderDefCustom.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }


    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }
        };

        NimUIKit.setSessionListener(listener);
    }


    /**
     * 消息转发过滤器
     */
    private static void registerMsgForwardFilter() {
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getDirect() == MsgDirectionEnum.In
                        && (message.getAttachStatus() == AttachStatusEnum.transferring
                        || message.getAttachStatus() == AttachStatusEnum.fail)) {
                    // 接收到的消息，附件没有下载成功，不允许转发
                    return true;
                } else if (message.getMsgType() == MsgTypeEnum.robot && message.getAttachment() != null && ((RobotAttachment) message.getAttachment()).isRobotSend()) {
                    return true; // 如果是机器人发送的消息 不支持转发
                }
                return false;
            }
        });
    }

    /**
     * 消息撤回过滤器
     */
    private static void registerMsgRevokeFilter() {
        NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (("user_" + DataSupport.findFirst(ResLoginInfoBean.class).getUserInfoId()).equals(message.getSessionId())) {
                    // 发给我的电脑 不允许撤回
                    return true;
                }
                return false;
            }
        });
    }

    private static void registerMsgRevokeObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRevokeMessage(new Observer<IMMessage>() {
            @Override
            public void onEvent(IMMessage message) {
                if (message == null) {
                    return;
                }

                MessageHelper.getInstance().onRevokeMessage(message);
            }
        }, true);
    }


}
