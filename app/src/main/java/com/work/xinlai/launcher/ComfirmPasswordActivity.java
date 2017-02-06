package com.work.xinlai.launcher;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.home.HomeActivity;

/**
 * Created by Administrator on 2016/11/30.
 */
public class ComfirmPasswordActivity extends BaseActivity implements OnClickListener {
    @ViewInject(R.id.password_one_et)
    private EditText passWordET;
    @ViewInject(R.id.password_two_et)
    private EditText passWordETCM;
    @ViewInject(R.id.comfiem_bt)
    private Button comfiemBT;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comfiem_bt:
                startActivity(HomeActivity.class);
                break;
        }
    }
}
