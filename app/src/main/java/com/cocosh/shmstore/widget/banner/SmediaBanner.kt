package com.cocosh.shmstore.widget.banner

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.support.annotation.NonNull
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.utils.DensityUtil


/**
 * Created by lmg on 2018/4/24.
 */
class SmediaBanner : FrameLayout, ViewPager.OnPageChangeListener {
    /**
     * 每个广告条目的图片地址
     */
    private var imageUrlList: List<String>? = null
    /**
     * 用来盛放广告条目的
     */
    private var mViewPager: ViewPager? = null
    /**
     * 底部小圆点整个布局
     */
    private var mPointLayout: LinearLayout? = null
    /**
     * 小圆点上一次的位置
     */
    private var lastPosition: Int = 0
    /**
     * 底部小圆点默认大小
     */
    private var POINT_DEFAULT_SIZE = 10
    private var POINT_DEFAULT_PADDING = 10
    private var POINT_DEFAULT_MAGINBOTTOM = 10
    /**
     * 切换广告的时长  单位：ms
     */
    private val BANNER_SWITCH_DELAY_MILLIS = 3000
    /**
     * 用户是否正在触摸banner
     */
    private var isTouched = false
    private val mHandler = PollingHandler()
    /**
     * banner点击事件监听器
     */
    private var listener: OnItemClickListener? = null

    private class PollingHandler : Handler()

