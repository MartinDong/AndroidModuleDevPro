package com.dong.lib.common.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dong.lib.common.R;

import java.lang.reflect.Field;

/**
 * <p>重写TabLayout，支持设置选项卡指示器宽度以及设置TabItem之间的间隔线</P>
 * Created by xiaoyulaoshi on 2018/3/14.
 */
public class CustomerTabLayout extends TabLayout {
    public CustomerTabLayout(Context context) {
        this(context, null);
    }

    public CustomerTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initView();
        reflex();
    }

    public void reflex() {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        post(() -> {
            try {
                //拿到tabLayout的mTabStrip属性
                LinearLayout mTabStrip = (LinearLayout) getChildAt(0);

                mTabStrip.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                mTabStrip.setDividerDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.line_v_top_bottom_inset));

                int dp10 = dp2px(10);

                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    //设置对象的访问权限，保证对private的属性的访问
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
                    params.width = width;
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);

                    //刷新控件，只会执行onDraw()，调requestLayout会执行onMeasure和onLayout，不会执行onDraw
                    tabView.invalidate();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * 将dp转成px使用
     *
     * @param value 目标单位
     * @return 转换后的单位
     */
    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
