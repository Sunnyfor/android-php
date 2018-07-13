package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_contact_service.*

/**
 * Created by lmg on 2018/4/25.
 * 收藏
 */
class ContactServiceActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_contact_service

    override fun initView() {
        titleManager.defaultTitle("联系客服")
        show.setOnClickListener(this)
        content.text = "我要退换货" +
                "\n" +
                "以下情况不予办理退货" +
                "\n" +
                "1、任何非首媒出售的商品" +
                "\n" +
                "2、商品已使用影响二次销售，一经拆封将不支持退货" +
                "\n" +
                "3、超过受理时限（签收后7天内未提交申请将不能申请售后）" +
                "\n" +
                "4、由于运输过程中的不可抗拒因素，商品出现轻微破损，但不影响正常使用" +
                "\n" +
                "5、因涉及到食品安全问题，食品类商品（包括奶粉，保健品）不支持退货" +
                "\n" +
                "6、商品出售时，已在页面明示保质期将到" +
                "\n" +
                "7、任何因客户的非正常使用及保管，导致出现商品质量问题的" +
                "\n" +
                "8、礼包或者套装中的商品不可以部分退换货" +
                "\n" +
                "9、销售商品附送赠品、组合销售商品、礼包、套装中的商品不可以部分退货；" +
                "\n" +
                "10、因商品的特殊性，易碎品（陶瓷，玻璃制品）不接受7天无忧退换货" +
                "\n" +
                "11、其他依法不办理退货的" +
                "\n" +
                "客服联系：400-966-1168" +
                "\n" +
                "在线时间：09:00-18:00"
    }

    override fun onListener(view: View) {
        when (view.id) {
            show.id -> {
                if (content.visibility == View.VISIBLE) {
                    content.visibility = View.GONE
                    show.setIcon(resources.getString(R.string.iconMore))
                    return
                }
                show.setIcon(resources.getString(R.string.iconDown))
                content.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, ContactServiceActivity::class.java))
        }
    }
}