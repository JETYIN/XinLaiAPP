package com.work.xinlai.component;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.work.xinlai.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */
public class ListDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ListView mListView;
    private MyDialogAdapter adapter;
    private List<String> dataList;
    private LayoutInflater mInflater;
    private ListDialogClick listDialogClick;

    public ListDialog(Context context) {
        this(context, R.style.Translucent_NoTitle);
    }

    public ListDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.compant_listdialog_item);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        /**设置点击外边缘dialog是否可以隐藏**/
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initDataList();
        adapter = new MyDialogAdapter();
        mListView = (ListView) findViewById(R.id.listdialog_lv);
        mListView.setAdapter(adapter);
    }

    /**
     * 获取资源文件中的数组
     **/
    private void initDataList() {
        dataList = new ArrayList<>();
        /**资源文件数组**/
        String[] array = mContext.getResources().getStringArray(R.array.choose_ask_leave);
        for (String list : array) {
            dataList.add(list);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_rl:
                String target = (String) v.getTag();
                /**点击时触发接口**/
                listDialogClick.OnListDialogItem(target);
                break;
        }
    }

    private class MyDialogAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public String getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String str = getItem(position);
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.item_listdialog, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.listdialog_tv);
            textView.setText(str);
            convertView.setTag(str);
            convertView.setOnClickListener(ListDialog.this);
            return convertView;
        }
    }

    public void rigisterListDialogClick(ListDialogClick listener) {
        listDialogClick = listener;
    }

    public void unrigisterListDialogClick() {
        listDialogClick = null;
    }

    /**
     * 回调接口
     **/
    public interface ListDialogClick {
        void OnListDialogItem(String str);
    }

}
