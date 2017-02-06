package com.work.xinlai.http.api;

import com.android.volley.Request;
import com.work.xinlai.http.AppHttpBase;
import com.work.xinlai.http.GsonRequest;
import com.work.xinlai.http.HttpUtils;
import com.work.xinlai.http.ResponseCallback;
import com.work.xinlai.work.testhttp.Student;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/30.
 */
public class Test extends AppHttpBase{

    public static void getPurchaseDetail(
            String action,
            String orderId,
            ResponseCallback<Student> callback) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("action", action);
        params.put("orderId", orderId);

        GsonRequest<Student> request = new GsonRequest<>(Request.Method.GET,
                APIBASE+ "/pr_orderEdit.action", Student.class, null, params, callback, callback);
        HttpUtils.getInstance().request("getPurchaseDetail", request);
    }
}
