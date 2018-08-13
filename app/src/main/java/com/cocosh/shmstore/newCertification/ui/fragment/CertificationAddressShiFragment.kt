package com.cocosh.shmstore.newCertification.ui.fragment

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.coco_sh.shmstore.mine.adapter.AddressShiAdapter
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.newCertification.model.AddressServiceModel
import com.cocosh.shmstore.newCertification.ui.CertificationAddressActivity
import com.cocosh.shmstore.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.activity_certification_address.*
import kotlinx.android.synthetic.main.fragment_shi.view.*

@SuppressLint("ValidFragment")
/**
 * Created by lmg on 2018/3/20.
 */
class CertificationAddressShiFragment(var list: ArrayList<AddressServiceModel>) : BaseFragment() {
    override fun reTryGetData() {

    }

    var adapter: AddressShiAdapter? = null
    override fun setLayout(): Int = R.layout.fragment_shi

    override fun initView() {
        adapter = AddressShiAdapter(list, (activity as CertificationAddressActivity).openType)
        getLayoutView().recycler_shi.addItemDecoration(DividerGridItemDecoration(activity, 0))
        getLayoutView().recycler_shi.layoutManager = LinearLayoutManager(activity)
        getLayoutView().recycler_shi.adapter = adapter

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                if ((activity as CertificationAddressActivity).openType == 333) {
                    if (list[index].full == 0) {
                        //弹出服务商已满弹框
                        (activity as CertificationAddressActivity).rl.visibility = View.VISIBLE
                    } else {
                        (activity as CertificationAddressActivity).chooseShi(list[index].name,list[index].fee, list[index].code.toInt())
                    }
                } else {
                    for (m in 0..(list.size - 1)) {
                        if (m == index) {
                            list[m].isChecked = 1
                        } else {
                            list[m].isChecked = 0
                        }
                        (activity as CertificationAddressActivity).chooseShi(list[index].name, list[index].fee, list[index].code.toInt())
                        notifyData()
                    }
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