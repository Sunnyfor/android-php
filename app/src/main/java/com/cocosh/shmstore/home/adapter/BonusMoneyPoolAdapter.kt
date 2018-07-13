package com.cocosh.shmstore.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.BonusListActivity
import com.cocosh.shmstore.home.model.BonusPool
import com.cocosh.shmstore.login.ui.activity.LoginActivity
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.layout_bonus_money_pool_item.view.*
import java.math.BigDecimal

/**
 * 红包资金池列表子条目
 * Created by zhangye on 2018/4/19.
 */
class BonusMoneyPoolAdapter(list: ArrayList<BonusPool.Data>) : BaseRecycleAdapter<BonusPool.Data>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        GlideUtils.load(context, getData(position).popularRedEnvelopesHeadPicture, holder.itemView.ivPhoto)

        holder.itemView.tvName.text = when (getData(position).redType) {
            1 -> "大众红包"
            2 -> "精准红包"
            else -> {
                "粉丝红包"
            }
        }
        holder.itemView.tvMoney.text = (BigDecimal(getData(position).popularRedEnvelopes).toLong().toString())
        holder.itemView.tvDesc.text = getData(position).popularRedEnvelopesFiles
        getData(position).percentPopularRedEnvelopes?.let {
            val progress = (it.toFloat() * 100)
            holder.itemView.progressBar.progress = progress
            holder.itemView.tvScale.text = ("已抢$progress%")
        }
        holder.itemView.btnNext.setOnClickListener {
            if (UserManager.isLogin()) {
                val intent = Intent(context, BonusListActivity::class.java)
                intent.putExtra("title", holder.itemView.tvName.text.toString())
                context.startActivity(intent)
            } else {
                SmediaDialog(context).showLogin()
            }

        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_bonus_money_pool_item, parent, false)
}