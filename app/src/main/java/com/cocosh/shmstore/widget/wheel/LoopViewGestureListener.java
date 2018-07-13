package com.cocosh.shmstore.widget.wheel;

import android.view.MotionEvent;

/**
 * Desc:
 * Author: chencha
 * Date: 16/11/4
 */

public class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final WheelView2 loopView;

    LoopViewGestureListener(WheelView2 loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
