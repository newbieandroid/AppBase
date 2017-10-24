package com.app.base.widgets.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

/**
 * Auther: chen
 * Creat at: 2017\8\14 0014
 * Desc:
 */

public class DialogViewHolder {
    private final SparseArray<View> mViews;
    private View mDialogView;
    private ViewGroup viewGroup;

    private DialogViewHolder(Context context, int layoutId) {
        this.mViews = new SparseArray<View>();
        mDialogView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        AutoUtils.auto(mDialogView);

    }

    public static DialogViewHolder get(Context context, int layoutId) {
        return new DialogViewHolder(context, layoutId);
    }

    public View getConvertView() {
        return mDialogView;
    }

    /**
     * Set the string for TextView
     *
     * @param viewId
     * @param text
     * @return
     */
    public DialogViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * set view visible
     *
     * @param viewId
     * @return
     */
    public DialogViewHolder setViewInViSible(int viewId) {
        TextView view = getView(viewId);
        view.setVisibility(View.INVISIBLE);
        return this;
    }

    /**
     * set view visible
     *
     * @param viewId
     * @return
     */
    public DialogViewHolder setViewViSible(int viewId) {
        TextView view = getView(viewId);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * set view gone
     *
     * @param viewId
     * @return
     */
    public DialogViewHolder setViewGone(int viewId) {
        TextView view = getView(viewId);
        view.setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置点击
     */
    public DialogViewHolder setOnClick(int viewId, View.OnClickListener onClick) {
        View view = getView(viewId);
        view.setOnClickListener(onClick);
        return this;
    }

    /**
     * Through control the Id of the access to control, if not join views
     *
     * @param viewId
     * @return
     */

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mDialogView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
}
