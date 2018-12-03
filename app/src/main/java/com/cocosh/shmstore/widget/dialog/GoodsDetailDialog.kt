package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.*
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.newhome.model.GoodsDetail
import kotlinx.android.synthetic.main.dialog_goods_detail_format.*
import kotlinx.android.synthetic.main.item_goods_detail_label.view.*

class GoodsDetailDialog(context: Context, var goodsDetail: GoodsDetail) : Dialog(context), View.OnClickListener {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_goods_detail_format)
        window.setBackgroundDrawableResource(R.color.transparent)
        window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        window.setGravity(Gravity.BOTTOM)

        itv_close.setOnClickListener(this)
        rl_add.setOnClickListener(this)
        rl_jian.setOnClickListener(this)
        initData()

    }

    fun initData() {

        Glide.with(context)
                .load(goodsDetail.goods.image[0])
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(ivPhoto)
        tvMoney.text = goodsDetail.goods.price



        goodsDetail.sku.attrs.forEach {

        }

        val view = LayoutInflater.from(context).inflate(R.layout.item_goods_detail_label, llLabel, false)
        view.tvName.text = "规格"
        view.labView.setLabels(arrayListOf(
                "30g",
                "50g",
                "100g"
        ))
        llLabel.addView(view)
    }

    override fun onClick(v: View) {
        when (v.id) {
            itv_close.id -> dismiss()
            rl_add.id -> add()
            rl_jian.id -> jian()
        }
    }

    fun add() {
        val count = tvCount.text.toString().toInt()
        if (count < 999) {
            tvCount.text = (count + 1).toString()
        }
    }

    private fun jian() {
        val count = tvCount.text.toString().toInt()
        if (count > 1) {
            tvCount.text = (count - 1).toString()
        }
    }
}