package com.ypz.killetom.basedevsdkui.ui.widget.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ypz.killetom.basedevsdkui.R;

public abstract class BaseViewGroup extends ViewGroup {
    public BaseViewGroup(Context context) {
        this(context, null);
    }

    public BaseViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);

    }

    public BaseViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context,attrs);
    }

    protected abstract void initAttr(Context context, @Nullable AttributeSet attrs);


    protected boolean isSetDimen(int measureSpecSize) {
        return (MeasureSpec.getMode(measureSpecSize) == MeasureSpec.EXACTLY);
    }
}
