package com.cocosh.shmstore.title

import android.content.Intent
import android.view.View
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.CityListActivity
import com.cocosh.shmstore.home.HomeActivity
import com.cocosh.shmstore.home.HomeFragment
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.model.Location
import com.cocosh.shmstore.model.ValueByKey
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.zxing.QrCodeActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_home_title.view.*

/**
 * 首页标题栏
 * Created by zhangye on 2018/4/18.
 */
class HomeTitleFragment : BaseFragment(), LocationUtil.LocationListener {

    var homeFragment: HomeFragment? = null

    override fun reTryGetData() {

    }

    private var locationUtil = LocationUtil()

    override fun onLocationChanged(location: Location?) {
        location?.let {
            if (location.city.isEmpty()) {
                it.city = "未知"
            } else {
                updateLocation(it) //上传经纬度
            }
            updateData(it)
        }
    }


    override fun setLayout(): Int = R.layout.layout_home_title

    override fun initView() {
        locationUtil.getLoaction(activity, this) //开启定位
        getLayoutView().vScan.setOnClickListener(this)
        getLayoutView().vShare.setOnClickListener(this)
//        getLayoutView().vLocal.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            getLayoutView().vScan.id -> {
                startCamera() //判断权限启动扫描二维码页面
            }
            getLayoutView().vShare.id -> {
                share()
            }
            getLayoutView().vLocal.id -> {
                startActivityForResult(Intent(activity, CityListActivity::class.java), IntentCode.LOCATION)
            }
        }
    }

    override fun close() {

    }

    private fun startCamera() {
        if (activity is HomeActivity) {
            if ((activity as HomeActivity).permissionUtil.cameraPermission()) {
                startActivity(Intent(context, QrCodeActivity::class.java))
            }
        }
    }

    private fun updateData(location: Location?) {
        location?.let {
            getLayoutView().tvCity.text = location.district
            if (activity is HomeActivity) {
                (activity as HomeActivity).homeFragment.homeBottomView.loadData(location) //更新底部数据
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == IntentCode.LOCATION) {
//            data?.let {
//                val city = it.getStringExtra("city")
//                updateData(city)
//
//                if (city == "未知") {
//                    return
//                }
//                city?.let {
//                    locationUtil.getLatlon(activity, it, object : LocationUtil.ILatLonResult {
//                        override fun onLatLonResult() {
//                            SmApplication.getApp().getData<Location>(DataCode.LOCATION, false)?.let {
//                                updateLocation(it) //更新用户定位数据
//                            }
//                        }
//                    }) //更新经纬度信息
//                }
//
//            }
//        }
//    }

    //更新城市区域
    private fun updateLocation(location: Location) {
        if (!UserManager.isLogin()) {
            return
        }
        val params = HashMap<String, String>()
        params["locationName"] = location.city
        params["countyName"] = location.district
        params["lat"] = location.latitude
        params["lng"] = location.longitude
        ApiManager.post(activity as BaseActivity, params, Constant.USER_LOCATION, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {

            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<String>) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        //更新切换用户城市区域
        SmApplication.getApp().getData<Boolean>(DataCode.CHANGE_USER, true)?.let {
            UserManager.loadMemberEntrance(getBaseActivity()) //加载用户资料
            SmApplication.getApp().getData<Location>(DataCode.LOCATION, false)?.let {
                updateLocation(it)
            }
            homeFragment?.loadBanner()
        }
    }

    private fun share() {
        val params = HashMap<String, String>()
        params["dictionaryKey"] = "index_share_url"
        ApiManager.get(0, activity as BaseActivity, params, Constant.GET_SHARE_URL, object : ApiManager.OnResult<BaseModel<ValueByKey>>() {
            override fun onSuccess(data: BaseModel<ValueByKey>) {
                if (data.success) {
                    (activity as HomeActivity).showShareDialg(data.entity?.dictionaryValue ?: "")
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<ValueByKey>) {
            }

        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            return
        }
        if (SmApplication.getApp().getData<Location>(DataCode.LOCATION, false) == null) {
            locationUtil.getLoaction(activity, this) //开启定位
        }
    }
}