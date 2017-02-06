package com.work.xinlai.component;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TextView;

import com.work.xinlai.R;
import com.work.xinlai.http.HttpUtils;

import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/11/24.
 */
public abstract class TabFragment extends Fragment {
    protected TextView titleTV;
    protected ImageButton backIB;
    protected LayoutInflater mInflater;

    protected String VOLLEY;

    protected abstract int getLayoutID();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        VOLLEY = getActivity().getClass().getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**加载布局**/
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        titleTV = (TextView) view.findViewById(R.id.tv_title);
        backIB = (ImageButton) view.findViewById(R.id.btn_left);
        /**加载viewstub后添加资源文件**/
        ViewStub vsContent = (ViewStub) view.findViewById(R.id.vs_content);
        vsContent.setLayoutResource(getLayoutID());
        vsContent.inflate();
        hideLeftBt();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        HttpUtils.cancelAll(VOLLEY);
    }

    /**
     * 设置主标题
     **/
    protected void showTitle(CharSequence charSequence) {
        titleTV.setText(charSequence);
    }

    /**
     * 左边返回图标隐藏
     **/
    protected void hideLeftBt() {
        backIB.setVisibility(View.GONE);
    }

    /**
     * fragment-activity的跳转动画
     **/
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_slide_right_in,
                R.anim.activity_slide_left_out);
    }

    /**
     * 引用动画
     **/
    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(getActivity(), cls));
    }
}
