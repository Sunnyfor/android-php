package com.cocosh.shmstore.widget.progressbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.cocosh.shmstore.R


/**
 *
 * Created by zhangye on 2018/4/12.
 */
class ArcProgressBar : View {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs, defStyleAttr)
    }

    private var paint: Paint? = null
    private lateinit var textPaint: Paint

    private val rectF = RectF()

    private var strokeWidth: Float = 0.toFloat()
    private var suffixTextSize: Float = 0.toFloat()
    private var bottomTextSize: Float = 0.toFloat()
    private var bottomText: String? = null
    private var textSize: Float = 0.toFloat()
    private var textColor: Int = 0
    var progress = 0
    private var max: Int = 0

    private var finishedStrokeColor: Int = 0
    private var unfinishedStrokeColor: Int = 0
    private var arcAngle: Float = 0.toFloat()
    private var suffixText: String? = "%"
    private var suffixTextPadding: Float = 0.toFloat()

    private var arcBottomHeight: Float = 0.toFloat()

    private val defaultFinishedColor = Color.WHITE
    private val defaultUnfinishedColor = Color.rgb(72, 106, 176)
    private val defaultTextColor = Color.rgb(66, 145, 241)
    private var defaultSuffixTextSize: Float = 0f
    private var defaultSuffixPadding: Float = 0f
    private var defaultBottomTextSize: Float = 0f
    private var defaultStrokeWidth: Float = 0f
    private var defaultSuffixText: String = ""
    private val defaultMax = 100
    private val defaultArcAngle = 360 * 0.8f
    private var defaultTextSize: Float = 0.toFloat()
    private var suggestedMinmumHeight: Int = 0


    fun initView(attrs: AttributeSet?, defStyleAttr: Int) {
        suggestedMinmumHeight = resources.getDimension(R.dimen.w230).toInt()
        defaultTextSize = resources.getDimension(R.dimen.w60)
        defaultSuffixTextSize = resources.getDimension(R.dimen.w60)
        defaultSuffixPadding = 0f
        defaultSuffixText = "%"
        defaultBottomTextSize = resources.getDimension(R.dimen.w40)
        defaultStrokeWidth = 0f

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0)
        initByAttributes(attributes)
        attributes.recycle()

        initPainters()
    }

    private fun initByAttributes(attributes: TypedArray) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, defaultFinishedColor)
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, defaultUnfinishedColor)
        textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, defaultTextColor)
        textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, defaultTextSize)
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, defaultArcAngle)
        max = attributes.getInt(R.styleable.ArcProgress_arc_max, defaultMax)
        progress = attributes.getInt(R.styleable.ArcProgress_arc_progress, 0)
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, defaultStrokeWidth)
        suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, defaultSuffixTextSize)
        suffixText = if (TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text))) defaultSuffixText else attributes.getString(R.styleable.ArcProgress_arc_suffix_text)
        suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_padding, defaultSuffixPadding)
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, defaultBottomTextSize)
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text)
    }

    private fun initPainters() {
        textPaint = TextPaint()
        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.isAntiAlias = true

        paint = Paint()
        paint?.color = defaultUnfinishedColor
        paint?.isAntiAlias = true
        paint?.strokeWidth = strokeWidth
        paint?.style = Paint.Style.STROKE
        paint?.strokeCap = Paint.Cap.ROUND
    }

    override fun invalidate() {
        initPainters()
        super.invalidate()
    }

    private fun getStrokeWidth(): Float = strokeWidth

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        this.invalidate()
    }

    private fun getSuffixTextSize(): Float = suffixTextSize

    fun setSuffixTextSize(suffixTextSize: Float) {
        this.suffixTextSize = suffixTextSize
        this.invalidate()
    }

    private fun getBottomText(): String? = bottomText

    fun setBottomText(bottomText: String) {
        this.bottomText = bottomText
        this.invalidate()
    }

    private fun getBottomTextSize(): Float = bottomTextSize

    fun setBottomTextSize(bottomTextSize: Float) {
        this.bottomTextSize = bottomTextSize
        this.invalidate()
    }

    private fun getTextSize(): Float = textSize

    fun setTextSize(textSize: Float) {
        this.textSize = textSize
        this.invalidate()
    }

    private fun getTextColor(): Int = textColor

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        this.invalidate()
    }

    private fun getFinishedStrokeColor(): Int = finishedStrokeColor

    fun setFinishedStrokeColor(finishedStrokeColor: Int) {
        this.finishedStrokeColor = finishedStrokeColor
        this.invalidate()
    }

    private fun getUnfinishedStrokeColor(): Int = unfinishedStrokeColor

    fun setUnfinishedStrokeColor(unfinishedStrokeColor: Int) {
        this.unfinishedStrokeColor = unfinishedStrokeColor
        this.invalidate()
    }

    private fun getArcAngle(): Float = arcAngle

    fun setArcAngle(arcAngle: Float) {
        this.arcAngle = arcAngle
        this.invalidate()
    }

    private fun getSuffixText(): String? = suffixText

    fun setSuffixText(suffixText: String) {
        this.suffixText = suffixText
        this.invalidate()
    }

    private fun getSuffixTextPadding(): Float = suffixTextPadding

    fun setSuffixTextPadding(suffixTextPadding: Float) {
        this.suffixTextPadding = suffixTextPadding
        this.invalidate()
    }

    override fun getSuggestedMinimumWidth(): Int = suggestedMinmumHeight

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f)
        val radius = width / 2f
        val angle = (360 - arcAngle) / 2f
        arcBottomHeight = radius * (1 - Math.cos(angle / 180 * Math.PI)).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val startAngle = 270 - arcAngle / 2f
        val finishedSweepAngle = this.progress / max.toFloat() * arcAngle
        var finishedStartAngle = startAngle
        if (this.progress == 0) finishedStartAngle = 0.01f
        paint?.color = unfinishedStrokeColor
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint)
        paint?.color = finishedStrokeColor
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint)

        val text = progress.toString()
        if (!TextUtils.isEmpty(text)) {
            textPaint.color = ContextCompat.getColor(context,R.color.textGray)
            textPaint.textSize = textSize
            val textHeight = textPaint.descent() + textPaint.ascent()
            val textBaseline = (height - textHeight) / 2.5f
            canvas.drawText(text, (width - textPaint.measureText(text + defaultSuffixText )) / 2.0f, textBaseline, textPaint)
            textPaint.textSize = suffixTextSize
            val suffixHeight = textPaint.descent() + textPaint.ascent()
            canvas.drawText(suffixText, (width - textPaint.measureText(text + defaultSuffixText )) / 2.0f + suffixTextPadding +textPaint.measureText(text) , textBaseline + textHeight - suffixHeight, textPaint)
        }

        if (arcBottomHeight == 0f) {
            val radius = width / 2f
            val angle = (360 - arcAngle) / 2f
            arcBottomHeight = radius * (1 - Math.cos(angle / 180 * Math.PI)).toFloat()
        }

        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint.textSize = bottomTextSize
            textPaint.color = ContextCompat.getColor(context,R.color.grayText)
            val bottomTextBaseline = height - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2
            canvas.drawText(getBottomText(), (width - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth())
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize())
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding())
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize())
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText())
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize())
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor())
        bundle.putInt(INSTANCE_PROGRESS, progress)
        bundle.putInt(INSTANCE_MAX, max)
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor())
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor())
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle())
        bundle.putString(INSTANCE_SUFFIX, getSuffixText())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            strokeWidth = state.getFloat(INSTANCE_STROKE_WIDTH)
            suffixTextSize = state.getFloat(INSTANCE_SUFFIX_TEXT_SIZE)
            suffixTextPadding = state.getFloat(INSTANCE_SUFFIX_TEXT_PADDING)
            bottomTextSize = state.getFloat(INSTANCE_BOTTOM_TEXT_SIZE)
            bottomText = state.getString(INSTANCE_BOTTOM_TEXT)
            textSize = state.getFloat(INSTANCE_TEXT_SIZE)
            textColor = state.getInt(INSTANCE_TEXT_COLOR)
            max = state.getInt(INSTANCE_MAX)
            progress = state.getFloat(INSTANCE_PROGRESS).toInt()
            finishedStrokeColor = state.getInt(INSTANCE_FINISHED_STROKE_COLOR)
            unfinishedStrokeColor = state.getInt(INSTANCE_UNFINISHED_STROKE_COLOR)
            suffixText = state.getString(INSTANCE_SUFFIX)
            initPainters()
            super.onRestoreInstanceState(state.getParcelable<Parcelable>(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    companion object {
        private const val INSTANCE_STATE = "saved_instance"
        private const val INSTANCE_STROKE_WIDTH = "stroke_width"
        private const val INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size"
        private const val INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding"
        private const val INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size"
        private const val INSTANCE_BOTTOM_TEXT = "bottom_text"
        private const val INSTANCE_TEXT_SIZE = "text_size"
        private const val INSTANCE_TEXT_COLOR = "text_color"
        private const val INSTANCE_PROGRESS = "progress"
        private const val INSTANCE_MAX = "max"
        private const val INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color"
        private const val INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color"
        private const val INSTANCE_ARC_ANGLE = "arc_angle"
        private const val INSTANCE_SUFFIX = "suffix"
    }
}