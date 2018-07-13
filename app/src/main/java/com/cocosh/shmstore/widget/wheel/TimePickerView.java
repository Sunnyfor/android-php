package com.cocosh.shmstore.widget.wheel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocosh.shmstore.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Desc:   时间选择器
 * Author: chencha
 * Date: 16/11/4
 */

public class TimePickerView extends BasePickerView implements View.OnClickListener {
    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH
    }// 四种选择模式，年月日时分，年月日，时分，月日时分

    WheelTime wheelTime;
    private TextView btnSubmit, btnCancel;
    private TextView tvTitle;
    private LinearLayout llLong;

    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private static final String TAG_LONG = "lllong";
    private OnTimeSelectListener timeSelectListener;

    public TimePickerView(Context context, Type type) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.pickerview_time_b, contentContainer);
        // -----确定和取消按钮
        btnSubmit = (TextView) findViewById(R.id.tvComplete);
        btnCancel = (TextView) findViewById(R.id.tvCancel);
        llLong = (LinearLayout) findViewById(R.id.ll_long);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel.setTag(TAG_CANCEL);
        llLong.setTag(TAG_LONG);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        llLong.setOnClickListener(this);
        //顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----时间转轮
        final View timepickerview = findViewById(R.id.timepicker);
        wheelTime = new WheelTime(timepickerview, type);

        //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);

    }

    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     */
    public void setRange(int startYear, int endYear) {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);
    }

    /**
     * 设置选中时间
     *
     * @param date 时间
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
    }

    //是否显示布局
    public void setLONGLayout(boolean isShow) {
        if (isShow) {
            llLong.setVisibility(View.VISIBLE);
        } else {
            llLong.setVisibility(View.GONE);
        }
    }


    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else if (tag.equals(TAG_SUBMIT)) {
            if (timeSelectListener != null) {
                String time = wheelTime.getTime();
                timeSelectListener.onTimeSelect(time);
            }
            dismiss();
            return;
        } else {
            if (timeSelectListener != null) {
                String time = "长期";
                timeSelectListener.onTimeSelect(time);
            }
            dismiss();
            return;
        }
    }

    public interface OnTimeSelectListener {
            void onTimeSelect(String date);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setBtnSubmit(String title) {
        btnSubmit.setText(title);
    }

    public void setBtnSize(int res) {
        btnCancel.setTextSize(res);
        btnSubmit.setTextSize(res);

        RelativeLayout.LayoutParams pl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pl.setMargins(40, 0, 0, 0);
        btnCancel.setLayoutParams(pl);
    }

    public void setTextXolor(Context context, int mColor) {
        btnSubmit.setTextColor(context.getResources().getColor(mColor));
//        btnSubmit.setTextSize(11);
    }

    public void setBtnCancel(String title) {
        btnCancel.setText(title);
    }
}
