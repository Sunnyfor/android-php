package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.home.model.HomeBottom
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.web.WebViewActivity
import kotlinx.android.synthetic.main.fragment_poverty.view.*

/**
 * 扶贫活动Fragment
 * Created by zhangye on 2018/5/31.
 */
class PovertyFragment: BaseFragment() {
    private var url:String? = null
    private val tvList = arrayListOf<TextView>()

    override fun setLayout(): Int = R.layout.fragment_poverty

    override fun initView() {
        tvList.add(getLayoutView().tv_1)
        tvList.add(getLayoutView().tv_2)
        tvList.add(getLayoutView().tv_3)
        tvList.add(getLayoutView().tv_4)
        tvList.add(getLayoutView().tv_5)
        tvList.add(getLayoutView().tv_6)
        tvList.add(getLayoutView().tv_7)
        tvList.add(getLayoutView().tv_8)
        tvList.add(getLayoutView().tv_9)


        getLayoutView().setOnClickListener {
            val intent = Intent(activity,WebViewActivity::class.java)
            intent.putExtra("title","点亮中国")
            intent.putExtra("url",url)
            startActivity(intent)
        }
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }

    fun loadData(data:HomeBottom.ResLightChinaVO?){
        data?.let {
            this.url = it.lightChinaUrl
            it.lightChinaNumberOfPeople?.let {
                    val peopleNumber = StringBuilder()
                    val size = 9 - it.length
                    for (i in 0 until size){
                        peopleNumber.append("0")
                    }
                peopleNumber.append(it)
                peopleNumber.toString().forEachIndexed { index, c ->
                    tvList[index].text = c.toString()
                }
            }
        }
    }
}