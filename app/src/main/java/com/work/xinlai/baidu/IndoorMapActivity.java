package com.work.xinlai.baidu;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.component.BaseActivity;

/**
 * Created by Administrator on 2016/11/30.室内地图，定位精度更高
 */
public class IndoorMapActivity extends BaseActivity {
    @ViewInject(R.id.location_mv)
    private MapView mMapView;

    public  MyLocationListenner myListener;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private LocationClientOption mLocationCp;
    private LocationMode mCurrentMode;
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle("室内定位");
        //地图实例化
        mBaiduMap = mMapView.getMap();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开启室内图
        mBaiduMap.setIndoorEnable(true);
        //定位初始化
        initMap();
    }

    private void initMap() {
        myListener = new MyLocationListenner();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);

        mLocationCp = new LocationClientOption();
        mLocationCp.setOpenGps(true); // 打开gps
        mLocationCp.setCoorType("bd09ll"); // 设置坐标类型
        mLocationCp.setScanSpan(3000);
        mLocationCp.setIsNeedAddress(true);
        mLocationClient.setLocOption(mLocationCp);
        mLocationClient.start();
    }

    public class MyLocationListenner implements BDLocationListener {

        private String lastFloor = null;

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            String bid = location.getBuildingID();
            if (bid != null && mMapBaseIndoorMapInfo != null) {

                if (bid.equals(mMapBaseIndoorMapInfo.getID())) {// 校验是否满足室内定位模式开启条件

                    String floor = location.getFloor().toUpperCase();// 楼层


                    boolean needUpdateFloor = true;
                    if (lastFloor == null) {
                        lastFloor = floor;
                    } else {
                        if (lastFloor.equals(floor)) {
                            needUpdateFloor = false;
                        } else {
                            lastFloor = floor;
                        }
                    }
                    if (needUpdateFloor) {// 切换楼层
                        mBaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());

                    }


                }
            }

            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
