package com.work.xinlai.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.work.xinlai.common.Constant;
import com.work.xinlai.util.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * 网络JSON数据请求基类，并根据传入的泛型，解析为对象
 * Created by Administrator on 2015/7/8.
 */
public class GsonRequest<T> extends Request<T> {
    private static final String TAG = GsonRequest.class.getSimpleName();

    private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final int SOCKET_TIMEOUT = 20 * 1000;

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Map<String, String> parameters;
    private final String body;
    private final Response.Listener<T> listener;
    private final String contentType;

    private GsonRequest(int method,
                        String contentType,
                        String url,
                        Class<T> clazz,
                        Map<String, String> headers,
                        Map<String, String> parameters,
                        String body,
                        Response.Listener<T> listener,/**成功请求回调**/
                        Response.ErrorListener errorListener/**请求失败回调**/) {
        super(method, url, errorListener);
        FakeX509TrustManager.allowAllSSL();
        this.contentType = contentType;
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.parameters = parameters;
        this.body = body;
        setSocketTimeout(SOCKET_TIMEOUT);
        if (Constant.IS_DEBUG_MODE) {
            Logger.d(TAG + " URL", url);
        }
    }

    /**
     * @param method        请求方式
     * @param url           URL
     * @param clazz         返回体接收类
     * @param headers       请求头信息
     * @param parameters    请求参数信息
     * @param listener      成功回调
     * @param errorListener 失败回调
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> headers,
                       Map<String, String> parameters,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        this(method, CONTENT_TYPE_FORM, url, clazz, headers, parameters, null, listener,
                errorListener);
    }

    /**
     * @param method        请求方式
     * @param url           URL
     * @param clazz         返回体接收类
     * @param headers       请求头信息
     * @param body          请求体信息
     * @param listener      成功回调
     * @param errorListener 失败回调
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> headers,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener,
                       String body) {
        this(method, CONTENT_TYPE_JSON, url, clazz, headers, null, body, listener, errorListener);
    }

    /**
     * 设置请求延迟
     *
     * @param timeOut
     */
    public void setSocketTimeout(int timeOut) {
        setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (Constant.IS_DEBUG_MODE) {
            Logger.d(TAG + " HEADERS", headers != null ? headers.toString() : "NULL");
        }
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (Constant.IS_DEBUG_MODE) {
            Logger.d(TAG + " PARAMS", parameters != null ? parameters.toString() : "NULL");
        }
        return parameters != null ? parameters : super.getParams();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (Constant.IS_DEBUG_MODE) {
            Logger.d(TAG + " BODY", body != null ? body : "NULL");
        }
        return body != null ? body.getBytes() : super.getBody();
    }

    @Override
    public String getBodyContentType() {
        return contentType;
    }

    /**分发调用Response**/
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            /**获取json串**/
            String json = new String(response.data, "UTF-8");
            if (Constant.IS_DEBUG_MODE) {
                Logger.d(TAG + " RESPONSE", json);
            }
            /**将json串翻译成对应的class,返回对应的对象-Student**/
            return Response.success(gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
