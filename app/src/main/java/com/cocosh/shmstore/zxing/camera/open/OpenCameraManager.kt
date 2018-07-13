package com.cocosh.shmstore.zxing.camera.open

import com.cocosh.shmstore.zxing.common.PlatformSupportManager

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class OpenCameraManager : PlatformSupportManager<OpenCameraInterface>(OpenCameraInterface::class.java, DefaultOpenCameraInterface()) {
    init {
        addImplementationClass(9, "com.google.zxing.client.android.camera.open.GingerbreadOpenCameraInterface")
    }

}