package com.work.xinlai.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.component.TabFragment;
import com.work.xinlai.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.TabLayout
 */
public class HomeActivity extends FragmentActivity {
    private final String TAG_NET = this.getClass().getSimpleName();
    private ListAdapter mListAdapter;
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.tablayout)
    private TabLayout mTabLayout;

    //标题
    private final String TAB_TITLES[] = new String[]{"消息", "工作", "公司", "我的"};
    //图片选择
    private final int[] TAB_IMAGE = new int[]{
            R.drawable.tab_weixin_selector,
            R.drawable.tab_contacts_selector,
            R.drawable.tab_find_selector,
            R.drawable.tab_me_selector};
    private static final int VIEWPAGER_LIMIT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewUtils.inject(this);
        biuildData();
    }

    private void biuildData() {
        //实例化adapter
        mListAdapter = new ListAdapter(getSupportFragmentManager());
        //向适配器中list添加数据
        mListAdapter.addFragment(new WifiFragment());
        mListAdapter.addFragment(new WorkFragment());
        mListAdapter.addFragment(new CompanyFragment());
        mListAdapter.addFragment(new MyFragment());
        //设置viewpager界限
        mViewPager.setOffscreenPageLimit(VIEWPAGER_LIMIT);
        mViewPager.setAdapter(mListAdapter);

        setTabs(mTabLayout, this.getLayoutInflater(), TAB_TITLES, TAB_IMAGE);

        //绑定viewpager
        //mTabLayout.setupWithViewPager(mViewPager);
        //mTabLayout.setTabsFromPagerAdapter(mListAdapter);
        /**添加监听**/
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

       /* //页面切换需要进行网络请求
        mViewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mListAdapter.refresh();
            }
        });*/

    }

    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, String[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.item_bottom_tablayout, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_tab);
            tvTitle.setText(tabTitlees[i]);
            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);

            tab.setCustomView(view);

            tabLayout.addTab(tab);

        }
    }

/**退为后台**/
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private class ListAdapter extends FragmentPagerAdapter {
        List<TabFragment> fragments = new ArrayList<>();

        public ListAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 添加fragment数据
         **/
        public void addFragment(TabFragment tanFragment) {
            fragments.add(tanFragment);
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        HttpUtils.cancelAll(TAG_NET);
    }
}
