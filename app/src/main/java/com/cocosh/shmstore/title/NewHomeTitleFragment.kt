package com.cocosh.shmstore.title

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.CityListActivity
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.ui.MessageActivity
import com.cocosh.shmstore.model.Location
import com.cocosh.shmstore.newhome.GoodsSearchActivity
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.LocationUtil
import com.cocosh.shmstore.utils.UserManager
import kotlinx.android.synthetic.main.layout_home_new_title.view.*

/**
 * 首页标题栏
 * Created by zhangye on 2018/4/18.
 */
class NewHomeTitleFragment : BaseFragment(), LocationUtil.LocationListener {

    override fun reTryGetData() {

    }

    private var locationUtil = LocationUtil()

    override fun onLocationChanged(location: Location?) {
        location?.let {
            if (location.city.isEmpty()) {
                it.city = "定位"
            } else {
                updateLocation(it) //上传经纬度
            }
            updateData(it)
        }
    }


    override fun setLayout(): Int = R.layout.layout_home_new_title

    override fun initView() {
        locationUtil.getLoaction(activity, this) //开启定位
        getLayoutView().llMessage.setOnClickListener(this)
        getLayoutView().llSearch.setOnClickListener(this)

    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.llMessage -> {
                startActivity(Intent(activity, MessageActivity::class.java))
            }
            R.id.vLocal -> {
                startActivityForResult(Intent(activity, CityListActivity::class.java), IntentCode.LOCATION)
            }

            R.id.llSearch -> {
                startActivity(Intent(activity, GoodsSearchActivity::class.java))

            }
        }
    }

    override fun close() {

    }



    private fun updateData(location: Location?) {
        location?.let {
            getLayoutView().tvCity.text = location.district
        }
    }


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
        SmApplication.getApp().getData<Boolean>(DataCode.CHANGE_USER, true)?.let { _ ->
            SmApplication.getApp().getData<Location>(DataCode.LOCATION, false)?.let {
                updateLocation(it)
            }
        }
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