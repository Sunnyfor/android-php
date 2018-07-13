package com.cocosh.shmstore.zxing.view

import com.google.zxing.ResultPoint
import com.google.zxing.ResultPointCallback

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class ViewfinderResultPointCallback(private val viewfinderView: ViewfinderView?) : ResultPointCallback {

    override fun foundPossibleResultPoint(point: ResultPoint) {
        viewfinderView?.addPossibleResultPoint(point)
    }

}
