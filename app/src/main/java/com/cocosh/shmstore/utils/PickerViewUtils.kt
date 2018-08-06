package com.cocosh.shmstore.utils

import android.support.v4.content.ContextCompat
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.cocosh.shmstore.model.City
import com.cocosh.shmstore.model.District
import com.cocosh.shmstore.model.Province
import com.cocosh.shmstore.R
import com.cocosh.shmstore.mine.model.BankModel
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.model.BankTypeModel
import com.cocosh.shmstore.model.ProvinceModel
import com.cocosh.shmstore.widget.dialog.BankListDialog
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * 选择器dialog
 * Created by zhangye on 2018/4/11.
 */
class PickerViewUtils(val activity: BaseActivity) {

    private var addressOptionsPickerView: OptionsPickerView<ProvinceModel>? = null
    private var bankOptionsPickerView: OptionsPickerView<String>? = null
    private var timePickerView: TimePickerView? = null
    private var disposable: Disposable? = null


    //显示年月
    fun showDateYYMM(onPickerViewResultListener: OnPickerViewResultListener) {
        showDate("yyyy-MM", booleanArrayOf(true, true, false, false, false, false), onPickerViewResultListener)
    }

    //显示年月日
    fun showDateYYMMDD(onPickerViewResultListener: OnPickerViewResultListener) {
        showDate("yyyy-MM-dd", booleanArrayOf(true, true, true, false, false, false), onPickerViewResultListener)
    }


    //日期选择器
    fun showDate(type: String, array: BooleanArray, onPickerViewResultListener: OnPickerViewResultListener) {

        if (timePickerView != null) {
            timePickerView?.show()
            return
        }

        val startCalendar = Calendar.getInstance()
        startCalendar.set(1897, 0, 1)

        timePickerView = TimePickerBuilder(activity, OnTimeSelectListener { date, _ ->
            val dateFormat = SimpleDateFormat(type, Locale.getDefault())
            onPickerViewResultListener.onPickerViewResult(dateFormat.format(date))
        })
                .setContentTextSize(22)
                .setRangDate(startCalendar, Calendar.getInstance())
                .setType(array)
                .setCancelColor(ContextCompat.getColor(activity, R.color.blackText))
                .setSubmitColor(ContextCompat.getColor(activity, R.color.blackText))
                .build()
        timePickerView?.show()
    }

    fun showReleaseTime(onPickerViewResultListener: OnPickerViewResultListener) {
        if (timePickerView != null) {
            timePickerView?.show()
            return
        }

        val startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.DATE, startCalendar.get(Calendar.DATE))

