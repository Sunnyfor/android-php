package com.cocosh.shmstore.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import com.cocosh.shmstore.R


/**
 * 圆形动画
 * Created by lmg on 2018/4/4.
 */
class SuperLoadingProgress : View {
    /**
     * 打钩
     */
    private var mRotateAnimation: ValueAnimator? = null
    /**
     * 打钩
     */
    private var mTickAnimation: ValueAnimator? = null
    /**
     * 绘制感叹号
     */
    private var mshockAnimation: ValueAnimator? = null
    /**
     * 绘制感叹号震动
     */
    private var mCommaAnimation: ValueAnimator? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mRectF = RectF()
    private var circlePaint = Paint()
    private var commaPaint = Paint()
    private var tickPaint = Paint()
    /**
     * 画笔宽度
     */
    private var strokeWidth = 20
    /**
     *
     */
    private var radius = 0f
    //0画圆,1抛出方块,2下落变粗,挤压圆形,3,绘制三叉，圆形恢复,4,绿色勾,5,红色感叹号出现，6,红色感叹号震动
    private var status = 0
    /**
     * 测量打钩
     */
    private var tickPathMeasure: PathMeasure? = null
    /**
     * 感叹号
     */
    private var commaPathMeasure1: PathMeasure? = null
    private var commaPathMeasure2: PathMeasure? = null
    /**
     * 打钩百分比
     * @param context
     */
    var tickPrecent = 0f
    /**
     * 感叹号百分比
     * @param context
     */
    var commaPrecent = 0f
    /**
     * 震动百分比
     * @param context
     */
    var shockPrecent = 0
    /**
     * 是否loading成功
     */
    var isSuccess = false
    /**
     * 震动角度
     */
    private var shockDir = 20

    constructor(context: Context) : super(context) {
        initData()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initData()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initData()
    }

