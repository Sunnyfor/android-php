package com.cocosh.shmstore.widget.photoview;

/**
 * Created by lmg on 2017/10/26.
 */

public interface OnGestureListener {
    void onDrag(float dx, float dy);

    void onFling(float startX, float startY, float velocityX,
                 float velocityY);

    void onScale(float scaleFactor, float focusX, float focusY);
}
