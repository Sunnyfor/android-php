package com.cocosh.shmstore.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.MineTopNavEntity
import kotlinx.android.synthetic.main.item_mine_top_nav.view.*

/**
 * 我的顶部导航适配器
 * Created by zhangye on 2018/3/13.
 */
class ShouMeiImageAdapter(list: ArrayList<String>) : BaseRecycleAdapter<String>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_image, parent, false)

}