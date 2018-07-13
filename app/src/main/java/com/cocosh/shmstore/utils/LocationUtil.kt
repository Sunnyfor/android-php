package com.cocosh.shmstore.utils

import android.app.Activity
import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.cocosh.shmstore.application.SmApplication
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import com.amap.api.services.geocoder.GeocodeQuery
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.cocosh.shmstore.model.Location


/**
 * 高德定位
 * Created by 张野 on 2017/10/18.
 */
class LocationUtil : AMapLocationListener {
    private var mLocationClient: AMapLocationClient = AMapLocationClient(SmApplication.getApp()) //初始化mLocationClient
    private lateinit var listener: LocationListener
    private lateinit var mLocation: Location

    init {
        //初AMapLocationClientOption对象
        val mOptions = AMapLocationClientOption()
        //获取最近3s内精度最高的一次定位结果
        mOptions.isOnceLocationLatest = true
        mLocationClient.setLocationOption(mOptions)
        //
        //设置定位回调监听
        mLocationClient.setLocationListener(this)

    }

    override fun onLocationChanged(location: AMapLocation) {
        LogUtil.i("定位结果" + location.toStr())

        if (!location.city.isEmpty()){
            mLocation = Location(
                    location.latitude.toString(),
                    location.longitude.toString(),
                    location.province,
                    location.city,
                    location.address,
                    location.district,
                    location.adCode)
            SmApplication.getApp().setData(DataCode.LOCATION, mLocation)
        }

        listener.onLocationChanged(mLocation)
    }


    fun getLoaction(activity: Activity, listener: LocationListener) {

        AndPermission.with(activity).requestCode(PermissionCode.LOCATION.type)
                .rationale { requestCode, rationale ->
                    if (requestCode == PermissionCode.LOCATION.type) {
                        rationale.resume()
                    }
                }
                .permission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(object : PermissionListener {
                    override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) {
                        this@LocationUtil.listener = listener
                        //启动定位
                        mLocationClient.startLocation()

                    }

                    override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) {
                        ToastUtil.show("失败")
                    }

                }).start()
    }


    fun getLatlon(context: Context, cityName: String,iLatLonResult:ILatLonResult) {
        val geocodeSearch = GeocodeSearch(context)
        geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
            override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult, i: Int) {

            }

            override fun onGeocodeSearched(geocodeResult: GeocodeResult?, i: Int) {

                if (i == 1000) {
                    if (geocodeResult != null && geocodeResult.geocodeAddressList != null &&
                            geocodeResult.geocodeAddressList.size > 0) {
                        val geocodeAddress = geocodeResult.geocodeAddressList[0]
                        val location = Location(
                                geocodeAddress.latLonPoint.latitude.toString(),
                                geocodeAddress.latLonPoint.longitude.toString(),
                                geocodeAddress.province,
                                geocodeResult.geocodeQuery.locationName,
                                geocodeAddress.formatAddress,
                                geocodeAddress.district,
                                geocodeAddress.adcode)
                        SmApplication.getApp().setData(DataCode.LOCATION, location)
                        iLatLonResult.onLatLonResult()
                    } else {
                        ToastUtil.show("地址名出错")
                    }
                }
            }
        })

        val geocodeQuery = GeocodeQuery(cityName.trim { it <= ' ' }, "29")
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery)

    }

    interface ILatLonResult{
       fun onLatLonResult(){}
    }

    interface LocationListener{
        fun onLocationChanged(location: Location?)
    }

}