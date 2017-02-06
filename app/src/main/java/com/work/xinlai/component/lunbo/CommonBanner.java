package com.work.xinlai.component.lunbo;

import android.content.Context;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.work.xinlai.R;
import com.work.xinlai.bean.CommanBannerClass;

import java.util.ArrayList;
import java.util.List;


public class CommonBanner extends ViewSwitcher implements OnClickListener {
    public int BANNER_TYPE;
    private MyViewAdapter myViewAdapter;
    private LayoutInflater mLayoutInflater;
    //此处定义数据
    private List<CommanBannerClass> list;

    public CommonBanner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CommonBanner);
        BANNER_TYPE = typedArray.getInteger(R.styleable.CommonBanner_banner_plate, -1);
        typedArray.recycle();
        mLayoutInflater = LayoutInflater.from(context);
        initData();
        //实例化抽象的viewswitcheradapter
        myViewAdapter = new MyViewAdapter();
        this.setAdapter(myViewAdapter);

    }

    /**
     * 点击跳转在此完成
     **/
    private void initData() {
        list = new ArrayList<>();
        list.add(new CommanBannerClass(R.drawable.splash_an));
        list.add(new CommanBannerClass(R.drawable.pic_prof_scene));
        list.add(new CommanBannerClass(R.drawable.splash_an));
        list.add(new CommanBannerClass(R.drawable.pic_prof_scene));
    }

    private class MyViewAdapter extends ViewSwitcherAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_switcher_image, parent, false);
            }

            ImageView image = (ImageView) convertView;
            image.setImageResource(list.get(position).imageID);
            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                
                break;
        }
    }
}
