package com.work.xinlai.http.message;

/**
 * Created by Administrator on 2016/11/23.
 */
public class ErrorBase {
    public ErrorBase(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code;
    public String message;
}
