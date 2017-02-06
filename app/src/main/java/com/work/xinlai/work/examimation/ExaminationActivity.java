package com.work.xinlai.work.examimation;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.bean.WorkGridItem;
import com.work.xinlai.component.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class ExaminationActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.wait_me_tv)
    private TextView waitTV;
    @ViewInject(R.id.my_send_tv)
    private TextView mySendTV;
    @ViewInject(R.id.send_to_mt_tv)
    private TextView sendMeTV;
    @ViewInject(R.id.examination_gv)
    private GridView mGridView;
    private List<WorkGridItem> workItemList = new ArrayList<>();
    private WorkGridAdapter workGridAdapter;
    String[] titles = new String[]
            {"请假", "报销", "出差", "加班"};
    int[] images = {
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher
    };

    private void initData() {
        for (int i = 0; i < images.length; i++) {
            WorkGridItem workGridItem = new WorkGridItem(images[i], titles[i]);
            workItemList.add(workGridItem);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_examination;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle("审批");
        initData();
        workGridAdapter = new WorkGridAdapter();
        mGridView.setAdapter(workGridAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work_lin:
                WorkGridItem workGridItem = (WorkGridItem) v.getTag();
                if (workGridItem.title.equals("请假")) {
                    startActivity(AskForLeaveActivity.class);
                }
                break;
        }
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
            WorkGridItem workGridItem = getItem(position);
            /**convertView为空加载布局**/
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_grid_work, parent, false);
            }
            ImageView imImage = (ImageView) convertView.findViewById(R.id.grid_work_im);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.grid_work_tv);
            imImage.setImageResource(workGridItem.imageId);
            tvTitle.setText(workGridItem.title);
            convertView.setTag(workGridItem);
            convertView.setOnClickListener(ExaminationActivity.this);
            return convertView;
        }
    }
}
