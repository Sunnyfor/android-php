package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.home.model.Banner
import com.cocosh.shmstore.home.model.Bonus2
import kotlinx.android.synthetic.main.item_card_view.view.*

/**
 * 首页bannerAdapter
 * Created by zhangye on 2018/1/11.
 */
class HomeBannerAdapter(var context: Context, var list: ArrayList<Bonus2>) : PagerAdapter() {

    var onItemClickListener: OnItemClickListener? = null

    init {
        if (list.size >= 1) {
            list.add(0, list[list.size - 1])
            list.add(0, list[list.size - 2])
            list.add(list[2])
            list.add(list[3])
        }
    }


    override fun isViewFromObject(view: View, `object`: Any?): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = View.inflate(context, R.layout.item_card_view, null)
        Glide.with(context).load(list[position].image).into(view.imageView)
        container.addView(view)
        view.setOnClickListener {
            onItemClickListener?.onItemClick(it, position)
        }
        return view
    }


    override fun getCount(): Int = list.size


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) {
//        container.removeView(`object` as View)
    }

    override fun getPageTitle(position: Int): String? = list[position].name
}