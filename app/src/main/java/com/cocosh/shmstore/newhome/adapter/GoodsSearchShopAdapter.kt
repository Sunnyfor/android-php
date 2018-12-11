package com.cocosh.shmstore.newhome.adapter

import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.model.Shop
import kotlinx.android.synthetic.main.activity_goods_shopping.*
import kotlinx.android.synthetic.main.layout_shop_search_item.view.*
import org.greenrobot.eventbus.EventBus

class GoodsSearchShopAdapter(var baseActivity: BaseActivity,mList: ArrayList<Shop>) : BaseRecycleAdapter<Shop>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

        val moneyList = arrayListOf(
                holder.itemView.tvMoney1,
                holder.itemView.tvMoney2,
                holder.itemView.tvMoney3
        )

        val photoList = arrayListOf(
                holder.itemView.ivPhoto1,
                holder.itemView.ivPhoto2,
                holder.itemView.ivPhoto3
        )

        val llGoods = arrayListOf(
                holder.itemView.llGoods1,
                holder.itemView.llGoods2,
                holder.itemView.llGoods3
        )

        Glide.with(context)
                .load(getData(position).logo)
                .dontAnimate()
                .centerCrop()
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        holder.itemView.tvName.text = getData(position).name
        holder.itemView.tvDesc.text = (getData(position).total + "件在售商品")

        if (getData(position).attention == "1") {
            holder.itemView.btnFollow.setBackgroundResource(R.mipmap.ic_shop_cancel_follow)
        } else {
            holder.itemView.btnFollow.setBackgroundResource(R.mipmap.ic_shop_follow)
        }

        holder.itemView.btnFollow.setOnClickListener {
            collectionShop(position)
        }


        getData(position).goods?.forEachIndexed { index, goods ->

            if (index>= llGoods.size){
                return@forEachIndexed
            }

            llGoods[index].visibility = View.VISIBLE
            moneyList[index].text = ("¥"+goods.price)
            Glide.with(context)
                    .load(goods.image)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                    .into(photoList[index])
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_shop_search_item, parent, false)


    private fun collectionShop(position: Int) {
        val params = hashMapOf<String, String>()
        params["store_id"] = getData(position).id
        params["op"] = if (getData(position).attention == "0") "1" else "2"
        ApiManager2.post(baseActivity, params, Constant.ESHOP_FAV_STORE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                if (getData(position).attention == "0") {
                    getData(position).attention = "1"
                } else {
                    getData(position).attention = "0"
                }
                notifyDataSetChanged()
                EventBus.getDefault().post(getData(position))
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}