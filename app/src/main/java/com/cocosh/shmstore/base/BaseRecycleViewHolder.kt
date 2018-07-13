package com.cocosh.shmstore.base

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 *
 * Created by zhangye on 2018/1/5.
 */
@Suppress("UNCHECKED_CAST")
class BaseRecycleViewHolder(itemView: View,
                            onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val viewMap = SparseArray<View>()
    private var onItemClickListener: OnItemClickListener? = null

    init {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
            itemView.setOnClickListener(this)
        }
    }

    fun <T : View> getView(id: Int): T {
        if (viewMap.get(id) != null) {
            return viewMap.get(id) as T
        }
        val view = itemView.findViewById(id) as T
        viewMap.put(id, view)
        return view
    }

    override fun onClick(v: View) {
        onItemClickListener?.onItemClick(v, adapterPosition)
    }
}