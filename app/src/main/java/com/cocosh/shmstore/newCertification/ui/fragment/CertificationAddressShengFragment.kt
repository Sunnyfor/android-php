package com.cocosh.shmstore.newCertification.ui.fragment

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.newCertification.adapter.AddressAdapter
import com.cocosh.shmstore.newCertification.model.AddressServiceModel
import com.cocosh.shmstore.newCertification.ui.CertificationAddressActivity
import com.cocosh.shmstore.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.activity_certification_address.*
import kotlinx.android.synthetic.main.fragment_sheng.view.*

@SuppressLint("ValidFragment")
/**
 * Created by lmg on 2018/3/20.
 */
class CertificationAddressShengFragment(var list: ArrayList<AddressServiceModel>) : BaseFragment() {
    override fun reTryGetData() {

    }

    var adapter: AddressAdapter? = null
    override fun setLayout(): Int = R.layout.fragment_sheng
    var isCity: Int = 0
//    var list = ArrayList<AddressServiceModel>()

    override fun initView() {
//        isCity = 1
        adapter = AddressAdapter(list, (activity as CertificationAddressActivity).openType)
        getLayoutView().recycler_sheng.addItemDecoration(DividerGridItemDecoration(activity, 0))
        getLayoutView().recycler_sheng.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        getLayoutView().recycler_sheng.adapter = adapter

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                if (list[index].full == 0 && (activity as CertificationAddressActivity).openType == 333) {
                    //弹出服务商已满弹框
                    (activity as CertificationAddressActivity).rl.visibility = View.VISIBLE
                } else {
                    (activity as CertificationAddressActivity).chooseSheng(list[index].name,list[index].code.toInt(), index)
                }
            }
        })
    }

    fun notifyData() {
        adapter?.notifyDataSetChanged()
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }
}