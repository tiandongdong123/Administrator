package com.wanfangdata.model;


/**
 * 返回数据实体类
 */
public class TResult<T> {
    public TResult() {

    }

    public TResult(int code) {
        this.code = code;
    }

    public TResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 状态码
     */
    private int code = 0;
    /**
     * 返回数据
     */
    private T data = null;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TResult{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
