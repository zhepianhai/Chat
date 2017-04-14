package zph.zhjx.com.chat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.fragment.LiuYanFragment;
import zph.zhjx.com.chat.fragment.SearchLiuYanFragment;
import zph.zhjx.com.chat.view.TopIndicator;

public class LiuYanActivity extends BaseActivity implements View.OnClickListener {
    private View headview;
    private LinearLayout back;
    private TextView title;


    private TopIndicator topSelete;
    private ViewPager vp;
    private List<Fragment> lists=new ArrayList<Fragment>();
    private Context context;
    private MyLiuYanPagerAdapter adapter;

    static {
        try {
            System.loadLibrary("geosot");
            String str=System.getProperty("java.library.path");
            Log.i("TAG", "加载成功...");
        } catch (UnsatisfiedLinkError e) {
            Log.i("TAG", "加载失败..."+e.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liu_yan);
        initView();
        setstatusbarcolor();
        initViewPager();
        initTitleDate();
        setLists();
    }
    private void initTitleDate() {
        topSelete.setTopText(this,"留言","搜索");
    }
    private void initViewPager() {
        adapter=new MyLiuYanPagerAdapter(getSupportFragmentManager(),lists);
        vp.setAdapter(adapter);
        topSelete.setOnTopIndicatorListener(new TopIndicator.OnTopIndicatorListener() {
            @Override
            public void onIndicatorSelected(int index) {
                vp.setCurrentItem(index);
            }
        });
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                topSelete.setTabsDisplay(context, position);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });
    }

    private void initView() {
        topSelete= (TopIndicator) findViewById(R.id.topindicator_all);
        vp= (ViewPager) findViewById(R.id.viewpager_all);
        headview=findViewById(R.id.headview1);
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        back.setOnClickListener(this);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        title.setText("网格留言"+"");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headview_left_layout:
                this.finish();
                break;
        }
    }

    class MyLiuYanPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> lists;
        public MyLiuYanPagerAdapter(FragmentManager fm, List<Fragment> lists) {
            super(fm);
            this.lists=lists;
        }
        @Override
        public Fragment getItem(int position) {
            return lists.get(position);
        }
        @Override
        public int getCount() {
            return lists.size();
        }

    }


    /***
     * 添加fragment的,如果放在OnResume中的话，就会在第二次进入的时候出现重复添加的错误
     * */
    private void setLists() {
        lists.add(new LiuYanFragment());
        lists.add(new SearchLiuYanFragment());
        adapter.notifyDataSetChanged();
    }










}
