package com.cocosh.shmstore.utils;

import android.content.Context;

import com.cocosh.shmstore.R;
import com.cocosh.shmstore.widget.dialog.BottomDialog;
import com.cocosh.shmstore.widget.wheel.ArrayWheelAdapter2;
import com.cocosh.shmstore.widget.wheel.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wt on 2018/2/5.
 */

public class DialogHelper {
    /**
     * 性别选择
     *
     * @param context
     * @return
     */
    public static BottomDialog showSexChooseDialog(Context context, ArrayList<String> list) {
        BottomDialog dialog = new BottomDialog(context);
        dialog.setTitle("性别选择");
        ArrayWheelAdapter2 adapter = new ArrayWheelAdapter2(list, 2);
        dialog.wvSex.setAdapter(adapter);
        dialog.wvSex.setCyclic(false);
        dialog.wvSex.setTextSize(context.getResources().getDimension(R.dimen.h43));

        dialog.show();

        return dialog;
    }

    /**
     * 民族选择
     *
     * @param context
     * @return
     */
    public static BottomDialog showNationChooseDialog(Context context, ArrayList<String> list) {
        BottomDialog dialog = new BottomDialog(context);
        dialog.setTitle("选择民族");
        ArrayWheelAdapter2 adapter = new ArrayWheelAdapter2(list, list.size());
        dialog.wvSex.setAdapter(adapter);
        dialog.wvSex.setCyclic(false);
        dialog.wvSex.setTextSize(context.getResources().getDimension(R.dimen.h43));

        dialog.show();

        return dialog;
    }

    /**
     * 时间选择器
     *
     * @param context
     * @param listener
     * @return
     */
    public static TimePickerView showTimePickerDialog(Context context, final OnSelecteChangeListener listener) {
        TimePickerView timePickerView = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        timePickerView.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR) + 100);//要在setTime 之前才有效果哦
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setLONGLayout(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(String date) {
                listener.onSelecte(date);
            }
        });
        //弹出时间选择器
        timePickerView.show();
        return timePickerView;
    }

    public interface OnSelecteChangeListener {
        void onSelecte(String s1);
    }

    private static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

}
