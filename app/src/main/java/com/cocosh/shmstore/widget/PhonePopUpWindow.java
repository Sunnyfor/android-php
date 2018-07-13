package com.cocosh.shmstore.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.login.ILoginContract;
import com.cocosh.shmstore.login.adapter.PhoneHistoryAdapter;
import com.cocosh.shmstore.login.model.LoginHistory;
import com.cocosh.shmstore.login.ui.activity.LoginActivity;
import com.cocosh.shmstore.widget.dialog.SmediaDialog;

import java.util.List;

/**
 *
 * Created by wt on 2018/1/22.
 */

public class PhonePopUpWindow extends PopupWindow {
    private View parent;
    private List<LoginHistory> list;
    private Context context;
    private final PhoneHistoryAdapter historyAdapter;
    private ILoginContract.IView iView;

    public PhonePopUpWindow(final Context context, final List<LoginHistory> list, int layout, final View parent, ILoginContract.IView iView) {
        super(context);
        this.parent = parent;
        this.list = list;
        this.context = context;
        this.iView = iView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null);
        ListView lv = (ListView) contentView.findViewById(R.id.lv);
        historyAdapter = new PhoneHistoryAdapter(list);
        lv.setAdapter(historyAdapter);
        historyAdapter.setOnItemDeleteListener(new PhoneHistoryAdapter.onItemDeleteListener() {
            @Override
            public void onItemDelete(int position) {
                showDeleteDialog(position);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((EditText) parent).setText(list.get(i).getPhone());
                ((EditText) parent).setSelection(((EditText) parent).getText().length());
                PhonePopUpWindow.this.dismiss();
            }
        });

        this.setContentView(contentView);
        this.setWidth(parent.getWidth());
        this.setHeight(((int) context.getResources().getDimension(R.dimen.h600)));
        this.setBackgroundDrawable(new ColorDrawable(0000000000));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
    }

    /**
     * 提示是否删除数据
     */
    private void showDeleteDialog(final int position) {
        SmediaDialog dialog = new SmediaDialog(context);
        dialog.setTitle("是否清除该号码");
        dialog.setPostion(position);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginHistory history = list.get(((int) v.getTag()));
                ((LoginActivity) context).getMPresenter().removeHistory(history);
                list.remove(history);
                historyAdapter.notifyDataSetChanged();

                if(list.isEmpty()){
                    iView.gonePhone(history);
                }

            }
        });
        dialog.show();
    }

    /**
     *显示popupWindow
     */
    public void showPopUp() {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
