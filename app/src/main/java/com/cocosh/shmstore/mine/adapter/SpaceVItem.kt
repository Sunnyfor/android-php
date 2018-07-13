package com.cocosh.shmstore.mine.adapter

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by lmg on 2018/4/12.
 */
class SpaceVItem(var spaceH: Int, var spaceHStart: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent?.getChildAdapterPosition(view) == 0) {
            outRect?.top = spaceHStart
        } else {
            outRect?.top = spaceH
        }
    }
}