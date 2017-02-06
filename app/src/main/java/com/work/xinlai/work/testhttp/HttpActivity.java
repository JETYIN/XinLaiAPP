package com.work.xinlai.work.testhttp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lidroid.xutils.ViewUtils;
import com.work.xinlai.R;
import com.work.xinlai.http.message.ErrorBase;
import com.work.xinlai.util.Logger;
import com.work.xinlai.component.BaseActivity;
import com.work.xinlai.http.ResponseCallback;

/**
 * Created by Administrator on 2016/12/8.
 */
public class HttpActivity extends BaseActivity  {

    private MyAdapter adapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_httpactivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        getHttpData();
        /**ptr可以转化为listview进而设置首页**/
      /*  ListView lvList = ptrList.getRefreshableView();
        mHeaderView = inflater.inflate(R.layout.campus_head_view, lvList, false);
        lvList.addHeaderView(mHeaderView, null, false);*/
        /**下拉刷新的设置模式（设置上下拉均可刷新），设置监听，ptr的首页可以单独设置**/
       /* ptrList.setMode(PullToRefreshBase.Mode.BOTH);
        ptrList.setOnRefreshListener(this);
        ptrList.setAdapter(adapter);
*/
    }



    private void getHttpData() {
        /**网络请求回调**/
        ApiRequest.queryList(new ResponseCallback<Student>() {
            /**抽象类成功回调**/
            @Override
            public void onRequestSuccess(Student result) {
                /**Gson翻译后的对象**/
                Logger.e("http_sucess", result.title);
            }

            @Override
            public void onReuquestFailed(ErrorBase error) {
                showToast(error.message);
                Logger.e("http_failed", "shibai");
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
