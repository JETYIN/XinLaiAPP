package com.work.xinlai.work;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.baidu.LocationService;
import com.work.xinlai.bean.LatLngWrapper;
import com.work.xinlai.util.Logger;
import com.work.xinlai.component.AvatarView;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.launcher.XinlaiApplication;
import com.work.xinlai.data.db.DataHelper;
import com.work.xinlai.util.MyStringUtils;
import com.work.xinlai.util.RegUtils;
import com.work.xinlai.util.WifiUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/28.
 */
public class SignActivity extends BaseActivity implements View.OnClickListener, BaiduMap.OnMapStatusChangeListener {
    private final static String WORK_MORNING = "9:30";
    private final static int WORK_MORNING_TYPE = 0;
    private final static String WORK_EVENING = "17:30";
    private final static int WORK_EVENING_TYPE = 1;
    private final static String GPS_STATUS = "GPS";
    public String TAG = "SignActivity";
    @ViewInject(R.id.start_time_bt)
    private Button dateBT;
    @ViewInject(R.id.image_1)
    private AvatarView userIM;
    @ViewInject(R.id.sign_tv)
    private TextView signTV;
    @ViewInject(R.id.wifi_tv)
    private TextView wifiTV;
    @ViewInject(R.id.time_tv)
    private TextView timerTV;
    private static final int MSG_JUMP = 0;
    private static final int UPDATE_UI = 1;
    private static final int UPDATE_WORKOUT_UI = 2;
    private static final int UODATE_SIGN = 3;
    @ViewInject(R.id.sign_time_tv)
    private TextView signTimeTV;
    @ViewInject(R.id.wifi_name_tv)
    private TextView wifiNameTV;
    @ViewInject(R.id.sign_status_tv)
    private TextView signStatusTV;
    /**
     * 3个父布局
     **/
    @ViewInject(R.id.sign_work_rl)
    private RelativeLayout signWorkRL;
    @ViewInject(R.id.sign_workout_rl)
    private RelativeLayout signWorkOutRL;
    @ViewInject(R.id.sign_rl)
    private RelativeLayout signBTRL;


    @ViewInject(R.id.sign_timeout_tv)
    private TextView workoutTimeTV;
    @ViewInject(R.id.wifi_nameout_tv)
    private TextView wifiWorkOutTV;
    @ViewInject(R.id.sign_statusout_tv)
    private TextView statusWorkOutTV;
    @ViewInject(R.id.update_sign_tv)
    private TextView updataSignTV;
    /**
     * 后天添加的wifi名,在外勤打卡时，获取物理位置以及经纬度
     **/
    private String userAddress;
    private LocationService locationService;
    private LatLng mLatLng;

