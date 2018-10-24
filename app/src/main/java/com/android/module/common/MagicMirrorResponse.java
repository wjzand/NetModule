package com.android.module.common;

import com.google.gson.annotations.SerializedName;


/**
 * 通用HTTP接口返回格式
 *
 * @author guohelong on 2018/1/2.
 */

public final class MagicMirrorResponse<T> {
    @SerializedName("success")
    private boolean success;

    @SerializedName("codeNum")
    private String codeNum;

    @SerializedName("codeDesc")
    private String codeDesc;

    @SerializedName("value")
    private T value;

    public boolean isSuccess() {
        return success;
    }

    public String getCodeNum() {
        return codeNum;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MagicMirrorResponse{" +
                "success=" + success +
                ", codeNum='" + codeNum + '\'' +
                ", codeDesc='" + codeDesc + '\'' +
                ", value=" + value +
                '}';
    }
}
