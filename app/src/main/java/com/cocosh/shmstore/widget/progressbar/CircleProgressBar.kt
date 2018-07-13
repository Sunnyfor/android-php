package com.cocosh.shmstore.widget.progressbar

import android.content.Context
import android.graphics.*
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.widget.ProgressBar
import com.cocosh.shmstore.R
import com.cocosh.shmstore.utils.DimensUtil
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 *
 * Created by zhangye on 2018/4/8.
 */
class CircleProgressBar : ProgressBar {

    private val mProgressRectF = RectF()
    private val mProgressTextRect = Rect()

    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mRadius: Float = 0.toFloat()
    private var mCenterX: Float = 0.toFloat()
    private var mCenterY: Float = 0.toFloat()

    //Only work well in the Line Style, represents the line count of the rings included
    private var mLineCount: Int = 0
    //Only work well in the Line Style, Height of the line of the progress bar
    private var mLineWidth: Float = 0.toFloat()

    //Stroke width of the progress of the progress bar
    private var mProgressStrokeWidth: Float = 0.toFloat()
    //Text size of the progress of the progress bar
    private var mProgressTextSize: Float = 0.toFloat()

    //Start color of the progress of the progress bar
    private var mProgressStartColor: Int = 0
    //End color of the progress of the progress bar
    private var mProgressEndColor: Int = 0
    //Color of the progress value of the progress bar
    private var mProgressTextColor: Int = 0
    //Background color of the progress of the progress bar
    private var mProgressBackgroundColor: Int = 0

    //If mDrawProgressText is true, will draw the progress text. otherwise, will not draw the progress text.
    private var mDrawProgressText: Boolean = false
    //Format the current progress value to the specified format
    private var mProgressTextFormatPattern: String? = null

    //The style of the progress color
    @Style
    private var mStyle: Int = 0

    //The Shader of mProgressPaint
    @ShaderMode
    private var mShader: Int = 0
    //The Stroke Cap of mProgressPaint and mBackgroundPaint
    private var mCap: Paint.Cap? = null

    var progressTextFormatPattern: String?
        get() = mProgressTextFormatPattern
        set(progressTextformatPattern) {
            this.mProgressTextFormatPattern = progressTextformatPattern
            invalidate()
        }

