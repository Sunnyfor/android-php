package com.cocosh.shmstore.widget


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import com.cocosh.shmstore.R


/**
 * ViewPager指示器
 */
class FlymeTabStrip : HorizontalScrollView {
    /**
     * 指示器容器
     */
    private var container: LinearLayout? = null
    /**
     * 指示器个数
     */
    private var tabCount: Int = 0
    /**
     * 当前tab位置，默认为0
     */
    private var currentPosition = 0
    /**
     * 选中的tab位置
     */
    private var selectedPosition: Int = 0
    /**
     *
     */
    private var currentPositionOffset = 0f
    /**
     *
     */
    private var lastScrollX = 0
    /**
     * LayoutParams用于添加指示器到指示器容器中时使用，按等权重分配指示器宽度
     */
    private var expandedTabLayoutParams: LinearLayout.LayoutParams? = null
    /**
     * 指示器颜色
     */
    private var indicatorColor: Int = 0
    /**
     * 文字颜色
     */
    private var textColor: Int = 0
    /**
     * 文字大小
     */
    private var textSize = resources.getDimension(R.dimen.w40)
    /**
     * 选中位置的文字大小
     */
    private var selectedTextSize = resources.getDimension(R.dimen.w40)
    /**
     * 指示器高度
     */
    private var indicatorHeight: Int = 0
    /**
     * 指示器左右间距
     */
    private var indicatorMargin: Int = 0
    /**
     * ViewPager
     */
    private var viewPager: ViewPager? = null
    /**
     * viewpager的适配器
     */
    private var pagerAdapter: PagerAdapter? = null

    /**
     * page改变监听器
     */
    private val pagerStateChangeListener = PagerStateChangeListener()
    /**
     * 画笔
     */
    private var paint: Paint? = null

    private var mBrounds: RectF? = null//rect


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    /**
     * 初始化
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        // 取消横向的滚动条
        isHorizontalScrollBarEnabled = false
        // 指示器容器初始化
        container = LinearLayout(context)
        container!!.orientation = LinearLayout.HORIZONTAL
        container!!.layoutParams = FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        // 添加指示器容器到scrollview
        addView(container)

        // 获取屏幕相关信息

        val typedArray = context.theme.obtainStyledAttributes(attrs,
                R.styleable.FlymeTabStrip, defStyleAttr, defStyleRes)
        val n = typedArray.indexCount
        for (i in 0 until n) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.FlymeTabStrip_indicatorColor) {
                indicatorColor = typedArray.getColor(attr, Color.YELLOW)

                // 指示器高度，默认2
            } else if (attr == R.styleable.FlymeTabStrip_indicatorHeight) {
                indicatorHeight = typedArray.getDimensionPixelSize(attr, 2)

                // 指示器左右间距，默认20
            } else if (attr == R.styleable.FlymeTabStrip_indicatorMargin) {
                indicatorMargin = typedArray.getDimensionPixelSize(attr, 20)

                // 文字颜色,默认黑色
            } else if (attr == R.styleable.FlymeTabStrip_indicatorTextColor) {
                textColor = typedArray.getColor(attr, ContextCompat.getColor(context,R.color.blackText))

                // 文字大小，默认15
            } else if (attr == R.styleable.FlymeTabStrip_indicatorTextSize) {

                textSize = typedArray.getDimension(attr, resources.getDimension(R.dimen.w40))
                // 选中项的文字大小，默认186
            } else if (attr == R.styleable.FlymeTabStrip_selectedIndicatorTextSize) {

                selectedTextSize = typedArray.getDimension(attr, resources.getDimension(R.dimen.w40))
            } else {
            }
        }
        // typedArray回收
        typedArray.recycle()

        expandedTabLayoutParams = LinearLayout.LayoutParams(0,
                FrameLayout.LayoutParams.MATCH_PARENT, 1.0f)
        // 初始化画笔
        paint = Paint()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 如果指示器个数为0，直接结束绘画
        if (tabCount == 0) {
            return
        }
        // 获取onMeasure后的高
        val height = height
        /*
         * 画指示器下方的线
		 */
        // 设置颜色
        paint!!.color = indicatorColor
        // 当前指示tab位置
        val currentTab = container!!.getChildAt(currentPosition)
        // 当前tab的左边相对父容器的左边距
        var leftPadding = currentTab.left.toFloat()
        // 当前tab的右边相对于父容器左边距
        var rightPadding = currentTab.right.toFloat()
        val tempPadding = 20f
        // 如果出现位移

        var centerPosition = 0.0f