    private static String WIFI_SCAN;
    /**
     * 上下班打卡方式
     **/
    private int COUNT = 0;
    /**
     * 外勤，公司打卡方式
     **/
    private int SIGN_TYPE;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UODATE_SIGN:
                    updateWorkOutUI(1);
                    break;
                case MSG_JUMP:
                    timerTV.setText(MyStringUtils.getNowDate());
                    break;
                case UPDATE_WORKOUT_UI:
                    updateWorkOutUI(0);
                    break;
                case UPDATE_UI:
                    updateWorkUI();
                    break;
            }
        }
    };

    /**
     * 获取share中的临近信息
     **/
    private int getGpsStatus() {
        return DataHelper.getSharedPreferences(this).getInt(GPS_STATUS, 0);
    }

    private void updateWorkUI() {

        signTV.setText("下班打卡");
        signTimeTV.setText(getString(R.string.sign_time, MyStringUtils.getNowDateWithoutSD()));
        if (SIGN_TYPE == 0) {
            wifiNameTV.setText(getWifiName());
            if (MyStringUtils.compareTime(WORK_MORNING, WORK_MORNING_TYPE)) {
                signStatusTV.setText("正常");
            } else {
                signStatusTV.setText("迟到");
            }
        }
        if (SIGN_TYPE == 1) {

            signStatusTV.setText("外勤");
        }
        showSignWork(signWorkRL);
    }

    private void updateWorkOutUI(int TYPE) {

        workoutTimeTV.setText(getString(R.string.sign_time, MyStringUtils.getNowDateWithoutSD()));
        if (SIGN_TYPE == 0) {
            wifiWorkOutTV.setText(getWifiName());
            if (MyStringUtils.compareTime(WORK_EVENING, WORK_EVENING_TYPE)) {
                statusWorkOutTV.setText("正常");
            } else {
                statusWorkOutTV.setText("早退");
            }
        }
        if (SIGN_TYPE == 1) {
            wifiWorkOutTV.setText(userAddress);
            statusWorkOutTV.setText("外勤");
        }

        if (TYPE == 0) {
            hideBottomRL(signBTRL);
        }
        showSignWork(signWorkOutRL);
    }

    private String getWifiName() {
        return TextUtils.isEmpty(WifiUtils.getWifiSSID(SignActivity.this)) ? WIFI_SCAN : WifiUtils.getWifiSSID(SignActivity.this);
    }

    private void showSignWork(View view) {
        view.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.activity_sign_animation);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);

    }

    /**
     * 隐藏打卡布局
     **/
    private void hideBottomRL(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_sign_rl);
        view.startAnimation(animation);
        view.setVisibility(View.GONE);
    }

    /**
     * 定义timer，0s后开始执行，1s更新一次
     **/
    private void initTime() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = MSG_JUMP;
                mHandler.sendMessage(message);
            }
        }, 0, 1000);
    }

    /**
     * 进入验证wifi
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle("打卡");
        setButtonTime(dateBT);
        Logger.e(TAG, WifiUtils.getWifiSSID(this));
        checkNetStatus();
        }

    /**
     * wifi连接-获取wifi名称
     **/
    private void checkNetStatus() {
        if (isCheck()) {
            setWorkInCompanySign();
        } else {
            setWorkOuterSign();
        }

    }

    /**
     * 在wifi范围以及打开wifi匹配上
     **/
    private boolean isCheck() {
        if (regWifi() || isWifiDomain()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * wifi连接设置textview为上下班打卡的方式
     **/
    private void setWorkInCompanySign() {
        SIGN_TYPE = 0;
        signTV.setText("上班打卡");
        if (!WifiUtils.isWifiConnected(this)) {
            wifiTV.setText(getString(R.string.sign_wifi_name, WIFI_SCAN));
        } else
            wifiTV.setText(getString(R.string.sign_wifi_name, WifiUtils.getWifiSSID(this)));
    }

    private void setWorkOuterSign() {
        //外勤时改变type值
        SIGN_TYPE = 1;
        signTV.setText("外勤打卡");
        wifiTV.setText("当前不在考勤WI-FI范围内");
    }

    /**
     * 匹配wifi
     **/
    private boolean regWifi() {
        if ((RegUtils.WIFI_NAME_PATTERN.matcher(WifiUtils.getWifiSSID(this))).matches()) {
            return true;
        }
        return false;
    }

    /**
     * wifi临近范围
     **/
    private boolean isWifiDomain() {
        List<ScanResult> wifiList = WifiUtils.getWifiList(this);
        for (ScanResult list : wifiList) {
            if ((RegUtils.WIFI_NAME_PATTERN.matcher(list.SSID)).matches()) {
                WIFI_SCAN = list.SSID;
                Logger.e("wifiList", list.SSID);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    /**
     * 启动定位
     **/
    @Override
    protected void onStart() {
        super.onStart();
        //更新时间显示
        initTime();
        //获取服务实例
        locationService = XinlaiApplication.getApp().locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        /**"from", 0为传入activity的key-value**/
        //注册监听
        //int type = getIntent().getIntExtra("from", 0);
        int type = 0;
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }

    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                /**location不为空时保存经纬度,以及地址信息,LatLng为经纬度对象**/
                new LatLngWrapper(location.getLatitude(), location.getLongitude());
                //new LatLngWrapper(mLatLng);
                userAddress = location.getAddrStr();
                StringBuffer sb = new StringBuffer();
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nDirection(not all devices have value): ");
                /**poi搜索经纬度附近的信息**/
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果

                } else if (location.getLocType() == BDLocation.TypeServerError) {

                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                }
                logMsg(location.getAddrStr());
            }
        }

    };

    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (wifiNameTV != null)
                wifiNameTV.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //更新打卡
            case R.id.update_sign_tv:
                Message myMessage = new Message();
                myMessage.what = UODATE_SIGN;
                mHandler.sendMessage(myMessage);
                break;
            case R.id.start_time_bt:
                datePick(dateBT);
                break;
            //点击打卡-连接wifi—未连接wifi
            case R.id.sign_rl:

                if (WifiUtils.isNetConnected(SignActivity.this)) {
                    if (!isCheck()) {

                        locationService.start();// 定位SDK
                    }
                    Message message = new Message();
                    if (COUNT == 1) {
                        message.what = UPDATE_WORKOUT_UI;
                    }
                    if (COUNT == 0) {
                        message.what = UPDATE_UI;
                    }
                    mHandler.sendMessage(message);
                    //showToast("在wifi范围，打卡成功，不获取定位");


                } else showToast("网络未开启");
                COUNT = 1;
                break;

        }
    }

    /**
     * 进入设置时间
     **/
    private void setButtonTime(Button button) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        button.setText(new StringBuilder()
                .append(year)
                .append("-")
                .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
                        : (monthOfYear + 1)).append("-")
                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));

    }

    /**
     * 时间选择器
     **/
    private void datePick(final Button button) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                button.setText(new StringBuilder()
                        .append(year)
                        .append("-")
                        .append((month + 1) < 10 ? "0" + (month + 1)
                                : (month + 1)).append("-")
                        .append((day < 10) ? "0" + day : day));
            }
        }, year, monthOfYear, dayOfMonth).show();
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        mLatLng = mapStatus.target;
        Logger.e("Mapview", mLatLng.toString());
    }

    /**
     * 读写权限
     **/

    @TargetApi(23)
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissions = new ArrayList<String>();
        }
    }
}
