package com.work.xinlai.home;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.component.SlideDeleteListView;
import com.work.xinlai.component.TabFragment;
import com.work.xinlai.data.FileManger;
import com.work.xinlai.util.WifiUtils;
import com.work.xinlai.work.SlideListActivity;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */
public class WifiFragment extends TabFragment implements View.OnClickListener, SlideDeleteListView.RemoveListener {
    private ListAdapter mAdapter;
    private List<ScanResult> dataList;
    private ScanResult mScanResult;


    @ViewInject(R.id.list_wifi)
    private SlideDeleteListView listView;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_wifi;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = WifiUtils.getWifiList(getActivity());
        mAdapter = new ListAdapter();
       /* *test file**/
        File file = FileManger.getXinLaiDir();
        String path = file.getAbsolutePath();
        File newFile = new File(path + "/" + "user");

        if (!newFile.exists()) {
            newFile.mkdir();
        }
       // FileManger.deleteFile(new File(FileManger.XIN_LAI));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ViewUtils.inject(this, view);
        listView.setAdapter(mAdapter);
        //注册删除监听
        listView.setRemoveListener(this);
        /**加载完成后设置标题，返回按钮影藏**/
        showTitle("可用wifi");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_parent:
                WifiHodler hodler = (WifiHodler) v.getTag();
                ScanResult scanResult = hodler.scanResult;
                startActivity(SlideListActivity.class);
                Toast.makeText(getActivity(), scanResult.SSID, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 删除item接口
     **/
    @Override
    public void removeItem(SlideDeleteListView.RemoveDirection direction, int position) {
        /**删除滑动项的数据对象**/
        dataList.remove(mAdapter.getItem(position));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新完成后需要自己写接口操作，向后太请求数据源成功后隐藏刷新-swipeRefreshLayout.setRefreshing(false);
     **/
    private class ListAdapter extends BaseAdapter {
        /**
         * 返回数组大小
         **/
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public ScanResult getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mScanResult = getItem(position);
            WifiHodler hodler;
            /**convertView为空加载布局**/
            if (convertView == null) {
                //convertview为空进行hodler创建
                hodler = new WifiHodler();
                //创建view的实例
                convertView = mInflater.inflate(R.layout.item_fragment_wifi, parent, false);
                //获取到hodler以及view实例进行注解
                ViewUtils.inject(hodler, convertView);
                //为viewholder设置对应的tag
                convertView.setTag(hodler);

            } else {
                //convertview不为空获取对应的hodler
                hodler = (WifiHodler) convertView.getTag();
            }
            //从hodler中获取对应的view组件进行值得设置
            hodler.textView.setText(mScanResult.SSID);
            //将没一个对象保存下来
            hodler.scanResult = mScanResult;
            convertView.setOnClickListener(WifiFragment.this);
            return convertView;
        }
    }

    private class WifiHodler {
        @ViewInject(R.id.tv_wifi)
        TextView textView;
        ScanResult scanResult;
    }
}