        timePickerView = TimePickerBuilder(activity, OnTimeSelectListener { date, _ ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            onPickerViewResultListener.onPickerViewResult(dateFormat.format(date))
        })
                .setContentTextSize(22)
                .setRangDate(startCalendar, null)
                .setType(booleanArrayOf(true, true, true, false, false, false))
                .setCancelColor(ContextCompat.getColor(activity, R.color.blackText))
                .setSubmitColor(ContextCompat.getColor(activity, R.color.blackText))
                .build()
        timePickerView?.show()
    }


    //性别选择器
    fun showSex(onPickerViewResultListener: OnPickerViewResultListener) {
        val bottomPhotoDialog = BottomPhotoDialog(activity)
        val sexs = arrayOf("男", "女")
        bottomPhotoDialog.setValue(sexs[0], sexs[1])
        bottomPhotoDialog.setOnItemClickListener(object : BottomPhotoDialog.OnItemClickListener {
            override fun onTopClick() {
                onPickerViewResultListener.onPickerViewResult(sexs[0])
            }

            override fun onBottomClick() {
                onPickerViewResultListener.onPickerViewResult(sexs[1])
            }

        })
        bottomPhotoDialog.show()
    }


    fun showAddress(addressresultlistener: OnAddressResultListener) {
        showAddress(addressresultlistener, false)
    }

    //地址选择器
    fun showAddress(addressresultlistener: OnAddressResultListener, isDouble: Boolean) {

        if (addressOptionsPickerView != null) {
            addressOptionsPickerView?.show()
            return
        }

        activity.showLoading()

        val options1Items = ArrayList<ProvinceModel>()
        val options2Items = ArrayList<ArrayList<ProvinceModel>>()
        val options3Items = ArrayList<ArrayList<ArrayList<ProvinceModel>>>()


        disposable = Observable.create<String> {
            parseAddressData(options1Items, options2Items, options3Items)
//            options1Items = parseAddressData()
//            options1Items.forEach {
//                val cityList = it.childrens
//                val provinceAreaList = ArrayList<ArrayList<ProvinceModel>>()//该省的所有地区列表（第三极）
//                it.childrens?.forEach {
//                    it.childrens?.let {
//                        provinceAreaList.add(it)
//                    }
//                }
//                cityList?.let {
//                    options2Items.add(it)
//                }
//
//                if (!isDouble) {
//                    options3Items.add(provinceAreaList)
//                }
//            }

            it.onNext("ok")
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    addressOptionsPickerView = OptionsPickerBuilder(activity, OnOptionsSelectListener { options1, option2, options3, _ ->
                        //返回的分别是三个级别的选中位置
                        val addressText = StringBuilder(options1Items[options1].name)
                        val city = options2Items[options1][option2].name

                        if (city.isNotEmpty()) {
                            if (isDouble) {
                                if (city != addressText.toString()) {
                                    addressText.append("-")
                                    addressText.append(city)
                                }
                            } else {
                                addressText.append("-")
                                addressText.append(city)
                            }
                        }
                        if (isDouble) {
                            addressresultlistener.onPickerViewResult(addressText.toString(), options2Items[options1][option2].id)
                        } else {
                            val area = options3Items[options1][option2][options3].name
                            if (area.isNotEmpty()) {
                                addressText.append("-")
                                addressText.append(area)
                            }
                            addressresultlistener.onPickerViewResult(addressText.toString(), options3Items[options1][option2][options3].id)
                        }

                    }).setSubmitColor(ContextCompat.getColor(activity, R.color.blackText))//确定按钮文字颜色
                            .setCancelColor(ContextCompat.getColor(activity, R.color.blackText))//取消按钮文字颜色
                            .build()
//                    if (isDouble) {
//                        addressOptionsPickerView?.setPicker(options1Items, options2Items as List<ArrayList<ProvinceModel>>?, null)
//                    } else {
                    addressOptionsPickerView?.setPicker(options1Items, options2Items as List<ArrayList<ProvinceModel>>?, options3Items as List<ArrayList<ArrayList<ProvinceModel>>>?)
//                    }
                    addressOptionsPickerView?.show()
                    activity.hideLoading()
                    disposable?.dispose() //销毁
                }
    }


    //地址选择器
    fun showSendBonusAddress(addressresultlistener: OnAddressResultListener) {

        if (addressOptionsPickerView != null) {
            addressOptionsPickerView?.show()
            return
        }

        activity.showLoading()

        val options1Items = ArrayList<ProvinceModel>()
        val options2Items = ArrayList<ArrayList<ProvinceModel>>()
        val options3Items = ArrayList<ArrayList<ArrayList<ProvinceModel>>>()

        disposable = Observable.create<String> {
            //            options1Items = parseAddressData(false)
//
//            val allChilddList = arrayListOf<ProvinceModel>()
//            allChilddList.add(ProvinceModel("", "000000", null))
//
//            val allCitydList = arrayListOf<ProvinceModel>()
//            allCitydList.add(ProvinceModel("", "000000", allChilddList))
//
//            options1Items.add(0, ProvinceModel("全国", "000000", allCitydList))
//
//            options1Items.forEach {
//                val cityList = it.childrens
//
//                if (it.name != "全国") {
//                    val childdList = arrayListOf<ProvinceModel>()
//                    childdList.add(ProvinceModel("", it.id, null))
//                    cityList?.add(0, ProvinceModel("全部", it.id, childdList))
//                }
//
//                val provinceAreaList = ArrayList<ArrayList<ProvinceModel>>()//该省的所有地区列表（第三极）
//                cityList?.forEach {
//                    if (it.name != "全部" && it.name != "" && !it.name.contains("北京") && !it.name.contains("天津") && !it.name.contains("上海") && !it.name.contains("重庆")) {
//                        val childdList = arrayListOf<ProvinceModel>()
//                        childdList.add(ProvinceModel("", it.id, null))
//                        it.childrens?.add(0, ProvinceModel("全部", it.id, childdList))
//                    }
//                    it.childrens?.let {
//                        provinceAreaList.add(it)
//                    }
//                }
//
//                cityList?.let {
//                    options2Items.add(it)
//                }
//
//
//                options3Items.add(provinceAreaList)
//            }
//            parseAddressData()
            it.onNext("ok")
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    addressOptionsPickerView = OptionsPickerBuilder(activity, OnOptionsSelectListener { options1, option2, options3, _ ->
                        //返回的分别是三个级别的选中位置
                        val addressText = StringBuilder(options1Items[options1].name)
                        val city = options2Items[options1][option2].name

                        if (city.isNotEmpty()) {
                            addressText.append("-")
                            addressText.append(city)
                        }

                        val area = options3Items[options1][option2][options3].name
                        if (area.isNotEmpty()) {
                            addressText.append("-")
                            addressText.append(area)
                        }
                        addressresultlistener.onPickerViewResult(addressText.toString(), options3Items[options1][option2][options3].id)


                    }).setSubmitColor(ContextCompat.getColor(activity, R.color.blackText))//确定按钮文字颜色
                            .setCancelColor(ContextCompat.getColor(activity, R.color.blackText))//取消按钮文字颜色
                            .build()

                    addressOptionsPickerView?.setPicker(options1Items, options2Items as List<ArrayList<ProvinceModel>>?, options3Items as List<ArrayList<ArrayList<ProvinceModel>>>?)

                    addressOptionsPickerView?.show()
                    activity.hideLoading()
                    disposable?.dispose() //销毁
                }
    }


    //银行卡类型选择器
    fun showBankType(onResultListener: OnResultListener, datas: List<BankTypeModel>) {

        if (bankOptionsPickerView != null) {
            bankOptionsPickerView?.show()
            return
        }
        val options1Items = ArrayList<String>()
        datas.forEach {
            options1Items.add(it.bankName ?: "")
        }


        bankOptionsPickerView = OptionsPickerBuilder(activity, OnOptionsSelectListener { options1, option2, options3, _ ->
            //返回的分别是三个级别的选中位置
//            val addressText = StringBuilder(options1Items[options1])
            onResultListener.onResult(datas[options1])
        }).setSubmitColor(ContextCompat.getColor(activity, R.color.blackText))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(activity, R.color.blackText))//取消按钮文字颜色
                .build()
        bankOptionsPickerView?.setNPicker(options1Items, null, null)
        bankOptionsPickerView?.show()
    }

    //回调结果
    interface OnPickerViewResultListener {
        fun onPickerViewResult(value: String)
    }

    //地址回调结果
    interface OnAddressResultListener {
        fun onPickerViewResult(address: String, code: String)
    }


    //回调结果
    interface OnResultListener {
        fun onResult(data: BankTypeModel)
    }


    //解析地区数据
    private fun parseAddressData(options1Items: ArrayList<ProvinceModel>, options2Items: ArrayList<ArrayList<ProvinceModel>>,
                                 options3Items: ArrayList<ArrayList<ArrayList<ProvinceModel>>>) {

        val provinceJson = GetJsonDataUtil.getJson("province.json")
        val cityJson = GetJsonDataUtil.getJson("city.json")
        val districtJson = GetJsonDataUtil.getJson("district.json")

        val provinces = Gson().fromJson<ArrayList<Province>>(provinceJson, object : TypeToken<java.util.ArrayList<Province>>() {}.type)

        val citys = Gson().fromJson<ArrayList<City>>(cityJson, object : TypeToken<java.util.ArrayList<City>>() {}.type)

        val districts = Gson().fromJson<ArrayList<District>>(districtJson, object : TypeToken<java.util.ArrayList<District>>() {}.type)

        provinces.forEach {
            val provinceId = it.provinceID

            options1Items.add(ProvinceModel(it.provinceName, it.divisionCode))

            val cityList = ArrayList<ProvinceModel>()
            val districtList = ArrayList<ArrayList<ProvinceModel>>()

            citys.filter { it.provinceID == provinceId }.forEach {
                val cityID = it.cityID
                cityList.add(ProvinceModel(it.cityName, it.divisionCode))

                val district = ArrayList<ProvinceModel>()

                districts.filter { it.cityID == cityID }.forEach {
                    district.add(ProvinceModel(it.districtName, it.divisionCode))
                }
                districtList.add(district)
            }

            options2Items.add(cityList)
            options3Items.add(districtList)
        }
    }

    //银行卡选择器
    fun showBankList(list: ArrayList<BankModel>, onItemClickListener: BankListDialog.OnItemClickListener) {
        val bottomPhotoDialog = BankListDialog(activity, list)
        bottomPhotoDialog.setOnItemClickListener(onItemClickListener)
        bottomPhotoDialog.show()
    }
}