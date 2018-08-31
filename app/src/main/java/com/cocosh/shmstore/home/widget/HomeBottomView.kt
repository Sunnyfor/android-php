package com.cocosh.shmstore.home.widget

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.*
import com.cocosh.shmstore.home.model.HomeBottom
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.model.Location
import kotlinx.android.synthetic.main.activity_contact_service.view.*
import kotlinx.android.synthetic.main.layout_home_bottom.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


/**
 *
 * Created by zhangye on 2018/4/18.
 */
class HomeBottomView : RelativeLayout {
    private var weatherFragment = WeatherFragment()
    private var bonusmoneyFragment = BonusMoneyFragment()
    private var povertyFragment = PovertyFragment()
    private var photoFragment1: PhotoFragment? = null
    private var photoFragment2: PhotoFragment? = null
    private val fragments = arrayListOf<Fragment>()
    private val pointList = arrayListOf<View>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var fragmentManager: FragmentManager
    var currentPosition = 0

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
        LayoutInflater.from(context).inflate(R.layout.layout_home_bottom, this, true)
        fragments.add(bonusmoneyFragment)
        fragments.add(weatherFragment)

//       initPhotoFragment()

        viewPager.offscreenPageLimit = fragments.size
        viewPager.adapter = PageAdapter(fragments, fragmentManager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {


            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                pointList[currentPosition].setBackgroundResource(R.color.lineGray)
                pointList[position].setBackgroundResource(R.color.red)
                currentPosition = position
            }
        })


        initPoint()
    }


    class PageAdapter(val fragment: ArrayList<Fragment>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = fragment[position]

        override fun getCount(): Int = fragment.size

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        }

        override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE
    }


    fun selectPage(index: Int) {
        viewPager.currentItem = index % fragments.size
    }


    /**
     * 加载数据
     */
    fun loadData(location: Location) {
        val params = hashMapOf<String, String>()
        params["city"] = location.city
        ApiManager2.get(0, context as BaseActivity, params, Constant.HOME_BOTTOM, object : ApiManager2.OnResult<BaseBean<HomeBottom>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<HomeBottom>) {
                data.message?.let {
                    launch(UI) {
                        //                            initPhotoFragment()
                        bonusmoneyFragment.loadData(it.redPacketAllAmount)
                        weatherFragment.loadData(it.weather)
                    }

//                        if (it.resLightChinaVO != null){
//                            if(it.resLightChinaVO?.lightChinaStatus != "3"){
//                                if (!fragments.contains(povertyFragment)){
//                                    fragments.add(povertyFragment)
//                                    viewPager.adapter.notifyDataSetChanged()
//                                    povertyFragment.loadData(it.resLightChinaVO)
//                                    initPoint()
//                                }else{
//                                    povertyFragment.loadData(it.resLightChinaVO)
//                                }
//                            }else{
//                                if (fragments.contains(povertyFragment)){
//                                    fragments.remove(povertyFragment)
//                                    viewPager.adapter.notifyDataSetChanged()
//                                    initPoint()
//                                }
//                            }
//                        }
                }
            }


            override fun onCatch(data: BaseBean<HomeBottom>) {
            }

        })
    }

    private fun initPoint() {
        currentPosition = 0
        llayoutPoint.removeAllViews()
        pointList.clear()
        val pointLayoutParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen.w25).toInt(), resources.getDimension(R.dimen.h6).toInt())
        pointLayoutParams.leftMargin = resources.getDimension(R.dimen.w10).toInt()

        for (i in 0 until fragments.size) {
            val view = View(context)
            view.alpha = 0.8f
            view.setBackgroundResource(R.color.lineGray)
            llayoutPoint.addView(view, pointLayoutParams)
            pointList.add(view)
        }

        pointList[0].setBackgroundResource(R.color.red)
    }

    private fun initPhotoFragment() {
        photoFragment1 = PhotoFragment()
        photoFragment1?.res = R.drawable.bg_default_id_front
        photoFragment1?.onClickListener = OnClickListener {
            val intent = Intent(context, BonusListActivity::class.java)
            intent.putExtra("title", "消费扶贫")
            context.startActivity(intent)
        }
        photoFragment2 = PhotoFragment()
        photoFragment2?.res = R.drawable.bg_default_id_back
        photoFragment2?.onClickListener = OnClickListener {
            val intent = Intent(context, BonusListActivity::class.java)
            intent.putExtra("title", "媒体扶贫")

            context.startActivity(intent)
        }
        fragments.add(photoFragment1!!)
        fragments.add(photoFragment2!!)
    }
}