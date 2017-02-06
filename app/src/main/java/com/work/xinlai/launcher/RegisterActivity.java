package com.work.xinlai.launcher;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.util.Logger;
import com.work.xinlai.R;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.data.phone.SmsObserver;
import com.work.xinlai.util.MyStringUtils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/11/23.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.et_nick_name)
    private EditText nickeNameET;
    @ViewInject(R.id.et_pass_word)
    private EditText passWordET;
    @ViewInject(R.id.sendinvalatedate_tv)
    private TextView validateTV;
    private String userAccont;
    private String userPassword;

    private SmsObserver smsObserver;

    public static final int SMS_CODE = 2;
    public final int SMS_MOB_ONE = 1;
    public final int SMS_MOB_ZERO = 0;
    public final long THREAD_SLEEP = 1000;
    /**
     * 短信验证
     **/
    private String APPKEY = "197484172b1ce";
    private String APPSECRETE = "c27bb025efcc2ab07eca7e0b23508193";
    private int TIMER = 60;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle(this.getResources().getString(R.string.lgi_login_text));
        updataSDK();
        rigisterOS();
    }

    //注册观察者
    private void rigisterOS() {
        smsObserver = new SmsObserver(this, handler);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, smsObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注销观察者
        getContentResolver().unregisterContentObserver(smsObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**注销事件**/
        SMSSDK.unregisterAllEventHandler();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SMS_CODE) {
                String code = (String) msg.obj;
                passWordET.setText(code);
            }
            if (msg.what == SMS_MOB_ZERO) {
                validateTV.setText("重新发送(" + TIMER + ")");
                validateTV.setClickable(false);
            } else if (msg.what == SMS_MOB_ONE) {
                validateTV.setText("获取验证码");
                validateTV.setClickable(true);
                TIMER = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Logger.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        showToast("提交验证码成功");
                        /**进入密码设置界面,传入用户名**/
                        startActivity(ComfirmPasswordActivity.class);

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        showToast("正在获取验证码");
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    // 启动短信验证sdk
    public void updataSDK() {
        SMSSDK.initSDK(this, APPKEY, APPSECRETE);

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);

    }

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (MyStringUtils.isMatchLength(phoneNums, 11)
                && MyStringUtils.isMobileNO(phoneNums)) {
            return true;
        }
        return false;
    }


    private boolean isCheaked() {
        if (!TextUtils.isEmpty(userAccont) && !TextUtils.isEmpty(userPassword)) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        userAccont = nickeNameET.getText().toString();
        switch (v.getId()) {
            case R.id.bt_next:
                SMSSDK.submitVerificationCode("86", userAccont, passWordET.getText().toString());
                startActivity(ComfirmPasswordActivity.class);
                break;
            case R.id.sendinvalatedate_tv:

                //判断输入的手机号是否合法
                if (!judgePhoneNums(userAccont)) {
                    return;
                }
                //号码格式正确进行验证码发送
                SMSSDK.getVerificationCode("86", userAccont);
                Logger.e("YIN", userAccont);
                //改变文字并且显示倒计时
                validateTV.setText("重新发送(" + TIMER + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; TIMER > 0; TIMER--) {
                            handler.sendEmptyMessage(SMS_MOB_ZERO);
                            if (TIMER <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(THREAD_SLEEP);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(SMS_MOB_ONE);
                    }
                }).start();
                break;
        }
    }
}
