package com.example.sasha.myapplication.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by sasha on 2/6/15.
 */
public class ScrollViewX extends ScrollView {
    private OnScrollViewListener mOnScrollViewListener;

    public ScrollViewX(Context context) {
        super(context);
    }

    public ScrollViewX(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewX(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface OnScrollViewListener {
        void onScrollChanged(ScrollViewX v, int l, int t, int oldl, int oldt);
    }
}
