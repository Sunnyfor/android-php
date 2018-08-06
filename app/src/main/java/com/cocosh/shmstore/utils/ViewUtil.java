package com.cocosh.shmstore.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * 功能说明： 百分百设置组件大小（以苹果5C的屏幕比例进行设置大小）
 * 日期：	2015年10月11日
 * 开发者：张野
 * （半成品）
 */
public class ViewUtil {

    /*
     * 常量
     */
    public static final int RELATIVELAYOUT = 100; // 相对布局
    public static final int LINEARLAYOUT = 200; // 线性布局

    /*
     * 设计稿的尺寸：Iphone6的屏幕
     */
    public static final int WEIGHT = 1080;
    public static final int HEIGHT = 1920;

    /*
     * 华为手机虚拟键适配
     */

    // 1280*720
    public static final int HEIGHT_1280 = 1280;
    public static final int WIDTH_720 = 720;

    // 1920*1080
    public static final int HEIGHT_1920 = 1920;
    public static final int WIDTH_1080 = 1080;

    /**
     * <pre>
     * 功能说明：获取上下边距百分比高度
     * 日期：	2015年11月23日
     *
     * @param px
     * @return
     * </pre>
     */
    public static int getHeight(int px, Context context) {

        int screenWidth = DimensUtil.INSTANCE.getScreenWidth();
        int screenHeight = DimensUtil.INSTANCE.getScreenHeight();

        if (screenWidth == WIDTH_1080 && screenHeight != HEIGHT_1920) {
            screenHeight = HEIGHT_1920;
        }

        if (screenWidth == WIDTH_720 && screenHeight != HEIGHT_1280) {
            screenHeight = HEIGHT_1280;
        }

        // 将屏幕分成100份，判断输入的像素占屏幕的几份
        double iPhoneProportion = px / (double) (HEIGHT / (double) 100);
        int result = (int) ((screenHeight / (double) 100) * iPhoneProportion);
        return result;

    }

    /**
     * <pre>
     * 功能说明：获取左右边距百分比长度
     * 日期：	2015年11月23日
     * </pre>
     */
    public static int getWidth(int px, Context context) {
        double iPhoneProportion = px / (double) (WEIGHT / (double) 100);
        int result = (int) ((DimensUtil.INSTANCE.getScreenWidth() / (double) 100) * iPhoneProportion);
        return result;
    }

    /**
     * 功能说明： 转换成百分比px 有时候会有一点误差的 ,该方法用来减少误差
     * 据你当前的 宽高 计算 等比例的 长度
     * 日期：	2015年11月23日
     */
    public static int getWidth(int height, int width) {

        float proportion = (height / (float) width);

        return (int) (height / proportion);

    }

    /**
     * 功能说明：设置TextView文字的大小
     * 日期：	2015年11月23日
     */
    public static void setTextSize(TextView view, int px, Context context) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getWidth(px, context));
    }

    /**
     * 功能说明：设置EditText文字大小
     * 日期：	2015年11月23日
     */
    public static void setTextSize(EditText view, int px, Context context) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getWidth(px, context));
    }

    /**
     * 功能说明：设置Button文字大小
     * 日期：	2015年11月23日
     */
    public static void setTextSize(Button view, int px, Context context) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getWidth(px, context));
    }

    /**
     * 功能说明：设置RadioButton文字大小
     * 日期：	2015年11月23日
     */
    public static void setTextSize(RadioButton view, int px, Context context) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getWidth(px, context));
    }

    /**
     * 功能说明：设置组件大小方法
     * 日期：	2015年11月23日
     */
    public static void setViewSize(View view, int height, int width, Context context) {
        if (view != null) {
            if (view.getLayoutParams() != null) {
                if (height != 0) {
                    view.getLayoutParams().height = getHeight(height, context);
                }

                if (width != 0) {
                    view.getLayoutParams().width = getWidth(width, context);
                }

            } else {

                LayoutParams params = new LayoutParams(getWidth(width, context),
                        getHeight(height, context));
                view.setLayoutParams(params);

            }

        }
    }

    /**
     * 功能说明：设置布局大小方法
     * 日期：	2015年11月23日
     */
    public static void setViewSize(View view, int height, int width, int type, Context context) {
        if (view != null) {
            if (view.getLayoutParams() != null) {
                if (height != 0) {
                    view.getLayoutParams().height = getHeight(height, context);
                }

                if (width != 0) {
                    view.getLayoutParams().width = getWidth(width, context);
                }

            } else {

                if (type == LINEARLAYOUT) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            getWidth(width, context), getHeight(height, context));
                    view.setLayoutParams(params);
                }

                if (type == RELATIVELAYOUT) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            getWidth(width, context), getHeight(height, context));
                    view.setLayoutParams(params);
                }

