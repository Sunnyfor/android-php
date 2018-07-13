package com.cocosh.shmstore.login.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cocosh.shmstore.R;
import com.cocosh.shmstore.login.model.LoginHistory;
import com.cocosh.shmstore.widget.textview.ICONTextView;
import java.util.List;

/**
 * Created by wt on 2018/1/23.
 */

public class PhoneHistoryAdapter extends BaseAdapter {
    private List<LoginHistory> list;

    public PhoneHistoryAdapter(List<LoginHistory> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(viewGroup.getContext(), R.layout.item_phone_history_layout, null);
        }
        TextView tvPhoneHistory = (TextView) convertView.findViewById(R.id.tvPhoneHistory);
        tvPhoneHistory.setText(list.get(i).getPhone());
        ICONTextView delete = (ICONTextView) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemDeleteListener.onItemDelete(i);
            }
        });
        return convertView;
    }

    public interface onItemDeleteListener {
        void onItemDelete(int position);
    }

    private onItemDeleteListener onItemDeleteListener;

    public void setOnItemDeleteListener(onItemDeleteListener listener) {
        onItemDeleteListener = listener;
    }
}
