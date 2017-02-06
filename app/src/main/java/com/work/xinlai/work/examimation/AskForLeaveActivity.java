package com.work.xinlai.work.examimation;

import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.component.ListDialog;
import com.work.xinlai.util.Utils;

/**
 * Created by Administrator on 2016/12/12.
 */
public class AskForLeaveActivity extends BaseActivity implements View.OnClickListener, ListDialog.ListDialogClick {
    @ViewInject(R.id.type_content_tv)
    private TextView typeTV;
    @ViewInject(R.id.start_time_tv)
    private TextView startTimeTV;
    @ViewInject(R.id.end_time_tv)
    private TextView endTimeTV;
    @ViewInject(R.id.leave_day_et)
    private EditText dayNumET;
    @ViewInject(R.id.leave_reason_et)
    private EditText dyaReasonET;
    private ListDialog listDialog;

    @ViewInject(R.id.ask_type_rl)
    private RelativeLayout relativeLayout;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_askforleave;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle("请假");
        Utils.setDataPickTime(startTimeTV);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**type**/
            case R.id.ask_type_rl:
            case R.id.type_content_tv:
                listDialog = new ListDialog(AskForLeaveActivity.this);
                listDialog.rigisterListDialogClick(this);
                listDialog.show();
                break;
            /**开始时间**/
            case R.id.start_time_rl:
            case R.id.start_time_tv:
                selectTime(startTimeTV);
                break;
            /**结束时间**/
            case R.id.end_time_rl:
            case R.id.end_time_tv:
                selectTime(endTimeTV);
                break;
            /**提交**/
            case R.id.commit_tv:
                break;
        }
    }

    private void selectTime(TextView textView) {
        Utils.datePick(this, textView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != listDialog) {
            listDialog.unrigisterListDialogClick();
        }
    }

    @Override
    public void OnListDialogItem(String str) {
        typeTV.setText(str);
        listDialog.dismiss();
    }
}
