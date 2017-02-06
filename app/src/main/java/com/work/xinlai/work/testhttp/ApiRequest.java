package com.work.xinlai.work.testhttp;

import com.android.volley.Request;
import com.work.xinlai.http.AppHttpBase;
import com.work.xinlai.http.GsonRequest;
import com.work.xinlai.http.HttpUtils;
import com.work.xinlai.http.ResponseCallback;


/**
 * Created by Administrator on 2016/12/8.
 */
public class ApiRequest extends AppHttpBase {
    /**
     * 请求方式，
     * url地址，
     * json解析对象，
     * 请求头信息，
     * 请求参数信息，
     * 请求成功回调，
     * 请求失败
     **/
    public static void queryList(ResponseCallback<Student> responseCallback) {


        /**实力化请求**/
        GsonRequest<Student> gsonRequest = new GsonRequest(Request.Method.GET,
                APIBASE + "waterCloud/agriculture/widget3.do",
                Student.class, null, null, responseCallback, responseCallback);
        /**将请求加入队列**/
        HttpUtils.getInstance().request("queryList", gsonRequest);
    }
}
