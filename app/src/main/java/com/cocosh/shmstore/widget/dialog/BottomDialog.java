package com.cocosh.shmstore.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.widget.wheel.WheelView2;

/**
 * Created by wt on 2018/2/5.
 */

public class BottomDialog extends Dialog implements View.OnClickListener {

    private TextView tvTitle;
    public WheelView2 wvSex;
    private TextView tvCancel;
    public TextView tvComplete;
    private OnCompleteListener listener;

    public BottomDialog(@NonNull Context context) {
        this(context, android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    public BottomDialog(@NonNull Context context, int themeResId) {
        this(context, false, null);
    }

    public BottomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
//        this.activity = activity;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 让dialog置于底部
        Window window = getWindow();
        assert window != null;
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);
        window.setBackgroundDrawableResource(R.color.transparent);

        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_layout, null);
        wvSex = (WheelView2) view.findViewById(R.id.wvSex);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvComplete = (TextView) view.findViewById(R.id.tvComplete);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        tvCancel.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
        setCancelable(true);

        setContentView(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                this.dismiss();
                break;
            case R.id.tvComplete:
                this.dismiss();
                listener.onComplete();
                break;
        }
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public interface OnCompleteListener{
        void onComplete();
    }

    /**
     * 设置完成的监听
     * @param listener
     */
    public void setOnCompleteListener(OnCompleteListener listener) {
        this.listener = listener;
    }


}
