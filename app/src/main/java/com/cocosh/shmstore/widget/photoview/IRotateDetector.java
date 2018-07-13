package com.cocosh.shmstore.widget.photoview;

import android.view.MotionEvent;

/**
 * Created by lmg on 2017/10/26.
 */

public interface IRotateDetector {
    /**
     * handle rotation in onTouchEvent
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    boolean onTouchEvent(MotionEvent event);

    /**
     * is the Gesture Rotate
     *
     * @return true:rotating;false,otherwise
     */
    boolean isRotating();
}
