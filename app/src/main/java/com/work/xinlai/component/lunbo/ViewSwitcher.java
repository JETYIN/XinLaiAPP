package com.work.xinlai.component.lunbo;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.work.xinlai.R;


public class ViewSwitcher extends FrameLayout implements ViewPagerTouchListener {
    //定义轮播的时间
    private static final long DEFAULT_SWITCH_INTERVAL = 2000L;

    private long mSwitchInterval = DEFAULT_SWITCH_INTERVAL;

    private DataSetObserver mDataSetObserver;
    private PagerAdapter mAdapter;

    private MLViewPager mVpContainer;
    private PageIndicator mPiSwitcher;

    private Handler mHandler;
    private Runnable mRunnable;


    /**
     * 在Framlayout中调用将ViewGroup传入
     **/
    public void setViewPageParent(ViewGroup parent) {
        mVpContainer.setNestedpParent(parent);
    }

    public ViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.compant_viewpager, this, true);

        mPiSwitcher = (PageIndicator) findViewById(R.id.pi_switcher);
        mVpContainer = (MLViewPager) findViewById(R.id.vp_container);
        mVpContainer.setTouchListener(this);

        mVpContainer.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                /**页面切换时设置小圆点对应的位置进行重绘**/
                mPiSwitcher.setCurrentPage(position);
            }
        });

        mHandler = new Handler();

        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                dataSetChanged();
            }
        };

        mRunnable = new Runnable() {
            @Override
            public void run() {
                showNextItem();
            }
        };
    }

    public void stop() {
        mHandler.removeCallbacks(mRunnable);
    }

    public void start() {
        if (mAdapter != null && mAdapter.getCount() > 1) {
            mHandler.postDelayed(mRunnable, mSwitchInterval);
        }
    }


    public void setAdapter(PagerAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;
        mVpContainer.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerDataSetObserver(mDataSetObserver);
        }

        dataSetChanged();
    }

    private void dataSetChanged() {
        stop();
        int count = mAdapter == null ? 0 : mAdapter.getCount();
        if (count > 0) {
            setVisibility(VISIBLE);
            mPiSwitcher.setNumPages(count);
            if (count > 1) {
                mPiSwitcher.setVisibility(VISIBLE);
                start();
            } else {
                mPiSwitcher.setVisibility(GONE);
            }
        } else {
            setVisibility(GONE);
        }
    }


    private void showNextItem() {
        showItem(1);
    }

    private void showItem(int shift) {
        stop();
        int count = mAdapter == null ? 0 : mAdapter.getCount();
        if (count == 0) {
            return;
        }

        int target = mVpContainer.getCurrentItem() + shift;
        if (target < 0) {
            target = count - 1;
        } else if (target >= count) {
            target = 0;
        }
        mVpContainer.setCurrentItem(target, true);
        start();
    }

    @Override
    public void onTouchMove() {
        stop();
    }

    @Override
    public void onTouchEnd() {
        start();
    }
}