    private fun initData() {
        circlePaint.isAntiAlias = true
        circlePaint.color = resources.getColor(R.color.red)
        circlePaint.strokeWidth = strokeWidth.toFloat()
        circlePaint.style = Paint.Style.STROKE

        commaPaint.isAntiAlias = true
        commaPaint.color = resources.getColor(R.color.red)
        commaPaint.strokeWidth = strokeWidth.toFloat()
        commaPaint.style = Paint.Style.STROKE

        tickPaint.color = resources.getColor(R.color.red)
        tickPaint.isAntiAlias = true
        tickPaint.strokeWidth = strokeWidth.toFloat()
        tickPaint.style = Paint.Style.STROKE

        //旋转动画
        mRotateAnimation = ValueAnimator.ofFloat(0f, 360f)
        mRotateAnimation?.duration = 1000
        mRotateAnimation?.interpolator = AccelerateInterpolator() as TimeInterpolator?
        mRotateAnimation?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            circlePrecent = animation.animatedValue as Float
            invalidate()
        })

        mRotateAnimation?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (isSuccess) {
                    status = 10
                    mTickAnimation?.start()
                } else {
                    status = 11
                    mCommaAnimation?.start()
                }
            }
        })

        //打钩动画
        mTickAnimation = ValueAnimator.ofFloat(0f, 1f)
        mTickAnimation?.duration = 500
        mTickAnimation?.interpolator = AccelerateInterpolator()
        mTickAnimation?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            tickPrecent = animation.animatedValue as Float
            invalidate()
        })

        mTickAnimation?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)

            }
        })

        //感叹号动画
        mCommaAnimation = ValueAnimator.ofFloat(0f, 1f)
        mCommaAnimation?.duration = 300
        mCommaAnimation?.interpolator = AccelerateInterpolator()
        mCommaAnimation?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            commaPrecent = animation.animatedValue as Float
            invalidate()
        })

        mCommaAnimation?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                status = 12
                mshockAnimation?.start()
            }
        })

        //震动动画
        mshockAnimation = ValueAnimator.ofInt(-1, 0, 1, 0, -1, 0, 1, 0)
        mshockAnimation?.duration = 500
        mshockAnimation?.interpolator = LinearInterpolator()
        mshockAnimation?.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            shockPrecent = animation.animatedValue as Int
            invalidate()
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h
        var minValue = Math.min(measuredWidth, measuredHeight)
        radius = (Math.min(measuredWidth, measuredHeight) / 4).toFloat()

        mRectF.set(RectF(0F + strokeWidth / 2, 1f + strokeWidth / 2, minValue.toFloat() - strokeWidth / 2, minValue.toFloat() - strokeWidth / 2))
        val tickPath = Path()
        tickPath.moveTo((0.3 * width).toFloat(), (0.5 * width).toFloat())
        tickPath.lineTo((0.43 * width).toFloat(), (0.66 * width).toFloat())
        tickPath.lineTo((0.75 * width).toFloat(), (0.4 * width).toFloat())
        tickPathMeasure = PathMeasure(tickPath, false)
        //感叹号路径
        val commaPath1 = Path()
        val commaPath2 = Path()
        commaPath1.moveTo(0.5f * width, 0.2f * width)
        commaPath1.lineTo(0.5f * width, 0.65f * width)
        commaPath2.moveTo(0.5f * width, 0.70f * width)
        commaPath2.lineTo(0.5f * width, 0.75f * width)
        commaPathMeasure1 = PathMeasure(commaPath1, false)
        commaPathMeasure2 = PathMeasure(commaPath2, false)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSpecSize + 10 * strokeWidth, heightSpecSize + 10 * strokeWidth)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        drawCircle(canvas!!)
        when (status) {
            10 -> drawTick(canvas!!)
            11 -> drawComma(canvas!!)
            12 -> drawShockComma(canvas!!)
        }
    }

    var circlePrecent = 0f
    fun drawCircle(canvas: Canvas) {
        canvas.drawArc(mRectF, 0f, circlePrecent, false, circlePaint)
    }

    /**
     * 绘制打钩
     * @param canvas
     */
    private fun drawTick(canvas: Canvas) {
        canvas.save()
        val path = Path()
        tickPathMeasure?.getSegment(0F, tickPrecent * tickPathMeasure!!.length, path, true)
        path.rLineTo(0F, 0F)
        canvas.drawPath(path, tickPaint)
    }

    /**
     * 绘制感叹号
     */
    private fun drawComma(canvas: Canvas) {
        val path1 = Path()
        commaPathMeasure1?.getSegment(0F, commaPrecent * commaPathMeasure1!!.length, path1, true)
        path1.rLineTo(0F, 0F)
        val path2 = Path()
        commaPathMeasure2?.getSegment(0F, commaPrecent * commaPathMeasure2!!.length, path2, true)
        path2.rLineTo(0F, 0F)
        canvas.drawPath(path1, commaPaint)
        canvas.drawPath(path2, commaPaint)
    }

    /**
     * 绘制震动效果
     * @param canvas
     */
    private fun drawShockComma(canvas: Canvas) {
        val path1 = Path()
        commaPathMeasure1?.getSegment(0F, commaPathMeasure1!!.length, path1, true)
        path1.rLineTo(0F, 0F)
        val path2 = Path()
        commaPathMeasure2?.getSegment(0F, commaPathMeasure2!!.length, path2, true)
        path2.rLineTo(0F, 0F)

        if (shockPrecent !== 0) {
            canvas.save()
            if (shockPrecent === 1)
                canvas.rotate(shockDir.toFloat(), 2 * radius, 2 * radius)
            else if (shockPrecent === -1)
                canvas.rotate((-shockDir).toFloat(), 2 * radius, 2 * radius)
        }
        canvas.drawPath(path1, commaPaint)
        canvas.drawPath(path2, commaPaint)

        if (shockPrecent !== 0) {
            canvas.restore()
        }
    }

    /**
     * 开始完成动画
     */
    private fun start() {
        post { mRotateAnimation?.start() }
    }

    /**
     * loading成功后调用
     */
    fun finishSuccess() {
        this.isSuccess = true
        start()
    }

    /**
     * loading失败后调用
     */
    fun finishFail() {
        this.isSuccess = false
        start()
    }

}