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
import com.cocosh.shmstore.newhome.GoodsDetailActivity
import com.cocosh.shmstore.newhome.model.AddCar
import com.cocosh.shmstore.newhome.model.ShoppingCarts
import kotlinx.android.synthetic.main.layout_shopping_goods_item.view.*
import org.greenrobot.eventbus.EventBus

class ShoppingListItemGoodsAdapter(var baseActivity: BaseActivity, mList: ArrayList<ShoppingCarts.Shopping>, var notifyData: () -> Unit) : BaseRecycleAdapter<ShoppingCarts.Shopping>(mList) {

    var isEdit: Boolean = false

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvGoodsName.text = getData(position).goods_name
        holder.itemView.tvMoney.text = getData(position).sku_price

        val descSb = StringBuilder()
        getData(position).sku_attrs?.values?.forEach {
            descSb.append("$it，")
        }
        if (descSb.isNotEmpty()) {
            descSb.deleteCharAt(descSb.lastIndex)
        }

        holder.itemView.tvDesc.text = descSb.toString()


        holder.itemView.tvCount.text = getData(position).num

        holder.itemView.tvShowCount.text = ("x ${getData(position).num}")

        Glide.with(context)
                .load(getData(position).sku_image)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        if (getData(position).isChecked) {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
        } else {
            holder.itemView.vSelect.setBackgroundResource(R.drawable.bg_select_round_gray_no)
        }

        if (isEdit) {
            holder.itemView.vJian.visibility = View.GONE
            holder.itemView.vAdd.visibility = View.GONE
            holder.itemView.tvCount.visibility = View.VISIBLE
            holder.itemView.tvShowCount.visibility = View.GONE
        } else {
            holder.itemView.vJian.visibility = View.VISIBLE
            holder.itemView.vAdd.visibility = View.VISIBLE
            holder.itemView.tvCount.visibility = View.GONE
            holder.itemView.tvShowCount.visibility = View.VISIBLE
        }

        holder.itemView.rlSelect.setOnClickListener {
            getData(position).isChecked = !getData(position).isChecked
            notifyDataSetChanged()
            notifyData()
        }

        holder.itemView.vAdd.setOnClickListener {
            modifyCount(position,"1")
        }

        holder.itemView.vJian.setOnClickListener {
            modifyCount(position,"2")
        }

        holder.itemView.setOnClickListener {
            if (!isEdit) {
                GoodsDetailActivity.start(context, getData(position).goods_name, getData(position).goods_id)
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_shopping_goods_item, parent, false)


    //修改购物车数量 变更方式 (必填,'1'-增加,'2'-减少)
    private fun modifyCount(position: Int, type: String) {
        val params = hashMapOf<String, String>()
        params["sku_id"] = getData(position).sku_id
        params["type"] = type
        ApiManager2.post(baseActivity, params, Constant.ESHOP_CART_SELLNUM, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                if (data.message != "101" && data.message !="102"){
                    if (type == "1") {
                        getData(position).num = (getData(position).num.toInt() + 1).toString()
                    }else{
                        getData(position).num = (getData(position).num.toInt() - 1).toString()
                    }
                    notifyData()
                    notifyDataSetChanged()

                }else{
                    EventBus.getDefault().post(AddCar())    //刷新购物车
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}