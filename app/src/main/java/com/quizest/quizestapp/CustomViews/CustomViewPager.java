package com.quizest.quizestapp.CustomViews;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    /*global field instances*/
    private boolean enabled;

    /*constructor for the view pager*/
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

//    handle the touch of the view pager
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.enabled && super.onInterceptTouchEvent(event);

    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
