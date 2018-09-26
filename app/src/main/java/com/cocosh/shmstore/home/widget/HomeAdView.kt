package com.cocosh.shmstore.home.widget

import android.content.Context
import android.content.Intent
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Scroller
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.home.BonusWebActivity
import com.cocosh.shmstore.home.adapter.HomeBannerAdapter
import com.cocosh.shmstore.home.model.Banner
import com.cocosh.shmstore.home.model.Bonus2
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.layout_home_ad.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


/**
 * 首页广告View
 * Created by zhangye on 2018/4/18.
 */
class HomeAdView : LinearLayout {
    private var delay = 3 * 1000
    private var initThred = false
    private var job: Job? = null
    private var pointList = arrayListOf<View>()
    private var adapter: HomeBannerAdapter? = null
    private var currentIndex = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val pointLayoutParams: LayoutParams

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_home_ad, this, true)
        pointLayoutParams = LayoutParams(resources.getDimension(R.dimen.w24).toInt(), resources.getDimension(R.dimen.w24).toInt())
        pointLayoutParams.leftMargin = resources.getDimension(R.dimen.w20).toInt()

        viewPagerAd.pageMargin = context.resources.getDimension(R.dimen.w25).toInt()
        viewPagerAd.setPageTransformer(false) { page, position ->
            val minScale = 0.9f
            if (position < -1 || position > 1) {
                page.scaleY = minScale
            } else if (position <= 1) { // [-1,1]
                if (position < 0) {
                    val scaleX = 1 + 0.1f * position
                    page.scaleY = scaleX
                } else {
                    val scaleX = 1 - 0.1f * position
                    page.scaleY = scaleX
                }
            }
        }


        viewPagerAd.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            var currentPosition = 0
            override fun onPageScrollStateChanged(state: Int) {
                if (currentPosition >= viewPagerAd.offscreenPageLimit - 2) {
                    viewPagerAd.setCurrentItem(2, false)
                }
                if (currentPosition <= 1) {
                    viewPagerAd.setCurrentItem(viewPagerAd.offscreenPageLimit - 3, false)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                tv_name.text = viewPagerAd.adapter.getPageTitle(position)
                pointList[currentIndex].setBackgroundResource(R.drawable.shape_banner_point_bg_disable)

                currentIndex = currentPosition - 2

                if (currentIndex > pointList.size - 1) {
                    currentIndex = 0
                }

                if (currentIndex < 0) {
                    currentIndex = pointList.size - 1
                }
                pointList[currentIndex].setBackgroundResource(R.drawable.shape_banner_point_bg_enable)
            }
        })

        viewPagerAd.setOnTouchListener({ _: View, motionEvent: MotionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    stopLoop()
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    startLooep()
                }

            }

            return@setOnTouchListener false
        })

    }

    fun loadData(list: ArrayList<Bonus2>) {
        stopLoop()
        currentIndex = 0
        pointList.clear()
        llayoutPoint.removeAllViews()

        if (list.isEmpty()) {
            tv_name.text = resources.getString(R.string.app_name)
            if (adapter != null) {
                adapter = HomeBannerAdapter(context, list)
                viewPagerAd.adapter = adapter
            }
            return
        }

        for (i in 0 until list.size) {
            val view = View(context)
            view.alpha = 0.8f
            view.setBackgroundResource(R.drawable.shape_banner_point_bg_disable)
            llayoutPoint.addView(view, pointLayoutParams)
            pointList.add(view)
        }

        pointList[0].setBackgroundResource(R.drawable.shape_banner_point_bg_enable)

        viewPagerAd.offscreenPageLimit = list.size + 4
        adapter = HomeBannerAdapter(context, list)
        adapter?.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {

                if (!UserManager2.isLogin()) {
                    SmediaDialog(context).showLogin()
                    return
                }

                list[index].let {
                    val intent = Intent(context, BonusWebActivity::class.java)
                    intent.putExtra("title",it.name)
                    intent.putExtra("no", it.no)
                    context.startActivity(intent)

                    //已抢
//                    when {
//                        it.draw == "1" -> {
////                            intentWeb(it,"RECEIVE")
//                        }
//                        it.draw == "" -> {
////                            intentWeb(it,"NONE")
//                        }
////                        else -> //开始抢红包
////                            hitBonus(it)
//                    }
                }
            }

        }
        viewPagerAd.adapter = adapter
        viewPagerAd.currentItem = 2
        setViewPagerScroller()
        startLooep()
    }


    fun startLooep() {

        job?.cancel()

        job = launch(CommonPool) {
            while (true) {
                delay(delay)

                val index = when {
                    viewPagerAd.currentItem >= viewPagerAd.offscreenPageLimit - 2 -> 2
                    viewPagerAd.currentItem <= 1 -> viewPagerAd.offscreenPageLimit - 3
                    else -> viewPagerAd.currentItem + 1
                }
                launch(UI) {
                    viewPagerAd.setCurrentItem(index, true)

                }

            }
        }
    }

    fun stopLoop() {
        job?.cancel()
    }


    private fun setViewPagerScroller() = try {
        val scrollerField = viewPagerAd::class.java.getDeclaredField("mScroller")
        scrollerField.isAccessible = true
        val interpolator = viewPagerAd::class.java.getDeclaredField("sInterpolator")
        interpolator.isAccessible = true

        val scroller = object : Scroller(context) {
            override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
                super.startScroll(startX, startY, dx, dy, 500)    // 这里是关键，将duration变长或变短
            }
        }
        scrollerField.set(viewPagerAd, scroller)
    } catch (e: NoSuchFieldException) {
        // Do nothing.
    } catch (e: IllegalAccessException) {
        // Do nothing.
    }


    /**
     * 抢红包（占位）
     */
    fun hitBonus(bannerData: Banner.Data) {

        if (bannerData.typeInfo != "1" && bannerData.typeInfo != "2" && bannerData.typeInfo != "3") {
            intentWeb(bannerData, "RECEIVE")
            return
        }

        val params = hashMapOf<String, String>()
        bannerData.redPacketOrderId.let {
            params["redPacketOrderId"] = it.toString()
        }
        ApiManager.post(context as BaseActivity, params, Constant.BONUS_HIT, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                if (data.success) {
//                    抢红包 RECEIVE("已领取"),SEIZE("已占位"),NONE("已抢光")
                    intentWeb(bannerData, data.entity ?: "NONE")
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<String>) {

            }
        })
    }


    fun intentWeb(it: Banner.Data, state: String) {
        val intentWeb = Intent(context, BonusWebActivity::class.java)
        intentWeb.putExtra("comment_id", it.redPacketOrderId.toString())
        intentWeb.putExtra("title", it.redPacketName)
        intentWeb.putExtra("htmUrl", it.advertisementUrl)
        intentWeb.putExtra("downUrl", it.androidUrl)
        intentWeb.putExtra("state", state)
        intentWeb.putExtra("typeInfo", it.typeInfo)
        intentWeb.putExtra("companyLogo", it.companyLogo)
        intentWeb.putExtra("companyName", it.companyName)
        intentWeb.putExtra("advertisementBaseType", it.advertisementBaseType)
        context.startActivity(intentWeb)
    }
}