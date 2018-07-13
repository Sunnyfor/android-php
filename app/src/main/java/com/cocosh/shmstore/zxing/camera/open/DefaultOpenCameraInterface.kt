package com.cocosh.shmstore.zxing.camera.open

import android.hardware.Camera

/**
 *
 * Created by zhangye on 2018/4/26.
 */
internal class DefaultOpenCameraInterface : OpenCameraInterface {

    /**
     * Calls [Camera.open].
     */
    override fun open(): Camera = Camera.open()
}