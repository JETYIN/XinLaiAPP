package com.work.xinlai.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.baidu.mapapi.model.LatLng;
import com.work.xinlai.R;
import com.work.xinlai.util.Logger;


/**
 * Created by Administrator on 2016/12/2.
 */
public class StaticMapView extends FrameLayout {
    /**
     * 百度的官方api，输入对应的经纬度可以进入百度地图（web端）
     **/
    private static final String MAP_URL_FORMAT = "http://api.map.baidu.com/staticimage?" +
            "center=%1$f,%2$f&width=%3$d&height=%4$d&zoom=16";
    private LatLng mLocation;

    public StaticMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**inflate定位图标**/
        LayoutInflater.from(context).inflate(R.layout.merge_staticmap, this, true);
    }

    /**
     * 外部调用方法
     **/
    public void showMap(double longitude, double latitude) {
        showMap(new LatLng(latitude, longitude));
    }

    public void showMap(LatLng location) {
        //setVisibility(VISIBLE);
        mLocation = location;
        Logger.e("location", String.valueOf(location.latitude));
        if (getWidth() > 0 && getHeight() > 0) {
            showImage(getWidth(), getHeight());
        }
    }

    public void setMarkerResource(int resource) {
        ((ImageView) findViewById(R.id.iv_smv_marker)).setImageResource(resource);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed && mLocation != null) {
            showImage(right - left, bottom - top);
        }
    }

    /**
     * 根据传入的经纬度将url地址format，
     **/
    private void showImage(int width, int height) {
        MlWebView piv = (MlWebView) findViewById(R.id.piv_map);
        String url = String.format(MAP_URL_FORMAT,
                mLocation.longitude, mLocation.latitude, width, height);
        /**根据url地址将网页端地图加载在webview中**/
        Logger.e("showImage", url);
        piv.loadUrl(url);
    }
}
