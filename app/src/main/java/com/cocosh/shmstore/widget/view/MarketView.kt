package com.cocosh.shmstore.widget.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.newhome.model.Recommend
import kotlinx.android.synthetic.main.layout_view_market.view.*

/**
 * 活动View
 */
class MarketView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private val title: ArrayList<TextView> by lazy {
        arrayListOf(tvName1, tvName2, tvName3, tvName4, tvName5)
    }

    private val desc: ArrayList<TextView> by lazy {
        arrayListOf(tvDesc1, tvDesc2, tvDesc3, tvDesc4, tvDesc5)
    }

    private val photo: ArrayList<ImageView> by lazy {
        arrayListOf(ivPhoto1, ivPhoto2, ivPhoto3, ivPhoto4, ivPhoto5)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_view_market, this, true)
    }


    fun initData(market: Recommend.Market) {
        tvName.text = market.name
        tvDesc.text = market.desc

        market.program?.let {
            it.forEachIndexed { index, program ->
                title[index].text = program.name
                desc[index].text = program.desc
                Glide.with(context)
                        .load(program.image)
                        .dontAnimate()
                        .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                        .into(photo[index])
            }
        }
    }
}