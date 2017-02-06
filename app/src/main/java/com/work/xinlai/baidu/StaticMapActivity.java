package com.work.xinlai.baidu;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.xinlai.R;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.component.StaticMapView;

/**
 * Created by Administrator on 2016/12/2.
 */
public class StaticMapActivity extends BaseActivity {
    private double Longidute = 104.05185;
    private double latidute = 30.555354;
    private StaticMapView staticMapView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.item_staticmapview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示地址
        TextView tv = (TextView) findViewById(R.id.location_adr_tv);
        tv.setText("成都市剑南大道世豪广场");
        //根据经纬度获取百度地图截图
        staticMapView = (StaticMapView) findViewById(R.id.mapvew_location_smv);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.baidumap_location_ll);
        staticMapView.setMarkerResource(R.drawable.map_ic_small_place);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MapActivity.class);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        staticMapView.showMap(Longidute, latidute);
    }

}
