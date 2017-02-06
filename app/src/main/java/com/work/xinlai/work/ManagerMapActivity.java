package com.work.xinlai.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.bean.LatLngWrapper;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.util.GpsUtils;
import com.work.xinlai.baidu.LocationHelper;

/**
 * Created by Administrator on 2016/11/28.管理员进入查看打卡位置界面
 */
public class ManagerMapActivity extends BaseActivity implements LocationHelper.LocationObserver, View.OnClickListener,
        BaiduMap.OnMapStatusChangeListener {
    /**
     * map
     **/
    @ViewInject(R.id.mv_map)
    private MapView mMapView;
    private BaiduMap mBaiduiMap;
    private final static int MAP_ZOOM = 16;
    private static final int MARKER_Z_INDEX = 9;
    private LatLng mLatLng;
    public static final String EXTRA_LOCATION = "location";
    public static final String EXTRA_PICK_LOCATION = "pick_location";
    public static final String LOCATION_ADDRESS = "address";
    private boolean mPickLocation;
    private LocationHelper mLocationHelper;
    private String mLocalAddr;
    private String mLocalAddrOther;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_managermap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle("地图");
        /**获取到baidumap并且设置模式**/
        mBaiduiMap = mMapView.getMap();
        mBaiduiMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduiMap.setMapStatus(MapStatusUpdateFactory.zoomTo(MAP_ZOOM));
        LatLngWrapper wrapper = getIntent().getParcelableExtra(EXTRA_LOCATION);
        if (wrapper != null) {
            mLatLng = wrapper.latLng;
            showTitle("查看位置");
        }
        updateMapCenter();

        mPickLocation = getIntent().getBooleanExtra(EXTRA_PICK_LOCATION, true);
        if (mPickLocation) {
            findViewById(R.id.iv_marker).setVisibility(View.VISIBLE);
            mBaiduiMap.setOnMapStatusChangeListener(this);
        }
        /**根据保存的经度设置当前经度的定位图标**/
        if (mLatLng != null) {
            mBaiduiMap.addOverlay(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_ic_big_place))
                    .position(mLatLng)
                    .zIndex(MARKER_Z_INDEX));
        }
        enableMyLocation();
        updateMyLocation();
    }

    /**
     * 更新经度
     **/
    private void updateMapCenter() {
        if (mLatLng != null) {
            mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng));
        }
    }

    private void enableMyLocation() {
        // BaiduMap map = mMapView.getMap();
        mBaiduiMap.setMyLocationEnabled(true);
        mBaiduiMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, false, null));
        mLocationHelper = LocationHelper.getInstance(this);
        mLocationHelper.addObserver(this);
        if (!mLocationHelper.isGpsEnabled()) {
            GpsUtils.showGpsNotOpenDialog(this);
        }
    }

    private void updateMyLocation() {
        BDLocation location = mLocationHelper.getCurrentLoc();
        mLocalAddr = mLocationHelper.getCurrentLocAddress();
        if (location != null) {
            /**mPickLocation为true即是用户端**/
            if (mPickLocation) {
                /**用户端右边显示**/
                // getTitleBar().showRightText(R.string.you_sure, this);
            }
            mMapView.getMap().setMyLocationData(new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
            if (mLatLng == null) {
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                updateMapCenter();
            }
        }
    }

    /**
     * show local address定位地图上的文字位置信息
     */
    private void showAddrTextOptions(LatLng latLng) {
        mMapView.getMap().clear();
        View view = getLayoutInflater().inflate(R.layout.item_mapactivity_location, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_marker_txt);
        textView.setText(mLocalAddrOther);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
        InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47, null);
        mMapView.getMap().showInfoWindow(infoWindow);
    }

    private void reverseGeoCode(LatLng latLng) {
        // 创建地理编码检索实例
        GeoCoder geoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result != null) {
                    mLocalAddrOther = result.getAddress();
                    mLocalAddr = result.getAddress();
                    if (!mPickLocation) {
                        showAddrTextOptions(mLatLng);
                    }
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }

        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mLocationHelper.requestUpdates(true);
        if (!mPickLocation && mLatLng != null) {
            reverseGeoCode(mLatLng);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationHelper.removeObserver(this);
    }

    @Override
    public void onReceiveLocation() {
        updateMyLocation();

    }

    /**
     * 点击右边标题栏保存当前的经纬度，并且将值传回上一页面
     **/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_right) {
            Intent data = new Intent();
            data.putExtra(EXTRA_LOCATION, new LatLngWrapper(mLatLng));
            data.putExtra(LOCATION_ADDRESS, mLocalAddr);
            setResult(RESULT_OK, data);
            finish();
        }
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
        reverseGeoCode(mLatLng);
    }
}
