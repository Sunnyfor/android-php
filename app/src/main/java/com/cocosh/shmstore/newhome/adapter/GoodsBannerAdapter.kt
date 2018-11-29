package com.cocosh.shmstore.newhome.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import kotlinx.android.synthetic.main.item_card_view.view.*

class GoodsBannerAdapter(var context: Context, var list: ArrayList<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = View.inflate(context, R.layout.item_card_view, null)
        Glide.with(context)
                .load(list[position])
                .dontAnimate()
                .placeholder(ColorDrawable(ContextCompat.getColor(context,R.color.activity_bg)))
                .into(view.imageView)
        container.addView(view)
        return view
    }
}