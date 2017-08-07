package com.work.xinlai.baidu;

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
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.bean.LatLngWrapper;
import com.work.xinlai.component.BaseActivity;

/**
 * Created by Administrator on 2016/11/30.使用基础地图
 */
public class MapActivity extends BaseActivity implements LocationHelper.LocationObserver,
        BaiduMap.OnMapStatusChangeListener {
    @ViewInject(R.id.location_mv)
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    public static final String EXTRA_PICK_LOCATION = "pick_location";
    public static final String EXTRA_LOCATION = "location";
    public static final String LOCATION_ADDRESS = "address";
    //地图模式
    private static final int MAP_ZOOM = 16;
    private static final int MARKER_Z_INDEX = 9;

    private LatLng mLatLng;
    private LocationHelper mLocationHelper;
    private boolean mPickLocation;
    private String mLocalAddr;
    private String mLocalAddrOther;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        showTitle("地图");
        //实例化baidumap

        mBaiduMap = mMapView.getMap();
        //设置地图模式-普通地图模式
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(MAP_ZOOM));
        /*// 开启室内图
        mBaiduMap.setIndoorEnable(true);*/
        //如果进入页面之前经度不为空改变标题并且取得精度
       /* LatLngWrapper wrapper = getIntent().getParcelableExtra(EXTRA_LOCATION);
        mPickLocation = getIntent().getBooleanExtra(EXTRA_PICK_LOCATION, true);*/
        /**设置世豪广场的死坐标**/
        LatLngWrapper w = new LatLngWrapper(30.555354, 104.05185);
        if (w != null) {
            mLatLng = w.latLng;
            showTitle("查看地图");
        }
        updateMapCenter();

        if (mPickLocation) {
            //findViewById(R.id.iv_marker).setVisibility(View.VISIBLE);
            mBaiduMap.setOnMapStatusChangeListener(this);
        } else if (mLatLng != null) {
            mBaiduMap.addOverlay(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_ic_big_place))
                    .position(mLatLng)
                    .zIndex(MARKER_Z_INDEX));
        }
        enableMyLocation();
        updateMyLocation();
        //RadarNearbyResult
    }

    private void show(){

    }
    private void updateMapCenter() {
        if (mLatLng != null) {
            mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng));
        }
    }

    private void enableMyLocation() {
        BaiduMap map = mMapView.getMap();
        map.setMyLocationEnabled(true);
        map.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, false, null));
        mLocationHelper = LocationHelper.getInstance(this);
        mLocationHelper.addObserver(this);
        /*if (mSettings.allowGpsHint() && !mLocationHelper.isGpsEnabled()) {
            Utils.showGpsHintDialog(this);
        }*/
    }

    private void updateMyLocation() {
        BDLocation location = mLocationHelper.getCurrentLoc();
        mLocalAddr = mLocationHelper.getCurrentLocAddress();
        if (location != null) {
            if (mPickLocation) {
                //getTitleBar().showRightText(R.string.you_sure, this);
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
     * show local address
     */
    private void showAddrTextOptions(LatLng latLng) {
        mMapView.getMap().clear();
        View view = getLayoutInflater().inflate(R.layout.item_mark_location, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_marker_txt);
        //textView.setText("adress");
        textView.setText(mLocalAddrOther);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
        InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47, null);
        mMapView.getMap().showInfoWindow(infoWindow);
    }

    /**
     * 将对应的地址经纬度转码成物理位置，或是将物理位置转换为经纬度
     **/
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

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        mLocationHelper.removeUpdates(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationHelper.removeObserver(this);
    }

    /**观察者更新经纬度**/
    @Override
    public void onReceiveLocation() {
        updateMyLocation();
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

    }
}
