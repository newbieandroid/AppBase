package com.netease.nim.uikit.session.helper;

import android.text.TextUtils;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamAllMuteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;
import com.netease.nimlib.sdk.team.model.MuteMemberAttachment;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import java.util.List;
import java.util.Map;

/**
 * 系统消息描述文本构造器。主要是将各个系统消息转换为显示的文本内容。<br>
 * Created by huangjun on 2015/3/11.
 */
public class TeamNotificationHelper {
    private static ThreadLocal<String> teamId = new ThreadLocal<>();

    public static String getMsgShowText(final IMMessage message) {
        String content = "";
        String messageTip = message.getMsgType().getSendMessageTip();
        if (messageTip.length() > 0) {
            content += "[" + messageTip + "]";
        } else {
            if (message.getSessionType() == SessionTypeEnum.Team && message.getAttachment() != null) {
                content += getTeamNotificationText(message, message.getSessionId());
            } else {
                content += message.getContent();
            }
        }

        return content;
    }

    public static String getTeamNotificationText(IMMessage message, String tid) {
        return getTeamNotificationText(message.getSessionId(), message.getFromAccount(), (NotificationAttachment) message.getAttachment());
    }

    public static String getTeamNotificationText(String tid, String fromAccount, NotificationAttachment attachment) {
        teamId.set(tid);
        String text = buildNotification(tid, fromAccount, attachment);
        teamId.set(null);
        return text;
    }

    private static String buildNotification(String tid, String fromAccount, NotificationAttachment attachment) {
        String text = "";
        switch (attachment.getType()) {
            case InviteMember:
                break;
            case KickMember:
                break;
            case LeaveTeam:
                break;
            case DismissTeam:
                break;
            case UpdateTeam:
                break;
            case PassTeamApply:
                break;
            case TransferOwner:
                break;
            case AddTeamManager:
                break;
            case RemoveTeamManager:
                break;
            case AcceptInvite:
                break;
            case MuteTeamMember:
                break;
            default:
                text = " unknown message";
                break;
        }

        return text;
    }


}
