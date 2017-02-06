package com.work.xinlai.http.message;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.work.xinlai.R;
import com.work.xinlai.launcher.XinlaiApplication;

/**
 * Created by Administrator on 2016/11/23.
 */
public class ErrorHelper {
    public static String getMessage(VolleyError error) {
        if (error instanceof TimeoutError) {
            return XinlaiApplication.getAppContext().getResources().getString(R.string.network_error_timeout);
        } else if (isNetworkProblem(error)) {
            return XinlaiApplication.getAppContext().getResources().getString(R.string.network_error_net_problem);
        } else if (isServerProblem(error)) {
            String message = "";
            Gson gson = new Gson();
            try {
                ResponseBase baseError = gson.fromJson(new String(error.networkResponse.data,
                        "UTF-8"), ResponseBase.class);
                message = baseError.msg;
            } catch (Exception e) {
                message =XinlaiApplication.getAppContext().getResources().getString(R.string.network_error_generic);
                e.printStackTrace();
            } finally {
                return message;
            }
        } else if (error instanceof ParseError) {
            return XinlaiApplication.getAppContext().getResources().getString(R.string.network_error_parse);
        } else {
            return XinlaiApplication.getAppContext().getResources().getString(R.string.network_error_generic);
        }
    }

    private static boolean isNetworkProblem(VolleyError error) {
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }

    private static boolean isServerProblem(VolleyError error) {
        return (error instanceof AuthFailureError || error instanceof ServerError);
    }
}
