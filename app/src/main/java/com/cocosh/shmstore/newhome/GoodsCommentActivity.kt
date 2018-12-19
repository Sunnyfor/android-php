package com.cocosh.shmstore.newhome

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.Order
import com.cocosh.shmstore.newhome.adapter.GoodsCommentAdapter
import com.cocosh.shmstore.utils.DataCode
import kotlinx.android.synthetic.main.layout_pull_refresh.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject

class GoodsCommentActivity : BaseActivity() {

    val goodsList = arrayListOf<Order.Goods>()
    private var order_sn = ""

    companion object {
        fun start(context:Context,order_sn:String){
            context.startActivity(Intent(context,GoodsCommentActivity::class.java)
                    .putExtra("order_sn",order_sn))
        }
    }

    override fun setLayout(): Int = R.layout.layout_pull_refresh

    override fun initView() {
        titleManager.rightText("评论", "评论", View.OnClickListener {
            comment()
        })

        order_sn = intent.getStringExtra("order_sn")

        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.recyclerView.layoutManager = LinearLayoutManager(this)
        SmApplication.getApp().getData<ArrayList<Order.Goods>>(DataCode.ORDER_GOODS, true)?.let {
            goodsList.addAll(it)
            swipeRefreshLayout.recyclerView.adapter = GoodsCommentAdapter(it)
        }

    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {
    }

    private fun comment() {
        showLoading()
        val params = hashMapOf<String, String>()
        params["order_sn"] =order_sn

        val jsonArray = JSONArray()
        goodsList.forEach {
            jsonArray.put(JSONObject()
                    .put("goods_id",it.goods_id)
                    .put("stars",it.ratingNum)
                    .put("words",it.commentStr))
        }
        params["data"] = jsonArray.toString()

        ApiManager2.post(this, params, Constant.ESHOP_COMMENT_ADD, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                hideLoading()
                EventBus.getDefault().post(Order("","","","","","","", arrayListOf()))
                finish()
            }

            override fun onFailed(code: String, message: String) {
                hideLoading()
            }

            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }
}