//				LogUtil.e("LayoutParams对象为空，创建新的布局参数");
            }

        } else {
//			LogUtil.e("View对象为空，无法获取布局参数");
        }
    }

    /**
     * 功能说明：按比例设置组件左边距
     * 日期：	2015年11月23日
     */
    public static void setMarginLeft(View view, int margin, int type, Context context) {
        if (type == RELATIVELAYOUT) {

            if (view.getLayoutParams() == null) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.leftMargin = getWidth(margin, context);
                view.setLayoutParams(params);
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                params.leftMargin = getWidth(margin, context);
            }

        }
        if (type == LINEARLAYOUT) {

            if (view.getLayoutParams() == null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.leftMargin = getWidth(margin, context);
                view.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
                        .getLayoutParams();
                params.leftMargin = getWidth(margin, context);
            }

        }
    }

    /**
     * 功能说明：按比例设置组件右边距
     * 日期：	2015年11月23日
     */
    public static void setMarginRight(View view, int margin, int type, Context context) {
        if (type == RELATIVELAYOUT) {
            if (view.getLayoutParams() == null) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.rightMargin = getWidth(margin, context);
                view.setLayoutParams(params);
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                params.rightMargin = getWidth(margin, context);
            }
        }
        if (type == LINEARLAYOUT) {

            if (view.getLayoutParams() == null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.rightMargin = getWidth(margin, context);
                view.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
                        .getLayoutParams();
                params.rightMargin = getWidth(margin, context);
            }

        }
    }

    /**
     * 功能说明：按比例设置组件的四个边距(相同)
     * 日期：	2015年11月23日
     */
    public static void setMargin(View view, int margin, int type, Context context) {
        setMarginTop(view, margin, type, context);
        setMarginBottom(view, margin, type, context);
        setMarginLeft(view, margin, type, context);
        setMarginRight(view, margin, type, context);
    }

    /**
     * 功能说明：按比例设置组件的四个边距(不同)
     * 日期：	2015年11月23日
     */
    public static void setMargin(View view, int marginTB, int marginLR, int type, Context context) {
        if (marginTB != 0) {
            setMarginTop(view, marginTB, type, context);
            setMarginBottom(view, marginTB, type, context);
        }

        if (marginLR != 0) {
            setMarginLeft(view, marginLR, type, context);
            setMarginRight(view, marginLR, type, context);
        }
    }

    /**
     * 功能说明：按比例设置组件的四个边距(不同)
     * 日期：	2015年11月23日
     */
    public static void setMargin(View view, int marginT, int marginR,
                                 int marginB, int marginL, int type, Context context) {
        if (marginT != 0) {
            setMarginTop(view, marginT, type, context);
        }

        if (marginR != 0) {
            setMarginRight(view, marginR, type, context);
        }

        if (marginB != 0) {
            setMarginBottom(view, marginB, type, context);
        }

        if (marginL != 0) {
            setMarginLeft(view, marginL, type, context);
        }
    }

    /**
     * 功能说明：按比例设置组件的上边距
     * 日期：	2015年11月23日
     */
    public static void setMarginTop(View view, int margin, int type, Context context) {
        if (type == RELATIVELAYOUT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                    .getLayoutParams();
            params.topMargin = getHeight(margin, context);
        }
        if (type == LINEARLAYOUT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
                    .getLayoutParams();
            params.topMargin = getHeight(margin, context);
        }
    }

    /**
     * 功能说明：按比例设置组件的四个填充值(不同)
     * 日期：	2015年11月23日
     */
    public static void setPadding(View view, int paddingTB, int paddingLR, Context context) {

        if (paddingTB != 0) {
            setPaddingTop(view, paddingTB, context);
            setPaddingBottom(view, paddingTB, context);
        }

        if (paddingLR != 0) {
            setPaddingLeft(view, paddingLR, context);
            setPaddingRight(view, paddingLR, context);
        }
    }

    /**
     * 功能说明：按比例设置组件的下边距
     * 日期：	2015年11月23日
     */
    public static void setMarginBottom(View view, int margin, int type, Context context) {
        if (type == RELATIVELAYOUT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                    .getLayoutParams();
            params.bottomMargin = getHeight(margin, context);
        }
        if (type == LINEARLAYOUT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
                    .getLayoutParams();
            params.bottomMargin = getHeight(margin, context);
        }
    }

    /**
     * 功能说明：按比例设置组件四边填充值(不同)
     * 日期：2015年11月23日
     */
    public static void setPadding(View view, int paddingTop, int paddingRight,
                                  int paddingBottom, int paddingLeft, Context context) {

        if (paddingTop != 0) {
            setPaddingTop(view, paddingTop, context);
        }

        if (paddingRight != 0) {
            setPaddingRight(view, paddingRight, context);
        }

        if (paddingBottom != 0) {
            setPaddingBottom(view, paddingBottom, context);
        }

        if (paddingLeft != 0) {
            setPaddingLeft(view, paddingLeft, context);
        }
    }

    /**
     * 功能说明：按比例设置组件四边填充值(相同)
     * 日期：	2015年11月23日
     */
    public static void setPadding(View view, int padding, Context context) {
        setPaddingTop(view, padding, context);
        setPaddingBottom(view, padding, context);
        setPaddingLeft(view, padding, context);
        setPaddingRight(view, padding, context);
    }

    /**
     * 功能说明：按比例设置组件左填充值
     * 日期：	2015年11月23日
     */
    public static void setPaddingLeft(View view, int padding, Context context) {
        view.setPadding(getWidth(padding, context), view.getPaddingTop(),
                view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * 功能说明：按比例设置组件右填充值
     * 日期：	2015年11月23日
     */
    public static void setPaddingRight(View view, int padding, Context context) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
                getWidth(padding, context), view.getPaddingBottom());
    }

    /**
     * 功能说明：按比例设置组件上填充值
     * 日期：	2015年11月23日
     */
    public static void setPaddingTop(View view, int padding, Context context) {
        view.setPadding(view.getPaddingLeft(), getWidth(padding, context),
                view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * 功能说明：按比例设置组件下填充值
     * 日期：	2015年11月23日
     */
    public static void setPaddingBottom(View view, int padding, Context context) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
                view.getPaddingRight(), getWidth(padding, context));
    }

    /**
     * 功能说明：设置组件DrawablePadding属性
     * 日期：	2015年11月23日
     */
    public static void setDrawablePadding(TextView view, int padding, Context context) {
        view.setCompoundDrawablePadding(getWidth(padding, context));
    }

    public static void setIndicator(TabLayout tabs, int leftPx, int rightPx) {
        Class tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            params.leftMargin = leftPx;
            params.rightMargin = rightPx;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    public  static void reflex(final TabLayout tabLayout){
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = DimensUtil.INSTANCE.dip2px( 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
