package com.cocosh.shmstore.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.LocationCityModel
import kotlinx.android.synthetic.main.layout_hot_city_item.view.*

/**
 *
 * Created by zhangye on 2018/5/14.
 */
class HotCityAdapter(list: ArrayList<LocationCityModel.NormalCityModel>) : BaseRecycleAdapter<LocationCityModel.NormalCityModel>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        getData(position).name.let {
            holder.itemView.tv_hot_city.text = it
            holder.itemView.tag = it
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            LayoutInflater.from(context).inflate(R.layout.layout_hot_city_item, parent, false)

}