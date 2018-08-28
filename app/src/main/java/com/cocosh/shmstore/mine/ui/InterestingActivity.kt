package com.cocosh.shmstore.mine.ui

import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.InterestingAdapter
import com.cocosh.shmstore.mine.model.Ethnic
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.UserManager2
import kotlinx.android.synthetic.main.activity_interesting.*

/**
 * 新版兴趣爱好
 * Created by zhangye on 2018/8/10.
 */
class InterestingActivity : BaseActivity() {
    val selectMap = HashMap<String,String>()
    override fun setLayout(): Int = R.layout.activity_interesting

    override fun initView() {

        intent.getStringExtra("hobby")?.let {

            it.split(",").forEach {
                if (it != "0" && it !=""){
                    selectMap[it] = it
                }
            }

        }

        titleManager.defaultTitle("兴趣爱好")

        btnNext.setOnClickListener(this)

        recyclerView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)

                if(parent.getChildLayoutPosition(view)%3 == 0){
                    outRect.left = resources.getDimension(R.dimen.w36).toInt()
                    outRect.right = resources.getDimension(R.dimen.w36).toInt()
                }

                if(parent.getChildLayoutPosition(view)%3 == 1){
                    outRect.left = resources.getDimension(R.dimen.w36).toInt()
                    outRect.right = resources.getDimension(R.dimen.w36).toInt()
                }

                if(parent.getChildLayoutPosition(view)%3 == 2){
                    outRect.left = resources.getDimension(R.dimen.w36).toInt()
                    outRect.right = resources.getDimension(R.dimen.w36).toInt()
                }
            }

        })
        reTryGetData()
    }

    override fun onListener(view: View) {
        when(view.id){
            btnNext.id -> {
                val hobbySb = StringBuilder()
                selectMap.forEach {
                    hobbySb.append(it.key).append(",")
                }
                val params = HashMap<String,String>()
                params["hobby"] =hobbySb.toString().substring(0,hobbySb.length - 1)
                UserManager2.updateMemberEntrance(this,params,object :ApiManager2.OnResult<BaseBean<String>>(){
                    override fun onSuccess(data: BaseBean<String>) {
                        UserManager2.getMemberEntrance()?.let {
                            it.hobby = params["hobby"]?:"0"
                            UserManager2.setMemberEntrance(it)
                        }
                        setResult(IntentCode.IS_INPUT,intent)
                        finish()
                    }

                    override fun onFailed(code: String, message: String) {
                    }

                    override fun onCatch(data: BaseBean<String>) {
                    }
                })


            }
        }
    }

    override fun reTryGetData() {

        ApiManager2.get(1, this@InterestingActivity, null, Constant.COMMON_HOBBY, object : ApiManager2.OnResult<BaseBean<ArrayList<Ethnic>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Ethnic>>) {
                data.message?.let {
                    val adapter = InterestingAdapter(it,selectMap)
                    recyclerView.adapter = adapter
                    adapter.onitemClickListener = object :OnItemClickListener{
                        override fun onItemClick(v: View, index: Int) {
                            loadStatus()
                        }
                    }
                    loadStatus()
                }

            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<ArrayList<Ethnic>>) {

            }

        })

    }

    fun loadStatus(){
        if (selectMap.size>0){
            btnNext.isClickable = true
            btnNext.setBackgroundResource(R.drawable.shape_btn_red)
            btnNext.setTextColor(ContextCompat.getColor(this@InterestingActivity,R.color.white))
        }else{
            btnNext.isClickable = false
            btnNext.setBackgroundResource(R.drawable.shape_interesting_gray)
            btnNext.setTextColor(ContextCompat.getColor(this@InterestingActivity,R.color.blackText))
        }
    }

}