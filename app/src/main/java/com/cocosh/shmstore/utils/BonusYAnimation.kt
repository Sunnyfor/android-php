package com.cocosh.shmstore.utils

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation


/**
 *
 * Created by zhangye on 2018/5/29.
 */
class BonusYAnimation: Animation() {
    private var centerX: Int = 0
    private var centerY: Int = 0
    private var camera = Camera()

    /**
     * 获取坐标，定义动画时间
     * @param width
     * @param height
     * @param parentWidth
     * @param parentHeight
     */
    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        //获得中心点坐标
        centerX = width / 2
        centerY = width / 2
        //动画执行时间 自行定义
        duration = (1 * 1000).toLong()
        interpolator = DecelerateInterpolator()
    }

    /**
     * 旋转的角度设置
     * @param interpolatedTime
     * @param t
     */
     override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val matrix = t.matrix
        camera.save()
        //中心是Y轴旋转，这里可以自行设置X轴 Y轴 Z轴
        camera.rotateY(360 * interpolatedTime)
        //把我们的摄像头加在变换矩阵上
        camera.getMatrix(matrix)
        //设置翻转中心点
        matrix.preTranslate((-centerX).toFloat(), (-centerY).toFloat())
        matrix.postTranslate(centerX.toFloat(), centerY.toFloat())
        camera.restore()
    }
}