# SmoothTabLayout

#### 如有帮助，请加星支持！

## 0.效果图
<center><img src="http://oueeb3f1q.bkt.clouddn.com/SmoothTabLayout1.gif"/></center>

## 1.项目的build.gradle中
```grovvy
compile 'com.namhofstadter.smoothtablayout:smoothTabLayout-master:1.0.0'
```

## 2.布局文件中
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    >

    <com.namhofstadter.smoothtablayout_master.SmoothTabLayout
        android:id="@+id/tab"
        android:layout_width="match_parent"  //必须填充整个屏幕的宽度，本view和父/爷/祖View都必须充满这个屏幕的宽度！！！
        android:layout_height="40dp"
        android:background="#0f0"/>

    <android.support.v4.view.ViewPager
        android:id="@+id/vp"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
</LinearLayout>
```

## 3.activity中使用
```java
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
```

## 4.其他方法

+ setTextSize(int textSize):设置tab的字体大小
+ setPaddingTopAndBot(int paddingTopAndBot)：设置背景的上下padding
+ setPaddingLeftAndRight(int paddingLeftAndRight)：设置背景的左右padding
+ setBgShape(int resId)：设置背景的资源id，必须是shape的xml文件的id
