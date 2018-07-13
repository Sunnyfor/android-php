package com.cocosh.shmstore.widget.photoview;

import android.view.MotionEvent;


/**
 * Created by lmg on 2017/10/26.
 */
public interface GestureDetector {

    boolean onTouchEvent(MotionEvent ev);

    boolean isScaling();

    boolean isDragging();

    void setOnGestureListener(OnGestureListener listener);

}