        if (currentPositionOffset >= 0f && currentPosition < tabCount - 1) {
            val nextTab = container!!.getChildAt(currentPosition + 1)
            val nextTabLeft = nextTab.left.toFloat()
            val nextTabRight = nextTab.right.toFloat()
            // 位置是在滑动过程中不断变化的
            leftPadding = currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * leftPadding
            rightPadding = currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * rightPadding
        }
        centerPosition = (rightPadding - leftPadding) / 2 + leftPadding
        val left = (centerPosition - tempPadding - formatPercent(currentPositionOffset) * tempPadding) - resources.getDimension(R.dimen.w16)
        val right = (centerPosition + tempPadding + formatPercent(currentPositionOffset) * tempPadding) + resources.getDimension(R.dimen.w16)

        // 绘制
        //        canvas.drawRect(left, height - indicatorHeight, right, height, paint);
        mBrounds = RectF()
        mBrounds?.set(left, (height - indicatorHeight).toFloat(), right, height.toFloat())
        canvas.drawRoundRect(mBrounds!!, resources.getDimension(R.dimen.w45), resources.getDimension(R.dimen.w45), paint!!)
    }

    /**
     * 设置ViewPager
     */
    fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        if (viewPager.adapter == null) {
            throw IllegalStateException(
                    "ViewPager does not has a adapter instance")
        } else {
            pagerAdapter = viewPager.adapter
        }
        viewPager.addOnPageChangeListener(pagerStateChangeListener)
        update()
    }

    /**
     * 更新界面
     */
    private fun update() {
        // 指示器容器移除所有子view
        container!!.removeAllViews()
        // 获取指示器个数
        tabCount = pagerAdapter!!.count
        // 逐个添加指示器
        for (i in 0 until tabCount) {
            addTab(i, pagerAdapter!!.getPageTitle(i))
        }
        // 更新Tab样式
        updateTabStyle()
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                    } else {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }

                    currentPosition = viewPager!!.currentItem
                    scrollToChild(currentPosition, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    /**
     * 滑动ScrollView
     */
    private fun scrollToChild(position: Int, offset: Int) {
        if (tabCount == 0) {
            return
        }
        val newScrollX = container!!.getChildAt(position).left + offset
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX
            scrollTo(newScrollX, 0)
        }
    }

    /**
     * 添加指示器
     */
    private fun addTab(position: Int, title: CharSequence) {
        val tvTab = TextView(context)
        tvTab.text = title
        tvTab.setTextColor(textColor)
        tvTab.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        tvTab.gravity = Gravity.CENTER
        tvTab.setSingleLine()
        tvTab.isFocusable = true
        tvTab.setOnClickListener { viewPager?.currentItem = position }
        tvTab.setPadding(indicatorMargin, 0, indicatorMargin, 0)
        container!!.addView(tvTab, position, expandedTabLayoutParams)
    }

    /**
     * 更新指示器样式
     */
    private fun updateTabStyle() {
        for (i in 0 until tabCount) {
            val tab = container!!.getChildAt(i) as TextView
            if (i == selectedPosition) {
                // 设置选中的指示器文字颜色和大小
                tab.setTextColor(indicatorColor)
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize)
            } else {
                tab.setTextColor(textColor)
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
        }
    }

    /**
     * 对偏移百分比进行格式化 保证是一个小到大 然后到小的一个驼峰过程
     * 这样才能保证滚动指示器是一个长度在随着滚动偏移不断变化的样子
     */
    private fun formatPercent(percent: Float): Float {

        val adsP = Math.abs(percent - 0.5f)

        return Math.abs(0.5f - adsP)
    }

    /**
     * viewPager状态改变监听
     */
    private inner class PagerStateChangeListener : OnPageChangeListener {

        /**
         * viewpager状态监听
         */
        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {  // 0 空闲状态  pager处于空闲状态
                scrollToChild(viewPager!!.currentItem, 0)
            }
            //            else if (state == ViewPager.SCROLL_STATE_SETTLING) {
            //                // 2 正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
            //            } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            //                // 1 viewpager正在被滑动,处于正在拖拽中
            //            }
        }

        /**
         * viewpager正在滑动，会回调一些偏移量
         * 滚动时，只要处理指示器下方横线的滚动
         *
         * @param position             当前页面
         * @param positionOffset       当前页面偏移的百分比
         * @param positionOffsetPixels 当前页面偏移的像素值
         */
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            currentPosition = position
            currentPositionOffset = positionOffset
            // 处理指示器下方横线的滚动,scrollToChild会不断调用ondraw方法，绘制在重绘下划线，这就是移动动画效果
            scrollToChild(position, (positionOffset * container!!.getChildAt(position).width).toInt())
            invalidate()
        }

        /**
         * page滚动结束
         *
         * @param position 滚动结束后选中的页面
         */
        override fun onPageSelected(position: Int) {
            // 滚动结束后的未知
            selectedPosition = position
            // 更新指示器状态
            updateTabStyle()
        }

    }
}