    var progressStrokeWidth: Float
        get() = mProgressStrokeWidth
        set(progressStrokeWidth) {
            this.mProgressStrokeWidth = progressStrokeWidth
            mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2)
            invalidate()
        }

    var progressTextSize: Float
        get() = mProgressTextSize
        set(progressTextSize) {
            this.mProgressTextSize = progressTextSize
            invalidate()
        }

    var progressStartColor: Int
        get() = mProgressStartColor
        set(progressStartColor) {
            this.mProgressStartColor = progressStartColor
            updateProgressShader()
            invalidate()
        }

    var progressEndColor: Int
        get() = mProgressEndColor
        set(progressEndColor) {
            this.mProgressEndColor = progressEndColor
            updateProgressShader()
            invalidate()
        }

    var progressTextColor: Int
        get() = mProgressTextColor
        set(progressTextColor) {
            this.mProgressTextColor = progressTextColor
            invalidate()
        }

    var progressBackgroundColor: Int
        get() = mProgressBackgroundColor
        set(progressBackgroundColor) {
            this.mProgressBackgroundColor = progressBackgroundColor
            mBackgroundPaint.color = mProgressBackgroundColor
            invalidate()
        }

    var lineCount: Int
        get() = mLineCount
        set(lineCount) {
            this.mLineCount = lineCount
            invalidate()
        }

    var lineWidth: Float
        get() = mLineWidth
        set(lineWidth) {
            this.mLineWidth = lineWidth
            invalidate()
        }

    var style: Int
        get() = mStyle
        set(@Style style) {
            this.mStyle = style
            mProgressPaint.style = if (mStyle == SOLID) Paint.Style.FILL else Paint.Style.STROKE
            mBackgroundPaint.style = if (mStyle == SOLID) Paint.Style.FILL else Paint.Style.STROKE
            invalidate()
        }

    var shader: Int
        get() = mShader
        set(@ShaderMode shader) {
            mShader = shader
            updateProgressShader()
            invalidate()
        }

    var cap: Paint.Cap?
        get() = mCap
        set(cap) {
            mCap = cap
            mProgressPaint.strokeCap = cap
            mBackgroundPaint.strokeCap = cap
            invalidate()
        }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    @IntDef(LINE.toLong(), SOLID.toLong(), SOLID_LINE.toLong())
    private annotation class Style

    @IntDef(LINEAR.toLong(), RADIAL.toLong(), SWEEP.toLong())
    private annotation class ShaderMode

    fun initView(attrs: AttributeSet?) {
        adjustIndeterminate()
        initFromAttributes(context, attrs)
        initPaint()
    }

    /**
     * Basic data initialization
     */
    private fun initFromAttributes(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)

        mDrawProgressText = a.getBoolean(R.styleable.CircleProgressBar_draw_progress_text, true)

        mLineCount = a.getInt(R.styleable.CircleProgressBar_line_count, DEFAULT_LINE_COUNT)
        mProgressTextFormatPattern = if (a.hasValue(R.styleable.CircleProgressBar_progress_text_format_pattern))
            a.getString(R.styleable.CircleProgressBar_progress_text_format_pattern)
        else
            DEFAULT_PATTERN

        mStyle = a.getInt(R.styleable.CircleProgressBar_style, LINE)
        mShader = a.getInt(R.styleable.CircleProgressBar_progress_shader, LINEAR)
        mCap = if (a.hasValue(R.styleable.CircleProgressBar_progress_stroke_cap))
            Paint.Cap.values()[a.getInt(R.styleable.CircleProgressBar_progress_stroke_cap, 0)]
        else
            Paint.Cap.BUTT

        mLineWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_line_width, DimensUtil.dip2px(DEFAULT_LINE_WIDTH)).toFloat()
        mProgressTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_text_size, DimensUtil.dip2px(DEFAULT_PROGRESS_TEXT_SIZE)).toFloat()
        mProgressStrokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_stroke_width, DimensUtil.dip2px(DEFAULT_PROGRESS_STROKE_WIDTH)).toFloat()

        mProgressStartColor = a.getColor(R.styleable.CircleProgressBar_progress_start_color, Color.parseColor(COLOR_FFF2A670))
        mProgressEndColor = a.getColor(R.styleable.CircleProgressBar_progress_end_color, Color.parseColor(COLOR_FFF2A670))
        mProgressTextColor = a.getColor(R.styleable.CircleProgressBar_progress_text_color, Color.parseColor(COLOR_FFF2A670))
        mProgressBackgroundColor = a.getColor(R.styleable.CircleProgressBar_progress_background_color, Color.parseColor(COLOR_FFD3D3D5))

        a.recycle()
    }

    /**
     * Paint initialization
     */
    private fun initPaint() {
        mProgressTextPaint.textAlign = Paint.Align.CENTER
        mProgressTextPaint.textSize = mProgressTextSize

        mProgressPaint.style = if (mStyle == SOLID) Paint.Style.FILL else Paint.Style.STROKE
        mProgressPaint.strokeWidth = mProgressStrokeWidth
        mProgressPaint.color = mProgressStartColor
        mProgressPaint.strokeCap = mCap

        mBackgroundPaint.style = if (mStyle == SOLID) Paint.Style.FILL else Paint.Style.STROKE
        mBackgroundPaint.strokeWidth = mProgressStrokeWidth
        mBackgroundPaint.color = mProgressBackgroundColor
        mBackgroundPaint.strokeCap = mCap
    }

    /**
     * The progress bar color gradient,
     * need to be invoked in the [.onSizeChanged]
     */
    private fun updateProgressShader() = if (mProgressStartColor != mProgressEndColor) {
        var shader: Shader? = null
        when (mShader) {
            LINEAR -> shader = LinearGradient(mProgressRectF.left, mProgressRectF.top,
                    mProgressRectF.left, mProgressRectF.bottom,
                    mProgressStartColor, mProgressEndColor, Shader.TileMode.CLAMP)
            RADIAL -> shader = RadialGradient(mCenterX, mCenterY, mRadius,
                    mProgressStartColor, mProgressEndColor, Shader.TileMode.CLAMP)
            SWEEP -> {
                //arc = radian * radius
                val radian = (mProgressStrokeWidth / Math.PI * 2.0f / mRadius).toFloat()
                val rotateDegrees = if (mCap == Paint.Cap.BUTT && mStyle == SOLID_LINE) {
                    DEFAULT_START_DEGREE
                } else {
                    DEFAULT_START_DEGREE - (Math.toDegrees(radian.toDouble())).toFloat()
                }

                shader = SweepGradient(mCenterX, mCenterY, intArrayOf(mProgressStartColor, mProgressEndColor),
                        floatArrayOf(0.0f, 1.0f))
                val matrix = Matrix()
                matrix.postRotate(rotateDegrees, mCenterX, mCenterY)
                shader.setLocalMatrix(matrix)
            }
        }

        mProgressPaint.shader = shader
    } else {
        mProgressPaint.shader = null
        mProgressPaint.color = mProgressStartColor
    }

    /**
     * In order to work well, need to modify some of the following fields through reflection.
     * Another available way: write the following attributes to the xml
     *
     *
     * android:indeterminateOnly="false"
     * android:indeterminate="false"
     */
    private fun adjustIndeterminate() {
        try {
            val mOnlyIndeterminateField = ProgressBar::class.java.getDeclaredField("mOnlyIndeterminate")
            mOnlyIndeterminateField.isAccessible = true
            mOnlyIndeterminateField.set(this, false)

            val mIndeterminateField = ProgressBar::class.java.getDeclaredField("mIndeterminate")
            mIndeterminateField.isAccessible = true
            mIndeterminateField.set(this, false)

            val mCurrentDrawableField = ProgressBar::class.java.getDeclaredField("mCurrentDrawable")
            mCurrentDrawableField.isAccessible = true
            mCurrentDrawableField.set(this, null)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        drawProgress(canvas)
        drawProgressText(canvas)
    }

    private fun drawProgressText(canvas: Canvas) {
        if (!mDrawProgressText) {
            return
        }

        val progressText = String.format(mProgressTextFormatPattern!!, progress)

        mProgressTextPaint.textSize = mProgressTextSize
        mProgressTextPaint.color = mProgressTextColor
        mProgressTextPaint.getTextBounds(progressText, 0, progressText.length, mProgressTextRect)
        canvas.drawText(progressText, mCenterX, mCenterY + mProgressTextRect.height() / 2, mProgressTextPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        when (mStyle) {
            SOLID -> drawSolidProgress(canvas)
            SOLID_LINE -> drawSolidLineProgress(canvas)
            LINE -> drawLineProgress(canvas)
            else -> drawLineProgress(canvas)
        }
    }

    /**
     * In the center of the drawing area as a reference point , rotate the canvas
     */
    private fun drawLineProgress(canvas: Canvas) {
        val unitDegrees = (2.0f * Math.PI / mLineCount).toFloat()
        val outerCircleRadius = mRadius
        val interCircleRadius = mRadius - mLineWidth

        val progressLineCount = (progress.toFloat() / max.toFloat() * mLineCount).toInt()

        for (i in 0 until mLineCount) {
            val rotateDegrees = i * unitDegrees

            val startX = mCenterX + Math.sin(rotateDegrees.toDouble()).toFloat() * interCircleRadius
            val startY = mCenterX - Math.cos(rotateDegrees.toDouble()).toFloat() * interCircleRadius

            val stopX = mCenterX + Math.sin(rotateDegrees.toDouble()).toFloat() * outerCircleRadius
            val stopY = mCenterX - Math.cos(rotateDegrees.toDouble()).toFloat() * outerCircleRadius

            if (i < progressLineCount) {
                canvas.drawLine(startX, startY, stopX, stopY, mProgressPaint)
            } else {
                canvas.drawLine(startX, startY, stopX, stopY, mBackgroundPaint)
            }
        }
    }

    /**
     * Just draw arc
     */
    private fun drawSolidProgress(canvas: Canvas) {
        canvas.drawArc(mProgressRectF, DEFAULT_START_DEGREE, 360.0f, false, mBackgroundPaint)
        canvas.drawArc(mProgressRectF, DEFAULT_START_DEGREE, 360.0f * progress / max, true, mProgressPaint)
    }

    /**
     * Just draw arc
     */
    private fun drawSolidLineProgress(canvas: Canvas) {
        canvas.drawArc(mProgressRectF, DEFAULT_START_DEGREE, 360.0f, false, mBackgroundPaint)
        canvas.drawArc(mProgressRectF, DEFAULT_START_DEGREE, 360.0f * progress / max, false, mProgressPaint)
    }

    /**
     * When the size of CircleProgressBar changed, need to re-adjust the drawing area
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = (w / 2).toFloat()
        mCenterY = (h / 2).toFloat()

        mRadius = Math.min(mCenterX, mCenterY)
        mProgressRectF.top = mCenterY - mRadius
        mProgressRectF.bottom = mCenterY + mRadius
        mProgressRectF.left = mCenterX - mRadius
        mProgressRectF.right = mCenterX + mRadius

        updateProgressShader()

        //Prevent the progress from clipping
        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2)
    }

    companion object {
        private const val LINE = 0
        private const val SOLID = 1
        private const val SOLID_LINE = 2

        private const val LINEAR = 0
        private const val RADIAL = 1
        private const val SWEEP = 2

        private val DEFAULT_START_DEGREE = -90.0f

        private val DEFAULT_LINE_COUNT = 45

        private val DEFAULT_LINE_WIDTH = 4.0f
        private val DEFAULT_PROGRESS_TEXT_SIZE = 11.0f
        private val DEFAULT_PROGRESS_STROKE_WIDTH = 1.0f

        private val COLOR_FFF2A670 = "#fff2a670"
        private val COLOR_FFD3D3D5 = "#ffe3e3e5"
        private val DEFAULT_PATTERN = "%d%%"
    }
}