package com.cocosh.shmstore.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * baseAdapter
 * Created by zhangye on 2018/1/5.
 */
abstract class BaseRecycleAdapter<T>(var list: ArrayList<T>) : RecyclerView.Adapter<BaseRecycleViewHolder>() {
    private var isDouble = false
    private var onItemClickListener: OnItemClickListener? = null
    lateinit var context: Context
    /*
     * 创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecycleViewHolder {
        context = parent.context
        return BaseRecycleViewHolder(setLayout(parent, viewType),onItemClickListener)
    }

    /*
     * 绑定数据
     */
    abstract override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int)


    override fun getItemCount(): Int = list.size

    abstract fun setLayout(parent: ViewGroup, viewType: Int): View


    fun getData(position: Int): T = list[position]

    fun deleteData(position: Int) {
        list.removeAt(position)
    }

    /*
     * 子条目点击事件
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    fun notifyDataSetChanged(mlist: ArrayList<T>) {
        if (!list.containsAll(mlist)) {
            list.addAll(mlist)
            notifyDataSetChanged()
        }
    }
}