    /**
     * 开启轮询?
     */
    private var pollingEnable = false

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.sBanner)
        POINT_DEFAULT_SIZE = typedArray.getDimension(R.styleable.sBanner_pointSize, context.resources.getDimension(R.dimen.h15)).toInt()
        POINT_DEFAULT_PADDING = typedArray.getDimension(R.styleable.sBanner_pointPadding, context.resources.getDimension(R.dimen.h20)).toInt()
        POINT_DEFAULT_MAGINBOTTOM = typedArray.getDimension(R.styleable.sBanner_pointMaginBoootom, context.resources.getDimension(R.dimen.h30)).toInt()
        typedArray.recycle()
        initView()
    }

    /**
     * 初始化View
     */
    private fun initView() {
        //加载布局   子View个数==0  则还没有加载布局
        if (childCount == 0) {
            View.inflate(context, R.layout.layout_banner, this)
            mViewPager = findViewById(R.id.vp_banner) as ViewPager
            mPointLayout = findViewById(R.id.ll_banner_point) as LinearLayout
            mPointLayout?.bottom = POINT_DEFAULT_MAGINBOTTOM
        }
    }

    /**
     * 初始化banner
     * @param imageUrlList 每个广告条目的图片地址
     */
    fun initBanner(@NonNull imageUrlList: List<String>?) {
        this.imageUrlList = imageUrlList
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            throw IllegalArgumentException("传入图片地址不能为空")
        }
        initView()
        initData()
    }

    private lateinit var bannerAdapter: BannerAdapter
    /**
     * 初始化数据
     */
    private fun initData() {
        var pointView: View
        val bannerSize = imageUrlList!!.size
        for (i in 0 until bannerSize) {
            //底部的小白点
            pointView = View(context)
            //设置背景
            pointView.setBackgroundResource(R.drawable.selector_banner_point)
            //设置小圆点的大小
            val layoutParams = LinearLayout.LayoutParams(POINT_DEFAULT_SIZE, POINT_DEFAULT_SIZE)

            //除第一个以外，其他小白点都需要设置左边距
            if (i != 0) {
                layoutParams.leftMargin = POINT_DEFAULT_PADDING
                pointView.isEnabled = false //默认小白点是不可用的
            } else {
                pointView.isEnabled = true
            }

            pointView.layoutParams = layoutParams
            mPointLayout!!.addView(pointView)  //添加到linearLayout中
        }

        bannerAdapter = BannerAdapter(context, imageUrlList as ArrayList<String>)
        mViewPager!!.adapter = bannerAdapter
        //页面切换监听器
        mViewPager!!.addOnPageChangeListener(this)

        mViewPager!!.currentItem = 1
        //指示器默认为第一个
        mPointLayout!!.getChildAt(0).isEnabled = true
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (position == 0 && positionOffsetPixels == 0)
            mViewPager?.setCurrentItem(bannerAdapter.count - 2, false);
        else if (position == bannerAdapter.count - 1 && positionOffsetPixels == 0)
            mViewPager?.setCurrentItem(1, false);

    }

    private var currentPosition = 0
    override fun onPageSelected(position: Int) {
        currentPosition = when (position) {
            bannerAdapter.count - 1 -> 1// 设置当前值为1
            0 -> bannerAdapter.count - 2// 如果索引值为0了,就设置索引值为倒数第二个
            else -> position
        }
        if (lastPosition - 1 > -1) {
            mPointLayout!!.getChildAt(lastPosition - 1).isEnabled = false
        }
        //当页面切换时，将底部白点的背景颜色换掉
        mPointLayout!!.getChildAt(currentPosition - 1).isEnabled = true
        lastPosition = currentPosition
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> isTouched = true   //正在触摸  按下
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> isTouched = false
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 延时的任务
     */
    var delayRunnable: Runnable = object : Runnable {
        override fun run() {
            //用户在触摸时不能进行自动滑动
            if (!isTouched) {
                //ViewPager设置为下一项
                mViewPager!!.currentItem = mViewPager!!.currentItem + 1
            }
            if (pollingEnable) {
                //继续延迟切换广告
                mHandler.postDelayed(this, BANNER_SWITCH_DELAY_MILLIS.toLong())
            }
        }
    }

    /**
     * banner中ViewPager的adapter
     */
    private inner class BannerAdapter(var context: Context, var list: ArrayList<String>) : PagerAdapter() {
        init {
            if (list.size >= 1) {
                list.add(0, list[list.size - 1])
                list.add(list[1])
            }
        }

        override fun getCount(): Int {
            return list.size;
        }

        /**
         * 复用判断逻辑
         */
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = View.inflate(context, R.layout.item_banner, null)
            var imageView = view.findViewById(R.id.image)
            //设置点击事件
            imageView.setOnClickListener {
                //回调
                if (listener != null) {
                    listener!!.onItemClick(position)
                }
            }
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    /**
     * 设置banner的item点击事件
     *
     * @param listener OnItemClickListener
     */
    fun setOnItemClickListener(@NonNull listener: OnItemClickListener?) {
        if (listener == null) {
            throw IllegalArgumentException("Item监听器不能为空！")
        }
        this.listener = listener
    }

    /**
     * Item点击的”监听器“
     */
    interface OnItemClickListener {
        /**
         * 点击item时的回调函数
         *
         * @param position 当前点击item的索引
         * @param title    当前点击item的标题
         */
        fun onItemClick(position: Int)
    }

    /**
     * 向外部暴露的图片加载器，外界需要通过Glide或者其他方式来进行网络加载图片
     */
    interface ImageLoader {
        /**
         * 加载图片
         *
         * @param imageView ImageView
         * @param url       图片地址
         */
        fun loadImage(imageView: ImageView, url: String)
    }

    /**
     * 获取当前轮播图是否在轮播
     * true:正在轮播  false:没有在轮播
     */
    fun isPollingEnable(): Boolean {
        return pollingEnable
    }

    /**
     * 开始轮播
     */
    fun start() {
        // 之前已经开启轮播  无需再开启
        if (pollingEnable) {
            return
        }
        pollingEnable = true
        mHandler.postDelayed(delayRunnable, BANNER_SWITCH_DELAY_MILLIS.toLong())
    }

    /**
     * 结束轮播
     */
    private fun stop() {
        pollingEnable = false
        isTouched = false
        //移除Handler Callback 和 Message 防止内存泄漏
        mHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 重新设置数据
     * @param imageUrlList 图片地址集合
     */
    fun resetData(@NonNull imageUrlList: List<String>?) {
        this.imageUrlList = imageUrlList
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            throw IllegalArgumentException("传入图片地址不能为空")
        }

        //判断是否之前在轮播
        if (pollingEnable) {
            //停止之前的轮播
            stop()
        }
        //开始新的轮播
        start()
    }
}