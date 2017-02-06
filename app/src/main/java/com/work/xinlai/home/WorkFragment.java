package com.work.xinlai.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.bean.WorkGridItem;
import com.work.xinlai.component.GridViewLine;
import com.work.xinlai.component.TabFragment;
import com.work.xinlai.launcher.AboutCompanyActivity;
import com.work.xinlai.work.examimation.ExaminationActivity;
import com.work.xinlai.work.SignActivity;
import com.work.xinlai.baidu.IndoorMapActivity;
import com.work.xinlai.baidu.MapActivity;
import com.work.xinlai.baidu.StaticMapActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */

public class WorkFragment extends TabFragment implements View.OnClickListener {
    @ViewInject(R.id.sign_tv)
    private TextView signTV;
    @ViewInject(R.id.wifi_name_tv)
    private TextView locationTV;
    @ViewInject(R.id.wifi_tv)
    private TextView wifiTV;
    @ViewInject(R.id.work_grid)
    private GridViewLine workGR;
    private List<WorkGridItem> workItemList = new ArrayList<>();
    /**
     * 定义数据源
     **/
    String[] titles = new String[]
            {"考勤打卡", "审批", "团队", "地图", "公司", "室内定位"};
    int[] images = {
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher
    };
    private WorkGridAdapter workGridAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        workGridAdapter = new WorkGridAdapter();
    }

    private void initData() {
        for (int i = 0; i < images.length; i++) {
            WorkGridItem workGridItem = new WorkGridItem(images[i], titles[i]);
            workItemList.add(workGridItem);
        }
    }

   /* private void initListener() {
        signTV.setOnClickListener(this);
        locationTV.setOnClickListener(this);
        wifiTV.setOnClickListener(this);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        /**fragment 中的注入**/
        ViewUtils.inject(this, view);
        /**设置监听**/
        //initListener();
        showTitle("工作");
        workGR.setRowNum(4);
        workGR.setAllCount(images.length);
        workGR.setAdapter(workGridAdapter);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work_lin:
                ViewHodler viewHodler = (ViewHodler) v.getTag();
                WorkGridItem workGridItem = viewHodler.workGridItem;
                if (workGridItem.title.equals("考勤打卡")) {
                    startActivity(SignActivity.class);
                }
                if (workGridItem.title.equals("审批")) {
                    startActivity(ExaminationActivity.class);
                }
                if (workGridItem.title.equals("公司")) {
                    startActivity(AboutCompanyActivity.class);

                }
                if (workGridItem.title.equals("地图")) {
                    startActivity(MapActivity.class);
                }
                if (workGridItem.title.equals("室内定位")) {
                    startActivity(IndoorMapActivity.class);

                }
                if (workGridItem.title.equals("团队")) {
                    startActivity(StaticMapActivity.class);
                }
                break;

        }
    }

    private class ViewHodler {
        @ViewInject(R.id.grid_work_im)
        ImageView imImage;
        @ViewInject(R.id.grid_work_tv)
        TextView tvTitle;
        WorkGridItem workGridItem;
    }

    private class WorkGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return workItemList.size();
        }

        @Override
        public WorkGridItem getItem(int position) {
            return workItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler hodler;
            WorkGridItem workGridItem = getItem(position);
            /**convertView为空加载布局**/
            if (convertView == null) {
                hodler = new ViewHodler();
                convertView = mInflater.inflate(R.layout.item_grid_work, parent, false);
                ViewUtils.inject(hodler, convertView);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
            }

            hodler.imImage.setImageResource(workGridItem.imageId);
            hodler.tvTitle.setText(workGridItem.title);
            hodler.workGridItem = workGridItem;
            convertView.setOnClickListener(WorkFragment.this);
            return convertView;
        }
    }
}
