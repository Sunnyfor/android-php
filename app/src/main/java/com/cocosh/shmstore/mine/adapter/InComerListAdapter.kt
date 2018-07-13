package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.ProfitModel
import kotlinx.android.synthetic.main.item_income_water_list.view.*
import kotlinx.android.synthetic.main.item_menu_sticky.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 适配器
 * Created by lmg on 2018/3/13.
 */
class InComerListAdapter(list: ArrayList<ProfitModel>) : BaseRecycleAdapter<ProfitModel>(list) {
    val ITEM = 1
    val SECTION = 2
    /**服务器返回的历史账单列表 */
    /**本地分组后的账单 */
    private var groupBills: TreeMap<String, ArrayList<ProfitModel>>? = null
    /**Adapter的数据源 */
    private var items: ArrayList<ProfitModel>? = null
    var sdf: SimpleDateFormat? = null
    override fun getItemCount(): Int {
        return items?.size!!
    }

    init {
        items = ArrayList<ProfitModel>()
        groupBills = TreeMap<String, ArrayList<ProfitModel>>()
        sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    }

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (items!![position].viewType == this.ITEM) {
            holder.itemView.tvType.text = items!![position].detailDesc
            holder.itemView.tvNum.text = items!![position].runningNum
            holder.itemView.tvTime.text = items!![position].dateTime
            holder.itemView.tvMoney.text = items!![position].money
        }

        if (items!![position].viewType == this.SECTION) {
            holder.itemView.title.text = items!![position].dateTime
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == SECTION) {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_menu_sticky, parent, false)
        }
        return LayoutInflater.from(parent.context).inflate(R.layout.item_income_water_list, parent, false)
    }

    override fun getItemViewType(position: Int): Int {
        return items!![position].viewType!!
    }

    private fun inflaterItems() {
        items?.clear()
        groupBills?.clear()
        for (bill in list) {//遍历bills将集合中的所有数据以月份进行分类
            val groupName = sdf?.format(sdf?.parse(bill.dateTime))
            if (groupBills?.containsKey(groupName)!!) {//如果Map已经存在以该记录的日期为分组名的分组，则将该条记录插入到该分组中
                groupBills?.get(groupName)?.add(bill)
            } else {//如果不存在，以该记录的日期作为分组名称创建分组，并将该记录插入到创建的分组中
                val tempBills = ArrayList<ProfitModel>()
                tempBills.add(bill)
                if (groupName != null) {
                    groupBills?.put(groupName, tempBills)
                }
            }
        }

        val iterator = groupBills?.descendingMap()?.entries?.iterator()
        while (iterator?.hasNext()!!) {//将分组后的数据添加到数据源的集合中
            val entry = iterator.next()
            val item = ProfitModel()
            item.dateTime = entry.key
            item.viewType = SECTION
            items?.add(item)//将分组添加到集合中
            for (bill in entry.value) {//将组中的数据添加到集合中
                val item = ProfitModel()
                item.dateTime = bill.dateTime
                item.viewType = ITEM
                item.detailDesc = bill.detailDesc
                item.detailId = bill.detailId
                item.money = bill.money
                item.runningNum = bill.runningNum
                items?.add(item)
            }
        }
    }

    /**
     * 点击事件
     */
    fun notifyDatas() {
        inflaterItems()
        notifyDataSetChanged()
    }

    fun getItemData(position: Int): String {
        return sdf?.format(sdf?.parse(items?.get(position)?.dateTime)) ?: ""
    }

    fun clear() {
        items?.clear()
        groupBills?.clear()
        list.clear()
    }

    fun getLastId(): String {
        return items!![items?.size!! - 1]?.detailId ?: ""
    }
}