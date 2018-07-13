package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.*
import com.cocosh.shmstore.home.ReportActivity
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.dialog_report.*
import kotlinx.android.synthetic.main.layout_select_item.view.*

/**
 * 举报dialog
 * Created by zhangye on 2018/6/12.
 * @param id  内容关联id
 * @param type 业务类型 1红包 2首媒之家贴子
 */
class ReportDialog(var context: BaseActivity, var id: String, var type: String) : Dialog(context) {
    var datas = arrayListOf<String>()
    var adapter: ReportAdapter? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_report)
        setCanceledOnTouchOutside(true)
        window.setGravity(Gravity.BOTTOM)
        window.setBackgroundDrawableResource(R.color.transparent)
        datas.add("淫秽色情")
        datas.add("违法信息")
        datas.add("营销广告")
        datas.add("恶意攻击谩骂")

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ReportAdapter(datas)
        recyclerView.adapter = adapter

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                adapter?.text = datas[index]
                adapter?.notifyDataSetChanged()
            }
        })

        tvName.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("type",type)
            context.startActivity(intent)
            dismiss()
        }

        btnNext.setOnClickListener {
            if ((adapter?.text?:"").isEmpty()){
                return@setOnClickListener
            }
            commit()
        }
    }

    class ReportAdapter(list: ArrayList<String>) : BaseRecycleAdapter<String>(list) {
        var text: String? = null

        override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
            holder.itemView.setBackgroundResource(R.color.white)
            holder.itemView.tvName.text = getData(position)
            if (text == getData(position)) {
                holder.itemView.tvName.setTextColor(ContextCompat.getColor(context, R.color.red))
            } else {
                holder.itemView.tvName.setTextColor(ContextCompat.getColor(context, R.color.blackText))
            }
        }

        override fun setLayout(parent: ViewGroup, viewType: Int): View =
                LayoutInflater.from(context).inflate(R.layout.layout_select_item, parent, false)

    }


    private fun commit() {
        val params = hashMapOf<String, String>()
        params["connectId"] = id
        params["reportType"] = type
        params["reportContent"] = adapter?.text ?: ""
        ApiManager.post(context, params, Constant.REPORT, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                if (data.success) {
                    dismiss()
                }
                ToastUtil.show(data.message)
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<String>) {
            }
        })
    }
}