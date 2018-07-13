package com.cocosh.shmstore.mine.adapter

import android.support.v7.widget.LinearLayoutManager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.listener.InterestingDataListener
import com.cocosh.shmstore.mine.model.InterestModel
import com.cocosh.shmstore.mine.ui.ArchiveInterestingActivity
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_archive_interesting_list.view.*

/**
 *
 * Created by lmg on 2018/4/12.
 */
class InterestingBottomAdapter(list: ArrayList<InterestModel>, private var selectData:ArrayList<InterestModel.Data>) : BaseRecycleAdapter<InterestModel>(list) {
    var adapterMap = HashMap<Int,InterestingBottomSubAdapter>()
    var onDataListener: InterestingDataListener? = null

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvSubName.text = list[position].interestGroupName
        holder.itemView.recyclerViewSub.layoutManager = LinearLayoutManager(context)

        val adapter = InterestingBottomSubAdapter(list[position].interestList?:ArrayList(),selectData,position)
        adapterMap[position] = adapter
        onDataListener?.let {
            adapter.setOnDataListener(it)
        }

        holder.itemView.recyclerViewSub.adapter = adapter
        GlideUtils.loadHead(context,list[position].interestGroupImg,holder.itemView.ivSubIcon)

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_archive_interesting_list, parent, false)

    fun setOnDataListener(onDataListener: InterestingDataListener): InterestingBottomAdapter {
        this.onDataListener = onDataListener
        return this
    }
}