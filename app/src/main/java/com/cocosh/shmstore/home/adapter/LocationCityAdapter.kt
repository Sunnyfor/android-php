package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.cocosh.shmstore.R
import com.cocosh.shmstore.home.model.LocationCityModel
import com.cocosh.shmstore.utils.FullyGridLayoutManager
import kotlinx.android.synthetic.main.layout_city_title_item.view.*
import kotlinx.android.synthetic.main.layout_normal_item.view.*

/**
 *
 * Created by zhangye on 2018/5/14.
 */
class LocationCityAdapter(var context: Context, var data: ArrayList<LocationCityModel>, private var isSearch: Boolean) : BaseExpandableListAdapter() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun getGroupCount(): Int = data.size

    override fun getChildrenCount(groupPosition: Int): Int {
        return if ((groupPosition == 1 || groupPosition == 0) && !isSearch) {
            1
        } else {
            data[groupPosition].citys.size
        }
    }

    override fun getGroup(groupPosition: Int): LocationCityModel = data[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): LocationCityModel.NormalCityModel =
            data[groupPosition].citys[childPosition]

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        var groupViewHolder: GroupViewHolder? = null
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_city_title_item, parent, false)
            groupViewHolder = GroupViewHolder(view)
            view.tag = groupViewHolder
        } else {
            groupViewHolder = view.tag as GroupViewHolder
        }
        if (groupPosition == 0 || groupPosition == 1) {
            groupViewHolder.itemCityTitleView.tv_city_title.setTextColor(context.resources.getColor(R.color.text_gray))
        } else {
            groupViewHolder.itemCityTitleView.tv_city_title.setTextColor(context.resources.getColor(R.color.black))
        }
        groupViewHolder.itemCityTitleView.tv_city_title.text = data[groupPosition].firstLetter
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View? {
        var childView = convertView
        val childViewHolder: ChildViewHolder?

        if (childView == null) {
            childView = LayoutInflater.from(context).inflate(R.layout.layout_normal_item, parent, false)
            childViewHolder = ChildViewHolder(childView)
            childView.tag = childViewHolder
        } else {
            childViewHolder = childView.tag as ChildViewHolder
        }
        if (groupPosition == 0 && !isSearch) {
            childViewHolder.itemNormalCityView.tv_city_name.visibility = View.GONE
            childViewHolder.itemNormalCityView.rv_hot_city.visibility = View.VISIBLE
            childViewHolder.itemNormalCityView.line.visibility = View.GONE
            val layoutManager = FullyGridLayoutManager(context, 3)
            val adapter = HotCityAdapter(data[groupPosition].citys)
            childViewHolder.itemNormalCityView.rv_hot_city.adapter = adapter
            childViewHolder.itemNormalCityView.rv_hot_city.layoutManager = layoutManager
            adapter.setOnItemClickListener(object : com.cocosh.shmstore.base.OnItemClickListener {
                override fun onItemClick(v: View, index: Int) {
                    onItemClickListener?.onItemClick(v.tag.toString())
                }

            })
        } else if (groupPosition == 1 && !isSearch) {
            val adapter = HotCityAdapter(data[groupPosition].citys)
            childViewHolder.itemNormalCityView.rv_hot_city.visibility = View.VISIBLE
            childViewHolder.itemNormalCityView.tv_city_name.visibility = View.GONE
            childViewHolder.itemNormalCityView.line.visibility = View.GONE
            val layoutManager = FullyGridLayoutManager(context, 3)
            childViewHolder.itemNormalCityView.rv_hot_city.adapter = adapter
            childViewHolder.itemNormalCityView.rv_hot_city.layoutManager = layoutManager

            adapter.setOnItemClickListener(object : com.cocosh.shmstore.base.OnItemClickListener {
                override fun onItemClick(v: View, index: Int) {
                    onItemClickListener?.onItemClick(v.tag.toString())
                }

            })

        } else {
            childViewHolder.itemNormalCityView.tv_city_name.text = data[groupPosition].citys[childPosition].name
            childViewHolder.itemNormalCityView.tv_city_name.visibility = View.VISIBLE
            childViewHolder.itemNormalCityView.rv_hot_city.visibility = View.GONE
            if (isLastChild) {
                childViewHolder.itemNormalCityView.line.visibility = View.GONE
            } else {
                childViewHolder.itemNormalCityView.line.visibility = View.VISIBLE
            }
            childViewHolder.itemNormalCityView.tv_city_name.setOnClickListener({
                if (onItemClickListener != null) {
                    onItemClickListener?.onItemClick(data[groupPosition].citys[childPosition].name)
                }
            })
        }

        return childView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = false

    internal inner class GroupViewHolder(var itemCityTitleView: View)

    internal inner class ChildViewHolder(var itemNormalCityView: View)


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(cityName: String)
    }
}