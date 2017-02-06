package com.work.xinlai.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.home.HomeActivity;
import com.work.xinlai.R;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.data.db.DataHelper;

/**
 * Created by Administrator on 2016/11/23.
 */
public class LoginRegisterActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.et_account)
    private EditText userET;
    @ViewInject(R.id.et_password)
    private EditText passwordET;

    private String userAccont;
    private String userPassword;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle(this.getResources().getString(R.string.lgi_login));
        hideLiftImage();
    }

    private void getEdit() {
        userAccont = userET.getText().toString();
        userPassword = passwordET.getText().toString();
    }

    /**
     * 登录验证-登录成功保存密码
     **/
    private boolean isChecked() {
        if (userAccont.equals("111111") && userPassword.equals("111111")) {
            return true;
        }
        return false;
    }

    private void saveUserInfo(String userJson) {
        SharedPreferences sp = DataHelper.getSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        //editor.putString(AccountInfo.SP_USER_INFO, userJson);
        editor.putString("user", userAccont);
        editor.putString("password", userPassword);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                getEdit();
                if (isChecked()) {
                    /**进入主页**/
                    LoginRegisterActivity.this.finish();
                    startActivity(HomeActivity.class);
                } else {
                    showToast("用户名或密码不正确");
                }
                break;
            case R.id.tv_forget_password:
                break;
            case R.id.tv_regsiter:
                startActivity(new Intent(LoginRegisterActivity.this, RegisterActivity.class));
                break;
        }
    }
}
