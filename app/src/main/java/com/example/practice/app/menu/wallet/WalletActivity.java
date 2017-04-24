package com.example.practice.app.menu.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.example.practice.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ChenTao on 2017/4/23.
 */

public class WalletActivity extends AppCompatActivity{
    ViewPager viewPager;
    int[] imagesid;
    List<ImageView> imagesList;
    MyPagerAdapter vpAdapter;
    LinearLayout ll_point;
    TextView tv_title;
    String[] titles;
    String[] mTitleStrs;
    int[] mDrawableIds;
    private GridView gv_home;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.my_wallet);
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        tv_title = (TextView) findViewById(R.id.tv_title);
        gv_home=(GridView) findViewById(R.id.gv_home);
        //九宫格控件设置数据适配器

        //初始化数据

        titles = new String[]{"第一个小人在干嘛","第二幅图","第三幅图，哈哈","第四幅图，呵呵","第五幅图，哈哈"};
        mTitleStrs = new String[]{
                "手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级设置","设置中心"
        };
        mDrawableIds=new int[]{
                R.drawable.home_safe,R.drawable.home_callmsgsafe,
                R.drawable.home_apps,R.drawable.home_taskmanager,
                R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings,
        };
        imagesid = new int[]{R.drawable.tu1,R.drawable.tu2,R.drawable.tu3,R.drawable.tu4,R.drawable.tu5};

        imagesList = new ArrayList<ImageView>();
        gv_home.setAdapter(new MyAdapter());
        ImageView iv;  //滚动的图片
        View point;    // 小点
        LinearLayout.LayoutParams params;
        for(int i=0;i<imagesid.length;i++){
            iv = new ImageView(this);
            iv.setImageResource(imagesid[i]);
            imagesList.add(iv);

            point = new View(this);
            point.setBackgroundResource(R.drawable.selector_point);

            if(i == 0){
                point.setEnabled(true);
            }else{
                point.setEnabled(false);
            }

            params = new LayoutParams(5, 5);
            params.rightMargin = 10;
            //将创建的点加入到 ll_point
            ll_point.addView(point, params);

        }

        //处理页面图片的显示
        viewPager = (ViewPager) findViewById(R.id.vpager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //选中viewPager中的视图就会调用
                int realposition = position%imagesList.size();

                //设置滚动的title
                tv_title.setText(titles[realposition]);

                //设置对应的小点被选中
                for(int i=0;i<ll_point.getChildCount();i++){

                    View point = ll_point.getChildAt(i);

                    point.setEnabled(realposition == i);
					/*
					if(position == i){
						point.setEnabled(true);
					}else{
						point.setEnabled(false);
					}
					*/
                }

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                //手指点中并移动的时候调用

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面变化的时候调用

            }
        });

        vpAdapter = new MyPagerAdapter();

        //也是通过PagerAdapter来显示条目的
        viewPager.setAdapter(vpAdapter);

        //为了能前后都无限滚动，将初始位置设置为中间
        //viewPager.setCurrentItem(Integer.MAX_VALUE/2 - ((Integer.MAX_VALUE/2) % imagesList.size()));


        //设置自动的滚动

        new Thread(){
            public void run() {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //将当前的位置向前移动一个
                //在子线程中，调用 Activity的方法runOnUiThread(Runnable)可以刷新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                });

            };
        }.start();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(getApplicationContext(),R.layout.gridview_item , null);

            TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
            ImageView iv_icon=(ImageView)view.findViewById(R.id.iv_icon);


            tv_title.setText(mTitleStrs[position]);
            iv_icon.setBackgroundResource(mDrawableIds[position]);

            return view;
        }

    }
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //获取条目的数量   伪无限，其实还是有限
            return Integer.MAX_VALUE;
            //return imagesList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            //判断view是否相等（相等就复用，不相等就销毁）
            //判断对象是否是同一个对象（固定写法）
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            System.out.println(position);

            int realposition = position%imagesList.size();

            System.out.println(realposition);

            //获得想要在viewPager中显示的内容
            ImageView iv = imagesList.get(realposition);

            //显示到视图上
            container.addView(iv);

            //返回的对象只是用来比较保存或者销毁
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            //int realposition = position%imagesList.size();
            //System.out.println("destroyItem的条目："+position);
            container.removeView((View) object);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
