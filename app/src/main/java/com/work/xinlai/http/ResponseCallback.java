package com.work.xinlai.http;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.work.xinlai.R;
import com.work.xinlai.common.Constant;
import com.work.xinlai.http.message.ErrorBase;
import com.work.xinlai.http.message.ErrorHelper;
import com.work.xinlai.util.Logger;
import com.work.xinlai.launcher.XinlaiApplication;

/**
 * 此处重写Response的ErrorListener接口，和Listener接口下的方法
 **/
public abstract class ResponseCallback<E> implements Response.ErrorListener, Response.Listener<E> {
    private static final String TAG = ResponseCallback.class.getSimpleName();

    public ResponseCallback() {
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        onReuquestFailed(new ErrorBase("-1", ErrorHelper.getMessage(volleyError)));
        if (Constant.IS_DEBUG_MODE) {
            Logger.d(TAG + " ERROR", ErrorHelper.getMessage(volleyError));
        }
    }

    /**
     * Response  Listener接口下的方法,具体看json怎么布置
     **/
    @Override
    public void onResponse(E result) {
        /**result为翻译成student的对象,而student继承自ResponseBase,ResponseBase中封装请求码与返回信息**/
        if (result != null) {
            onRequestSuccess(result);
            Logger.e("result", String.valueOf(result));
            //com.work.xinlai.work.testhttp.Student@3ed2d30
        } else {
            onReuquestFailed(new ErrorBase("-1", XinlaiApplication.getAppContext().getResources().getString(R.string.network_none_return)));
        }
       /* if (result == null) {
            onReuquestFailed(new ErrorBase("-1", XinlaiApplication.getAppContext().getResources().getString(R.string.network_none_return)));

            return;
        }
        if (result instanceof ResponseBase) {
            ResponseBase responseBase = (ResponseBase) result;
            if ("SUCCESS".equals(responseBase.status)) {
                onRequestSuccess(result);
            } else {
                *//**错误状态码和错误信息**//*
                onReuquestFailed(new ErrorBase(responseBase.status, responseBase.msg));
                if (Constant.IS_DEBUG_MODE) {
                    Logger.d(TAG + " ERROR", responseBase.msg == null ? "NULL" :
                            responseBase.msg);
                }
            }
        } else {
            onRequestSuccess(result);
        }*/
    }

    public abstract void onRequestSuccess(E result);

    public abstract void onReuquestFailed(ErrorBase error);
}
