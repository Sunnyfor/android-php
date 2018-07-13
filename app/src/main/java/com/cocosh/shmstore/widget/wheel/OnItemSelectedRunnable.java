package com.cocosh.shmstore.widget.wheel;

import com.cocosh.shmstore.widget.wheel.WheelView2;

/**
 * Desc:
 * Author: chencha
 * Date: 16/11/4
 */

final class OnItemSelectedRunnable implements Runnable {
    final WheelView2 loopView;

    OnItemSelectedRunnable(WheelView2 loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
    }
}
