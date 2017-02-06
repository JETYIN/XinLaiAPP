package com.work.xinlai.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.work.xinlai.R;
import com.work.xinlai.component.SlideDeleteListView;
import com.work.xinlai.component.TabFragment;
import com.work.xinlai.data.phone.PhoneInfo;
import com.work.xinlai.data.phone.PhoneManger;
import com.work.xinlai.work.testhttp.HttpActivity;

import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class MyFragment extends TabFragment implements SlideDeleteListView.RemoveListener,View.OnClickListener {
    @ViewInject(R.id.list_wifi)
    private SlideDeleteListView listView;

    private List<PhoneInfo> list;
    private MyAdapter adapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_wifi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ViewUtils.inject(this, view);
        listView.setAdapter(adapter);
        //注册删除监听
        listView.setRemoveListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //list = PhoneManger.getPhoneNum(getActivity(), Phone.CONTENT_URI);
        //Uri.parse("content://icc/adn") sim卡
        list = PhoneManger.getPhoneNum(getActivity(), Phone.CONTENT_URI);
        adapter = new MyAdapter();
    }

    @Override
    public void removeItem(SlideDeleteListView.RemoveDirection direction, int position) {
        list.remove(adapter.getItem(position));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        startActivity(HttpActivity.class);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public PhoneInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhoneInfo phone = getItem(position);
            ViewHoder viewHoder;
            if (null == convertView) {
                viewHoder = new ViewHoder();
                //创建view的实例
                convertView = mInflater.inflate(R.layout.item_fragment_wifi, parent, false);
                //获取到hodler以及view实例进行注解
                ViewUtils.inject(viewHoder, convertView);
                //为viewholder设置对应的tag
                convertView.setTag(viewHoder);
            } else {
                //convertview不为空获取对应的hodler
                viewHoder = (ViewHoder) convertView.getTag();
            }
            viewHoder.nameTV.setText(phone.name);
            viewHoder.phoneTV.setText(phone.phone);
            convertView.setOnClickListener(MyFragment.this);
            return convertView;
        }
    }

    private class ViewHoder {
        @ViewInject(R.id.tv_wifi)
        TextView nameTV;
        @ViewInject(R.id.text)
        TextView phoneTV;
        PhoneInfo phoneInfo;
    }
}
