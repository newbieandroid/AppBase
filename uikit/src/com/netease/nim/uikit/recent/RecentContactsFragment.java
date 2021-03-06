package com.netease.nim.uikit.recent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.OnlineStateChangeListener;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.StatusBarUtils;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.netease.nim.uikit.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nim.uikit.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.netease.nim.uikit.NimUIKit.ACTIVITYCONTACTID;
import static com.netease.nim.uikit.NimUIKit.NOTIFYCONTACTID;
import static com.netease.nim.uikit.NimUIKit.SERVICECONTACTID;
import static com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog.onSeparateItemClickListener;

/**
 * 最近联系人列表(会话列表)
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class RecentContactsFragment extends TFragment {

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            StatusBarUtils.StatusBarLightMode(getActivity(), R.color.white);
        }
    }

    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

    // view
    private RecyclerView recyclerView;

    private View emptyBg;

    private TextView emptyHint;

    // data
    private List<RecentContact> items;

    private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）

    private RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    private UserInfoObservable.UserInfoObserver userInfoObserver;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        StatusBarUtils.setTranslucentForImageView(getActivity(), 30, recyclerView.getVisibility() == View.VISIBLE ? recyclerView : emptyBg);
        onHiddenChanged(false);

        initMessageList();
        requestMessages(true);
        registerObservers(true);
        registerDropCompletedListener(true);
        registerOnlineStateChangeListener(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nim_recent_contacts, container, false);
    }


    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;

        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
        emptyHint.setHint("还没有会话，在通讯录中找个人聊聊吧！");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerDropCompletedListener(false);
        registerOnlineStateChangeListener(false);
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        recyclerView = findView(R.id.recycler_view);
        emptyBg = findView(R.id.emptyBg);
        emptyHint = findView(R.id.message_list_empty_hint);
    }

    /**
     * 初始化消息列表,手动添加三个最近联系人
     */
    private void initMessageList() {
        items = new ArrayList<>();
        cached = new HashMap<>(3);

        // adapter
        adapter = new RecentContactAdapter(recyclerView, items);
        initCallBack();
        adapter.setCallback(callback);

        // recyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(touchListener);

        // ios style
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        // drop listener
        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {
            @Override
            public void onDropBegin() {
                touchListener.setShouldDetectGesture(false);
            }

            @Override
            public void onDropEnd() {
                touchListener.setShouldDetectGesture(true);
            }
        });
    }

    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    NimUIKit.startP2PSession(getActivity(), recent.getContactId());
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }

    private SimpleClickListener<RecentContactAdapter> touchListener = new SimpleClickListener<RecentContactAdapter>() {
        @Override
        public void onItemClick(RecentContactAdapter adapter, View view, int position) {
            if (callback != null) {
                RecentContact recent = adapter.getItem(position);
                callback.onItemClick(recent);
            }
        }

        @Override
        public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
            showLongClickMenu(adapter.getItem(position), position);
        }

        @Override
        public void onItemChildClick(RecentContactAdapter adapter, View view, int position) {

        }

        @Override
        public void onItemChildLongClick(RecentContactAdapter adapter, View view, int position) {

        }
    };

    OnlineStateChangeListener onlineStateChangeListener = new OnlineStateChangeListener() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            notifyDataSetChanged();
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKit.enableOnlineState()) {
            return;
        }
        if (register) {
            NimUIKit.addOnlineStateChangeListeners(onlineStateChangeListener);
        } else {
            NimUIKit.removeOnlineStateChangeListeners(onlineStateChangeListener);
        }
    }

    private void showLongClickMenu(final RecentContact recent, final int position) {


        //如果是置顶的系统联系人则不删除
        if (recent.getContactId().equals(NimUIKit.SERVICECONTACTID)
                || recent.getContactId().equals(NimUIKit.ACTIVITYCONTACTID)
                || recent.getContactId().equals(NimUIKit.NOTIFYCONTACTID)) {

            return;
        }


        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setTitle(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));
        String title = getString(R.string.main_msg_list_delete_chatting);
        alertDialog.addItem(title, new onSeparateItemClickListener() {
            @Override
            public void onClick() {
                // 删除会话，删除后，消息历史被一起删除
                NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                adapter.remove(position);

                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        refreshMessages(true);
                    }
                });
            }
        });

        //TODO 是否置顶聊天
        title = (isTagSet(recent, RECENT_TAG_STICKY) ? getString(R.string.main_msg_list_clear_sticky_on_top) : getString(R.string.main_msg_list_sticky_on_top));
        alertDialog.addItem(title, new onSeparateItemClickListener() {
            @Override
            public void onClick() {
                if (isTagSet(recent, RECENT_TAG_STICKY)) {
                    removeTag(recent, RECENT_TAG_STICKY);
                } else {
                    addTag(recent, RECENT_TAG_STICKY);
                }
                NIMClient.getService(MsgService.class).updateRecent(recent);

                refreshMessages(false);
            }
        });

        alertDialog.addItem("删除该聊天（仅服务器）", new onSeparateItemClickListener() {
            @Override
            public void onClick() {
                NIMClient.getService(MsgService.class)
                        .deleteRoamingRecentContact(recent.getContactId(), recent.getSessionType())
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                Toast.makeText(getActivity(), "delete success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(int code) {
                                Toast.makeText(getActivity(), "delete failed, code:" + code, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onException(Throwable exception) {

                            }
                        });
            }
        });
        alertDialog.show();
    }

    private void addTag(RecentContact recent, long tag) {
        tag = recent.getTag() | tag;
        recent.setTag(tag);
    }

    private void removeTag(RecentContact recent, long tag) {
        tag = recent.getTag() & ~tag;
        recent.setTag(tag);
    }

    private boolean isTagSet(RecentContact recent, long tag) {
        return (recent.getTag() & tag) == tag;
    }

    private List<RecentContact> loadedRecents;

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        getHandler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {


                        Log.e("csl", "获取到的最近联系人列表:\n" + JSON.toJSONString(recents));

                        loadedRecents = recents;
                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                    }
                });
            }
        }, delay ? 250 : 0);
    }

    private void onRecentContactsLoaded() {
        items.clear();


        if (loadedRecents != null) {

            boolean hasSERVICECONTACTID = false;
            boolean hasNOTIFYCONTACTID = false;
            boolean hasACTIVITYCONTACTID = false;

            for (RecentContact recentContact : loadedRecents) {

                if (recentContact.getContactId().equals(NimUIKit.SERVICECONTACTID)) {
                    hasSERVICECONTACTID = true;
                    addTag(recentContact, RECENT_TAG_STICKY);
                } else if (recentContact.getContactId().equals(NimUIKit.NOTIFYCONTACTID)) {
                    hasNOTIFYCONTACTID = true;
                    addTag(recentContact, RECENT_TAG_STICKY);
                } else if (recentContact.getContactId().equals(NimUIKit.ACTIVITYCONTACTID)) {
                    hasACTIVITYCONTACTID = true;
                    addTag(recentContact, RECENT_TAG_STICKY);
                }

            }

            if (!hasSERVICECONTACTID) {
                items.add(serviceContact);
            }

            if (!hasNOTIFYCONTACTID) {
                items.add(notifyContact);
            }

            if (!hasACTIVITYCONTACTID) {
                items.add(activityContact);
            }

            items.addAll(loadedRecents);
            loadedRecents = null;
        } else {
            if (!items.contains(serviceContact)) {
                items.add(0, serviceContact);
            }
            if (!items.contains(notifyContact)) {
                items.add(1, notifyContact);
            }
            if (!items.contains(activityContact)) {
                items.add(2, activityContact);
            }
        }

        refreshMessages(true);

        if (callback != null) {
            callback.onRecentContactsLoaded();
        }
    }


    /**
     * 三问客服
     **/
    RecentContact serviceContact = new RecentContact() {

        @Override
        public String getContactId() {
            return SERVICECONTACTID;
        }

        @Override
        public String getFromAccount() {
            return null;
        }

        @Override
        public String getFromNick() {
            return null;
        }

        @Override
        public SessionTypeEnum getSessionType() {
            return SessionTypeEnum.P2P;
        }

        @Override
        public String getRecentMessageId() {
            return null;
        }

        @Override
        public MsgTypeEnum getMsgType() {
            return MsgTypeEnum.text;
        }

        @Override
        public MsgStatusEnum getMsgStatus() {
            return MsgStatusEnum.read;
        }

        @Override
        public void setMsgStatus(MsgStatusEnum msgStatusEnum) {
        }

        @Override
        public int getUnreadCount() {
            return 0;
        }

        @Override
        public String getContent() {


            return null;
        }

        @Override
        public long getTime() {
            return System.currentTimeMillis();
        }

        @Override
        public MsgAttachment getAttachment() {
            return null;
        }

        @Override
        public void setTag(long l) {

        }

        @Override
        public long getTag() {
            return RECENT_TAG_STICKY;
        }

        @Override
        public Map<String, Object> getExtension() {
            return null;
        }

        @Override
        public void setExtension(Map<String, Object> map) {

        }
    };

    /**
     * 系统消息
     **/
    RecentContact notifyContact = new RecentContact() {
        @Override
        public String getContactId() {
            return NOTIFYCONTACTID;
        }

        @Override
        public String getFromAccount() {
            return null;
        }

        @Override
        public String getFromNick() {
            return null;
        }

        @Override
        public SessionTypeEnum getSessionType() {
            return SessionTypeEnum.P2P;
        }

        @Override
        public String getRecentMessageId() {
            return null;
        }

        @Override
        public MsgTypeEnum getMsgType() {
            return MsgTypeEnum.text;
        }

        @Override
        public MsgStatusEnum getMsgStatus() {
            return MsgStatusEnum.read;
        }

        @Override
        public void setMsgStatus(MsgStatusEnum msgStatusEnum) {

        }

        @Override
        public int getUnreadCount() {
            return 0;
        }

        @Override
        public String getContent() {
            return null;
        }

        @Override
        public long getTime() {
            return System.currentTimeMillis();
        }

        @Override
        public MsgAttachment getAttachment() {
            return null;
        }

        @Override
        public void setTag(long l) {

        }

        @Override
        public long getTag() {
            return RECENT_TAG_STICKY;
        }

        @Override
        public Map<String, Object> getExtension() {
            return null;
        }

        @Override
        public void setExtension(Map<String, Object> map) {

        }
    };

    /**
     * 活动通知
     **/
    RecentContact activityContact = new RecentContact() {
        @Override
        public String getContactId() {
            return ACTIVITYCONTACTID;
        }

        @Override
        public String getFromAccount() {
            return null;
        }

        @Override
        public String getFromNick() {
            return null;
        }

        @Override
        public SessionTypeEnum getSessionType() {
            return SessionTypeEnum.P2P;
        }

        @Override
        public String getRecentMessageId() {
            return null;
        }

        @Override
        public MsgTypeEnum getMsgType() {
            return MsgTypeEnum.text;
        }

        @Override
        public MsgStatusEnum getMsgStatus() {
            return MsgStatusEnum.read;
        }

        @Override
        public void setMsgStatus(MsgStatusEnum msgStatusEnum) {

        }

        @Override
        public int getUnreadCount() {
            return 0;
        }

        @Override
        public String getContent() {
            return null;
        }

        @Override
        public long getTime() {
            return System.currentTimeMillis();
        }

        @Override
        public MsgAttachment getAttachment() {
            return null;
        }

        @Override
        public void setTag(long l) {

        }

        @Override
        public long getTag() {
            return RECENT_TAG_STICKY;
        }

        @Override
        public Map<String, Object> getExtension() {

            return null;
        }

        @Override
        public void setExtension(Map<String, Object> map) {

        }
    };


    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();

        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）

            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }

            // 方式二：直接从SDK读取（相对慢）
            //int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

            if (callback != null) {
                callback.onUnreadCountChange(unreadNum);
            }

            Badger.updateBadgerCount(unreadNum);
        }
    }

    /**
     * 根据置顶的tag进行排序
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);

        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }


    private void registerDropCompletedListener(boolean register) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
        }
    }

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {

                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);
                }
            }
        }
    };

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            if (!DropManager.getInstance().isTouchable()) {
                // 正在拖拽红点，缓存数据
                for (RecentContact r : recentContacts) {
                    cached.put(r.getContactId(), r);
                }

                return;
            }

            onRecentContactChanged(recentContacts);
        }
    };

    private void onRecentContactChanged(List<RecentContact> recentContacts) {
        int index;
        for (RecentContact r : recentContacts) {
            index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (r.getContactId().equals(items.get(i).getContactId())
                        && r.getSessionType() == (items.get(i).getSessionType())) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                items.remove(index);
            }

            items.add(r);
        }

        cacheMessages.clear();

        refreshMessages(true);
    }

    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {
        @Override
        public void onCompleted(Object id, boolean explosive) {
            if (cached != null && !cached.isEmpty()) {
                // 红点爆裂，已经要清除未读，不需要再刷cached
                if (explosive) {
                    if (id instanceof RecentContact) {
                        RecentContact r = (RecentContact) id;
                        cached.remove(r.getContactId());
                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
                        cached.clear();
                    }
                }

                // 刷cached
                if (!cached.isEmpty()) {
                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
                    recentContacts.addAll(cached.values());
                    cached.clear();

                    onRecentContactChanged(recentContacts);
                }
            }
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };


    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyItemChanged(index);
            }
        });
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }

        UserInfoHelper.registerObserver(userInfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

}
