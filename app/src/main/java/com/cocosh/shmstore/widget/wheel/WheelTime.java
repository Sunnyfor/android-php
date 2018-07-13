package com.cocosh.shmstore.widget.wheel;

import android.content.Context;
import android.view.View;

import com.cocosh.shmstore.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.cocosh.shmstore.widget.wheel.TimePickerView.Type;

/**
 * Desc:
 * Author: chencha
 * Date: 16/11/4
 */

public class WheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View view;
    private WheelView2 wv_year;
    private WheelView2 wv_month;
    private WheelView2 wv_day;

    private Type type;
    public static final int DEFULT_START_YEAR = 1900;
    public static final int DEFULT_END_YEAR = 2100;
    private int startYear = DEFULT_START_YEAR;
    private int endYear = DEFULT_END_YEAR;

    private int startMonth;

    public WheelTime(View view) {
        super();
        this.view = view;
        type = Type.ALL;
        setView(view);
    }

    public WheelTime(View view, Type type) {
        super();
        this.view = view;
        this.type = type;
        setView(view);
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0);
    }

    public void setPicker(int year, final int month, final int day, int h, int m) {
        Calendar calendar = Calendar.getInstance();
//        endYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH) + 1;
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        Context context = view.getContext();
        // 年
        wv_year = (WheelView2) view.findViewById(R.id.year);
        wv_year.setLabel("年");
        wv_year.setAdapter(new NumericWheelAdapter2(startYear, endYear));// 设置"年"的显示数据
        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据

        // 月
        wv_month = (WheelView2) view.findViewById(R.id.month);
        wv_month.setLabel("月");
        wv_month.setAdapter(new NumericWheelAdapter2(1, 12));
        wv_month.setCurrentItem(month);

        // 日
        wv_day = (WheelView2) view.findViewById(R.id.day);
        wv_day.setLabel("日");
        // 判断大小月及是否闰年,用来确定"日"的数据

        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter2(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter2(1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter2(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter2(1, 28));
        }
        wv_day.setCurrentItem(day - 1);

        // 添加"年"监听
        OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                // 判断大小月及是否闰年,用来确定"日"的数据
                int maxItem = 30;
                if (endYear == year_num) {
                    wv_month.setAdapter(new NumericWheelAdapter2(1, startMonth));
                    if (list_big.contains(String.valueOf(month))) {
                        wv_day.setAdapter(new NumericWheelAdapter2(1, day));
                        maxItem = day;
                    } else if (list_little.contains(String.valueOf(month))) {
                        wv_day.setAdapter(new NumericWheelAdapter2(1, day));
                        maxItem = day;
                    } else {
                        if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0) {
                            wv_day.setAdapter(new NumericWheelAdapter2(1, day));
                            maxItem = day;
                        } else {
                            wv_day.setAdapter(new NumericWheelAdapter2(1, day));
                            maxItem = day;
                        }
                    }
                } else {
                    wv_month.setAdapter(new NumericWheelAdapter2(1, 12));
                    if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                        wv_day.setAdapter(new NumericWheelAdapter2(1, 31));
                        maxItem = 31;
                    } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                        wv_day.setAdapter(new NumericWheelAdapter2(1, 30));
                        maxItem = 30;
                    } else {
                        if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0) {
                            wv_day.setAdapter(new NumericWheelAdapter2(1, 29));
                            maxItem = 29;
                        } else {
                            wv_day.setAdapter(new NumericWheelAdapter2(1, 28));
                            maxItem = 28;
                        }
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }
            }
        };
        // 添加"月"监听
        OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;
                int maxItem = 30;
                if (startMonth == month_num) {
                    // 判断大小月及是否闰年,用来确定"日"的数据
                    if (list_big.contains(String.valueOf(month_num))) {
                        wv_day.setAdapter(new NumericWheelAdapter2(1, day));
                        maxItem = day;
                    } else if (list_little.contains(String.valueOf(month_num))) {
                        wv_day.setAdapter(new NumericWheelAdapter2(1, day));
                        maxItem = day;
                    } else {
                        if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
                                .getCurrentItem() + startYear) % 100 != 0)
                                || (wv_year.getCurrentItem() + startYear) % 400 == 0) {
                            wv_day.setAdapter(new NumericWheelAdapter2(1, day));
                            maxItem = day;
                        } else {
                            wv_day.setAdapter(new NumericWheelAdapter2(1, day));
                            maxItem = day;
                        }
                    }
                } else {
                    // 判断大小月及是否闰年,用来确定"日"的数据
                    if (list_big.contains(String.valueOf(month_num))) {
                        wv_day.setAdapter(new NumericWheelAdapter2(1, 31));
                        maxItem = 31;
                    } else if (list_little.contains(String.valueOf(month_num))) {
                        wv_day.setAdapter(new NumericWheelAdapter2(1, 30));
                        maxItem = 30;
                    } else {
                        if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
                                .getCurrentItem() + startYear) % 100 != 0)
                                || (wv_year.getCurrentItem() + startYear) % 400 == 0) {
                            wv_day.setAdapter(new NumericWheelAdapter2(1, 29));
                            maxItem = 29;
                        } else {
                            wv_day.setAdapter(new NumericWheelAdapter2(1, 28));
                            maxItem = 28;
                        }
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

            }
        };
        wv_year.setOnItemSelectedListener(wheelListener_year);
        wv_month.setOnItemSelectedListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
//        int textSize = 4;
//        switch (type) {
//            case ALL:
//                textSize = textSize * 3;
//                break;
//            case YEAR_MONTH_DAY:
//                textSize = textSize * 4;
//                break;
//            case HOURS_MINS:
//                textSize = textSize * 4;
//                wv_year.setVisibility(View.GONE);
//                wv_month.setVisibility(View.GONE);
//                wv_day.setVisibility(View.GONE);
//                break;
//            case MONTH_DAY_HOUR_MIN:
//                textSize = textSize * 3;
//                wv_year.setVisibility(View.GONE);
//                break;
//            case YEAR_MONTH:
//                textSize = textSize * 4;
//                wv_day.setVisibility(View.GONE);
//        }
        wv_day.setTextSize(context.getResources().getDimension(R.dimen.h43));
        wv_month.setTextSize(context.getResources().getDimension(R.dimen.h43));
        wv_year.setTextSize(context.getResources().getDimension(R.dimen.h43));

    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
    }

    /**
     * 获取选择的日期
     * @return
     */
    public String getTime() {
        StringBuffer sb = new StringBuffer();

        sb.append((wv_year.getCurrentItem() + startYear)).append("-");
        if ((wv_month.getCurrentItem() + 1) > 9) {
            sb.append((wv_month.getCurrentItem() + 1)).append("-");
        } else {
            sb.append("0" + (wv_month.getCurrentItem() + 1)).append("-");
        }
        if ((wv_day.getCurrentItem() + 1) > 9) {
//            sb.append((wv_day.getCurrentItem() + 1)).append("-");
            sb.append((wv_day.getCurrentItem() + 1));
        } else {
            sb.append("0" + (wv_day.getCurrentItem() + 1));
        }
        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}
