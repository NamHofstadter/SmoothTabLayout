package com.namhofstadter.smoothtablayout;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namhofstadter.smoothtablayout_master.SmoothTabLayout;

public class MainActivity extends AppCompatActivity {

    private SmoothTabLayout tabs;
    private ViewPager vp;
    private String[] titles = {"林黛玉", "董小宛", "柳如是", "李玉环"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = (SmoothTabLayout) findViewById(R.id.tab);
        vp = (ViewPager) findViewById(R.id.vp);

        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                TextView view = new TextView(container.getContext());
                view.setText("这是第" + position + "个界面");
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        tabs.setViewPager(vp);
    }
}
