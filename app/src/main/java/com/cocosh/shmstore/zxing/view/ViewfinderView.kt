package com.cocosh.shmstore.zxing.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.zxing.camera.CameraManager
import com.google.zxing.ResultPoint
import java.util.ArrayList

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class ViewfinderView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var cameraManager: CameraManager? = null
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var resultBitmap: Bitmap? = null
    private val maskColor: Int
    private val resultColor: Int
    private var scannerAlpha: Int = 0
    private val possibleResultPoints: MutableList<ResultPoint>

    private var i = 0// 添加的
    private val mRect: Rect// 扫描线填充边界
    private val lineDrawable: Drawable?// 采用图片作为扫描线

    init {
        maskColor = ContextCompat.getColor(context, R.color.viewfinder_mask)
        resultColor = ContextCompat.getColor(context,R.color.result_view)

        // GradientDrawable、lineDrawable
        mRect = Rect()
        lineDrawable = ContextCompat.getDrawable(context,R.drawable.zx_code_line)
        scannerAlpha = 0
        possibleResultPoints = ArrayList(5)
    }

    fun setCameraManager(cameraManager: CameraManager) {
        this.cameraManager = cameraManager
    }

    public override fun onDraw(canvas: Canvas) {
        if (cameraManager == null) {
            return
        }

        val frame = cameraManager!!.getFramingRect() ?: return

        val width = canvas.width
        val height = canvas.height

        // 画扫描框外部的暗色背景
        // 设置蒙板颜色
        paint.color = if (resultBitmap != null) resultColor else maskColor
        // 头部
        canvas.drawRect(0f, 0f, width.toFloat(), frame.top.toFloat(), paint)
        // 左边
        canvas.drawRect(0f, frame.top.toFloat(), frame.left.toFloat(), frame.bottom.toFloat(), paint)
        // 右边
        canvas.drawRect(frame.right.toFloat(), frame.top.toFloat(), width.toFloat(), frame.bottom.toFloat(), paint)
        // 底部
        canvas.drawRect(0f, frame.bottom.toFloat(), width.toFloat(), height.toFloat(), paint)

        if (resultBitmap != null) {
            // 在扫描框中画出预览图
            paint.alpha = CURRENT_POINT_OPACITY
            canvas.drawBitmap(resultBitmap!!, null, frame, paint)
        } else {
            // 画出四个角
            paint.color = ContextCompat.getColor(context,R.color.red)

            canvas.drawRect(frame.left.toFloat(), frame.top.toFloat(), (frame.left + 50).toFloat(),
                    (frame.top + 5).toFloat(), paint)
            canvas.drawRect(frame.left.toFloat(), frame.top.toFloat(), (frame.left + 5).toFloat(),
                    (frame.top + 50).toFloat(), paint)
            // 右上角
            canvas.drawRect((frame.right - 50).toFloat(), frame.top.toFloat(), frame.right.toFloat(),
                    (frame.top + 5).toFloat(), paint)
            canvas.drawRect((frame.right - 5).toFloat(), frame.top.toFloat(), frame.right.toFloat(),
                    (frame.top + 50).toFloat(), paint)
            // 左下角
            canvas.drawRect(frame.left.toFloat(), (frame.bottom - 5).toFloat(), (frame.left + 50).toFloat(),
                    frame.bottom.toFloat(), paint)
            canvas.drawRect(frame.left.toFloat(), (frame.bottom - 50).toFloat(), (frame.left + 5).toFloat(),
                    frame.bottom.toFloat(), paint)
            // 右下角
            canvas.drawRect((frame.right - 50).toFloat(), (frame.bottom - 5).toFloat(), frame.right.toFloat(),
                    frame.bottom.toFloat(), paint)
            canvas.drawRect((frame.right - 5).toFloat(), (frame.bottom - 50).toFloat(), frame.right.toFloat(),
                    frame.bottom.toFloat(), paint)

            // 在扫描框中画出模拟扫描的线条
            // 设置扫描线条颜色为绿色
            paint.color = ContextCompat.getColor(context,R.color.red)
            // 设置绿色线条的透明值
            paint.alpha = SCANNER_ALPHA[scannerAlpha]
            // 透明度变化
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.size

            // 画出固定在中部的线条
            // int middle = frame.height() / 2 + frame.top;
            // canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1,
            // middle + 2, paint);

            // 将扫描线修改为上下走的线
            i+=6
            if (i < frame.bottom - frame.top) {

                /* 以下为图片作为扫描线 */
                mRect.set(frame.left - 6, frame.top + i - 6, frame.right + 6,
                        frame.top + 6 + i)
                lineDrawable?.bounds = mRect
                lineDrawable?.draw(canvas)

                // 刷新
                invalidate()
            } else {
                i = 0
            }

            // 画扫描框下面的字
            paint.color = Color.WHITE
            paint.textSize = TEXT_SIZE * density
            paint.alpha = 0x40
            paint.typeface = Typeface.create("System", Typeface.BOLD)
            canvas.drawText(
                    resources.getString(R.string.scan_text),
                    frame.left.toFloat(),
                    (frame.bottom + TEXT_PADDING_TOP.toFloat() * density),
                    paint)

            // 重复执行扫描框区域绘制(画四个角及扫描线)
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom)
        }
    }

    fun drawViewfinder() {
        val resultBitmap = this.resultBitmap
        this.resultBitmap = null
        resultBitmap?.recycle()
        invalidate()
    }

    fun drawResultBitmap(barcode: Bitmap) {
        resultBitmap = barcode
        invalidate()
    }

    fun addPossibleResultPoint(point: ResultPoint) {
        val points = possibleResultPoints
        synchronized(points) {
            points.add(point)
            val size = points.size
            if (size > MAX_RESULT_POINTS) {
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear()
            }
        }
    }

    fun recycleLineDrawable() {
        if (lineDrawable != null) {
            lineDrawable.callback = null
        }
    }

    companion object {
        /**
         * 字体大小
         */
        private const val TEXT_SIZE = 16
        /**
         * 手机的屏幕密度
         */
        /**
         * 字体距离扫描框下面的距离
         */
        private const val TEXT_PADDING_TOP = 30
        private const val density: Float = 0.toFloat()
        private val SCANNER_ALPHA = intArrayOf(0, 64, 128, 192, 255, 192, 128, 64)
        private const val CURRENT_POINT_OPACITY = 0xA0
        private const val MAX_RESULT_POINTS = 20
        private const val ANIMATION_DELAY = 80L
    }
}
