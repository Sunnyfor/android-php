package com.cocosh.shmstore.title

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import kotlinx.android.synthetic.main.layout_goods_search_title.view.*

class SearchTitleFragment: BaseFragment() {

    var onKeyWord:((String)->Unit)? = null

    override fun setLayout(): Int = R.layout.layout_goods_search_title

    override fun initView() {
        getLayoutView().tvLeft.setOnClickListener {
            activity.finish()
        }

        getLayoutView().tvSearch.setOnClickListener {
            onKeyWord?.invoke(getLayoutView().editView.text.toString())
        }

        getLayoutView().editView.requestFocus()
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }
}