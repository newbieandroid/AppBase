package com.csl.refresh.impl;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.csl.refresh.SmartRefreshLayout;
import com.csl.refresh.api.RefreshHeader;
import com.csl.refresh.api.RefreshKernel;
import com.csl.refresh.api.RefreshLayout;
import com.csl.refresh.constant.RefreshState;
import com.csl.refresh.constant.SpinnerStyle;


/**
 * 刷新头部包装
 * Created by SCWANG on 2017/5/26.
 */

public class RefreshHeaderWrapper implements RefreshHeader {

//    private static final String TAG_REFRESH_HEADER_WRAPPER = "TAG_REFRESH_HEADER_WRAPPER";

    private View mWrapperView;
    private SpinnerStyle mSpinnerStyle;

    public RefreshHeaderWrapper(View wrapper) {
        this.mWrapperView = wrapper;
//        this.mWrapperView.setTag(TAG_REFRESH_HEADER_WRAPPER.hashCode(), TAG_REFRESH_HEADER_WRAPPER);
    }

//    public static boolean isTagedHeader(View view) {
//        return TAG_REFRESH_HEADER_WRAPPER.equals(view.getTag(TAG_REFRESH_HEADER_WRAPPER.hashCode()));
//    }

    @NonNull
    public View getView() {
        return mWrapperView;
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;
    }

    @Override
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        if (mSpinnerStyle != null) {
            return mSpinnerStyle;
        }
        ViewGroup.LayoutParams params = mWrapperView.getLayoutParams();
        if (params instanceof SmartRefreshLayout.LayoutParams) {
            mSpinnerStyle = ((SmartRefreshLayout.LayoutParams) params).spinnerStyle;
            if (mSpinnerStyle != null) {
                return mSpinnerStyle;
            }
        }
        if (params != null) {
            if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                return mSpinnerStyle = SpinnerStyle.Scale;
            }
        }
        return mSpinnerStyle = SpinnerStyle.Translate;
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        ViewGroup.LayoutParams params = mWrapperView.getLayoutParams();
        if (params instanceof SmartRefreshLayout.LayoutParams) {
            kernel.requestDrawBackgoundForHeader(((SmartRefreshLayout.LayoutParams) params).backgroundColor);
        }
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {

    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {

    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
