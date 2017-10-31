package com.namhofstadter.smoothtablayout_master;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Project: WidgetDemos
 * Author:  吴亚男
 * Version:  1.0
 * Date:    2017/10/9
 * Copyright notice:
 */

public class SmoothTabLayout extends RelativeLayout {
    private Context mContext;
    private LinearLayout bot;
    private RelativeLayout bar;
    private NoScrollHorizontalScrollView top;
    private LinearLayout topInner;
    private int paddingTopAndBot = 0;
    private int paddingLeftAndRight = 0;
    private int textSize = 0;
    private int bgShape = R.drawable.shape_tab_item_bg;
    private int normalColor = Color.BLACK;
    private int selectColor = Color.WHITE;

    public SmoothTabLayout(Context context) {
        this(context, null);
    }

    public SmoothTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SmoothTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        paddingTopAndBot = dp2px(10);
        paddingLeftAndRight = dp2px(10);
        textSize = 14;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setPaddingTopAndBot(int paddingTopAndBot) {
        this.paddingTopAndBot = paddingTopAndBot;
    }

    public void setPaddingLeftAndRight(int paddingLeftAndRight) {
        this.paddingLeftAndRight = paddingLeftAndRight;
    }

    public void setBgShape(int resId) {
        this.bgShape = resId;
    }

    public void setSelectorColor(int normalColor, int selectColor) {
        this.normalColor = normalColor;
        this.selectColor = selectColor;
    }

    public void setViewPager(final ViewPager viewPager) {
        bot = new LinearLayout(mContext);
        LayoutParams botParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bot.setLayoutParams(botParams);
        this.addView(bot);
        PagerAdapter adapter = viewPager.getAdapter();
        final int tabCount = adapter.getCount();
        bot.setWeightSum(tabCount);
        final int screenWidth = getScreenWidth();
        int barWidth = (int) (screenWidth * 1f / tabCount);
        //添加底部tab
        for (int i = 0; i < tabCount; i++) {
            String title = adapter.getPageTitle(i).toString();
            TextView view = new TextView(mContext);
            view.setText(title);
            view.setTextColor(normalColor);
            view.setTextSize(textSize);
            view.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(barWidth, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.weight = 1;
            view.setLayoutParams(params);
            bot.addView(view);
            final int clickPos = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(clickPos);
                }
            });
        }
        //添加滑块
        bar = new RelativeLayout(mContext);
        bar.setBackgroundResource(bgShape);
        LayoutParams barParams = new LayoutParams(barWidth - paddingLeftAndRight, ViewGroup.LayoutParams.MATCH_PARENT);
        barParams.topMargin = paddingTopAndBot;
        barParams.bottomMargin = paddingTopAndBot;
        bar.setLayoutParams(barParams);
        this.addView(bar);

        top = new NoScrollHorizontalScrollView(mContext);
        top.setHorizontalScrollBarEnabled(false);
        LayoutParams topParams = new LayoutParams(barWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        top.setLayoutParams(topParams);
        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(barWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        topInner = new LinearLayout(mContext);
        topInner.setLayoutParams(params);

        //向滑块中增加上层tab
        for (int i = 0; i < tabCount; i++) {
            String title = adapter.getPageTitle(i).toString();
            TextView view = new TextView(mContext);
            view.setText(title);
            view.setTextSize(textSize);
            view.setTextColor(selectColor);
            view.setGravity(Gravity.CENTER);
            //每一个tab的宽度都与滑块的宽度相同
            LinearLayout.LayoutParams topTabParams = new LinearLayout.LayoutParams(barWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(topTabParams);
            topInner.addView(view);
        }
        top.addView(topInner);
        bar.addView(top);
        bar.setTranslationX(paddingLeftAndRight * 1f / 2);
        //控制滑块的滑动
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float dia = paddingLeftAndRight * 1f / 2 + screenWidth * 1f * position / tabCount + positionOffset * screenWidth * 1f / tabCount;
                if (positionOffset % 1 != 0) {
                    //滑块移动
                    bar.setTranslationX(dia);
                }
                //滑块内容反向移动
                topInner.setTranslationX(-dia);
            }

            @Override
            public void onPageSelected(int position) {
                bar.setTranslationX(screenWidth * 1f * position / tabCount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    private class NoScrollHorizontalScrollView extends HorizontalScrollView {

        public NoScrollHorizontalScrollView(Context context) {
            super(context);
        }

        public NoScrollHorizontalScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public NoScrollHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return false;
        }
    }
}
