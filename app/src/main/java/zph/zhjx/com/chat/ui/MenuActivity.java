package zph.zhjx.com.chat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.adapter.SectionsPagerAdapter;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.fragment.ChatFragment;
import zph.zhjx.com.chat.fragment.GeoSotFragment;
import zph.zhjx.com.chat.fragment.MeFragment;
import zph.zhjx.com.chat.fragment.PersonFragment;

public class MenuActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private BottomNavigationBar bottomNavigationBar;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initview();
        setstatusbarcolor();
        setBottomNavigationBar();
        initviewpager();

    }

    private void setBottomNavigationBar() {
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                ).setInActiveColor(R.color.colorHei).setBarBackgroundColor("#FFFFFF").setInActiveColor(R.color.colorline)
        ;


        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.chat, "消息").setActiveColorResource(R.color.colorhome))
                .addItem(new BottomNavigationItem(R.mipmap.lianxiren, "联系人").setActiveColorResource(R.color.colorhome))
                .addItem(new BottomNavigationItem(R.mipmap.geosot, "网格").setActiveColorResource(R.color.colorhome))
                .addItem(new BottomNavigationItem(R.mipmap.setting, "设置").setActiveColorResource(R.color.colorhome))
//                .setFirstSelectedPosition(0)
                .initialise();
    }
    private void initviewpager() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new ChatFragment());
        fragments.add(new PersonFragment());
        fragments.add(new GeoSotFragment());
        fragments.add(new MeFragment());

        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }
    private void initview() {
        bottomNavigationBar= (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
