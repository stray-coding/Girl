package com.coding.girl.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

/**
 * Date 2018/12/27 23:08
 * author HDL
 * Mail 229101253@qq.com
 */
public class WindowInsetsFrameLayout extends FrameLayout {
    public WindowInsetsFrameLayout(Context context) {
        super(context);
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                requestApplyInsets();
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++)
            getChildAt(index).dispatchApplyWindowInsets(insets);
        return insets;
    }

}
