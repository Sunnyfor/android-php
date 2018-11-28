package com.cocosh.shmstore.title

import android.text.Editable
import android.text.TextWatcher
import android.text.method.KeyListener
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import kotlinx.android.synthetic.main.layout_goods_search_title.view.*

class SearchTitleFragment : BaseFragment() {

    var onKeyWord: (() -> Unit)? = null

    var onKeyWordChanger: ((String) -> Unit)? = null

    override fun setLayout(): Int = R.layout.layout_goods_search_title

    override fun initView() {
        getLayoutView().tvLeft.setOnClickListener {
            activity.finish()
        }

        getLayoutView().tvSearch.setOnClickListener {
            onKeyWord?.invoke()
        }
        getLayoutView().editView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                onKeyWordChanger?.invoke(s.toString())
            }

        })


        getLayoutView().editView.requestFocus()
        getLayoutView().editView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onKeyWord?.invoke()
                getBaseActivity().hideKeyboard(getLayoutView().windowToken)
            }
            false
        }

    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }
}