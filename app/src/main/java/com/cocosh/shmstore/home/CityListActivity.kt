package com.cocosh.shmstore.home

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.home.adapter.LocationCityAdapter
import com.cocosh.shmstore.home.model.LocationCityModel
import com.cocosh.shmstore.model.Location
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.ToastUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_city_list.*
import java.io.IOException

/**
 *
 * Created by zhangye on 2018/5/14.
 */
class CityListActivity : BaseActivity(), LocationCityAdapter.OnItemClickListener {
    private lateinit var data: ArrayList<LocationCityModel>
    private var searchData = ArrayList<LocationCityModel>()
    private var adapter: LocationCityAdapter? = null
    private var keyword = ""

    override fun setLayout(): Int = R.layout.activity_city_list

    override fun initView() {
        titleManager.defaultTitle("城市列表")
        showLoading()

        Observable.create<String> {
            val json = readJsonStringFromAsset()
            data = Gson().fromJson(json, object : TypeToken<ArrayList<LocationCityModel>>() {}.type)
            data.forEach {
                it.citys = it.citys.filter { it.type != "3" } as ArrayList<LocationCityModel.NormalCityModel>
            }
            it.onNext("ok")
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    hideLoading()
                    val location = SmApplication.getApp().getData<Location>(DataCode.LOCATION, false)
                    if (location != null) {
                        data[0].citys[0].name = location.city
                    } else {
                        data[0].citys[0].name = "未知"
                    }

                    adapter = LocationCityAdapter(this, data, false)
                    adapter?.let {
                        elv_city.setAdapter(it)
                        for (i in 0 until it.groupCount) {
                            elv_city.expandGroup(i)
                        }
                    }

                    elv_city.setOnGroupClickListener { _, _, _, _ ->
                        return@setOnGroupClickListener true
                    }

                    et_search.setOnEditorActionListener { v, actionId, event ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                            keyword = et_search.text.toString().trim()
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(et_search.windowToken, 0)
                            searchData(keyword)
                            return@setOnEditorActionListener true
                        }

                        return@setOnEditorActionListener false
                    }

                    adapter?.setOnItemClickListener(this)
                }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    private fun searchData(keyword: String) {
        searchData.clear()
        if (TextUtils.isEmpty(keyword)) {
            ToastUtil.show("关键词不能为空")
            return
        }
        val cityModels = java.util.ArrayList<LocationCityModel.NormalCityModel>()
        data
                .filter { it.firstLetter != "定位城市" && it.firstLetter != "热门城市" }
                .flatMap { it.citys }
                .filterTo(cityModels) { it.name.contains(keyword) }
        val locationCityModel = LocationCityModel(cityModels, "搜索结果")
        searchData.add(locationCityModel)
        adapter = LocationCityAdapter(this, searchData, true)
        adapter?.setOnItemClickListener(this)
        adapter?.let {
            elv_city.setAdapter(it)
            for (i in 0 until it.groupCount) {
                elv_city.expandGroup(i)
            }
        }
    }


    //读取城市JSON
    private fun readJsonStringFromAsset(): String? {
        var json: String? = null
        try {
            val input = assets.open("location_city.json")
            val size = input.available()

            val buffer = ByteArray(size)

            input.read(buffer)

            input.close()
            json = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return json
    }


    override fun onItemClick(cityName: String) {
        val intent = Intent()
        intent.putExtra("city", cityName)
        setResult(IntentCode.LOCATION, intent)
        finish()
    }
}